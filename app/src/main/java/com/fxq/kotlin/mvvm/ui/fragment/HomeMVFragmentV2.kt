package com.fxq.kotlin.mvvm.ui.fragment

import android.content.res.ColorStateList
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.flyco.tablayout.listener.CustomTabEntity
import com.fxq.kotlin.mvvm.ui.adapter.TabPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.gyf.immersionbar.ImmersionBar
import com.hjq.base.FragmentPagerAdapter
import com.hjq.demo.R
import com.hjq.demo.app.AppFragment
import com.hjq.demo.app.TitleBarFragment
import com.hjq.demo.ui.activity.HomeActivity
import com.hjq.demo.ui.adapter.TabAdapter
import com.hjq.demo.ui.fragment.StatusFragment
import com.hjq.demo.widget.XCollapsingToolbarLayout
import java.util.ArrayList

/**
 * @author Fangxq
 * @date 2023/10/8
 */
class HomeMVFragmentV2 : TitleBarFragment<HomeActivity>() , TabAdapter.OnTabListener,
  ViewPager.OnPageChangeListener, XCollapsingToolbarLayout.OnScrimsListener {

  companion object {

    fun newInstance(): HomeMVFragmentV2 {
      return HomeMVFragmentV2()
    }
  }

  private val collapsingToolbarLayout: XCollapsingToolbarLayout? by lazy { findViewById(R.id.ctl_home_bar) }
  private val toolbar: Toolbar? by lazy { findViewById(R.id.tb_home_title) }
  private val addressView: TextView? by lazy { findViewById(R.id.tv_home_address) }
  private val hintView: TextView? by lazy { findViewById(R.id.tv_home_hint) }
  private val searchView: AppCompatImageView? by lazy { findViewById(R.id.iv_home_search) }
  private val tabLayout: TabLayout? by lazy { findViewById(R.id.tab_layout) }
  private val viewPager: ViewPager? by lazy { findViewById(R.id.vp_home_pager) }

  private var pagerAdapter: TabPagerAdapter? = null

  override fun getLayoutId(): Int {
    return R.layout.fragment_home
  }

  override fun initView() {
    val pageNames = ArrayList<String>()
    for (i in 0 until 10) {
      pageNames.add("列表演示$i")
    }
    pagerAdapter = TabPagerAdapter(pageNames)
    viewPager?.adapter = pagerAdapter
    viewPager?.addOnPageChangeListener(this)
    tabLayout?.setupWithViewPager(viewPager)

    // 给这个 ToolBar 设置顶部内边距，才能和 TitleBar 进行对齐
    ImmersionBar.setTitleBar(getAttachActivity(), toolbar)

    // 设置渐变监听
    collapsingToolbarLayout?.setOnScrimsListener(this)
  }

  override fun initData() {
  }

  override fun isStatusBarEnabled(): Boolean {
    // 使用沉浸式状态栏
    return !super.isStatusBarEnabled()
  }

  override fun isStatusBarDarkFont(): Boolean {
    return collapsingToolbarLayout?.isScrimsShown() == true
  }

  /**
   * [TabAdapter.OnTabListener]
   */
  override fun onTabSelected(recyclerView: RecyclerView?, position: Int): Boolean {
    viewPager?.currentItem = position
    return true
  }

  /**
   * [ViewPager.OnPageChangeListener]
   */
  override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

  override fun onPageSelected(position: Int) {

  }

  override fun onPageScrollStateChanged(state: Int) {}

  /**
   * CollapsingToolbarLayout 渐变回调
   *
   * [XCollapsingToolbarLayout.OnScrimsListener]
   */
  @Suppress("RestrictedApi")
  override fun onScrimsStateChange(layout: XCollapsingToolbarLayout?, shown: Boolean) {
    getStatusBarConfig().statusBarDarkFont(shown).init()
    addressView?.setTextColor(ContextCompat.getColor(getAttachActivity()!!, if (shown) R.color.black else R.color.white))
    hintView?.setBackgroundResource(if (shown) R.drawable.home_search_bar_gray_bg else R.drawable.home_search_bar_transparent_bg)
    hintView?.setTextColor(ContextCompat.getColor(getAttachActivity()!!, if (shown) R.color.black60 else R.color.white60))
    searchView?.supportImageTintList = ColorStateList.valueOf(
      ContextCompat.getColor(getAttachActivity()!!,
      if (shown) R.color.common_icon_color else R.color.white))
  }

  override fun onDestroy() {
    super.onDestroy()
    viewPager?.adapter = null
    viewPager?.removeOnPageChangeListener(this)
  }
}