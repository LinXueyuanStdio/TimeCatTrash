package com.timecat.component.theme.skin;

import android.content.Context;
import android.graphics.drawable.Drawable;

import skin.support.content.res.SkinCompatResources;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-08-13
 * @description null
 * @usage null
 */
public class SkinResource {
    public static int getColor(Context context, int resId) {
        return SkinCompatResources.getColor(context, resId);
    }

    public static boolean getBoolean(Context context, int resId) {
        return SkinCompatResources.getInstance().getSkinResources().getBoolean(resId);
    }

    public static Drawable getDrawable(Context context, int resId) {
        return SkinCompatResources.getDrawable(context, resId);
    }
}
