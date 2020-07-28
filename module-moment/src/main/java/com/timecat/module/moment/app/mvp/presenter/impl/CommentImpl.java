package com.timecat.module.moment.app.mvp.presenter.impl;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.timecat.component.lightui.common.entity.bmob.CommentInfo;
import com.timecat.component.lightui.common.request.AddCommentRequest;
import com.timecat.component.lightui.common.request.DeleteCommentRequest;
import com.timecat.component.lightui.common.request.SimpleResponseListener;
import com.timecat.module.moment.app.mvp.callback.OnCommentChangeCallback;
import com.timecat.module.moment.app.mvp.presenter.ICommentPresenter;

/**
 * Created by 大灯泡 on 2016/12/7.
 * <p>
 * 评论Model
 */

public class CommentImpl implements ICommentPresenter {
    @Override
    public void addComment(@NonNull String momentid,
                           @NonNull String authorid,
                           @Nullable String replyUserId,
                           @NonNull String content,
                           @NonNull final OnCommentChangeCallback onCommentChangeCallback) {
        if (onCommentChangeCallback == null) return;
        AddCommentRequest addCommentRequest = new AddCommentRequest();
        addCommentRequest.setContent(content);
        addCommentRequest.setMomentsInfoId(momentid);
        addCommentRequest.setAuthorId(authorid);
        if (!TextUtils.isEmpty(replyUserId)) {
            addCommentRequest.setReplyUserId(replyUserId);
        }
        addCommentRequest.setOnResponseListener(new SimpleResponseListener<CommentInfo>() {
            @Override
            public void onSuccess(CommentInfo response, int requestType) {
                onCommentChangeCallback.onAddComment(response);
            }
        });
        addCommentRequest.execute();
    }

    @Override
    public void deleteComment(@NonNull String commentid, @NonNull final OnCommentChangeCallback onCommentChangeCallback) {
        if (onCommentChangeCallback == null) return;
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest();
        deleteCommentRequest.setCommentid(commentid);
        deleteCommentRequest.setOnResponseListener(new SimpleResponseListener<String>() {
            @Override
            public void onSuccess(String response, int requestType) {
                onCommentChangeCallback.onDeleteComment(response);
            }
        });
        deleteCommentRequest.execute();

    }
}
