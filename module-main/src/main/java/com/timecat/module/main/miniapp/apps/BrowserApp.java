package com.timecat.module.main.miniapp.apps;

import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebIconDatabase;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ViewSwitcher;

import com.timecat.component.alert.ToastUtil;
import com.timecat.module.main.R;
import com.timecat.module.main.miniapp.adapters.ListItemAdapter;
import com.timecat.module.main.miniapp.models.ListItemModel;
import com.timecat.module.main.miniapp.utilities.GeneralUtils;
import com.timecat.module.main.miniapp.utilities.SettingsUtils;
import com.timecat.plugin.window.StandOutFlags;
import com.timecat.plugin.window.StandOutWindow;
import com.timecat.plugin.window.Window;
import com.timecat.plugin.window.WindowAgreement;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

//TODO 抽离出BaseApp，简化配置，实现透明度的用户自定义。之后抽离为插件
public class BrowserApp extends StandOutWindow {
    public static int id = 1;
    private Context context;
    private int publicId;
    private View publicView;

    public String getAppName() {
        return getString(R.string.main_miniapp_browser);
    }

    public int getAppIcon() {
        return R.drawable.ic_window_menu;
    }

    public String getTitle(int id) {
        return getString(R.string.main_miniapp_browser);
    }

    public String getPersistentNotificationTitle(int id) {
        return getString(R.string.main_miniapp_browser);
    }

    public String getPersistentNotificationMessage(int id) {
        return getString(R.string.main_miniapp_running);
    }

    public int getHiddenIcon() {
        return R.mipmap.browser;
    }

