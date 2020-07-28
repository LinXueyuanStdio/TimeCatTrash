package com.timecat.module.photoselector.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import com.timecat.component.lightui.base.BaseFragment;
import com.timecat.component.lightui.common.entity.ImageInfo;
import com.timecat.component.lightui.common.entity.photo.AlbumInfo;
import com.timecat.component.lightui.manager.localphoto.LocalPhotoManager;
import com.timecat.module.photoselector.R;
import com.timecat.module.photoselector.adapter.PhotoAlbumAdapter;
import com.timecat.module.photoselector.bus.EventSelectAlbum;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by 大灯泡 on 2017/3/29.
 * <p>
 * 相册浏览fragment
 */

public class PhotoAlbumFragement extends BaseFragment {
    private static final String TAG = "PhotoGridFragement";

    private RecyclerView mPhotoContent;
    private PhotoAlbumAdapter adapter;

    private List<AlbumInfo> datas;

    @Override
    public int getLayoutResId() {
        return R.layout.photoselector_frag_photo_album;
    }

    @Override
    protected void onInitData() {
        if (!LocalPhotoManager.INSTANCE.hasData()) return;
        findAndSetDatas();
    }


    @Override
    protected void onInitView(View rootView) {
        mPhotoContent = findView(R.id.album_content);
        adapter = new PhotoAlbumAdapter(getActivity(), datas);
        mPhotoContent.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter.setOnRecyclerViewItemClickListener(
            (v, position, data) -> EventBus.getDefault().post(new EventSelectAlbum(data.getAlbumName())));
        mPhotoContent.setAdapter(adapter);
    }

    private void findAndSetDatas() {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        LinkedHashMap<String, List<ImageInfo>> map = LocalPhotoManager.INSTANCE.getLocalImagesMap();
        Iterator iterator = map.entrySet().iterator();
        datas.clear();
        while (iterator.hasNext()) {
            Map.Entry<String, List<ImageInfo>> entry = (Map.Entry<String, List<ImageInfo>>) iterator.next();
            String albumName = entry.getKey();
            List<ImageInfo> photos = entry.getValue();
            AlbumInfo info = new AlbumInfo();
            info.setAlbumName(albumName);
            info.setPhotoCounts(photos.size());
            if (!isListEmpty(photos)) {
                ImageInfo lastInfo = photos.get(photos.size() - 1);
                String firstPhoto = TextUtils.isEmpty(lastInfo.thumbnailPath) ? lastInfo.imagePath : lastInfo.thumbnailPath;
                info.setFirstPhoto(firstPhoto);
            }
            datas.add(info);
        }
    }

    boolean isListEmpty(List<?> datas) {
        return datas == null || datas.size() <= 0;
    }

}
