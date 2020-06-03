package com.app.imageeditor.editor.emoji

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.imageeditor.R
import kotlinx.android.synthetic.main.list_item_emoji.view.*

/**
 * This adapter is used to show available emojis.
 */
class EmojiAdapter(private val emojiList:ArrayList<String>, private var listener: (String) -> Unit) : RecyclerView.Adapter<EmojiAdapter.EmojiVewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= EmojiVewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_emoji, parent, false
        )
    )

    override fun getItemCount(): Int {
        return emojiList.size
    }

    override fun onBindViewHolder(holder: EmojiAdapter.EmojiVewHolder, position: Int) {
        holder.bind(emojiList[position],listener)
    }

    class EmojiVewHolder(viewItem: View):RecyclerView.ViewHolder(viewItem){
        fun bind(item: String, listener:(String) -> Unit) = with(itemView){
            itemView.tvEmoji.text = item
            setOnClickListener{listener(item)}
        }
    }
}