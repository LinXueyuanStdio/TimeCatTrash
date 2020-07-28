package com.timecat.module.search.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/8
 * @description null
 * @usage null
 */
class SearchViewModel : ViewModel() {
    val searchText: MutableLiveData<String> = MutableLiveData()
}