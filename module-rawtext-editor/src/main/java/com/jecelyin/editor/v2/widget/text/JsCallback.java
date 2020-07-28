package com.jecelyin.editor.v2.widget.text;

import android.webkit.ValueCallback;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */

public abstract class JsCallback<T>  implements ValueCallback<String> {
    private final Type type;

    protected JsCallback(){
        Type superClass = getClass().getGenericSuperclass();
        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    @Deprecated
    @Override
    public final void onReceiveValue(String value) {
        T result = JSON.parseObject(value, type);
        onCallback(result);
    }

    abstract public void onCallback(T data);
}