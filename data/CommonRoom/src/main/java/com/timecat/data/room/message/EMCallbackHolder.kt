package com.timecat.data.room.message

import android.util.Log
import com.timecat.data.room.record.RoomRecord.Companion.TAG
import java.lang.ref.WeakReference

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-01-18
 * @description null
 * @usage null
 */
/**
 * EMXXXListener doesn't need EMCallbackHolder, user will be responsible for add, remove listener reference.
 * Message's callback function is different from listener, user add callback, and won't remove the reference.
 * Old implementation for Message's callback is simply hold the reference, which leads to memory leak.
 * SDK caches hold reference of message, message hold callback, callback hold Activity's this reference, so activities will never be released.
 *
 * Solution:
 * 1. change the message's callback to weak reference.
 * 2. when sendMessage or downloadMessageAttachment make the reference be strong one.
 * 3. when callback be executed, onSuccess or onError, release the reference.
 *
 * Declaim:
 * 1. don't set message be strong when user call setMessageStatusCallback, user may just attach the callback, there will not be onError or onSuccess to be called
 */
class EMCallbackHolder(callback: RoomCallBack?) : RoomCallBack {
    @Synchronized
    fun update(callback: RoomCallBack?) {
        if (strong != null) {
            strong = callback
            return
        }
        weak = WeakReference(callback)
    }

    @Synchronized
    fun makeItStrong() {
        if (strong != null) {
            return
        }
        if (weak != null && weak!!.get() != null) {
            strong = weak!!.get()
        }
    }

    @Synchronized
    fun release() {
        if (strong == null) {
            return
        }
        weak = WeakReference(strong)
        strong = null
    }

    @get:Synchronized
    val ref: RoomCallBack?
        get() {
            if (strong != null) {
                return strong
            }
            if (weak != null) {
                val callBack: RoomCallBack? = weak!!.get()
                if (callBack == null) {
                    Log.d(TAG, "getRef weak:$callBack")
                }
                return callBack
            }
            return null
        }

    override fun onSuccess() {
        innerCallback?.onSuccess()
        val callback: RoomCallBack? = ref
        if (callback != null) {
            callback.onSuccess()
            release()
        } else {
            Log.d(TAG, "CallbackHolder getRef: null")
        }
    }

    override fun onError(code: Int, error: String?) {
        innerCallback?.onError(code, error)
        val callback: RoomCallBack? = ref
        if (callback != null) {
            callback.onError(code, error)
            release()
        } else {
            Log.d(TAG, "CallbackHolder getRef: null")
        }
    }

    override fun onProgress(progress: Int, status: String?) {
        innerCallback?.onProgress(progress, status)
        val callback: RoomCallBack? = ref
        if (callback != null) {
            callback.onProgress(progress, status)
        }
    }

    // strong & weak are used for external callback reference
    // inner callback is used by chat manager sendMessage, to handle changes of msgId and msg time
    private var strong: RoomCallBack? = null
    private var weak: WeakReference<RoomCallBack?>?
    var innerCallback: RoomCallBack? = null

    init {
        weak = WeakReference<RoomCallBack?>(callback)
    }
}