package com.timecat.module.moment.activity.circledemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.timecat.component.alert.ToastUtil;
import com.timecat.component.bmob.dao.UserDao;
import com.timecat.component.bmob.data._User;
import com.timecat.component.bmob.listener.OnResponseListener;
import com.timecat.component.commonarms.core.BaseApplication;
import com.timecat.component.commonbase.base.OnFragmentOpenDrawerListener;
import com.timecat.component.data.model.events.OnActivityResultEvent;
import com.timecat.component.lightui.base.BaseTitleBarFragment;
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
import com.timecat.component.lightui.widget.pullrecyclerview.interfaces.OnRefreshListener2;
import com.timecat.component.readonly.RouterHub;
import com.timecat.module.moment.R;
import com.timecat.module.moment.activity.ActivityLauncher;
import com.timecat.module.moment.app.mvp.presenter.impl.MomentPresenter;
import com.timecat.module.moment.app.mvp.view.IMomentView;
import com.timecat.module.moment.ui.adapter.CircleMomentsAdapter;
import com.timecat.module.moment.ui.helper.TitleBarAlphaChangeHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import me.yokeyword.fragmentation.ExtraTransaction;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportFragmentDelegate;
import me.yokeyword.fragmentation.SupportHelper;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by 大灯泡 on 2018/10/26.
 */
