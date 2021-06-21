package com.timecat.component.router.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;

import androidx.annotation.AnyThread;
import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

import com.xiaojinzi.component.bean.ActivityResult;
import com.xiaojinzi.component.impl.Navigator;
import com.xiaojinzi.component.impl.Router;
import com.xiaojinzi.component.support.Action;
import com.xiaojinzi.component.support.Consumer;
import com.xiaojinzi.component.support.NavigationDisposable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/28
 * @description null
 * @usage null
 */
public class Navi {
    Navigator delegate;

    public Navi(Navigator navigator) {
        this.delegate = navigator;
    }

    private Navi() {
    }

    public Navi interceptors(@Nullable NavInterceptor... interceptorArr) {
        delegate.interceptors(interceptorArr);
        return this;
    }

    public Navi interceptors(@Nullable Class<? extends NavInterceptor>... interceptorClassArr) {
        delegate.interceptors(interceptorClassArr);
        return this;
    }

    public Navi interceptorNames(@Nullable String... interceptorNameArr) {
        delegate.interceptorNames(interceptorNameArr);
        return this;
    }

    /**
     * requestCode 会随机的生成
     */
    public Navi requestCodeRandom() {
        delegate.requestCodeRandom();
        return this;
    }

    public Navi autoCancel(boolean autoCancel) {
        delegate.autoCancel(autoCancel);
        return this;
    }

    public Navi useRouteRepeatCheck(boolean useRouteRepeatCheck) {
        delegate.useRouteRepeatCheck(useRouteRepeatCheck);
        return this;
    }

    public Navi proxyBundle(@NonNull Bundle bundle) {
        delegate.proxyBundle(bundle);
        return this;
    }

    public Navi intentConsumer(@Nullable @UiThread Consumer<Intent> intentConsumer) {
        delegate.intentConsumer(intentConsumer);
        return this;
    }

    public Navi addIntentFlags(@Nullable Integer... flags) {
        delegate.addIntentFlags(flags);
        return this;
    }

    public Navi addIntentCategories(@Nullable String... categories) {
        delegate.addIntentCategories(categories);
        return this;
    }

    public Navi beforAction(@Nullable @UiThread Action action) {
        delegate.beforAction(action);
        return this;
    }

    public Navi beforStartAction(@Nullable Action action) {
        delegate.beforStartAction(action);
        return this;
    }

    public Navi afterStartAction(@Nullable Action action) {
        delegate.afterStartAction(action);
        return this;
    }

    public Navi afterAction(@Nullable @UiThread Action action) {
        delegate.afterAction(action);
        return this;
    }

    public Navi afterErrorAction(@Nullable @UiThread Action action) {
        delegate.afterErrorAction(action);
        return this;
    }

    public Navi afterEventAction(@Nullable @UiThread Action action) {
        delegate.afterEventAction(action);
        return this;
    }

    public Navi requestCode(@Nullable Integer requestCode) {
        delegate.requestCode(requestCode);
        return this;
    }

    public Navi options(@Nullable Bundle options) {
        delegate.options(options);
        return this;
    }

    public Navi url(@NonNull String url) {
        delegate.url(url);
        return this;
    }

    public Navi scheme(@NonNull String scheme) {
        delegate.scheme(scheme);
        return this;
    }

    public Navi hostAndPath(@NonNull String hostAndPath) {
        delegate.hostAndPath(hostAndPath);
        return this;
    }

    public Navi userInfo(@NonNull String userInfo) {
        delegate.userInfo(userInfo);
        return this;
    }

    public Navi host(@NonNull String host) {
        delegate.host(host);
        return this;
    }

    public Navi path(@NonNull String path) {
        delegate.path(path);
        return this;
    }

    //region putXXX()
    public Navi putAll(@NonNull Bundle bundle) {
        delegate.putAll(bundle);
        return this;
    }

    public Navi putBundle(@NonNull String key, @Nullable Bundle bundle) {
        delegate.putBundle(key, bundle);
        return this;
    }

    public Navi putCharSequence(@NonNull String key, @Nullable CharSequence value) {
        delegate.putCharSequence(key, value);
        return this;
    }

    public Navi putCharSequenceArray(@NonNull String key, @Nullable CharSequence[] value) {
        delegate.putCharSequenceArray(key, value);
        return this;
    }

    public Navi putCharSequenceArrayList(@NonNull String key, @Nullable ArrayList<CharSequence> value) {
        delegate.putCharSequenceArrayList(key, value);
        return this;
    }

