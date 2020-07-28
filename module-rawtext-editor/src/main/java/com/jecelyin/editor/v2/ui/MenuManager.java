package com.jecelyin.editor.v2.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.core.graphics.drawable.DrawableCompat;

import com.jecelyin.editor.v2.R;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class MenuManager {

    private static int toolbarIconNormalColor;
    private static int toolbarIconDisabledColor;
    private static int menuIconNormalColor;

    public MenuManager() {
    }

    @SuppressWarnings("ResourceType")
    public static void init(Context context) {
        int[] attrs = new int[] {
                R.attr.toolbarIconNormalColor,
                R.attr.toolbarIconDisabledColor,
                R.attr.menuIconNormalColor,
        };
        TypedArray a = context.obtainStyledAttributes(attrs);
        toolbarIconNormalColor = a.getColor(0, 0);
        toolbarIconDisabledColor = a.getColor(1, 0);
        menuIconNormalColor = a.getColor(2, 0);
        a.recycle();
    }

    public static Drawable makeToolbarNormalIcon(Resources res, int resId) {
        Drawable d = res.getDrawable(resId);
        return tintDrawable(d, toolbarIconNormalColor);
    }

    public static Drawable makeToolbarNormalIcon(Drawable d) {
        return tintDrawable(d, toolbarIconNormalColor);
    }

    public static Drawable makeToolbarDisabledIcon(Drawable d) {
        return tintDrawable(d, toolbarIconDisabledColor);
    }

    public static Drawable tintDrawable(Drawable d, @ColorInt int color) {
        Drawable wd = DrawableCompat.wrap(d);
        DrawableCompat.setTint(wd, color);
        return wd;
    }

    public static Drawable makeMenuNormalIcon(Resources res, int resId) {
        Drawable d = res.getDrawable(resId);
        return tintDrawable(d, menuIconNormalColor);
    }

    public static Drawable makeMenuNormalIcon(Drawable d) {
        return tintDrawable(d, menuIconNormalColor);
    }
}
