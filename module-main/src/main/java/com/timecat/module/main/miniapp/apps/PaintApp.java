package com.timecat.module.main.miniapp.apps;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.timecat.component.alert.ToastUtil;
import com.timecat.component.ui.standard.colorpicker.HoloColorPicker;
import com.timecat.component.ui.standard.colorpicker.OpacityBar;
import com.timecat.component.ui.standard.colorpicker.SVBar;
import com.timecat.module.main.R;
import com.timecat.module.main.miniapp.others.DoodleView;
import com.timecat.module.main.miniapp.utilities.GeneralUtils;
import com.timecat.module.main.miniapp.utilities.SettingsUtils;
import com.timecat.plugin.window.StandOutFlags;
import com.timecat.plugin.window.StandOutWindow;
import com.timecat.plugin.window.Window;
import com.timecat.plugin.window.WindowAgreement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PaintApp extends StandOutWindow {
    public static int id = 12;
    private Context context;
    private int publicId;
    private View publicView;

    public String getAppName() {
        return getString(R.string.main_miniapp_Paint);
    }

    public int getAppIcon() {
        return R.drawable.ic_window_menu;
    }

    public String getTitle(int id) {
        return getString(R.string.main_miniapp_Paint);
    }

    public String getPersistentNotificationTitle(int id) {
        return getString(R.string.main_miniapp_Paint);
    }

    public String getPersistentNotificationMessage(int id) {
        return getString(R.string.main_miniapp_running);
    }

    public int getHiddenIcon() {
        return R.mipmap.paint;
    }

    public String getHiddenNotificationTitle(int id) {
        return getString(R.string.main_miniapp_Paint);
    }

    public String getHiddenNotificationMessage(int id) {
        return getString(R.string.main_miniapp_mininized);
    }

    public Intent getHiddenNotificationIntent(int id) {
        return WindowAgreement.getShowIntent(this, getClass(), id);
    }

    public Animation getShowAnimation(int id) {
        if (isExistingId(id)) {
            return AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        }
        return super.getShowAnimation(id);
    }

    public Animation getHideAnimation(int id) {
        return AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
    }

    public StandOutLayoutParams getParams(int id, Window window) {
        int h = SettingsUtils.GetValue(window.getContext(), getAppName() + "HEIGHT").equals("") ? 200 : Integer.parseInt(SettingsUtils.GetValue(window.getContext(), getAppName() + "HEIGHT"));
        int w = SettingsUtils.GetValue(window.getContext(), getAppName() + "WIDTH").equals("") ? 200 : Integer.parseInt(SettingsUtils.GetValue(window.getContext(), getAppName() + "WIDTH"));
        int x = SettingsUtils.GetValue(window.getContext(), getAppName() + "XPOS").equals("") ? Integer.MIN_VALUE : (int) Float.parseFloat(SettingsUtils.GetValue(window.getContext(), getAppName() + "XPOS"));
        int y = SettingsUtils.GetValue(window.getContext(), getAppName() + "YPOS").equals("") ? Integer.MIN_VALUE : (int) Float.parseFloat(SettingsUtils.GetValue(window.getContext(), getAppName() + "YPOS"));
        if (h < GeneralUtils.GetDP(window.getContext(), 200)) {
            h = GeneralUtils.GetDP(window.getContext(), 200);
        }
        if (w < GeneralUtils.GetDP(window.getContext(), 200)) {
            w = GeneralUtils.GetDP(window.getContext(), 200);
        }
        return new StandOutLayoutParams(id, w, h, x, y, GeneralUtils.GetDP(window.getContext(), 56), GeneralUtils.GetDP(window.getContext(), 56));
    }

    public int getFlags(int id) {
        return (((StandOutFlags.FLAG_DECORATION_SYSTEM | StandOutFlags.FLAG_BODY_MOVE_ENABLE) | StandOutFlags.FLAG_WINDOW_HIDE_ENABLE) | StandOutFlags.FLAG_WINDOW_BRING_TO_FRONT_ON_TAP) | StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE;
    }

    public List<DropDownListItem> getDropDownItems(final int id) {
        List<DropDownListItem> items = new ArrayList<>();
        items.add(new DropDownListItem(R.mipmap.menu_undo, getString(R.string.main_miniapp_Undo), new Runnable() {
            public void run() {
                ((PaintCreator) GeneralUtils.PaintMap.get(Integer.valueOf(id))).doodleView.onClickUndo();
            }
        }));
        items.add(new DropDownListItem(R.mipmap.menu_delete, "Clear", new Runnable() {
            public void run() {
                ((PaintCreator) GeneralUtils.PaintMap.get(Integer.valueOf(id))).doodleView.onClickClear();
            }
        }));
        items.add(new DropDownListItem(R.mipmap.menu_edit, "Size", new Runnable() {
            public void run() {
                ((PaintCreator) GeneralUtils.PaintMap.get(Integer.valueOf(id))).switchView(3);
            }
        }));
        items.add(new DropDownListItem(R.mipmap.menu_colors, "Color", new Runnable() {
            public void run() {
                ((PaintCreator) GeneralUtils.PaintMap.get(Integer.valueOf(id))).switchView(2);
            }
        }));
        items.add(new DropDownListItem(R.mipmap.menu_save, getString(R.string.main_miniapp_Save), new Runnable() {
            public void run() {
                ((PaintCreator) GeneralUtils.PaintMap.get(Integer.valueOf(id))).switchView(1);
            }
        }));
        return items;
    }

    public void createAndAttachView(int id, FrameLayout frame) {
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.app_paint, frame, true);
        this.publicId = id;
        this.publicView = view;
        this.context = getApplicationContext();
        GeneralUtils.PaintMap.put(Integer.valueOf(id), new PaintCreator());
    }

    public class PaintCreator {
        private DoodleView doodleView;
        private EditText editTextFilename;
        private ImageButton imgBtnBack;
        private ImageButton imgBtnBackFromSize;
        private ImageButton imgBtnCancel;
        private ImageButton imgBtnSave;
        private OpacityBar opacityBar;
        private HoloColorPicker picker;
        private RelativeLayout popupAnchor;
        private SeekBar seekBarWidth;
        private SVBar svBar;
        private TextView textViewSize;
        private ViewSwitcher viewSwitcher1;
        private ViewSwitcher viewSwitcher2;
        private ViewSwitcher viewSwitcher3;

        public PaintCreator() {
            this.popupAnchor = (RelativeLayout) PaintApp.this.publicView.findViewById(R.id.layoutPopupAnchor);
            this.viewSwitcher1 = (ViewSwitcher) PaintApp.this.publicView.findViewById(R.id.viewSwitcher1);
            this.viewSwitcher2 = (ViewSwitcher) PaintApp.this.publicView.findViewById(R.id.viewSwitcher2);
            this.viewSwitcher3 = (ViewSwitcher) PaintApp.this.publicView.findViewById(R.id.viewSwitcher3);
            this.editTextFilename = (EditText) PaintApp.this.publicView.findViewById(R.id.editTextFilename);
            this.imgBtnSave = (ImageButton) PaintApp.this.publicView.findViewById(R.id.imageButtonSave);
            this.imgBtnCancel = (ImageButton) PaintApp.this.publicView.findViewById(R.id.imageButtonCancel);
            this.imgBtnBack = (ImageButton) PaintApp.this.publicView.findViewById(R.id.imageButtonBack);
            this.imgBtnBackFromSize = (ImageButton) PaintApp.this.publicView.findViewById(R.id.imageButtonBackFromSize);
            this.doodleView = (DoodleView) PaintApp.this.publicView.findViewById(R.id.doodleView);
            new File(GeneralUtils.GetPaintFolderPath()).mkdirs();
            this.imgBtnSave.setOnClickListener(v -> {
                if (PaintCreator.this.editTextFilename.getText().toString().length() != 0) {
                    Bitmap bitmap = Bitmap.createBitmap(((PaintCreator) GeneralUtils.PaintMap.get(Integer.valueOf(PaintApp.this.publicId))).doodleView.getWidth(), ((PaintCreator) GeneralUtils.PaintMap.get(Integer.valueOf(PaintApp.this.publicId))).doodleView.getHeight(), Config.RGB_565);
                    ((PaintCreator) GeneralUtils.PaintMap.get(Integer.valueOf(PaintApp.this.publicId))).doodleView.draw(new Canvas(bitmap));
                    try {
                        File doodleImages = new File(GeneralUtils.GetPaintFolderPath());
                        doodleImages.mkdirs();
                        bitmap.compress(CompressFormat.PNG, 100, new FileOutputStream(new File(doodleImages, PaintCreator.this.editTextFilename.getText().toString().toLowerCase().endsWith(".png") ? PaintCreator.this.editTextFilename.getText().toString() : PaintCreator.this.editTextFilename.getText().toString() + ".png")));
                        ToastUtil.i(PaintApp.this.getApplicationContext(), "Image saved");
                        return;
                    } catch (FileNotFoundException e) {
                        ToastUtil.i(PaintApp.this.getApplicationContext(), "Unable to save image");
                        return;
                    }
                }
                ToastUtil.i(PaintApp.this.getApplicationContext(), "Please enter file name");
            });
            this.imgBtnCancel.setOnClickListener(v -> PaintCreator.this.switchView(0));
            this.imgBtnBack.setOnClickListener(v -> PaintCreator.this.switchView(0));
            this.imgBtnBackFromSize.setOnClickListener(v -> PaintCreator.this.switchView(0));
            this.picker = (HoloColorPicker) PaintApp.this.publicView.findViewById(R.id.picker);
            this.svBar = (SVBar) PaintApp.this.publicView.findViewById(R.id.svbar);
            this.opacityBar = (OpacityBar) PaintApp.this.publicView.findViewById(R.id.opacitybar);
            this.picker.addSVBar(this.svBar);
            this.picker.addOpacityBar(this.opacityBar);
            this.picker.setOnColorChangedListener(color -> {
                PaintCreator.this.doodleView.changeColor(color);
                PaintCreator.this.picker.setOldCenterColor(color);
            });
            int oldColor = (SettingsUtils.GetValue(PaintApp.this.context, "PAINT_COLOR") == null || SettingsUtils.GetValue(PaintApp.this.context, "PAINT_COLOR").equals("")) ? Color.parseColor("#FFFFFF") : Integer.parseInt(SettingsUtils.GetValue(PaintApp.this.context, "PAINT_COLOR"));
            this.picker.setColor(oldColor);
            this.textViewSize = (TextView) PaintApp.this.publicView.findViewById(R.id.textViewPaintWidth);
            this.seekBarWidth = (SeekBar) PaintApp.this.publicView.findViewById(R.id.seekBarPaintWidth);
            int savedWidth = (SettingsUtils.GetValue(PaintApp.this.context, "PAINT_WIDTH") == null || SettingsUtils.GetValue(PaintApp.this.context, "PAINT_WIDTH").equals("")) ? 3 : Integer.parseInt(SettingsUtils.GetValue(PaintApp.this.context, "PAINT_WIDTH"));
            this.seekBarWidth.setProgress(savedWidth - 1);
            this.textViewSize.setText(savedWidth + "px");
            this.seekBarWidth.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    PaintCreator.this.doodleView.changeWidth(progress + 1);
                    PaintCreator.this.textViewSize.setText((progress + 1) + "px");
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        }

        public void switchView(int index) {
            switch (index) {
                case 0:
                    this.viewSwitcher1.setDisplayedChild(0);
                    this.viewSwitcher2.setDisplayedChild(0);
                    return;
                case 1:
                    this.viewSwitcher1.setDisplayedChild(0);
                    this.viewSwitcher2.setDisplayedChild(1);
                    return;
                case 2:
                    this.viewSwitcher1.setDisplayedChild(1);
                    this.viewSwitcher3.setDisplayedChild(0);
                    return;
                case 3:
                    this.viewSwitcher1.setDisplayedChild(1);
                    this.viewSwitcher3.setDisplayedChild(1);
                    return;
                default:
                    return;
            }
        }
    }
}