    public Navi putByte(@NonNull String key, @Nullable byte value) {
        delegate.putByte(key, value);
        return this;
    }

    public Navi putByteArray(@NonNull String key, @Nullable byte[] value) {
        delegate.putByteArray(key, value);
        return this;
    }

    public Navi putChar(@NonNull String key, @Nullable char value) {
        delegate.putChar(key, value);
        return this;
    }

    public Navi putCharArray(@NonNull String key, @Nullable char[] value) {
        delegate.putCharArray(key, value);
        return this;
    }

    public Navi putBoolean(@NonNull String key, @Nullable boolean value) {
        delegate.putBoolean(key, value);
        return this;
    }

    public Navi putBooleanArray(@NonNull String key, @Nullable boolean[] value) {
        delegate.putBooleanArray(key, value);
        return this;
    }

    public Navi putString(@NonNull String key, @Nullable String value) {
        delegate.putString(key, value);
        return this;
    }

    public Navi putStringArray(@NonNull String key, @Nullable String[] value) {
        delegate.putStringArray(key, value);
        return this;
    }

    public Navi putStringArrayList(@NonNull String key, @Nullable ArrayList<String> value) {
        delegate.putStringArrayList(key, value);
        return this;
    }

    public Navi putShort(@NonNull String key, @Nullable short value) {
        delegate.putShort(key, value);
        return this;
    }

    public Navi putShortArray(@NonNull String key, @Nullable short[] value) {
        delegate.putShortArray(key, value);
        return this;
    }

    public Navi putInt(@NonNull String key, @Nullable int value) {
        delegate.putInt(key, value);
        return this;
    }

    public Navi putIntArray(@NonNull String key, @Nullable int[] value) {
        delegate.putIntArray(key, value);
        return this;
    }

    public Navi putIntegerArrayList(@NonNull String key, @Nullable ArrayList<Integer> value) {
        delegate.putIntegerArrayList(key, value);
        return this;
    }

    public Navi putLong(@NonNull String key, @Nullable long value) {
        delegate.putLong(key, value);
        return this;
    }

    public Navi putLongArray(@NonNull String key, @Nullable long[] value) {
        delegate.putLongArray(key, value);
        return this;
    }

    public Navi putFloat(@NonNull String key, @Nullable float value) {
        delegate.putFloat(key, value);
        return this;
    }

    public Navi putFloatArray(@NonNull String key, @Nullable float[] value) {
        delegate.putFloatArray(key, value);
        return this;
    }

    public Navi putDouble(@NonNull String key, @Nullable double value) {
        delegate.putDouble(key, value);
        return this;
    }

    public Navi putDoubleArray(@NonNull String key, @Nullable double[] value) {
        delegate.putDoubleArray(key, value);
        return this;
    }

    public Navi putParcelable(@NonNull String key, @Nullable Parcelable value) {
        delegate.putParcelable(key, value);
        return this;
    }

    public Navi putParcelableArray(@NonNull String key, @Nullable Parcelable[] value) {
        delegate.putParcelableArray(key, value);
        return this;
    }

    public Navi putParcelableArrayList(@NonNull String key, @Nullable ArrayList<? extends Parcelable> value) {
        delegate.putParcelableArrayList(key, value);
        return this;
    }

    public Navi putSparseParcelableArray(@NonNull String key, @Nullable SparseArray<? extends Parcelable> value) {
        delegate.putSparseParcelableArray(key, value);
        return this;
    }

    public Navi putSerializable(@NonNull String key, @Nullable Serializable value) {
        delegate.putSerializable(key, value);
        return this;
    }
    //endregion

    //region withXXX()

    public Navi withAll(@NonNull Bundle bundle) {
        delegate.putAll(bundle);
        return this;
    }

    public Navi withBundle(@NonNull String key, @Nullable Bundle bundle) {
        delegate.putBundle(key, bundle);
        return this;
    }

    public Navi withCharSequence(@NonNull String key, @Nullable CharSequence value) {
        delegate.putCharSequence(key, value);
        return this;
    }

    public Navi withCharSequenceArray(@NonNull String key, @Nullable CharSequence[] value) {
        delegate.putCharSequenceArray(key, value);
        return this;
    }

    public Navi withCharSequenceArrayList(@NonNull String key, @Nullable ArrayList<CharSequence> value) {
        delegate.putCharSequenceArrayList(key, value);
        return this;
    }

