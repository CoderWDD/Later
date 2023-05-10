package com.example.common.recyclerview.proxy

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.common.databinding.LaterItemImageBinding
import com.example.common.entity.LaterViewImageItem
import com.example.common.recyclerview.RVProxy

class LaterImageItemCardProxy: RVProxy<LaterViewImageItem, LaterImageItemCardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LaterItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LaterImageItemCardViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: LaterImageItemCardViewHolder,
        data: LaterViewImageItem,
        index: Int,
        action: ((Any?) -> Unit)?
    ) {
        holder.apply {
            titleText.text = data.title
            tagText.text = data.tag.joinToString(", ")
            timeText.text = data.updateTime.toString()
            contentText.text = data.content
            starIcon.setOnClickListener {

            }
            shareIcon.setOnClickListener {

            }
            moreIcon.setOnClickListener {

            }
        }
    }
}

class LaterImageItemCardViewHolder(itemBinding: LaterItemImageBinding): RecyclerView.ViewHolder(itemBinding.root) {
    var titleText: TextView
    var tagText: TextView
    var timeText: TextView
    var contentText: TextView
    var starIcon: ImageView
    var shareIcon: ImageView
    var moreIcon: ImageView
    var image: ImageView
    init {
        titleText = itemBinding.itemTitle
        tagText = itemBinding.itemTags
        timeText = itemBinding.itemUpdateTime
        contentText = itemBinding.itemContent
        starIcon = itemBinding.itemFavoriteImage
        shareIcon = itemBinding.itemShareImage
        moreIcon = itemBinding.itemMoreImage
        image = itemBinding.itemImage
    }
}