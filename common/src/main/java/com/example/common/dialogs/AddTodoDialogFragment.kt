package com.example.common.dialogs

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.EditText
import android.widget.Toast
import com.example.common.R
import com.example.common.callback.AddTodoItemDialogClickCallBack
import com.example.common.custom.BaseDialogFragment
import com.example.common.databinding.FragmentAddTodoDialogBinding
import com.example.common.entity.TodoItem
import com.example.common.extents.showToast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.properties.Delegates

class AddTodoDialogFragment : BaseDialogFragment<FragmentAddTodoDialogBinding>(FragmentAddTodoDialogBinding::inflate) {
    private var customDialogCallback: AddTodoItemDialogClickCallBack<TodoItem>? = null
    private var date: String = ""
    private var startTime = 0L
    private var endTime = 0L

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
        initTimeText()
    }

    private fun initTimeText(){
        val oneHour = 60 * 60 * 1000
        val currentTime = Date()
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        startTime = currentTime.time
        endTime = currentTime.time + oneHour
        viewBinding.todoDialogTextDate.text = date
        viewBinding.todoDialogTextStartTime.text = dateFormat.format(currentTime)
        viewBinding.todoDialogTextEndTime.text = dateFormat.format(currentTime.time + oneHour)
    }

    override fun initClickListener() {

        viewBinding.todoDialogTextDate.setOnClickListener {
            // show date picker dialog
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(), R.style.TodoDatePickerStyle, { _, selectedYear, selectedMonth, selectedDay ->
                // Code to handle the selected date
                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                viewBinding.todoDialogTextDate.text = selectedDate
            }, year, month, day)
            datePickerDialog.show()
        }

        viewBinding.todoDialogTextStartTime.setOnClickListener {
            // show time picker dialog

            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(requireContext(), R.style.TodoTimePickerStyle, { _, selectedHour, selectedMinute ->
                // Code to handle the selected time
                calendar.apply {
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                startTime = calendar.timeInMillis
                val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                viewBinding.todoDialogTextStartTime.text = dateFormat.format(startTime)
            }, hour, minute, true)
            timePickerDialog.show()
        }

        viewBinding.todoDialogTextEndTime.setOnClickListener {
            // show time picker dialog
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(requireContext(), R.style.TodoTimePickerStyle, { _, selectedHour, selectedMinute ->
                // Code to handle the selected time
                calendar.apply {
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                endTime = calendar.timeInMillis
                val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                viewBinding.todoDialogTextEndTime.text = dateFormat.format(endTime)
            }, hour, minute, true)
            timePickerDialog.show()
        }

        viewBinding.todoDialogButton.setOnClickListener {
            // check if the time is valid
            if (startTime >= endTime){
                showToast("Start time should be earlier than end time")
                return@setOnClickListener
            }

            // check if the title is empty
            if (viewBinding.todoDialogEditTextTitle.text.toString().isEmpty()){
                showToast("Title should not be empty")
                return@setOnClickListener
            }
            val title = viewBinding.todoDialogEditTextTitle.text.toString()
            val description = viewBinding.todoDialogEditTextDescription.text.toString()
            val todoItem = TodoItem(title = title, description = description, startTime = startTime, endTime = endTime)
            customDialogCallback?.onConfirmClickListener(todoItem, date)
            dismiss()
        }
    }

    override fun focusWidget(): EditText {
        return viewBinding.todoDialogEditTextTitle
    }
}