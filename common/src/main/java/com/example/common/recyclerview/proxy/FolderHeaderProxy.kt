package com.example.common.recyclerview.proxy

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.common.R
import com.example.common.recyclerview.RVProxy

class FolderHeaderProxy : RVProxy<FolderHeaderData, FolderHeaderViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.folder_header, parent, false)
        return FolderHeaderViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: FolderHeaderViewHolder,
        data: FolderHeaderData,
        index: Int,
        action: ((Any?) -> Unit)?
    ) {
        holder.apply {
            titleText.text = data.title
            iconImage.setImageDrawable(data.icon)
        }
    }
}

data class FolderHeaderData(
    val title: String,
    val icon: Drawable?
)

class FolderHeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    var titleText: TextView
    var iconImage: ImageView
    init {
        titleText = itemView.findViewById(R.id.folder_header_title)
        iconImage = itemView.findViewById(R.id.folder_header_icon)
    }
}