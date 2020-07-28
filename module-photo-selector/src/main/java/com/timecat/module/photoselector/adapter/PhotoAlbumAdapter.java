package com.timecat.module.photoselector.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.timecat.component.lightui.base.adapter.BaseRecyclerViewAdapter;
import com.timecat.component.lightui.base.adapter.BaseRecyclerViewHolder;
import com.timecat.component.lightui.common.entity.photo.AlbumInfo;
import com.timecat.component.lightui.imageloader.ImageLoadManager;
import com.timecat.module.photoselector.R;
import java.util.List;
import java.util.Locale;

/**
 * Created by 大灯泡 on 2017/3/24.
 * <p>
 * 相册adapter
 */

public class PhotoAlbumAdapter extends BaseRecyclerViewAdapter<AlbumInfo> {
    private static final String TAG = "PhotoAlbumAdapter";

    public PhotoAlbumAdapter(@NonNull Context context, @NonNull List<AlbumInfo> datas) {
        super(context, datas);
    }


    @Override
    protected int getViewType(int position, @NonNull AlbumInfo data) {
        return 0;
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.photoselector_item_photo_album;
    }

    @Override
    protected InnerViewHolder getViewHolder(ViewGroup parent, View rootView, int viewType) {
        return new InnerViewHolder(rootView, viewType);
    }

    private class InnerViewHolder extends BaseRecyclerViewHolder<AlbumInfo> {

        private ImageView albumThumb;
        private TextView albumName;
        private TextView photoCounts;



        public InnerViewHolder(View itemView, int viewType) {
            super(itemView, viewType);
            albumThumb= (ImageView) findViewById(R.id.album_thumb);
            albumName= (TextView) findViewById(R.id.album_name);
            photoCounts= (TextView) findViewById(R.id.album_photo_counts);
        }

        @Override
        public void onBindData(AlbumInfo data, int position) {
            photoCounts.setText(String.format(Locale.getDefault(),"(%d)",data.getPhotoCounts()));
            albumName.setText(data.getAlbumName());
            ImageLoadManager.INSTANCE.loadImage(albumThumb,data.getFirstPhoto());
        }
    }

}
