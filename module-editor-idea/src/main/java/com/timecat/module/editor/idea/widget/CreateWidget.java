package com.timecat.module.editor.idea.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.timecat.module.editor.idea.R;
import com.timecat.module.editor.idea.RouterActivity;

/**
 * Created by ywwynm on 2016/7/27. App Widget for creating new thing
 */
public class CreateWidget extends AppWidgetProvider {

    public static final String TAG = "CreateWidget";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(
                    context.getPackageName(), R.layout.app_widget_simple);
            remoteViews.setImageViewResource(
                    R.id.iv_widget_simple, R.drawable.widget_create_content);
            remoteViews.setContentDescription(
                    R.id.iv_widget_simple, context.getString(R.string.title_create_thing));

            Intent contentIntent = RouterActivity.infoOperation(context);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    appWidgetId, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.iv_widget_simple, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }
}
