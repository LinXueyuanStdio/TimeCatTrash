package com.timecat.module.main.miniapp.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import android.widget.RemoteViews;

import com.timecat.component.commonsdk.utils.phone.NotificationChannelUtils;
import com.timecat.component.readonly.Constants;
import com.timecat.component.setting.DEF;
import com.timecat.module.main.R;
import com.timecat.module.main.miniapp.activities.MiniAppsActivity;
import com.timecat.module.main.miniapp.utilities.GeneralUtils;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/1/1
 * @description null
 * @usage null
 */
public class MiniAppNotification {
    public static final String TOOLBOX_NOTIFICATION_CHANNEL_ID = "com.time.cat.channel.ToolboxNotification";
    public static final int TOOLBOX_NOTIFICATION_ID = 100001;

    public static void stopNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(TOOLBOX_NOTIFICATION_ID);
        }
    }

    public static void startNotification(Context context) {
        NotificationChannelUtils.createNotificationChannel(context, TOOLBOX_NOTIFICATION_CHANNEL_ID);
        NotificationManager notificationManager = NotificationChannelUtils.getNotificationManager(context);
        Notification notification = new NotificationCompat.Builder(context, TOOLBOX_NOTIFICATION_CHANNEL_ID)
                .setChannelId(TOOLBOX_NOTIFICATION_CHANNEL_ID)
                .setOngoing(true)
                .setAutoCancel(false)
                .setSmallIcon(R.drawable.notification)
                .setTicker(null)
                .setWhen(System.currentTimeMillis())
                .build();
        RemoteViews notificationView;
        switch (DEF.config().getSettingWhiteCustomNotificationView()) {
            case Constants.SETTING_WHITE_CUSTOM_NOTIFICATION_VIEW_WHITE_TRANS:
                notificationView = new RemoteViews(context.getPackageName(), R.layout.notification_miniapps_white_trans);break;
            case Constants.SETTING_WHITE_CUSTOM_NOTIFICATION_VIEW_BLACK_TRANS:
                notificationView = new RemoteViews(context.getPackageName(), R.layout.notification_miniapps_black_trans);break;
            case Constants.SETTING_WHITE_CUSTOM_NOTIFICATION_VIEW_WHITE:
                notificationView = new RemoteViews(context.getPackageName(), R.layout.notification_miniapps_white);break;
            case Constants.SETTING_WHITE_CUSTOM_NOTIFICATION_VIEW_BLACK:
                notificationView = new RemoteViews(context.getPackageName(), R.layout.notification_miniapps_black);break;
            default:
                notificationView = new RemoteViews(context.getPackageName(), R.layout.notification_miniapps_black_trans);break;
        }
        notificationView.setImageViewResource(R.id.imageViewNotificationButton1, GeneralUtils.GetNotificationIcon(context, 0));
        notificationView.setImageViewResource(R.id.imageViewNotificationButton2, GeneralUtils.GetNotificationIcon(context, 1));
        notificationView.setImageViewResource(R.id.imageViewNotificationButton3, GeneralUtils.GetNotificationIcon(context, 2));
        notificationView.setImageViewResource(R.id.imageViewNotificationButton4, GeneralUtils.GetNotificationIcon(context, 3));
        notificationView.setImageViewResource(R.id.imageViewNotificationButton5, GeneralUtils.GetNotificationIcon(context, 4));
        notificationView.setImageViewResource(R.id.imageViewNotificationButton6, GeneralUtils.GetNotificationIcon(context, 5));
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(context, 0, new Intent(context, MiniAppsActivity.class), 0);
        notification.contentView = notificationView;
        notification.contentIntent = pendingNotificationIntent;
        notification.flags |= 32;
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, new Intent(context, Shortcut1Activity.class), 0);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, new Intent(context, Shortcut2Activity.class), 0);
        PendingIntent pendingIntent3 = PendingIntent.getActivity(context, 0, new Intent(context, Shortcut3Activity.class), 0);
        PendingIntent pendingIntent4 = PendingIntent.getActivity(context, 0, new Intent(context, Shortcut4Activity.class), 0);
        PendingIntent pendingIntent5 = PendingIntent.getActivity(context, 0, new Intent(context, Shortcut5Activity.class), 0);
        PendingIntent pendingIntent6 = PendingIntent.getActivity(context, 0, new Intent(context, Shortcut6Activity.class), 0);
        notificationView.setOnClickPendingIntent(R.id.imageViewNotificationButton1, pendingIntent1);
        notificationView.setOnClickPendingIntent(R.id.imageViewNotificationButton2, pendingIntent2);
        notificationView.setOnClickPendingIntent(R.id.imageViewNotificationButton3, pendingIntent3);
        notificationView.setOnClickPendingIntent(R.id.imageViewNotificationButton4, pendingIntent4);
        notificationView.setOnClickPendingIntent(R.id.imageViewNotificationButton5, pendingIntent5);
        notificationView.setOnClickPendingIntent(R.id.imageViewNotificationButton6, pendingIntent6);
        notificationView.setOnClickPendingIntent(R.id.imageViewNotificationButtonMore, pendingNotificationIntent);
        if (notificationManager != null) {
            notificationManager.notify(TOOLBOX_NOTIFICATION_ID, notification);
        }
    }
}
