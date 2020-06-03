package com.app.imageeditor

import android.content.Context
import android.content.Intent

/**
 * Builder class for configure image editor options
 */
class ImageEditorBuilder(context: Context, activity: Class<*>) {
    private val intent = Intent(context, activity)

    /**
     * This will open only image croping functionality
     */
    fun openOnlyImageCropper(cropOnly:Boolean):ImageEditorBuilder{
        intent.putExtra(BaseActivity.EXTRA_IMAGE_CROP_ONLY, cropOnly)
        return this
    }


    /**
     * Sets a bitmap loaded from the given Android URI as the content of the CropImageView.<br>
     * Can be used with URI from gallery or camera source.<br>
     * Will rotate the image by exif data.<br>
     *
     * @param uri the URI to load the image from
     */
    fun setImageUri(imageUri: String): ImageEditorBuilder {
        intent.putExtra(BaseActivity.EXTRA_IMAGE_URI, imageUri)
        return this
    }

    /**
     * Set output path to save cropped image.
     */
    fun setOutputPath(outputPath: String): ImageEditorBuilder {
        intent.putExtra(BaseActivity.EXTRA_OUTPUT_PATH, outputPath)
        return this
    }

    /**
     * Set shape of the cropping area
     * @param cropShape Rectangle or Circular
     */
    fun setCropShape(cropShape: Int): ImageEditorBuilder {
        intent.putExtra(BaseActivity.EXTRA_CROP_SHAPE, cropShape)
        return this
    }

    /**
     * Set width and height of cropped image
     */
    fun setOutputImageSize(width:Int, height:Int): ImageEditorBuilder {
        intent.putExtra(BaseActivity.EXTRA_IMAGE_WIDTH, width)
        intent.putExtra(BaseActivity.EXTRA_IMAGE_HEIGHT,height)
        return this
    }

    /**
     * Sets whether the aspect ratio is fixed or not; true fixes the aspect ratio, while false allows
     * it to be changed.
     */
    fun setFixAspectRation(isFixed: Boolean): ImageEditorBuilder {
        intent.putExtra(BaseActivity.EXTRA_FIXED_ASPECT_RATION, isFixed)
        return this
    }

    /**
     * Sets the both the X and Y values of the aspectRatio.<br>
     * Sets fixed aspect ratio to TRUE.
     * ex: 1:1, 4:3, 16:9, 9:16
     * @param aspectRatioX int that specifies the new X value of the aspect ratio
     * @param aspectRatioY int that specifies the new Y value of the aspect ratio
     */
    fun setAspectRation(aspectRationX:Int, aspectRationY:Int): ImageEditorBuilder {
        intent.putExtra(BaseActivity.EXTRA_ASPECT_RATION_X, aspectRationX)
        intent.putExtra(BaseActivity.EXTRA_ASPECT_RATION_Y, aspectRationY)
        intent.putExtra(BaseActivity.EXTRA_FIXED_ASPECT_RATION, true)
        return this
    }

    /**
     * Define list of image editor tools, which you want to show. Pass array list of an option
     */
    fun setImageEditorTools(list:ArrayList<String>):ImageEditorBuilder{
       intent.putExtra(BaseActivity.EXTRA_SELECTED_TOOLS, list)
        return this
    }

    /**
     * Add sticker images drawable ids, which you want to use as a stickers
     */
    fun setStickerImages(list: ArrayList<Int>): ImageEditorBuilder{
        intent.putExtra(BaseActivity.EXTRA_STICKER_IMAGES, list)
        return this
    }

    fun build() = intent

}