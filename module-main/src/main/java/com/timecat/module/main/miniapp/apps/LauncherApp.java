package com.timecat.module.main.miniapp.apps;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

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
import java.util.Iterator;
import java.util.List;

public class LauncherApp extends StandOutWindow {
    public static int id = 9;
    private Context context;
    private int publicId;
    private View publicView;

    public String getAppName() {
        return getString(R.string.main_miniapp_Launcher);
    }

    public int getAppIcon() {
        return R.drawable.ic_window_menu;
    }

    public String getTitle(int id) {
        return getString(R.string.main_miniapp_Launcher);
    }

    public String getPersistentNotificationTitle(int id) {
        return getString(R.string.main_miniapp_Launcher);
    }

    public String getPersistentNotificationMessage(int id) {
        return getString(R.string.main_miniapp_running);
    }

    public int getHiddenIcon() {
        return R.mipmap.launcher;
    }

    public String getHiddenNotificationTitle(int id) {
        return getString(R.string.main_miniapp_Launcher);
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
        items.add(new DropDownListItem(R.mipmap.menu_undo, getString(R.string.main_miniapp_Reload), new Runnable() {
            public void run() {
                ((LauncherCreator) GeneralUtils.LauncherMap.get(Integer.valueOf(id))).reloadApps();
            }
        }));
        return items;
    }

    public void createAndAttachView(int id, FrameLayout frame) {
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.app_launcher, frame, true);
        this.publicId = id;
        this.publicView = view;
        this.context = getApplicationContext();
        GeneralUtils.LauncherMap.put(Integer.valueOf(id), new LauncherCreator());
    }

    public class LauncherCreator {
        GetAppsAsync getAppsAsync;
        private EditText etSearch;
        private ArrayList<ListItemModel> filteredListItems = new ArrayList<>();
        private ListItemAdapter listAdapter;
        private ArrayList<ListItemModel> listItems = new ArrayList<>();
        private ListView listView;
        private ProgressBar loader;
        private PackageManager manager;

        public LauncherCreator() {
            this.listView = (ListView) LauncherApp.this.publicView.findViewById(R.id.listView);
            this.loader = (ProgressBar) LauncherApp.this.publicView.findViewById(R.id.progressBarLoading);
            this.listAdapter = new ListItemAdapter(LauncherApp.this.context, this.filteredListItems);
            this.listView.setAdapter(this.listAdapter);
            this.manager = LauncherApp.this.getPackageManager();
            this.listAdapter.refreshItems();
            this.listView.setOnItemClickListener((parent, view, position, id) -> LauncherApp.this.startActivity(LauncherCreator.this.manager.getLaunchIntentForPackage(((ListItemModel) parent.getItemAtPosition(position)).getSubtitle().toString())));
            this.etSearch = (EditText) LauncherApp.this.publicView.findViewById(R.id.editTextSearch);
            this.etSearch.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                    LauncherCreator.this.filteredListItems.clear();
                    Iterator it = LauncherCreator.this.listItems.iterator();
                    while (it.hasNext()) {
                        ListItemModel item = (ListItemModel) it.next();
                        if (item.getTitle().toLowerCase().trim().contains(s.toString().toLowerCase().trim())) {
                            LauncherCreator.this.filteredListItems.add(item);
                        }
                    }
                    LauncherCreator.this.listAdapter.refreshItems();
                }
            });
            this.getAppsAsync = new GetAppsAsync();
            this.getAppsAsync.execute(new Void[0]);
        }

        void reloadApps() {
            this.getAppsAsync.cancel(true);
            this.getAppsAsync = new GetAppsAsync();
            this.getAppsAsync.execute(new Void[0]);
        }

        class GetAppsAsync extends AsyncTask<Void, Void, Void> {
            List<ResolveInfo> availableActivities;

            GetAppsAsync() {
            }

            protected Void doInBackground(Void... params) {
                Intent i = new Intent("android.intent.action.MAIN", null);
                i.addCategory("android.intent.category.LAUNCHER");
                this.availableActivities = LauncherCreator.this.manager.queryIntentActivities(i, 0);
                for (ResolveInfo ri : this.availableActivities) {
                    LauncherCreator.this.listItems.add(new ListItemModel(ri.activityInfo.loadIcon(LauncherCreator.this.manager), ri.loadLabel(LauncherCreator.this.manager).toString(), ri.activityInfo.packageName));
                }
                return null;
            }

            protected void onPreExecute() {
                LauncherCreator.this.listItems.clear();
                LauncherCreator.this.listView.setVisibility(View.INVISIBLE);
                LauncherCreator.this.loader.setVisibility(View.VISIBLE);
            }

            protected void onPostExecute(Void result) {
                LauncherCreator.this.filteredListItems.clear();
                LauncherCreator.this.filteredListItems.addAll(LauncherCreator.this.listItems);
                LauncherCreator.this.listAdapter.refreshItems();
                LauncherCreator.this.listView.setVisibility(View.VISIBLE);
                LauncherCreator.this.loader.setVisibility(View.INVISIBLE);
            }
        }
    }
}
