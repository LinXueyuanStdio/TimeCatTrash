package com.timecat.module.moment.activity.circledemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.timecat.component.alert.ToastUtil;
import com.timecat.component.bmob.dao.UserDao;
import com.timecat.component.bmob.data._User;
import com.timecat.component.bmob.listener.OnResponseListener;
import com.timecat.component.lightui.base.BaseTitleBarActivity;
import com.timecat.component.lightui.common.entity.ImageInfo;
import com.timecat.component.lightui.common.entity.bmob.CommentInfo;
import com.timecat.component.lightui.common.entity.bmob.LikesInfo;
import com.timecat.component.lightui.common.entity.bmob.MomentsInfo;
import com.timecat.component.lightui.common.request.MomentsRequest;
import com.timecat.component.lightui.helper.AppFileHelper;
import com.timecat.component.lightui.helper.PhotoHelper;
import com.timecat.component.lightui.imageloader.ImageLoadManager;
import com.timecat.component.lightui.manager.KeyboardControlMnanager;
import com.timecat.component.lightui.util.UIHelper;
import com.timecat.component.lightui.widget.commentwidget.CommentBox;
import com.timecat.component.lightui.widget.commentwidget.CommentWidget;
import com.timecat.component.lightui.widget.commentwidget.IComment;
import com.timecat.component.lightui.widget.common.TitleBarMode;
import com.timecat.component.lightui.widget.popup.SelectPhotoMenuPopup;
import com.timecat.component.lightui.widget.pullrecyclerview.CircleRecyclerView;
import com.timecat.component.lightui.widget.pullrecyclerview.CircleRecyclerView.OnPreDispatchTouchListener;
import com.timecat.component.lightui.widget.pullrecyclerview.interfaces.OnRefreshListener2;
import com.timecat.component.readonly.RouterHub;
import com.timecat.module.moment.R;
import com.timecat.module.moment.activity.ActivityLauncher;
import com.timecat.module.moment.app.mvp.presenter.impl.MomentPresenter;
import com.timecat.module.moment.app.mvp.view.IMomentView;
import com.timecat.module.moment.ui.adapter.CircleMomentsAdapter;
import com.timecat.module.moment.ui.helper.TitleBarAlphaChangeHelper;
import com.timecat.module.moment.ui.widget.popup.PopupTextAction;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;


/**
 * Created by 大灯泡 on 2016/10/26.
 * <p>
 * 朋友圈主界面
 */
