package com.app.imageeditor.editor

import android.app.Application
import android.content.Intent
import android.util.Pair
import com.app.imageeditor.BaseActivity
import com.app.imageeditor.BaseViewModel
import com.app.imageeditor.ImageEditorViewOptions
import com.app.imageeditor.R
import ja.burhanrashid52.photoeditor.PhotoFilter

class ImageEditorActivityViewModel(app: Application) : BaseViewModel(app) {

    private val imageEditorOption = ImageEditorViewOptions()

    fun setImageEditorOptions(intent: Intent?) {
        imageEditorOption.imageUri = intent?.getStringExtra(BaseActivity.EXTRA_IMAGE_URI) ?: ""
        imageEditorOption.outPutPath = intent?.getStringExtra(BaseActivity.EXTRA_OUTPUT_PATH) ?: ""
        imageEditorOption.outPutWidth = intent?.getIntExtra(BaseActivity.EXTRA_IMAGE_WIDTH, 0) ?: 0
        imageEditorOption.outPutHeight =
                intent?.getIntExtra(BaseActivity.EXTRA_IMAGE_HEIGHT, 0) ?: 0

        imageEditorOption.isCropOnly =
                intent?.getBooleanExtra(BaseActivity.EXTRA_IMAGE_CROP_ONLY, false) ?: false
        val aspectRationX = intent?.getIntExtra(BaseActivity.EXTRA_ASPECT_RATION_X, 1) ?: 1
        val aspectRationY = intent?.getIntExtra(BaseActivity.EXTRA_ASPECT_RATION_Y, 1) ?: 1
        imageEditorOption.aspectRatio = Pair(aspectRationX, aspectRationY)
        imageEditorOption.fixAspectRatio =
                intent?.getBooleanExtra(BaseActivity.EXTRA_FIXED_ASPECT_RATION, false) ?: false
        imageEditorOption.selectedTools =
                intent?.getStringArrayListExtra(BaseActivity.EXTRA_SELECTED_TOOLS) ?: ArrayList()
        imageEditorOption.stickerImages =
                intent?.getIntegerArrayListExtra(BaseActivity.EXTRA_STICKER_IMAGES) ?: ArrayList()
    }

    fun getImageEditorOptions() = imageEditorOption

    fun isCropOnly() = imageEditorOption.isCropOnly

    /**
     * This will return list of available image editor tools according to user configuration.
     */
    fun getEditorTools(): ArrayList<ToolModel> {
        val tools = ArrayList<ToolModel>()
        if (imageEditorOption.selectedTools.isEmpty()) {
            tools.add(
                    getBrushTool()
            )
            tools.add(
                    getTextTool()
            )
            tools.add(
                    getEraseTool()
            )
            tools.add(
                    getFilterTool()
            )
            tools.add(
                    getEmojiTool()
            )
            tools.add(
                    getStickerTool()
            )
        } else {
            imageEditorOption.selectedTools.forEach {
                when {
                    it.equals(BaseActivity.TOOL_BRUSH, false) -> {
                        tools.add(getBrushTool())
                    }
                    it.equals(BaseActivity.TOOL_TEXT, false) -> {
                        tools.add(getTextTool())
                    }
                    it.equals(BaseActivity.TOOL_ERASER, false) -> {
                        tools.add(getEraseTool())
                    }
                    it.equals(BaseActivity.TOOL_FILTER, false) -> {
                        tools.add(getFilterTool())
                    }
                    it.equals(BaseActivity.TOOL_EMOJI, false) -> {
                        tools.add(getEmojiTool())
                    }
                    it.equals(BaseActivity.TOOL_STICKER, false) -> {
                        tools.add(getStickerTool())
                    }
                }
            }
        }
        return tools
    }

