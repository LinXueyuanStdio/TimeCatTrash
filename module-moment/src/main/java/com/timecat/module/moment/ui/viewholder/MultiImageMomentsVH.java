package com.timecat.module.moment.ui.viewholder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.timecat.component.lightui.common.MomentsType;
import com.timecat.component.lightui.common.entity.PhotoBrowseInfo;
import com.timecat.component.lightui.common.entity.PhotoInfo;
import com.timecat.component.lightui.common.entity.bmob.MomentsInfo;
import com.timecat.component.lightui.imageloader.ImageLoadManager;
import com.timecat.component.lightui.widget.imageview.ForceClickImageView;
import com.timecat.module.moment.R;
import com.timecat.module.moment.activity.ActivityLauncher;

import java.util.ArrayList;
import java.util.List;

import razerdp.github.com.widget.PhotoContents;

/**
 * Created by 大灯泡 on 2016/11/3.
 * <p>
 * 九宮格圖片的vh
 *
 * @see MomentsType
 */

public class MultiImageMomentsVH extends CircleBaseViewHolder {

    @Override
    public int layoutId() {
        return R.layout.moment_moments_multi_image;
    }

    private PhotoContents imageContainer;
    private InnerContainerAdapter adapter;

    public MultiImageMomentsVH(View itemView, int viewType) {
        super(itemView, viewType);
    }


    @Override
    public void onFindView(@NonNull View rootView) {
        imageContainer = (PhotoContents) findView(imageContainer, R.id.circle_image_container);
    }

    @Override
    public void onBindDataToView(@NonNull MomentsInfo data, int position, int viewType) {
        if (adapter == null) {
            adapter = new InnerContainerAdapter(getContext(), data.getContent().getPics());
            imageContainer.setAdapter(adapter);
        } else {
            Log.i("",
                    "update image" + data.getAuthor().getNick()
                            + "     :  " + data.getContent().getPics()
                            .size());
            adapter.updateData(data.getContent().getPics());
        }
    }

    private class InnerContainerAdapter extends PhotoContents.Adapter {


        private Context context;
        private List<PhotoInfo> datas;

        InnerContainerAdapter(Context context, List<PhotoInfo> datas) {
            this.context = context;
            this.datas = new ArrayList<>();
            this.datas.addAll(datas);
        }

        public void updateData(List<PhotoInfo> datas) {
            this.datas.clear();
            this.datas.addAll(datas);
            notifyDataSetChanged();
        }

        public List<String> getPhotoUrls() {
            List<String> result = new ArrayList<>();
            for (PhotoInfo data : datas) {
                result.add(data.getUrl());
            }
            return result;
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public PhotoContents.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new PhotoHolder(new ForceClickImageView(context));
        }

        @Override
        public void onBindViewHolder(PhotoContents.ViewHolder viewHolder, int i) {
            PhotoInfo img = datas.get(i);
            String imageUrl = img.getUrl();
            Log.i("", "处理的url  >>>  " + imageUrl);
            ImageLoadManager.INSTANCE.loadImage((ImageView) viewHolder.rootView, imageUrl);
            viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Rect> rects = new ArrayList<>();
                    for (int j=0; j< getItemCount(); j++) {
                        Rect rect = new Rect();
                        viewHolder.rootView.getHitRect(rect );
                    }
                    PhotoBrowseInfo info = PhotoBrowseInfo
                            .create(adapter.getPhotoUrls(), rects, i);
                    ActivityLauncher.startToPhotoBrosweActivity((Activity) getContext(), info);
                }
            });
        }
    }

    class PhotoHolder extends PhotoContents.ViewHolder {

        public PhotoHolder(ForceClickImageView rootView) {
            super(rootView);
            rootView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
}
