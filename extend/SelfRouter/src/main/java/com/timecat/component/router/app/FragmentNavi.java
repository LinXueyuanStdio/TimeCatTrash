package com.timecat.component.router.app;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xiaojinzi.component.impl.FragmentNavigator;
import com.xiaojinzi.component.support.Utils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/12/6
 * @description null
 * @usage null
 */
public class FragmentNavi {
    @NonNull
    FragmentNavigator delegate;

    public FragmentNavi(@NonNull FragmentNavigator delegate) {
        this.delegate = delegate;
    }

    public FragmentNavi putAll(@NonNull Bundle bundle) {
        Utils.checkNullPointer(bundle, "bundle");
        this.delegate.putAll(bundle);
        return this;
    }

    public FragmentNavi putBundle(@NonNull String key, @Nullable Bundle bundle) {
        this.delegate.putBundle(key, bundle);
        return this;
    }

    public FragmentNavi putCharSequence(@NonNull String key, @Nullable CharSequence value) {
        this.delegate.putCharSequence(key, value);
        return this;
    }

    public FragmentNavi putCharSequenceArray(@NonNull String key, @Nullable CharSequence[] value) {
        this.delegate.putCharSequenceArray(key, value);
        return this;
    }

    public FragmentNavi putCharSequenceArrayList(@NonNull String key, @Nullable ArrayList<CharSequence> value) {
        this.delegate.putCharSequenceArrayList(key, value);
        return this;
    }

    public FragmentNavi putByte(@NonNull String key, @Nullable byte value) {
        this.delegate.putByte(key, value);
        return this;
    }

    public FragmentNavi putByteArray(@NonNull String key, @Nullable byte[] value) {
        this.delegate.putByteArray(key, value);
        return this;
    }

    public FragmentNavi putChar(@NonNull String key, @Nullable char value) {
        this.delegate.putChar(key, value);
        return this;
    }

    public FragmentNavi putCharArray(@NonNull String key, @Nullable char[] value) {
        this.delegate.putCharArray(key, value);
        return this;
    }

    public FragmentNavi putBoolean(@NonNull String key, @Nullable boolean value) {
        this.delegate.putBoolean(key, value);
        return this;
    }

    public FragmentNavi putBooleanArray(@NonNull String key, @Nullable boolean[] value) {
        this.delegate.putBooleanArray(key, value);
        return this;
    }

    public FragmentNavi putString(@NonNull String key, @Nullable String value) {
        this.delegate.putString(key, value);
        return this;
    }

    public FragmentNavi putStringArray(@NonNull String key, @Nullable String[] value) {
        this.delegate.putStringArray(key, value);
        return this;
    }

    public FragmentNavi putStringArrayList(@NonNull String key, @Nullable ArrayList<String> value) {
        this.delegate.putStringArrayList(key, value);
        return this;
    }

    public FragmentNavi putShort(@NonNull String key, @Nullable short value) {
        this.delegate.putShort(key, value);
        return this;
    }

    public FragmentNavi putShortArray(@NonNull String key, @Nullable short[] value) {
        this.delegate.putShortArray(key, value);
        return this;
    }

    public FragmentNavi putInt(@NonNull String key, @Nullable int value) {
        this.delegate.putInt(key, value);
        return this;
    }

    public FragmentNavi putIntArray(@NonNull String key, @Nullable int[] value) {
        this.delegate.putIntArray(key, value);
        return this;
    }

    public FragmentNavi putIntegerArrayList(@NonNull String key, @Nullable ArrayList<Integer> value) {
        this.delegate.putIntegerArrayList(key, value);
        return this;
    }

    public FragmentNavi putLong(@NonNull String key, @Nullable long value) {
        this.delegate.putLong(key, value);
        return this;
    }

    public FragmentNavi putLongArray(@NonNull String key, @Nullable long[] value) {
        this.delegate.putLongArray(key, value);
        return this;
    }

    public FragmentNavi putFloat(@NonNull String key, @Nullable float value) {
        this.delegate.putFloat(key, value);
        return this;
    }

    public FragmentNavi putFloatArray(@NonNull String key, @Nullable float[] value) {
        this.delegate.putFloatArray(key, value);
        return this;
    }

    public FragmentNavi putDouble(@NonNull String key, @Nullable double value) {
        this.delegate.putDouble(key, value);
        return this;
    }

    public FragmentNavi putDoubleArray(@NonNull String key, @Nullable double[] value) {
        this.delegate.putDoubleArray(key, value);
        return this;
    }

    public FragmentNavi putParcelable(@NonNull String key, @Nullable Parcelable value) {
        this.delegate.putParcelable(key, value);
        return this;
    }

    public FragmentNavi putParcelableArray(@NonNull String key, @Nullable Parcelable[] value) {
        this.delegate.putParcelableArray(key, value);
        return this;
    }

