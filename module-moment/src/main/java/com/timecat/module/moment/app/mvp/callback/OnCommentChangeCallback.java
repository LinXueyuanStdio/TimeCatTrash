package com.timecat.module.moment.app.mvp.callback;


import com.timecat.component.lightui.common.entity.bmob.CommentInfo;

/**
 * Created by 大灯泡 on 2016/12/9.
 * <p>
 * 评论Callback
 */

public interface OnCommentChangeCallback {

    void onAddComment(CommentInfo response);

    void onDeleteComment(String commentid);

}
