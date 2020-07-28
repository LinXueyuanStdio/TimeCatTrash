package com.timecat.module.moment.ui.viewholder;

import android.app.Activity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.InputCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.timecat.component.alert.ToastUtil;
import com.timecat.component.bmob.dao.UserDao;
import com.timecat.component.bmob.data._User;
import com.timecat.component.bmob.listener.OnResponseListener;
import com.timecat.component.commonsdk.utils.override.LogUtil;
import com.timecat.component.commonsdk.utils.string.StringUtil;
import com.timecat.component.lightui.base.adapter.BaseMultiRecyclerViewHolder;
import com.timecat.component.lightui.common.entity.bmob.CommentInfo;
import com.timecat.component.lightui.common.entity.bmob.LikesInfo;
import com.timecat.component.lightui.common.entity.bmob.MomentsInfo;
import com.timecat.component.lightui.common.request.AddMomentsRequest;
import com.timecat.component.lightui.imageloader.ImageLoadManager;
import com.timecat.component.lightui.util.ViewUtil;
import com.timecat.component.lightui.widget.commentwidget.CommentContentsLayout;
import com.timecat.component.lightui.widget.commentwidget.CommentWidget;
import com.timecat.component.lightui.widget.commentwidget.IComment;
import com.timecat.component.lightui.widget.common.ClickShowMoreLayout;
import com.timecat.component.lightui.widget.imageview.RoundedImageView;
import com.timecat.component.lightui.widget.popup.PopupProgress;
import com.timecat.component.readonly.RouterHub;
import com.timecat.component.setting.DEF;
import com.timecat.module.moment.R;
import com.timecat.module.moment.TimeUtil;
import com.timecat.module.moment.app.mvp.presenter.impl.MomentPresenter;
import com.timecat.module.moment.ui.widget.popup.CommentPopup;
import com.timecat.module.moment.ui.widget.popup.DeleteCommentPopup;
import com.timecat.module.moment.ui.widget.praisewidget.PraiseWidget;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 大灯泡 on 2016/11/1.
 * <p>
 * 朋友圈基本item
 */
