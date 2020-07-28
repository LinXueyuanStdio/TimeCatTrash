package com.timecat.module.moment.ui.widget.popup;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.timecat.component.bmob.dao.UserDao;
import com.timecat.component.lightui.WeakHandler;
import com.timecat.component.lightui.common.entity.bmob.LikesInfo;
import com.timecat.component.lightui.common.entity.bmob.MomentsInfo;
import com.timecat.module.moment.R;

import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 大灯泡 on 2016/3/6.
 * 朋友圈点赞
 */
public class CommentPopup extends BasePopupWindow implements View.OnClickListener {
    private static final String TAG = "CommentPopup";

    private ImageView mLikeView;
    private ImageView mLikeViewAnimate;
    private TextView mLikeText;

    private ViewGroup mLikeClikcLayout;
    private ViewGroup mCommentClickLayout;

    private MomentsInfo mMomentsInfo;

    private WeakHandler handler;
    private ScaleAnimation mScaleAnimation;

    private OnCommentPopupClickListener mOnCommentPopupClickListener;

    //是否已经点赞
    private boolean hasLiked;

    public CommentPopup(Context context) {
        super(context);
        handler = new WeakHandler();

        mLikeView = findViewById(R.id.iv_like);
        mLikeViewAnimate = findViewById(R.id.iv_like_blue);
        mLikeText = findViewById(R.id.tv_like);

        mLikeClikcLayout = findViewById(R.id.item_like);
        mCommentClickLayout = findViewById(R.id.item_comment);

        mLikeClikcLayout.setOnClickListener(this);
        mCommentClickLayout.setOnClickListener(this);

        buildAnima();
        setAllowInterceptTouchEvent(false);
        setAllowDismissWhenTouchOutside(true);
        setPopupFadeEnable(false);
        setBackground(0);
        setPopupGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        TranslateAnimation showAnima = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                1f,
                Animation.RELATIVE_TO_PARENT,
                0,
                Animation.RELATIVE_TO_PARENT,
                0,
                Animation.RELATIVE_TO_PARENT,
                0);
        showAnima.setInterpolator(new FastOutSlowInInterpolator());
        showAnima.setDuration(250);
        showAnima.setFillAfter(true);
        return showAnima;
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        TranslateAnimation showAnima = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                0,
                Animation.RELATIVE_TO_PARENT,
                1f,
                Animation.RELATIVE_TO_PARENT,
                0,
                Animation.RELATIVE_TO_PARENT,
                0);
        showAnima.setInterpolator(new FastOutSlowInInterpolator());
        showAnima.setDuration(250);
        showAnima.setFillAfter(true);
        return showAnima;
    }

    private void buildAnima() {
        mScaleAnimation = new ScaleAnimation(1f, 2f, 1f, 2f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mScaleAnimation.setDuration(250);
        mScaleAnimation.setInterpolator(new SpringInterPolator());
        mScaleAnimation.setFillAfter(false);

        mScaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mLikeViewAnimate.setAlpha(1f);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLikeViewAnimate.setAlpha(0f);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 150);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.moment_popup_comment);
    }

    @Override
    public boolean onOutSideTouch() {
        dismiss(false);
        return super.onOutSideTouch();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.item_like) {
            if (mOnCommentPopupClickListener != null) {
                mOnCommentPopupClickListener.onLikeClick(v, mMomentsInfo, hasLiked);
                mLikeViewAnimate.clearAnimation();
                mLikeViewAnimate.startAnimation(mScaleAnimation);
            }

        } else if (i == R.id.item_comment) {
            if (mOnCommentPopupClickListener != null) {
                mOnCommentPopupClickListener.onCommentClick(v, mMomentsInfo);
                dismissWithOutAnimate();
            }

        }
    }
    //=============================================================Getter/Setter

    public OnCommentPopupClickListener getOnCommentPopupClickListener() {
        return mOnCommentPopupClickListener;
    }

    public void setOnCommentPopupClickListener(OnCommentPopupClickListener onCommentPopupClickListener) {
        mOnCommentPopupClickListener = onCommentPopupClickListener;
    }


    public void updateMomentInfo(@NonNull MomentsInfo info) {
        this.mMomentsInfo = info;
        hasLiked = false;
        if (!isListEmpty(info.getLikesList())) {
            for (LikesInfo likesInfo : info.getLikesList()) {
                if (TextUtils.equals(likesInfo.getUserid(), UserDao.getCurrentUser().getObjectId())) {
                    hasLiked = true;
                    break;
                }
            }
        }
        mLikeText.setText(hasLiked ? "取消" : "赞");

    }

    boolean isListEmpty(List<?> datas) {
        return datas == null || datas.size() <= 0;
    }

    //=============================================================InterFace
    public interface OnCommentPopupClickListener {
        void onLikeClick(View v, @NonNull MomentsInfo info, boolean hasLiked);

        void onCommentClick(View v, @NonNull MomentsInfo info);
    }

    static class SpringInterPolator extends LinearInterpolator {

        public SpringInterPolator() {
        }


        @Override
        public float getInterpolation(float input) {
            return (float) Math.sin(input * Math.PI);
        }
    }
}
