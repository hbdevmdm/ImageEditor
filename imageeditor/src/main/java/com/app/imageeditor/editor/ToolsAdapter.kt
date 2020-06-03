package com.app.imageeditor.editor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.imageeditor.R
import kotlinx.android.synthetic.main.list_item_editor_tool.view.*

class ToolsAdapter(private val toolList: ArrayList<ToolModel>, val listener: (ToolModel) -> Unit) : RecyclerView.Adapter<ToolsAdapter.ToolViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ToolViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.list_item_editor_tool,parent,false))

    override fun getItemCount(): Int {
        return toolList.size
    }

    override fun onBindViewHolder(holder: ToolViewHolder, position: Int) {
        holder.bind(toolList[position],listener)
    }

    class ToolViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(item: ToolModel, listener: (ToolModel) -> Unit) = with(itemView) {
            tvToolName.text = item.toolName
            imageToolIcon.setImageResource(item.toolIcon)
            setOnClickListener { listener(item) }
        }
    }
}