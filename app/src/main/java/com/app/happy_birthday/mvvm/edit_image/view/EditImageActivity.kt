package com.app.happy_birthday.mvvm.edit_image.view

import AdsManager
import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.GridLayout
import android.widget.GridView
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.app.happy_birthday.databinding.ActivityEditImageBinding
import com.app.happy_birthday.helper.BaseActivity
import com.app.happy_birthday.R
import com.app.happy_birthday.helper.Extensions
import com.google.android.gms.ads.MobileAds
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView
import ja.burhanrashid52.photoeditor.TextStyleBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditImageActivity : BaseActivity() {

    private lateinit var photoEditorView: PhotoEditorView
    private lateinit var photoEditor: PhotoEditor
    lateinit var binding: ActivityEditImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        photoEditorView = findViewById(R.id.photoEditorView)

        MobileAds.initialize(this) {  // Callback for init complete
            Log.d("AdMob", "Initialized")
        }

        val bitmap = Extensions.BitmapCache.getBitmap() ?: run {
            Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Get bitmap from intent (passed from viewer)
//        originalBitmap = intent.getParcelableExtra<Bitmap>("bitmap")
        photoEditorView.source.setImageBitmap(bitmap)

        photoEditor = PhotoEditor.Builder(this, photoEditorView)
            .setPinchTextScalable(true)
            .build()

        setupEditingTools()

    }

    override fun getTopInsetView(): View? {
        return  binding.root
    }

    private fun saveEditedImage() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val editedBitmap = photoEditor.saveAsBitmap()
                // Make a safe copy before storing
                val bitmapToCache = editedBitmap.copy(editedBitmap.config ?: Bitmap.Config.ARGB_8888, true)
// DO NOT recycle editedBitmap here

                withContext(Dispatchers.Main) {
                    Extensions.BitmapCache.setBitmap(bitmapToCache)
                    setResult(Activity.RESULT_OK)
                    AdsManager.showInterstitial(this@EditImageActivity) {}
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@EditImageActivity,
                        "Save failed: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    override fun onDestroy() {
//        Extensions.BitmapCache.clear()
        super.onDestroy()
    }
    // In EditImageActivity.kt - Replace your current tool click listeners with this code

    private var selectedTool: String? = null

    private fun setupEditingTools() {
        // Brush Tool
        binding.btnBrush.setOnClickListener {
            showBrushSettingsDialog()            // Full opacity
        }

        // Text Tool
        binding.btnText.setOnClickListener {
            showTextEditorDialog()
        }

        // Emoji Tool
        binding.btnEmoji.setOnClickListener {
            showEmojiPickerDialog()
        }

        // Eraser Tool
        binding.btnEraser.setOnClickListener {
            photoEditor.brushEraser()             // Larger size for easy erasing
        }

        // Undo
        binding.btnUndo.setOnClickListener {
            photoEditor.undo()
            // No need to change selected tool
        }

        // Redo
        binding.btnRedo.setOnClickListener {
            photoEditor.redo()
        }

        binding.ivEditBack.setOnClickListener {
            finish()
        }

        binding.tvSaveEdit.setOnClickListener {
            saveEditedImage()
        }
    }

    private fun selectTool(tool: String) {
        selectedTool = tool
        // Optional: Add visual highlight (change background or tint of selected button)
        // Example: Change icon tint or add border - you can customize this
    }

    private fun showTextEditorDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_text, null)
        val editText = dialogView.findViewById<EditText>(R.id.etTextInput)
        val tvPreview = dialogView.findViewById<TextView>(R.id.tvTextPreview)
        val colorGrid = dialogView.findViewById<GridLayout>(R.id.colorGrid)
        val btnBold = dialogView.findViewById<ToggleButton>(R.id.btnBold)
        val btnItalic = dialogView.findViewById<ToggleButton>(R.id.btnItalic)

//        editText.setText("Merry Christmas!")

        var selectedColor = Color.WHITE
        var selectedColorView: View? = null

        // Live preview
        fun updatePreview() {
            tvPreview.text = editText.text
            tvPreview.setTextColor(selectedColor)

            val style = when {
                btnBold.isChecked && btnItalic.isChecked -> Typeface.BOLD_ITALIC
                btnBold.isChecked -> Typeface.BOLD
                btnItalic.isChecked -> Typeface.ITALIC
                else -> Typeface.NORMAL
            }
            tvPreview.typeface = Typeface.create(Typeface.DEFAULT, style)
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { updatePreview() }
        })

