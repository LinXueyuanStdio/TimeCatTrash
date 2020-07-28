package com.timecat.module.main.miniapp.apps;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.timecat.module.main.R;
import com.timecat.module.main.miniapp.utilities.GeneralUtils;
import com.timecat.module.main.miniapp.utilities.SettingsUtils;
import com.timecat.plugin.window.StandOutFlags;
import com.timecat.plugin.window.StandOutWindow;
import com.timecat.plugin.window.Window;
import com.timecat.plugin.window.WindowAgreement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SystemApp extends StandOutWindow {
    public static int id = 14;
    private Context context;
    private int publicId;
    private View publicView;

    public String getAppName() {
        return getString(R.string.main_miniapp_System);
    }

    public int getAppIcon() {
        return R.drawable.ic_window_menu;
    }

    public String getTitle(int id) {
        return getString(R.string.main_miniapp_System);
    }

    public String getPersistentNotificationTitle(int id) {
        return getString(R.string.main_miniapp_System);
    }

    public String getPersistentNotificationMessage(int id) {
        return getString(R.string.main_miniapp_running);
    }

    public int getHiddenIcon() {
        return R.mipmap.system;
    }

    public String getHiddenNotificationTitle(int id) {
        return getString(R.string.main_miniapp_System);
    }

    public String getHiddenNotificationMessage(int id) {
        return getString(R.string.main_miniapp_mininized);
    }

    public Intent getHiddenNotificationIntent(int id) {
        return WindowAgreement.getShowIntent(this, getClass(), id);
    }

    public Animation getShowAnimation(int id) {
        if (isExistingId(id)) {
            return AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        }
        return super.getShowAnimation(id);
    }

    public Animation getHideAnimation(int id) {
        return AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
    }

    public StandOutLayoutParams getParams(int id, Window window) {
        int h = SettingsUtils.GetValue(window.getContext(), getAppName() + "HEIGHT").equals("") ? 200 : Integer.parseInt(SettingsUtils.GetValue(window.getContext(), getAppName() + "HEIGHT"));
        int w = SettingsUtils.GetValue(window.getContext(), getAppName() + "WIDTH").equals("") ? 200 : Integer.parseInt(SettingsUtils.GetValue(window.getContext(), getAppName() + "WIDTH"));
        int x = SettingsUtils.GetValue(window.getContext(), getAppName() + "XPOS").equals("") ? Integer.MIN_VALUE : (int) Float.parseFloat(SettingsUtils.GetValue(window.getContext(), getAppName() + "XPOS"));
        int y = SettingsUtils.GetValue(window.getContext(), getAppName() + "YPOS").equals("") ? Integer.MIN_VALUE : (int) Float.parseFloat(SettingsUtils.GetValue(window.getContext(), getAppName() + "YPOS"));
        if (h < GeneralUtils.GetDP(window.getContext(), 200)) {
            h = GeneralUtils.GetDP(window.getContext(), 200);
        }
        if (w < GeneralUtils.GetDP(window.getContext(), 200)) {
            w = GeneralUtils.GetDP(window.getContext(), 200);
        }
        return new StandOutLayoutParams(id, w, h, x, y, GeneralUtils.GetDP(window.getContext(), 56), GeneralUtils.GetDP(window.getContext(), 56));
    }

    public int getFlags(int id) {
        return (((StandOutFlags.FLAG_DECORATION_SYSTEM | StandOutFlags.FLAG_BODY_MOVE_ENABLE) | StandOutFlags.FLAG_WINDOW_HIDE_ENABLE) | StandOutFlags.FLAG_WINDOW_BRING_TO_FRONT_ON_TAP) | StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE;
    }

    public List<DropDownListItem> getDropDownItems(int id) {
        return new ArrayList<>();
    }

    public void createAndAttachView(int id, FrameLayout frame) {
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.app_system, frame, true);
        this.publicId = id;
        this.publicView = view;
        this.context = getApplicationContext();
        SystemCreator system = new SystemCreator();
    }

    public class SystemCreator {
        private final Runnable mRunnable = new C03241();
        Intent batteryStatus;
        String batteryStatusString;
        float bytesAvailable;
        float bytesTotal;
        float gigAvailable;
        float gigTotal;
        IntentFilter ifilter;
        float megAvailable;
        float megTotal;
        StatFs stat;
        TextView tvBattery;
        TextView tvDownload;
        TextView tvExternal;
        TextView tvInternal;
        TextView tvNetwork;
        TextView tvRam;
        TextView tvUpload;
        private Handler mHandler = new Handler();
        private float mStartRX = 0.0f;
        private float mStartTX = 0.0f;

        public SystemCreator() {
            String str;
            this.tvDownload = (TextView) publicView.findViewById(R.id.textViewDownload);
            this.tvUpload = (TextView) publicView.findViewById(R.id.textViewUpload);
            this.mStartRX = (float) TrafficStats.getTotalRxBytes();
            this.mStartTX = (float) TrafficStats.getTotalTxBytes();
            if (this.mStartRX == -1.0f || this.mStartTX == -1.0f) {
                this.tvDownload.setText("NA");
                this.tvUpload.setText("NA");
            } else {
                this.mHandler.postDelayed(this.mRunnable, 1000);
            }
            this.batteryStatusString = "";
            this.tvBattery = (TextView) publicView.findViewById(R.id.textViewBattery);
            this.ifilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
            this.batteryStatus = context.registerReceiver(null, this.ifilter);
            this.batteryStatusString = ((int) (100.0f * (((float) this.batteryStatus.getIntExtra("level", -1)) / ((float) this.batteryStatus.getIntExtra("scale", -1))))) + "%";
            int status = this.batteryStatus.getIntExtra("status", -1);
            boolean isCharging = status == 2 || status == 5;
            if (isCharging) {
                int chargePlug = this.batteryStatus.getIntExtra("plugged", -1);
                boolean usbCharge = chargePlug == 2;
                if (chargePlug == 1) {
                }
                if (usbCharge) {
                    this.batteryStatusString += " (Charging via USB)";
                } else {
                    this.batteryStatusString += " (Charging via Adapter)";
                }
            }
            this.tvBattery.setText(this.batteryStatusString);
            this.tvInternal = (TextView) publicView.findViewById(R.id.textViewInternalStorage);
            this.tvExternal = (TextView) publicView.findViewById(R.id.textViewExternalStorage);
            this.stat = new StatFs(Environment.getDataDirectory().getPath());
            this.bytesAvailable = (float) (((long) this.stat.getFreeBlocks()) * ((long) this.stat.getBlockSize()));
            this.megAvailable = this.bytesAvailable / 1048576.0f;
            this.megAvailable = roundFloat(this.megAvailable);
            this.gigAvailable = this.megAvailable / 1024.0f;
            this.gigAvailable = roundFloat(this.gigAvailable);
            this.bytesTotal = (float) (((long) this.stat.getBlockSize()) * ((long) this.stat.getBlockCount()));
            this.megTotal = this.bytesTotal / 1048576.0f;
            this.megTotal = roundFloat(this.megTotal);
            this.gigTotal = this.megTotal / 1024.0f;
            this.gigTotal = roundFloat(this.gigTotal);
            TextView textView = this.tvInternal;
            StringBuilder append = new StringBuilder().append("Free: ");
            if (this.gigAvailable > 1.0f) {
                str = this.gigAvailable + " GB";
            } else {
                str = this.megAvailable + " MB";
            }
            append = append.append(str).append(" / Total: ");
            if (this.gigTotal > 1.0f) {
                str = this.gigTotal + " GB";
            } else {
                str = this.megTotal + " MB";
            }
            textView.setText(append.append(str).toString());
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            this.bytesAvailable = (float) (((long) stat.getFreeBlocks()) * ((long) stat.getBlockSize()));
            this.megAvailable = this.bytesAvailable / 1048576.0f;
            this.megAvailable = roundFloat(this.megAvailable);
            this.gigAvailable = this.megAvailable / 1024.0f;
            this.gigAvailable = roundFloat(this.gigAvailable);
            this.bytesTotal = (float) (((long) stat.getBlockSize()) * ((long) stat.getBlockCount()));
            this.megTotal = this.bytesTotal / 1048576.0f;
            this.megTotal = roundFloat(this.megTotal);
            this.gigTotal = this.megTotal / 1024.0f;
            this.gigTotal = roundFloat(this.gigTotal);
            textView = this.tvExternal;
            append = new StringBuilder().append("Free: ");
            if (this.gigAvailable > 1.0f) {
                str = this.gigAvailable + " GB";
            } else {
                str = this.megAvailable + " MB";
            }
            append = append.append(str).append(" / Total: ");
            if (this.gigTotal > 1.0f) {
                str = this.gigTotal + " GB";
            } else {
                str = this.megTotal + " MB";
            }
            textView.setText(append.append(str).toString());
            this.tvRam = (TextView) publicView.findViewById(R.id.textViewRAM);
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            MemoryInfo memoryInfo = new MemoryInfo();
            activityManager.getMemoryInfo(memoryInfo);
            if (VERSION.SDK_INT >= 16) {
                this.tvRam.setText("Free: " + (memoryInfo.availMem / 1048576) + " MB / " + "Total: " + (memoryInfo.totalMem / 1048576) + " MB");
            } else {
                this.tvRam.setText("Free: " + (memoryInfo.availMem / 1048576) + " MB");
            }
            this.tvNetwork = (TextView) publicView.findViewById(R.id.textViewNetwork);
            if (isConnected(context)) {
                if (isConnectedMobile(context)) {
                    this.tvNetwork.setText("Mobile Data (" + getConnectionType(getNetworkInfo(context).getSubtype()) + ")");
                }
                if (isConnectedWifi(context)) {
                    this.tvNetwork.setText("WiFi");
                    return;
                }
                return;
            }
            this.tvNetwork.setText("Not connected");
        }

        public float roundFloat(float d) {
            return new BigDecimal(Float.toString(d)).setScale(2, 4).floatValue();
        }

        public NetworkInfo getNetworkInfo(Context context) {
            return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        }

        public boolean isConnected(Context context) {
            NetworkInfo info = getNetworkInfo(context);
            return info != null && info.isConnected();
        }

        public boolean isConnectedWifi(Context context) {
            NetworkInfo info = getNetworkInfo(context);
            if (info != null && info.isConnected() && info.getType() == 1) {
                return true;
            }
            return false;
        }

        public boolean isConnectedMobile(Context context) {
            NetworkInfo info = getNetworkInfo(context);
            return info != null && info.isConnected() && info.getType() == 0;
        }

        public boolean isConnectedFast(Context context) {
            NetworkInfo info = getNetworkInfo(context);
            return info != null && info.isConnected() && isConnectionFast(info.getType(), info.getSubtype());
        }

        public boolean isConnectionFast(int type, int subType) {
            if (type == 1) {
                return true;
            }
            if (type != 0) {
                return false;
            }
            switch (subType) {
                case 1:
                    return false;
                case 2:
                    return false;
                case 3:
                case 5:
                case 6:
                case 8:
                case 9:
                case 10:
                case 12:
                case 13:
                case 14:
                case 15:
                    return true;
                case 4:
                    return false;
                case 7:
                    return false;
                case 11:
                    return false;
                default:
                    return false;
            }
        }

        public String getConnectionType(int subType) {
            switch (subType) {
                case 1:
                    return "GPRS";
                case 2:
                    return "EDGE";
                case 3:
                    return "UMTS";
                case 4:
                    return "CDMA";
                case 5:
                    return "EVDO_0";
                case 6:
                    return "EVDO_A";
                case 7:
                    return "1xRTT";
                case 8:
                    return "HSDPA";
                case 9:
                    return "HSUPA";
                case 10:
                    return "HSPA";
                case 11:
                    return "IDEN";
                case 12:
                    return "EVDO_B";
                case 13:
                    return "LTE";
                case 14:
                    return "EHRPD";
                case 15:
                    return "HSPAP";
                default:
                    return "Unknown";
            }
        }

        class C03241 implements Runnable {
            C03241() {
            }

            public void run() {
                CharSequence charSequence;
                float rxBytes = ((float) TrafficStats.getTotalRxBytes()) - SystemCreator.this.mStartRX;
                SystemCreator.this.tvDownload.setText(rxBytes < 1024.0f ? Float.toString(SystemCreator.this.roundFloat(rxBytes)) + " B/s" : Float.toString(SystemCreator.this.roundFloat(rxBytes / 1024.0f)) + " KB/s");
                float txBytes = ((float) TrafficStats.getTotalTxBytes()) - SystemCreator.this.mStartTX;
                TextView textView = SystemCreator.this.tvUpload;
                if (txBytes < 1024.0f) {
                    charSequence = Float.toString(SystemCreator.this.roundFloat(txBytes)) + " B/s";
                } else {
                    charSequence = Float.toString(SystemCreator.this.roundFloat(txBytes / 1024.0f)) + " KB/s";
                }
                textView.setText(charSequence);
                SystemCreator.this.mHandler.postDelayed(SystemCreator.this.mRunnable, 1000);
                SystemCreator.this.mStartRX = (float) TrafficStats.getTotalRxBytes();
                SystemCreator.this.mStartTX = (float) TrafficStats.getTotalTxBytes();
                SystemCreator.this.batteryStatusString = "";
                SystemCreator.this.tvBattery = (TextView) publicView.findViewById(R.id.textViewBattery);
                SystemCreator.this.ifilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
                SystemCreator.this.batteryStatus = context.registerReceiver(null, SystemCreator.this.ifilter);
                SystemCreator.this.batteryStatusString = ((int) (100.0f * (((float) SystemCreator.this.batteryStatus.getIntExtra("level", -1)) / ((float) SystemCreator.this.batteryStatus.getIntExtra("scale", -1))))) + "%";
                int status = SystemCreator.this.batteryStatus.getIntExtra("status", -1);
                boolean isCharging = status == 2 || status == 5;
                if (isCharging) {
                    int chargePlug = SystemCreator.this.batteryStatus.getIntExtra("plugged", -1);
                    boolean usbCharge = chargePlug == 2;
                    if (chargePlug == 1) {
                    }
                    StringBuilder stringBuilder;
                    SystemCreator systemCreator;
                    if (usbCharge) {
                        stringBuilder = new StringBuilder();
                        systemCreator = SystemCreator.this;
                        systemCreator.batteryStatusString = stringBuilder.append(systemCreator.batteryStatusString).append(" (Charging via USB)").toString();
                    } else {
                        stringBuilder = new StringBuilder();
                        systemCreator = SystemCreator.this;
                        systemCreator.batteryStatusString = stringBuilder.append(systemCreator.batteryStatusString).append(" (Charging via Adapter)").toString();
                    }
                }
                SystemCreator.this.tvBattery.setText(SystemCreator.this.batteryStatusString);
                if (SystemCreator.this.isConnected(context)) {
                    if (SystemCreator.this.isConnectedMobile(context)) {
                        SystemCreator.this.tvNetwork.setText("Mobile Data (" + SystemCreator.this.getConnectionType(SystemCreator.this.getNetworkInfo(context).getSubtype()) + ")");
                    }
                    if (SystemCreator.this.isConnectedWifi(context)) {
                        SystemCreator.this.tvNetwork.setText("WiFi");
                        return;
                    }
                    return;
                }
                SystemCreator.this.tvNetwork.setText("Not connected");
            }
        }
    }
}
