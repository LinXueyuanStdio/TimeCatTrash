package com.timecat.module.editor.idea.widget.single;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.timecat.component.commonsdk.utils.override.LogUtil;
import com.timecat.component.data.database.help.CheckListHelper;
import com.timecat.data.room.RoomClient;
import com.timecat.data.room.record.RoomRecord;
import com.timecat.module.editor.idea.R;
import com.timecat.module.editor.idea.widget.AppWidgetHelper;
import com.timecat.ui.block.temp.Def;

import java.util.List;

/**
 * Created by qiizhang on 2016/8/1. adapter service for checklist in a thing
 */
public class ChecklistWidgetService extends RemoteViewsService {

    public static final String TAG = "ChecklistWidgetService";

    private static final int LL_CHECK_LIST = R.id.ll_check_list_tv;


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ChecklistViewFactory(getApplicationContext(), intent);
    }

    static class ChecklistViewFactory implements RemoteViewsFactory {
        private String SIGNAL_0 = CheckListHelper.SIGNAL_0;
        private String SIGNAL_1 = CheckListHelper.SIGNAL_1;
        private String SIGNAL_2 = CheckListHelper.SIGNAL_2;
        private String SIGNAL_3 = CheckListHelper.SIGNAL_3;
        private String SIGNAL_4 = CheckListHelper.SIGNAL_4;

        private Context mContext;
        private Intent mIntent;

        private RoomRecord mThing;
        private List<String> mItems;

        ChecklistViewFactory(Context context, Intent intent) {
            mContext = context;
            mIntent = intent;
        }

        @Override
        public void onCreate() {
            init();
        }

        private void init() {
            LogUtil.i(TAG, "init()");
            long id = mIntent.getLongExtra(Def.Communication.KEY_ID, -1);

            mThing = RoomClient.recordDao().get(id);
            if (mThing == null) {
                LogUtil.i(TAG, "thing is null!");
                return;
            }

            LogUtil.i(TAG, "mThing.content[" + mThing.getContent() + "]");
            mItems = CheckListHelper.toCheckListItems(mThing.getContent(), false);
            mItems.remove(SIGNAL_2);
            mItems.remove(SIGNAL_3);
            mItems.remove(SIGNAL_4);
        }

        @Override
        public void onDataSetChanged() {
            Log.i(TAG, "onDataSetChanged()");
            init();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            final int count = getCount();
            if (position < 0 || position >= count) {
                return null;
            }
            RemoteViews rv = AppWidgetHelper.createRemoteViewsForChecklistItem(
                    mContext, mItems.get(position), count, true);
            setupEvents(rv, position);
            return rv;
        }

        private void setupEvents(RemoteViews rv, int position) {
            Intent intent = new Intent(Def.Communication.BROADCAST_ACTION_UPDATE_CHECKLIST);
            intent.putExtra(Def.Communication.KEY_ID, mThing.getId());
            intent.putExtra(Def.Communication.KEY_POSITION, position);
            rv.setOnClickFillInIntent(LL_CHECK_LIST, intent);
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
            final int count = getCount();
            if (position < 0 || position >= count) {
                return -1L;
            }
            return mItems.get(position).hashCode();
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }

}
