package com.app.happy_birthday.mvvm.collage.view

import android.annotation.SuppressLint
import android.graphics.Matrix
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import com.app.happy_birthday.helper.collage.CollageRenderer
import com.app.happy_birthday.mvvm.collage.view_model.CollageViewModel

/** Lets the user drag, pinch and double-tap photos directly on the collage preview. */
class CollagePreviewGestures(
    private val preview: ImageView,
    private val viewModel: CollageViewModel
) {

    private var isAdjusting = false
    private var lastX = 0f
    private var lastY = 0f

    private val scaleDetector = ScaleGestureDetector(
        preview.context,
        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                if (isAdjusting) viewModel.zoomPhoto(detector.scaleFactor)
                return true
            }
        }
    )

    private val tapDetector = GestureDetector(
        preview.context,
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(event: MotionEvent): Boolean {
                previewPoint(event.x, event.y)?.let { (x, y) -> viewModel.resetPhotoAt(x, y) }
                return true
            }
        }
    )

    @SuppressLint("ClickableViewAccessibility")
    fun attach() {
        preview.setOnTouchListener { _, event ->
            scaleDetector.onTouchEvent(event)
            tapDetector.onTouchEvent(event)
            handleTouch(event)
        }
    }

    private fun handleTouch(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                val point = previewPoint(event.x, event.y)
                isAdjusting = point != null && viewModel.startAdjusting(point.first, point.second)
                rememberPosition(event)
                return isAdjusting
            }

            // The finger used for tracking may have changed, so start again from here.
            MotionEvent.ACTION_POINTER_DOWN,
            MotionEvent.ACTION_POINTER_UP -> markPositionUnknown()

            MotionEvent.ACTION_MOVE -> {
                if (!isAdjusting) return false
                if (event.pointerCount == 1 && !scaleDetector.isInProgress) {
                    if (lastX.isNaN()) {
                        rememberPosition(event)
                    } else {
                        val start = previewPoint(lastX, lastY)
                        val end = previewPoint(event.x, event.y)
                        if (start != null && end != null) {
                            viewModel.movePhoto(end.first - start.first, end.second - start.second)
                        }
                        rememberPosition(event)
                    }
                }
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> isAdjusting = false
        }
        return true
    }

    private fun rememberPosition(event: MotionEvent) {
        lastX = event.x
        lastY = event.y
    }

    private fun markPositionUnknown() {
        lastX = Float.NaN
        lastY = Float.NaN
    }

    /** Converts a point on the view to preview pixels, or null if nothing is showing yet. */
    private fun previewPoint(viewX: Float, viewY: Float): Pair<Float, Float>? {
        val drawable = preview.drawable ?: return null
        if (drawable.intrinsicWidth <= 0) return null

        val toImage = Matrix()
        if (!preview.imageMatrix.invert(toImage)) return null
        val point = floatArrayOf(viewX - preview.paddingLeft, viewY - preview.paddingTop)
        toImage.mapPoints(point)

        val scale = CollageRenderer.PREVIEW_WIDTH.toFloat() / drawable.intrinsicWidth
        return point[0] * scale to point[1] * scale
    }
}