@Route(path = RouterHub.MOMENT_MomentFriendCircleActivity)
public class MomentFriendCircleActivity extends BaseTitleBarActivity implements OnRefreshListener2,
        IMomentView, OnPreDispatchTouchListener {

    private static final int REQUEST_REFRESH = 0x10;
    private static final int REQUEST_LOADMORE = 0x11;

    private CircleRecyclerView circleRecyclerView;
    private CommentBox commentBox;
    private HostViewHolder hostViewHolder;
    private CircleMomentsAdapter adapter;
    private List<MomentsInfo> momentsInfoList;
    //request
    private MomentsRequest momentsRequest;
    private MomentPresenter presenter;

    private CircleViewHelper mViewHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moment_activity_main);
        momentsInfoList = new ArrayList<>();
        momentsRequest = new MomentsRequest();
        initView();
        initKeyboardHeightObserver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppFileHelper.initStroagePath(this);
    }

    @Override
    public void onHandleIntent(Intent intent) {

    }


    private void initView() {
        if (mViewHelper == null) {
            mViewHelper = new CircleViewHelper(this);
        }
        getTitleBar().getLeftTextView().setAlpha(0f);
        getTitleBar().setLeftText("");
        setLeftTextColor(Color.parseColor("#040404"));
        setTitleMode(TitleBarMode.MODE_BOTH);
        setTitleRightIcon(R.drawable.ic_camera);
        setTitleLeftIcon(R.drawable.back_left);
        setTitleBarBackground(Color.TRANSPARENT);
        presenter = new MomentPresenter(this);

        hostViewHolder = new HostViewHolder(this);
        circleRecyclerView = (CircleRecyclerView) findViewById(R.id.recycler);
        circleRecyclerView.setOnRefreshListener(this);
        circleRecyclerView.setOnPreDispatchTouchListener(this);
        circleRecyclerView.addHeaderView(hostViewHolder.getView());

        commentBox = (CommentBox) findViewById(R.id.widget_comment);
        commentBox.setOnCommentSendClickListener(onCommentSendClickListener);

        adapter = new CircleMomentsAdapter(this, momentsInfoList, presenter);

        circleRecyclerView.setAdapter(adapter);
        circleRecyclerView.autoRefresh();

        TitleBarAlphaChangeHelper.handle(getTitleBar(),
                circleRecyclerView.getRecyclerView(),
                hostViewHolder.friend_avatar,
                (alpha, color) -> {
                    setStatusBarDark(alpha > 1);
                    setStatusBarHolderBackgroundColor(color);
                });

    }

    private void initKeyboardHeightObserver() {
        //观察键盘弹出与消退
        KeyboardControlMnanager.observerKeyboardVisibleChange(this,
                new KeyboardControlMnanager.OnKeyboardStateChangeListener() {
                    View anchorView;

                    @Override
                    public void onKeyboardChange(int keyboardHeight, boolean isVisible) {
                        int commentType = commentBox.getCommentType();
                        if (isVisible) {
                            //定位评论框到view
                            commentBox.setTranslationY(-keyboardHeight + commentBox.getHeight() - UIHelper
                                    .getStatusBarHeight(MomentFriendCircleActivity.this));
                            anchorView = mViewHelper
                                    .alignCommentBoxToView(circleRecyclerView, commentBox, commentType);
                        } else {
                            //定位到底部
                            commentBox.setTranslationY(0);
                            commentBox.dismissCommentBox(false);
                            mViewHelper
                                    .alignCommentBoxToViewWhenDismiss(circleRecyclerView, commentBox, commentType,
                                            anchorView);
                        }
                    }
                });
    }

    @Override
    protected boolean isTranslucentStatus() {
        return true;
    }

    @Override
    protected boolean isFitsSystemWindows() {
        return false;
    }

    @Override
    public void onRefresh() {
        Log.i("onRefresh", "onRefresh");
        momentsRequest.setOnResponseListener(momentsRequestCallBack);
        momentsRequest.setRequestType(REQUEST_REFRESH);
        momentsRequest.setCurPage(0);
        momentsRequest.execute();
    }

    @Override
    public void onLoadMore() {
        momentsRequest.setOnResponseListener(momentsRequestCallBack);
        momentsRequest.setRequestType(REQUEST_LOADMORE);
        momentsRequest.execute();
    }

    //titlebar click


    @Override
    public boolean onTitleLongClick(View v) {
        new PopupTextAction(this)
                .setTitle("开发工具")
                .setTitleColor(UIHelper.getColor(R.color.text_gray))
                .addData("跳转到Fragment朋友圈", 1)
                .setOnSelectedListener(new PopupTextAction.OnActionClickedListener() {
                    @Override
                    public void onClicked(CharSequence action, int actionCode) {
                        ActivityLauncher
                                .startToFriendCircleFragmentDemoActivity(MomentFriendCircleActivity.this);
                    }
                })
                .showPopupWindow();
        return super.onTitleLongClick(v);
    }

    @Override
    public void onTitleDoubleClick() {
        super.onTitleDoubleClick();
        if (circleRecyclerView != null) {
            int firstVisibleItemPos = circleRecyclerView.findFirstVisibleItemPosition();
            circleRecyclerView.getRecyclerView().smoothScrollToPosition(0);
            if (firstVisibleItemPos > 1) {
                circleRecyclerView.postDelayed(() -> circleRecyclerView.autoRefresh(), 200);
            }
        }

    }

    @Override
    public void onTitleLeftClick() {
        finish();
    }

    @Override
    public void onTitleRightClick() {
        new SelectPhotoMenuPopup(this).setOnSelectPhotoMenuClickListener(
                new SelectPhotoMenuPopup.OnSelectPhotoMenuClickListener() {
                    @Override
                    public void onShootClick() {
                        PhotoHelper.fromCamera(MomentFriendCircleActivity.this, false);
                    }

                    @Override
                    public void onAlbumClick() {
                        ActivityLauncher.startToPhotoSelectActivity(getActivity(),
                                RouterHub.PhotoSelectActivity.requestCode);
                    }
                }).showPopupWindow();
    }

    @Override
    public boolean onTitleRightLongClick() {
        ActivityLauncher.startToPublishActivityWithResult(this,
                RouterHub.PublishActivity.MODE_TEXT, null,
                RouterHub.PublishActivity.requestCode);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoHelper
                .handleActivityResult(this, requestCode, resultCode, data, new PhotoHelper.PhotoCallback() {
                    @Override
                    public void onFinish(String filePath) {
                        List<ImageInfo> selectedPhotos = new ArrayList<ImageInfo>();
                        selectedPhotos.add(new ImageInfo(filePath, null, null, 0, 0));
                        ActivityLauncher.startToPublishActivityWithResult(MomentFriendCircleActivity.this,
                                RouterHub.PublishActivity.MODE_MULTI,
                                selectedPhotos,
                                RouterHub.PublishActivity.requestCode);
                    }

                    @Override
                    public void onError(String msg) {
                        ToastUtil.e(msg);
                    }
                });
        if (requestCode == RouterHub.PhotoSelectActivity.requestCode && resultCode == RESULT_OK) {
            List<ImageInfo> selectedPhotos = data
                    .getParcelableArrayListExtra(RouterHub.PhotoSelectActivity.key_result);
            if (selectedPhotos != null) {
                ActivityLauncher
                        .startToPublishActivityWithResult(this, RouterHub.PublishActivity.MODE_MULTI,
                                selectedPhotos, RouterHub.PublishActivity.requestCode);
            }
        }

        if (requestCode == RouterHub.PublishActivity.requestCode && resultCode == RESULT_OK) {
            circleRecyclerView.autoRefresh();
        }
    }

    //request
    //==============================================
    private OnResponseListener.SimpleResponseListener<List<MomentsInfo>> momentsRequestCallBack = new OnResponseListener.SimpleResponseListener<List<MomentsInfo>>() {
        @Override
        public void onSuccess(List<MomentsInfo> response, int requestType) {
            circleRecyclerView.compelete();
            switch (requestType) {
                case REQUEST_REFRESH:
                    if (response != null && response.size() > 0) {
                        Log.i("firstMomentid", "第一条动态ID   >>>   " + response.get(0).getMomentid());
                        hostViewHolder.loadHostData(UserDao.getCurrentUser());
                        adapter.updateData(response);
                    }
                    break;
                case REQUEST_LOADMORE:
                    adapter.addMore(response);
                    break;
            }
        }

        @Override
        public void onError(BmobException e, int requestType) {
            super.onError(e, requestType);
            circleRecyclerView.compelete();
        }
    };


    //=============================================================View's method
    @Override
    public void onLikeChange(int itemPos, List<LikesInfo> likeUserList) {
        MomentsInfo momentsInfo = adapter.findData(itemPos);
        if (momentsInfo != null) {
            momentsInfo.setLikesList(likeUserList);
            adapter.notifyItemChanged(itemPos);
        }
    }

    @Override
    public void onCommentChange(int itemPos, List<CommentInfo> commentInfoList) {
        MomentsInfo momentsInfo = adapter.findData(itemPos);
        if (momentsInfo != null) {
            momentsInfo.setCommentList(commentInfoList);
            adapter.notifyItemChanged(itemPos);
        }
    }

    @Override
    public void showCommentBox(@Nullable View viewHolderRootView, int itemPos, String momentid,
                               CommentWidget commentWidget) {
        if (viewHolderRootView != null) {
            mViewHelper.setCommentAnchorView(viewHolderRootView);
        } else if (commentWidget != null) {
            mViewHelper.setCommentAnchorView(commentWidget);
        }
        mViewHelper.setCommentItemDataPosition(itemPos);
        commentBox
                .toggleCommentBox(momentid, commentWidget == null ? null : commentWidget.getData(), false);
    }

    @Override
    public void onDeleteMomentsInfo(@NonNull MomentsInfo momentsInfo) {
        int pos = adapter.getDatas().indexOf(momentsInfo);
        if (pos < 0) {
            return;
        }
        adapter.deleteData(pos);
    }

    @Override
    public boolean onPreTouch(MotionEvent ev) {
        if (commentBox != null && commentBox.isShowing()) {
            commentBox.dismissCommentBox(false);
            return true;
        }
        return false;
    }

    //=============================================================call back
    private CommentBox.OnCommentSendClickListener onCommentSendClickListener = new CommentBox.OnCommentSendClickListener() {
        @Override
        public void onCommentSendClick(View v, IComment comment, String commentContent) {
            if (TextUtils.isEmpty(commentContent)) {
                commentBox.dismissCommentBox(true);
                return;
            }
            int itemPos = mViewHelper.getCommentItemDataPosition();
            if (itemPos < 0 || itemPos > adapter.getItemCount()) {
                return;
            }
            List<CommentInfo> commentInfos = adapter.findData(itemPos).getCommentList();
            String userid = (comment instanceof CommentInfo)
                    ? ((CommentInfo) comment).getAuthor().getUserid()
                    : null;
            presenter.addComment(itemPos, commentBox.getMomentid(), userid, commentContent, commentInfos);
            commentBox.clearDraft();
            commentBox.dismissCommentBox(true);
        }
    };


    private static class HostViewHolder {

        private View rootView;
        private ImageView friend_wall_pic;
        private ImageView friend_avatar;
        private TextView hostid;

        public HostViewHolder(Context context) {
            this.rootView = LayoutInflater.from(context)
                    .inflate(R.layout.moment_circle_host_header, null);
            this.hostid = (TextView) rootView.findViewById(R.id.host_id);
            this.friend_wall_pic = (ImageView) rootView.findViewById(R.id.friend_wall_pic);
            this.friend_avatar = (ImageView) rootView.findViewById(R.id.friend_avatar);
        }

        public void loadHostData(_User hostInfo) {
            if (hostInfo == null) {
                return;
            }
            ImageLoadManager.INSTANCE.loadImage(friend_wall_pic, hostInfo.getCover());
            ImageLoadManager.INSTANCE.loadImage(friend_avatar, hostInfo.getAvatar());
            hostid.setText(hostInfo.getNick());
        }

        public View getView() {
            return rootView;
        }

    }
}