    public Navi withByte(@NonNull String key, @Nullable byte value) {
        delegate.putByte(key, value);
        return this;
    }

    public Navi withByteArray(@NonNull String key, @Nullable byte[] value) {
        delegate.putByteArray(key, value);
        return this;
    }

    public Navi withChar(@NonNull String key, @Nullable char value) {
        delegate.putChar(key, value);
        return this;
    }

    public Navi withCharArray(@NonNull String key, @Nullable char[] value) {
        delegate.putCharArray(key, value);
        return this;
    }

    public Navi withBoolean(@NonNull String key, @Nullable boolean value) {
        delegate.putBoolean(key, value);
        return this;
    }

    public Navi withBooleanArray(@NonNull String key, @Nullable boolean[] value) {
        delegate.putBooleanArray(key, value);
        return this;
    }

    public Navi withString(@NonNull String key, @Nullable String value) {
        delegate.putString(key, value);
        return this;
    }

    public Navi withStringArray(@NonNull String key, @Nullable String[] value) {
        delegate.putStringArray(key, value);
        return this;
    }

    public Navi withStringArrayList(@NonNull String key, @Nullable ArrayList<String> value) {
        delegate.putStringArrayList(key, value);
        return this;
    }

    public Navi withShort(@NonNull String key, @Nullable short value) {
        delegate.putShort(key, value);
        return this;
    }

    public Navi withShortArray(@NonNull String key, @Nullable short[] value) {
        delegate.putShortArray(key, value);
        return this;
    }

    public Navi withInt(@NonNull String key, @Nullable int value) {
        delegate.putInt(key, value);
        return this;
    }

    public Navi withIntArray(@NonNull String key, @Nullable int[] value) {
        delegate.putIntArray(key, value);
        return this;
    }

    public Navi withIntegerArrayList(@NonNull String key, @Nullable ArrayList<Integer> value) {
        delegate.putIntegerArrayList(key, value);
        return this;
    }

    public Navi withLong(@NonNull String key, @Nullable long value) {
        delegate.putLong(key, value);
        return this;
    }

    public Navi withLongArray(@NonNull String key, @Nullable long[] value) {
        delegate.putLongArray(key, value);
        return this;
    }

    public Navi withFloat(@NonNull String key, @Nullable float value) {
        delegate.putFloat(key, value);
        return this;
    }

    public Navi withFloatArray(@NonNull String key, @Nullable float[] value) {
        delegate.putFloatArray(key, value);
        return this;
    }

    public Navi withDouble(@NonNull String key, @Nullable double value) {
        delegate.putDouble(key, value);
        return this;
    }

    public Navi withDoubleArray(@NonNull String key, @Nullable double[] value) {
        delegate.putDoubleArray(key, value);
        return this;
    }

    public Navi withParcelable(@NonNull String key, @Nullable Parcelable value) {
        delegate.putParcelable(key, value);
        return this;
    }

    public Navi withParcelableArray(@NonNull String key, @Nullable Parcelable[] value) {
        delegate.putParcelableArray(key, value);
        return this;
    }

    public Navi withParcelableArrayList(@NonNull String key, @Nullable ArrayList<? extends Parcelable> value) {
        delegate.putParcelableArrayList(key, value);
        return this;
    }

    public Navi withSparseParcelableArray(@NonNull String key, @Nullable SparseArray<? extends Parcelable> value) {
        delegate.putSparseParcelableArray(key, value);
        return this;
    }

    public Navi withSerializable(@NonNull String key, @Nullable Serializable value) {
        delegate.putSerializable(key, value);
        return this;
    }
    //endregion

    //region query()
    public Navi query(@NonNull String queryName, @NonNull String queryValue) {
        delegate.query(queryName, queryValue);
        return this;
    }

    public Navi query(@NonNull String queryName, boolean queryValue) {
        delegate.query(queryName, queryValue);
        return this;
    }

    public Navi query(@NonNull String queryName, byte queryValue) {
        delegate.query(queryName, queryValue);
        return this;
    }

    public Navi query(@NonNull String queryName, int queryValue) {
        delegate.query(queryName, queryValue);
        return this;
    }

    public Navi query(@NonNull String queryName, float queryValue) {
        delegate.query(queryName, queryValue);
        return this;
    }

    public Navi query(@NonNull String queryName, long queryValue) {
        delegate.query(queryName, queryValue);
        return this;
    }

