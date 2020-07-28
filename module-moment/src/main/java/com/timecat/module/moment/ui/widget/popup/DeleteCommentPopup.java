package com.timecat.module.moment.ui.widget.popup;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.timecat.component.lightui.common.entity.bmob.CommentInfo;
import com.timecat.module.moment.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 大灯泡 on 2016/4/22.
 * 删除评论的popup
 */
public class DeleteCommentPopup extends BasePopupWindow implements View.OnClickListener {

    private TextView mDel;
    private TextView mCancel;
    private CommentInfo commentInfo;

    public DeleteCommentPopup(Activity context) {
        super(context);

        mDel = (TextView) findViewById(R.id.delete);
        mCancel = (TextView) findViewById(R.id.cancel);

        setViewClickListener(this, mDel, mCancel);
        setBlurBackgroundEnable(true);
    }


    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 500);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 500);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.moment_popup_delete_comment);
    }


    public void showPopupWindow(CommentInfo commentInfo) {
        if (commentInfo == null) return;
        this.commentInfo = commentInfo;
        super.showPopupWindow();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.delete) {
            if (mDeleteCommentClickListener != null) {
                mDeleteCommentClickListener.onDelClick(commentInfo);
            }
            dismiss();

        } else if (i == R.id.cancel) {
            dismiss();

        } else {
        }
    }

    private OnDeleteCommentClickListener mDeleteCommentClickListener;

    public OnDeleteCommentClickListener getOnDeleteCommentClickListener() {
        return mDeleteCommentClickListener;
    }

    public void setOnDeleteCommentClickListener(OnDeleteCommentClickListener deleteCommentClickListener) {
        mDeleteCommentClickListener = deleteCommentClickListener;
    }

    public interface OnDeleteCommentClickListener {
        void onDelClick(CommentInfo commentInfo);
    }
}
