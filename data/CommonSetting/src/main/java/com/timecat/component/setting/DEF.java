package com.timecat.component.setting;

import com.tencent.mmkv.MMKV;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/7/19
 * @description 自定义设置
 * @usage null
 */
public class DEF {

    public static Config config() {
        return Config.Companion.obj();
    }

    public static MMKV widget() {
        return MMKV.defaultMMKV();
    }

    public static MMKV temp() {
        return MMKV.mmkvWithID("temp");
    }

    /**
     * 悬浮窗设置
     * @return MMKV
     */
    public static MMKV floatview() {
        //多进程模式
        return MMKV.mmkvWithID("floatview", MMKV.MULTI_PROCESS_MODE);
    }

    public static MMKV setting() {
        return MMKV.mmkvWithID("setting");
    }

    public static MMKV skin() {
        return MMKV.mmkvWithID("skin");
    }

    public static MMKV miaoKey() {
        return MMKV.mmkvWithID("miaoKey");
    }

    public static MMKV filter() {
        return MMKV.mmkvWithID("filter");
    }

    public static MMKV moment() {
        return MMKV.mmkvWithID("moment");
    }

    /**
     * 插件设置
     * @return MMKV
     */
    public static MMKV plugin() {
        return MMKV.mmkvWithID("plugin", MMKV.MULTI_PROCESS_MODE);
    }

    public static MMKV local() {
        return MMKV.mmkvWithID("local");
    }

    public static MMKV speech() {
        return MMKV.mmkvWithID("speech");
    }

    /**
     * 块设置
     * @return MMKV
     */
    public static MMKV block() {
        return MMKV.mmkvWithID("block");
    }

    public static MMKV git() {
        return MMKV.mmkvWithID("git");
    }

    public static MMKV thing() {
        return MMKV.mmkvWithID("thing");
    }
}
