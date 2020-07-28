package com.timecat.module.main.miniapp.apps;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.timecat.component.alert.ToastUtil;
import com.timecat.component.commonsdk.utils.override.LogUtil;
import com.timecat.module.main.R;
import com.timecat.module.main.miniapp.utilities.GeneralUtils;
import com.timecat.module.main.miniapp.utilities.SettingsUtils;
import com.timecat.plugin.window.StandOutFlags;
import com.timecat.plugin.window.StandOutWindow;
import com.timecat.plugin.window.Window;
import com.timecat.plugin.window.WindowAgreement;

import java.util.ArrayList;
import java.util.List;

public class DialerApp extends StandOutWindow {
    public static int id = 5;
    private Context context;
    private int publicId;
    private View publicView;

    public String getAppName() {
        return getString(R.string.main_miniapp_dialer);
    }

    public int getAppIcon() {
        return R.drawable.ic_window_menu;
    }

    public String getTitle(int id) {
        return getString(R.string.main_miniapp_dialer);
    }

    public String getPersistentNotificationTitle(int id) {
        return getString(R.string.main_miniapp_dialer);
    }

    public String getPersistentNotificationMessage(int id) {
        return getString(R.string.main_miniapp_running);
    }

    public int getHiddenIcon() {
        return R.mipmap.dialer;
    }

    public String getHiddenNotificationTitle(int id) {
        return getString(R.string.main_miniapp_dialer);
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
        items.add(new DropDownListItem(R.mipmap.menu_add_contact, getString(R.string.main_miniapp_AddContact), new Runnable() {
            public void run() {
                if (((DialerCreator) GeneralUtils.DialerMap.get(Integer.valueOf(id))).number.getText().toString().trim().equals("")) {
                    ToastUtil.i(context, "No number to add");
                    return;
                }
                Intent intent = new Intent("com.android.contacts.action.SHOW_OR_CREATE_CONTACT", Uri.parse("tel:" + ((DialerCreator) GeneralUtils.DialerMap.get(Integer.valueOf(id))).number.getText()));
                intent.putExtra("com.android.contacts.action.FORCE_CREATE", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }));
        return items;
    }

    public void createAndAttachView(int id, FrameLayout frame) {
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.app_dialer, frame, true);
        this.publicId = id;
        this.publicView = view;
        this.context = getApplicationContext();
        GeneralUtils.DialerMap.put(id, new DialerCreator());
    }

    public class DialerCreator {
        Button btn0;
        Button btn1;
        Button btn2;
        Button btn3;
        Button btn4;
        Button btn5;
        Button btn6;
        Button btn7;
        Button btn8;
        Button btn9;
        Button btnAsterisk;
        Button btnHash;
        Button btnPlus;
        TextView number;
        RelativeLayout rlBackspace;
        RelativeLayout rlDial;

        public DialerCreator() {
            this.btn1 = (Button) publicView.findViewById(R.id.button1);
            this.btn2 = (Button) publicView.findViewById(R.id.button2);
            this.btn3 = (Button) publicView.findViewById(R.id.button3);
            this.btn4 = (Button) publicView.findViewById(R.id.button4);
            this.btn5 = (Button) publicView.findViewById(R.id.button5);
            this.btn6 = (Button) publicView.findViewById(R.id.button6);
            this.btn7 = (Button) publicView.findViewById(R.id.button7);
            this.btn8 = (Button) publicView.findViewById(R.id.button8);
            this.btn9 = (Button) publicView.findViewById(R.id.button9);
            this.btn0 = (Button) publicView.findViewById(R.id.button0);
            this.btnHash = (Button) publicView.findViewById(R.id.buttonHash);
            this.btnAsterisk = (Button) publicView.findViewById(R.id.buttonAsterisk);
            this.btnPlus = (Button) publicView.findViewById(R.id.buttonPlus);
            this.number = (TextView) publicView.findViewById(R.id.textViewDialer);
            this.rlBackspace = (RelativeLayout) publicView.findViewById(R.id.buttonBackspace);
            this.rlDial = (RelativeLayout) publicView.findViewById(R.id.buttonDial);
            this.btn1.setOnClickListener(arg0 -> number.setText(number.getText().toString() + "1"));
            this.btn2.setOnClickListener(arg0 -> number.setText(number.getText().toString() + "2"));
            this.btn3.setOnClickListener(arg0 -> number.setText(number.getText().toString() + "3"));
            this.btn4.setOnClickListener(arg0 -> number.setText(number.getText().toString() + "4"));
            this.btn5.setOnClickListener(arg0 -> number.setText(number.getText().toString() + "5"));
            this.btn6.setOnClickListener(arg0 -> number.setText(number.getText().toString() + "6"));
            this.btn7.setOnClickListener(arg0 -> number.setText(number.getText().toString() + "7"));
            this.btn8.setOnClickListener(arg0 -> number.setText(number.getText().toString() + "8"));
            this.btn9.setOnClickListener(arg0 -> number.setText(number.getText().toString() + "9"));
            this.btn0.setOnClickListener(arg0 -> number.setText(number.getText().toString() + "0"));
            this.btnPlus.setOnClickListener(arg0 -> number.setText(number.getText().toString() + "+"));
            this.btnAsterisk.setOnClickListener(arg0 -> number.setText(number.getText().toString() + "*"));
            this.btnHash.setOnClickListener(arg0 -> number.setText(number.getText().toString() + "#"));
            this.rlBackspace.setOnClickListener(arg0 -> {
                String s = number.getText().toString();
                int l = s.length();
                if (l > 0) {
                    number.setText(s.subSequence(0, l - 1));
                }
            });
            this.rlBackspace.setOnLongClickListener(arg0 -> {
                number.setText("");
                return false;
            });
            this.rlDial.setOnClickListener(arg0 -> {
                try {
                    Intent dial = new Intent("android.intent.action.CALL",
                            Uri.parse("tel:" + number.getText()));
                    dial.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(dial);
                } catch (Exception e) {
                    ToastUtil.e("打不开...去[主页侧边栏 > 权限]看看有没有权限喵~");
                    LogUtil.e(e.toString());
                }
            });
        }
    }
}
