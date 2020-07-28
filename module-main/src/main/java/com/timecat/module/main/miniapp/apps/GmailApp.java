package com.timecat.module.main.miniapp.apps;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebIconDatabase;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.timecat.component.alert.ToastUtil;
import com.timecat.module.main.R;
import com.timecat.module.main.miniapp.utilities.GeneralUtils;
import com.timecat.module.main.miniapp.utilities.SettingsUtils;
import com.timecat.plugin.window.StandOutFlags;
import com.timecat.plugin.window.StandOutWindow;
import com.timecat.plugin.window.Window;
import com.timecat.plugin.window.WindowAgreement;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class GmailApp extends StandOutWindow {
    public static int id = 8;
    private Context context;
    private int publicId;
    private View publicView;

    public String getAppName() {
        return getString(R.string.main_miniapp_GMail);
    }

    public int getAppIcon() {
        return R.drawable.ic_window_menu;
    }

    public String getTitle(int id) {
        return getString(R.string.main_miniapp_GMail);
    }

    public String getPersistentNotificationTitle(int id) {
        return getString(R.string.main_miniapp_GMail);
    }

    public String getPersistentNotificationMessage(int id) {
        return getString(R.string.main_miniapp_running);
    }

    public int getHiddenIcon() {
        return R.mipmap.gmail;
    }

    public String getHiddenNotificationTitle(int id) {
        return getString(R.string.main_miniapp_GMail);
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
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.app_gmail, frame, true);
        this.publicId = id;
        this.publicView = view;
        this.context = getApplicationContext();
        GeneralUtils.GMailMap.put(Integer.valueOf(id), new GMailCreator());
    }

    public class GMailCreator {
        private ImageButton imgBtnBack;
        private ImageButton imgBtnForward;
        private ImageButton imgBtnReload;
        private ProgressBar progressBar;
        private Pattern urlPattern = Pattern.compile("^(https?|ftp|file)://(.*?)");
        private WebView webview;

        public GMailCreator() {
            this.imgBtnBack = (ImageButton) GmailApp.this.publicView.findViewById(R.id.imageButtonBrowserBack);
            this.imgBtnForward = (ImageButton) GmailApp.this.publicView.findViewById(R.id.imageButtonBrowserForward);
            this.imgBtnReload = (ImageButton) GmailApp.this.publicView.findViewById(R.id.imageButtonBrowserReload);
            this.webview = (WebView) GmailApp.this.publicView.findViewById(R.id.webkit);
            this.progressBar = (ProgressBar) GmailApp.this.publicView.findViewById(R.id.progressBar);
            this.imgBtnBack.setOnClickListener(v -> GMailCreator.this.back());
            this.imgBtnForward.setOnClickListener(v -> GMailCreator.this.forward());
            this.imgBtnReload.setOnClickListener(v -> GMailCreator.this.reload());
            WebIconDatabase.getInstance().open(GmailApp.this.getDir("icons", 0).getPath());
            this.webview.getSettings().setJavaScriptEnabled(true);
            this.webview.getSettings().setBuiltInZoomControls(true);
            this.webview.getSettings().setPluginState(PluginState.ON);
            this.webview.getSettings().setGeolocationDatabasePath(GmailApp.this.context.getFilesDir().getPath());
            this.webview.getSettings().setGeolocationEnabled(true);
            this.webview.setWebViewClient(new CustomWebViewClient());
            this.webview.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    GMailCreator.this.progressBar.setProgress(progress);
                }

                public void onReceivedTitle(WebView view, String title) {
                }

                public void onReceivedIcon(WebView view, Bitmap icon) {
                }
            });
            this.webview.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case 0:
                    case 1:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                            break;
                        }
                        break;
                }
                return false;
            });
            this.webview.requestFocus();
            if (checkConnectivity()) {
                this.webview.loadUrl("http://www.gmail.com");
            }
        }

        public void back() {
            if (checkConnectivity()) {
                this.webview.goBack();
            }
        }

        public void forward() {
            if (checkConnectivity()) {
                this.webview.goForward();
            }
        }

        public void reload() {
            this.webview.reload();
        }

        boolean checkConnectivity() {
            NetworkInfo info = ((ConnectivityManager) GmailApp.this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (info != null && info.isConnected() && info.isAvailable()) {
                return true;
            }
            ToastUtil.i(GmailApp.this.getApplication(), "No Connection");
            return false;
        }

        class CustomWebViewClient extends WebViewClient {
            CustomWebViewClient() {
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!url.endsWith(".mp3") && !url.endsWith(".aac")) {
                    return false;
                }
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setDataAndType(Uri.parse(url), "audio/mpeg");
                GmailApp.this.startActivity(intent);
                return true;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (GMailCreator.this.checkConnectivity()) {
                    GMailCreator.this.progressBar.setProgress(0);
                }
            }

            public void onPageFinished(WebView view, String url) {
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            }
        }
    }
}
