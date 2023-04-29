package com.example.common.extents

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(content: String){
    Toast.makeText(this.requireContext(), content, Toast.LENGTH_SHORT).show()
}