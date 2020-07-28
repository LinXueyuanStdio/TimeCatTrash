package com.jecelyin.editor.v2.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jecelyin.editor.v2.R;
import com.jecelyin.editor.v2.common.TabInfo;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class TabAdapter extends RecyclerView.Adapter {
    private TabInfo[] list;
    private View.OnClickListener onClickListener;
    private int currentTab = 0;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        TabInfo tabInfo = getItem(position);

//        if (position == currentTab) {
//            viewHolder.itemView.setBackgroundResource(R.drawable.drawer_tab_item_background);
//        } else {
//            viewHolder.itemView.setBackgroundResource(R.drawable.white_selectable_item_background);
//        }
        viewHolder.itemView.setSelected(position == currentTab);

        viewHolder.mTitleTextView.setText((tabInfo.hasChanged() ? "* " : "") + tabInfo.getTitle());
        viewHolder.mFileTextView.setText(tabInfo.getPath());

        if(onClickListener != null) {
            viewHolder.mCloseImageView.setTag(position);
            viewHolder.mCloseImageView.setOnClickListener(onClickListener);

            viewHolder.itemView.setTag(position);
            viewHolder.itemView.setOnClickListener(onClickListener);
        }
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.length;
    }

    public TabInfo getItem(int position) {
        return list[position];
    }


    public void setTabInfoList(TabInfo[] tabInfoList) {
        this.list = tabInfoList;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setCurrentTab(int index) {
        this.currentTab = index;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTextView;
        TextView mFileTextView;
        ImageView mCloseImageView;

        ViewHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
            mFileTextView = (TextView) itemView.findViewById(R.id.file_text_view);
            mCloseImageView = (ImageView) itemView.findViewById(R.id.close_image_view);
        }
    }

}
