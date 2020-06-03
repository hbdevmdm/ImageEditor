package com.app.imageeditor.editor.filter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.imageeditor.R
import ja.burhanrashid52.photoeditor.PhotoFilter
import kotlinx.android.synthetic.main.list_item_filter.view.*
import java.io.IOException
import java.io.InputStream

class FilterAdapter(private val filterList: ArrayList<Pair<String, PhotoFilter>>, private val listener:(PhotoFilter) -> Unit):RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        FilterViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_filter, parent, false
            )
        )

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(filterList[position], listener)
    }

    class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
       fun bind(item:Pair<String, PhotoFilter>, listener: (PhotoFilter) -> Unit) = with(itemView){
            itemView.imgFilterView.setImageBitmap(getBitmapFromAsset(itemView.context, item.first))
            itemView.txtFilterName.text = item.second.name.replace("_"," ")
           setOnClickListener { listener(item.second) }
       }

        private fun getBitmapFromAsset(
            context: Context,
            strName: String
        ): Bitmap? {
            val assetManager = context.assets
            var istr: InputStream? = null
            return try {
                istr = assetManager.open(strName)
                BitmapFactory.decodeStream(istr)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

}