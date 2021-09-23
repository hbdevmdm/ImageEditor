package com.app.imageeditor

import android.R
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.imageeditor.crop.CropActivity
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CROP = 101
        const val CROP_SHAPE_RECT = 1
        const val CROP_SHAPE_OVAL = 2
        const val EXTRA_IMAGE_URI = "image_uri"
        const val EXTRA_OUTPUT_PATH = "output_path"
        const val EXTRA_CROP_SHAPE = "crop_shape"
        const val EXTRA_IMAGE_WIDTH = "image_width"
        const val EXTRA_IMAGE_HEIGHT = "image_height"
        const val EXTRA_FIXED_ASPECT_RATION = "fixed_aspect_ration"
        const val EXTRA_ASPECT_RATION_X = "aspect_ration_x"
        const val EXTRA_ASPECT_RATION_Y = "aspect_ration_y"
        const val EXTRA_IMAGE_CROP_ONLY = "image_crop_only"
        const val EXTRA_INPUT_TEXT = "extra_input_text"
        const val EXTRA_COLOR_CODE = "extra_color_code"
        const val EXTRA_SELECTED_TOOLS = "extra_selected_tools"
        const val EXTRA_STICKER_IMAGES = "sticker_images"
        const val TOOL_BRUSH = "brush"
        const val TOOL_TEXT = "text"
        const val TOOL_ERASER = "eraser"
        const val TOOL_FILTER = "filter"
        const val TOOL_EMOJI = "emoji"
        const val TOOL_STICKER = "sticker"
    }

    private var progressDialogFragment: ProgressDialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeItFullscreen(window)
        changeStatusBarIcon(true)
        progressDialogFragment = ProgressDialogFragment.newInstance()
    }

    fun getCropIntent(options: ImageEditorViewOptions): Intent {
        return ImageEditorBuilder(this@BaseActivity, CropActivity::class.java)
            .setImageUri(imageUri = options.imageUri)
            .setOutputPath(options.outPutPath)
            .setCropShape(options.cropShape)
            .setAspectRation(options.aspectRatio.first, options.aspectRatio.second)
            .setFixAspectRation(options.fixAspectRatio)
            .build()
    }

    fun showHideProgressDialog(isShow: Boolean, message: String = "") {
        try {
            if (isShow) {
                if (progressDialogFragment?.dialog == null || progressDialogFragment?.dialog?.isShowing == false) {
                    ProgressDialogFragment.message = message
                    progressDialogFragment?.show(
                        supportFragmentManager,
                        javaClass.simpleName
                    )
                }
            } else {
                progressDialogFragment?.dismissAllowingStateLoss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected open fun showSnackbar(message: String) {
        val view = findViewById<View>(R.id.content)
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun sendResult(imageUri: Uri? = null, isSuccess: Boolean) {
        val intent = Intent()
        if (isSuccess) {
            setResult(Activity.RESULT_OK, intent)
            intent.putExtra("URI", imageUri)
        } else {
            setResult(Activity.RESULT_CANCELED, intent)
        }
        finish()
    }

    private fun makeItFullscreen(window: Window) {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    private fun changeStatusBarIcon(lightIcon: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags: Int = window.decorView.systemUiVisibility
            if (lightIcon) {
                flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            } else {
                flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            window.decorView.systemUiVisibility = flags
            window.statusBarColor = Color.TRANSPARENT
        }
    }
}
