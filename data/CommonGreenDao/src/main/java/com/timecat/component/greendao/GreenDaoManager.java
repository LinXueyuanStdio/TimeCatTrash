package com.timecat.component.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.timecat.component.greendao.dao.DaoMaster;
import com.timecat.component.greendao.dao.DaoSession;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-10-29
 * @description null
 * @usage null
 */
public class GreenDaoManager {
    private static final String DB_NAME = "greendao.db";
    private static GreenDaoManager sInstance;
    private DaoMaster.DevOpenHelper mDevOpenHelper;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private SQLiteDatabase mDatabase;

    private GreenDaoManager(Context context) {
        mDevOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        mDatabase = mDevOpenHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(mDatabase);
        mDaoSession = mDaoMaster.newSession();
    }

    public static GreenDaoManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (GreenDaoManager.class) {
                if (sInstance == null) {
                    sInstance = new GreenDaoManager(context);
                }
            }
        }
        return sInstance;
    }

    public DaoMaster getDaoMaster() {
        return mDaoMaster;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}
