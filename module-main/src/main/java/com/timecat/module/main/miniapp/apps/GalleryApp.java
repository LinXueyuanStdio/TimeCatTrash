package com.timecat.module.main.miniapp.apps;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore.Images.Media;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.timecat.module.main.R;
import com.timecat.module.main.miniapp.adapters.GridViewGalleryAdapter;
import com.timecat.module.main.miniapp.utilities.GeneralUtils;
import com.timecat.module.main.miniapp.utilities.SettingsUtils;
import com.timecat.plugin.window.StandOutFlags;
import com.timecat.plugin.window.StandOutWindow;
import com.timecat.plugin.window.Window;
import com.timecat.plugin.window.WindowAgreement;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GalleryApp extends StandOutWindow {

    public static int id = 8;
    public Context context;
    public int publicId;
    public View publicView;

    public String getAppName() {
        return getString(R.string.main_miniapp_gallery);
    }

    public int getAppIcon() {
        return R.drawable.ic_window_menu;
    }

    public String getTitle(int id) {
        return getString(R.string.main_miniapp_gallery);
    }

    public String getPersistentNotificationTitle(int id) {
        return getString(R.string.main_miniapp_gallery);
    }

    public String getPersistentNotificationMessage(int id) {
        return getString(R.string.main_miniapp_running);
    }

    public int getHiddenIcon() {
        return R.mipmap.gallery;
    }

    public String getHiddenNotificationTitle(int id) {
        return getString(R.string.main_miniapp_gallery);
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
        int h = SettingsUtils.GetValue(window.getContext(), getAppName() + "HEIGHT").equals("") ? 200
                : Integer.parseInt(SettingsUtils.GetValue(window.getContext(), getAppName() + "HEIGHT"));
        int w = SettingsUtils.GetValue(window.getContext(), getAppName() + "WIDTH").equals("") ? 200
                : Integer.parseInt(SettingsUtils.GetValue(window.getContext(), getAppName() + "WIDTH"));
        int x = SettingsUtils.GetValue(window.getContext(), getAppName() + "XPOS").equals("")
                ? Integer.MIN_VALUE : (int) Float
                .parseFloat(SettingsUtils.GetValue(window.getContext(), getAppName() + "XPOS"));
        int y = SettingsUtils.GetValue(window.getContext(), getAppName() + "YPOS").equals("")
                ? Integer.MIN_VALUE : (int) Float
                .parseFloat(SettingsUtils.GetValue(window.getContext(), getAppName() + "YPOS"));
        if (h < GeneralUtils.GetDP(window.getContext(), 200)) {
            h = GeneralUtils.GetDP(window.getContext(), 200);
        }
        if (w < GeneralUtils.GetDP(window.getContext(), 200)) {
            w = GeneralUtils.GetDP(window.getContext(), 200);
        }
        return new StandOutLayoutParams(id, w, h, x, y, GeneralUtils.GetDP(window.getContext(), 56),
                GeneralUtils.GetDP(window.getContext(), 56));
    }

    public int getFlags(int id) {
        return (((StandOutFlags.FLAG_DECORATION_SYSTEM | StandOutFlags.FLAG_BODY_MOVE_ENABLE)
                | StandOutFlags.FLAG_WINDOW_HIDE_ENABLE) | StandOutFlags.FLAG_WINDOW_BRING_TO_FRONT_ON_TAP)
                | StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE;
    }

    public List<DropDownListItem> getDropDownItems(int id) {
        return new ArrayList<>();
    }

    public void createAndAttachView(int id, FrameLayout frame) {
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.app_gallery, frame, true);
        publicId = id;
        publicView = view;
        context = getApplicationContext();
        GeneralUtils.GalleryMap.put(id, new GalleryCreator());
    }

    public class GalleryCreator {

        ArrayList<Bitmap> arrayListBitmap = new ArrayList<>();
        ArrayList<Integer> arrayListOrientation = new ArrayList<>();
        ArrayList<Uri> arrayListUri = new ArrayList<>();
        GridView gridView;
        GridViewGalleryAdapter gridViewGalleryAdapter;
        ImageView imageView;
        ImageButton imgBtnBack;
        TextView tvCount;
        private ViewSwitcher viewSwitcher1;
        private ViewSwitcher viewSwitcher2;
        private ViewSwitcher viewSwitcher3;

        public GalleryCreator() {
            viewSwitcher1 = publicView.findViewById(R.id.viewSwitcher1);
            viewSwitcher2 = publicView.findViewById(R.id.viewSwitcher2);
            viewSwitcher3 = publicView.findViewById(R.id.viewSwitcher3);
            gridView = publicView.findViewById(R.id.gridView);
            String[] projection = new String[]{"_id", "orientation"};
            int index = -1;
            Cursor cursor = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI,
                    projection, null, null, "_id DESC");
            if (cursor != null) {
                Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.mipmap.gallery_thumb);
                while (cursor.moveToNext()) {
                    index++;
                    arrayListBitmap.add(b);
                    try {
                        String ori = cursor.getString(cursor.getColumnIndexOrThrow("orientation"));
                        arrayListOrientation.add(Integer.parseInt(ori));
                    } catch (Exception e) {
                        arrayListOrientation.add(0);
                    }
                }
                cursor.close();
            }
            gridViewGalleryAdapter = new GridViewGalleryAdapter(arrayListBitmap, context,
                    R.layout.layout_gallery_griditem);
            gridView.setAdapter(gridViewGalleryAdapter);
            gridView.setOnItemClickListener((adapterView, view, position, id) -> {
                imageView.setImageBitmap(getThumbnail(arrayListUri.get(position), position, 1024));
                switchView(1);
            });
            imageView = publicView.findViewById(R.id.imageView);
            imgBtnBack = publicView.findViewById(R.id.imageButtonGalleryBack);
            imgBtnBack.setOnClickListener(v -> switchView(0));
            tvCount = publicView.findViewById(R.id.textViewGalleryCount);
            tvCount.setText(arrayListBitmap.size() + " images");
            cursor = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI,
                    projection, null, null, "_id DESC");
            index = -1;
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    index++;
                    String path = "" + cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                    Uri uri = Uri.withAppendedPath(Media.EXTERNAL_CONTENT_URI, path);
                    arrayListUri.add(uri);
                    new GenerateThumbAsync().execute(uri, index);
                }
                cursor.close();
            }
            switchView(0);
        }

        public Bitmap getThumbnail(Uri uri, int index, int size) {
            try {
                InputStream input = getContentResolver().openInputStream(uri);
                Options onlyBoundsOptions = new Options();
                onlyBoundsOptions.inJustDecodeBounds = true;
                onlyBoundsOptions.inDither = true;
                onlyBoundsOptions.inPreferredConfig = Config.ARGB_8888;
                BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
                input.close();
                if (onlyBoundsOptions.outWidth == -1 || onlyBoundsOptions.outHeight == -1) {
                    return null;
                }
                int originalSize =
                        onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth ? onlyBoundsOptions.outHeight
                                : onlyBoundsOptions.outWidth;
                double ratio = 1.0d;
                if (size != 0) {
                    ratio = originalSize > size ? (double) (originalSize / size) : 1.0d;
                }
                Options bitmapOptions = new Options();
                bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
                bitmapOptions.inDither = true;
                bitmapOptions.inPreferredConfig = Config.ARGB_8888;
                input = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
                input.close();
                return getRotatedBitmap(context, uri, bitmap, index);
            } catch (Exception e) {
                return BitmapFactory.decodeResource(context.getResources(), R.mipmap.gallery);
            }
        }

        private int getPowerOfTwoForSampleRatio(double ratio) {
            int k = Integer.highestOneBit((int) Math.floor(ratio));
            if (k == 0) {
                return 1;
            }
            return k;
        }

        public Bitmap getRotatedBitmap(Context context, Uri photoUri, Bitmap img, int index) {
            Matrix matrix = new Matrix();
            switch (arrayListOrientation.get(index)) {
                case 90:
                    matrix.postRotate(90.0f);
                    break;
                case 180:
                    matrix.postRotate(180.0f);
                    break;
                case 270:
                    matrix.postRotate(270.0f);
                    break;
            }
            return Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        }

        public void switchView(int index) {
            switch (index) {
                case 0:
                    viewSwitcher1.setDisplayedChild(0);
                    viewSwitcher2.setDisplayedChild(0);
                    return;
                case 1:
                    viewSwitcher1.setDisplayedChild(0);
                    viewSwitcher2.setDisplayedChild(1);
                    return;
                case 2:
                    viewSwitcher1.setDisplayedChild(1);
                    viewSwitcher3.setDisplayedChild(0);
                    return;
                case 3:
                    viewSwitcher1.setDisplayedChild(1);
                    viewSwitcher3.setDisplayedChild(1);
                    return;
                default:
                    return;
            }
        }

        class GenerateBitmapAsync extends AsyncTask<Object, Void, Bitmap> {

            int index = 0;

            GenerateBitmapAsync() {
            }

            protected Bitmap doInBackground(Object... params) {
                Uri uri = (Uri) params[0];
                index = (Integer) params[1];
                return getThumbnail(uri, index, 196);
            }

            protected void onPostExecute(Bitmap result) {
                arrayListBitmap.set(index, result);
                gridViewGalleryAdapter.refreshItems();
            }
        }

        class GenerateThumbAsync extends AsyncTask<Object, Void, Bitmap> {

            int index = 0;

            GenerateThumbAsync() {
            }

            protected Bitmap doInBackground(Object... params) {
                Uri uri = (Uri) params[0];
                index = (Integer) params[1];
                return getThumbnail(uri, index, 196);
            }

            protected void onPostExecute(Bitmap result) {
                arrayListBitmap.set(index, result);
                gridViewGalleryAdapter.refreshItems();
            }
        }
    }
}
