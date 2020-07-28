package com.timecat.module.editor.idea.widget.list;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.timecat.data.room.RoomClient;
import com.timecat.data.room.record.RoomRecord;
import com.timecat.module.editor.idea.R;
import com.timecat.module.editor.idea.widget.AppWidgetHelper;
import com.timecat.ui.block.temp.Def;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by qiizhang on 2016/8/8. adapter service for things list
 */
public class ThingsListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ThingsListViewFactory(getApplicationContext(), intent);
    }

    static class ThingsListViewFactory implements RemoteViewsFactory {

        private Context mContext;
        private Intent mIntent;

        private int mAppWidgetId;

        private List<RoomRecord> mThings;

        ThingsListViewFactory(Context context, Intent intent) {
            mContext = context;
            mIntent = intent;
        }

        @Override
        public void onCreate() {
            init();
        }

        private void init() {
            //find by limit TODO
            int limit = mIntent.getIntExtra(Def.Communication.KEY_LIMIT, 0);

            mThings = RoomClient.recordDao().getBetween(
                    new DateTime().withMillisOfDay(0).getMillis(),
                    new DateTime().plusDays(1).withMillisOfDay(0).getMillis());

            mAppWidgetId = mIntent.getIntExtra(Def.Communication.KEY_WIDGET_ID, 0);
        }

        @Override
        public void onDataSetChanged() {
            init();
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return mThings.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position < -1 || position >= getCount()) {
                return null;
            }
            RoomRecord thing = mThings.get(position);
            RemoteViews rv = AppWidgetHelper.createRemoteViewsForThingsListItem(
                    mContext, thing, mAppWidgetId);
            Intent intent = new Intent();
            intent.putExtra(Def.Communication.KEY_ID, thing.getId());
            intent.putExtra(Def.Communication.KEY_POSITION, position + 1);
            rv.setOnClickFillInIntent(R.id.root_widget_thing, intent);
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            if (position < 0 || position > mThings.size() - 1) {
                return -1L;
            }
            return mThings.get(position).getId();
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
