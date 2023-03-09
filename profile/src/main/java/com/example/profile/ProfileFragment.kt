package com.example.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.profile.databinding.FragmentProfileBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthCredential
import com.therouter.router.Route

@Route(
    path = RoutePathConstant.ProfileFragment,
    description = "The entrance fragment of profile"
)
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView() {
        viewBinding.button.setOnClickListener {
        }
    }

}