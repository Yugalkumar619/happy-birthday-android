package com.app.happy_birthday.mvvm.collage.view_model

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.happy_birthday.helper.collage.CollageImageLoader
import com.app.happy_birthday.helper.collage.CollageRenderer
import com.app.happy_birthday.helper.collage.CollageStorage
import com.app.happy_birthday.helper.collage.PlacedPhoto
import com.app.happy_birthday.helper.helper_model.BirthdayPerson
import com.app.happy_birthday.mvvm.collage.model.CollageCrop
import com.app.happy_birthday.mvvm.collage.model.CollageLayout
import com.app.happy_birthday.mvvm.collage.model.CollageRect
import com.app.happy_birthday.mvvm.collage.model.CollageSlot
import com.app.happy_birthday.mvvm.collage.model.CollageTemplate
import com.app.happy_birthday.mvvm.collage.model.CollageTemplates
import com.app.happy_birthday.mvvm.collage.model.PhotoAdjustment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

sealed interface CollageSaveState {
    data object Editing : CollageSaveState
    data object Saving : CollageSaveState
    data class ReadyForAd(val uri: Uri) : CollageSaveState
    data object ShowingAd : CollageSaveState
    data class Saved(val uri: Uri) : CollageSaveState
    data object Failed : CollageSaveState
}

class CollageViewModel(application: Application) : AndroidViewModel(application) {

    private val _photos = MutableLiveData<List<Uri>>(emptyList())
    val photos: LiveData<List<Uri>> = _photos

    private val _template = MutableLiveData(CollageTemplates.all.first())
    val template: LiveData<CollageTemplate> = _template

    private val _wish = MutableLiveData<String>()
    val wish: LiveData<String> = _wish

    private val _preview = MutableLiveData<Bitmap>()
    val preview: LiveData<Bitmap> = _preview

    private val _saveState = MutableLiveData<CollageSaveState>(CollageSaveState.Editing)
    val saveState: LiveData<CollageSaveState> = _saveState

    private val _photosRemoved = MutableLiveData(false)
    val photosRemoved: LiveData<Boolean> = _photosRemoved

    var hasOpenedPicker = false
    var selectedPerson: BirthdayPerson? = null

    private var title = ""
    private var previewJob: Job? = null
    private var isLivePreview = false
    private var livePreviewPending = false

    private val adjustments = mutableMapOf<Uri, PhotoAdjustment>()
    private val photoSizes = mutableMapOf<Uri, Pair<Int, Int>>()
    private var adjustingPhoto: Uri? = null

    /** Sets text from the screen, so that it follows the screen's language. */
    fun initialize(title: String, defaultWish: String) {
        if (_wish.value != null) return
        this.title = title
        _wish.value = defaultWish
        requestPreview()
    }

    /** @return true if some photos were left out because of the limit */
    fun addPhotos(uris: List<Uri>): Boolean {
        val result = CollageLayout.mergePhotos(_photos.value.orEmpty(), uris)
        _photos.value = result.photos
        requestPreview()
        return result.wasLimited
    }

    fun removePhoto(uri: Uri) {
        _photos.value = _photos.value.orEmpty() - uri
        adjustments.remove(uri)
        photoSizes.remove(uri)
        requestPreview()
    }

    fun isPhotoLimitReached(): Boolean =
        _photos.value.orEmpty().size >= CollageLayout.MAX_PHOTOS

    fun hasUnsavedCollage(): Boolean =
        _photos.value.orEmpty().isNotEmpty() && _saveState.value !is CollageSaveState.Saved

    fun selectTemplate(template: CollageTemplate) {
        _template.value = template
        requestPreview()
    }

    fun selectWish(wish: String) {
        _wish.value = wish
        requestPreview()
    }

    fun setTitle(title: String) {
        if (this.title == title) return
        this.title = title
        requestPreview()
    }

