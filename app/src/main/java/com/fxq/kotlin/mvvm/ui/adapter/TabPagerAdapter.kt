package com.fxq.kotlin.mvvm.ui.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
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
 * @date 2023/10/8
 */
class TabPagerAdapter constructor(pages: List<String>)  : PagerAdapter(), HandlerAction, ToastAction {
  var mPages = pages

  var type = 1
  /**
   * 缓存未被销毁的page界面，用于定向更新界面
   */
  private val mMapViewHolders: MutableMap<String, PagerViewHolder> = HashMap()

  // constructor(pages: List<String>): this(pages) {
  //   mPages = pages
  // }

  fun setDataSource(pages: List<String>) {
    mPages = pages
    notifyDataSetChanged()
  }

  fun changeModel() {
    if (type == 1) {
      type = 2
    } else {
      type = 1
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      mMapViewHolders.forEach { key, value ->
        value.changeModel()
      }
    }
  }

  override fun getCount(): Int {
    return if (mPages == null) 0 else mPages!!.size
  }

  override fun isViewFromObject(view: View, `object`: Any): Boolean {
    return view === `object`
  }

  override fun instantiateItem(container: ViewGroup, position: Int): Any {
    val pageName = mPages!![position]
    val viewHolder = PagerViewHolder()
    val view = viewHolder.instantiateItem(container)
    view.tag = pageName
    container.addView(view)
    mMapViewHolders.put(pageName, viewHolder)
    return view
  }

  override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
    val view = `object` as View
    val bean = view.tag as String
    if (bean != null) {
      mMapViewHolders.remove(bean)
    }
    container.removeView(view)
  }

  override fun getPageTitle(position: Int): CharSequence? {
    return mPages!![position]
  }

  inner class PagerViewHolder: OnRefreshLoadMoreListener {
    private var adapter: StatusAdapter? = null
    var recyclerView: WrapRecyclerView? = null

    fun instantiateItem(container: ViewGroup): View {
      val view =
        LayoutInflater.from(container.context).inflate(R.layout.status_fragment, container, false)
      val refreshLayout: SmartRefreshLayout = view.findViewById(R.id.rl_status_refresh)
       recyclerView = view.findViewById(R.id.rv_status_list)
      adapter = StatusAdapter(view.context)
      adapter?.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
        override fun onItemClick(recyclerView: RecyclerView?, itemView: View?, position: Int) {
          toast(adapter?.getItem(position))
        }
      })

      adapter?.setData(analogData())
      recyclerView?.adapter = adapter
      if (type == 2) {
        recyclerView?.layoutManager = LinearLayoutManager(recyclerView?.context)
      } else {
        recyclerView?.layoutManager = GridLayoutManager(recyclerView?.context, 2)
        recyclerView?.adjustSpanSize()
      }
      val headerView = recyclerView?.addHeaderView<TextView>(R.layout.picker_item)
      headerView?.text = "我是头部"
      headerView?.setOnClickListener {toast("点击了头部")  }
      val footerView = recyclerView?.addFooterView<TextView>(R.layout.picker_item)
      footerView?.text = "我是尾部"
      footerView?.setOnClickListener { toast("点击了头部") }
      refreshLayout.setOnRefreshLoadMoreListener(this)
      return view
    }

    fun changeModel() {
      // adapter?.changeType(type)
      if (type == 2) {
        recyclerView?.layoutManager = LinearLayoutManager(recyclerView?.context)
      } else {
        recyclerView?.layoutManager = GridLayoutManager(recyclerView?.context, 2)
        recyclerView?.adjustSpanSize()
      }
      val animation = AnimationUtils.loadAnimation(recyclerView?.context, R.anim.left_in_activity)
      val layoutAnimationController = LayoutAnimationController(animation)
      layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL)
      layoutAnimationController.setDelay(0.2f)
      recyclerView?.setLayoutAnimation(layoutAnimationController)
      recyclerView?.requestLayout()
    }

    /**
     * 模拟数据
     */
    private fun analogData(): MutableList<String?> {
      val data: MutableList<String?> = ArrayList()
      adapter?.let {
        for (i in it.getCount() until it.getCount() + 20) {
          data.add("我是第" + i + "条目")
        }
        return data
      }
      return data
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
      postDelayed({
        adapter?.clearData()
        adapter?.setData(analogData())
        refreshLayout?.finishRefresh()
      }, 1000)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
      postDelayed({
        refreshLayout?.finishLoadMore()
        adapter?.apply {
          addData(analogData())
          setLastPage(getCount() >= 100)
          refreshLayout?.setNoMoreData(isLastPage())
        }
      }, 1000)
    }
  }
}