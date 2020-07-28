package com.timecat.module.editor.idea.widget.single;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.util.Pair;

import com.timecat.component.commonsdk.utils.override.LogUtil;
import com.timecat.data.room.RoomClient;
import com.timecat.data.room.record.RoomRecord;
import com.timecat.module.editor.idea.R;
import com.timecat.module.editor.idea.widget.AppWidgetDAO;
import com.timecat.module.editor.idea.widget.AppWidgetHelper;
import com.timecat.module.editor.idea.widget.RemoteActionHelper;
import com.timecat.module.editor.idea.widget.ThingWidgetInfo;
import com.timecat.ui.block.temp.Def;

import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by qiizhang on 2016/8/1. basic single thing widget
 */
public abstract class BaseThingWidget extends AppWidgetProvider {

    protected abstract String getTag();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(getTag(), "onReceive(context, intent) is called, action[" + intent.getAction() + "]");
        if (Def.Communication.BROADCAST_ACTION_UPDATE_CHECKLIST.equals(intent.getAction())) {
            long id = intent.getLongExtra(Def.Communication.KEY_ID, -1);
            int itemPos = intent.getIntExtra(Def.Communication.KEY_POSITION, 0);
            RemoteActionHelper.toggleChecklistItem(context, id, itemPos);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        AppWidgetDAO appWidgetDAO = AppWidgetDAO.getInstance(context);
        for (int appWidgetId : appWidgetIds) {
            updateSingleThingAppWidget(appWidgetDAO, appWidgetManager, context, appWidgetId);
        }
    }

    private void updateSingleThingAppWidget(AppWidgetDAO appWidgetDAO,
                                            AppWidgetManager appWidgetManager, Context context, int appWidgetId) {
        final String TAG = getTag();
        LogUtil.i(TAG, "updateSingleThingAppWidget is called, appWidgetId[" + appWidgetId + "]");
        ThingWidgetInfo thingWidgetInfo = appWidgetDAO.getThingWidgetInfoById(appWidgetId);
        if (thingWidgetInfo == null) {
            LogUtil.e(TAG, "updateSingleThingAppWidget but thingWidgetInfo is null");
            return;
        }

        Pair<Integer, RoomRecord> pair = getThingAndPositionFromManager(thingWidgetInfo.getThingId());
        int position;
        RoomRecord thing;
        if (pair == null) {
            position = -1;
            thing = RoomClient.recordDao().get(thingWidgetInfo.getThingId());
            if (thing == null) {
                LogUtil.e(TAG, "updateSingleThingAppWidget but thing is null");
                return;
            }
        } else {
            position = pair.first;
            thing = pair.second;
        }

        // This line is necessary if there is a checklist
        // _(:3」∠)_, it seems this line should be written above next line....
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_thing_check_list);

        LogUtil.e(TAG, "updateSingleThingAppWidget, thing.content[" + thing.getContent() + "]");
        appWidgetManager.updateAppWidget(appWidgetId,
                AppWidgetHelper.createRemoteViewsForSingleThing(
                        context, thing, position, appWidgetId, getClass()));
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        AppWidgetDAO appWidgetDAO = AppWidgetDAO.getInstance(context);
        for (int appWidgetId : appWidgetIds) {
            appWidgetDAO.delete(appWidgetId);
        }
    }

    @Nullable
    private Pair<Integer, RoomRecord> getThingAndPositionFromManager(long thingId) {
        Pair<Integer, RoomRecord> pair = null;
        List<RoomRecord> things = RoomClient.recordDao().getBetween(
                new DateTime().withMillisOfDay(0).getMillis(),
                new DateTime().plusDays(1).withMillisOfDay(0).getMillis());
        if (things == null) return null;
        final int size = things.size();
        for (int i = 0; i < size; i++) {
            RoomRecord thing = things.get(i);
            if (thing.getId() == thingId) {
                pair = new Pair<>(i, thing);
            }
        }
        return pair;
    }

}
