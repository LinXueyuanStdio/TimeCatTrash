package com.timecat.module.moment.app.mvp.view;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.timecat.component.lightui.common.entity.bmob.CommentInfo;
import com.timecat.component.lightui.common.entity.bmob.LikesInfo;
import com.timecat.component.lightui.common.entity.bmob.MomentsInfo;
import com.timecat.component.lightui.mvp.IBaseView;
import com.timecat.component.lightui.widget.commentwidget.CommentWidget;

import java.util.List;


/**
 * Created by 大灯泡 on 2016/12/7.
 */

public interface IMomentView extends IBaseView {

    void onLikeChange(int itemPos, List<LikesInfo> likeUserList);

    void onCommentChange(int itemPos, List<CommentInfo> commentInfoList);

    /**
     * 因为recyclerview通过位置找到itemview有可能会找不到对应的View，所以这次直接传值
     *
     * @param viewHolderRootView
     * @param itemPos
     * @param momentid
     * @param commentWidget
     */
    void showCommentBox(@Nullable View viewHolderRootView, int itemPos, String momentid,
                        CommentWidget commentWidget);

    void onDeleteMomentsInfo(@NonNull MomentsInfo momentsInfo);

}