    public Navi query(@NonNull String queryName, double queryValue) {
        delegate.query(queryName, queryValue);
        return this;
    }
    //endregion

    //region forwardXXX()
    /**
     * 为了拿到 {@link ActivityResult#resultCode}
     *
     * @param callback 回调方法
     */
    @AnyThread
    public void forwardForResultCode(@NonNull @UiThread final ResultCodeCallback callback) {
        delegate.forwardForResultCode(callback);
    }

    /**
     * 为了拿到 {@link ActivityResult#resultCode}
     *
     * @param callback 回调方法
     */
    @NonNull
    @AnyThread
    @CheckResult
    public NavigationDisposable navigateForResultCode(@NonNull @UiThread final ResultCodeCallback callback) {
        return delegate.navigateForResultCode(callback);
    }

    /**
     * 为了拿到 {@link ActivityResult#resultCode}
     *
     * @param callback 回调方法
     */
    @AnyThread
    public void forwardForResultCodeMatch(
            @NonNull @UiThread final ForwardCallback callback, final int expectedResultCode) {
        delegate.forwardForResultCodeMatch(callback, expectedResultCode);
    }

    /**
     * 为了拿到 {@link ActivityResult#resultCode}
     *
     * @param callback 回调方法
     */
    @NonNull
    @AnyThread
    @CheckResult
    public NavigationDisposable navigateForResultCodeMatch(@NonNull @UiThread final ForwardCallback callback, final int expectedResultCode) {
        return delegate.navigateForResultCodeMatch(callback, expectedResultCode);
    }

    /**
     * 为了拿到 {@link Intent}
     *
     * @param callback 回调方法
     */
    @AnyThread
    public void forwardForIntentAndResultCodeMatch(@NonNull @UiThread final IntentCallback callback, final int expectedResultCode) {
        delegate.forwardForIntentAndResultCodeMatch(callback, expectedResultCode);
    }

    /**
     * 为了拿到 {@link Intent}
     *
     * @param callback 回调方法
     */
    @NonNull
    @AnyThread
    @CheckResult
    public NavigationDisposable navigateForIntentAndResultCodeMatch(
            @NonNull @UiThread final IntentCallback callback,
            final int expectedResultCode) {
        return delegate.navigateForIntentAndResultCodeMatch(callback, expectedResultCode);
    }

    /**
     * 为了拿到 {@link Intent}
     *
     * @param callback 回调方法
     */
    @AnyThread
    public void forwardForIntent(@NonNull @UiThread final IntentCallback callback) {
        delegate.forwardForIntent(callback);
    }

    /**
     * 为了拿到 {@link Intent}
     *
     * @param callback 回调方法
     */
    @NonNull
    @AnyThread
    @CheckResult
    public NavigationDisposable navigateForIntent(@NonNull @UiThread final IntentCallback callback) {
        return delegate.navigateForIntent(callback);
    }

    /**
     * 为了拿 {@link ActivityResult}
     *
     * @param callback 这里是为了拿返回的东西是不可以为空的
     */
    @AnyThread
    public void forwardForResult(@NonNull @UiThread final ActivityResultCallback callback) {
        delegate.forwardForResult(callback);
    }


    /**
     * 为了拿 {@link ActivityResult}
     *
     * @param callback 这里是为了拿返回的东西是不可以为空的
     */
    @NonNull
    @AnyThread
    @CheckResult
    public NavigationDisposable navigateForResult(@NonNull @UiThread final ActivityResultCallback callback) {
        return delegate.navigateForResult(callback);
    }

    /**
     * 没有返回值
     */
    @AnyThread
    public void forward() {
        delegate.forward();
    }

    /**
     * 没有返回值
     */
    @AnyThread
    public void navigation() {
        forward();
    }

    /**
     * @return 返回的对象有可能是一个空实现对象 {@link Router#emptyNavigationDisposable}
     */
    @NonNull
    @AnyThread
    @CheckResult
    public NavigationDisposable navigate() {
        return delegate.navigate();
    }

    /**
     * 没有返回值
     *
     * @param callback 路由的回调
     */
    @AnyThread
    public void forward(@Nullable @UiThread final ForwardCallback callback) {
        delegate.forward(callback);
    }

    @NonNull
    @AnyThread
    @CheckResult
    public synchronized NavigationDisposable navigate(@Nullable @UiThread final ForwardCallback callback) {
        return delegate.navigate(callback);
    }
    //endregion

}