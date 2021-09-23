package com.app.imageeditor.editor.brush

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.imageeditor.R
import kotlinx.android.synthetic.main.color_picker_item_list.view.*

/**
 * This class is used to pick color for draw oven an image
 */
class ColorPickerAdapter(private val colorList: ArrayList<Int>, private val listener: (Int) -> Unit) : RecyclerView.Adapter<ColorPickerAdapter.ColorPickerViewModel>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    )= ColorPickerViewModel(
        LayoutInflater.from(parent.context).inflate(
            R.layout.color_picker_item_list, parent, false
        )
    )

    override fun getItemCount(): Int {
       return colorList.size
    }

    override fun onBindViewHolder(holder: ColorPickerViewModel, position: Int) {
        holder.bind(colorList[position],listener)
    }

    class ColorPickerViewModel(itemView : View) : RecyclerView.ViewHolder(itemView){
       fun bind(item: Int, listener: (Int) -> Unit) = with(itemView){
          color_picker_view.setBackgroundColor(item)
           setOnClickListener{listener(item)}
       }
    }
}