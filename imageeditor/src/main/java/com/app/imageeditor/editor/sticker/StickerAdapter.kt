package com.app.imageeditor.editor.sticker

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.imageeditor.R
import kotlinx.android.synthetic.main.list_item_sticker.view.*

/**
 * This is a adapter class to show sticker listing
 */
class StickerAdapter(private val stickerList:ArrayList<Int>, private val listener :(Bitmap) -> Unit):RecyclerView.Adapter<StickerAdapter.StickerViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= StickerViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_sticker, parent, false
        )
    )

    override fun getItemCount(): Int {
        return stickerList.size
    }

    override fun onBindViewHolder(holder: StickerViewHolder, position: Int) {
       holder.bind(stickerList[position], listener)
    }

    class StickerViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bind(item:Int, listener: (Bitmap) -> Unit) = with(itemView){
            itemView.imgSticker.setImageResource(item)
            setOnClickListener { listener(
                BitmapFactory.decodeResource(
                    resources,
                    item
                )) }
        }
    }

}