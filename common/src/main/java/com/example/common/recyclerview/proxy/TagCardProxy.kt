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

class TagCardProxy : RVProxy<TagCardData, TagCardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.tag_card, parent, false)
        return TagCardViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TagCardViewHolder,
        data: TagCardData,
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

data class TagCardData(
    val title: String,
    val icon: Drawable? = null,
    val cnt: String
)

class TagCardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    var titleText: TextView
    var iconImage: ImageView
    var cntText: TextView
    init {
        titleText = itemView.findViewById(R.id.tag_text)
        iconImage = itemView.findViewById(R.id.tag_icon)
        cntText = itemView.findViewById(R.id.tag_cnt)
    }
}