    public String getHiddenNotificationTitle(int id) {
        return getString(R.string.main_miniapp_browser);
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

    public List<DropDownListItem> getDropDownItems(final int id) {
        List<DropDownListItem> items = new ArrayList<>();
        items.add(new DropDownListItem(R.mipmap.menu_desktop, getString(R.string.main_miniapp_Desktop), new Runnable() {
            @Override
            public void run() {
                if (SettingsUtils.GetValue(context, "DESKTOP_MODE").equals("") || SettingsUtils.GetValue(context, "DESKTOP_MODE").equals("NO")) {
                    SettingsUtils.SetValue(context, "DESKTOP_MODE", "YES");
                    ToastUtil.i(context, "Desktop Mode On - Restart browser app to reflect changes!");
                    return;
                }
                SettingsUtils.SetValue(context, "DESKTOP_MODE", "NO");
                ToastUtil.i(context, "Desktop Mode Off - Restart browser app to reflect changes!");
            }
        }));
        items.add(new DropDownListItem(R.mipmap.menu_recent_notes, getString(R.string.main_miniapp_History), new Runnable() {
            public void run() {
                ((BrowserCreator) GeneralUtils.BrowserMap.get(id)).showHistory();
            }
        }));
        items.add(new DropDownListItem(R.mipmap.menu_home, getString(R.string.main_miniapp_Home), new Runnable() {
            public void run() {
                if (SettingsUtils.GetValue(context, "BROWSER_HOME").equals("")) {
                    ((BrowserCreator) GeneralUtils.BrowserMap.get(id)).editTextUrl.setText("http://www.baidu.com");
                } else {
                    ((BrowserCreator) GeneralUtils.BrowserMap.get(id)).editTextUrl.setText(SettingsUtils.GetValue(context, "BROWSER_HOME"));
                }
                ((BrowserCreator) GeneralUtils.BrowserMap.get(id)).go();
            }
        }));
        items.add(new DropDownListItem(R.mipmap.menu_home, getString(R.string.main_miniapp_setasHome), new Runnable() {
            public void run() {
                SettingsUtils.SetValue(context, "BROWSER_HOME", ((BrowserCreator) GeneralUtils.BrowserMap.get(id)).editTextUrl.getText().toString());
            }
        }));
        items.add(new DropDownListItem(R.mipmap.menu_copy, getString(R.string.main_miniapp_Copy), new Runnable() {
            public void run() {
                if (((BrowserCreator) GeneralUtils.BrowserMap.get(id)).editTextUrl.getText().toString().length() != 0) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Note", ((BrowserCreator) GeneralUtils.BrowserMap.get(id)).editTextUrl.getText().toString());
                    ToastUtil.i(getApplicationContext(), "Copied to clipboard");
                    clipboard.setPrimaryClip(clip);
                    return;
                }
                ToastUtil.i(getApplicationContext(), "No URL to copy");
            }
        }));
        items.add(new DropDownListItem(R.mipmap.menu_share, getString(R.string.main_miniapp_Share), new Runnable() {
            public void run() {
                if (((BrowserCreator) GeneralUtils.BrowserMap.get(id)).editTextUrl.getText().toString().length() != 0) {
                    Intent sharingIntent = new Intent("android.intent.action.SEND");
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra("android.intent.extra.TEXT", ((BrowserCreator) GeneralUtils.BrowserMap.get(id)).editTextUrl.getText().toString());
                    sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(Intent.createChooser(sharingIntent, getString(R.string.main_miniapp_Share)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    return;
                }
                ToastUtil.i(getApplicationContext(), "No URL to share");
            }
        }));
        items.add(new DropDownListItem(R.mipmap.menu_delete, getString(R.string.main_miniapp_clearHistory), new Runnable() {
            public void run() {
                SettingsUtils.SetValue(context, "BROWSER_HISTORY", "");
                ((BrowserCreator) GeneralUtils.BrowserMap.get(id)).showHistory();
            }
        }));
        return items;
    }

    public void createAndAttachView(int id, FrameLayout frame) {
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.app_browser, frame, true);
        this.publicId = id;
        this.publicView = view;
        this.context = getApplicationContext();
        GeneralUtils.BrowserMap.put(id, new BrowserCreator());
    }

    public class BrowserCreator {
        private EditText editTextUrl;
        private ImageButton imgBtnBack;
        private ImageButton imgBtnBackFromLabel;
        private ImageButton imgBtnBackFromList;
        private ImageButton imgBtnForward;
        private ImageButton imgBtnGo;
        private ImageButton imgBtnReload;
        private ImageButton imgBtnStop;
        private ListItemAdapter listAdapter;
        private ArrayList<ListItemModel> listItems;
        private ListView listView;
        private ProgressBar progressBar;
        private Pattern urlPattern = Pattern.compile("^(https?|ftp|file)://(.*?)");
        private ViewSwitcher viewSwitcher1;
        private ViewSwitcher viewSwitcher2;
        private ViewSwitcher viewSwitcher3;
        private WebView webview;

        public BrowserCreator() {
            this.viewSwitcher1 = (ViewSwitcher) publicView.findViewById(R.id.viewSwitcher1);
            this.viewSwitcher2 = (ViewSwitcher) publicView.findViewById(R.id.viewSwitcher2);
            this.viewSwitcher3 = (ViewSwitcher) publicView.findViewById(R.id.viewSwitcher3);
            this.listView = (ListView) publicView.findViewById(R.id.listView);
            this.editTextUrl = (EditText) publicView.findViewById(R.id.editTextUrl);
            this.imgBtnGo = (ImageButton) publicView.findViewById(R.id.imageButtonBrowserGo);
            this.imgBtnBack = (ImageButton) publicView.findViewById(R.id.imageButtonBrowserBack);
            this.imgBtnForward = (ImageButton) publicView.findViewById(R.id.imageButtonBrowserForward);
            this.imgBtnStop = (ImageButton) publicView.findViewById(R.id.imageButtonBrowserStop);
            this.imgBtnReload = (ImageButton) publicView.findViewById(R.id.imageButtonBrowserReload);
            this.imgBtnBackFromList = (ImageButton) publicView.findViewById(R.id.imageButtonBackFromList);
            this.imgBtnBackFromLabel = (ImageButton) publicView.findViewById(R.id.imageButtonBackFromNoItems);
            this.webview = (WebView) publicView.findViewById(R.id.webkit);
            this.progressBar = (ProgressBar) publicView.findViewById(R.id.progressBar);
            this.listItems = new ArrayList<>();
            this.listAdapter = new ListItemAdapter(context, this.listItems);
            this.listView.setAdapter(this.listAdapter);
            this.listView.setOnItemClickListener((parent, view, position, id) -> {
                editTextUrl.setText(((ListItemModel) parent.getItemAtPosition(position)).getTitle());
                go();
                switchView(0);
            });
            this.imgBtnGo.setOnClickListener(v -> go());
            this.imgBtnBack.setOnClickListener(v -> back());
            this.imgBtnForward.setOnClickListener(v -> forward());
            this.imgBtnStop.setOnClickListener(v -> stop());
            this.imgBtnReload.setOnClickListener(v -> reload());
            this.imgBtnBackFromList.setOnClickListener(v -> switchView(0));
            this.imgBtnBackFromLabel.setOnClickListener(v -> switchView(0));
            WebIconDatabase.getInstance().open(getDir("icons", 0).getPath());
            WebSettings settings = webview.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setBuiltInZoomControls(true);
            settings.setPluginState(PluginState.ON);
            switchView(0);
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            settings.setAllowFileAccess(true);
            settings.setLoadWithOverviewMode(true);
            settings.setUseWideViewPort(true);
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            this.webview.setWebViewClient(new CustomWebViewClient());
            this.webview.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(progress);
                    if (progress == 100) {
                        progressBar.setVisibility(View.GONE);
                    }
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
                    default:
                        break;
                }
                return false;
            });
            this.webview.requestFocus();
            if (SettingsUtils.GetValue(context, "DESKTOP_MODE").equals("") || SettingsUtils.GetValue(context, "DESKTOP_MODE").equals("NO")) {
                this.webview.getSettings().setUserAgentString("");
            } else {
                this.webview.getSettings().setUserAgentString("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0");
            }
            this.editTextUrl.setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() != 0 || keyCode != 66) {
                    return false;
                }
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editTextUrl.getWindowToken(), 0);
                if (checkConnectivity()) {
                    if (!urlPattern.matcher(editTextUrl.getText().toString()).matches()) {
                        editTextUrl.setText("http://" + editTextUrl.getText().toString());
                    }
                    webview.loadUrl(editTextUrl.getText().toString());
                }
                return true;
            });
            String sharedURL = GeneralUtils.SharedURL;
            if (!sharedURL.trim().equals("")) {
                this.editTextUrl.setText(sharedURL);
                GeneralUtils.SharedURL = "";
                go();
            }
        }

        public void switchView(int index) {
            switch (index) {
                case 0:
                    this.viewSwitcher1.setDisplayedChild(0);
                    this.viewSwitcher2.setDisplayedChild(0);
                    return;
                case 1:
                    this.viewSwitcher1.setDisplayedChild(0);
                    this.viewSwitcher2.setDisplayedChild(1);
                    return;
                case 2:
                    this.viewSwitcher1.setDisplayedChild(1);
                    this.viewSwitcher3.setDisplayedChild(0);
                    return;
                case 3:
                    this.viewSwitcher1.setDisplayedChild(1);
                    this.viewSwitcher3.setDisplayedChild(1);
                    return;
                default:
                    return;
            }
        }

        public void refreshListItems() {
            this.listItems.clear();
            String hist = SettingsUtils.GetValue(context, "BROWSER_HISTORY");
            if (hist.contains("|")) {
                for (String s : hist.split("[|]")) {
                    this.listItems.add(new ListItemModel((int) R.mipmap.browser, s, ""));
                }
            }
            this.listAdapter.refreshItems();
        }

        void showHistory() {
            refreshListItems();
            if (this.listItems.size() == 0) {
                switchView(2);
            } else {
                switchView(1);
            }
        }

        public void go() {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.editTextUrl.getWindowToken(), 0);
            if (checkConnectivity()) {
                if (!this.urlPattern.matcher(this.editTextUrl.getText().toString()).matches()) {
                    this.editTextUrl.setText("http://" + this.editTextUrl.getText().toString());
                }
                this.webview.loadUrl(this.editTextUrl.getText().toString());
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

        public void stop() {
            this.webview.stopLoading();
        }

        public void reload() {
            this.webview.reload();
        }

        boolean checkConnectivity() {
            NetworkInfo info = ((ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (info != null && info.isConnected() && info.isAvailable()) {
                return true;
            }
            ToastUtil.i(getApplication(), "No Connection");
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
                startActivity(intent);
                return true;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (checkConnectivity()) {
                    progressBar.setProgress(0);
                    editTextUrl.setText(url);
                }
            }

            public void onPageFinished(WebView view, String url) {
                String hist = SettingsUtils.GetValue(context, "BROWSER_HISTORY");
                if (hist.contains(url + "|")) {
                    hist.replace(url + "|", "");
                }
                SettingsUtils.SetValue(context, "BROWSER_HISTORY", url + "|" + hist);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                ToastUtil.i(getApplication(), "Error");
            }
        }
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        switch (newConfig.orientation) {
//            case Configuration.ORIENTATION_LANDSCAPE:
//                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                break;
//            case Configuration.ORIENTATION_PORTRAIT:
//                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//                break;
//        }
//    }
}
