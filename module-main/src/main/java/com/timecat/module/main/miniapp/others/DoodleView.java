package com.timecat.module.main.miniapp.others;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.timecat.module.main.miniapp.utilities.GeneralUtils;
import com.timecat.module.main.miniapp.utilities.SettingsUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DoodleView extends View {
    private static final float TOUCH_TOLERANCE = 4.0f;
    private Bitmap bitmap;
    Map<Path, Integer> colorsMap;
    Context context_new;
    private Path mPath;
    private float mX;
    private float mY;
    private Canvas myCanvas;
    private Paint paintLine;
    private ArrayList<Path> paths = new ArrayList<>();
    int selectedColor;
    int selectedWidth;
    private ArrayList<Path> undonePaths = new ArrayList<>();
    Map<Path, Integer> widthMap;

    public DoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int parseColor = (SettingsUtils.GetValue(getContext(), "PAINT_COLOR") == null || SettingsUtils.GetValue(getContext(), "PAINT_COLOR").equals("")) ? Color.parseColor("#FFFFFF") : Integer.parseInt(SettingsUtils.GetValue(getContext(), "PAINT_COLOR"));
        this.selectedColor = parseColor;
        parseColor = (SettingsUtils.GetValue(getContext(), "PAINT_WIDTH") == null || SettingsUtils.GetValue(getContext(), "PAINT_WIDTH").equals("")) ? 3 : Integer.parseInt(SettingsUtils.GetValue(getContext(), "PAINT_WIDTH"));
        this.selectedWidth = parseColor;
        this.colorsMap = new HashMap();
        this.widthMap = new HashMap();
        this.context_new = context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        setDrawingCacheEnabled(false);
        this.paintLine = new Paint();
        this.paintLine.setAntiAlias(true);
        this.paintLine.setDither(true);
        this.paintLine.setColor(this.selectedColor);
        this.paintLine.setStyle(Style.STROKE);
        this.paintLine.setStrokeJoin(Join.ROUND);
        this.paintLine.setStrokeWidth((float) GeneralUtils.GetDP(getContext(), this.selectedWidth));
        this.paintLine.setStrokeCap(Cap.ROUND);
        this.bitmap = Bitmap.createBitmap(500, 500, Config.ARGB_8888);
        this.myCanvas = new Canvas(this.bitmap);
        this.mPath = new Path();
    }

    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        this.bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        this.myCanvas = new Canvas(this.bitmap);
    }

    protected void onDraw(Canvas canvas) {
        Iterator it = this.paths.iterator();
        while (it.hasNext()) {
            Path p = (Path) it.next();
            this.paintLine.setColor(((Integer) this.colorsMap.get(p)).intValue());
            this.paintLine.setStrokeWidth((float) GeneralUtils.GetDP(getContext(), ((Integer) this.widthMap.get(p)).intValue()));
            canvas.drawPath(p, this.paintLine);
        }
        this.paintLine.setColor(this.selectedColor);
        this.paintLine.setStrokeWidth((float) GeneralUtils.GetDP(getContext(), this.selectedWidth));
        canvas.drawPath(this.mPath, this.paintLine);
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case 0:
                touch_start(x, y);
                invalidate();
                break;
            case 1:
                this.colorsMap.put(this.mPath, Integer.valueOf(this.selectedColor));
                this.widthMap.put(this.mPath, Integer.valueOf(this.selectedWidth));
                touch_up();
                invalidate();
                break;
            case 2:
                touch_move(x, y);
                invalidate();
                break;
        }
        return true;
    }

    private void touch_start(float x, float y) {
        this.undonePaths.clear();
        this.mPath.reset();
        this.mPath.moveTo(x, y);
        this.mX = x;
        this.mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - this.mX);
        float dy = Math.abs(y - this.mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            this.mPath.quadTo(this.mX, this.mY, (this.mX + x) / 2.0f, (this.mY + y) / 2.0f);
            this.mX = x;
            this.mY = y;
        }
    }

    private void touch_up() {
        this.mPath.lineTo(this.mX, this.mY);
        this.myCanvas.drawPath(this.mPath, this.paintLine);
        this.paths.add(this.mPath);
        this.mPath = new Path();
    }

    public void onClickUndo() {
        if (this.paths.size() > 0) {
            this.undonePaths.add(this.paths.remove(this.paths.size() - 1));
            this.colorsMap.remove(this.undonePaths.get(this.undonePaths.size() - 1));
            postInvalidate();
        }
    }

    public void onClickClear() {
        this.mPath.reset();
        this.paths.clear();
        this.colorsMap.clear();
        this.bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        this.myCanvas = new Canvas(this.bitmap);
        postInvalidate();
    }

    public void changeColor(int color) {
        this.selectedColor = color;
        SettingsUtils.SetValue(getContext(), "PAINT_COLOR", color + "");
    }

    public void changeWidth(int width) {
        this.selectedWidth = width;
        SettingsUtils.SetValue(getContext(), "PAINT_WIDTH", width + "");
    }
}
