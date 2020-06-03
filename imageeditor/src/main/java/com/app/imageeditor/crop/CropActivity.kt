package com.app.imageeditor.crop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.app.imageeditor.BaseActivity
import com.app.imageeditor.R
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_crop.*
import java.io.File

class CropActivity : BaseActivity() {

    private val viewModel: CropActivityViewModel by lazy {
        ViewModelProviders.of(this).get(CropActivityViewModel::class.java)
    }

    private var rotateDegree = 0
    private var outputPath = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop)

        outputPath = intent?.getStringExtra(EXTRA_OUTPUT_PATH) ?: ""
        initView(intent)
    }

    /**
     * Initialize view
     */
    private fun initView(intent: Intent?) {
        when {
            (intent?.getStringExtra("image_uri") ?: "").isEmpty() -> {
                sendResult(false, "Image uri can not be empty")
            }
            outputPath.isEmpty() -> {
                sendResult(false, message = "Output path can not be empty.")
            }
            else -> {
                setCropView(intent)
                initListeners()
            }
        }
    }

    /**
     * Show crop view with an image
     */
    private fun setCropView(intent: Intent?) {
        viewModel.setCropImageConfig(cropImageView, intent)

        cropImageView.scaleType = CropImageView.ScaleType.CENTER
        cropImageView.setOnCropImageCompleteListener { _, result ->
            sendResult(
                true,
                cropResult = result
            )
        }
    }

    private fun initListeners() {
        btnCancel.setOnClickListener {
           onBackPressed()
        }

        btnRotate.setOnClickListener {
            rotateDegree = (rotateDegree + 90) % 360
            cropImageView.rotatedDegrees = rotateDegree
        }

        btnDone.setOnClickListener {
            if(cropImageView.cropShape == CropImageView.CropShape.OVAL){
                if(viewModel.outPutImageHeight>0 && viewModel.outPutImageWidth>0){
                    cropImageView.getCroppedImageAsync(viewModel.outPutImageWidth, viewModel.outPutImageHeight)
                }else {
                    cropImageView.getCroppedImageAsync()
                }
            }else {
                if(viewModel.outPutImageHeight>0 && viewModel.outPutImageWidth>0){
                    cropImageView.saveCroppedImageAsync(Uri.fromFile(File(outputPath)),Bitmap.CompressFormat.JPEG, 95,viewModel.outPutImageWidth, viewModel.outPutImageHeight)
                }else {
                    cropImageView.saveCroppedImageAsync(Uri.fromFile(File(outputPath)))
                }
            }
        }
    }


    private fun handleCropResult(cropResult: CropImageView.CropResult?) {
        val intent = Intent()
        if (cropResult != null && cropResult.error == null) {
            intent.putExtra("SAMPLE_SIZE", cropResult.sampleSize)
            if (cropResult.uri != null) {
                if (cropImageView.cropShape == CropImageView.CropShape.OVAL) {
                    viewModel.saveBitmapToUri(baseContext, cropResult.bitmap, outputPath)
                }
                intent.putExtra("URI", cropResult.uri)
                setResult(Activity.RESULT_OK, intent)
            } else {
                if (cropImageView.cropShape == CropImageView.CropShape.OVAL) {
                    viewModel.saveBitmapToUri(baseContext, CropImage.toOvalBitmap(cropResult.bitmap), outputPath)
                }else{
                    viewModel.saveBitmapToUri(baseContext, cropResult.bitmap, outputPath)
                }
                intent.putExtra("URI", Uri.fromFile(File(outputPath)))
                setResult(Activity.RESULT_OK, intent)
            }
        }else{
            Toast.makeText(
                this@CropActivity,
                "Image crop failed: " + cropResult?.error?.message,
                Toast.LENGTH_LONG
            )
                .show()
            setResult(Activity.RESULT_CANCELED, intent)
        }
    }


    private fun sendResult(
        success: Boolean,
        message: String = "",
        cropResult: CropImageView.CropResult? = null
    ) {
        if (success) {
            handleCropResult(cropResult)
        } else {
            Toast.makeText(
                this@CropActivity,
                "Image crop failed: $message",
                Toast.LENGTH_LONG
            )
                .show()
            setResult(Activity.RESULT_CANCELED, Intent())
        }
        finish()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED, Intent())
        super.onBackPressed()
    }
}