    /**
     * Starts moving the photo under this point of the preview.
     *
     * @return false if there's no photo there
     */
    fun startAdjusting(previewX: Float, previewY: Float): Boolean {
        adjustingPhoto = photoAt(previewX, previewY)
        return adjustingPhoto != null
    }

    fun movePhoto(dragX: Float, dragY: Float) {
        adjustPhoto { adjustment, width, height, slot ->
            // A tilted photo moves along its own axes, so turn the drag to match.
            val angle = Math.toRadians(-slot.rotation.toDouble())
            val turnedX = (dragX * cos(angle) - dragY * sin(angle)).toFloat()
            val turnedY = (dragX * sin(angle) + dragY * cos(angle)).toFloat()
            CollageCrop.moved(adjustment, width, height, slot.rect, turnedX, turnedY)
        }
    }

    fun zoomPhoto(factor: Float) {
        adjustPhoto { adjustment, width, height, slot ->
            CollageCrop.zoomed(adjustment, width, height, slot.rect, factor)
        }
    }

    /** Puts the photo under this point of the preview back to its starting position. */
    fun resetPhotoAt(previewX: Float, previewY: Float) {
        val uri = photoAt(previewX, previewY) ?: return
        if (adjustments.remove(uri) != null) requestPreview()
    }

    fun save() {
        if (_saveState.value == CollageSaveState.Saving) return
        previewJob?.cancel()
        _saveState.value = CollageSaveState.Saving

        viewModelScope.launch {
            val collage = try {
                renderCollage(CollageRenderer.OUTPUT_WIDTH, CollageRenderer.OUTPUT_HEIGHT)
            } catch (e: OutOfMemoryError) {
                _saveState.value = CollageSaveState.Failed
                return@launch
            }

            if (collage.failedPhotos.isNotEmpty()) {
                // Let the user see the collage without those photos before saving it.
                removeFailedPhotos(collage.failedPhotos)
                _saveState.value = CollageSaveState.Editing
                return@launch
            }

            val uri = CollageStorage.save(getApplication(), collage.bitmap)
            _saveState.value =
                if (uri != null) CollageSaveState.ReadyForAd(uri) else CollageSaveState.Failed
        }
    }

    fun onAdStarted() {
        _saveState.value = CollageSaveState.ShowingAd
    }

    fun onAdFinished(uri: Uri) {
        _saveState.value = CollageSaveState.Saved(uri)
    }

    fun onFailureShown() {
        _saveState.value = CollageSaveState.Editing
        requestPreview()
    }

    fun onPhotosRemovedShown() {
        _photosRemoved.value = false
    }

    fun startNewCollage() {
        _photos.value = emptyList()
        adjustments.clear()
        photoSizes.clear()
        _saveState.value = CollageSaveState.Editing
        requestPreview()
    }

    private fun previewSlots(): List<CollageSlot> {
        val template = _template.value ?: CollageTemplates.all.first()
        return CollageRenderer.photoSlots(
            template,
            _photos.value.orEmpty().size,
            CollageRenderer.PREVIEW_WIDTH,
            CollageRenderer.PREVIEW_HEIGHT
        )
    }

    /** The top photo at this point of the preview. Photos drawn last are on top. */
    private fun photoAt(previewX: Float, previewY: Float): Uri? {
        if (_saveState.value != CollageSaveState.Editing) return null
        val uris = _photos.value.orEmpty()
        val slots = previewSlots()
        for (index in slots.indices.reversed()) {
            if (slots[index].contains(previewX, previewY)) return uris.getOrNull(index)
        }
        return null
    }

    private fun CollageSlot.contains(x: Float, y: Float): Boolean {
        val centerX = (rect.left + rect.right) / 2f
        val centerY = (rect.top + rect.bottom) / 2f
        val angle = Math.toRadians(-rotation.toDouble())
        val dx = x - centerX
        val dy = y - centerY
        val localX = (dx * cos(angle) - dy * sin(angle)).toFloat() + centerX
        val localY = (dx * sin(angle) + dy * cos(angle)).toFloat() + centerY
        return localX in rect.left..rect.right && localY in rect.top..rect.bottom
    }

