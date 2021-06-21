package com.timecat.component.bmob.dao;

import android.content.Context;

import com.timecat.extend.arms.BaseApplication;

public abstract class BaseModel {

    public static int CODE_NULL=1000;
    public static int CODE_NOT_EQUAL=1001;

    public static final int DEFAULT_LIMIT=20;

    public Context getContext(){
        return BaseApplication.getContext();
    }
}
