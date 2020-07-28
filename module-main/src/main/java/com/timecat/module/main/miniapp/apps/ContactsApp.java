package com.timecat.module.main.miniapp.apps;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import java.util.Iterator;
import java.util.List;

public class ContactsApp extends StandOutWindow {

    public static int id = 4;
    private Context context;
    private int publicId;
    private View publicView;

    public String getAppName() {
        return getString(R.string.main_miniapp_contacts);
    }

    public int getAppIcon() {
        return R.drawable.ic_window_menu;
    }

    public String getTitle(int id) {
        return getString(R.string.main_miniapp_contacts);
    }

    public String getPersistentNotificationTitle(int id) {
        return getString(R.string.main_miniapp_contacts);
    }

    public String getPersistentNotificationMessage(int id) {
        return getString(R.string.main_miniapp_running);
    }

    public int getHiddenIcon() {
        return R.mipmap.contacts;
    }

    public String getHiddenNotificationTitle(int id) {
        return getString(R.string.main_miniapp_contacts);
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
        int h = SettingsUtils.GetValue(window.getContext(), getAppName() + "HEIGHT").equals("") ? 200
                : Integer.parseInt(SettingsUtils.GetValue(window.getContext(), getAppName() + "HEIGHT"));
        int w = SettingsUtils.GetValue(window.getContext(), getAppName() + "WIDTH").equals("") ? 200
                : Integer.parseInt(SettingsUtils.GetValue(window.getContext(), getAppName() + "WIDTH"));
        int x = SettingsUtils.GetValue(window.getContext(), getAppName() + "XPOS").equals("")
                ? Integer.MIN_VALUE : (int) Float
                .parseFloat(SettingsUtils.GetValue(window.getContext(), getAppName() + "XPOS"));
        int y = SettingsUtils.GetValue(window.getContext(), getAppName() + "YPOS").equals("")
                ? Integer.MIN_VALUE : (int) Float
                .parseFloat(SettingsUtils.GetValue(window.getContext(), getAppName() + "YPOS"));
        if (h < GeneralUtils.GetDP(window.getContext(), 200)) {
            h = GeneralUtils.GetDP(window.getContext(), 200);
        }
        if (w < GeneralUtils.GetDP(window.getContext(), 200)) {
            w = GeneralUtils.GetDP(window.getContext(), 200);
        }
        return new StandOutLayoutParams(id, w, h, x, y, GeneralUtils.GetDP(window.getContext(), 56),
                GeneralUtils.GetDP(window.getContext(), 56));
    }

    public int getFlags(int id) {
        return (((StandOutFlags.FLAG_DECORATION_SYSTEM | StandOutFlags.FLAG_BODY_MOVE_ENABLE)
                | StandOutFlags.FLAG_WINDOW_HIDE_ENABLE) | StandOutFlags.FLAG_WINDOW_BRING_TO_FRONT_ON_TAP)
                | StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE;
    }

    public List<DropDownListItem> getDropDownItems(int id) {
        return new ArrayList<>();
    }

