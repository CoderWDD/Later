package com.example.common.recyclerview.proxy

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.common.databinding.LaterItemWebsiteBinding
import com.example.common.entity.LaterViewItem
import com.example.common.recyclerview.RVProxy

class LaterWebsiteItemCardProxy: RVProxy<LaterViewItem, LaterWebsiteItemCardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LaterItemWebsiteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LaterWebsiteItemCardViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: LaterWebsiteItemCardViewHolder,
        data: LaterViewItem,
        index: Int,
        action: ((Any?) -> Unit)?
    ) {
        holder.apply {
            titleText.text = data.title
            tagText.text = data.tag.joinToString(", ")
            timeText.text = data.updateTime.toString()
            contentText.text = data.content
            starIcon.setOnClickListener {
//                action?.invoke(data)
            }
            shareIcon.setOnClickListener {
//                action?.invoke(data)
            }
            moreIcon.setOnClickListener {
//                action?.invoke(data)
            }
        }
    }
}

class LaterWebsiteItemCardViewHolder(itemBinding: LaterItemWebsiteBinding): RecyclerView.ViewHolder(itemBinding.root){
    var titleText: TextView
    var tagText: TextView
    var timeText: TextView
    var contentText: TextView
    var starIcon: ImageView
    var shareIcon: ImageView
    var moreIcon: ImageView
    init {
        titleText = itemBinding.itemTitle
        tagText = itemBinding.itemTags
        timeText = itemBinding.itemUpdateTime
        contentText = itemBinding.itemContent
        starIcon = itemBinding.itemFavorite
        shareIcon = itemBinding.itemShare
        moreIcon = itemBinding.itemMore
    }
}