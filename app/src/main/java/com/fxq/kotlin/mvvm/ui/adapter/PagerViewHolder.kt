package com.fxq.kotlin.mvvm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hjq.base.BaseAdapter
import com.hjq.base.action.HandlerAction
import com.hjq.demo.R
import com.hjq.demo.action.ToastAction
import com.hjq.demo.ui.adapter.StatusAdapter
import com.hjq.widget.layout.WrapRecyclerView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * @author Fangxq
 * @date 2023/10/10
 */
class PagerViewHolder: OnRefreshLoadMoreListener, HandlerAction, ToastAction {
  private var mAdapter: StatusAdapter? = null
  private var mRecyclerView: WrapRecyclerView? = null
  private var mType = 1

  fun setType(type: Int) {
    mType = type
  }

  fun instantiateItem(container: ViewGroup): View {
    val view =
      LayoutInflater.from(container.context).inflate(R.layout.status_fragment, container, false)
    val refreshLayout: SmartRefreshLayout = view.findViewById(R.id.rl_status_refresh)
    mRecyclerView = view.findViewById(R.id.rv_status_list)
    mAdapter = StatusAdapter(view.context)
    mAdapter?.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
      override fun onItemClick(recyclerView: RecyclerView?, itemView: View?, position: Int) {
        toast(mAdapter?.getItem(position))
      }
    })

    mAdapter?.setData(analogData())
    mRecyclerView?.adapter = mAdapter
    if (mType == 2) {
      mRecyclerView?.layoutManager = LinearLayoutManager(mRecyclerView?.context)
    } else {
      mRecyclerView?.layoutManager = GridLayoutManager(mRecyclerView?.context, 2)
      mRecyclerView?.adjustSpanSize()
    }
    val headerView = mRecyclerView?.addHeaderView<TextView>(R.layout.picker_item)
    headerView?.text = "我是头部"
    headerView?.setOnClickListener {toast("点击了头部")  }
    val footerView = mRecyclerView?.addFooterView<TextView>(R.layout.picker_item)
    footerView?.text = "我是尾部"
    footerView?.setOnClickListener { toast("点击了头部") }
    refreshLayout.setOnRefreshLoadMoreListener(this)
    return view
  }

  fun changeModel() {
    // adapter?.changeType(type)
    if (mType == 2) {
      mRecyclerView?.layoutManager = LinearLayoutManager(mRecyclerView?.context)
    } else {
      mRecyclerView?.layoutManager = GridLayoutManager(mRecyclerView?.context, 2)
      mRecyclerView?.adjustSpanSize()
    }
    val animation = AnimationUtils.loadAnimation(mRecyclerView?.context, R.anim.left_in_activity)
    val layoutAnimationController = LayoutAnimationController(animation)
    layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL)
    layoutAnimationController.setDelay(0.2f)
    mRecyclerView?.setLayoutAnimation(layoutAnimationController)
    mRecyclerView?.requestLayout()
  }

  /**
   * 模拟数据
   */
  private fun analogData(): MutableList<String?> {
    val data: MutableList<String?> = ArrayList()
    mAdapter?.let {
      for (i in it.getCount() until it.getCount() + 20) {
        data.add("我是第" + i + "条目")
      }
      return data
    }
    return data
  }

  override fun onRefresh(refreshLayout: RefreshLayout) {
    postDelayed({
      mAdapter?.clearData()
      mAdapter?.setData(analogData())
      refreshLayout?.finishRefresh()
    }, 1000)
  }

  override fun onLoadMore(refreshLayout: RefreshLayout) {
    postDelayed({
      refreshLayout?.finishLoadMore()
      mAdapter?.apply {
        addData(analogData())
        setLastPage(getCount() >= 100)
        refreshLayout?.setNoMoreData(isLastPage())
      }
    }, 1000)
  }
}