    public void createAndAttachView(int id, FrameLayout frame) {
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.app_contacts, frame, true);
        this.publicId = id;
        this.publicView = view;
        this.context = getApplicationContext();
        GeneralUtils.ContactsMap.put(Integer.valueOf(id), new ContactsCreator());
    }

    public class ContactsCreator {

        private EditText etSearch;
        private ArrayList<ListItemModel> filteredListItems = new ArrayList<>();
        private ListItemAdapter listAdapter;
        private ArrayList<ListItemModel> listItems = new ArrayList<>();
        private ListView listView;
        private RelativeLayout rlBack;
        private RelativeLayout rlDial;
        private RelativeLayout rlMessage;
        private TextView tvName;
        private TextView tvNoFiles;
        private TextView tvNumber;
        private ViewSwitcher viewSwitcher1;
        private ViewSwitcher viewSwitcher2;
        private ViewSwitcher viewSwitcher3;

        public ContactsCreator() {
            this.viewSwitcher1 = (ViewSwitcher) publicView.findViewById(R.id.viewSwitcher1);
            this.viewSwitcher2 = (ViewSwitcher) publicView.findViewById(R.id.viewSwitcher2);
            this.viewSwitcher3 = (ViewSwitcher) publicView.findViewById(R.id.viewSwitcher3);
            this.tvNoFiles = (TextView) publicView.findViewById(R.id.textViewNoFiles);
            this.tvName = (TextView) publicView.findViewById(R.id.textViewContactName);
            this.tvNumber = (TextView) publicView.findViewById(R.id.textViewContactNumber);
            this.listView = (ListView) publicView.findViewById(R.id.listView);
            this.listAdapter = new ListItemAdapter(context, this.filteredListItems);
            this.listView.setAdapter(this.listAdapter);
            if (getContentResolver() == null) {

            } else {
                try {
                    Cursor cursor = getContentResolver().query(Contacts.CONTENT_URI, null,
                            null, null, "display_name");
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            String contactId = cursor.getString(cursor.getColumnIndex("_id"));
                            if (Integer
                                    .parseInt(cursor.getString(cursor.getColumnIndex("has_phone_number")))
                                    > 0) {
                                Cursor pCur = getContentResolver()
                                        .query(Phone.CONTENT_URI, null, "contact_id = ?",
                                                new String[]{contactId}, "display_name");
                                if (pCur != null) {
                                    if (pCur.moveToNext()) {
                                        String contactNumber = pCur
                                                .getString(pCur.getColumnIndex("data1"));
                                        this.listItems.add(new ListItemModel((int) R.mipmap.contacts,
                                                pCur.getString(pCur.getColumnIndex("display_name")),
                                                contactNumber));
                                    }
                                    pCur.close();
                                }
                            }
                        }
                        cursor.close();
                    }
                } catch (Exception e) {
                    ToastUtil.e("需要权限喵~没有权限很难替您办事喵~");
                    e.printStackTrace();
                }
            }
            this.listAdapter.refreshItems();
            if (this.listItems.size() == 0) {
                this.listView.setVisibility(View.INVISIBLE);
                this.tvNoFiles.setVisibility(View.VISIBLE);
            } else {
                this.listView.setVisibility(View.VISIBLE);
                this.tvNoFiles.setVisibility(View.INVISIBLE);
            }
            this.listView.setOnItemClickListener((parent, view, position, id) -> {
                ListItemModel item = (ListItemModel) parent.getItemAtPosition(position);
                tvName.setText(item.getTitle());
                tvNumber.setText(item.getSubtitle());
                switchView(1);
            });
            this.rlBack = (RelativeLayout) publicView.findViewById(R.id.buttonBack);
            this.rlMessage = (RelativeLayout) publicView.findViewById(R.id.buttonMessage);
            this.rlDial = (RelativeLayout) publicView.findViewById(R.id.buttonDial);
            this.rlBack.setOnClickListener(v -> switchView(0));
            this.rlMessage.setOnClickListener(v -> {
                Intent sms = new Intent("android.intent.action.VIEW",
                        Uri.parse("sms:" + tvNumber.getText()));
                sms.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(sms);
            });
            this.rlDial.setOnClickListener(v -> {
                Intent dial = new Intent("android.intent.action.CALL",
                        Uri.parse("tel:" + tvNumber.getText()));
                dial.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(dial);
            });
            this.etSearch = (EditText) publicView.findViewById(R.id.editTextSearch);
            this.etSearch.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                    filteredListItems.clear();
                    Iterator it = listItems.iterator();
                    while (it.hasNext()) {
                        ListItemModel item = (ListItemModel) it.next();
                        if (item.getTitle().toLowerCase().trim().contains(s.toString().toLowerCase().trim())) {
                            filteredListItems.add(item);
                        }
                    }
                    listAdapter.refreshItems();
                }
            });
            this.filteredListItems.clear();
            this.filteredListItems.addAll(this.listItems);
            this.listAdapter.refreshItems();
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
    }
}
