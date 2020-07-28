package com.jecelyin.editor.v2.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.jecelyin.editor.v2.R;
import com.jecelyin.editor.v2.adapter.RangeAdapter;
import com.timecat.component.commonsdk.utils.utils.SysUtils;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class CursorWidthPreference extends JecListPreference {
    public CursorWidthPreference(Context context) {
        super(context);
        init();
    }

    public CursorWidthPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CursorWidthPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CursorWidthPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        ItemAdapter adapter = new ItemAdapter(1, 6, "%d sp");
        setEntries(adapter.getItems());
        setEntryValues(adapter.getValues());
        setAdapter(adapter);
    }

    static class ItemAdapter extends RangeAdapter {

        public ItemAdapter(int min, int max, String format) {
            super(min, max, format);
        }

        @Override
        public RangeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RangeViewHolder rvh = super.onCreateViewHolder(parent, viewType);
            rvh.mViewMap.put(R.id.cursorView, rvh.itemView.findViewById(R.id.cursorView));
            return rvh;
        }

        @Override
        public void onBindViewHolder(RangeViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);

            int value = getValue(position);

            View cursorView = holder.mViewMap.get(R.id.cursorView);
            ViewGroup.LayoutParams lp = cursorView.getLayoutParams();
            if(lp == null)
                lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.width = SysUtils.dpAsPixels(cursorView.getContext(), value);
            cursorView.setLayoutParams(lp);
        }


        @Override
        protected int getLayoutResId() {
            return R.layout.pref_cursor_width_layout;
        }

        @Override
        protected int getTextResId() {
            return R.id.title;
        }
    }
}