    /**
     * This will return list of all available filters
     */
    fun getFilters(): ArrayList<Pair<String, PhotoFilter>> {
        val mPairList = ArrayList<Pair<String, PhotoFilter>>()
        mPairList.add(
                Pair(
                        "filters/original.jpg",
                        PhotoFilter.NONE
                )
        )
        mPairList.add(
                Pair(
                        "filters/auto_fix.png",
                        PhotoFilter.AUTO_FIX
                )
        )
        mPairList.add(
                Pair(
                        "filters/brightness.png",
                        PhotoFilter.BRIGHTNESS
                )
        )
        mPairList.add(
                Pair(
                        "filters/contrast.png",
                        PhotoFilter.CONTRAST
                )
        )
        mPairList.add(
                Pair(
                        "filters/documentary.png",
                        PhotoFilter.DOCUMENTARY
                )
        )
        mPairList.add(
                Pair(
                        "filters/dual_tone.png",
                        PhotoFilter.DUE_TONE
                )
        )
        mPairList.add(
                Pair(
                        "filters/fill_light.png",
                        PhotoFilter.FILL_LIGHT
                )
        )
        mPairList.add(
                Pair(
                        "filters/fish_eye.png",
                        PhotoFilter.FISH_EYE
                )
        )
        mPairList.add(
                Pair(
                        "filters/grain.png",
                        PhotoFilter.GRAIN
                )
        )
        mPairList.add(
                Pair(
                        "filters/gray_scale.png",
                        PhotoFilter.GRAY_SCALE
                )
        )
        mPairList.add(
                Pair(
                        "filters/lomish.png",
                        PhotoFilter.LOMISH
                )
        )
        mPairList.add(
                Pair(
                        "filters/negative.png",
                        PhotoFilter.NEGATIVE
                )
        )
        mPairList.add(
                Pair(
                        "filters/posterize.png",
                        PhotoFilter.POSTERIZE
                )
        )
        mPairList.add(
                Pair(
                        "filters/saturate.png",
                        PhotoFilter.SATURATE
                )
        )
        mPairList.add(
                Pair(
                        "filters/sepia.png",
                        PhotoFilter.SEPIA
                )
        )
        mPairList.add(
                Pair(
                        "filters/sharpen.png",
                        PhotoFilter.SHARPEN
                )
        )
        mPairList.add(
                Pair(
                        "filters/temprature.png",
                        PhotoFilter.TEMPERATURE
                )
        )
        mPairList.add(
                Pair(
                        "filters/tint.png",
                        PhotoFilter.TINT
                )
        )
        mPairList.add(
                Pair(
                        "filters/vignette.png",
                        PhotoFilter.VIGNETTE
                )
        )
        mPairList.add(
                Pair(
                        "filters/cross_process.png",
                        PhotoFilter.CROSS_PROCESS
                )
        )
        mPairList.add(
                Pair(
                        "filters/b_n_w.png",
                        PhotoFilter.BLACK_WHITE
                )
        )
        mPairList.add(
                Pair(
                        "filters/flip_horizental.png",
                        PhotoFilter.FLIP_HORIZONTAL
                )
        )
        mPairList.add(
                Pair(
                        "filters/flip_vertical.png",
                        PhotoFilter.FLIP_VERTICAL
                )
        )
        mPairList.add(
                Pair(
                        "filters/rotate.png",
                        PhotoFilter.ROTATE
                )
        )

        return mPairList
    }

    private fun getBrushTool(): ToolModel {
        return ToolModel(
                toolName = "Brush",
                toolIcon = R.drawable.ic_brush,
                toolType = ToolType.BRUSH
        )
    }

    private fun getTextTool(): ToolModel {
        return ToolModel(
                toolName = "Text",
                toolIcon = R.drawable.ic_text,
                toolType = ToolType.TEXT
        )
    }

    private fun getEraseTool(): ToolModel {
        return ToolModel(
                toolName = "Eraser",
                toolIcon = R.drawable.ic_eraser,
                toolType = ToolType.ERASER
        )
    }

    private fun getFilterTool(): ToolModel {
        return ToolModel(
                toolName = "Filter",
                toolIcon = R.drawable.ic_photo_filter,
                toolType = ToolType.FILTER
        )
    }

    private fun getEmojiTool(): ToolModel {
        return ToolModel(
                toolName = "Emoji",
                toolIcon = R.drawable.ic_insert_emoticon,
                toolType = ToolType.EMOJI
        )
    }

    private fun getStickerTool(): ToolModel {
        return ToolModel(
                toolName = "Sticker",
                toolIcon = R.drawable.ic_sticker,
                toolType = ToolType.STICKER
        )
    }
}