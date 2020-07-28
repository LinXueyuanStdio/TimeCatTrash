package com.timecat.module.main.miniapp.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.timecat.module.main.R;
import com.timecat.module.main.miniapp.models.ListItemModel;
import java.util.ArrayList;

public class ListItemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ListItemModel> listItems;

    public ListItemAdapter(Context context, ArrayList<ListItemModel> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    public int getCount() {
        return this.listItems.size();
    }

    public Object getItem(int index) {
        return this.listItems.get(index);
    }

    public long getItemId(int index) {
        return (long) index;
    }

    public void refreshItems() {
        notifyDataSetChanged();
    }

    public View getView(int index, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.layout_listitem, null);
        }
        ImageView icon = (ImageView) convertView.findViewById(R.id.imageViewListItem);
        TextView txtHeading = (TextView) convertView.findViewById(R.id.textViewTitle);
        TextView txtSubHeading = (TextView) convertView.findViewById(R.id.textViewSubtitle);
        TextView txtMetadata = (TextView) convertView.findViewById(R.id.textViewMetadata);
        if (((ListItemModel) this.listItems.get(index)).getImage() != null) {
            icon.setImageDrawable(((ListItemModel) this.listItems.get(index)).getImage());
            icon.setPadding(0, 0, 0, 0);
        } else {
            icon.setImageResource(((ListItemModel) this.listItems.get(index)).getIcon());
            icon.setBackgroundResource(R.drawable.circular_background);
        }
        txtHeading.setText(Html.fromHtml(((ListItemModel) this.listItems.get(index)).getTitle()));
        txtSubHeading.setText(Html.fromHtml(((ListItemModel) this.listItems.get(index)).getSubtitle()));
        if (((ListItemModel) this.listItems.get(index)).getSubtitle().equals("")) {
            txtSubHeading.setVisibility(View.GONE);
        } else {
            txtSubHeading.setVisibility(View.VISIBLE);
        }
        txtMetadata.setText(Html.fromHtml(((ListItemModel) this.listItems.get(index)).getMetadata()));
        if (((ListItemModel) this.listItems.get(index)).getMetadata().equals("")) {
            txtMetadata.setVisibility(View.GONE);
        } else {
            txtMetadata.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
}
