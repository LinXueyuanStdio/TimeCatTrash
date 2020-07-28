package com.timecat.module.main.miniapp.apps;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FilesApp extends StandOutWindow {
    public static int id = 7;
    Handler mHandler;
    MediaRecorder recorder;
    private Context context;
    private int publicId;
    private View publicView;

    public String getAppName() {
        return getString(R.string.main_miniapp_files);
    }

    public int getAppIcon() {
        return R.drawable.ic_window_menu;
    }

    public String getTitle(int id) {
        return getString(R.string.main_miniapp_files);
    }

    public String getPersistentNotificationTitle(int id) {
        return getString(R.string.main_miniapp_files);
    }

    public String getPersistentNotificationMessage(int id) {
        return getString(R.string.main_miniapp_running);
    }

    public int getHiddenIcon() {
        return R.mipmap.files;
    }

    public String getHiddenNotificationTitle(int id) {
        return getString(R.string.main_miniapp_files);
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
        items.add(new DropDownListItem(R.mipmap.menu_home, getString(R.string.main_miniapp_Home), new Runnable() {
            public void run() {
                ((FilesCreator) GeneralUtils.FilesMap.get(Integer.valueOf(id))).gotoRoot();
            }
        }));
        items.add(new DropDownListItem(R.mipmap.menu_undo, getString(R.string.main_miniapp_Reload), new Runnable() {
            public void run() {
                ((FilesCreator) GeneralUtils.FilesMap.get(Integer.valueOf(id))).reloadApps();
            }
        }));
        return items;
    }

    public void createAndAttachView(int id, FrameLayout frame) {
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.app_files, frame, true);
        this.publicId = id;
        this.publicView = view;
        this.context = getApplicationContext();
        GeneralUtils.FilesMap.put(Integer.valueOf(id), new FilesCreator());
    }

    public class FilesCreator {
        String currentPath;
        String defaultFolder = "mnt/";
        GetFilesAsync getFilesAsyncTask;
        ImageButton imgBtnBack;
        File startDirectory;
        TextView tvNoFiles;
        TextView tvPath;
        private ListItemAdapter listAdapter;
        private ArrayList<ListItemModel> listItems;
        private ListView listView;

        public FilesCreator() {
            this.listView = (ListView) FilesApp.this.publicView.findViewById(R.id.listView);
            this.listItems = new ArrayList<>();
            this.listAdapter = new ListItemAdapter(FilesApp.this.context, this.listItems);
            this.listView.setAdapter(this.listAdapter);
            this.startDirectory = new File(this.defaultFolder);
            this.listView.setOnItemClickListener((parent, view, position, id) -> {
                ListItemModel item = (ListItemModel) parent.getItemAtPosition(position);
                File fileObj = new File(item.getSubtitle() + item.getTitle());
                if (fileObj.isDirectory()) {
                    FilesCreator.this.tvPath.setText(FilesCreator.this.tvPath.getText() + fileObj.getName() + "/");
                    FilesCreator.this.tvPath.setSelected(true);
                    FilesCreator.this.currentPath = FilesCreator.this.tvPath.getText().toString();
                    FilesCreator.this.listItems.clear();
                    FilesCreator.this.getFilesAsyncTask = new GetFilesAsync();
                    FilesCreator.this.getFilesAsyncTask.execute(new String[0]);
                    return;
                }
                try {
                    String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(fileObj.getName()));
                    if (type == null) {
                        type = "*/*";
                    }
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setDataAndType(Uri.fromFile(fileObj), type);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    FilesApp.this.startActivity(intent);
                } catch (Exception e) {
                    ToastUtil.i(FilesApp.this.getApplicationContext(), "Unable to open file");
                }
            });
            this.tvPath = (TextView) FilesApp.this.publicView.findViewById(R.id.textViewPath);
            this.tvNoFiles = (TextView) FilesApp.this.publicView.findViewById(R.id.textViewNoFiles);
            this.tvPath.setText(this.defaultFolder);
            this.tvPath.setSelected(true);
            this.currentPath = this.defaultFolder;
            this.listItems.clear();
            this.getFilesAsyncTask = new GetFilesAsync();
            this.getFilesAsyncTask.execute(new String[0]);
            this.imgBtnBack = (ImageButton) FilesApp.this.publicView.findViewById(R.id.imageButtonBack);
            this.imgBtnBack.setOnClickListener(v -> {
                if (!FilesCreator.this.tvPath.getText().toString().toLowerCase().equals(FilesCreator.this.defaultFolder)) {
                    String prevPath = FilesCreator.this.getPreviousFolder(FilesCreator.this.tvPath.getText().toString());
                    FilesCreator.this.tvPath.setText(prevPath);
                    FilesCreator.this.tvPath.setSelected(true);
                    FilesCreator.this.currentPath = prevPath;
                    FilesCreator.this.listItems.clear();
                    FilesCreator.this.getFilesAsyncTask = new GetFilesAsync();
                    FilesCreator.this.getFilesAsyncTask.execute(new String[0]);
                }
            });
        }

        void reloadApps() {
            this.listItems.clear();
            this.getFilesAsyncTask.cancel(true);
            this.getFilesAsyncTask = new GetFilesAsync();
            this.getFilesAsyncTask.execute(new String[0]);
        }

        String getPreviousFolder(String path) {
            String prevPath = "";
            path = path.substring(0, path.length() - 2);
            return path.substring(0, path.lastIndexOf("/") + 1);
        }

        void gotoRoot() {
            this.tvPath.setText(this.defaultFolder);
            this.tvPath.setSelected(true);
            this.currentPath = this.defaultFolder;
            this.listItems.clear();
            this.getFilesAsyncTask = new GetFilesAsync();
            this.getFilesAsyncTask.execute(new String[0]);
        }

        public void getAllDocuments(File directory) {
            File[] listFile = directory.listFiles();
            if (listFile != null) {
                int i;
                File myFile;
                for (i = 0; i < listFile.length; i++) {
                    if (listFile[i].isDirectory()) {
                        myFile = listFile[i];
                        if (!myFile.isHidden()) {
                            this.listItems.add(new ListItemModel((int) R.mipmap.files, myFile.getName(), myFile.getPath().replace(myFile.getName(), "")));
                        }
                    }
                }
                for (i = 0; i < listFile.length; i++) {
                    if (!listFile[i].isDirectory()) {
                        myFile = listFile[i];
                        if (!myFile.isHidden()) {
                            this.listItems.add(new ListItemModel(R.mipmap.menu_file, myFile.getName(), myFile.getPath().replace(myFile.getName(), ""), (myFile.length() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) + " KBs"));
                        }
                    }
                }
            }
        }

        public class ComparatorNameAsc implements Comparator<ListItemModel> {
            public int compare(ListItemModel o1, ListItemModel o2) {
                return o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase());
            }
        }

        class GetFilesAsync extends AsyncTask<String, Void, String> {
            GetFilesAsync() {
            }

            protected void onPreExecute() {
                FilesCreator.this.tvNoFiles.setVisibility(View.GONE);
                FilesCreator.this.listView.setVisibility(View.GONE);
            }

            protected String doInBackground(String... params) {
                try {
                    FilesCreator.this.getAllDocuments(new File(FilesCreator.this.currentPath));
                } catch (Exception e) {
                }
                return null;
            }

            protected void onPostExecute(String result) {
                FilesCreator.this.listAdapter = new ListItemAdapter(FilesApp.this.context, FilesCreator.this.listItems);
                FilesCreator.this.listView.setAdapter(FilesCreator.this.listAdapter);
                if (FilesCreator.this.listItems.size() == 0) {
                    FilesCreator.this.tvNoFiles.setVisibility(View.VISIBLE);
                    FilesCreator.this.listView.setVisibility(View.GONE);
                    return;
                }
                FilesCreator.this.tvNoFiles.setVisibility(View.GONE);
                FilesCreator.this.listView.setVisibility(View.VISIBLE);
                Collections.sort(FilesCreator.this.listItems, new ComparatorNameAsc());
                FilesCreator.this.listAdapter.refreshItems();
            }
        }
    }
}
