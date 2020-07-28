package com.timecat.module.moment.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.timecat.component.lightui.base.adapter.BaseMultiRecyclerViewAdapter;
import com.timecat.component.lightui.base.adapter.BaseRecyclerViewHolder;
import com.timecat.component.lightui.common.MomentsType;
import com.timecat.component.lightui.common.entity.bmob.MomentsInfo;
import com.timecat.module.moment.R;
import com.timecat.module.moment.app.mvp.presenter.impl.MomentPresenter;
import com.timecat.module.moment.ui.viewholder.CircleBaseViewHolder;
import com.timecat.module.moment.ui.viewholder.EmptyMomentsVH;
import com.timecat.module.moment.ui.viewholder.MultiImageMomentsVH;
import com.timecat.module.moment.ui.viewholder.TextOnlyMomentsVH;
import com.timecat.module.moment.ui.viewholder.WebMomentsVH;

import java.util.List;

/**
 * Created by 大灯泡 on 2016/11/1.
 * <p>
 * 朋友圈adapter
 */

public class CircleMomentsAdapter extends
        BaseMultiRecyclerViewAdapter<CircleMomentsAdapter, MomentsInfo> {

    private MomentPresenter momentPresenter;

    public CircleMomentsAdapter(@NonNull Context context, @NonNull List<MomentsInfo> datas,
                                MomentPresenter presenter) {
        super(context, datas);
        this.momentPresenter = presenter;
    }

    @Override
    protected void onInitViewHolder(BaseRecyclerViewHolder holder) {
        if (holder instanceof CircleBaseViewHolder) {
            ((CircleBaseViewHolder) holder).setPresenter(momentPresenter);
        }
    }

    @Override
    protected int getLayoutResId(int viewType) {
        switch (viewType) {
            case MomentsType.EMPTY_CONTENT:
                return R.layout.moment_moments_empty_content;
            case MomentsType.MULTI_IMAGES:
                return R.layout.moment_moments_multi_image;
            case MomentsType.TEXT_ONLY:
                return R.layout.moment_moments_only_text;
            case MomentsType.WEB:
                return R.layout.moment_moments_web;
        }
        return R.layout.moment_moments_empty_content;
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(ViewGroup parent, View rootView,
                                                   int viewType) {
        rootView = View.inflate(context, getLayoutResId(viewType), null);
        switch (viewType) {
            case MomentsType.EMPTY_CONTENT:
                return new EmptyMomentsVH(rootView, viewType);
            case MomentsType.MULTI_IMAGES:
                return new MultiImageMomentsVH(rootView, viewType);
            case MomentsType.TEXT_ONLY:
                return new TextOnlyMomentsVH(rootView, viewType);
            case MomentsType.WEB:
                return new WebMomentsVH(rootView, viewType);
        }
        return new EmptyMomentsVH(rootView, viewType);
    }
}
