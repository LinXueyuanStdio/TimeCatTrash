package com.timecat.module.editor.idea.widget.list;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.timecat.component.commonarms.core.BaseApplication;
import com.timecat.module.editor.idea.R;
import com.timecat.ui.block.adapter.RadioChooserAdapter;
import com.timecat.ui.block.base.BaseViewHolder;
import com.timecat.ui.block.temp.Def;
import com.timecat.ui.block.util.DisplayUtil;
import com.timecat.module.editor.idea.widget.AppWidgetDAO;
import com.timecat.module.editor.idea.widget.AppWidgetHelper;
import com.timecat.module.editor.idea.widget.ThingWidgetInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiizhang on 2016/8/10. Configuration Activity for things list widget
 */
public class ThingsListWidgetConfiguration extends AppCompatActivity {

    public static final String TAG = "ThingsListWidgetConfiguration";

    private RadioChooserAdapter mAdapter;

    private SeekBar mSbAlpha;
    private AppCompatCheckBox mCbAlphaHeader;
    private AppCompatCheckBox mCbSimpleView;

    private int mAppWidgetId;

    private boolean mIsSetting = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_things_list_widget_configuration);

        int color = DisplayUtil.getRandomColor(getApplicationContext());
        TextView tvTitle = (TextView) findViewById(R.id.tv_title_things_list_widget_configuration);
        if (tvTitle != null) {
            tvTitle.setTextColor(color);
        }
        TextView tvConfirm = (TextView) findViewById(R.id.tv_confirm_as_bt_things_list_config);
        if (tvConfirm != null) {
            tvConfirm.setTextColor(color);
        }

        List<String> items = new ArrayList<>(5);
        items.add("所有");
        items.add("日程1");
        items.add("日程2");
        items.add("日程3");
        items.add("日程4");

        final int p = (int) (DisplayUtil.getScreenDensity(this) * 8);
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_types_list_things_list_widget_config);
        mAdapter = new RadioChooserAdapter(this, items, color) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                holder.itemView.setPadding(p, 0, p, 0);
            }
        };
        mAdapter.pick(0);
        rv.setAdapter(mAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        mSbAlpha = (SeekBar) findViewById(R.id.sb_app_widget_alpha);
        mSbAlpha.setMax(100);
        DisplayUtil.setSeekBarColor(mSbAlpha, color);

        mCbSimpleView = (AppCompatCheckBox) findViewById(R.id.cb_simple_view);
        DisplayUtil.setCheckBoxColor(mCbSimpleView, color);
        findViewById(R.id.rl_simple_view_as_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCbSimpleView.toggle();
            }
        });

        mCbAlphaHeader = (AppCompatCheckBox) findViewById(R.id.cb_alpha_header);
        DisplayUtil.setCheckBoxColor(mCbAlphaHeader, color);
        findViewById(R.id.rl_alpha_header_as_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCbAlphaHeader.toggle();
            }
        });

        Intent intent = getIntent();
        mAppWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        int appWidgetId2 = intent.getIntExtra(Def.Communication.KEY_WIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        intent = new Intent();
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_CANCELED, intent);

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            mAppWidgetId = appWidgetId2;
            if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                finish();
            } else {
                mIsSetting = true;
            }
        }

        AppWidgetDAO dao = AppWidgetDAO.getInstance(getApplicationContext());
        ThingWidgetInfo info = dao.getThingWidgetInfoById(mAppWidgetId);
        int alpha = 100;
        if (info != null) {
            alpha = info.getAlpha();
            mCbSimpleView.setChecked(info.getStyle() == ThingWidgetInfo.STYLE_SIMPLE);
            mCbAlphaHeader.setChecked(alpha < 0);
        }
        if (alpha == ThingWidgetInfo.HEADER_ALPHA_0) {
            mSbAlpha.setProgress(0);
        } else {
            mSbAlpha.setProgress(Math.abs(alpha));
        }
    }

    public void onConfirmClicked(View view) {
        int limit = mAdapter.getPickedPosition();
        Context app = BaseApplication.getContext();
        AppWidgetDAO appWidgetDAO = AppWidgetDAO.getInstance(app);
        if (mIsSetting) {
            appWidgetDAO.delete(mAppWidgetId);
        }

        @ThingWidgetInfo.Style int style = ThingWidgetInfo.STYLE_NORMAL;
        if (mCbSimpleView.isChecked()) {
            style = ThingWidgetInfo.STYLE_SIMPLE;
        }
        int alpha = mSbAlpha.getProgress();
        if (mCbAlphaHeader.isChecked()) {
            if (alpha != 0) {
                alpha = -alpha;
            } else {
                alpha = ThingWidgetInfo.HEADER_ALPHA_0;
            }
        }
        appWidgetDAO.insert(mAppWidgetId, -limit - 1, ThingWidgetInfo.SIZE_MIDDLE,
                alpha, style);

        if (!mIsSetting) {
            RemoteViews views = AppWidgetHelper.createRemoteViewsForThingsList(
                    this, limit, mAppWidgetId);
            AppWidgetManager.getInstance(app).updateAppWidget(mAppWidgetId, views);
            Intent intent = new Intent();
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, intent);
        } else {
            AppWidgetHelper.updateThingsListAppWidget(app, mAppWidgetId);
        }
        finish();
    }
}
