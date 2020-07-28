package com.jecelyin.editor.v2;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.jecelyin.styles.R;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class FullScreenActivity extends JecActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThemeList.Theme theme = Pref.getInstance(this).getThemeInfo();
        if (theme != null) {
            setTheme(theme.isDark ? R.style.DarkTheme : R.style.DefaultTheme);
        }

        if (isFullScreenMode()) {
            enabledFullScreenMode();
        }
    }

    @Override
    protected boolean isFullScreenMode() {
        return Pref.getInstance(this).isFullScreenMode();
    }

    private void enabledFullScreenMode() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
