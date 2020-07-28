package com.timecat.module.main.miniapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.timecat.module.main.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter extends BaseAdapter {
    private Context mContext;
    private Calendar month;
    public GregorianCalendar pmonth;
    public GregorianCalendar pmonthmaxset;
    private GregorianCalendar selectedDate;
    private View previousView;
    public static List<String> dayString;
    String itemvalue;
    int calMaxP;
    int firstDay;
    int lastWeekDay;
    int leftDays;
    int maxP;
    int maxWeeknumber;
    int mnthlength;
    private ArrayList<String> items = new ArrayList<>();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    String curentDateString;

    public CalendarAdapter(Context c, GregorianCalendar monthCalendar) {
        dayString = new ArrayList<>();
        Locale.setDefault(Locale.US);
        this.month = monthCalendar;
        this.selectedDate = (GregorianCalendar) monthCalendar.clone();
        this.mContext = c;
        this.month.set(GregorianCalendar.DAY_OF_MONTH, 1);
        curentDateString = this.df.format(this.selectedDate.getTime());
        refreshDays();
    }

    public void setItems(ArrayList<String> items) {
        for (int i = 0; i != items.size(); i++) {
            if (((String) items.get(i)).length() == 1) {
                items.set(i, "0" + ((String) items.get(i)));
            }
        }
        this.items = items;
    }

    public int getCount() {
        return dayString.size();
    }

    public Object getItem(int position) {
        return dayString.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (convertView == null) {
            v = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.calendar_item, null);
        }
        TextView dayView = (TextView) v.findViewById(R.id.date);
        String gridvalue = ((String) dayString.get(position)).split("-")[2].replaceFirst("^0*", "");
        if (Integer.parseInt(gridvalue) > 1 && position < this.firstDay) {
            dayView.setTextColor(Color.parseColor("#888888"));
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else if (Integer.parseInt(gridvalue) >= 7 || position <= 28) {
            dayView.setTextColor(-1);
        } else {
            dayView.setTextColor(Color.parseColor("#888888"));
            dayView.setClickable(false);
            dayView.setFocusable(false);
        }
        if (((String) dayString.get(position)).equals(this.curentDateString)) {
            setSelected(v);
            this.previousView = v;
        }
        dayView.setText(gridvalue);
        String date = (String) dayString.get(position);
        if (date.length() == 1) {
            date = "0" + date;
        }
        String monthStr = "" + (this.month.get(GregorianCalendar.MONTH) + 1);
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }
        ImageView iw = (ImageView) v.findViewById(R.id.date_icon);
        if (date.length() <= 0 || this.items == null || !this.items.contains(date)) {
            iw.setVisibility(View.INVISIBLE);
        } else {
            iw.setVisibility(View.VISIBLE);
        }
        return v;
    }

    public View setSelected(View view) {
        if (this.previousView != null) {
            this.previousView = view;
            view.setBackgroundResource(R.drawable.circular_background);
        } else {
            this.previousView = view;
            view.setBackgroundResource(R.drawable.circular_background);
        }
        return view;
    }

    public void refreshDays() {
        this.items.clear();
        dayString.clear();
        Locale.setDefault(Locale.US);
        this.pmonth = (GregorianCalendar) this.month.clone();
        this.firstDay = this.month.get(GregorianCalendar.DAY_OF_WEEK);
        this.maxWeeknumber = this.month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        this.mnthlength = this.maxWeeknumber * 7;
        this.maxP = getMaxP();
        this.calMaxP = this.maxP - (this.firstDay - 1);
        this.pmonthmaxset = (GregorianCalendar) this.pmonth.clone();
        this.pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, this.calMaxP + 1);
        for (int n = 0; n < this.mnthlength; n++) {
            this.itemvalue = this.df.format(this.pmonthmaxset.getTime());
            this.pmonthmaxset.add(GregorianCalendar.DAY_OF_MONTH, 1);
            dayString.add(this.itemvalue);
        }
    }

    private int getMaxP() {
        if (this.month.get(GregorianCalendar.MONTH) == this.month.getActualMinimum(GregorianCalendar.YEAR)) {
            this.pmonth.set(this.month.get(GregorianCalendar.MONTH) - 1, this.month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            this.pmonth.set(GregorianCalendar.MONTH, this.month.get(GregorianCalendar.MONTH) - 1);
        }
        return this.pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
    }
}