public abstract class CircleBaseViewHolder extends
        BaseMultiRecyclerViewHolder<MomentsInfo> implements BaseMomentVH<MomentsInfo> {


    //头部
    protected RoundedImageView avatar;
    protected TextView nick;
    protected TextView helpYourself;
    protected ClickShowMoreLayout userText;

    //底部
    protected TextView createTime;
    protected TextView deleteMoments;
    protected ImageView commentImage;
    protected LinearLayout commentAndPraiseLayout;
    protected PraiseWidget praiseWidget;
    protected View line;
    protected CommentContentsLayout commentLayout;

    //内容区
    protected LinearLayout contentLayout;

    private CommentPopup commentPopup;
    private DeleteCommentPopup deleteCommentPopup;

    private MomentPresenter momentPresenter;
    private int itemPosition;
    private MomentsInfo momentsInfo;

    @LayoutRes
    public abstract int layoutId();

    public CircleBaseViewHolder(View itemView, int viewType) {
        super(itemView, viewType);
        onFindView(itemView);

        //header
        avatar = (RoundedImageView) findView(avatar, R.id.avatar);
        nick = (TextView) findView(nick, R.id.nick);
        helpYourself = (TextView) findView(helpYourself, R.id.help_yourself);
        userText = (ClickShowMoreLayout) findView(userText, R.id.item_text_field);
        userText.setOnStateKeyGenerateListener(originKey -> originKey + itemPosition);

        //bottom
        createTime = (TextView) findView(createTime, R.id.create_time);
        deleteMoments = (TextView) findView(deleteMoments, R.id.tv_delete_moment);
        commentImage = (ImageView) findView(commentImage, R.id.menu_img);
        ViewUtil.expandViewTouchDelegate(commentImage, 40, 40, 40, 40);
        commentAndPraiseLayout = (LinearLayout) findView(commentAndPraiseLayout,
                R.id.comment_praise_layout);
        praiseWidget = (PraiseWidget) findView(praiseWidget, R.id.praise);
        line = findView(line, R.id.divider);
        commentLayout = (CommentContentsLayout) findView(commentLayout, R.id.comment_layout);
        commentLayout.setMode(CommentContentsLayout.Mode.NORMAL);
        commentLayout.setOnCommentItemClickListener(widget -> {
            IComment comment = widget.getData();
            CommentInfo commentInfo = null;
            if (comment instanceof CommentInfo) {
                commentInfo = (CommentInfo) comment;
            }
            if (commentInfo == null) {
                return;
            }
            if (commentInfo.canDelete()) {
                deleteCommentPopup.showPopupWindow(commentInfo);
            } else {
                momentPresenter.showCommentBox(null, itemPosition, momentsInfo.getMomentid(), widget);
            }
        });
        CommentContentsLayout.OnCommentItemLongClickListener onCommentItemLongClickListener = new CommentContentsLayout.OnCommentItemLongClickListener() {
            @Override
            public boolean onCommentWidgetLongClick(@NonNull CommentWidget widget) {
                return false;
            }
        };
        commentLayout.setOnCommentItemLongClickListener(onCommentItemLongClickListener);
        /**
         * ==================  click listener block
         */
        commentLayout.setOnCommentWidgetItemClickListener((comment, text) -> {
            if (comment instanceof CommentInfo) {
                ARouter.getInstance().build(RouterHub.USER_UserDetailActivity)
                        .withString("userId", ((CommentInfo) comment).getAuthor().getUserid())
                        .navigation();
            }
        });
        // FIXME: 2018/1/3 暂时未开发完
//        commentLayout.setMode(CommentContentsLayout.Mode.EXPANDABLE);
        //content
        contentLayout = (LinearLayout) findView(contentLayout, R.id.content);

        if (commentPopup == null) {
            commentPopup = new CommentPopup(getContext());
            commentPopup.setOnCommentPopupClickListener(new CommentPopup.OnCommentPopupClickListener() {
                @Override
                public void onLikeClick(View v, @NonNull MomentsInfo info, boolean hasLiked) {
                    if (hasLiked) {
                        momentPresenter.unLike(itemPosition, info.getLikesObjectid(), info.getLikesList());
                    } else {
                        momentPresenter.addLike(itemPosition, info.getMomentid(), info.getLikesList());
                    }

                }

                @Override
                public void onCommentClick(View v, @NonNull MomentsInfo info) {
                    momentPresenter.showCommentBox(itemView, itemPosition, info.getMomentid(), null);
                }
            });
        }

        if (deleteCommentPopup == null) {
            deleteCommentPopup = new DeleteCommentPopup((Activity) getContext());
            deleteCommentPopup.setOnDeleteCommentClickListener(commentInfo -> momentPresenter
                    .deleteComment(itemPosition, commentInfo.getCommentid(),
                            momentsInfo.getCommentList()));
        }
    }

    public void setPresenter(MomentPresenter momentPresenter) {
        this.momentPresenter = momentPresenter;
    }

    public MomentPresenter getPresenter() {
        return momentPresenter;
    }

    @Override
    public void onBindData(MomentsInfo data, int position) {
        if (data == null) {
            LogUtil.e("数据是空的！！！！");
            findView(userText, R.id.item_text_field);
            userText.setText("这个动态的数据是空的。。。。OMG");
            return;
        }
        this.momentsInfo = data;
        this.itemPosition = position;
        //通用数据绑定
        onBindMutualDataToViews(data);
        //点击事件
        commentImage.setOnClickListener(onMenuButtonClickListener);
        commentImage.setTag(R.id.momentinfo_data_tag_id, data);
        deleteMoments.setOnClickListener(onDeleteMomentClickListener);
        //传递到子类
        onBindDataToView(data, position, getViewType());
    }

    private void onBindMutualDataToViews(@NonNull MomentsInfo data) {
        //header
        ImageLoadManager.INSTANCE.loadImage(avatar, data.getAuthor().getAvatar());
        nick.setText(data.getAuthor().getNick());
        OnClickListener userId = v -> ARouter.getInstance().build(RouterHub.USER_UserDetailActivity)
                .withString("userId", data.getAuthor().getUserid())
                .navigation();
        avatar.setOnClickListener(userId);
        nick.setOnClickListener(userId);
        helpYourself.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.i("装作是开发者推荐，悄悄出现在TA的眼前");
                recommend(data);
            }
        });

        userText.setText(data.getContent().getText());
        ViewUtil.setViewsVisible(StringUtil.noEmpty(data.getContent().getText()) ?
                View.VISIBLE : View.GONE, userText);

        //bottom
        createTime.setText(TimeUtil.getTimeStringFromBmob(data.getCreatedAt()));
        ViewUtil.setViewsVisible(
                TextUtils.equals(momentsInfo.getAuthor().getUserid(),
                        UserDao.getCurrentUser().getObjectId())
                        ? View.VISIBLE
                        : View.GONE,
                deleteMoments);
        boolean needPraiseData = addLikes(data.getLikesList());
        boolean needCommentData = commentLayout.addComments(data.getCommentList());
        praiseWidget.setVisibility(needPraiseData ? View.VISIBLE : View.GONE);
        commentLayout.setVisibility(needCommentData ? View.VISIBLE : View.GONE);
        line.setVisibility(needPraiseData && needCommentData ? View.VISIBLE : View.GONE);
        commentAndPraiseLayout
                .setVisibility(needCommentData || needPraiseData ? View.VISIBLE : View.GONE);

    }

    protected void recommend(@NonNull MomentsInfo data) {
        long last_recommend_moment_time = DEF.setting().getLong("last_recommend_moment_time", 0);
        long current_time = System.currentTimeMillis();
        boolean canRecommend = current_time - last_recommend_moment_time > 24 * 60 * 60 * 1000;
        if (canRecommend) {
            new MaterialDialog.Builder(getContext())
                    .title("推荐本时间片")
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input("用户名", "", false, new InputCallback() {
                        @Override
                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            String username = input.toString();
                            BmobQuery<_User> q = new BmobQuery<>();
                            q.addWhereEqualTo("username", username);
                            q.findObjects(new FindListener<_User>() {
                                @Override
                                public void done(List<_User> list, BmobException e) {
                                    if (list != null && list.size() > 0) {
                                        publish(list.get(0).getObjectId(), data);
                                    } else {
                                        ToastUtil.e("自助推荐失败");
                                    }
                                }
                            });
                        }
                    }).show();
        } else {
            ToastUtil.w("一天内只能自助推荐一次喵~");
        }
    }

    public PopupProgress mPopupProgress;

    public void publish(String TA, @NonNull MomentsInfo data) {
        if (mPopupProgress == null) {
            mPopupProgress = new PopupProgress(getContext());
        }
        mPopupProgress.setProgressTips("正在推荐");
        if (!mPopupProgress.isShowing()) {
            mPopupProgress.showPopupWindow();
        }
        AddMomentsRequest addMomentsRequest = new AddMomentsRequest();
        addMomentsRequest.setAuthId(data.getAuthor().getUserid())
                .setHostId(TA)
                .addPictureBuckets(data.getContent() == null ? null : data.getContent().getPics())
                .addText(data.getContent() == null ? "" : data.getContent().getText());
        addMomentsRequest
                .setOnResponseListener(new OnResponseListener.SimpleResponseListener<String>() {
                    @Override
                    public void onSuccess(String response, int requestType) {
                        mPopupProgress.dismiss();
                        ToastUtil.ok("自助推荐成功");
                        DEF.setting().putLong("last_recommend_moment_time", System.currentTimeMillis());
                    }

                    @Override
                    public void onError(BmobException e, int requestType) {
                        mPopupProgress.dismiss();
                        ToastUtil.e(e.toString());
                    }
                });
        addMomentsRequest.execute();
    }


    /**
     * 添加点赞
     *
     * @return ture=显示点赞，false=不显示点赞
     */
    private boolean addLikes(List<LikesInfo> likesList) {
        if (likesList == null || likesList.size() <= 0) {
            return false;
        }
        praiseWidget.setDatas(likesList);
        return true;
    }

    private View.OnClickListener onDeleteMomentClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            momentPresenter.deleteMoments(v.getContext(), momentsInfo);
        }
    };


    private View.OnClickListener onMenuButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MomentsInfo info = (MomentsInfo) v.getTag(R.id.momentinfo_data_tag_id);
            if (info != null) {
                commentPopup.updateMomentInfo(info);
                commentPopup.showPopupWindow(commentImage);
            }
        }
    };


    /**
     * ============  tools method block
     */


    protected final View findView(View view, int resid) {
        if (resid > 0 && itemView != null && view == null) {
            return itemView.findViewById(resid);
        }
        return view;
    }


}
