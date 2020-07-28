package com.timecat.module.editor.idea.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.timecat.module.editor.idea.R;
import com.timecat.ui.block.temp.Def;


/**
 * Created by ywwynm on 2016/10/24. App Widget for checking upcoming thing
 */
public class CheckUpcomingWidget extends AppWidgetProvider {

    public static final String TAG = "CreateWidget";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(
                    context.getPackageName(), R.layout.app_widget_simple);
            remoteViews.setImageViewResource(
                    R.id.iv_widget_simple, R.drawable.widget_check_upcoming_content);
            remoteViews.setContentDescription(
                    R.id.iv_widget_simple, "将要提醒的记事");

            Intent contentIntent = new Intent(context, ShortcutActivity.class);
            // Well, this is not very elegant but I don't want to change more code.
            // And today is programmer's day, who cares about this?
            contentIntent.setAction(Def.Communication.SHORTCUT_ACTION_CHECK_UPCOMING);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    appWidgetId, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.iv_widget_simple, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }
}
