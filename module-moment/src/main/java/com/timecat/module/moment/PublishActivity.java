package com.timecat.module.moment;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.timecat.component.alert.ToastUtil;
import com.timecat.component.bmob.dao.UserDao;
import com.timecat.component.bmob.listener.OnResponseListener;
import com.timecat.component.commonsdk.utils.override.LogUtil;
import com.timecat.component.commonsdk.utils.string.StringUtil;
import com.timecat.component.lightui.base.BaseTitleBarActivity;
import com.timecat.component.lightui.common.entity.ImageInfo;
import com.timecat.component.lightui.common.entity.PhotoInfo;
import com.timecat.component.lightui.common.entity.photo.PhotoBrowserInfo;
import com.timecat.component.lightui.common.request.AddMomentsRequest;
import com.timecat.component.lightui.helper.PhotoHelper;
import com.timecat.component.lightui.imageloader.ImageLoadManager;
import com.timecat.component.lightui.interfaces.adapter.TextWatcherAdapter;
import com.timecat.component.lightui.manager.compress.CompressManager;
import com.timecat.component.lightui.manager.compress.CompressResult;
import com.timecat.component.lightui.manager.compress.OnCompressListener;
import com.timecat.component.lightui.util.SwitchActivityTransitionUtil;
import com.timecat.component.lightui.util.UIHelper;
import com.timecat.component.lightui.util.ViewUtil;
import com.timecat.component.lightui.widget.common.TitleBarMode;
import com.timecat.component.lightui.widget.imageview.PreviewImageView;
import com.timecat.component.lightui.widget.popup.PopupProgress;
import com.timecat.component.lightui.widget.popup.SelectPhotoMenuPopup;
import com.timecat.component.readonly.RouterHub;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadBatchListener;


/**
 * Created by 大灯泡 on 2017/3/1.
 * <p>
 * 发布朋友圈页面
 */
@Route(path = RouterHub.PublishActivity.path)
public class PublishActivity extends BaseTitleBarActivity {

    @Autowired(name = RouterHub.PublishActivity.key_mode)
    int mode = -1;

    private boolean canTitleRightClick = false;

    @Autowired(name = RouterHub.PublishActivity.key_photoList)
    ArrayList<ImageInfo> selectedPhotos;

    private EditText mInputContent;
    private PreviewImageView<ImageInfo> mPreviewImageView;

    private SelectPhotoMenuPopup mSelectPhotoMenuPopup;
    private PopupProgress mPopupProgress;

    @Override
    public void onHandleIntent(Intent intent) {
        mode = intent.getIntExtra(RouterHub.PublishActivity.key_mode, -1);
        selectedPhotos = intent.getParcelableArrayListExtra(RouterHub.PublishActivity.key_photoList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moment_activity_publish);
        if (mode == -1) {
            finish();
            return;
        } else if (mode == RouterHub.PublishActivity.MODE_MULTI && selectedPhotos == null) {
            finish();
            return;
        }
        initView();
    }

