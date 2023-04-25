package com.example.common.dialogs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.common.callback.AddTodoItemDialogClickCallBack
import com.example.common.callback.MenuItemDialogClickCallBack
import com.example.common.custom.BaseDialogFragment
import com.example.common.databinding.FragmentAddTodoDialogBinding
import com.example.common.entity.LaterViewItem
import com.example.common.entity.TodoItem


class AddTodoDialogFragment : BaseDialogFragment<FragmentAddTodoDialogBinding>(FragmentAddTodoDialogBinding::inflate) {
    private var customDialogCallback: AddTodoItemDialogClickCallBack<TodoItem>? = null
    private var date: String = ""

    companion object{
        fun newInstance(
            customDialogCallback: AddTodoItemDialogClickCallBack<TodoItem>,
            date: String
        ): AddTodoDialogFragment {
            val fragment = AddTodoDialogFragment()
            fragment.apply {
                this.customDialogCallback = customDialogCallback
                this.date = date
            }
            return fragment
        }
    }

    override fun onCreateView() {
        viewBinding.todoDialogTextDate.setText(date)
    }

    override fun initClickListener() {
    }

    override fun focusWidget(): EditText? {
        return viewBinding.todoDialogEditTextTitle
    }
}