package com.jecelyin.editor.v2.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.jecelyin.editor.v2.R;
import com.jecelyin.editor.v2.common.OnVisibilityChangedListener;
import com.jecelyin.editor.v2.widget.text.EditAreaView;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class EditorView extends RelativeLayout {
    private EditAreaView editAreaView;
    private ProgressBar progressView;
    private boolean removed = false;
    private OnVisibilityChangedListener visibilityChangedListener;

    public EditorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        editAreaView = (EditAreaView) findViewById(R.id.edit_text);
        progressView = (ProgressBar) findViewById(R.id.progress_view);

    }

    public EditAreaView getEditAreaView() {
        return editAreaView;
    }

    public void setLoading(boolean loading) {
        if (loading) {
            editAreaView.setVisibility(GONE);
            progressView.setVisibility(VISIBLE);
        } else {
            editAreaView.setVisibility(VISIBLE);
            progressView.setVisibility(GONE);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removed = true;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved() {
        this.removed = true;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);

        if (visibilityChangedListener != null)
            visibilityChangedListener.onVisibilityChanged(visibility);
    }

    public void setVisibilityChangedListener(OnVisibilityChangedListener visibilityChangedListener) {
        this.visibilityChangedListener = visibilityChangedListener;
    }
}
