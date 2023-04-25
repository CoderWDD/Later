package com.example.laterlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.common.custom.BaseDialogFragment
import com.example.common.entity.LaterViewItem
import com.example.common.callback.MenuItemDialogClickCallBack
import com.example.laterlist.databinding.FragmentCreateImageBinding

class CreateImageFragment : BaseDialogFragment<FragmentCreateImageBinding>(FragmentCreateImageBinding::inflate) {
    private var customDialogCallback: MenuItemDialogClickCallBack<LaterViewItem>? = null

    companion object{
        fun newInstance(customDialogCallback: MenuItemDialogClickCallBack<LaterViewItem>): CreateImageFragment {
            val fragment = CreateImageFragment()
            fragment.customDialogCallback = customDialogCallback
            return fragment
        }
    }

    override fun onCreateView() { }

    override fun initClickListener() {
        TODO("Not yet implemented")
    }

    override fun focusWidget(): EditText? {
        TODO("Not yet implemented")
    }
}