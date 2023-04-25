package com.example.calendar

import android.view.Gravity
import androidx.lifecycle.ViewModelProvider
import com.example.calendar.databinding.FragmentCalenderBinding
import com.example.calendar.viewmodel.TodoViewModel
import com.example.common.adapter.RecyclerViewAdapter
import com.example.common.callback.AddTodoItemDialogClickCallBack
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.dialogs.AddTodoDialogFragment
import com.example.common.entity.TodoItem
import com.example.common.recyclerview.RVProxy
import com.example.common.recyclerview.proxy.TodoItemProxy
import com.example.common.reporesource.Resource
import com.example.common.utils.DateUtil
import com.therouter.router.Route

@Route(
    path = RoutePathConstant.CalendarFragment,
    description = "The entrance fragment of calendar"
)
class CalenderFragment : BaseFragment<FragmentCalenderBinding>(FragmentCalenderBinding::inflate) {
    private lateinit var viewModel: TodoViewModel
    private val todoList = mutableListOf<Any>()
    private lateinit var todoAdapter: RecyclerViewAdapter

    override fun onCreateView() {
        initObject()
        initCalendarClickListener()
        initFloatingButtonClickListener()
        initToolbar()
        initTodoRecyclerView()
        getData()
    }

    private fun initFloatingButtonClickListener(){
        viewBinding.todoFab.setOnClickListener {
            val time = viewBinding.todoCalendar.calendarView.date
            val date = DateUtil.longToDateString(time, "yyyy-MM-dd")
            AddTodoDialogFragment.newInstance(object : AddTodoItemDialogClickCallBack<TodoItem> {
                override fun onConfirmClickListener(todoItem: TodoItem, date: String) {
                    viewModel.createTodoItem(todoItem = todoItem, date = date)
                }
            }, date).show(requireActivity().supportFragmentManager, "addTodoDialog")
        }
    }

    private fun initObject(){
        viewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]
        val todoItemProxy = mutableListOf<RVProxy<*, *>>(TodoItemProxy())
        todoAdapter = RecyclerViewAdapter(dataList = todoList, proxyList = todoItemProxy)
    }

    private fun initCalendarClickListener(){
        viewBinding.todoCalendar.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val date = "$year-${month + 1}-$dayOfMonth"
            getTodoListByDate(date)
        }
    }

    private fun getData(){
        val timeStamp = viewBinding.todoCalendar.calendarView.date
        val date = DateUtil.longToDateString(timeStamp, "yyyy-MM-dd")
        getTodoListByDate(date)
    }

    private fun getTodoListByDate(date: String){
        viewModel.getTodoListByDate(date).observe(viewLifecycleOwner) {resource ->
            when(resource){
                is Resource.Success -> {
                    todoList.clear()
                    todoList.addAll(resource.data)
                    todoAdapter.notifyDataSetChanged()
                }
                is Resource.Loading-> {
                    // todo show the loading view
                }
                is Resource.Cached -> {

                }
                else -> {}
            }
        }
    }

    private fun initToolbar(){
        viewBinding.todoToolBarTitle.text = "待办"
        viewBinding.todoToolBarTitle.gravity = Gravity.CENTER
    }

    private fun initTodoRecyclerView(){

    }
}