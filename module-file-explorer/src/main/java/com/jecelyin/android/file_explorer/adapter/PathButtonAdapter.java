package com.jecelyin.android.file_explorer.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jecelyin.android.file_explorer.R;
import com.jecelyin.android.file_explorer.io.JecFile;
import com.timecat.component.commonbase.listeners.OnItemClickListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class PathButtonAdapter extends RecyclerView.Adapter<PathButtonAdapter.ViewHolder> {
    private ArrayList<JecFile> pathList;
    private OnItemClickListener onItemClickListener;

    public JecFile getItem(int position) {
        return pathList.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.path_button_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        JecFile path = pathList.get(position);
        String name = path.getName();
        if("/".equals(name) || TextUtils.isEmpty(name))
            name = holder.textView.getContext().getString(R.string.root_path);
        holder.textView.setText(name);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null)
                    onItemClickListener.onItemClick(position, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pathList == null ? 0 : pathList.size();
    }

    public void setPath(JecFile path) {
        if(pathList == null)
            pathList = new ArrayList<>();
        else
            pathList.clear();

        for(;path != null;) {
            pathList.add(path);
            path = path.getParentFile();
        }

        Collections.reverse(pathList);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = (TextView)itemView;
        }
    }
}
