package com.example.common.dialogs

import android.content.Context
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.AbsoluteLayout.LayoutParams
import android.widget.EditText
import androidx.core.view.marginBottom
import androidx.core.view.setMargins
import com.example.common.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder


fun showDeleteDialog(context: Context, title: String, content: String, positiveText: String, negativeText: String, positiveListener: () -> Unit, negativeListener: () -> Unit) {
    val dialog = MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(content)
        .setPositiveButton(positiveText) { dialog, which ->
            positiveListener()
            dialog.dismiss()
        }
        .setNegativeButton(negativeText) { dialog, which ->
            negativeListener()
            dialog.dismiss()
        }
        .create()
    dialog.show()
}

// todo 修改inputView的外边距
fun showAlertDialog(context: Context, title: String, content: String, positiveText: String, negativeText: String, positiveListener: (inputText: String) -> Unit, negativeListener: () -> Unit) {
    val inputView = EditText(context).apply {
        inputType = android.text.InputType.TYPE_CLASS_TEXT
        background = context.getDrawable(R.drawable.edittext_background)
        hint = "Conversation Name"
        maxLines = 1
        isSingleLine = true
        val layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt()
        layoutParams.setMargins(margin)
        this.layoutParams = layoutParams
        val paddingHorizontal =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics)
                .toInt()
        val paddingVertical =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics)
                .toInt()
        setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
        setTextAppearance(com.google.android.material.R.style.TextAppearance_MaterialComponents_Body1)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
    }
    val dialog = MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(content)
        .setView(inputView)
        .setPositiveButton(positiveText) { dialog, which ->
            var input = inputView.text.toString()
            if (input.isEmpty()) input = "Default Conversation"
            positiveListener(input)
            dialog.dismiss()
        }
        .setNegativeButton(negativeText) { dialog, which ->
            negativeListener()
            dialog.dismiss()
        }
        .create()
    dialog.show()
}