//        editText.setText("Merry Christmas!")
//
//        // ADD THESE LINES:
//        editText.requestFocus()                                   // Give focus to EditText
//        editText.setSelection(editText.text.length)               // Place cursor at the end

// Show keyboard automatically

        // Colors
        val colors = intArrayOf(
            Color.WHITE,                    // White (great for dark backgrounds)
            Color.BLACK,                    // Black (great for light backgrounds)
            Color.parseColor("#FFD700"),    // Gold
            Color.parseColor("#FF0000"),    // Christmas Red
            Color.parseColor("#008000"),    // Christmas Green (Dark Green)
            Color.parseColor("#C0C0C0"),    // Silver
            Color.parseColor("#FF69B4"),    // Hot Pink
            Color.parseColor("#00CED1"),    // Turquoise
            Color.parseColor("#FFA500"),    // Orange
            Color.parseColor("#32CD32"),    // Lime Green (Bright festive)
            Color.parseColor("#FF4500"),    // Orange Red (Vibrant)
            Color.parseColor("#9400D3")     // Dark Violet (Royal touch)
        )

        for (color in colors) {
            val colorView = View(this).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 100
                    height = 100
                    setMargins(16, 16, 16, 16)
                }
                setBackgroundColor(color)
                background = ContextCompat.getDrawable(this@EditImageActivity, R.drawable.color_selector)
                backgroundTintList = ColorStateList.valueOf(color)
            }

            colorView.setOnClickListener {
                // Reset previous selection
                selectedColorView?.background = ContextCompat.getDrawable(this@EditImageActivity, R.drawable.color_selector)
                selectedColorView?.backgroundTintList = ColorStateList.valueOf(selectedColor)  // Restore its color

                // Apply selected state
                colorView.background = ContextCompat.getDrawable(this@EditImageActivity, R.drawable.color_selected)
                // Keep the color tint
                colorView.backgroundTintList = ColorStateList.valueOf(color)

                selectedColor = color
                selectedColorView = colorView
                updatePreview()
            }

            colorGrid.addView(colorView)

            // Default: Select White
            if (color == Color.WHITE) {
                selectedColor = Color.WHITE
                selectedColorView = colorView
                colorView.background = ContextCompat.getDrawable(this@EditImageActivity, R.drawable.color_selected)
            }
        }

        btnBold.setOnCheckedChangeListener { _, _ -> updatePreview() }
        btnItalic.setOnCheckedChangeListener { _, _ -> updatePreview() }

        updatePreview()

        // DIALOG - SAFE VERSION WITHOUT COMPLEX LAMBDA CAPTURE
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setNegativeButton("Cancel", null)
            .create()

        // Set positive button manually to avoid lambda capture issues
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add") { _, _ ->
            val userText = editText.text.toString().trim()
            if (userText.isNotEmpty()) {
                val style = when {
                    btnBold.isChecked && btnItalic.isChecked -> Typeface.BOLD_ITALIC
                    btnBold.isChecked -> Typeface.BOLD
                    btnItalic.isChecked -> Typeface.ITALIC
                    else -> Typeface.NORMAL
                }

                // AVOID apply {} here — create manually
                val styleBuilder = TextStyleBuilder()
                styleBuilder.withTextColor(selectedColor)
                styleBuilder.withTextStyle(style)
                styleBuilder.withTextSize(50f)

                photoEditor.addText(userText, styleBuilder)
            }
        }

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)


//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#CC000000")))
        dialog.show()
    }

    private fun showEmojiPickerDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_emoji_picker, null)
        val emojiGrid = dialogView.findViewById<GridView>(R.id.emojiGrid)

        // Popular Christmas + General Emojis
        val emojiList = listOf(
            "😊", "😂", "❤️", "🎄", "🎅", "🤶", "❄️", "☃️", "🔔",
            "🎁", "🎀", "🦌", "🕯️", "⭐", "✨", "🌟", "🔥", "💫",
            "🥰", "😍", "😘", "😎", "🤩", "🎉", "🥳", "✌️", "👌",
            "🙌", "👏", "💪", "🤝", "🙏", "🌹", "🌺", "🌈", "🦋"
        )

        val adapter = object : ArrayAdapter<String>(this, R.layout.item_emoji, R.id.tvEmoji, emojiList) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val tvEmoji = view.findViewById<TextView>(R.id.tvEmoji)
                tvEmoji.text = emojiList[position]
                tvEmoji.textSize = 26f  // Large emojis
                return view
            }
        }

        emojiGrid.adapter = adapter


        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setNegativeButton("Close", null)
            .create()


        emojiGrid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            photoEditor.addEmoji(emojiList[position])
            // Optional: close dialog after selection
            dialog.dismiss()
        }

