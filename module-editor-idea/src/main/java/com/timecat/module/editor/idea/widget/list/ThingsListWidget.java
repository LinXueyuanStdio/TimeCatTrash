package com.timecat.module.editor.idea.widget.list;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

import com.timecat.module.editor.idea.R;
import com.timecat.module.editor.idea.widget.AppWidgetDAO;
import com.timecat.module.editor.idea.widget.AppWidgetHelper;
import com.timecat.module.editor.idea.widget.ThingWidgetInfo;


/**
 * Created by ywwynm on 2016/8/7. App widget that shows a list of things
 */
public class ThingsListWidget extends AppWidgetProvider {

    public static final String TAG = "ThingsListWidget";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        AppWidgetDAO appWidgetDAO = AppWidgetDAO.getInstance(context);
        for (int appWidgetId : appWidgetIds) {
            ThingWidgetInfo info = appWidgetDAO.getThingWidgetInfoById(appWidgetId);
            if (info == null) {
                break;
            }

            // notify data set changed for things list
            // _(:3」∠)_, it seems this line should be written above next line....
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_things_list);

            int limit = -1 * (int) info.getThingId() - 1;
            appWidgetManager.updateAppWidget(appWidgetId,
                    AppWidgetHelper.createRemoteViewsForThingsList(context, limit, appWidgetId));

        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        AppWidgetDAO appWidgetDAO = AppWidgetDAO.getInstance(context);
        for (int appWidgetId : appWidgetIds) {
            appWidgetDAO.delete(appWidgetId);
        }
    }
}