    private void initView() {
        initTitle();
        mInputContent = findView(R.id.publish_input);
        mPreviewImageView = findView(R.id.preview_image_content);
        ViewUtil
                .setViewsVisible(mode == RouterHub.PublishActivity.MODE_TEXT ? View.GONE : View.VISIBLE,
                        mPreviewImageView);
        mInputContent.setHint(mode == RouterHub.PublishActivity.MODE_MULTI ? "这一刻的想法..." : null);
        mInputContent.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                refreshTitleRightClickable();
            }
        });

        if (mode == RouterHub.PublishActivity.MODE_TEXT) {
            UIHelper.showInputMethod(mInputContent, 300);
        }

        if (mode == RouterHub.PublishActivity.MODE_MULTI) {
            initPreviewImageView();
            loadImage();
        }
        refreshTitleRightClickable();
    }

    private void initPreviewImageView() {
        mPreviewImageView.setOnPhotoClickListener((pos, data, imageView) -> {
            PhotoBrowserInfo info = PhotoBrowserInfo.create(pos, null, selectedPhotos);
            ARouter.getInstance()
                    .build(RouterHub.PhotoMultiBrowserActivity.path)
                    .withParcelable(RouterHub.PhotoMultiBrowserActivity.key_browserinfo, info)
                    .withInt(RouterHub.PhotoMultiBrowserActivity.key_maxSelectCount,
                            selectedPhotos.size())
                    .navigation(PublishActivity.this, RouterHub.PhotoMultiBrowserActivity.requestCode);
        });
        mPreviewImageView.setOnAddPhotoClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectPhotoPopup();
            }
        });
    }

    private void loadImage() {
        mPreviewImageView
                .setDatas(selectedPhotos, new PreviewImageView.OnLoadPhotoListener<ImageInfo>() {
                    @Override
                    public void onPhotoLoading(int pos, ImageInfo data, @NonNull ImageView imageView) {
                        LogUtil.i(data.getImagePath());
                        ImageLoadManager.INSTANCE.loadImage(imageView, data.getImagePath());
                    }
                });
    }

    private void showSelectPhotoPopup() {
        if (mSelectPhotoMenuPopup == null) {
            mSelectPhotoMenuPopup = new SelectPhotoMenuPopup(this);
            mSelectPhotoMenuPopup.setOnSelectPhotoMenuClickListener(
                    new SelectPhotoMenuPopup.OnSelectPhotoMenuClickListener() {
                        @Override
                        public void onShootClick() {
                            PhotoHelper.fromCamera(PublishActivity.this, false);
                        }

                        @Override
                        public void onAlbumClick() {
                            ARouter.getInstance()
                                    .build(RouterHub.PhotoSelectActivity.path)
                                    .withObject(RouterHub.PhotoSelectActivity.key_photoList,
                                            mPreviewImageView.getDatas())
                                    .withInt(RouterHub.PhotoSelectActivity.key_maxSelectCount,
                                            mPreviewImageView.getRestPhotoCount())
                                    .navigation(PublishActivity.this, RouterHub.PhotoSelectActivity.requestCode);
                        }
                    });
        }
        mSelectPhotoMenuPopup.showPopupWindow();

    }

    //title init
    private void initTitle() {
        setTitle(mode == RouterHub.PublishActivity.MODE_TEXT ? "发表文字" : null);
        setTitleRightTextColor(mode != RouterHub.PublishActivity.MODE_TEXT);
        setTitleMode(TitleBarMode.MODE_BOTH);
        setTitleLeftText("取消");
        setTitleLeftIcon(0);
        setTitleRightText("发送");
        setTitleRightIcon(0);
    }


    private void setTitleRightTextColor(boolean canClick) {
        setRightTextColor(canClick ? UIHelper.getColor(R.color.wechat_green_bg)
                : UIHelper.getColor(R.color.wechat_green_transparent));
        canTitleRightClick = canClick;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoHelper
                .handleActivityResult(this, requestCode, resultCode, data, new PhotoHelper.PhotoCallback() {
                    @Override
                    public void onFinish(String filePath) {
                        mPreviewImageView.addData(new ImageInfo(filePath, null, null, 0, 0));
                        refreshTitleRightClickable();
                    }

                    @Override
                    public void onError(String msg) {
                        ToastUtil.e(msg);
                    }
                });

        //相册选择
        if (requestCode == RouterHub.PhotoSelectActivity.requestCode && resultCode == RESULT_OK) {
            List<ImageInfo> result = data
                    .getParcelableArrayListExtra(RouterHub.PhotoSelectActivity.key_result);
            if (result != null) {
                mPreviewImageView.clear();
                mPreviewImageView.addData(result);
            }
            refreshTitleRightClickable();
        }

        //多图选择
        if (requestCode == RouterHub.PhotoMultiBrowserActivity.requestCode
                && resultCode == RESULT_OK) {
            List<ImageInfo> result = data
                    .getParcelableArrayListExtra(RouterHub.PhotoMultiBrowserActivity.key_result);
            selectedPhotos.clear();
            if (result != null) {
                selectedPhotos.addAll(result);
            }
            loadImage();
            refreshTitleRightClickable();
        }


    }

    @Override
    public void onTitleRightClick() {
        if (!canTitleRightClick) {
            return;
        }
        publish();
    }

    private void refreshTitleRightClickable() {
        String inputContent = mInputContent.getText().toString();
        switch (mode) {
            case RouterHub.PublishActivity.MODE_MULTI:
                setTitleRightTextColor(!isListEmpty(mPreviewImageView.getDatas()) && StringUtil
                        .noEmpty(inputContent));
                break;
            case RouterHub.PublishActivity.MODE_TEXT:
                setTitleRightTextColor(StringUtil.noEmpty(inputContent));
                break;
        }

    }

    @Override
    public void finish() {
        super.finish();
        if (mPopupProgress != null) {
            mPopupProgress.dismiss();
        }
        SwitchActivityTransitionUtil.transitionVerticalOnFinish(this);
    }

    private void publish() {
        UIHelper.hideInputMethod(mInputContent);
        List<ImageInfo> datas = mPreviewImageView.getDatas();
        final boolean hasImage = !isListEmpty(datas);
        final String inputContent = mInputContent.getText().toString();
        if (mPopupProgress == null) {
            mPopupProgress = new PopupProgress(this);
        }

        final String[] uploadTaskPaths;
        if (hasImage) {
            uploadTaskPaths = new String[datas.size()];
            for (int i = 0; i < datas.size(); i++) {
                uploadTaskPaths[i] = datas.get(i).getImagePath();
            }
            doCompress(uploadTaskPaths, new OnCompressListener.OnCompressListenerAdapter() {
                @Override
                public void onSuccess(List<CompressResult> imagePaths) {
                    if (!isListEmpty(imagePaths)) {
                        doUpload(imagePaths, inputContent);
                    } else {
                        publishInternal(inputContent, null);
                    }
                }
            });
        } else {
            publishInternal(inputContent, null);
        }
    }

    private void doCompress(String[] uploadPaths,
                            final OnCompressListener.OnCompressListenerAdapter listener) {
        CompressManager compressManager = CompressManager.create(this);
        for (String uploadPath : uploadPaths) {
            compressManager.addTask().setOriginalImagePath(uploadPath);
        }
        mPopupProgress.showPopupWindow();
        compressManager.start(new OnCompressListener.OnCompressListenerAdapter() {
            @Override
            public void onSuccess(List<CompressResult> imagePath) {
                if (listener != null) {
                    listener.onSuccess(imagePath);
                }
                mPopupProgress.dismiss();
            }

            @Override
            public void onCompress(long current, long target) {
                float progress = (float) current / target;
                mPopupProgress.setProgressTips("正在压缩第" + current + "/" + target + "张图片");
                mPopupProgress.setProgress((int) (progress * 100));
            }

            @Override
            public void onError(String tag) {
                mPopupProgress.dismiss();
                ToastUtil.e(tag);
            }
        });
    }

    private void doUpload(final List<CompressResult> imagePaths, final String inputContent) {
        String[] uploadTaskPaths = new String[imagePaths.size()];
        for (int i = 0; i < imagePaths.size(); i++) {
            uploadTaskPaths[i] = imagePaths.get(i).getCompressedFilePath();
        }
        BmobFile.uploadBatch(uploadTaskPaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                //1、有多少个文件上传，onSuccess方法就会执行多少次;
                //2、通过onSuccess回调方法中的files或urls集合的大小与上传的总文件个数比较，如果一样，则表示全部文件上传成功。
                if (!isListEmpty(list1) && list1.size() == imagePaths.size()) {
                    List<PhotoInfo> photoInfos = new ArrayList<>();
                    for (int i = 0; i < imagePaths.size(); i++) {
                        CompressResult result = imagePaths.get(i);
                        photoInfos.add(new PhotoInfo()
                                .setUrl(list1.get(i))
                                .setWidth(result.getCompressedWidth())
                                .setHeight(result.getCompressedHeight()));
                    }
                    publishInternal(inputContent, photoInfos);
                }
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
                mPopupProgress.setProgressTips("正在上传第" + curIndex + "/" + total + "张图片");
                mPopupProgress.setProgress(totalPercent);
                if (!mPopupProgress.isShowing()) {
                    mPopupProgress.showPopupWindow();
                }
            }

            @Override
            public void onError(int i, String s) {
                mPopupProgress.dismiss();
                ToastUtil.e(s);
            }
        });
    }

    private void publishInternal(String input, List<PhotoInfo> uploadPicPaths) {
        mPopupProgress.setProgressTips("正在发布");
        if (!mPopupProgress.isShowing()) {
            mPopupProgress.showPopupWindow();
        }
        if (UserDao.getCurrentUser() == null) {
            mPopupProgress.dismiss();
            ToastUtil.w("未登录！");
            ARouter.getInstance().build(RouterHub.LOGIN_LoginActivity)
                    .withString("path", RouterHub.PublishActivity.path)
                    .navigation();
            return;
        }
        String me = UserDao.getCurrentUser().getObjectId();
        AddMomentsRequest addMomentsRequest = new AddMomentsRequest();
        addMomentsRequest.setAuthId(me)
                .setHostId(me)
                .addPictureBuckets(uploadPicPaths)
                .addText(input);
        addMomentsRequest
                .setOnResponseListener(new OnResponseListener.SimpleResponseListener<String>() {
                    @Override
                    public void onSuccess(String response, int requestType) {
                        mPopupProgress.dismiss();
                        ToastUtil.ok("发布成功");
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError(BmobException e, int requestType) {
                        mPopupProgress.dismiss();
                        ToastUtil.e(e.toString());
                    }
                });
        addMomentsRequest.execute();
    }

    boolean isListEmpty(List<?> datas) {
        return datas == null || datas.size() <= 0;
    }
}
