package com.example.profile

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.common.MyApplication
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.datastore.SettingDataStore
import com.example.common.datastore.settingDataStore
import com.example.common.utils.FragmentStackUtil
import com.example.common.viewmodel.OpenAISettingViewModel
import com.example.profile.databinding.FragmentOpenAiSettingBinding
import com.therouter.router.Route
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Route(
    path = RoutePathConstant.ProfileOpenAISettingFragment,
    description = "The entrance fragment of profile openai setting"
)
class OpenAISettingFragment : BaseFragment<FragmentOpenAiSettingBinding>(FragmentOpenAiSettingBinding::inflate) {
    private lateinit var settingDataStore: DataStore<Preferences>
    private lateinit var viewModel: OpenAISettingViewModel
    override fun onCreateView() {
        initObject()
        initObserver()
        initToolbar()
        initClick()
    }

    private fun initObserver(){
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.getOpenAIAll().collect{
                    viewBinding.modelSpinner.setSelection(it.model.ordinal)
                    viewBinding.keyEditText.setText(it.token)
                    viewBinding.temperatureSlider.value = it.temperature.toFloat()
                    viewBinding.messageNumRangeSlider.value = it.msgNum.toFloat()
                }
            }
        }
    }

    private fun initClick(){
        viewBinding.saveButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val settingData = SettingDataStore.SettingDataParameters(
                    model = SettingDataStore.OpenAIModel.values()[viewBinding.modelSpinner.selectedItemPosition],
                    token = viewBinding.keyEditText.text.toString(),
                    temperature = viewBinding.temperatureSlider.value.toDouble(),
                    msgNum = viewBinding.messageNumRangeSlider.value.toInt()
                )
                viewModel.setOpenAIAll(
                    model = SettingDataStore.OpenAIModel.values()[viewBinding.modelSpinner.selectedItemPosition],
                    token = viewBinding.keyEditText.text.toString(),
                    temperature = viewBinding.temperatureSlider.value.toDouble(),
                    msgNum = viewBinding.messageNumRangeSlider.value.toInt()
                )
                MyApplication.setSettingData(settingData)
            }
        }

        viewBinding.modelSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun initObject(){
        viewModel = ViewModelProvider(requireActivity())[OpenAISettingViewModel::class.java]
        settingDataStore = requireContext().settingDataStore
        viewModel.setDataStore(settingDataStore)
        ArrayAdapter.createFromResource(requireContext(), R.array.model_options, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            viewBinding.modelSpinner.adapter = adapter
        }
    }

    private fun initToolbar(){
        viewBinding.openaiSettingToolBarTitle.text = "OpenAI Setting"
        viewBinding.openaiSettingToolBarTitle.gravity = Gravity.CENTER

        viewBinding.toolbarOpenaiSetting.navigationIcon = resources.getDrawable(com.example.common.R.drawable.baseline_arrow_back_24)
        viewBinding.toolbarOpenaiSetting.setNavigationOnClickListener {
            FragmentStackUtil.goBack()
        }
    }
}