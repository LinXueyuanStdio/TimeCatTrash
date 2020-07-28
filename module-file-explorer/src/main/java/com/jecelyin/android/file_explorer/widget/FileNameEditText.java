package com.jecelyin.android.file_explorer.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */

public class FileNameEditText extends EditText {

    public FileNameEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FileNameEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setSelectAllOnFocus(true);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        if (!focused)
            return;
        String name = getText().toString();
        int endSel = name.indexOf('.');
        if (endSel < 0)
            endSel = name.length();
        if (endSel > 0) {
            requestFocus();
            setSelection(0, endSel);
        }
    }
}
