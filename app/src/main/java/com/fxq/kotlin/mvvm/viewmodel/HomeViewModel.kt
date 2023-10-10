package com.fxq.kotlin.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fxq.kotlin.mvvm.ui.adapter.TabPagerAdapter
import java.util.ArrayList

/**
 * @author Fangxq
 * @date 2023/10/10
 */
class HomeViewModel : ViewModel() {

  private val mPageNames: MutableLiveData<ArrayList<String>> = MutableLiveData()

  fun getPageNamesLiveData() = mPageNames


  fun buildPageData() {
    val pageList = ArrayList<String>()
    for (i in 0 until 10) {
      pageList.add("列表演示$i")
    }
    mPageNames.postValue(pageList)
  }

}