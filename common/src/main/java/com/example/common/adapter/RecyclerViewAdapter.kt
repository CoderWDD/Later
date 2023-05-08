package com.example.common.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.common.recyclerview.RVProxy
import java.lang.reflect.ParameterizedType

// 写法参考：https://juejin.cn/post/6876967151975006221
class RecyclerViewAdapter(
    private var proxyList: MutableList<RVProxy<*, *>> = mutableListOf(),
    var dataList: MutableList<Any> = mutableListOf()
): RecyclerView.Adapter<ViewHolder>() {

    /**
     * a way for [ViewHolder] to communicate with [RecyclerView.Adapter]
     */
    var action: ((Any?) -> Unit)? = null

    // viewType should start with 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return proxyList[viewType].onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (proxyList[getItemViewType(position)] as RVProxy<Any, ViewHolder>).onBindViewHolder(holder, dataList[position], position, action)
    }

    // 将填充表项分发给策略（布局刷新）
    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        (proxyList[getItemViewType(position)] as RVProxy<Any, ViewHolder>).onBindViewHolder( holder, dataList[position], position, action, payloads )
    }

    override fun getItemCount(): Int = dataList.size

    override fun getItemViewType(position: Int): Int {
        return getProxyIndex(dataList[position])
    }

    // 获取策略在列表中的索引
    private fun getProxyIndex(data: Any): Int = proxyList.indexOfFirst {
        // 如果Proxy<T,VH>中的第一个类型参数T和数据的类型相同，则返回对应策略的索引
        (it.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0].toString() == data.javaClass.toString()
    }


    fun <T, VH: ViewHolder> addProxy(proxy: RVProxy<T, VH>){
        proxyList.add(proxy)
    }

    fun <T, VH: ViewHolder> removeProxy(proxy: RVProxy<T, VH>){
        proxyList.remove(proxy)
    }

    fun addData(data: Any){
        dataList.add(0, data)
        notifyItemInserted(0)
    }

    fun addDataList(dataList: MutableList<Any>){
        this.dataList.addAll(0, dataList)
        notifyItemRangeInserted(0, dataList.size)
    }

    fun deleteData(position: Int){
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

}