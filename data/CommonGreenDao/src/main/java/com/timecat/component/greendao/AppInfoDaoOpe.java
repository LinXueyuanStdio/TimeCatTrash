package com.timecat.component.greendao;

import android.content.Context;

import com.timecat.component.greendao.dao.AppInfoDao;
import com.timecat.component.greendao.model.AppInfo;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;
import java.util.Set;

public class AppInfoDaoOpe {
    public static void insertAppInfo(Context context, AppInfo info){
        GreenDaoManager.getInstance(context).getDaoSession().getAppInfoDao().insert(info);
    }
    public static void insertAppInfos(Context context, List<AppInfo> infos){
        GreenDaoManager.getInstance(context).getDaoSession().getAppInfoDao().insertInTx(infos,false);
    }
    public static void deleteAppInfo(Context context,AppInfo info){
        GreenDaoManager.getInstance(context).getDaoSession().getAppInfoDao().delete(info);
    }
    public static void deleteAppInfoById(Context context,long id){
        GreenDaoManager.getInstance(context).getDaoSession().getAppInfoDao().deleteByKey(id);
    }
    public static void deleteAll(Context context){
        GreenDaoManager.getInstance(context).getDaoSession().getAppInfoDao().deleteAll();
    }
    public static void updateAppInfo(Context context,AppInfo info){
        GreenDaoManager.getInstance(context).getDaoSession().getAppInfoDao().update(info);
    }
    public static void updateAppInfos(Context context,Set<AppInfo> infos){
        GreenDaoManager.getInstance(context).getDaoSession().getAppInfoDao().updateInTx(infos);
    }
    public static List<AppInfo> queryAll(Context context){
        return GreenDaoManager.getInstance(context).getDaoSession().getAppInfoDao().queryBuilder().build().list();
    }
    public static List<AppInfo> queryEnableAppInfos(Context context){
        return GreenDaoManager.getInstance(context).getDaoSession().getAppInfoDao().
                queryBuilder().where(AppInfoDao.Properties.EnableState.eq(AppInfo.APP_ENABLE)).list();
    }
    public static List<AppInfo> querySystemAppInfos(Context context){
        return GreenDaoManager.getInstance(context).getDaoSession().getAppInfoDao().
                queryBuilder().where(AppInfoDao.Properties.AppType.eq(AppInfo.SYSTEM_APP)).list();
    }
    public static List<AppInfo> queryUserAppInfos(Context context){
        return GreenDaoManager.getInstance(context).getDaoSession().getAppInfoDao().
                queryBuilder().where(AppInfoDao.Properties.AppType.eq(AppInfo.USER_APP)).list();
    }
    public static List<AppInfo> queryAppInfosByTypeWithPage(Context context,int appType,int page){
        QueryBuilder queryBuilder = GreenDaoManager.getInstance(context).getDaoSession().getAppInfoDao().queryBuilder();
        return queryBuilder.where(AppInfoDao.Properties.AppType.eq(appType)).offset(page * 100).limit(100).build().list();
    }
    public static List<AppInfo> queryAppInfosByPage(Context context, int page){
        return GreenDaoManager.getInstance(context).getDaoSession().getAppInfoDao().
                queryBuilder().offset(page * 24).limit(24).build().list();
    }

    public static boolean isExist(Context context,String who){
        AppInfo info = GreenDaoManager.getInstance(context).getDaoSession().getAppInfoDao().
                queryBuilder().where(AppInfoDao.Properties.PackageName.eq(who)).unique();
        return info != null;
    }
    public static boolean appEnable(Context context,String who){
        List<AppInfo> infoList = GreenDaoManager.getInstance(context).getDaoSession().getAppInfoDao().
                queryBuilder().where(AppInfoDao.Properties.EnableState.eq(AppInfo.APP_ENABLE)).build().list();
        for(AppInfo info : infoList){
            if(info.getPackageName().equals(who)){
                return true;
            }
        }
        return false;
    }
}
