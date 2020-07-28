package com.timecat.module.moment.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.timecat.component.lightui.common.entity.ImageInfo;
import com.timecat.component.lightui.common.entity.PhotoBrowseInfo;
import com.timecat.component.readonly.RouterHub;
import com.timecat.component.readonly.RouterHub.PhotoSelectActivity;
import com.timecat.module.moment.R;
import com.timecat.module.moment.activity.circledemo.MomentFriendCircleFragmentActivity;
import com.timecat.module.moment.activity.gallery.PhotoBrowseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 大灯泡 on 2017/3/1.
 * <p>
 * activity发射器~
 */

public class ActivityLauncher {

    /**
     * 发射到发布朋友圈页面
     */
    public static void startToPublishActivityWithResult(Activity act,
                                                        @RouterHub.PublishActivity int mode,
                                                        @Nullable List<ImageInfo> selectedPhotos,
                                                        int requestCode) {
        ARouter.getInstance().build(RouterHub.PublishActivity.path)
                .withInt(RouterHub.PublishActivity.key_mode, mode)
                .withParcelableArrayList(RouterHub.PublishActivity.key_photoList,
                        (ArrayList<? extends Parcelable>) selectedPhotos)
                .withTransition(R.anim.scale_slide_in_bottom, R.anim.no_translate)
                .navigation(act, requestCode);
//    Intent intent = new Intent(act, PublishActivity.class);
//    intent.putExtra(RouterHub.PublishActivity.key_mode, mode);
//    if (selectedPhotos != null) {
//      intent.putParcelableArrayListExtra(RouterHub.PublishActivity.key_photoList,
//          (ArrayList<? extends Parcelable>) selectedPhotos);
//    }
//    act.startActivityForResult(intent, requestCode);
//    SwitchActivityTransitionUtil.transitionVerticalIn(act);
    }


    public static void startToPhotoBrosweActivity(Activity act, @NonNull PhotoBrowseInfo info) {
        if (info == null) {
            return;
        }
        PhotoBrowseActivity.startToPhotoBrowseActivity(act, info);
    }

    public static void startToFriendCircleFragmentDemoActivity(Context context) {
        context.startActivity(new Intent(context, MomentFriendCircleFragmentActivity.class));
    }

    /**
     * 发射到选择图片页面
     */
    public static void startToPhotoSelectActivity(Activity act, int requestCode) {
        ARouter.getInstance().build(PhotoSelectActivity.path)
                .withTransition(R.anim.scale_slide_in_bottom, R.anim.no_translate)
                .navigation(act, requestCode);
    }

}
