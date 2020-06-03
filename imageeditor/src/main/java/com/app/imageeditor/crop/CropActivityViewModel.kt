package com.app.imageeditor.crop

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import com.app.imageeditor.BaseViewModel
import com.app.imageeditor.BaseActivity.Companion.CROP_SHAPE_RECT
import com.app.imageeditor.BaseActivity.Companion.EXTRA_ASPECT_RATION_X
import com.app.imageeditor.BaseActivity.Companion.EXTRA_ASPECT_RATION_Y
import com.app.imageeditor.BaseActivity.Companion.EXTRA_CROP_SHAPE
import com.app.imageeditor.BaseActivity.Companion.EXTRA_FIXED_ASPECT_RATION
import com.app.imageeditor.BaseActivity.Companion.EXTRA_IMAGE_HEIGHT
import com.app.imageeditor.BaseActivity.Companion.EXTRA_IMAGE_URI
import com.app.imageeditor.BaseActivity.Companion.EXTRA_IMAGE_WIDTH
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.Closeable
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class CropActivityViewModel(app: Application) : BaseViewModel(app) {

    var outPutImageWidth = 0
    var outPutImageHeight = 0
    /**
     * Set crop image configuration
     */
    fun setCropImageConfig(cropImageView: CropImageView, intent: Intent?){
        cropImageView.setImageUriAsync(Uri.parse(intent?.getStringExtra(EXTRA_IMAGE_URI)?:""))
        cropImageView.cropShape = if((intent?.getIntExtra(EXTRA_CROP_SHAPE, CROP_SHAPE_RECT)?: CROP_SHAPE_RECT) == CROP_SHAPE_RECT){
            CropImageView.CropShape.RECTANGLE
        }else{
            CropImageView.CropShape.OVAL
        }
        cropImageView.setFixedAspectRatio(intent?.getBooleanExtra(EXTRA_FIXED_ASPECT_RATION, false)?:false)
        val aspectRationX = intent?.getIntExtra(EXTRA_ASPECT_RATION_X,0)?:0
        val aspectRationY = intent?.getIntExtra(EXTRA_ASPECT_RATION_Y,0)?:0
        if(aspectRationX>0 && aspectRationY> 0){
            cropImageView.setAspectRatio(aspectRationX, aspectRationY)
        }
        outPutImageWidth = intent?.getIntExtra(EXTRA_IMAGE_WIDTH, 0)?:0
        outPutImageHeight = intent?.getIntExtra(EXTRA_IMAGE_HEIGHT, 0)?:0
    }

    @Throws(FileNotFoundException::class)
    fun saveBitmapToUri(context: Context, bitmap: Bitmap, outPutPath:String) {
        val outPutStream = context.contentResolver.openOutputStream(Uri.fromFile(
            File(outPutPath)))
        bitmap.compress(Bitmap.CompressFormat.PNG, 95, outPutStream)
        closeSafe(outPutStream)
    }

    /**
     * Close the given closeable object (Stream) in a safe way: check if it is null and catch-log
     * exception thrown.
     *
     * @param closeable the closable object to close
     */
    private fun closeSafe(closeable: Closeable?) {
        if (closeable != null) {
            try {
                closeable.close()
            } catch (ignored: IOException) {
            }
        }
    }
}