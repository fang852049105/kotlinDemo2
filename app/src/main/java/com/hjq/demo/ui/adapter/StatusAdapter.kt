package com.hjq.demo.ui.adapter

import android.content.*
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hjq.demo.R
import com.hjq.demo.app.AppAdapter

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2019/09/22
 *    desc   : 状态数据列表
 */
class StatusAdapter constructor(context: Context) : AppAdapter<String?>(context) {

    private var mType = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        // if (viewType == 1) {
        //     return ViewHolder()
        // } else {
        //     return ViewHolderV2()
        // }
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.status_item) {

        private val textView: TextView? by lazy { findViewById(R.id.tv_status_text) }

        override fun onBindView(position: Int) {
            textView?.text = getItem(position)
        }
    }

    // inner class ViewHolderV2 : AppViewHolder(R.layout.status_item_v2) {
    //
    //     private val textView: TextView? by lazy { findViewById(R.id.tv_status_text) }
    //
    //     override fun onBindView(position: Int) {
    //         textView?.text = getItem(position)
    //     }
    // }

    // fun changeType(type:Int) {
    //     mType = type
    // }
    //
    // override fun getItemViewType(position: Int): Int {
    //    return mType
    // }

    // override fun generateDefaultLayoutManager(context: Context): RecyclerView.LayoutManager {
    //     return GridLayoutManager(context, 2)
    // }
}