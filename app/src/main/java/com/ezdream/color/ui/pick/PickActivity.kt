package com.ezdream.color.ui.pick

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ezdream.color.databinding.ActivityPickBinding
import com.ezdream.color.util.UiUtil.getDominantColors
import com.skydoves.colorpickerview.listeners.ColorListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PickActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPickBinding
    private lateinit var viewModel: PickActivityVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("TAG", "onCreate: PickActivity")

        binding = ActivityPickBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[PickActivityVM::class.java]
        setContentView(binding.root)

        val receivedImageUri: Uri? = intent.getParcelableExtra("IMAGE_URI")

        val loadedBitmap: Bitmap? = receivedImageUri?.let { uri ->
            contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        }

        val drawable: Drawable = BitmapDrawable(resources, loadedBitmap)
        binding.colorPickerView.setPaletteDrawable(drawable)

        binding.colorPickerView.setColorListener(ColorListener { color, _ ->
            val hexColor = java.lang.String.format("#%06X", 0xFFFFFF and color)
            binding.test.setBackgroundColor(color)
            binding.hex.text = hexColor
        })

        binding.linear.setOnClickListener {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", binding.hex.text)
            clipboardManager.setPrimaryClip(clipData)
        }

        binding.dominant.setOnClickListener {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", binding.hex.text)
            clipboardManager.setPrimaryClip(clipData)
        }

        val background = object : Thread() {
            override fun run() {
                try {
                    val dominantColors = loadedBitmap?.let { getDominantColors(it, 1) }

                    if (dominantColors != null) {
                        for (color in dominantColors) {
                            val hexColor = String.format("#%06X", 0xFFFFFF and color)
                            runOnUiThread {
                                binding.dominant.visibility = View.VISIBLE
                                binding.loading.visibility = View.GONE
                                binding.hex2.text = hexColor
                                binding.test2.setBackgroundColor(Color.parseColor(hexColor))
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
}
