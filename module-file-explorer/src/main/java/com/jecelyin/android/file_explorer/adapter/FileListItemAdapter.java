package com.jecelyin.android.file_explorer.adapter;

import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.jecelyin.android.file_explorer.R;
import com.jecelyin.android.file_explorer.io.JecFile;
import com.jecelyin.android.file_explorer.util.MimeTypes;
import com.jecelyin.android.file_explorer.util.OnCheckedChangeListener;
import com.jecelyin.android.file_explorer.widget.IconImageView;
import com.timecat.component.commonsdk.utils.string.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.timecat.component.commonbase.listeners.OnItemClickListener;


/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class FileListItemAdapter extends RecyclerView.Adapter<FileListItemAdapter.ViewHolder> implements SectionIndexer {
    private JecFile[] data;
    private final String year;
    private final SparseIntArray checkedArray;
    private OnCheckedChangeListener onCheckedChangeListener;
    private OnItemClickListener onItemClickListener;
    private JecFile[] mOriginalValues;
    private int itemCount;
    private ArrayList<Integer> mSectionPositions;

    public FileListItemAdapter() {
//        year = String.valueOf(new Date().getYear());
        year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        checkedArray = new SparseIntArray();
    }

    public void setData(JecFile[] data) {
        this.data = data;
        itemCount = data.length;
        mOriginalValues = data.clone();
        notifyDataSetChanged();
    }

    public void filter(CharSequence filterText) {
        if (mOriginalValues == null)
            return;

        if (TextUtils.isEmpty(filterText)) {
            data = mOriginalValues;
            itemCount = mOriginalValues.length;
            notifyDataSetChanged();
            return;
        }

        data = new JecFile[mOriginalValues.length];

        filterText = filterText.toString().toLowerCase();
        int index = 0;
        for(JecFile path : mOriginalValues) {
            if (path.getName().toLowerCase().contains(filterText)) {
                data[index++] = path;
            }
        }
        itemCount = index;
        notifyDataSetChanged();
    }



    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        if (data == null) {
            return new String[0];
        }
        List<String> sections = new ArrayList<>(27);
        mSectionPositions = new ArrayList<>(27);
        for (int i = 0, size = data.length; i < size; i++) {
            String section = getSectionName(i);
            if (section != null && !sections.contains(section)) {
                sections.add(section);
                mSectionPositions.add(i);
            }
        }
        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        if (mSectionPositions == null || mSectionPositions.size() <= sectionIndex)
            return 0;
        return mSectionPositions.get(sectionIndex);
    }

    private String getSectionName(int position) {
        JecFile file = getItem(position);
        if (file == null)
            return null;
        char c = file.getName().charAt(0);

        String prefix = file.isDirectory() ? "/" : "";
        if ( (c >= '0' && c <= '9')
            || (c >= 'a' && c <= 'z')
            || (c >= 'A' && c <= 'Z')
                ) {
            return prefix + String.valueOf(c).toUpperCase();
        }

        return prefix + "#";
    }

    public JecFile getItem(int position) {
        return data[position];
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        JecFile path = data[position];

        MimeTypes mimeTypes = MimeTypes.getInstance();
        Resources res = holder.itemView.getResources();
        int color, icon;
        if (path.isDirectory()) {
            color = R.color.type_folder;
            icon = R.drawable.file_type_folder;

        } else if (mimeTypes.isImageFile(path)) {
            color = R.color.type_media;
            icon = R.drawable.file_type_image;

        } else if (mimeTypes.isVideoFile(path)) {
            color = R.color.type_media;
            icon = R.drawable.file_type_video;

        } else if (mimeTypes.isAudioFile(path)) {
            color = R.color.type_media;
            icon = R.drawable.file_type_audio;

        } else if (mimeTypes.isAPKFile(path)) {
            color = R.color.type_apk;
            icon = R.drawable.file_type_apk;

        } else if (mimeTypes.isArchive(path)) {
            color = R.color.type_archive;
            icon = R.drawable.file_type_archive;

        } else if (mimeTypes.isCodeFile(path)) {
            color = R.color.type_code;
            icon = R.drawable.file_type_code;

        } else if (mimeTypes.isTextFile(path)) {
            color = R.color.type_text;
            icon = R.drawable.file_type_text;

        } else {
            color = R.color.type_file;
            icon = TextUtils.isEmpty(path.getExtension()) ? R.drawable.file_type_file : 0;
        }

        holder.iconImageView.setDefaultImageResource(icon);
        holder.iconImageView.setDefaultBackgroundColor(res.getColor(color));

        holder.extTextView.setText(icon == 0 && color == R.color.type_file ? path.getExtension() : "");
        holder.nameTextView.setText(path.getName());
        holder.dateTextView.setText(getDate(path.lastModified()));
        holder.secondLineTextView.setText(path.isFile() ? StringUtils.formatSize(path.length()) : "");

//        FileItemModel item = new FileItemModel();
//        item.setName(path.getName());
//        item.setExt(icon == 0 && color == R.color.type_file ? path.getExtension() : "");
//        item.setDate(getDate(path.lastModified()));
//        item.setSecondLine(path.isFile() ? StringUtils.formatSize(path.length()) : "");
//        binding.setItem(item);

        holder.iconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleChecked(position, holder);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                toggleChecked(position, holder);
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedArray != null && checkedArray.size() > 0) {
                    toggleChecked(position, holder);
                    return;
                }
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(position, v);
            }
        });

        boolean isChecked = isChecked(position);
        setViewCheckedStatus(isChecked, holder);
    }

    private void setViewCheckedStatus(boolean isChecked, ViewHolder binding) {
        binding.iconImageView.setChecked(isChecked);

        if(!isChecked) {
            binding.itemView.setSelected(false);
            binding.extTextView.setVisibility(View.VISIBLE);
        } else {
            binding.itemView.setSelected(true);
            binding.extTextView.setVisibility(View.INVISIBLE);
        }
    }

    private void toggleChecked(int position, ViewHolder binding) {
        boolean isChecked = isChecked(position);
        if(isChecked) {
            checkedArray.delete(position);
        } else {
            checkedArray.put(position, 1);
        }

        setViewCheckedStatus(!isChecked, binding);

        if(onCheckedChangeListener != null) {
            onCheckedChangeListener.onCheckedChanged(getItem(position), position, !isChecked);
            onCheckedChangeListener.onCheckedChanged(checkedArray.size());
        }

    }

    public boolean isChecked(int position) {
        return checkedArray.get(position) == 1;
    }

    private String getDate(long f) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = (sdf.format(f));
        if (date.substring(0, 4).equals(year))
            date = date.substring(5);
        return date;
    }

    public void checkAll(boolean checked) {
        if (checked) {
            int count = getItemCount();
            for (int i = 0; i < count; i++) {
                checkedArray.put(i, 1);
            }
        } else {
            checkedArray.clear();
        }

        if (onCheckedChangeListener != null) {
            onCheckedChangeListener.onCheckedChanged(checkedArray.size());
        }

        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        IconImageView iconImageView;
        TextView extTextView;
        TextView nameTextView;
        TextView dateTextView;
        TextView secondLineTextView;

        ViewHolder(View itemView) {
            super(itemView);
            secondLineTextView = itemView.findViewById(R.id.secondLineTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            extTextView = (TextView) itemView.findViewById(R.id.extTextView);
            iconImageView = (IconImageView) itemView.findViewById(R.id.iconImageView);
        }
    }
}
