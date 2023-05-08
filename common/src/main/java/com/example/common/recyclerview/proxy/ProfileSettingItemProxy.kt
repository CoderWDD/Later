package com.example.common.recyclerview.proxy

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.common.databinding.SettingItemBinding
import com.example.common.entity.ProfileSettingData
import com.example.common.recyclerview.RVProxy

class ProfileSettingItemProxy: RVProxy<ProfileSettingData, ProfileSettingItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = SettingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileSettingItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ProfileSettingItemViewHolder,
        data: ProfileSettingData,
        index: Int,
        action: ((Any?) -> Unit)?
    ) {
        holder.apply {
            data.icon?.let { icon.setImageDrawable(it) }
            title.text = data.title
        }
    }
}

class ProfileSettingItemViewHolder(itemViewBinding: SettingItemBinding) : RecyclerView.ViewHolder(itemViewBinding.root){
    val icon: ImageView
    val title: TextView
    init {
        icon = itemViewBinding.settingIcon
        title = itemViewBinding.settingTitle
    }
}