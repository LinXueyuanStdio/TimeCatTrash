package com.jecelyin.editor.v2.ui.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jecelyin.editor.v2.BaseActivity;
import com.jecelyin.editor.v2.R;
import com.timecat.component.readonly.RouterHub;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 * <p/>
 */
@Route(path = RouterHub.EDITOR_RawTextSettingsActivity)
public class RawTextSettingsActivity extends BaseActivity {

    public static void startActivity(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, RawTextSettingsActivity.class), requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting_rawtext);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
