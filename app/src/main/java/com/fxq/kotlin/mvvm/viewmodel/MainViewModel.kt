package com.fxq.kotlin.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.flyco.tablayout.listener.CustomTabEntity
import com.hjq.demo.R
import java.util.ArrayList

/**
 * @author Fangxq
 * @date 2023/10/7
 */
class MainViewModel : ViewModel() {

  private val mTitles = arrayOf("首页","首页2", "发现", "消息", "我的")

  // 未被选中的图标
  private val mIconUnSelectIds = intArrayOf(
    R.drawable.home_home_off_ic, R.drawable.home_home_off_ic, R.drawable.home_found_off_ic, R.drawable.home_message_off_ic, R.drawable.home_me_off_ic)
  // 被选中的图标
  private val mIconSelectIds = intArrayOf(R.drawable.home_home_on_ic,R.drawable.home_home_on_ic, R.drawable.home_found_on_ic, R.drawable.home_message_on_ic, R.drawable.home_me_on_ic)
  //默认为0
  private var mIndex = 0

  private val mTabEntities = ArrayList<CustomTabEntity>()

  fun getIndex() = mIndex

  fun getTitles() = mTitles

  fun getIconUnSelectIds() = mIconUnSelectIds

  fun getIconSelectIds() = mIconSelectIds

  fun getTabEntities() = mTabEntities

  fun setIndx(index: Int) {
    mIndex = index
  }
}