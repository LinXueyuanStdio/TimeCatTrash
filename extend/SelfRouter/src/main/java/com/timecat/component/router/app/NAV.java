package com.timecat.component.router.app;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xiaojinzi.component.Component;
import com.xiaojinzi.component.impl.Router;
import com.xiaojinzi.component.impl.service.ServiceManager;
import com.xiaojinzi.component.support.Action;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 导航
 * 隔绝 Router 框架的变化
 */
public class NAV {
    private NAV() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    @NonNull
    public static Fragment fragment(String path) {
        if (path.startsWith("/")) path = path.substring(1);
        Fragment fragment = (Fragment) Router.with(path).navigate();
        if (fragment == null) return new FallBackFragment();
        return fragment;
    }

    @NonNull
    public static Fragment fragment(String path, String key, String value) {
        if (path.startsWith("/")) path = path.substring(1);
        Fragment fragment = (Fragment) Router.with(path).putString(key, value).navigate();
        if (fragment == null) return new FallBackFragment();
        return fragment;
    }

    public static void go(String path) {
        if (path.startsWith("/")) path = path.substring(1);
        Router.with().hostAndPath(path).forward();
    }

    public static void go(String path, String key, String value) {
        if (path.startsWith("/")) path = path.substring(1);
        Router.with().hostAndPath(path).putString(key, value).forward();
    }

    public static void go(String path, String key, Long value) {
        if (path.startsWith("/")) path = path.substring(1);
        Router.with().hostAndPath(path).putLong(key, value).forward();
    }

    public static void go(String path, String key, Parcelable value) {
        if (path.startsWith("/")) path = path.substring(1);
        Router.with().hostAndPath(path).putParcelable(key, value).forward();
    }

    public static void go(String path, String key, Serializable value) {
        if (path.startsWith("/")) path = path.substring(1);
        Router.with().hostAndPath(path).putSerializable(key, value).forward();
    }

    public static void go(String path, String key, ArrayList<String> value) {
        if (path.startsWith("/")) path = path.substring(1);
        Router.with().hostAndPath(path).putStringArrayList(key, value).forward();
    }

    public static void go(Context context, String path) {
        if (path.startsWith("/")) path = path.substring(1);
        Router.with(context).hostAndPath(path).forward();
    }

    public static void goAndFinish(@NonNull Activity context, String path) {
        if (path.startsWith("/")) path = path.substring(1);
        Router.with(context).hostAndPath(path).afterAction(new Action() {
            @Override
            public void run() {
                context.finish();
            }
        }).forward();
    }

    public static void goAndFinish(Activity context, String path, String key, String value) {
        if (path.startsWith("/")) path = path.substring(1);
        Router.with(context).hostAndPath(path).putString(key, value).afterAction(new Action() {
            @Override
            public void run() {
                context.finish();
            }
        }).forward();
    }

    public static void go(Context context, String path, String key, String value) {
        if (path.startsWith("/")) path = path.substring(1);
        Router.with(context).hostAndPath(path).putString(key, value).forward();
    }

    public static void goAndFinish(Activity context, String path, String key, Long value) {
        if (path.startsWith("/")) path = path.substring(1);
        Router.with(context).hostAndPath(path).putLong(key, value).afterAction(new Action() {
            @Override
            public void run() {
                context.finish();
            }
        }).forward();
    }

    public static void go(Context context, String path, String key, Long value) {
        if (path.startsWith("/")) path = path.substring(1);
        Router.with(context).hostAndPath(path).putLong(key, value).forward();
    }

    public static void goAndFinish(Activity context, String path, String key, Parcelable value) {
        if (path.startsWith("/")) path = path.substring(1);
        Router.with(context).hostAndPath(path).putParcelable(key, value).afterAction(new Action() {
            @Override
            public void run() {
                context.finish();
            }
        }).forward();
    }

    public static void go(Context context, String path, String key, Parcelable value) {
        if (path.startsWith("/")) path = path.substring(1);
        Router.with(context).hostAndPath(path).putParcelable(key, value).forward();
    }

    public static void go(Context context, String path, String key, Serializable value) {
        if (path.startsWith("/")) path = path.substring(1);
        Router.with(context).hostAndPath(path).putSerializable(key, value).forward();
    }

    public static void go(Context context, String path, String key, ArrayList<String> value) {
        if (path.startsWith("/")) path = path.substring(1);
        Router.with(context).hostAndPath(path).putStringArrayList(key, value).forward();
    }

    @Nullable
    @MainThread
    public static <T> T service(Class<T> tClass) {
        return ServiceManager.get(tClass);
    }

    public static void inject(Object it) {
        Component.inject(it);
    }

    public static FragmentNavi rawFragment(String path) {
        if (path.startsWith("/")) path = path.substring(1);
        return new FragmentNavi(Router.with(path));
    }

    public static Navi raw(String path) {
        if (path.startsWith("/")) path = path.substring(1);
        return new Navi(Router.with().hostAndPath(path));
    }

    public static Navi raw(Context context, String path) {
        if (path.startsWith("/")) path = path.substring(1);
        return new Navi(Router.with(context).hostAndPath(path));
    }
}
