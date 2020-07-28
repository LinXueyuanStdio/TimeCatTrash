package com.jecelyin.android.file_explorer;


import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.timecat.component.data.DBHelper;

import java.util.List;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */

public class RecentPathsMenu extends PopupMenu implements PopupMenu.OnMenuItemClickListener {
    private final Context context;
    private List<String> list;
    private OnPathSelectListener onPathSelectListener;

    public interface OnPathSelectListener {
        void onSelect(String path);
    }

    public RecentPathsMenu(Context context, View anchor) {
        super(context, anchor);
        this.context = context;
        setOnMenuItemClickListener(this);
    }

    public void setOnPathSelectListener(OnPathSelectListener onPathSelectListener) {
        this.onPathSelectListener = onPathSelectListener;
    }

    @Override
    public void show() {
        list = DBHelper.getInstance(context).getRecentPathList();
        Menu menu = getMenu();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            menu.add(0, i, 0, list.get(i));
        }
        try {
            super.show();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId < list.size()) {
            if (onPathSelectListener != null) {
                onPathSelectListener.onSelect(list.get(itemId));
                dismiss();
                return true;
            }
        }
        return false;
    }
}
