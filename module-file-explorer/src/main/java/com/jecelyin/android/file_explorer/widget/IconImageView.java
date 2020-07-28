package com.jecelyin.android.file_explorer.widget;

import android.content.Context;
import androidx.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.Checkable;

import com.jecelyin.android.file_explorer.R;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by jecelyin on 16/7/29.
 */

public class IconImageView extends RoundedImageView implements Checkable {
    private int defaultBackgroundColor;
    private int defaultImageResource;
    private boolean checked;

    public IconImageView(Context context) {
        super(context);
    }

    public IconImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IconImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setDefaultBackgroundColor(int color) {
        super.setBackgroundColor(color);
        defaultBackgroundColor = color;
    }

    public void setDefaultImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        defaultImageResource = resId;
    }

    public void reset() {
        setBackgroundColor(defaultBackgroundColor);
        setImageResource(defaultImageResource);
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
        if (checked) {
            setBackgroundColor(getResources().getColor(R.color.item_icon_select_status));
            setImageResource(R.drawable.file_checked);
        } else {
            setBackgroundColor(defaultBackgroundColor);
            setImageResource(defaultImageResource);
        }
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        checked = !checked;
        setChecked(checked);
    }
}
