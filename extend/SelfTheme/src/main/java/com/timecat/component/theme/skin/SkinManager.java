package com.timecat.component.theme.skin;

import skin.support.SkinCompatManager;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-08-13
 * @description null
 * @usage null
 */
public class SkinManager {
    public interface SkinLoaderListener extends SkinCompatManager.SkinLoaderListener {}

    public static void loadSkin(String url, SkinLoaderListener skinLoaderListener) {
        SkinCompatManager.getInstance().loadSkin(url, skinLoaderListener, SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
    }

    public static void restoreDefaultTheme() {
        SkinCompatManager.getInstance().restoreDefaultTheme();
    }
}
