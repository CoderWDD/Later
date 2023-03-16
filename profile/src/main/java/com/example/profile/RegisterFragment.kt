package com.example.profile

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.profile.databinding.FragmentRegisterBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : BottomSheetDialogFragment() {
    private lateinit var viewBinding: FragmentRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentRegisterBinding.inflate(inflater, container, false)
        initClickListener()
        // Inflate the layout for this fragment
        return viewBinding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val layoutParams = bottomSheet.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            bottomSheet.layoutParams = layoutParams
        }
        return dialog
    }

    private fun initClickListener(){
        viewBinding.btnSignup.setOnClickListener {
            if (viewBinding.etEmail.text.isNullOrBlank() || viewBinding.etPassword.text.isNullOrBlank()) {
                Snackbar.make(viewBinding.root, "请输入账号密码", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // 通过 firebaseAuth 创建用户
            val email = viewBinding.etEmail.text.toString()
            val password = viewBinding.etPassword.text.toString()
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    when {
                        task.isSuccessful -> {
                            Snackbar.make(viewBinding.root, "注册成功", Snackbar.LENGTH_SHORT).show()
                            dismiss()
                        }
                        else -> {
                            Snackbar.make(viewBinding.root, "注册失败", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
        }
    }
}