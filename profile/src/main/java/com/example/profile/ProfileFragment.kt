package com.example.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.profile.databinding.FragmentProfileBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.therouter.router.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@Route(
    path = RoutePathConstant.ProfileFragment,
    description = "The entrance fragment of profile"
)
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var viewModel: ProfileViewModel
    private var currentUser: FirebaseUser? = null
    private var mIsLogin = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 获取 viewModel
        viewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        firebaseAuth = FirebaseAuth.getInstance()
        // 判断是否已经登录
        currentUser = firebaseAuth.currentUser
        currentUser?.let {
            viewModel.setLoginState(true)
        }
    }

    override fun onCreateView() {
        initView()

        viewBinding.avatar.setOnClickListener {
            if (!mIsLogin) {
                LoginFragment().show(requireActivity().supportFragmentManager, "login")
            }
        }

        // 在 lifecycle 的生命周期中，监听 viewModel 的 loginState，避免内存泄漏
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginState.collect { isLogin ->
                mIsLogin = isLogin
                when {
                    isLogin -> {
                        currentUser = firebaseAuth.currentUser
                        currentUser?.let { user ->
                            viewBinding.username.text = user.phoneNumber
                            viewBinding.email.text = user.email
                        }
                    }
                    else -> {
                        viewBinding.username.text = "未登录"
                        viewBinding.email.text = "点击头像登录"
                    }
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initView(){
        Glide
            .with(this)
            .load(resources.getDrawable(R.drawable.user_man_placeholder, resources.newTheme()))
            .transform(CircleCrop())
            .into(viewBinding.avatar)
    }
}