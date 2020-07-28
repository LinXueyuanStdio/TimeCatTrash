package com.timecat.module.editor.idea.widget.single;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.timecat.data.room.RoomClient;
import com.timecat.data.room.record.RoomRecord;
import com.timecat.module.editor.idea.EverythingDoneBaseActivity;
import com.timecat.module.editor.idea.R;
import com.timecat.module.editor.idea.util.SimplePermissionCallback;
import com.timecat.module.editor.idea.widget.AppWidgetDAO;
import com.timecat.module.editor.idea.widget.AppWidgetHelper;
import com.timecat.module.editor.idea.widget.BaseThingsAdapter;
import com.timecat.module.editor.idea.widget.ThingWidgetInfo;
import com.timecat.ui.block.temp.Def;
import com.timecat.ui.block.util.DeviceUtil;
import com.timecat.ui.block.util.DisplayUtil;
import com.timecat.ui.block.util.EdgeEffectUtil;
import com.timecat.ui.block.util.PermissionUtil;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

public class BaseThingWidgetConfiguration extends EverythingDoneBaseActivity {

    public static final int NORMAL = 0;
    public static final int MOVING = 1;
    public static final int SELECTING = 2;
    private Toolbar mActionBar;
    private RecyclerView mRecyclerView;
    private ThingsAdapter mAdapter;
    private List<RoomRecord> mThings;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private int mSpanCount;
    private int mAppWidgetId;
    private FrameLayout mFlPreviewAndConfig;
    private LinearLayout mLlConfig;
    private int mWidgetAlpha = 100;

    protected Class getSenderClass() {
        return BaseThingWidget.class;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_thing_widget_configuration;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mSpanCount = DisplayUtil.isTablet(this) ? 3 : 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mSpanCount++;
        }
        mStaggeredGridLayoutManager.setSpanCount(mSpanCount);

        if (mThings.size() > 1) {
            mRecyclerView.scrollToPosition(0);
        }
        mAdapter.notifyDataSetChanged();

        if (DeviceUtil.hasKitKatApi()) {
            updateBottomUiMarginForNavBar((FrameLayout.LayoutParams) mLlConfig.getLayoutParams());
        }
    }

    @Override
    protected void initMembers() {
        Intent intent = getIntent();
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_CANCELED, intent);

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        mSpanCount = DisplayUtil.isTablet(this) ? 3 : 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mSpanCount++;
        }

        DateTime time = new DateTime().withMillisOfDay(0);
        long start = time.getMillis();
        long end = time.plusDays(1).getMillis();
        mThings = RoomClient.recordDao().getBetween(start, end);
