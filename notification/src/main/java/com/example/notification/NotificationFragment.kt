package com.example.notification

import android.view.Gravity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.common.adapter.RecyclerViewAdapter
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.custom.RecyclerViewItemDecoration
import com.example.common.dialogs.showAlertDialog
import com.example.common.dialogs.showDeleteDialog
import com.example.common.extents.dp
import com.example.common.recyclerview.RVProxy
import com.example.common.recyclerview.proxy.ConversationItemProxy
import com.example.common.recyclerview.setOnItemClickListener
import com.example.common.recyclerview.setOnItemLongClickListener
import com.example.common.room.entities.ConversationEntity
import com.example.common.utils.FragmentStackUtil
import com.example.notification.databinding.FragmentNotificationBinding
import com.example.notification.recyclerview.ConversationDividerItemDecoration
import com.example.notification.viewmodel.MsgViewModel
import com.therouter.TheRouter
import com.therouter.router.Route
import kotlinx.coroutines.launch

@Route(
    path = RoutePathConstant.NotificationFragment,
    description = "The entrance fragment of notification"
)
class NotificationFragment : BaseFragment<FragmentNotificationBinding>(FragmentNotificationBinding::inflate) {
    private lateinit var viewModel: MsgViewModel
    private lateinit var conversationRVAdapter: RecyclerViewAdapter
    private val conversationList = mutableListOf<Any>()

    override fun onCreateView() {
        initObject()
        initToolbar()
        initClickListener()
        loadConversationList()
    }

    private fun loadConversationList(){
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val conversationListFromDB = viewModel.getConversationList()
                conversationList.addAll(conversationListFromDB)
                conversationRVAdapter.addDataList(conversationList)
            }
        }
    }

    private fun initClickListener(){
        viewBinding.chatFab.setOnClickListener {
            showAlertDialog(requireContext(), "新建对话", "请输入对话名称", "确定", "取消", positiveListener = {inputText ->
                // get the current time as the conversation name
                // create a new chat
                viewLifecycleOwner.lifecycleScope.launch {
                    val conversationId = viewModel.createConversation(inputText)
                    val conversationEntity = viewModel.getConversationById(conversationId)
                    conversationList.add(0, conversationEntity)
                    conversationRVAdapter.addData(conversationEntity)
                    viewModel.conversationId = conversationId
                    // switch to a new chat fragment
                    // 跳转到相应页面
                    val chatMessageFragment = TheRouter.build(RoutePathConstant.ChatMessageFragment)
                        .withString("conversationId", conversationId.toString())
                        .withString("conversationName", inputText)
                        .createFragment<ChatMessageFragment>()
                    if (chatMessageFragment != null) {
                        FragmentStackUtil.addFragment(chatMessageFragment)
                    }
                }
            }, negativeListener = {})
        }

        viewBinding.chatList.setOnItemClickListener { _, position ->
            // switch to chat fragment
            val conversationId = (conversationList[position] as ConversationEntity).conversationId
            val conversationName = (conversationList[position] as ConversationEntity).conversationName
            val chatMessageFragment = TheRouter.build(RoutePathConstant.ChatMessageFragment)
                .withString("conversationId", conversationId.toString())
                .withString("conversationName", conversationName)
                .createFragment<ChatMessageFragment>()
            if (chatMessageFragment != null) {
                FragmentStackUtil.addFragment(chatMessageFragment)
            }
        }

        viewBinding.chatList.setOnItemLongClickListener{ _, position ->
            // 弹出删除对话框
            showDeleteDialog(context = requireContext(), title = "删除对话", content = "确定要删除对话吗？", positiveText = "确定", negativeText = "取消", positiveListener = {
                // 删除收藏夹
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.deleteConversation(conversation = conversationList[position] as ConversationEntity)
                    conversationRVAdapter.deleteData(position)
                }
            }, negativeListener = {})
        }
    }

    private fun initObject(){
        viewModel = ViewModelProvider(this)[MsgViewModel::class.java]
        val conversationItemProxy = mutableListOf<RVProxy<*, *>>(ConversationItemProxy())
        conversationRVAdapter = RecyclerViewAdapter(conversationItemProxy)
        viewBinding.chatList.adapter = conversationRVAdapter
        viewBinding.chatList.addItemDecoration(RecyclerViewItemDecoration(requireContext(), RecyclerViewItemDecoration.VERTICAL, 16.dp.toInt(), 16.dp.toInt()))

    }

    private fun initToolbar(){
        viewBinding.msgToolbarTitle.text = "消息"
        viewBinding.msgToolbarTitle.gravity = Gravity.CENTER
    }
}