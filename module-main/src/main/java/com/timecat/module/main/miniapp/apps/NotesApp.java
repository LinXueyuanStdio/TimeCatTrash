package com.timecat.module.main.miniapp.apps;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class NotesApp extends StandOutWindow {
    public static int id = 11;
    private Context context;
    private int publicId;
    private View publicView;

    public String getAppName() {
        return getString(R.string.main_miniapp_Notes);
    }

    public int getAppIcon() {
        return R.drawable.ic_window_menu;
    }

    public String getTitle(int id) {
        return getString(R.string.main_miniapp_Notes);
    }

    public String getPersistentNotificationTitle(int id) {
        return getString(R.string.main_miniapp_Notes);
    }

    public String getPersistentNotificationMessage(int id) {
        return getString(R.string.main_miniapp_running);
    }

    public int getHiddenIcon() {
        return R.mipmap.notes;
    }

    public String getHiddenNotificationTitle(int id) {
        return getString(R.string.main_miniapp_Notes);
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
        items.add(new DropDownListItem(R.mipmap.menu_open, getString(R.string.main_miniapp_Open), new Runnable() {

            public void run() {
                List<DropDownListItem> items = new ArrayList<>();
                items.add(new DropDownListItem(R.mipmap.menu_saved_notes, "Saved", new C02931()));
                items.add(new DropDownListItem(R.mipmap.menu_recent_notes, "Recent", new C02942()));
                GeneralUtils.getMenu(NotesApp.this.context, items).showAsDropDown(((NotesCreator) GeneralUtils.NotesMap.get(Integer.valueOf(id))).popupAnchor);
            }

            class C02931 implements Runnable {
                C02931() {
                }

                public void run() {
                    ((NotesCreator) GeneralUtils.NotesMap.get(Integer.valueOf(id))).refreshListItems();
                }
            }

            class C02942 implements Runnable {
                C02942() {
                }

                public void run() {
                    String recent = SettingsUtils.GetValue(NotesApp.this.context, "RECENT_NOTES");
                    if (recent.equals("")) {
                        ToastUtil.i(NotesApp.this.context, "No recent notes");
                        return;
                    }
                    ((NotesCreator) GeneralUtils.NotesMap.get(Integer.valueOf(id))).editTextNotes.setText(recent);
                    ((NotesCreator) GeneralUtils.NotesMap.get(Integer.valueOf(id))).editTextNotes.setSelection(((NotesCreator) GeneralUtils.NotesMap.get(Integer.valueOf(id))).editTextNotes.length());
                }
            }
        }));
        items.add(new DropDownListItem(R.mipmap.menu_copy, getString(R.string.main_miniapp_Copy), new Runnable() {
            public void run() {
                if (((NotesCreator) GeneralUtils.NotesMap.get(Integer.valueOf(id))).editTextNotes.getText().toString().length() != 0) {
                    ClipboardManager clipboard = (ClipboardManager) NotesApp.this.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Note", ((NotesCreator) GeneralUtils.NotesMap.get(Integer.valueOf(id))).editTextNotes.getText().toString());
                    ToastUtil.i(NotesApp.this.getApplicationContext(), "Copied to clipboard");
                    clipboard.setPrimaryClip(clip);
                    return;
                }
                ToastUtil.i(NotesApp.this.getApplicationContext(), "No content to copy");
            }
        }));
        items.add(new DropDownListItem(R.mipmap.menu_paste, getString(R.string.main_miniapp_Paste), new Runnable() {
            public void run() {
                ClipboardManager clipboard = (ClipboardManager) NotesApp.this.getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard.hasPrimaryClip()) {
                    ((NotesCreator) GeneralUtils.NotesMap.get(Integer.valueOf(id))).editTextNotes.getText().insert(((NotesCreator) GeneralUtils.NotesMap.get(Integer.valueOf(id))).editTextNotes.getSelectionStart(), clipboard.getPrimaryClip().getItemAt(0).getText().toString());
                } else {
                    ToastUtil.i(NotesApp.this.getApplicationContext(), "No content to paste");
                }
                ((NotesCreator) GeneralUtils.NotesMap.get(Integer.valueOf(id))).switchView(0);
            }
        }));
        items.add(new DropDownListItem(R.mipmap.menu_share, getString(R.string.main_miniapp_Share), new Runnable() {
            public void run() {
                if (((NotesCreator) GeneralUtils.NotesMap.get(Integer.valueOf(id))).editTextNotes.getText().toString().length() != 0) {
                    Intent sharingIntent = new Intent("android.intent.action.SEND");
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra("android.intent.extra.TEXT", ((NotesCreator) GeneralUtils.NotesMap.get(Integer.valueOf(id))).editTextNotes.getText().toString());
                    sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    NotesApp.this.startActivity(Intent.createChooser(sharingIntent, getString(R.string.main_miniapp_Share)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    return;
                }
                ToastUtil.i(NotesApp.this.getApplicationContext(), "No content to share");
            }
        }));
        items.add(new DropDownListItem(R.mipmap.menu_save, getString(R.string.main_miniapp_Save), new Runnable() {
            public void run() {
                ((NotesCreator) GeneralUtils.NotesMap.get(Integer.valueOf(id))).switchView(1);
            }
        }));
        return items;
    }

    public void createAndAttachView(int id, FrameLayout frame) {
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.app_notes, frame, true);
        this.publicId = id;
        this.publicView = view;
        this.context = getApplicationContext();
        GeneralUtils.NotesMap.put(Integer.valueOf(id), new NotesCreator());
    }

    public class NotesCreator {
        private EditText editTextFilename;
        private EditText editTextNotes;
        private ImageButton imgBtnBackFromLabel;
        private ImageButton imgBtnBackFromList;
        private ImageButton imgBtnCancel;
        private ImageButton imgBtnSave;
        private ListItemAdapter listAdapter;
        private ArrayList<ListItemModel> listItems = new ArrayList<>();
        private ListView listView;
        private RelativeLayout popupAnchor;
        private ViewSwitcher viewSwitcher1;
        private ViewSwitcher viewSwitcher2;
        private ViewSwitcher viewSwitcher3;

        public NotesCreator() {
            this.popupAnchor = (RelativeLayout) NotesApp.this.publicView.findViewById(R.id.layoutPopupAnchor);
            this.viewSwitcher1 = (ViewSwitcher) NotesApp.this.publicView.findViewById(R.id.viewSwitcher1);
            this.viewSwitcher2 = (ViewSwitcher) NotesApp.this.publicView.findViewById(R.id.viewSwitcher2);
            this.viewSwitcher3 = (ViewSwitcher) NotesApp.this.publicView.findViewById(R.id.viewSwitcher3);
            this.listView = (ListView) NotesApp.this.publicView.findViewById(R.id.listView);
            this.editTextNotes = (EditText) NotesApp.this.publicView.findViewById(R.id.editTextNote);
            this.editTextFilename = (EditText) NotesApp.this.publicView.findViewById(R.id.editTextFilename);
            this.imgBtnSave = (ImageButton) NotesApp.this.publicView.findViewById(R.id.imageButtonSave);
            this.imgBtnCancel = (ImageButton) NotesApp.this.publicView.findViewById(R.id.imageButtonCancel);
            this.imgBtnBackFromList = (ImageButton) NotesApp.this.publicView.findViewById(R.id.imageButtonBackFromList);
            this.imgBtnBackFromLabel = (ImageButton) NotesApp.this.publicView.findViewById(R.id.imageButtonBackFromNoFiles);
            new File(GeneralUtils.GetNotesFolderPath()).mkdirs();
            this.listAdapter = new ListItemAdapter(NotesApp.this.context, this.listItems);
            this.listView.setAdapter(this.listAdapter);
            this.listView.setOnItemClickListener((parent, view, position, id) -> {
                ListItemModel item = (ListItemModel) parent.getItemAtPosition(position);
                File file = new File(GeneralUtils.GetNotesFolderPath(), item.getTitle());
                StringBuilder content = new StringBuilder();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    while (true) {
                        String line = br.readLine();
                        if (line != null) {
                            content.append(line);
                            content.append('\n');
                        } else {
                            NotesCreator.this.editTextNotes.setText(content.toString());
                            NotesCreator.this.editTextNotes.setSelection(NotesCreator.this.editTextNotes.length());
                            NotesCreator.this.editTextFilename.setText(item.getTitle());
                            NotesCreator.this.editTextFilename.setSelection(NotesCreator.this.editTextFilename.length());
                            NotesCreator.this.switchView(0);
                            return;
                        }
                    }
                } catch (IOException e) {
                    ToastUtil.i(NotesApp.this.getApplicationContext(), "Unable to read file");
                }
            });
            this.listView.setOnItemLongClickListener((parent, view, position, id) -> {
                final ListItemModel item = (ListItemModel) parent.getItemAtPosition(position);
                List<DropDownListItem> items = new ArrayList<>();
                items.add(new DropDownListItem(R.mipmap.menu_delete, "Delete", new Runnable() {
                    public void run() {
                        if (new File(GeneralUtils.GetNotesFolderPath() + item.getTitle()).delete()) {
                            ToastUtil.i(NotesApp.this.getApplicationContext(), "Note deleted successfully");
                            NotesCreator.this.listItems.clear();
                            for (File f : new File(GeneralUtils.GetNotesFolderPath()).listFiles()) {
                                if (!f.isDirectory() && f.getName().toLowerCase().endsWith(".txt")) {
                                    NotesCreator.this.listItems.add(new ListItemModel((int) R.mipmap.notes, f.getName(), new DecimalFormat("0.00").format(((double) f.length()) / 1024.0d) + " KB"));
                                }
                            }
                            NotesCreator.this.listAdapter.refreshItems();
                            if (NotesCreator.this.listItems.size() == 0) {
                                NotesCreator.this.switchView(3);
                            } else {
                                NotesCreator.this.switchView(2);
                            }
                        }
                    }
                }));
                items.add(new DropDownListItem(R.mipmap.menu_share, getString(R.string.main_miniapp_Share), new Runnable() {
                    public void run() {
                        File file = new File(GeneralUtils.GetNotesFolderPath() + item.getTitle());
                        StringBuilder content = new StringBuilder();
                        try {
                            BufferedReader br = new BufferedReader(new FileReader(file));
                            while (true) {
                                String line = br.readLine();
                                if (line == null) {
                                    break;
                                }
                                content.append(line);
                                content.append('\n');
                            }
                        } catch (IOException e) {
                        }
                        Intent sharingIntent = new Intent("android.intent.action.SEND");
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra("android.intent.extra.TEXT", content.toString());
                        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        NotesApp.this.startActivity(Intent.createChooser(sharingIntent, getString(R.string.main_miniapp_Share)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                }));
                GeneralUtils.getMenu(NotesApp.this.context, items).showAsDropDown(view);
                return true;
            });
            this.editTextNotes.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                    SettingsUtils.SetValue(NotesApp.this.context, "RECENT_NOTES", s.toString());
                }
            });
            this.imgBtnSave.setOnClickListener(v -> {
                if (NotesCreator.this.editTextFilename.getText().toString().trim().equals("")) {
                    ToastUtil.i(NotesApp.this.getApplicationContext(), "Please enter file name");
                    return;
                }
                try {
                    String outFile = GeneralUtils.GetNotesFolderPath() + NotesCreator.this.editTextFilename.getText().toString();
                    if (!outFile.toLowerCase().endsWith(".txt")) {
                        outFile = outFile + ".txt";
                    }
                    FileWriter writer = new FileWriter(outFile);
                    writer.append(NotesCreator.this.editTextNotes.getText().toString());
                    writer.flush();
                    writer.close();
                    ToastUtil.i(NotesApp.this.getApplicationContext(), "Note saved successfully");
                    NotesCreator.this.switchView(0);
                    NotesCreator.this.editTextFilename.setText("");
                } catch (IOException e) {
                    ToastUtil.i(NotesApp.this.getApplicationContext(), "Unable to save note");
                }
            });
            this.imgBtnCancel.setOnClickListener(v -> NotesCreator.this.switchView(0));
            this.imgBtnBackFromList.setOnClickListener(v -> NotesCreator.this.switchView(0));
            this.imgBtnBackFromLabel.setOnClickListener(v -> NotesCreator.this.switchView(0));
            String sharedText = GeneralUtils.SharedText;
            if (!sharedText.trim().equals("")) {
                this.editTextNotes.setText(sharedText);
                this.editTextNotes.setSelection(this.editTextNotes.getText().length());
                GeneralUtils.SharedText = "";
            }
            switchView(0);
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
            for (File f : new File(GeneralUtils.GetNotesFolderPath()).listFiles()) {
                if (!f.isDirectory() && f.getName().toLowerCase().endsWith(".txt")) {
                    this.listItems.add(new ListItemModel((int) R.mipmap.notes, f.getName(), new DecimalFormat("0.00").format(((double) f.length()) / 1024.0d) + " KB"));
                }
            }
            this.listAdapter.refreshItems();
            if (this.listItems.size() == 0) {
                switchView(3);
            } else {
                switchView(2);
            }
        }
    }
}
