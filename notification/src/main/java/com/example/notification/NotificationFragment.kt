package com.example.notification

import android.view.Gravity
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.common.adapter.RecyclerViewAdapter
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.log.LaterLog
import com.example.common.recyclerview.RVProxy
import com.example.common.recyclerview.proxy.ConversationItemProxy
import com.example.common.recyclerview.setOnItemClickListener
import com.example.common.room.entities.ConversationEntity
import com.example.common.utils.FragmentStackUtil
import com.example.common.utils.TheRouterUtil
import com.example.notification.databinding.FragmentNotificationBinding
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
                LaterLog.d("conversationListFromDB: $conversationListFromDB", tag = "loadConversationList")
                conversationList.addAll(conversationListFromDB)
                conversationRVAdapter.addDataList(conversationList)
            }
        }
    }

    private fun initClickListener(){
        viewBinding.chatFab.setOnClickListener {
            // get the current time as the conversation name
            val conversationName = System.currentTimeMillis().toString()
            // create a new chat
            viewLifecycleOwner.lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    val conversationId = viewModel.createConversation(conversationName)
                    viewModel.conversationId = conversationId
                    // switch to a new chat fragment
                    // 跳转到相应页面
                    val chatMessageFragment = TheRouter.build(RoutePathConstant.ChatMessageFragment)
                        .withString("conversationId", conversationId.toString())
                        .withString("conversationName", conversationName)
                        .createFragment<ChatMessageFragment>()
                    if (chatMessageFragment != null) {
                        FragmentStackUtil.addFragment(chatMessageFragment)
                    }
                }
            }
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

        viewBinding.chatList.setOnItemClickListener { view, position, fl, fl2 ->
            view.findViewById<ImageView>(com.example.common.R.id.delete_icon).setOnClickListener {
                // delete the item
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.deleteConversation(conversationList[position] as ConversationEntity)
                    conversationList.removeAt(position)
                    conversationRVAdapter.notifyItemRemoved(position)
                }
            }

            view.findViewById<ImageView>(com.example.common.R.id.edit_icon).setOnClickListener {icon ->
                icon.visibility = ImageView.GONE
                view.findViewById<ImageView>(com.example.common.R.id.finish_icon).visibility = ImageView.VISIBLE
            }

            view.findViewById<ImageView>(com.example.common.R.id.finish_icon).setOnClickListener {
                it.visibility = ImageView.GONE
                view.findViewById<ImageView>(com.example.common.R.id.edit_icon).visibility = ImageView.VISIBLE
            }
        }
    }

    private fun initObject(){
        viewModel = ViewModelProvider(this)[MsgViewModel::class.java]
        val conversationItemProxy = mutableListOf<RVProxy<*, *>>(ConversationItemProxy())
        conversationRVAdapter = RecyclerViewAdapter(conversationItemProxy)
        viewBinding.chatList.adapter = conversationRVAdapter
    }

    private fun initToolbar(){
        viewBinding.msgToolbarTitle.text = "消息"
        viewBinding.msgToolbarTitle.gravity = Gravity.CENTER
    }
}