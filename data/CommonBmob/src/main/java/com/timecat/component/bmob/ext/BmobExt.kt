package com.timecat.component.bmob.ext

import cn.bmob.v3.exception.BmobException
import com.timecat.identity.data.service.DataError

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-11-25
 * @description null
 * @usage null
 */
fun BmobException.toDataError(): DataError = DataError(this.errorCode, this.message)

fun DataError.toBmobException(): BmobException = BmobException(this.code, this.message)