//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#CC000000")))
        dialog.show()
    }

    private var brushColor = Color.RED
    private var brushSize = 30f
    private var brushOpacity = 255
    private var isEraserMode = false

    private fun showBrushSettingsDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_brush_settings, null)
        val colorGrid = dialogView.findViewById<GridLayout>(R.id.colorGrid)
        val sizeSlider = dialogView.findViewById<SeekBar>(R.id.sizeSlider)
        val sizeValue = dialogView.findViewById<TextView>(R.id.tvSizeValue)
        val opacitySlider = dialogView.findViewById<SeekBar>(R.id.opacitySlider)
        val opacityValue = dialogView.findViewById<TextView>(R.id.tvOpacityValue)
        val previewView = dialogView.findViewById<ImageView>(R.id.previewBrush)
        val toggleEraser = dialogView.findViewById<ToggleButton>(R.id.toggleEraser)

        // Same Christmas colors
        val colors = intArrayOf(
            Color.WHITE, Color.BLACK,
            Color.parseColor("#FFD700"), Color.parseColor("#FF0000"),
            Color.parseColor("#008000"), Color.parseColor("#C0C0C0"),
            Color.parseColor("#FF69B4"), Color.parseColor("#00CED1"),
            Color.parseColor("#FFA500"), Color.parseColor("#FF4500")
        )

        var selectedColorView: View? = null

        for (color in colors) {
            val colorView = View(this).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 100
                    height = 100
                    setMargins(16, 16, 16, 16)
                }
                background = ContextCompat.getDrawable(this@EditImageActivity, R.drawable.color_selector)
                backgroundTintList = ColorStateList.valueOf(color)
            }

            colorView.setOnClickListener {
                brushColor = color
                selectedColorView?.background = ContextCompat.getDrawable(this@EditImageActivity, R.drawable.color_selector)
                colorView.background = ContextCompat.getDrawable(this@EditImageActivity, R.drawable.color_selected)
                selectedColorView = colorView
                updateBrushPreview(previewView)
            }

            colorGrid.addView(colorView)

            if (color == Color.RED) {  // Default red
                selectedColorView = colorView
                colorView.background = ContextCompat.getDrawable(this@EditImageActivity, R.drawable.color_selected)
            }
        }

        // Size slider
        sizeSlider.max = 70  // 10 to 80
        sizeSlider.progress = (brushSize - 10).toInt()
        sizeValue.text = "${brushSize.toInt()}px"
        sizeSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                brushSize = 10f + progress
                sizeValue.text = "${brushSize.toInt()}px"
                updateBrushPreview(previewView)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Opacity slider
        opacitySlider.max = 255
        opacitySlider.progress = brushOpacity
        opacityValue.text = "$brushOpacity"
        opacitySlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                brushOpacity = progress
                opacityValue.text = "$brushOpacity"
                updateBrushPreview(previewView)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Eraser toggle
        toggleEraser.isChecked = isEraserMode
        toggleEraser.setOnCheckedChangeListener { _, isChecked ->
            isEraserMode = isChecked
            updateBrushPreview(previewView)
        }

        // Initial preview
        updateBrushPreview(previewView)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton(getString(R.string.label_done)) { _, _ ->
                photoEditor.setBrushDrawingMode(true)
                if (isEraserMode) {
                    photoEditor.brushEraser()
                } else {
                    photoEditor.brushColor = brushColor
                    photoEditor.brushSize = brushSize
                    photoEditor.setOpacity(brushOpacity)
                }
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                // Optional: reset to previous
            }
            .create()

//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#CC000000")))
        dialog.show()
    }

    private fun updateBrushPreview(previewView: ImageView) {
        val bitmap = Bitmap.createBitmap(200, 100, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.parseColor("#333333"))

        val paint = Paint().apply {
            color = if (isEraserMode) Color.TRANSPARENT else brushColor
            alpha = brushOpacity
            style = Paint.Style.STROKE
            strokeWidth = brushSize
            strokeCap = Paint.Cap.ROUND
        }

        canvas.drawLine(30f, 50f, 170f, 50f, paint)
        if (isEraserMode) {
            canvas.drawText("Eraser Mode", 50f, 80f, Paint().apply { color = Color.WHITE; textSize = 30f })
        }

        previewView.setImageBitmap(bitmap)
    }



}