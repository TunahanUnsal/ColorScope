package com.ezdream.color.util

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Movie
import android.view.View
import androidx.core.graphics.get
import com.google.android.material.snackbar.Snackbar
import java.io.InputStream

object UiUtil {

    fun customAlertDialog(context: Context, message: String) {
        val builder = AlertDialog.Builder(context)

        with(builder)
        {
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    fun showSnackBar(parentView: View, message: String) {
        val snack = Snackbar.make(parentView, message, Snackbar.LENGTH_SHORT)
        snack.show()
    }

    fun getDuration(context: Context, id: Int): Int {
        val temp: InputStream = context.resources.openRawResource(id)
        val movie = Movie.decodeStream(temp)
        return movie.duration()
    }

    fun getDominantColors(bitmap: Bitmap, numberOfColors: Int): List<Int> {
        val pixelMap = mutableMapOf<Int, Int>()

        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                val pixelColor = bitmap[x, y]

                if (pixelMap.containsKey(pixelColor)) {
                    pixelMap[pixelColor] = pixelMap[pixelColor]!! + 1
                } else {
                    pixelMap[pixelColor] = 1
                }
            }
        }

        val sortedMap = pixelMap.toList().sortedByDescending { (_, value) -> value }.toMap()

        val dominantColors = mutableListOf<Int>()
        val iterator = sortedMap.entries.iterator()
        var count = 0
        while (iterator.hasNext() && count < numberOfColors) {
            dominantColors.add(iterator.next().key)
            count++
        }

        return dominantColors
    }
}