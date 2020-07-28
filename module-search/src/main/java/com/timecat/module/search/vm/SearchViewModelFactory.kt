package com.timecat.module.search.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/8
 * @description null
 * @usage null
 */
class SearchViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SearchViewModel() as T
    }

    companion object {
        fun getInstance(): ViewModelProvider.Factory {
            return SearchViewModelFactory()
        }
    }
}