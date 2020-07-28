package com.timecat.module.main.miniapp.apps;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.timecat.component.alert.ToastUtil;
import com.timecat.module.main.R;
import com.timecat.module.main.miniapp.adapters.CalendarAdapter;
import com.timecat.module.main.miniapp.utilities.GeneralUtils;
import com.timecat.module.main.miniapp.utilities.SettingsUtils;
import com.timecat.plugin.window.StandOutFlags;
import com.timecat.plugin.window.StandOutWindow;
import com.timecat.plugin.window.Window;
import com.timecat.plugin.window.WindowAgreement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class CalenderApp extends StandOutWindow {
    public static int id = 3;
    private Context context;
    private int publicId;
    private View publicView;

    public String getAppName() {
        return getString(R.string.main_miniapp_calender);
    }

    public int getAppIcon() {
        return R.drawable.ic_window_menu;
    }

    public String getTitle(int id) {
        return getString(R.string.main_miniapp_calender);
    }

    public String getPersistentNotificationTitle(int id) {
        return getString(R.string.main_miniapp_calender);
    }

    public String getPersistentNotificationMessage(int id) {
        return getString(R.string.main_miniapp_running);
    }

    public int getHiddenIcon() {
        return R.mipmap.calender;
    }

    public String getHiddenNotificationTitle(int id) {
        return getString(R.string.main_miniapp_calender);
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
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.app_calender, frame, true);
        this.publicId = id;
        this.publicView = view;
        this.context = getApplicationContext();
        CalenderCreator calender = new CalenderCreator();
    }

    public class CalenderCreator {
        public CalendarAdapter adapter;
        public Handler handler;
        public ArrayList<String> items = new ArrayList<>();
        public GregorianCalendar month;
        public GregorianCalendar itemmonth;
        public Runnable calendarUpdater;
        TextView tvDate;

        public CalenderCreator() {
            month = ((GregorianCalendar) GregorianCalendar.getInstance());
            itemmonth = ((GregorianCalendar) this.month.clone());
            calendarUpdater = () -> {
                items.clear();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                for (int i = 0; i < 7; i++) {
                    String itemvalue = df.format(itemmonth.getTime());
                    itemmonth.add(GregorianCalendar.DAY_OF_MONTH, 1);
                    items.add("2012-09-12");
                    items.add("2012-10-07");
                    items.add("2012-10-15");
                    items.add("2012-10-20");
                    items.add("2012-11-30");
                    items.add("2012-11-28");
                }
                adapter.setItems(items);
                adapter.notifyDataSetChanged();
            };
            this.adapter = new CalendarAdapter(context, this.month);
            GridView gridview = (GridView) publicView.findViewById(R.id.gridview);
            gridview.setAdapter(this.adapter);
            this.handler = new Handler();
            this.handler.post(this.calendarUpdater);
            ((TextView) publicView.findViewById(R.id.title)).setText(android.text.format.DateFormat.format("MMMM yyyy", this.month));
            this.tvDate = (TextView) publicView.findViewById(R.id.textViewCurrentDate);
            this.tvDate.setText(new SimpleDateFormat("MMM dd, yyyy").format(Calendar.getInstance().getTime()).toUpperCase());
            ((RelativeLayout) publicView.findViewById(R.id.previous)).setOnClickListener(v -> {
                setPreviousMonth();
                refreshCalendar();
            });
            ((RelativeLayout) publicView.findViewById(R.id.next)).setOnClickListener(v -> {
                setNextMonth();
                refreshCalendar();
            });
            gridview.setOnItemClickListener((adapterView, v, position, id) -> {
                int gridvalue = Integer.parseInt(((String) CalendarAdapter.dayString.get(position)).split("-")[2].replaceFirst("^0*", ""));
                if (gridvalue > 10 && position < 8) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if (gridvalue < 7 && position > 28) {
                    setNextMonth();
                    refreshCalendar();
                }
            });
        }

        protected void setNextMonth() {
            if (this.month.get(GregorianCalendar.MONTH) == this.month.getActualMaximum(GregorianCalendar.MONTH)) {
                this.month.set(this.month.get(GregorianCalendar.YEAR) + 1, this.month.getActualMinimum(GregorianCalendar.MONTH), 1);
            } else {
                this.month.set(GregorianCalendar.MONTH, this.month.get(GregorianCalendar.MONTH) + 1);
            }
        }

        protected void setPreviousMonth() {
            if (this.month.get(GregorianCalendar.MONTH) == this.month.getActualMinimum(GregorianCalendar.MONTH)) {
                this.month.set(this.month.get(GregorianCalendar.YEAR) - 1, this.month.getActualMaximum(GregorianCalendar.MONTH), 1);
            } else {
                this.month.set(GregorianCalendar.MONTH, this.month.get(GregorianCalendar.MONTH) - 1);
            }
        }

        protected void showToast(String string) {
            ToastUtil.i(context, string);
        }

        public void refreshCalendar() {
            TextView title = (TextView) publicView.findViewById(R.id.title);
            this.adapter.refreshDays();
            this.adapter.notifyDataSetChanged();
            this.handler.post(this.calendarUpdater);
            title.setText(android.text.format.DateFormat.format("MMMM yyyy", this.month));
        }
    }
}
