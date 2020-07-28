package com.timecat.module.editor.markdown;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.timecat.component.commonbase.base.rxevent.BaseEventActivity;
import com.timecat.component.readonly.RouterHub;
import com.timecat.module.editor.R;

import java.util.Locale;

import butterknife.ButterKnife;

@Route(path = RouterHub.EDITOR_MarkdownEditorActivity)
public class MarkdownEditorActivity extends BaseEventActivity {

    public static final String TO_UPDATE_NOTE = "to_update_note";
    public static final String TO_SAVE_STR = "to_save_str";
    public static final String TO_ATTACH_NOTEBOOK = "to_attach_notebook";

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_markdown);
        ButterKnife.bind(this);

        // get default shared preferences
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        // Setting language by shared preferences
        settingLanguage();

        // open file list fragment
        final Fragment fragment = new EditorFragment();
        Bundle args = getIntent().getExtras();
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void settingLanguage() {
        Resources res = getResources();
        Configuration cfg = res.getConfiguration();
        DisplayMetrics metrics = res.getDisplayMetrics();
        String value = sharedPref.getString("language", "");
        Locale locale;
        if (value.equals("auto")) {
            locale = Locale.getDefault();
        } else {
            if (value.contains("_")) {
                String[] parts = value.split("_");
                locale = new Locale(parts[0], parts[1]);
            } else {
                locale = new Locale(value);
            }
        }
        cfg.setLocale(locale);
        res.updateConfiguration(cfg, metrics);
    }
}