    private fun adjustPhoto(
        change: (PhotoAdjustment, width: Int, height: Int, slot: CollageSlot) -> PhotoAdjustment
    ) {
        val uri = adjustingPhoto ?: return
        val (width, height) = photoSizes[uri] ?: return
        val slot = previewSlots().getOrNull(_photos.value.orEmpty().indexOf(uri)) ?: return
        adjustments[uri] = change(adjustments[uri] ?: PhotoAdjustment(), width, height, slot)
        requestLivePreview()
    }

    private fun requestPreview() {
        previewJob?.cancel()
        isLivePreview = false
        previewJob = viewModelScope.launch {
            // Changes often arrive together, so render only the last one.
            delay(PREVIEW_DELAY_MILLIS)
            renderPreview()
        }
    }

    /** Redraws as often as the device can while a photo is moving, without building a queue. */
    private fun requestLivePreview() {
        livePreviewPending = true
        if (isLivePreview && previewJob?.isActive == true) return

        previewJob?.cancel()
        isLivePreview = true
        previewJob = viewModelScope.launch {
            while (livePreviewPending) {
                livePreviewPending = false
                renderPreview()
            }
        }
    }

    private suspend fun renderPreview() {
        try {
            val collage = renderCollage(CollageRenderer.PREVIEW_WIDTH, CollageRenderer.PREVIEW_HEIGHT)
            _preview.value = collage.bitmap
            if (collage.failedPhotos.isNotEmpty()) removeFailedPhotos(collage.failedPhotos)
        } catch (e: OutOfMemoryError) {
            // Keep showing the previous preview.
        }
    }

    private fun removeFailedPhotos(failedPhotos: List<Uri>) {
        _photos.value = _photos.value.orEmpty() - failedPhotos.toSet()
        failedPhotos.forEach { uri ->
            adjustments.remove(uri)
            photoSizes.remove(uri)
        }
        _photosRemoved.value = true
        requestPreview()
    }

    private suspend fun renderCollage(width: Int, height: Int): RenderedCollage {
        val template = _template.value ?: CollageTemplates.all.first()
        val uris = _photos.value.orEmpty()
        val slots = CollageRenderer.photoSlots(template, uris.size, width, height)
        val context = getApplication<Application>()

        val loaded = coroutineScope {
            uris.zip(slots).map { (uri, slot) ->
                async { uri to loadPhoto(uri, slot.rect) }
            }.awaitAll()
        }
        val photos = loaded.mapNotNull { (uri, bitmap) ->
            bitmap?.let {
                photoSizes[uri] = it.width to it.height
                PlacedPhoto(it, adjustments[uri] ?: PhotoAdjustment())
            }
        }
        val failedPhotos = loaded.filter { it.second == null }.map { it.first }
        val currentTitle = title
        val wish = _wish.value.orEmpty()

        val bitmap = withContext(Dispatchers.Default) {
            CollageRenderer.render(context, template, photos, currentTitle, wish, width, height)
        }
        return RenderedCollage(bitmap, failedPhotos)
    }

    /** Loads more detail for a zoomed photo, in steps so that each zoom doesn't reload it. */
    private suspend fun loadPhoto(uri: Uri, slot: CollageRect): Bitmap? {
        val zoomStep = ceil(adjustments[uri]?.zoom ?: 1f)
        return CollageImageLoader.load(
            getApplication<Application>(),
            uri,
            min(slot.width * zoomStep, MAX_LOAD_SIZE).toInt(),
            min(slot.height * zoomStep, MAX_LOAD_SIZE).toInt()
        )
    }

    private class RenderedCollage(val bitmap: Bitmap, val failedPhotos: List<Uri>)

    private companion object {
        const val PREVIEW_DELAY_MILLIS = 80L
        const val MAX_LOAD_SIZE = 1800f
    }
}
