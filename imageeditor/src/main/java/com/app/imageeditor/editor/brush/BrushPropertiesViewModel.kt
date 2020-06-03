package com.app.imageeditor.editor.brush

import android.app.Application
import androidx.core.content.ContextCompat
import com.app.imageeditor.BaseViewModel
import com.app.imageeditor.R

class BrushPropertiesViewModel(app: Application):BaseViewModel(app) {

    /**
     * This will return available colors for draw on an image
     */
    fun getColorList():ArrayList<Int>{
        val colorPickerColors = ArrayList<Int>()
        colorPickerColors.add(ContextCompat.getColor(app.baseContext, R.color.blue_color_picker))
        colorPickerColors.add(ContextCompat.getColor(app.baseContext, R.color.brown_color_picker))
        colorPickerColors.add(ContextCompat.getColor(app.baseContext, R.color.green_color_picker))
        colorPickerColors.add(ContextCompat.getColor(app.baseContext, R.color.orange_color_picker))
        colorPickerColors.add(ContextCompat.getColor(app.baseContext, R.color.red_color_picker))
        colorPickerColors.add(ContextCompat.getColor(app.baseContext, R.color.black))
        colorPickerColors.add(ContextCompat.getColor(app.baseContext, R.color.red_orange_color_picker))
        colorPickerColors.add(ContextCompat.getColor(app.baseContext, R.color.sky_blue_color_picker))
        colorPickerColors.add(ContextCompat.getColor(app.baseContext, R.color.violet_color_picker))
        colorPickerColors.add(ContextCompat.getColor(app.baseContext, R.color.white))
        colorPickerColors.add(ContextCompat.getColor(app.baseContext, R.color.yellow_color_picker))
        colorPickerColors.add(ContextCompat.getColor(app.baseContext, R.color.yellow_green_color_picker))
        return colorPickerColors
    }
}