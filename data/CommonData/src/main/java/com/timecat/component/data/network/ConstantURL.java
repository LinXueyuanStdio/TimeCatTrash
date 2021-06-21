package com.timecat.component.data.network;

import com.timecat.component.setting.DEF;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/2/20
 * @description 常量url
 * @usage null
 */
public class ConstantURL {
    public static final String SEGMENT_URL = "http://api.bosonnlp.com/";
    public static final String YOUDAO_URL = "http://fanyi.youdao.com/";
    public static final String OCR_URL = "https://api.ocr.space/";
    public static final String MICSOFT_OCR_URL = "https://api.projectoxford.ai/";
    //     public static final String IMAGE_UPLOAD_URL = "http://up.imgapi.com/";
    public static final String IMAGE_UPLOAD_URL = "https://sm.ms/";
    public static final String PIC_UPLOAD_URL = "https://yotuku.cn/";
    public static final String BASE_URL = "http://192。168.88.107:8000/";
    public static final String BASE_URL_USERS = getBaseURL() + "users/";
    public static final String BASE_URL_TASK = getBaseURL() + "tasks/";
    public static final String BASE_BILIBILI_URL = "http://app.bilibili.com/";

    public static String getBaseURL() {
        return DEF.config().getServerAddress();
    }
}
