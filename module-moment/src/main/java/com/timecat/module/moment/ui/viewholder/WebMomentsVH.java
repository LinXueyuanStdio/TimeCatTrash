package com.timecat.module.moment.ui.viewholder;

import android.view.View;

import androidx.annotation.NonNull;

import com.timecat.component.lightui.common.MomentsType;
import com.timecat.component.lightui.common.entity.bmob.MomentsInfo;
import com.timecat.module.moment.R;


/**
 * Created by 大灯泡 on 2016/11/3.
 * <p>
 * 網頁vh
 *
 * @see MomentsType
 */

public class WebMomentsVH extends CircleBaseViewHolder {

    @Override
    public int layoutId() {
        return R.layout.moment_moments_web;
    }


    public WebMomentsVH(View itemView, int viewType) {
        super(itemView, viewType);
    }

    @Override
    public void onFindView(@NonNull View rootView) {

    }

    @Override
    public void onBindDataToView(@NonNull MomentsInfo data, int position, int viewType) {

    }
}
