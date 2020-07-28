package com.timecat.module.moment.activity.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.timecat.component.lightui.base.BaseActivity;
import com.timecat.component.lightui.common.entity.PhotoBrowseInfo;
import com.timecat.component.lightui.imageloader.ImageLoadManager;
import com.timecat.component.lightui.widget.common.HackyViewPager;
import com.timecat.component.lightui.widget.imageview.GalleryPhotoView;
import com.timecat.component.lightui.widget.indicator.DotIndicator;
import com.timecat.component.readonly.RouterHub;
import com.timecat.module.moment.R;
import com.timecat.widget.photoview.PhotoView;
import com.timecat.widget.photoview.PhotoViewAttacher;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 大灯泡 on 2016/12/16.
 * <p>
 * 朋友圈图片浏览控件
 */
@Route(path = RouterHub.MOMENT_PhotoBrowseActivity)
public class PhotoBrowseActivity extends BaseActivity {

    private static final String TAG = "PhotoBrowseActivity";

    private HackyViewPager photoViewpager;
    private View blackBackground;
    private DotIndicator dotIndicator;//小圆点指示器
    private List<GalleryPhotoView> viewBuckets;
    private PhotoBrowseInfo photoBrowseInfo;
    private InnerPhotoViewerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moment_activity_photo_browse);
        preInitData();
        initView();
    }

    @Override
    public void onHandleIntent(Intent intent) {

    }

    private void preInitData() {
        photoBrowseInfo = getIntent().getParcelableExtra("photoinfo");
        viewBuckets = new LinkedList<>();
        final int photoCount = photoBrowseInfo.getPhotosCount();
        for (int i = 0; i < photoCount; i++) {
            GalleryPhotoView photoView = new GalleryPhotoView(this);
            photoView.setCleanOnDetachedFromWindow(false);
            photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    finish();
                }
            });
            viewBuckets.add(photoView);
        }
    }

    private void initView() {
        photoViewpager = (HackyViewPager) findViewById(R.id.photo_viewpager);
        blackBackground = findViewById(R.id.v_background);
        dotIndicator = (DotIndicator) findViewById(R.id.dot_indicator);

        dotIndicator.init(this, photoBrowseInfo.getPhotosCount());
        dotIndicator.setCurrentSelection(photoBrowseInfo.getCurrentPhotoPosition());

        adapter = new InnerPhotoViewerAdapter(this);
        photoViewpager.setAdapter(adapter);
        photoViewpager.setLocked(photoBrowseInfo.getPhotosCount() == 1);
        photoViewpager.setCurrentItem(photoBrowseInfo.getCurrentPhotoPosition());
        photoViewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                dotIndicator.setCurrentSelection(position);
            }
        });
        blackBackground.animate()
                .alpha(1f)
                .setDuration(500)
                .start();
    }

    @Override
    public void onDestroy() {
        if (viewBuckets != null && viewBuckets.size() > 0) {
            for (PhotoView photoView : viewBuckets) {
                photoView.destroy();
            }
        }
        Glide.get(this).clearMemory();
        super.onDestroy();
    }

    //=============================================================Tools method
    public static void startToPhotoBrowseActivity(Activity from, @NonNull PhotoBrowseInfo info) {
        if (info == null || !info.isValided()) {
            return;
        }
        Intent intent = new Intent(from, PhotoBrowseActivity.class);
        intent.putExtra("photoinfo", info);
        from.startActivity(intent);
        //禁用动画
        from.overridePendingTransition(0, 0);
    }

    @Override
    public void finish() {
        showStatusBar();
        final GalleryPhotoView currentPhotoView = viewBuckets.get(photoViewpager.getCurrentItem());
        if (currentPhotoView == null) {
            Log.e(TAG, "childView is null");
            super.finish();
            return;
        }
        final Rect endRect = photoBrowseInfo.getViewLocalRects().get(photoViewpager.getCurrentItem());
        currentPhotoView
                .playExitAnima(endRect, blackBackground, new GalleryPhotoView.OnExitAnimaEndListener() {
                    @Override
                    public void onExitAnimaEnd() {
                        PhotoBrowseActivity.super.finish();
                        overridePendingTransition(0, 0);
                    }
                });
    }

    //=============================================================InnerAdapter

    private class InnerPhotoViewerAdapter extends PagerAdapter {

        private Context context;
        private boolean isFirstInitlize;

        public InnerPhotoViewerAdapter(Context context) {
            this.context = context;
            isFirstInitlize = true;
        }

        @Override
        public int getCount() {
            return photoBrowseInfo.getPhotosCount();
        }

        @NotNull
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            GalleryPhotoView photoView = viewBuckets.get(position);
            String photoUrl = photoBrowseInfo.getPhotoUrls().get(position);
            ImageLoadManager.INSTANCE.loadImageCenterInside(photoView, photoUrl);
            container.addView(photoView);
            return photoView;
        }

        @Override
        public void setPrimaryItem(@NotNull ViewGroup container,
                                   final int position,
                                   @NotNull final Object object) {
            if (isFirstInitlize && object instanceof GalleryPhotoView && position == photoBrowseInfo
                    .getCurrentPhotoPosition()) {
                isFirstInitlize = false;
                final GalleryPhotoView targetView = (GalleryPhotoView) object;
                final Rect startRect = photoBrowseInfo.getViewLocalRects().get(position);
                targetView.playEnterAnima(startRect, null);
            }
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NotNull View view, @NotNull Object object) {
            return view == object;
        }
    }
}
