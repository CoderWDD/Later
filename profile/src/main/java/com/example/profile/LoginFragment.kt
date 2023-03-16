package com.example.profile

import android.app.Dialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import com.example.profile.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : BottomSheetDialogFragment() {
    private lateinit var viewBinding: FragmentLoginBinding
    private lateinit var activityLauncher: ActivityResultLauncher<Intent>
    private lateinit var viewModel: ProfileViewModel
    private lateinit var firebaseAuth: FirebaseAuth

    private enum class LoginType(val num: Int) {
        GOOGLE(1),
        FACEBOOK(2),
        TWITTER(3),
        GITHUB(4),
        EMAIL(5),
        PHONE(6)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 获取 viewModel
        viewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        firebaseAuth = FirebaseAuth.getInstance()

        activityLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){ result ->
            // todo handle result
            when (result.resultCode) {
                LoginType.GOOGLE.num -> {
                    val accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    handleGoogleSignInResult(accountTask)
                }
                LoginType.FACEBOOK.num -> {}
                LoginType.TWITTER.num -> {}
                LoginType.GITHUB.num -> {}
                LoginType.EMAIL.num -> {}
                LoginType.PHONE.num -> {}
            }
        }
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentLoginBinding.inflate(inflater, container, false)

        initView()
        initClickListener()

        return viewBinding.root
    }

    private fun initClickListener(){
        // 设置账号密码登录按钮点击事件
        viewBinding.btnLogin.setOnClickListener {
            if (viewBinding.etEmail.text.isNullOrBlank() || viewBinding.etPassword.text.isNullOrBlank()) {
                Snackbar.make(viewBinding.root, "请输入账号密码", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val email = viewBinding.etEmail.text.toString()
            val password = viewBinding.etPassword.text.toString()
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    viewModel.setLoginState(true)
                    dismiss()
                }
                .addOnFailureListener {
                    viewModel.setLoginState(false)
                }
        }

        // 设置 google 登录按钮点击事件
        viewBinding.ibGoogle.setOnClickListener {
            val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(com.example.common.R.string.google_server_client_id))
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
            val signInIntent: Intent = googleSignInClient.signInIntent
            activityLauncher.launch(signInIntent)
        }

        // 设置 facebook 登录按钮点击事件
        viewBinding.ibFacebook.setOnClickListener {
            // todo
        }

        // 设置 signup 文字点击事件
        viewBinding.tvSignup.setOnClickListener {
            RegisterFragment().show(requireActivity().supportFragmentManager, "register")
        }
    }

    private fun initView(){
        viewBinding.tvSignup.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        viewBinding.tvSignup.paint.isAntiAlias = true
    }

    private fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        task.addOnSuccessListener {
            viewModel.setLoginState(true)
            dismiss()
        }
            .addOnFailureListener { viewModel.setLoginState(false) }
            .addOnCanceledListener {  }
            .addOnCompleteListener {  }
    }
}