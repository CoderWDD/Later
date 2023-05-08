package com.example.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.profile.databinding.FragmentOpenAiSettingBinding
import com.therouter.router.Route

@Route(
    path = RoutePathConstant.ProfileOpenAISettingFragment,
    description = "The entrance fragment of profile openai setting"
)
class OpenAISettingFragment : BaseFragment<FragmentOpenAiSettingBinding>(FragmentOpenAiSettingBinding::inflate) {
    override fun onCreateView() {
        initObject()
        initClick()
    }

    private fun initClick(){

    }

    private fun initObject(){
        ArrayAdapter.createFromResource(requireContext(), R.array.model_options, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            viewBinding.modelSpinner.adapter = adapter
        }
        viewBinding.modelSpinner.setOnItemClickListener { parent, view, position, id ->
            when (position){
                0 -> {}
            }
        }


        viewBinding.saveButton.setOnClickListener {

        }
    }
}