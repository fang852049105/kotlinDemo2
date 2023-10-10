package com.fxq.kotlin.mvvm.ui.adapter

import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

/**
 * @author Fangxq
 * @date 2023/10/8
 */
class TabPagerAdapter constructor(pages: List<String>)  : PagerAdapter() {
  var mPages = pages

  var mType = 1
  /**
   * 缓存未被销毁的page界面，用于定向更新界面
   */
  private val mMapViewHolders: MutableMap<String, PagerViewHolder> = HashMap()

  fun setDataSource(pages: List<String>) {
    mPages = pages
    notifyDataSetChanged()
  }

  fun changeModel() {
    if (mType == 1) {
      mType = 2
    } else {
      mType = 1
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
    viewHolder.setType(mType)
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
}