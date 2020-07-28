package com.timecat.module.main.miniapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.timecat.module.main.R;
import com.timecat.module.main.miniapp.models.GridItemModel;
import java.util.ArrayList;

public class GridItemAdapter extends BaseAdapter {
    private Context context;
    private int layoutView;
    private ArrayList<GridItemModel> listItems;

    public GridItemAdapter(ArrayList<GridItemModel> listItems, Context context, int layoutView) {
        this.listItems = listItems;
        this.context = context;
        this.layoutView = layoutView;
    }

    public int getCount() {
        return listItems.size();
    }

    public Object getItem(int index) {
        return listItems.get(index);
    }

    public long getItemId(int index) {
        return (long) index;
    }

    public View getView(int index, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(layoutView, null);
        }
        TextView title = convertView.findViewById(R.id.textViewListTitle);
        TextView separator = convertView.findViewById(R.id.textViewListSeparator);
        ((ImageView) convertView.findViewById(R.id.imageViewListIcon)).setImageResource(listItems.get(index).getIconResource());
        title.setText(listItems.get(index).getTitle());
        if (listItems.get(index).getTitle().trim().equals("")) {
            title.setVisibility(View.GONE);
            separator.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void refreshItems() {
        notifyDataSetChanged();
    }
}