@Route(path = RouterHub.MOMENT_MomentFriendCircleFragment)
public class MomentFriendCircleFragment extends BaseTitleBarFragment implements OnRefreshListener2,
        IMomentView, CircleRecyclerView.OnPreDispatchTouchListener, ISupportFragment {

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
        return R.layout.moment_fragment_main;
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
        getTitleBar().getLeftTextView().setAlpha(0f);
        getTitleBar().setLeftText("");
        setLeftTextColor(Color.parseColor("#040404"));
        setTitleMode(TitleBarMode.MODE_BOTH);
        setTitleRightIcon(R.drawable.ic_camera);
        setTitleLeftIcon(R.drawable.ic_menu_white_24dp);
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
        TitleBarAlphaChangeHelper.handle(getTitleBar(),
                circleRecyclerView.getRecyclerView(),
                hostViewHolder.friend_avatar,
                (alpha, color) -> {
//          setStatusBarDark(alpha > 1);
                    setStatusBarHolderBackgroundColor(color);
                });
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

    @Override
    public void onTitleDoubleClick() {
        super.onTitleDoubleClick();
        if (circleRecyclerView != null) {
            int firstVisibleItemPos = circleRecyclerView.findFirstVisibleItemPosition();
            circleRecyclerView.getRecyclerView().smoothScrollToPosition(0);
            if (firstVisibleItemPos > 1) {
                circleRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        circleRecyclerView.autoRefresh();
                    }
                }, 200);
            }
        }

    }

    @Override
    public void onTitleLeftClick() {
        if (mOpenDraweListener != null) {
            mOpenDraweListener.onOpenDrawer(getTitleBar());
        }
    }

    @Override
    public void onTitleRightClick() {
        new SelectPhotoMenuPopup(getActivity()).setOnSelectPhotoMenuClickListener(
                new SelectPhotoMenuPopup.OnSelectPhotoMenuClickListener() {
                    @Override
                    public void onShootClick() {
                        PhotoHelper.fromCamera(this, false);
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
        ActivityLauncher
                .startToPublishActivityWithResult(getActivity(), RouterHub.PublishActivity.MODE_TEXT, null,
                        RouterHub.PublishActivity.requestCode);
        return true;
    }

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

    //region ISupportFragment
    final SupportFragmentDelegate mDelegate = new SupportFragmentDelegate(this);
    protected FragmentActivity _mActivity;

    @Override
    public SupportFragmentDelegate getSupportDelegate() {
        return mDelegate;
    }

    /**
     * Perform some extra transactions. 额外的事务：自定义Tag，添加SharedElement动画，操作非回退栈Fragment
     */
    @Override
    public ExtraTransaction extraTransaction() {
        return mDelegate.extraTransaction();
    }

    protected boolean needScopeEventBus() {
        return true;
    }

    public boolean needEventBus() {
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (needScopeEventBus()) {
            EventBusActivityScope.getDefault(_mActivity).register(this);
        }
        if (needEventBus() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        if (needEventBus() && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (needScopeEventBus()) {
            EventBusActivityScope.getDefault(_mActivity).unregister(this);
        }
        super.onStop();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDelegate.onAttach(activity);
        _mActivity = mDelegate.getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate.onCreate(savedInstanceState);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return mDelegate.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDelegate.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mDelegate.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mDelegate.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mDelegate.onPause();
    }

    @Override
    public void onDestroyView() {
        mDelegate.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mDelegate.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mDelegate.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mDelegate.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * Causes the Runnable r to be added to the action queue.
     * <p>
     * The runnable will be run after all the previous action has been run.
     * <p>
     * 前面的事务全部执行后 执行该Action
     *
     * @deprecated Use {@link #post(Runnable)} instead.
     */
    @Deprecated
    @Override
    public void enqueueAction(Runnable runnable) {
        mDelegate.enqueueAction(runnable);
    }

    /**
     * Causes the Runnable r to be added to the action queue.
     * <p>
     * The runnable will be run after all the previous action has been run.
     * <p>
     * 前面的事务全部执行后 执行该Action
     */
    @Override
    public void post(Runnable runnable) {
        mDelegate.post(runnable);
    }

    /**
     * Called when the enter-animation end. 入栈动画 结束时,回调
     */
    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        mDelegate.onEnterAnimationEnd(savedInstanceState);
    }


    /**
     * Lazy initial，Called when fragment is first called.
     * <p>
     * 同级下的 懒加载 ＋ ViewPager下的懒加载  的结合回调方法
     */
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        mDelegate.onLazyInitView(savedInstanceState);
    }

    /**
     * Called when the fragment is visible. 当Fragment对用户可见时回调
     * <p>
     * Is the combination of  [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
     */
    @Override
    public void onSupportVisible() {
        mDelegate.onSupportVisible();
    }

    /**
     * Called when the fragment is invivible.
     * <p>
     * Is the combination of  [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
     */
    @Override
    public void onSupportInvisible() {
        mDelegate.onSupportInvisible();
    }

    /**
     * Return true if the fragment has been supportVisible.
     */
    @Override
    final public boolean isSupportVisible() {
        return mDelegate.isSupportVisible();
    }

    /**
     * Set fragment animation with a higher priority than the ISupportActivity
     * 设定当前Fragmemt动画,优先级比在SupportActivity里高
     */
    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return mDelegate.onCreateFragmentAnimator();
    }

    /**
     * 获取设置的全局动画 copy
     *
     * @return FragmentAnimator
     */
    @Override
    public FragmentAnimator getFragmentAnimator() {
        return mDelegate.getFragmentAnimator();
    }

    /**
     * 设置Fragment内的全局动画
     */
    @Override
    public void setFragmentAnimator(FragmentAnimator fragmentAnimator) {
        mDelegate.setFragmentAnimator(fragmentAnimator);
    }

    /**
     * 按返回键触发,前提是SupportActivity的onBackPressed()方法能被调用
     *
     * @return false则继续向上传递, true则消费掉该事件
     */
    @Override
    public boolean onBackPressedSupport() {
        return mDelegate.onBackPressedSupport();
    }

    /**
     * 类似 {@link Activity#setResult(int, Intent)}
     * <p>
     * Similar to {@link Activity#setResult(int, Intent)}
     *
     * @see #startForResult(ISupportFragment, int)
     */
    @Override
    public void setFragmentResult(int resultCode, Bundle bundle) {
        mDelegate.setFragmentResult(resultCode, bundle);
    }

    /**
     * 类似  {@link Activity#onActivityResult(int, int, Intent)}
     * <p>
     * Similar to {@link Activity#onActivityResult(int, int, Intent)}
     *
     * @see #startForResult(ISupportFragment, int)
     */
    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        mDelegate.onFragmentResult(requestCode, resultCode, data);
    }

    /**
     * 在start(TargetFragment,LaunchMode)时,启动模式为SingleTask/SingleTop, 回调TargetFragment的该方法 类似 {@link
     * Activity#onNewIntent(Intent)}
     * <p>
     * Similar to {@link Activity#onNewIntent(Intent)}
     *
     * @param args putNewBundle(Bundle newBundle)
     * @see #start(ISupportFragment, int)
     */
    @Override
    public void onNewBundle(Bundle args) {
        mDelegate.onNewBundle(args);
    }

    /**
     * 添加NewBundle,用于启动模式为SingleTask/SingleTop时
     *
     * @see #start(ISupportFragment, int)
     */
    @Override
    public void putNewBundle(Bundle newBundle) {
        mDelegate.putNewBundle(newBundle);
    }

    /****************************************以下为可选方法(Optional methods)******************************************************/

    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput() {
        mDelegate.hideSoftInput();
    }

    /**
     * 显示软键盘,调用该方法后,会在onPause时自动隐藏软键盘
     */
    protected void showSoftInput(final View view) {
        mDelegate.showSoftInput(view);
    }

    /**
     * 加载根Fragment, 即Activity内的第一个Fragment 或 Fragment内的第一个子Fragment
     *
     * @param containerId 容器id
     * @param toFragment  目标Fragment
     */
    public void loadRootFragment(int containerId, ISupportFragment toFragment) {
        mDelegate.loadRootFragment(containerId, toFragment);
    }

    public void loadRootFragment(int containerId, ISupportFragment toFragment, boolean addToBackStack,
                                 boolean allowAnim) {
        mDelegate.loadRootFragment(containerId, toFragment, addToBackStack, allowAnim);
    }

    /**
     * 加载多个同级根Fragment,类似Wechat, QQ主页的场景
     */
    public void loadMultipleRootFragment(int containerId, int showPosition,
                                         ISupportFragment... toFragments) {
        mDelegate.loadMultipleRootFragment(containerId, showPosition, toFragments);
    }

    /**
     * show一个Fragment,hide其他同栈所有Fragment 使用该方法时，要确保同级栈内无多余的Fragment,(只有通过loadMultipleRootFragment()载入的Fragment)
     * <p>
     * 建议使用更明确的{@link #showHideFragment(ISupportFragment, ISupportFragment)}
     *
     * @param showFragment 需要show的Fragment
     */
    public void showHideFragment(ISupportFragment showFragment) {
        mDelegate.showHideFragment(showFragment);
    }

    /**
     * show一个Fragment,hide一个Fragment ; 主要用于类似微信主页那种 切换tab的情况
     */
    public void showHideFragment(ISupportFragment showFragment, ISupportFragment hideFragment) {
        mDelegate.showHideFragment(showFragment, hideFragment);
    }

    public void start(ISupportFragment toFragment) {
        mDelegate.start(toFragment);
    }

    /**
     * @param launchMode Similar to Activity's LaunchMode.
     */
    public void start(final ISupportFragment toFragment, @LaunchMode int launchMode) {
        mDelegate.start(toFragment, launchMode);
    }

    /**
     * Launch an fragment for which you would like a result when it poped.
     */
    public void startForResult(ISupportFragment toFragment, int requestCode) {
        mDelegate.startForResult(toFragment, requestCode);
    }

    /**
     * Start the target Fragment and pop itself
     */
    public void startWithPop(ISupportFragment toFragment) {
        mDelegate.startWithPop(toFragment);
    }

    /**
     * @see #popTo(Class, boolean) +
     * @see #start(ISupportFragment)
     */
    public void startWithPopTo(ISupportFragment toFragment, Class<?> targetFragmentClass,
                               boolean includeTargetFragment) {
        mDelegate.startWithPopTo(toFragment, targetFragmentClass, includeTargetFragment);
    }


    public void replaceFragment(ISupportFragment toFragment, boolean addToBackStack) {
        mDelegate.replaceFragment(toFragment, addToBackStack);
    }

    public void pop() {
        mDelegate.pop();
    }

    /**
     * Pop the child fragment.
     */
    public void popChild() {
        mDelegate.popChild();
    }

    /**
     * Pop the last fragment transition from the manager's fragment back stack.
     * <p>
     * 出栈到目标fragment
     *
     * @param targetFragmentClass   目标fragment
     * @param includeTargetFragment 是否包含该fragment
     */
    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment);
    }

    /**
     * If you want to begin another FragmentTransaction immediately after popTo(), use this method.
     * 如果你想在出栈后, 立刻进行FragmentTransaction操作，请使用该方法
     */
    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment,
                      Runnable afterPopTransactionRunnable) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable);
    }

    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment,
                      Runnable afterPopTransactionRunnable, int popAnim) {
        mDelegate
                .popTo(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable, popAnim);
    }

    public void popToChild(Class<?> targetFragmentClass, boolean includeTargetFragment) {
        mDelegate.popToChild(targetFragmentClass, includeTargetFragment);
    }

    public void popToChild(Class<?> targetFragmentClass, boolean includeTargetFragment,
                           Runnable afterPopTransactionRunnable) {
        mDelegate.popToChild(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable);
    }

    public void popToChild(Class<?> targetFragmentClass, boolean includeTargetFragment,
                           Runnable afterPopTransactionRunnable, int popAnim) {
        mDelegate.popToChild(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable,
                popAnim);
    }

    /**
     * 得到位于栈顶Fragment
     */
    public ISupportFragment getTopFragment() {
        return SupportHelper.getTopFragment(getFragmentManager());
    }

    public ISupportFragment getTopChildFragment() {
        return SupportHelper.getTopFragment(getChildFragmentManager());
    }

    /**
     * @return 位于当前Fragment的前一个Fragment
     */
    public ISupportFragment getPreFragment() {
        return SupportHelper.getPreFragment(this);
    }

    /**
     * 获取栈内的fragment对象
     */
    public <T extends ISupportFragment> T findFragment(Class<T> fragmentClass) {
        return SupportHelper.findFragment(getFragmentManager(), fragmentClass);
    }

    /**
     * 获取栈内的fragment对象
     */
    public <T extends ISupportFragment> T findChildFragment(Class<T> fragmentClass) {
        return SupportHelper.findFragment(getChildFragmentManager(), fragmentClass);
    }
    //endregion
}


