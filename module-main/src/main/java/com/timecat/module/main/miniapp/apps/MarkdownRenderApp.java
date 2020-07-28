package com.timecat.module.main.miniapp.apps;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient;
import android.webkit.WebIconDatabase;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.timecat.component.alert.ToastUtil;
import com.timecat.component.data.model.events.RenderMarkdownEvent;
import com.timecat.module.main.R;
import com.timecat.module.main.miniapp.utilities.GeneralUtils;
import com.timecat.module.main.miniapp.utilities.SettingsUtils;
import com.timecat.plugin.window.StandOutFlags;
import com.timecat.plugin.window.StandOutWindow;
import com.timecat.plugin.window.Window;
import com.timecat.plugin.window.WindowAgreement;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MarkdownRenderApp extends StandOutWindow {
    public static int id = 18;
    private Context context;
    private int publicId;
    private View publicView;

    public String getAppName() {
        return getString(R.string.main_miniapp_Markdown_Render);
    }

    public int getAppIcon() {
        return R.drawable.ic_window_menu;
    }

    public String getTitle(int id) {
        return getString(R.string.main_miniapp_Markdown_Render);
    }

    public String getPersistentNotificationTitle(int id) {
        return getString(R.string.main_miniapp_Markdown_Render);
    }

    public String getPersistentNotificationMessage(int id) {
        return getString(R.string.main_miniapp_running);
    }

    public int getHiddenIcon() {
        return R.drawable.ic_markdown_white_24dp;
    }

    public String getHiddenNotificationTitle(int id) {
        return getString(R.string.main_miniapp_Markdown_Render);
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
        int h = SettingsUtils.GetValue(window.getContext(), getAppName() + "HEIGHT").equals("") ? 300 : Integer.parseInt(SettingsUtils.GetValue(window.getContext(), getAppName() + "HEIGHT"));
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
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.app_maps, frame, true);
        publicId = id;
        publicView = view;
        context = getApplicationContext();
        GeneralUtils.MarkdownRenderMap.put(id, new MarkdownRenderCreator());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onContentChangedEvent(RenderMarkdownEvent event) {
        if (event.content != null && !event.content.equals("")) {
            new Handler().postDelayed(() -> {
                GeneralUtils.MarkdownRenderMap.get(id).setMarkdown(event.content);
                GeneralUtils.MarkdownRenderMap.get(id).loadMarkdown(event.content);
            }, 256);
        }
    }

    @Override
    public boolean onShow(int id, Window window) {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        return super.onShow(id, window);
    }

    @Override
    public boolean onHide(int id, Window window) {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        return super.onHide(id, window);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    public class MarkdownRenderCreator {
        private ImageButton imgBtnBack;
        private ImageButton imgBtnForward;
        private ImageButton imgBtnReload;
        private ProgressBar progressBar;
        private Pattern urlPattern = Pattern.compile("^(https?|ftp|file)://(.*?)");
        private WebView webview;
        private boolean pageFinish = false;
        private String markdown = "显示有问题...";

        public MarkdownRenderCreator() {
            imgBtnBack = (ImageButton) publicView.findViewById(R.id.imageButtonBrowserBack);
            imgBtnForward = (ImageButton) publicView.findViewById(R.id.imageButtonBrowserForward);
            imgBtnReload = (ImageButton) publicView.findViewById(R.id.imageButtonBrowserReload);
            webview = (WebView) publicView.findViewById(R.id.webkit);
            progressBar = (ProgressBar) publicView.findViewById(R.id.progressBar);
            imgBtnBack.setOnClickListener(v -> back());
            imgBtnForward.setOnClickListener(v -> forward());
            imgBtnReload.setOnClickListener(v -> reload());
            configWebView();
        }

        public void back() {
            if (checkConnectivity()) {
                webview.goBack();
            }
        }

        public void forward() {
            if (checkConnectivity()) {
                webview.goForward();
            }
        }

        public void reload() {
//            webview.reload();
            loadMarkdown(markdown);
        }

        boolean checkConnectivity() {
            NetworkInfo info = ((ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (info != null && info.isConnected() && info.isAvailable()) {
                return true;
            }
            ToastUtil.i(getApplication(), "No Connection");
            return false;
        }

        public void configWebView() {
            WebIconDatabase.getInstance().open(getDir("icons", 0).getPath());
            WebSettings webSettings = webview.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setBuiltInZoomControls(true);
            webview.setVerticalScrollBarEnabled(false);
            webview.setHorizontalScrollBarEnabled(false);
            webview.setWebViewClient(new CustomWebViewClient());
            webview.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    progressBar.setProgress(progress);
                    if (progress == 100) {
                        pageFinish = true;
                    }
                }

                public void onReceivedTitle(WebView view, String title) {
                }

                public void onReceivedIcon(WebView view, Bitmap icon) {
                }

                public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
                    callback.invoke(origin, true, false);
                }
            });
            webview.requestFocus();
            if (checkConnectivity()) {
                ToastUtil.w("网络连接失败，可能显示不出图片");
            }

            webview.loadUrl("file:///android_asset/markdownRender/markdown.html");
        }

        public String getMarkdown() {
            return markdown;
        }

        public void setMarkdown(String markdown) {
            this.markdown = markdown;
        }

        public void loadMarkdown(String markdown) {
            if (pageFinish) {
                String content = markdown
                        .replace("\n", "\\n")
                        .replace("\"", "\\\"")
                        .replace("'", "\\'");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    webview.evaluateJavascript("javascript:parseMarkdown(\"" + content + "\");", null);
                } else {
                    webview.loadUrl("javascript:parseMarkdown(\"" + content + "\");");
                }
            }
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
                startActivity(intent);
                return true;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (checkConnectivity()) {
                    progressBar.setProgress(0);
                }
            }

            public void onPageFinished(WebView view, String url) {
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            }
        }
    }

}
