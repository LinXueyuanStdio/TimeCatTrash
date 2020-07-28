package com.jecelyin.editor.v2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jecelyin.editor.v2.adapter.adapter.SimpleAdapter;
import com.jecelyin.editor.v2.R;
import com.jecelyin.editor.v2.utils.ExtGrep;

import java.io.File;
import java.util.List;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class FindInFilesAdapter extends SimpleAdapter {
    private List<ExtGrep.Result> data;
    private List<File> files;

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_in_files_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        ExtGrep.Result item = getItem(position);
        File f = files.get(0);
        String file = item.file.getPath().replaceFirst(f.isFile() ? f.getParent() : f.getPath(), "");
        vh.mFileTextView.setText(file + ":" + item.lineNumber);
        vh.mMatchLineTextView.setText(item.line);
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public ExtGrep.Result getItem(int position) {
        return data.get(position);
    }

    public void setResults(List<ExtGrep.Result> results) {
        this.data = results;
        notifyDataSetChanged();
    }

    public void setFindFiles(List<File> filesToProcess) {
        this.files = filesToProcess;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'find_in_files_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder extends SimpleViewHolder {
        TextView mFileTextView;
        TextView mMatchLineTextView;

        ViewHolder(View view) {
            super(view);
            mFileTextView = (TextView) view.findViewById(R.id.file_text_view);
            mMatchLineTextView = (TextView) view.findViewById(R.id.match_line_text_view);
        }
    }
}
