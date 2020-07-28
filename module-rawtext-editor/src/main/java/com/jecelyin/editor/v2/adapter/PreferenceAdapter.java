package com.jecelyin.editor.v2.adapter;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public abstract class PreferenceAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private OnItemClickListener<VH> onItemClickListener;

    public static interface OnItemClickListener<VH extends RecyclerView.ViewHolder> {
        void onItemClick(VH holder, int position);
    }

    public OnItemClickListener<VH> getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener<VH> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
