package com.timecat.component.bmob.ext.bmob

import com.timecat.identity.data.service.DataError

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description null
 * @usage null
 */
open class RequestCallback<T> {
    var onError: ((DataError) -> Unit)? = null
    var onEmpty: (() -> Unit)? = null
    var onSuccess: ((T) -> Unit)? = null
    var onListSuccess: ((List<T>) -> Unit)? = null
}

open class SimpleRequestCallback<T> {
    var onError: ((DataError) -> Unit)? = null
    var onSuccess: ((T) -> Unit)? = null
}