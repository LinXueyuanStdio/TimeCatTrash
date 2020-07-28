package com.timecat.module.moment.ui.widget.praisewidget;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.View;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.timecat.component.bmob.data._User;
import com.timecat.component.lightui.util.UIHelper;
import com.timecat.component.lightui.widget.span.ClickableSpanEx;
import com.timecat.component.readonly.RouterHub;

/**
 * Created by 大灯泡 on 2016/2/21. 点击事件
 */
public class PraiseClick extends ClickableSpanEx {

    private static final int DEFAULT_COLOR = 0xff517fae;
    private int color;
    private Context mContext;
    private int textSize;
    private _User mPraiseInfo;

    private PraiseClick() {
    }


    private PraiseClick(Builder builder) {
        super(builder.color, builder.clickBgColor);
        mContext = builder.mContext;
        mPraiseInfo = builder.mPraiseInfo;
        this.textSize = builder.textSize;
    }

    @Override
    public void onClickEx(View widget, CharSequence text) {

    }

    @Override
    public void onClick(View widget) {
        if (mPraiseInfo != null) {
            ARouter.getInstance().build(RouterHub.USER_UserDetailActivity)
                    .withString("userId", mPraiseInfo.getUserid())
                    .navigation();
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setTextSize(textSize);
        ds.setTypeface(Typeface.DEFAULT_BOLD);
    }


    public static class Builder {

        private int color;
        private Context mContext;
        private int textSize = 16;
        private _User mPraiseInfo;
        private int clickBgColor;

        public Builder(Context context, @NonNull _User info) {
            mContext = context;
            mPraiseInfo = info;
        }

        public Builder setTextSize(int textSize) {
            this.textSize = UIHelper.sp2px(textSize);
            return this;
        }

        public Builder setColor(int color) {
            this.color = color;
            return this;
        }

        public Builder setClickEventColor(int color) {
            this.clickBgColor = color;
            return this;
        }

        public PraiseClick build() {
            return new PraiseClick(this);
        }
    }
}
