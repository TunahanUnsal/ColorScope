package com.ezdream.color.ui.palette

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ezdream.color.databinding.ActivityPaletteBinding
import com.skydoves.colorpickerview.listeners.ColorListener
import dagger.hilt.android.AndroidEntryPoint
import java.lang.String


@AndroidEntryPoint
class PaletteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaletteBinding
    private lateinit var viewModel: PaletteActivityVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("TAG", "onCreate: PaletteActivity")

        binding = ActivityPaletteBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[PaletteActivityVM::class.java]
        setContentView(binding.root)

        binding.colorPickerView.setColorListener(ColorListener { color, _ ->
            val hexColor = String.format("#%06X", 0xFFFFFF and color)
            Log.i("TAG", "hexColor: $hexColor")
            binding.hex.text = hexColor
            binding.box.setBackgroundColor(color)
        })

        binding.linear.setOnClickListener {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", binding.hex.text)
            clipboardManager.setPrimaryClip(clipData)
        }
    }
}
