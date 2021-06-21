package com.timecat.component.router.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timecat.component.router.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-07-29
 * @description null
 * @usage null
 */
public class FallBackFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(getContext(), getContext().getTheme());
        LayoutInflater themeAwareInflater = inflater.cloneInContext(contextThemeWrapper);
        return themeAwareInflater.inflate(R.layout.router_fragment_fallback, container, false);
    }
}
