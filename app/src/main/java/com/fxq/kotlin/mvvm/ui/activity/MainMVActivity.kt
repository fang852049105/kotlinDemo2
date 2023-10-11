package com.fxq.kotlin.mvvm.ui.activity

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.flyco.tablayout.CommonTabLayout
import com.flyco.tablayout.listener.OnTabSelectListener
import com.fxq.kotlin.mvvm.ui.fragment.HomeMVFragment
import com.fxq.kotlin.mvvm.ui.fragment.HomeMVFragmentV2
import com.fxq.kotlin.mvvm.viewmodel.MainViewModel
import com.hazz.kotlinmvp.mvp.model.bean.TabEntity
import com.hjq.demo.R
import com.hjq.demo.app.AppActivity
import com.hjq.demo.ui.fragment.FindFragment
import com.hjq.demo.ui.fragment.HomeFragment
import com.hjq.demo.ui.fragment.MessageFragment
import com.hjq.demo.ui.fragment.MineFragment

/**
 * @author Fangxq
 * @date 2023/10/7
 */
class MainMVActivity : AppActivity() {

  private lateinit var mMainViewModel: MainViewModel
  private val mCommonTabLayout: CommonTabLayout? by lazy { findViewById(R.id.tab_layout) }
  // private var mHomeFragmentV2: HomeMVFragmentV2? = null
  private var mHomeFragment: HomeMVFragment? = null
  private var mFindFragment: FindFragment? = null
  private var mMessageFragment: MessageFragment? = null
  private var mMineFragment: MineFragment? = null

  override fun getLayoutId(): Int {
    return R.layout.activity_main
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    mMainViewModel = ViewModelProvider(
      viewModelStore,
      ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    )[MainViewModel::class.java]
    if (savedInstanceState != null) {
      mMainViewModel.setIndx(savedInstanceState.getInt("currTabIndex"))
    }
    super.onCreate(savedInstanceState)
  }

  override fun initView() {
  }

  override fun initData() {
    initTab()
    mCommonTabLayout?.currentTab = mMainViewModel.getIndex()
    switchFragment(mMainViewModel.getIndex())
  }

  //初始化底部菜单
  private fun initTab() {
    (0 until mMainViewModel.getTitles().size)
      .mapTo(mMainViewModel.getTabEntities()) {
        TabEntity(
          mMainViewModel.getTitles()[it],
          mMainViewModel.getIconSelectIds()[it],
          mMainViewModel.getIconUnSelectIds()[it]
        )
      }
    //为Tab赋值
    mCommonTabLayout?.setTabData(mMainViewModel.getTabEntities())
    mCommonTabLayout?.setOnTabSelectListener(object : OnTabSelectListener {
      override fun onTabSelect(position: Int) {
        //切换Fragment
        switchFragment(position)
      }

      override fun onTabReselect(position: Int) {
      }
    })
  }

  /* 切换Fragment
  * @param position 下标
  */
  private fun switchFragment(position: Int) {
    val transaction = supportFragmentManager.beginTransaction()
    hideFragments(transaction)
    when (position) {
      0 // 首页
      -> mHomeFragment?.let {
        transaction.show(it)
      } ?: HomeMVFragment.newInstance().let {
        mHomeFragment = it
        transaction.add(R.id.fl_container, it, "home")
      }
      // 0 // 首页测试
      // -> mHomeFragmentV2?.let {
      //   transaction.show(it)
      // } ?: HomeMVFragmentV2.newInstance().let {
      //   mHomeFragmentV2 = it
      //   transaction.add(R.id.fl_container, it, "homeV2")
      // }
      1  //发现
      -> mFindFragment?.let {
        transaction.show(it)
      } ?: FindFragment.newInstance().let {
        mFindFragment = it
        transaction.add(R.id.fl_container, it, "discovery")
      }
      2  //消息
      -> mMessageFragment?.let {
        transaction.show(it)
      } ?: MessageFragment.newInstance().let {
        mMessageFragment = it
        transaction.add(R.id.fl_container, it, "message")
      }
      3 //我的
      -> mMineFragment?.let {
        transaction.show(it)
      } ?: MineFragment.newInstance().let {
        mMineFragment = it
        transaction.add(R.id.fl_container, it, "mine")
      }

      else -> {
      }
    }

    mMainViewModel.setIndx(position)
    mCommonTabLayout?.currentTab = mMainViewModel.getIndex()
    transaction.commitAllowingStateLoss()
  }


  /**
   * 隐藏所有的Fragment
   * @param transaction transaction
   */
  private fun hideFragments(transaction: FragmentTransaction) {
    mHomeFragment?.let { transaction.hide(it) }
    mFindFragment?.let { transaction.hide(it) }
    mMessageFragment?.let { transaction.hide(it) }
    mMineFragment?.let { transaction.hide(it) }
  }
}