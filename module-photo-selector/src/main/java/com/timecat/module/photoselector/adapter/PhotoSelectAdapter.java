package com.timecat.module.photoselector.adapter;

import static com.timecat.module.photoselector.fragment.PhotoGridFragement.MAX_COUNT;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.alibaba.android.arouter.launcher.ARouter;
import com.timecat.component.readonly.RouterHub;
import com.timecat.component.lightui.base.adapter.BaseRecyclerViewAdapter;
import com.timecat.component.lightui.base.adapter.BaseRecyclerViewHolder;
import com.timecat.component.lightui.common.entity.ImageInfo;
import com.timecat.component.lightui.common.entity.photo.PhotoBrowserInfo;
import com.timecat.component.lightui.imageloader.ImageLoadManager;
import com.timecat.component.lightui.util.UIHelper;
import com.timecat.component.lightui.util.ViewUtil;
import com.timecat.component.lightui.widget.imageview.CheckImageView;
import com.timecat.module.photoselector.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 大灯泡 on 2017/3/24.
 * <p>
 * 图片选择adapter
 */

public class PhotoSelectAdapter extends BaseRecyclerViewAdapter<ImageInfo> {

  private static final String TAG = "PhotoSelectAdapter";

  private final int itemDecoration;
  private List<ImageInfo> selectedRecordLists;
  private boolean selectable = true;
  private String curAlbumName;
  private int maxCount = MAX_COUNT;

  public PhotoSelectAdapter(@NonNull Context context, int itemDecoration,
      @NonNull List<ImageInfo> datas) {
    this(context, itemDecoration, datas, MAX_COUNT);
  }

  public PhotoSelectAdapter(@NonNull Context context, int itemDecoration,
      @NonNull List<ImageInfo> datas, int maxCount) {
    super(context, datas);
    this.itemDecoration = itemDecoration;
    this.selectedRecordLists = new ArrayList<>();
    this.maxCount = maxCount;
  }

  @Override
  protected int getViewType(int position, @NonNull ImageInfo data) {
    return 0;
  }

  @Override
  protected int getLayoutResId(int viewType) {
    return R.layout.photoselector_item_photo_select;
  }

  @Override
  protected InnerViewHolder getViewHolder(ViewGroup parent, View rootView, int viewType) {
    return new InnerViewHolder(rootView, viewType);
  }

  private void onSelectPhoto(ImageInfo info) {
    if (info == null) {
      return;
    }
    if (!checkSelectListLength()) {
      return;
    }
    selectedRecordLists.add(info);
    callSelectListenerChange();
  }

  /**
   * 这里不可以用list.remove(Object)，因为当进行过选择之后，内部的实例不一定是同一个对象了。
   * <p>
   * {@link com.timecat.module.photoselector.fragment.PhotoGridFragement#updateSelectList(List)}以及{@link
   * com.timecat.module.photoselector.PhotoMultiBrowserActivity#finish()}
   */
  private void onUnSelectPhoto(ImageInfo info) {
    if (info == null) {
      return;
    }
    if (selectedRecordLists.size() <= 0) {
      return;
    }
    for (int i = 0; i < selectedRecordLists.size(); i++) {
      ImageInfo selectedInfo = selectedRecordLists.get(i);
      if (selectedInfo.compareTo(info) == 0) {
        selectedRecordLists.remove(i);
        break;
      }
    }
    callSelectListenerChange();
  }

  private void callSelectListenerChange() {
    final int size = selectedRecordLists.size();
    boolean hasChangeState = selectable;
    selectable = checkSelectListLength();
    hasChangeState = hasChangeState != selectable;
    if (hasChangeState) {
      notifyDataSetChanged();
    }
    if (onSelectCountChangeLisntenr != null) {
      onSelectCountChangeLisntenr.onSelectCountChange(size);
    }
  }

  private boolean checkSelectListLength() {
    return !(selectedRecordLists.size() >= maxCount || selectedRecordLists.size() < 0);
  }

  @Override
  public void updateData(List<ImageInfo> datas) {
    clearSelectRecord();
    super.updateData(datas);
  }

  public void updateSelections(List<ImageInfo> newDatas) {
    if (newDatas != null) {
      selectedRecordLists.clear();
      selectedRecordLists.addAll(newDatas);
      selectable = selectedRecordLists.size() != maxCount;
    }
  }

  public String getCurAlbumName() {
    return curAlbumName;
  }

  public void setCurAlbumName(String curAlbumName) {
    this.curAlbumName = curAlbumName;
  }

  public List<ImageInfo> getSelectedRecordLists() {
    return new ArrayList<>(selectedRecordLists);
  }