    public FragmentNavi putParcelableArrayList(@NonNull String key, @Nullable ArrayList<? extends Parcelable> value) {
        this.delegate.putParcelableArrayList(key, value);
        return this;
    }

    public FragmentNavi putSparseParcelableArray(@NonNull String key, @Nullable SparseArray<? extends Parcelable> value) {
        this.delegate.putSparseParcelableArray(key, value);
        return this;
    }

    public FragmentNavi putSerializable(@NonNull String key, @Nullable Serializable value) {
        this.delegate.putSerializable(key, value);
        return this;
    }

    public FragmentNavi withAll(@NonNull Bundle bundle) {
        return this.putAll(bundle);
    }

    public FragmentNavi withBundle(@NonNull String key, @Nullable Bundle bundle) {
        return this.putBundle(key, bundle);
    }

    public FragmentNavi withCharSequence(@NonNull String key, @Nullable CharSequence value) {
        return this.putCharSequence(key, value);
    }

    public FragmentNavi withCharSequenceArray(@NonNull String key, @Nullable CharSequence[] value) {
        return this.putCharSequenceArray(key, value);
    }

    public FragmentNavi withCharSequenceArrayList(@NonNull String key, @Nullable ArrayList<CharSequence> value) {
        return this.putCharSequenceArrayList(key, value);
    }

    public FragmentNavi withByte(@NonNull String key, @Nullable byte value) {
        return this.putByte(key, value);
    }

    public FragmentNavi withByteArray(@NonNull String key, @Nullable byte[] value) {
        return this.putByteArray(key, value);
    }

    public FragmentNavi withChar(@NonNull String key, @Nullable char value) {
        return this.putChar(key, value);
    }

    public FragmentNavi withCharArray(@NonNull String key, @Nullable char[] value) {
        return this.putCharArray(key, value);
    }

    public FragmentNavi withBoolean(@NonNull String key, @Nullable boolean value) {
        return this.putBoolean(key, value);
    }

    public FragmentNavi withBooleanArray(@NonNull String key, @Nullable boolean[] value) {
        return this.putBooleanArray(key, value);
    }

    public FragmentNavi withString(@NonNull String key, @Nullable String value) {
        return this.putString(key, value);
    }

    public FragmentNavi withStringArray(@NonNull String key, @Nullable String[] value) {
        return this.putStringArray(key, value);
    }

    public FragmentNavi withStringArrayList(@NonNull String key, @Nullable ArrayList<String> value) {
        return this.putStringArrayList(key, value);
    }

    public FragmentNavi withShort(@NonNull String key, @Nullable short value) {
        return this.putShort(key, value);
    }

    public FragmentNavi withShortArray(@NonNull String key, @Nullable short[] value) {
        return this.putShortArray(key, value);
    }

    public FragmentNavi withInt(@NonNull String key, @Nullable int value) {
        return this.putInt(key, value);
    }

    public FragmentNavi withIntArray(@NonNull String key, @Nullable int[] value) {
        return this.putIntArray(key, value);
    }

    public FragmentNavi withIntegerArrayList(@NonNull String key, @Nullable ArrayList<Integer> value) {
        return this.putIntegerArrayList(key, value);
    }

    public FragmentNavi withLong(@NonNull String key, @Nullable long value) {
        return this.putLong(key, value);
    }

    public FragmentNavi withLongArray(@NonNull String key, @Nullable long[] value) {
        return this.putLongArray(key, value);
    }

    public FragmentNavi withFloat(@NonNull String key, @Nullable float value) {
        return this.putFloat(key, value);
    }

    public FragmentNavi withFloatArray(@NonNull String key, @Nullable float[] value) {
        return this.putFloatArray(key, value);
    }

    public FragmentNavi withDouble(@NonNull String key, @Nullable double value) {
        return this.putDouble(key, value);
    }

    public FragmentNavi withDoubleArray(@NonNull String key, @Nullable double[] value) {
        return this.putDoubleArray(key, value);
    }

    public FragmentNavi withParcelable(@NonNull String key, @Nullable Parcelable value) {
        return this.putParcelable(key, value);
    }

    public FragmentNavi withParcelableArray(@NonNull String key, @Nullable Parcelable[] value) {
        return this.putParcelableArray(key, value);
    }

    public FragmentNavi withParcelableArrayList(@NonNull String key, @Nullable ArrayList<? extends Parcelable> value) {
        return this.putParcelableArrayList(key, value);
    }

    public FragmentNavi withSparseParcelableArray(@NonNull String key, @Nullable SparseArray<? extends Parcelable> value) {
        return this.putSparseParcelableArray(key, value);
    }

    public FragmentNavi withSerializable(@NonNull String key, @Nullable Serializable value) {
        return this.putSerializable(key, value);
    }

    @Nullable
    public Fragment navigate() {
        return delegate.navigate();
    }

    @Nullable
    public Fragment navigation() {
        return delegate.navigate();
    }
}
