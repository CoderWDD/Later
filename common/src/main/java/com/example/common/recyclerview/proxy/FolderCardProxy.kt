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

class FolderCardProxy: RVProxy<FolderData, FolderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.folder_card, parent, false)
        return FolderViewHolder(view);
    }

    override fun onBindViewHolder(
        holder: FolderViewHolder,
        data: FolderData,
        index: Int,
        action: ((Any?) -> Unit)?
    ) {
        holder.apply {
            titleText.text = data.title
            iconImage.setImageDrawable(data.icon)
            cntText.text = data.cnt
        }
    }
}

data class FolderData(
    val title: String,
    val cnt: String,
    val icon: Drawable? = null
)

class FolderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    var iconImage: ImageView
    var titleText: TextView
    var cntText: TextView
    init {
        iconImage = itemView.findViewById(R.id.category_icon)
        titleText = itemView.findViewById(R.id.category_text)
        cntText = itemView.findViewById(R.id.category_cnt)
    }

}