//                    .getThingsForDisplay(Def.LimitForGettingThings.ALL_UNDERWAY);TODO 不要简单粗暴findAll，要根据ALL_UNDERWAY来找
//        mThings.remove(0); // no header
        mAdapter = new ThingsAdapter();
    }

    @Override
    protected void findViews() {
        mActionBar = f(R.id.actionbar);
        mRecyclerView = f(R.id.rv_things);

        mFlPreviewAndConfig = f(R.id.fl_app_widget_preview_and_ui_config);
        mLlConfig = f(R.id.ll_widget_ui_config);
    }

    @Override
    protected void initUI() {
        updateStatusBarAndBottomUi(true);
        DisplayUtil.expandLayoutToStatusBarAboveLollipop(this);
        DisplayUtil.expandStatusBarViewAboveKitkat(findViewById(R.id.view_status_bar));
        DisplayUtil.darkStatusBar(this);

        if (DeviceUtil.hasLollipopApi()) {
            mLlConfig.setBackgroundColor(Color.parseColor("#66000000"));
        } else if (DeviceUtil.hasKitKatApi()) {
            mLlConfig.setBackgroundColor(Color.TRANSPARENT);
        }

        if (!PermissionUtil.hasStoragePermission(this)) {
            doWithPermissionChecked(new SimplePermissionCallback() {
                                        @Override
                                        public void onGranted() {
                                            initRecyclerView();
                                        }

                                        @Override
                                        public void onDenied() {
                                            super.onDenied();
                                            finish();
                                        }
                                    }, Def.Communication.REQUEST_PERMISSION_LOAD_THINGS_2,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            initRecyclerView();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void updateStatusBarAndBottomUi(boolean selecting) {
        if (!DeviceUtil.hasKitKatApi()) {
            return;
        }

        final Window window = getWindow();
        FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams) mLlConfig.getLayoutParams();

        if (selecting) {
            if (DeviceUtil.hasLollipopApi()) {
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.bg_statusbar_lollipop));
            }
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            flp.bottomMargin = 0;
            mLlConfig.requestLayout();
        } else {
            if (DeviceUtil.hasLollipopApi()) {
                window.setStatusBarColor(Color.TRANSPARENT);
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            updateBottomUiMarginForNavBar(flp);
        }
    }

    private void updateBottomUiMarginForNavBar(FrameLayout.LayoutParams flp) {
        if (DisplayUtil.hasNavigationBar(this)) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                flp.rightMargin = 0;
                flp.bottomMargin = DisplayUtil.getNavigationBarHeight(this);
            } else {
                flp.rightMargin = DisplayUtil.getNavigationBarHeight(this);
                flp.bottomMargin = 0;
            }
            mLlConfig.requestLayout();
        }
    }

    private void initRecyclerView() {
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(
                mSpanCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setActionbar() {
        setSupportActionBar(mActionBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void setEvents() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            final int edgeColor = EdgeEffectUtil.getEdgeColorDark();

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                EdgeEffectUtil.forRecyclerView(recyclerView, edgeColor);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(BaseThingWidgetConfiguration.this).resumeRequests();
                } else { // dragging or settling
                    Glide.with(BaseThingWidgetConfiguration.this).pauseRequests();
                }
            }
        });
    }

    private void previewAppWidget(final RoomRecord thing) {
        mFlPreviewAndConfig.setVisibility(View.VISIBLE);
        mActionBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        DisplayUtil.cancelDarkStatusBar(this);

        ImageView ivBackground = f(R.id.iv_app_widget_preview_background);
        WallpaperManager wm = WallpaperManager.getInstance(getApplicationContext());
        Drawable wallpaper = wm.getDrawable();
        if (wallpaper != null) {
            ivBackground.setImageDrawable(wallpaper);
        }

        final List<RoomRecord> singleThing = Collections.singletonList(thing);
        final BaseThingsAdapter adapter = new BaseThingsAdapter(this) {

            @Override
            protected int getCurrentMode() {
                return NORMAL;
            }

            @Override
            protected List<RoomRecord> getThings() {
                return singleThing;
            }

            @Override
            public void onBindViewHolder(@NotNull BaseThingViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                holder.cv.setRadius(0);
                holder.cv.setCardElevation(0);
                int alpha = (int) (mWidgetAlpha / 100f * 255);
                int alphaColor = DisplayUtil.getTransparentColor(
                        thing.taskLabelColor(), alpha);
                holder.cv.setCardBackgroundColor(alphaColor);
                holder.ivStickyOngoing.setImageAlpha(alpha);
            }
        };
        final RecyclerView rvPreview = f(R.id.rv_app_widget_preview);
        FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams) rvPreview.getLayoutParams();
        flp.width = DisplayUtil.getThingCardWidth(this);
        rvPreview.requestLayout();
        rvPreview.setAdapter(adapter);
        rvPreview.setLayoutManager(new LinearLayoutManager(this));
        rvPreview.setOnTouchListener(new View.OnTouchListener() {
            private int mDx, mDy;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int rawX = (int) event.getRawX();
                final int rawY = (int) event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams)
                                rvPreview.getLayoutParams();
                        mDx = rawX - flp.leftMargin;
                        mDy = rawY - flp.topMargin;
                        return true;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams)
                                rvPreview.getLayoutParams();
                        flp.leftMargin = rawX - mDx;
                        flp.topMargin = rawY - mDy;
                        rvPreview.requestLayout();
                        return true;
                    }
                }
                return false;
            }
        });

        SeekBar sbAlpha = f(R.id.sb_app_widget_alpha);
        sbAlpha.setMax(100);
        sbAlpha.setProgress(100);
        DisplayUtil.setSeekBarColor(sbAlpha, ContextCompat.getColor(this, R.color.app_accent));
        sbAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mWidgetAlpha = progress;
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        Button btFinish = f(R.id.bt_finish_set_alpha_app_widget);
        DisplayUtil.setButtonColor(btFinish, Color.WHITE);
        btFinish.setTextColor(thing.taskLabelColor());
        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endSelectThing(thing);
            }
        });
    }

    private void endPreviewAppWidget() {
        mFlPreviewAndConfig.setVisibility(View.GONE);
        mActionBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);

        updateStatusBarAndBottomUi(true);
        DisplayUtil.darkStatusBar(this);
    }

    private void endSelectThing(RoomRecord thing) {
        Class clazz = getSenderClass();
        AppWidgetDAO.getInstance(this).insert(mAppWidgetId, thing.getId(),
                AppWidgetHelper.getSizeByProviderClass(clazz), mWidgetAlpha,
                ThingWidgetInfo.STYLE_NORMAL);

        RemoteViews views = AppWidgetHelper.createRemoteViewsForSingleThing(
                this, thing, -1, mAppWidgetId, clazz);
        AppWidgetManager.getInstance(this).updateAppWidget(mAppWidgetId, views);

        Intent intent = new Intent();
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (mFlPreviewAndConfig.getVisibility() == View.VISIBLE) {
            endPreviewAppWidget();
        } else {
            super.onBackPressed();
        }
    }

    class ThingsAdapter extends BaseThingsAdapter {

        ThingsAdapter() {
            super(BaseThingWidgetConfiguration.this);
        }

        @Override
        protected int getCurrentMode() {
            return NORMAL;
        }

        @Override
        protected List<RoomRecord> getThings() {
            return mThings;
        }

        @NotNull
        @Override
        public BaseThingViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
            return new Holder(mInflater.inflate(R.layout.card_thing, parent, false));
        }

        @Override
        public void onBindViewHolder(@NotNull BaseThingViewHolder holder, int position) {
            int m = (int) (mDensity * 4);
            if (DeviceUtil.hasLollipopApi()) {
                m = (int) (mDensity * 6);
            }

            StaggeredGridLayoutManager.LayoutParams lp =
                    (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            lp.setMargins(m, m, m, m);

            super.onBindViewHolder(holder, position);
        }

        class Holder extends BaseThingViewHolder {

            Holder(View item) {
                super(item);

                cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateStatusBarAndBottomUi(false);
                        previewAppWidget(mThings.get(getAdapterPosition()));
                    }
                });
            }
        }

    }
}
