package com.hb.imageeditor

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.app.imageeditor.BaseActivity
import com.app.imageeditor.ImageEditorBuilder
import com.app.imageeditor.editor.ImageEditorActivity
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_image_editor_sample.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ImageEditorSampleActivity : AppCompatActivity() {
    private var mCropImageUri: Uri? = null
    private val REQUEST_CROP = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_editor_sample)
        initView()
    }

    private fun initView() {
        btnCrop.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkGalleryPermission()) {
                    CropImage.startPickImageActivity(this@ImageEditorSampleActivity)
                }
            } else {
                CropImage.startPickImageActivity(this@ImageEditorSampleActivity)
            }

//            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this@MainActivity)

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                openCropperActivity(data)
            } else if (requestCode == REQUEST_CROP) {
                val imageUri = data?.getParcelableExtra<Uri>("URI")
                imageView.setImageURI(imageUri)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkGalleryPermission(): Boolean {
        return if (CropImage.isReadExternalStoragePermissionsRequired(this, Uri.EMPTY)) {

            // request permissions and handle the result in onRequestPermissionsResult()
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE
            )
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                // required permissions granted, start crop image activity
                CropImage.startPickImageActivity(this@ImageEditorSampleActivity)
            } else {
                Toast.makeText(this, R.string.crop_image_activity_no_permissions, Toast.LENGTH_LONG)
                    .show()
//                setResultCancel()
            }
        }

        if (requestCode == CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE) {
            // Irrespective of whether camera permission was given or not, we show the picker
            // The picker will not add the camera intent if permission is not available
            CropImage.startPickImageActivity(this)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun openCropperActivity(data: Intent?) {
        val imageUri = CropImage.getPickImageResultUri(this, data)

        // For API >= 23 we need to check specifically that we have permissions to read external
        // storage,
        // but we don't know if we need to for the URI so the simplest is to try open the stream and
        // see if we get error.
        if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {

            // request permissions and handle the result in onRequestPermissionsResult()
            mCropImageUri = imageUri
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE
            )
        } else {
            startActivityForResult(
                getImageEditorIntent(imageUri)
                , REQUEST_CROP
            )
        }
    }

    //image take from camera or gallery
    private fun getOutputImageFilePath(): String {
        // External sdcard location
        val mediaStorageDir = baseContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (mediaStorageDir?.exists() == false) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Create Directory", "Oops! Failed create  directory")
                return ""
            }
        }

        return if (mediaStorageDir != null) {
            // Create a media file name
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            File(mediaStorageDir.path + File.separator + "IMG_" + timeStamp + ".jpg").absolutePath
                ?: ""
        } else {
            ""
        }
    }

    private fun getImageEditorIntent(imageUri: Uri): Intent {
        val builder =
            ImageEditorBuilder(this@ImageEditorSampleActivity, ImageEditorActivity::class.java)
                .setImageUri(imageUri = imageUri.toString())
                .setOutputPath(getOutputImageFilePath())
                .openOnlyImageCropper(cbOnlyCrop.isChecked)
                .setCropShape(if (rbRectangle.isChecked) BaseActivity.CROP_SHAPE_RECT else BaseActivity.CROP_SHAPE_OVAL)
                .setImageEditorTools(getToolsList())
                .setStickerImages(arrayListOf(R.drawable.aa, R.drawable.bb))

        when {
            rbFree.isChecked -> {
                builder.setFixAspectRation(false)
            }
            rb1to1.isChecked -> {
                builder.setAspectRation(1, 1)
            }
            rb3to4.isChecked -> {
                builder.setAspectRation(3, 4)
            }
            rb4to3.isChecked -> {
                builder.setAspectRation(4, 3)
            }
            rb9to16.isChecked -> {
                builder.setAspectRation(9, 16)
            }
            rb16to9.isChecked -> {
                builder.setAspectRation(16, 9)
            }
        }
        return builder.build()
    }

    private fun getToolsList(): ArrayList<String> {
        val list = ArrayList<String>()
        if (cbBrush.isChecked) list.add(BaseActivity.TOOL_BRUSH)
        if (cbText.isChecked) list.add(BaseActivity.TOOL_TEXT)
        if (cbEraser.isChecked) list.add(BaseActivity.TOOL_ERASER)
        if (cbFilter.isChecked) list.add(BaseActivity.TOOL_FILTER)
        if (cbEmoji.isChecked) list.add(BaseActivity.TOOL_EMOJI)
        if (cbSticker.isChecked) list.add(BaseActivity.TOOL_STICKER)
        return list
    }
}
