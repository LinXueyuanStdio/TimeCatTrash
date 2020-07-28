package com.timecat.module.moment.activity.circledemo;

import android.content.Context;
import android.content.Intent;
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
import com.timecat.component.commonarms.core.BaseApplication;
import com.timecat.component.commonbase.base.OnFragmentOpenDrawerListener;
import com.timecat.component.data.model.events.OnActivityResultEvent;
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
import com.timecat.component.lightui.widget.pullrecyclerview.CircleRecyclerView;
import com.timecat.component.lightui.widget.pullrecyclerview.interfaces.OnRefreshListener2;
import com.timecat.component.readonly.RouterHub;
import com.timecat.module.moment.R;
import com.timecat.module.moment.activity.ActivityLauncher;
import com.timecat.module.moment.app.mvp.presenter.impl.MomentPresenter;
import com.timecat.module.moment.app.mvp.view.IMomentView;
import com.timecat.module.moment.ui.adapter.CircleMomentsAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 大灯泡 on 2018/10/26.
 */
@Route(path = RouterHub.MOMENT_MomentFriendCircleMiniFragment)
public class MomentFriendCircleMiniFragment extends BaseTitleBarSupportFragment implements OnRefreshListener2,
        IMomentView, CircleRecyclerView.OnPreDispatchTouchListener {

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
    protected OnFragmentOpenDrawerListener mOpenDraweListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentOpenDrawerListener) {
            mOpenDraweListener = (OnFragmentOpenDrawerListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOpenDraweListener = null;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.moment_fragment_main_mini;
    }

    @Override
    protected void onInitData() {
        AppFileHelper.initStroagePathInternal();
    }

    @Override
    protected void onInitView(View rootView) {
        momentsInfoList = new ArrayList<>();
        momentsRequest = new MomentsRequest();
        initView(rootView);
        initKeyboardHeightObserver();
    }

    private void initView(View rootView) {
        if (mViewHelper == null) {
            mViewHelper = new CircleViewHelper(getActivity());
        }
        presenter = new MomentPresenter(this);

        hostViewHolder = new HostViewHolder(getContext());
        circleRecyclerView = findView(R.id.recycler);
        circleRecyclerView.setOnRefreshListener(this);
        circleRecyclerView.setOnPreDispatchTouchListener(this);
        circleRecyclerView.addHeaderView(hostViewHolder.getView());
        hostViewHolder.loadHostData(UserDao.getCurrentUser());

        commentBox = findView(R.id.widget_comment);
        commentBox.setOnCommentSendClickListener(onCommentSendClickListener);

        if (getContext() == null) {
            adapter = new CircleMomentsAdapter(BaseApplication.getContext(), momentsInfoList, presenter);
        } else {
            adapter = new CircleMomentsAdapter(getContext(), momentsInfoList, presenter);
        }

        circleRecyclerView.setAdapter(adapter);
        circleRecyclerView.autoRefresh();
    }

    @Override
    protected boolean isTranslucentStatus() {
        return false;
    }

    @Override
    protected boolean isFitsSystemWindows() {
        return false;
    }

    private void initKeyboardHeightObserver() {
        //观察键盘弹出与消退
        KeyboardControlMnanager.observerKeyboardVisibleChange(getActivity(),
                new KeyboardControlMnanager.OnKeyboardStateChangeListener() {
                    View anchorView;

                    @Override
                    public void onKeyboardChange(int keyboardHeight, boolean isVisible) {
                        int commentType = commentBox.getCommentType();
                        if (isVisible) {
                            //定位评论框到view
                            commentBox.setTranslationY(-keyboardHeight + commentBox.getHeight()
                                    + UIHelper.getStatusBarHeight(BaseApplication.getContext()));
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
    public void onRefresh() {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActivityResult(OnActivityResultEvent e) {
        int requestCode = e.requestCode;
        int resultCode = e.resultCode;
        Intent data = e.data;
        super.onActivityResult(requestCode, resultCode, data);
//    LogUtil.e("requestCode = " + requestCode + "resultCode = " + resultCode);
        PhotoHelper.handleActivityResult(this,
                requestCode, resultCode, data, new PhotoHelper.PhotoCallback() {
                    @Override
                    public void onFinish(String filePath) {
                        List<ImageInfo> selectedPhotos = new ArrayList<ImageInfo>();
                        selectedPhotos.add(new ImageInfo(filePath, null, null, 0, 0));
                        ActivityLauncher.startToPublishActivityWithResult(getActivity(),
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
                ActivityLauncher.startToPublishActivityWithResult(getActivity(),
                        RouterHub.PublishActivity.MODE_MULTI,
                        selectedPhotos,
                        RouterHub.PublishActivity.requestCode);
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
                    if (!isListEmpty(response)) {
                        Log.i("firstMomentid", "第一条动态ID   >>>   " + response.get(0).getMomentid());
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
            String userid =
                    (comment instanceof CommentInfo) ? ((CommentInfo) comment).getAuthor().getUserid() : null;
            presenter.addComment(itemPos, commentBox.getMomentid(), userid, commentContent, commentInfos);
            commentBox.clearDraft();
            commentBox.dismissCommentBox(true);
        }
    };

    private static class HostViewHolder {

        private View rootView;
        private ImageView friend_wall_pic;
        private ImageView friend_avatar;
        private ImageView message_avatar;
        private TextView message_detail;
        private TextView hostid;

        public HostViewHolder(Context context) {
            this.rootView = LayoutInflater.from(context)
                    .inflate(R.layout.moment_circle_host_header, null);
            this.hostid = (TextView) rootView.findViewById(R.id.host_id);
            this.friend_wall_pic = (ImageView) rootView.findViewById(R.id.friend_wall_pic);
            this.friend_avatar = (ImageView) rootView.findViewById(R.id.friend_avatar);
            this.message_avatar = (ImageView) rootView.findViewById(R.id.message_avatar);
            this.message_detail = (TextView) rootView.findViewById(R.id.message_detail);
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

    boolean isListEmpty(List<?> datas) {
        return datas == null || datas.size() <= 0;
    }

}


