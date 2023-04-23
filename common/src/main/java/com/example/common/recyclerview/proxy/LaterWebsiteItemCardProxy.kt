package com.example.common.recyclerview.proxy

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.common.R
import com.example.common.databinding.LaterItemWebsiteBinding
import com.example.common.entity.LaterViewItem
import com.example.common.entity.LaterViewWebPageItem
import com.example.common.log.LaterLog
import com.example.common.recyclerview.RVProxy

class LaterWebsiteItemCardProxy: RVProxy<LaterViewWebPageItem, LaterWebsiteItemCardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        LaterLog.d("onCreateViewHolder: $viewType")
        val binding = LaterItemWebsiteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LaterWebsiteItemCardViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: LaterWebsiteItemCardViewHolder,
        data: LaterViewWebPageItem,
        index: Int,
        action: ((Any?) -> Unit)?
    ) {
        holder.apply {
            titleText.text = data.title
            tagText.text = data.tag.joinToString(", ")
            timeText.text = data.updateTime.toString()
            contentText.text = data.content
            starIcon.isSelected = data.isStar
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