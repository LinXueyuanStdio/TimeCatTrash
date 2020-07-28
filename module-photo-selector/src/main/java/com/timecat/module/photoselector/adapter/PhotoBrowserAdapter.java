package com.timecat.module.photoselector.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.timecat.component.lightui.common.entity.ImageInfo;
import com.timecat.component.lightui.imageloader.ImageLoadManager;
import com.timecat.component.lightui.interfaces.ClearMemoryObject;
import com.timecat.component.lightui.util.SimpleObjectPool;
import com.timecat.widget.photoview.PhotoViewAttacher;
import com.timecat.widget.photoview.PhotoViewEx;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by 大灯泡 on 2017/4/1.
 * <p>
 * 图片预览的adapter
 */

public class PhotoBrowserAdapter extends PagerAdapter implements ClearMemoryObject {

    private List<ImageInfo> datas;
    private Context context;
    private SimpleObjectPool<PhotoViewEx> viewPool;
    private PhotoViewAttacher.OnViewTapListener onViewTapListener;

    public PhotoBrowserAdapter(Context context, List<ImageInfo> datas) {
        viewPool = new SimpleObjectPool<>(PhotoViewEx.class);
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    public ImageInfo getImageInfo(int pos) {
        try {
            return datas.get(pos);
        } catch (Exception e) {
            return null;
        }
    }

    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup container, int position) {
        final ImageInfo info = datas.get(position);
        PhotoViewEx photoView = viewPool.get();
        if (photoView == null) {
            photoView = new PhotoViewEx(context);
            photoView.setCacheInViewPager(true);
            photoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        ImageLoadManager.INSTANCE.loadImage(photoView, info.getImagePath());
        container.addView(photoView);
        if (photoView.getOnViewTapListener() == null) {
            photoView.setOnViewTapListener(onViewTapListener);
        }
        return photoView;
    }

    @Override
    public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
        if (object instanceof PhotoViewEx) {
//            ((PhotoViewEx) object).setImageBitmap(null);
            container.removeView((View) object);
            viewPool.put((PhotoViewEx) object);
        }
    }


    @Override
    public boolean isViewFromObject(@NotNull View view, @NotNull Object object) {
        return view == object;
    }

    public PhotoViewAttacher.OnViewTapListener getOnViewTapListener() {
        return onViewTapListener;
    }

    public void setOnViewTapListener(PhotoViewAttacher.OnViewTapListener onViewTapListener) {
        this.onViewTapListener = onViewTapListener;
    }

    @Override
    public void clearMemroy(boolean setNull) {

    }

}
