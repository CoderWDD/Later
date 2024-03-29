package com.example.calendar

import android.view.Gravity
import android.view.View
import android.widget.CheckBox
import androidx.lifecycle.ViewModelProvider
import com.example.calendar.databinding.FragmentCalenderBinding
import com.example.calendar.viewmodel.TodoViewModel
import com.example.common.adapter.RecyclerViewAdapter
import com.example.common.callback.AddTodoItemDialogClickCallBack
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.dialogs.AddTodoDialogFragment
import com.example.common.dialogs.showDeleteDialog
import com.example.common.entity.TodoItem
import com.example.common.entity.TodoState
import com.example.common.extents.hideLoadingView
import com.example.common.extents.scheduleNotification
import com.example.common.extents.showLoadingView
import com.example.common.log.LaterLog
import com.example.common.recyclerview.RVProxy
import com.example.common.recyclerview.proxy.TodoItemProxy
import com.example.common.recyclerview.setOnItemClickListener
import com.example.common.recyclerview.setOnItemLongClickListener
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
    private var curDate = ""

    override fun onCreateView() {
        initObject()
        initCalendarClickListener()
        initFloatingButtonClickListener()
        initToolbar()
        initTodoRecyclerView()
        initTodoListClickListener()
        getData()
    }

    private fun initFloatingButtonClickListener(){
        viewBinding.todoFab.setOnClickListener {
            val time = viewBinding.todoCalendar.calendarView.date
            val date = DateUtil.longToDateString(time, "yyyy-M-d")
            AddTodoDialogFragment.newInstance(object : AddTodoItemDialogClickCallBack<TodoItem> {
                override fun onConfirmClickListener(todoItem: TodoItem, date: String) {
                    viewModel.createTodoItem(todoItem = todoItem, date = date).observe(viewLifecycleOwner) {resource ->
                        when(resource){
                            is Resource.Success -> {
                                requireActivity().hideLoadingView()
                                // 加入到定时提醒通知中
                                requireActivity().scheduleNotification(todoItem)
                            }
                            is Resource.Loading-> {
                                // show the loading view
                                requireActivity().showLoadingView()
                            }
                            is Resource.Cached -> {
                                requireActivity().hideLoadingView()
                            }
                            else -> {
                                requireActivity().hideLoadingView()
                            }
                        }
                    }
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
        val date = DateUtil.longToDateString(timeStamp, "yyyy-M-d")
        getTodoListByDate(date)
    }

    private fun getTodoListByDate(date: String){
        curDate = date
        viewModel.getTodoListByDate(date).observe(viewLifecycleOwner) {resource ->
            when(resource){
                is Resource.Success -> {
                    requireActivity().hideLoadingView()
                    todoList.clear()
                    todoList.addAll(resource.data)
                    todoAdapter.notifyDataSetChanged()
                }
                is Resource.Loading-> {
                    // show the loading view
                    requireActivity().showLoadingView()
                }
                is Resource.Cached -> {
                    requireActivity().hideLoadingView()
                }
                else -> {
                    requireActivity().hideLoadingView()
                }
            }
        }
    }

    private fun initToolbar(){
        viewBinding.todoToolBarTitle.text = "待办"
        viewBinding.todoToolBarTitle.gravity = Gravity.CENTER
    }

    private fun initTodoRecyclerView(){
        viewBinding.todoList.todoListRecyclerView.adapter = todoAdapter
    }

    private fun initTodoListClickListener(){
        viewBinding.todoList.todoListRecyclerView.setOnItemClickListener { view, position ->
            // todo check if the item is a later todo item, and if it is, open the later todo item page
        }

        viewBinding.todoList.todoListRecyclerView.setOnItemClickListener { view, position, fl, fl2 ->
            view.findViewById<CheckBox>(com.example.common.R.id.todo_checkbox).setOnCheckedChangeListener { buttonView, isChecked ->
                val todoItem = todoList[position] as TodoItem
                todoItem.state = if (isChecked) TodoState.DONE else TodoState.TODO
                viewModel.updateTodoItem(date = curDate, todoItem).observe(viewLifecycleOwner) {resource ->
                    when(resource){
                        is Resource.Success -> {
                            requireActivity().hideLoadingView()
                            LaterLog.d("update todo item success")
                        }
                        is Resource.Loading-> {
                            // show the loading view
                            requireActivity().showLoadingView()
                        }
                        is Resource.Cached -> {
                            requireActivity().hideLoadingView()
                        }
                        else -> {
                            requireActivity().hideLoadingView()
                        }
                    }
                }
            }
        }

        viewBinding.todoList.todoListRecyclerView.setOnItemLongClickListener{ _, position ->
            showDeleteDialog(requireContext(), "删除", "确定删除该待办事项吗？", positiveText = "确定", negativeText = "取消", negativeListener = {}, positiveListener = {
                val todoItem = todoList[position] as TodoItem
                viewModel.deleteTodoItem(date = curDate, todoItem).observe(viewLifecycleOwner) {resource ->
                    when(resource){
                        is Resource.Success -> {
                            requireActivity().hideLoadingView()
                            LaterLog.d("delete todo item success")
                        }
                        is Resource.Loading-> {
                            // show the loading view
                            requireActivity().showLoadingView()
                        }
                        is Resource.Cached -> {
                            requireActivity().hideLoadingView()
                        }
                        else -> {
                            requireActivity().hideLoadingView()
                        }
                    }
                }
            })
        }
    }
}