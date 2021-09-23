package com.app.imageeditor

import android.util.Pair

/**
 * Image editor configuration class is used to customize image editor options
 */
class ImageEditorViewOptions {
    var imageUri = ""
    var outPutPath = ""
    var outPutHeight = 0
    var outPutWidth = 0
    var isCropOnly = false
    var cropShape = BaseActivity.CROP_SHAPE_RECT
    var aspectRatio = Pair(1, 1)
    var fixAspectRatio = false
    var selectedTools = ArrayList<String>()
    var stickerImages = ArrayList<Int>() // This will be drawable ids
}