package com.timecat.module.main.miniapp.activities;

import android.content.Intent;
import android.net.Uri;

import com.timecat.component.commonbase.base.BaseNoDisplayActivity;
import com.timecat.module.main.miniapp.apps.BrowserApp;
import com.timecat.module.main.miniapp.apps.NotesApp;
import com.timecat.module.main.miniapp.utilities.GeneralUtils;
import com.timecat.plugin.window.WindowAgreement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/4/16
 * @description null
 * @usage null
 */
public class RouterActivity extends BaseNoDisplayActivity {
    @Override
    protected void run() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        GeneralUtils.SharedText = "";
        String text;
        if (!"android.intent.action.SEND".equals(action) || type == null) {
            if (!"android.intent.action.VIEW".equals(action) || type == null) {
                if ("android.intent.action.VIEW".equals(action)
                        && intent.getScheme() != null
                        && "http".equals(intent.getScheme().toLowerCase())) {
                    text = intent.getDataString();
                    if (text != null) {
                        GeneralUtils.SharedURL = text;
                        WindowAgreement.show(this, BrowserApp.class, BrowserApp.id);
                    }
                }
            } else if (type.startsWith("text/plain")) {
                Uri fileUri = intent.getData();
                if (fileUri != null) {
                    String path = fileUri.getPath();
                    StringBuilder content = new StringBuilder();
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(path));
                        while (true) {
                            String line = br.readLine();
                            if (line == null) {
                                break;
                            }
                            content.append(line);
                            content.append('\n');
                        }
                    } catch (IOException ignored) {
                    }
                    GeneralUtils.SharedText = content.toString();
                    WindowAgreement.show(this, NotesApp.class, NotesApp.id);
                }
            }
        } else if (type.startsWith("text/plain")) {
            text = intent.getStringExtra("android.intent.extra.TEXT");
            if (text != null) {
                GeneralUtils.SharedText = text;
                WindowAgreement.show(this, NotesApp.class, NotesApp.id);
            }
        }
    }
}
