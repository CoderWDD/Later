package com.example.laterlist.spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.common.log.LaterLog
import com.example.common.recyclerview.proxy.FolderData
import com.example.laterlist.R
import com.example.laterlist.databinding.FolderSpinnerItemBinding

class FolderSpinnerAdapter(context: Context, private val dataList: List<FolderData>): ArrayAdapter<FolderData>(context, R.layout.folder_spinner_item) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = FolderSpinnerItemBinding.inflate(LayoutInflater.from(context), parent, false)
        binding.folderSpinnerText.text = dataList[position].title
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = FolderSpinnerItemBinding.inflate(LayoutInflater.from(context), parent, false)
        binding.folderSpinnerText.text = dataList[position].title
        return binding.root
    }
}