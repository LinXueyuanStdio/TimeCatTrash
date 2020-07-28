package com.jecelyin.editor.v2.io;

import android.text.TextUtils;

import com.timecat.component.commonsdk.utils.override.L;
import com.jecelyin.editor.v2.utils.CharsetDetector;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */

public class FileEncodingDetector {
    public final static String DEFAULT_ENCODING = "UTF-8";

    public static String detectEncoding(File file) {
//        CharsetDetector detector = new CharsetDetector();
//        try {
//            detector.setText(new BufferedInputStream(new FileInputStream(file)));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        CharsetMatch detect = detector.detect();
//        if(detect == null)
//            return DEFAULT_ENCODING;
//        String encoding = detect.getName();
        String encoding = null;
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            encoding = CharsetDetector.detect(bufferedInputStream);
            bufferedInputStream.close();

        } catch (Exception e) {
            L.e(e);
        }
        if(TextUtils.isEmpty(encoding)) {
            encoding = DEFAULT_ENCODING;
        }

        return encoding;
    }
}
