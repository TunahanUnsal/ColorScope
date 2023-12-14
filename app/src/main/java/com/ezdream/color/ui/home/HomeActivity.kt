package com.ezdream.color.ui.home


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ezdream.color.R
import com.ezdream.color.databinding.ActivityHomeBinding
import com.ezdream.color.ui.palette.PaletteActivity
import com.ezdream.color.ui.pick.PickActivity

class HomeActivity : AppCompatActivity() {

    private val REQUEST_CAMERA_PERMISSION = 101
    private val REQUEST_GALLERY_PERMISSION = 202
    private val REQUEST_IMAGE_CAPTURE = 303
    private lateinit var binding: ActivityHomeBinding
    private lateinit var imageUri: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        binding = ActivityHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.cameraButton.setOnClickListener {
            checkCameraPermission()
        }

        binding.galleryButton.setOnClickListener {
            checkGalleryPermission()
        }

        binding.colorPickerButton.setOnClickListener {
            val intent = Intent(baseContext, PaletteActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        } else {
            dispatchTakePictureIntent()
        }
    }

    private fun checkGalleryPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    REQUEST_GALLERY_PERMISSION
                )
            }

        } else {
            openGallery()
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.TITLE, "Image Title")
                put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
            }
            imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_GALLERY_PERMISSION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent()
                } else {
                    Toast.makeText(this, "Kamera izni reddedildi", Toast.LENGTH_SHORT).show()
                }
            }

            REQUEST_GALLERY_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    Toast.makeText(this, "Galeri izni reddedildi", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val intent = Intent(this, PickActivity::class.java)
            intent.putExtra("IMAGE_URI", imageUri)
            startActivity(intent)
        } else if (requestCode == REQUEST_GALLERY_PERMISSION && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage: Uri? = data.data
            val intent = Intent(this, PickActivity::class.java)
            intent.putExtra("IMAGE_URI", selectedImage)
            startActivity(intent)
        }
    }
}
