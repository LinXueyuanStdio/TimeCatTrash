package com.timecat.module.editor.markdown;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

public abstract class BaseEditorFragment extends Fragment {

    protected static boolean saved = false;
    protected static boolean fromFile = false;
    protected static boolean fromNote = false;
    protected static String fileName;
    protected static String filePath;
    protected static String fileContent;
    public View view;
    protected AppCompatActivity context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(getContext(),
                getContext().getTheme());
        LayoutInflater themeAwareInflater = inflater.cloneInContext(contextThemeWrapper);
        view = themeAwareInflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        context = (AppCompatActivity) getContext();
        if (!fromFile && !fromNote) {
            saved = false;
            fileName = null;
            filePath = null;
            fileContent = null;
        }
        initView();
        return view;
    }

    public abstract void initView();

    public abstract int getLayoutId();

    @Override
    public void onStart() {
        super.onStart();
        if (needEventBus() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        if (needEventBus() && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    public boolean needEventBus() {
        return false;
    }
}
