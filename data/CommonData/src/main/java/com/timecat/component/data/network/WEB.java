package com.timecat.component.data.network;

import android.content.Context;

import com.timecat.component.data.network.api.BilibiliService;
import com.timecat.component.data.network.api.GiteeService;
import com.timecat.component.data.network.api.GithubService;
import com.timecat.component.data.network.api.ImageUploadService;
import com.timecat.component.data.network.api.OcrService;
import com.timecat.component.data.network.api.PicUploadService;
import com.timecat.component.data.network.api.TranslationService;
import com.timecat.component.data.network.api.WordSegmentService;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/6/30
 * @description null
 * @usage null
 */
public class WEB {

    // initialized flag
    private static boolean initialized = false;

    private static TranslationService translationService;
    private static WordSegmentService wordSegmentService;
    private static OcrService ocrService;
    private static ImageUploadService imageUploadService;
    private static PicUploadService picUploadService;
    private static BilibiliService bilibili;
    private static GithubService githubService;
    private static GiteeService giteeService;

    public synchronized static void init(Context context) {
        if (!initialized) {
            initialized = true;

            translationService = RetrofitHelper.getTranslationService();
            wordSegmentService = RetrofitHelper.getWordSegmentService();
            ocrService = RetrofitHelper.getOcrService();
            imageUploadService = RetrofitHelper.getImageUploadService();
            picUploadService = RetrofitHelper.getPicUploadService();
            bilibili = RetrofitHelper.getBilibiliService();
            githubService = RetrofitHelper.getGithubService();
            giteeService = RetrofitHelper.getGiteeService();
        }
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static TranslationService translationService() {
        return translationService;
    }

    public static WordSegmentService wordSegmentService() {
        return wordSegmentService;
    }

    public static OcrService ocrService() {
        return ocrService;
    }

    public static ImageUploadService imageUploadService() {
        return imageUploadService;
    }

    public static PicUploadService picUploadService() {
        return picUploadService;
    }

    public static BilibiliService bilibili() {
        return bilibili;
    }
    public static GithubService github() {
        return githubService;
    }
    public static GiteeService gitee() {
        return giteeService;
    }
}
