package com.timecat.module.editor.idea;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.timecat.component.readonly.RouterHub;
import com.timecat.component.ui.utils.MenuTintUtils;
import com.timecat.ui.block.adapter.ImageViewerPagerAdapter;
import com.timecat.ui.block.fragment.AlertDialogFragment;
import com.timecat.ui.block.temp.Def;
import com.timecat.ui.block.util.AttachmentHelper;
import com.timecat.ui.block.util.DeviceUtil;
import com.timecat.ui.block.util.DisplayUtil;
import com.timecat.ui.block.util.EdgeEffectUtil;
import com.timecat.ui.block.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Route(path = RouterHub.IDEA_EDITOR_ImageViewerActivity)
public class ImageViewerActivity extends EverythingDoneBaseActivity {

    public static final String TAG = "ImageViewerActivity";

    private boolean mSystemUiVisible = true;

    private int mAccentColor;
    private boolean mEditable;
    private List<String> imagePaths;
    private List<String> videoPaths;
    private int mPosition;

    private boolean mUpdated = false;

    private Toolbar mActionbar;

    private ViewPager mVpImage;
    private ImageViewerPagerAdapter mAdapter;
    private List<View> mTabs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_image_viewer;
    }

    @Override
    public void onBackPressed() {
        returnToDetailActivity();
    }

    @Override
    protected void initMembers() {
        Intent intent = getIntent();
        mAccentColor = intent.getIntExtra(Def.Communication.KEY_COLOR, 0);
        mEditable = intent.getBooleanExtra(Def.Communication.KEY_EDITABLE, true);
        imagePaths = intent.getStringArrayListExtra(Def.Communication.KEY_TYPE_IMAGE_PATH_NAME);
        videoPaths = intent.getStringArrayListExtra(Def.Communication.KEY_TYPE_VIDEO_PATH_NAME);
        mPosition = intent.getIntExtra(Def.Communication.KEY_POSITION, 0);

        int size1 = imagePaths.size();
        int size2 = videoPaths.size();
        mTabs = new ArrayList<>(size1 + size2);
    }

    @Override
    protected void findViews() {
        mActionbar = f(R.id.actionbar);
        mVpImage = f(R.id.vp_image_viewer);
    }

    @Override
    protected void initUI() {
        setTheme(R.style.EverythingDoneTheme_ImageViewer);
        View decorView = getWindow().getDecorView();
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(flags);

        int appAccent = ContextCompat.getColor(this, R.color.app_accent);
        EdgeEffectUtil.forViewPager(mVpImage, appAccent);

        int[] size = getImageSize();
        OnPhotoTapListener imageListener = getImageListener();
        View.OnClickListener videoListener = getVideoListener();

        LayoutInflater inflater = LayoutInflater.from(this);

        for (String videoPath : videoPaths) {
            @SuppressLint("InflateParams")
            View tab = inflater.inflate(R.layout.tab_image_attachment, null);

            ProgressBar pb = f(tab, R.id.pb_image_attachment);
            ImageView iv = f(tab, R.id.iv_image_attachment);
            ImageView videoSignal = f(tab, R.id.iv_video_signal);

            pb.getIndeterminateDrawable().setColorFilter(appAccent, PorterDuff.Mode.SRC_IN);

            final PhotoViewAttacher attacher = new PhotoViewAttacher(iv);
            attacher.setScaleLevels(1.0f, 3.0f, 6.0f);

            iv.setContentDescription(getString(R.string.cd_video_attachment));
            videoSignal.setVisibility(View.VISIBLE);

            loadImage(videoPath, iv, attacher, pb, size);

            videoSignal.setOnClickListener(videoListener);
            attacher.setZoomable(false);

            mTabs.add(tab);
        }

        for (String imagePath : imagePaths) {
            @SuppressLint("InflateParams")
            View tab = inflater.inflate(R.layout.tab_image_attachment, null);

            ProgressBar pb = f(tab, R.id.pb_image_attachment);
            ImageView iv = f(tab, R.id.iv_image_attachment);
            ImageView videoSignal = f(tab, R.id.iv_video_signal);

            pb.getIndeterminateDrawable().setColorFilter(appAccent, PorterDuff.Mode.SRC_IN);

            final PhotoViewAttacher attacher = new PhotoViewAttacher(iv);
            attacher.setScaleLevels(1.0f, 3.0f, 6.0f);

            iv.setContentDescription(getString(R.string.cd_image_attachment));
            videoSignal.setVisibility(View.GONE);

            loadImage(imagePath, iv, attacher, pb, size);

            attacher.setOnPhotoTapListener(imageListener);

            mTabs.add(tab);
        }

        mAdapter = new ImageViewerPagerAdapter(mTabs);
        mVpImage.setAdapter(mAdapter);

        mVpImage.setCurrentItem(mPosition);
    }

    private int[] getImageSize() {
        Point screen = DisplayUtil.getScreenSize(this);
        int width = screen.x;
        int height = screen.y;
        if (!DeviceUtil.hasKitKatApi() && DisplayUtil.hasNavigationBar(this)) {
            int navigationBarHeight = DisplayUtil.getNavigationBarHeight(this);
            if (getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_LANDSCAPE) {
                width -= navigationBarHeight;
            } else {
                height -= navigationBarHeight;
            }
        }
        return new int[]{width, height};
    }

    private OnPhotoTapListener getImageListener() {
        return new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                toggleSystemUI();
            }
        };
    }

    private View.OnClickListener getVideoListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = mVpImage.getCurrentItem();
                String typePathName = imagePaths.get(pos);
                String pathName = typePathName.substring(1, typePathName.length());
                File file = new File(pathName);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri;
                if (DeviceUtil.hasNougatApi()) {
                    ContentValues contentValues = new ContentValues(1);
                    contentValues.put(MediaStore.Video.Media.DATA, file.getAbsolutePath());
                    uri = getContentResolver()
                            .insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
                } else {
                    uri = Uri.fromFile(file);
                }
                intent.setDataAndType(uri,
                        "video/" + FileUtil.getPostfix(pathName));
                startActivity(intent);
            }
        };
    }

    private void loadImage(
            String pathName, final ImageView iv, final PhotoViewAttacher attacher,
            final ProgressBar pb, int[] size) {
        Glide.with(this)
                .load(pathName)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                   DataSource dataSource, boolean isFirstResource) {
                        iv.setImageDrawable(resource);
                        attacher.update();
                        pb.setVisibility(View.GONE);
                        return true;
                    }
                })
                .apply(new RequestOptions().override(size[0], size[1]))
                .into(iv);
    }

    @Override
    protected void setActionbar() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mActionbar.getLayoutParams();
        params.setMargins(0, DisplayUtil.getStatusbarHeight(this), 0, 0);
        mActionbar.requestLayout();

        setSupportActionBar(mActionbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        updateAttachmentNumber();
        mActionbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToDetailActivity();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_viewer, menu);
        MenuTintUtils.tintAllIcons(menu, this);
        if (!mEditable) {
            MenuItem item = menu.findItem(R.id.act_delete_attachment);
            item.setVisible(false);
            item.setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.act_show_attachment_info) {
            int cur = mVpImage.getCurrentItem();
            if (cur < imagePaths.size()) {
                AttachmentHelper.showImageAttachmentInfoDialog(
                        this, mAccentColor, imagePaths.get(cur));
            } else {
                AttachmentHelper.showVideoAttachmentInfoDialog(
                        this, mAccentColor, videoPaths.get(cur - imagePaths.size()));
            }
        } else if (id == R.id.act_delete_attachment) {
            final AlertDialogFragment adf = new AlertDialogFragment();
            adf.setContentColor(ContextCompat.getColor(this, R.color.black_69p));
            adf.setConfirmColor(mAccentColor);
            adf.setContent(getString(R.string.alert_delete_attachment));
            adf.setConfirmListener(new AlertDialogFragment.ConfirmListener() {
                @Override
                public void onConfirm() {
                    int currentIndex = mVpImage.getCurrentItem();
                    imagePaths.remove(currentIndex);
                    mAdapter.removeTab(mVpImage, currentIndex);
                    updateAttachmentNumber();
                    mUpdated = true;
                    if (mAdapter.getCount() == 0) {
                        returnToDetailActivity();
                    }
                }
            });
            adf.show(getFragmentManager(), AlertDialogFragment.TAG);
            return true;
        }
        return false;
    }

    @Override
    protected void setEvents() {
        mVpImage.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                updateAttachmentNumber();
            }
        });
    }

    private void updateAttachmentNumber() {
        int current = mVpImage.getCurrentItem() + 1;
        int total = imagePaths.size();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(current + " / " + total);
        }
    }

    private void toggleSystemUI() {
        View decorView = getWindow().getDecorView();
        int visibility = decorView.getSystemUiVisibility();
        if (mSystemUiVisible) {
            if (DeviceUtil.hasKitKatApi()) {
                decorView.setSystemUiVisibility(visibility
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
            } else {
                decorView.setSystemUiVisibility(visibility
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE);
            }
            mActionbar.setVisibility(View.GONE);
        } else {
            if (DeviceUtil.hasKitKatApi()) {
                decorView.setSystemUiVisibility(visibility
                        & ~View.SYSTEM_UI_FLAG_FULLSCREEN
                        & ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        & ~View.SYSTEM_UI_FLAG_IMMERSIVE);
            } else {
                decorView.setSystemUiVisibility(visibility
                        & ~View.SYSTEM_UI_FLAG_FULLSCREEN
                        & ~View.SYSTEM_UI_FLAG_LOW_PROFILE);
            }
            mActionbar.setVisibility(View.VISIBLE);
        }
        mSystemUiVisible = !mSystemUiVisible;
    }

    private void returnToDetailActivity() {
        if (mUpdated) {
            Intent intent = new Intent();
            intent.putExtra(Def.Communication.KEY_TYPE_IMAGE_PATH_NAME, (ArrayList) imagePaths);
            setResult(Def.Communication.RESULT_UPDATE_IMAGE_DONE, intent);
        } else {
            setResult(Def.Communication.RESULT_NO_UPDATE);
        }
        finish();
    }
}