  private void clearSelectRecord() {
    selectedRecordLists.clear();
    selectable = true;
  }

  private class InnerViewHolder extends BaseRecyclerViewHolder<ImageInfo> {

    private CheckImageView checkImageView;
    private InnerClickEventClass clickEventClass;
    private View maskView;

    InnerViewHolder(View itemView, int viewType) {
      super(itemView, viewType);
      checkImageView = (CheckImageView) findViewById(R.id.iv_photo_select);
      maskView = findViewById(R.id.iv_photo_mask);
      setCheckImageViewLayoutParams();
      clickEventClass = new InnerClickEventClass();
    }

    /**
     * 根据屏幕宽度设置iv的大小
     */
    private void setCheckImageViewLayoutParams() {
      int itemDercorationSum = 5 * itemDecoration;
      int screenWidth = UIHelper.getScreenWidthPix(getContext());
      final int size = (screenWidth - itemDercorationSum) >> 2;
      FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size, size);
      params.width = params.height = size;
      checkImageView.setLayoutParams(params);

      FrameLayout.LayoutParams maskParams = new FrameLayout.LayoutParams(size, size);
      maskParams.width = maskParams.height = size;
      maskView.setLayoutParams(maskParams);
    }

    @Override
    public void onBindData(ImageInfo data, int position) {
      String url = TextUtils.isEmpty(data.thumbnailPath) ? data.imagePath : data.thumbnailPath;
      boolean isSelected = checkIsSelect(data);
      checkImageView.setCanSelect(isSelected || selectable);
      ViewUtil.setViewsVisible((isSelected || selectable) ? View.GONE : View.VISIBLE, maskView);
      checkImageView.setSelected(isSelected);
      ImageLoadManager.INSTANCE.loadImage(checkImageView, url);
      setupSelectChangeListener(data);
      clickEventClass.setCurPos(position);
      checkImageView.setOnClickListener(clickEventClass);

    }

    private void setupSelectChangeListener(ImageInfo data) {
      CheckImageView.OnSelectedChangeListener listener = checkImageView
          .getOnSelectedChangeListener();
      InnerSelectChangeClass innerSelectChangeClass;
      if (!(listener instanceof InnerSelectChangeClass)) {
        innerSelectChangeClass = new InnerSelectChangeClass();
      } else {
        innerSelectChangeClass = (InnerSelectChangeClass) listener;
      }
      checkImageView.setOnSelectedChangeListener(innerSelectChangeClass);
      innerSelectChangeClass.setData(data);
    }

    private boolean checkIsSelect(ImageInfo imageInfo) {
      if (imageInfo == null) {
        return false;
      }
      if (selectedRecordLists == null || selectedRecordLists.size() <= 0) {
        return false;
      }
      for (ImageInfo localSelectedPhoto : selectedRecordLists) {
        if (localSelectedPhoto.compareTo(imageInfo) == 0) {
          return true;
        }
      }
      return false;
    }

    class InnerSelectChangeClass implements CheckImageView.OnSelectedChangeListener {

      private ImageInfo data;

      public InnerSelectChangeClass() {
      }

      public ImageInfo getData() {
        return data;
      }

      public void setData(ImageInfo data) {
        this.data = data;
      }

      @Override
      public void onSelectChange(boolean isSelect) {
        if (isSelect) {
          onSelectPhoto(data);
        } else {
          onUnSelectPhoto(data);
        }
      }
    }

    class InnerClickEventClass implements View.OnClickListener {

      private int curPos;

      @Override
      public void onClick(View v) {
        PhotoBrowserInfo info = PhotoBrowserInfo.create(curPos, curAlbumName, selectedRecordLists);
        ARouter.getInstance()
            .build(RouterHub.PhotoMultiBrowserActivity.path)
            .withParcelable(RouterHub.PhotoMultiBrowserActivity.key_browserinfo, info)
            .withInt(RouterHub.PhotoMultiBrowserActivity.key_maxSelectCount, maxCount)
            .navigation((Activity) getContext(), RouterHub.PhotoMultiBrowserActivity.requestCode);
      }

      public void setCurPos(int curPos) {
        this.curPos = curPos;
      }

    }
  }

  private OnSelectCountChangeLisntenr onSelectCountChangeLisntenr;

  public OnSelectCountChangeLisntenr getOnSelectCountChangeLisntenr() {
    return onSelectCountChangeLisntenr;
  }

  public void setOnSelectCountChangeLisntenr(
      OnSelectCountChangeLisntenr onSelectCountChangeLisntenr) {
    this.onSelectCountChangeLisntenr = onSelectCountChangeLisntenr;
  }

  public interface OnSelectCountChangeLisntenr {

    void onSelectCountChange(int count);
  }
}
