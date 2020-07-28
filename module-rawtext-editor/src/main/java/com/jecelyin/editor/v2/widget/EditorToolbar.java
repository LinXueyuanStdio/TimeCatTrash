package com.jecelyin.editor.v2.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.appcompat.widget.Toolbar;
import android.util.AttributeSet;

import com.timecat.component.commonsdk.utils.utils.SysUtils;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class EditorToolbar extends Toolbar {
    private Paint titlePaint;
    private CharSequence title;

    public EditorToolbar(Context context) {
        super(context);
        init();
    }

    public EditorToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditorToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        titlePaint = new Paint();
        titlePaint.setColor(Color.WHITE);
        titlePaint.setTextSize(SysUtils.dpAsPixels(getContext(), 10));
        titlePaint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    public void setTitle(int resId) {
        setTitle(getContext().getString(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(title == null)
            return;
        canvas.drawText(title, 0, title.length(), 40, getHeight() - 10, titlePaint);
    }
}
