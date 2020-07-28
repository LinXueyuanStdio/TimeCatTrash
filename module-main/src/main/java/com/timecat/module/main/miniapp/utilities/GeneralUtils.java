package com.timecat.module.main.miniapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.timecat.component.setting.DEF;
import com.timecat.module.main.R;
import com.timecat.module.main.miniapp.apps.BiliBiliApp.BiliBiliCreator;
import com.timecat.module.main.miniapp.apps.BiliBiliCoverApp.BiliBiliCoverCreator;
import com.timecat.module.main.miniapp.apps.BrowserApp.BrowserCreator;
import com.timecat.module.main.miniapp.apps.CalculatorApp.CalculatorCreator;
import com.timecat.module.main.miniapp.apps.CameraApp.CameraCreator;
import com.timecat.module.main.miniapp.apps.ContactsApp.ContactsCreator;
import com.timecat.module.main.miniapp.apps.DialerApp.DialerCreator;
import com.timecat.module.main.miniapp.apps.FacebookApp.FacebookCreator;
import com.timecat.module.main.miniapp.apps.FilesApp.FilesCreator;
import com.timecat.module.main.miniapp.apps.GalleryApp.GalleryCreator;
import com.timecat.module.main.miniapp.apps.GmailApp.GMailCreator;
import com.timecat.module.main.miniapp.apps.LauncherApp.LauncherCreator;
import com.timecat.module.main.miniapp.apps.MapsApp.MapsCreator;
import com.timecat.module.main.miniapp.apps.MarkdownRenderApp.MarkdownRenderCreator;
import com.timecat.module.main.miniapp.apps.NotesApp.NotesCreator;
import com.timecat.module.main.miniapp.apps.PaintApp.PaintCreator;
import com.timecat.module.main.miniapp.apps.StopwatchApp.StopwatchCreator;
import com.timecat.module.main.miniapp.apps.TwitterApp.TwitterCreator;
import com.timecat.module.main.miniapp.apps.VideoApp.VideoCreator;
import com.timecat.module.main.miniapp.apps.YoutubeApp.YouTubeCreator;
import com.timecat.plugin.window.StandOutWindow.DropDownListItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralUtils {
    public static Map<Integer, BrowserCreator> BrowserMap = new HashMap();
    public static Map<Integer, CalculatorCreator> CalculatorMap = new HashMap();
    public static CameraCreator CameraObject;
    public static Map<Integer, ContactsCreator> ContactsMap = new HashMap();
    public static Map<Integer, DialerCreator> DialerMap = new HashMap();
    public static Map<Integer, FacebookCreator> FacebookMap = new HashMap();
    public static Map<Integer, FilesCreator> FilesMap = new HashMap();
    public static Map<Integer, GMailCreator> GMailMap = new HashMap();
    public static Map<Integer, GalleryCreator> GalleryMap = new HashMap();
    public static Map<Integer, LauncherCreator> LauncherMap = new HashMap();
    public static Map<Integer, MapsCreator> MapsMap = new HashMap();
    public static Map<Integer, MarkdownRenderCreator> MarkdownRenderMap = new HashMap();
    public static Map<Integer, BiliBiliCoverCreator> BiliBiliCoverMap = new HashMap();
    public static Map<Integer, NotesCreator> NotesMap = new HashMap();
    public static Map<Integer, PaintCreator> PaintMap = new HashMap();
    public static String SharedText = "";
    public static String SharedURL = "";
    public static Map<Integer, StopwatchCreator> StopwatchMap = new HashMap();
    public static Map<Integer, TwitterCreator> TwitterMap = new HashMap();
    public static Map<Integer, VideoCreator> VideoMap = new HashMap();
    public static Map<Integer, YouTubeCreator> YouTubeMap = new HashMap();
    public static Map<Integer, BiliBiliCreator> BiliBiliMap = new HashMap();

    public static int GetDP(Context context, int pixels) {
        return (int) (((float) pixels) * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f));
    }

    public static String GetNotesFolderPath() {
        return Environment.getExternalStorageDirectory() + "/timecat/MiniApps/Notes/";
    }

    public static String GetPaintFolderPath() {
        return Environment.getExternalStorageDirectory() + "/timecat/MiniApps/Paint/";
    }

    public static String GetRecorderFolderPath() {
        return Environment.getExternalStorageDirectory() + "/timecat/MiniApps/Recorder/";
    }

    public static String GetCameraFolderPath() {
        return Environment.getExternalStorageDirectory() + "/timecat/MiniApps/Camera/";
    }

    public static PopupWindow getMenu(Context context, List<DropDownListItem> items) {
        LinearLayout scroll = new LinearLayout(context);
        scroll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout list = new LinearLayout(context);
        list.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(list);
        final PopupWindow dropDown = new PopupWindow(scroll, -2, -2, true);
        LayoutInflater inflater = LayoutInflater.from(context);
        for (final DropDownListItem item : items) {
            ViewGroup listItem = (ViewGroup) inflater.inflate(R.layout.window_menu_listitem, null);
            list.addView(listItem);
            ((ImageView) listItem.findViewById(R.id.icon)).setImageResource(item.icon);
            ((TextView) listItem.findViewById(R.id.description)).setText(item.description);
            listItem.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    item.action.run();
                    dropDown.dismiss();
                }
            });
        }
        dropDown.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shadow_right_bottom));
        return dropDown;
    }

    public static String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";
        int hours = (int) (milliseconds / 3600000);
        int minutes = ((int) (milliseconds % 3600000)) / 60000;
        int seconds = (int) (((milliseconds % 3600000) % 60000) / 1000);
        if (hours > 0) {
            finalTimerString = hours + ":";
        }
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }
        return finalTimerString + minutes + ":" + secondsString;
    }

    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = Double.valueOf(0.0d);
        return Double.valueOf((((double) ((long) ((int) (currentDuration / 1000)))) / ((double) ((long) ((int) (totalDuration / 1000))))) * 100.0d).intValue();
    }

    public static int progressToTimer(int progress, int totalDuration) {
        return ((int) ((((double) progress) / 100.0d) * ((double) (totalDuration / 1000)))) * 1000;
    }

    public static int GetNotificationIcon(Context context, int index) {
        SharedPreferences settings = DEF.floatview();
        String key = "NOTIFICATION_ICONS";
        switch (index) {
            case 0:
                return settings.getInt(key + index, R.mipmap.calculator);
            case 1:
                return settings.getInt(key + index, R.mipmap.notes);
            case 2:
                return settings.getInt(key + index, R.mipmap.paint);
            case 3:
                return settings.getInt(key + index, R.mipmap.player);
            case 4:
                return settings.getInt(key + index, R.mipmap.browser);
            case 5:
                return settings.getInt(key + index, R.mipmap.camera);
            default:
                return 0;
        }
    }

    public static void SetNotificationIcon(Context context, int index, int resourceId) {
        DEF.floatview().putInt("NOTIFICATION_ICONS" + index, resourceId);
    }

}
