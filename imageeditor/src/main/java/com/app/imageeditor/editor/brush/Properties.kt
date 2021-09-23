package com.app.imageeditor.editor.brush

interface Properties {
    fun onColorChanged(colorCode: Int)
    fun onOpacityChanged(opacity: Int)
    fun onBrushSizeChanged(brushSize: Int)
}