package com.jecelyin.editor.v2.adapter.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public abstract class SimpleAdapter extends BaseAdapter {
    public static class SimpleViewHolder {
        public View itemView;

        public SimpleViewHolder(View itemView) {
            this.itemView = itemView;
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    final public View getView(int position, View convertView, ViewGroup parent) {
        SimpleViewHolder holder;
        if(convertView == null) {
            holder = onCreateViewHolder(parent);
            convertView = holder.itemView;
            convertView.setTag(holder);
        } else {
            holder = (SimpleViewHolder) convertView.getTag();
        }
        onBindViewHolder(holder, position);
        return convertView;
    }

    public abstract SimpleViewHolder onCreateViewHolder(ViewGroup parent);
    public abstract void onBindViewHolder(SimpleViewHolder holder, int position);
}
