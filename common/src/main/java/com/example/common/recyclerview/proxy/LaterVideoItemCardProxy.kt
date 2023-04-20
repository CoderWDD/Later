package com.example.common.recyclerview.proxy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.common.databinding.LaterItemVideoBinding
import com.example.common.entity.LaterViewItem
import com.example.common.recyclerview.RVProxy

class LaterVideoItemCardProxy: RVProxy<LaterViewItem, LaterVideoItemCardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LaterItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LaterVideoItemCardViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: LaterVideoItemCardViewHolder,
        data: LaterViewItem,
        index: Int,
        action: ((Any?) -> Unit)?
    ) {
        // TODO set the data to the viewHolder widget
    }

}

class LaterVideoItemCardViewHolder(itemBinding: LaterItemVideoBinding): RecyclerView.ViewHolder(itemBinding.root) {
    // TODO get the viewHolder widget from itemBinding
    init {

    }
}