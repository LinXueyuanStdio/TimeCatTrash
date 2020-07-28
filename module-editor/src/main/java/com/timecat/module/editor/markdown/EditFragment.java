package com.timecat.module.editor.markdown;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.LogUtils;
import com.timecat.component.alert.ToastUtil;
import com.timecat.component.commonbase.utils.FileUtils;
import com.timecat.component.commonsdk.utils.override.LogUtil;
import com.timecat.component.data.StorageHelper;
import com.timecat.component.data.model.events.RefreshTitleEvent;
import com.timecat.component.data.model.events.RenderMarkdownEvent;
import com.timecat.component.readonly.Constants;
import com.timecat.component.setting.DEF;
import com.timecat.component.ui.utils.MenuTintUtils;
import com.timecat.module.editor.R;
import com.timecat.module.editor.R2;
import com.timecat.module.editor.base.SaveFileTask;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindString;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import my.shouheng.palmmarkdown.MDItemView;
import my.shouheng.palmmarkdown.MarkdownEditor;
import my.shouheng.palmmarkdown.dialog.AttachmentPickerDialog;
import my.shouheng.palmmarkdown.dialog.LinkInputDialog;
import my.shouheng.palmmarkdown.dialog.MathJaxEditor;
import my.shouheng.palmmarkdown.dialog.TableInputDialog;
import my.shouheng.palmmarkdown.fastscroller.FastScrollScrollView;
import my.shouheng.palmmarkdown.tools.AttachmentHelper;
import my.shouheng.palmmarkdown.tools.FileHelper;
import my.shouheng.palmmarkdown.tools.MarkdownFormat;
import my.shouheng.palmmarkdown.tools.RequestCodes;
import my.shouheng.palmmarkdown.utils.StringUtils;

public class EditFragment extends BaseEditorFragment {

    private final int REQ_MENU_SORT = 0x0101;
    @BindView(R2.id.et_content)
    MarkdownEditor etContent;
    @BindView(R2.id.ll_container)
    LinearLayout llContainer;
    @BindView(R2.id.iv_enable_format)
    ImageView ivEnableFormat;
    @BindView(R2.id.iv_setting)
    ImageView ivSetting;
    @BindView(R2.id.rl_bottom_editors)
    RelativeLayout rlBottomEditors;
    @BindView(R2.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R2.id.fssv)
    FastScrollScrollView fssv;
    @BindString(R2.string.dialog_item_text_local_image)
    String localImage;
    @BindString(R2.string.dialog_item_text_internet_image)
    String internetImage;
    private Menu menu;
    private String rootPath;
    private EditorAction editorAction;
    private long lastSaveTime = 0;

    private boolean textChange;
    private Disposable mDisposable;
    private TextWatcher contentWatcher_forOpenFromFile = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            EventBus.getDefault()
                    .post(new RefreshTitleEvent(fileName == null ? "新文件" : fileName, s.toString().length()));
        }
    };
    private TextWatcher contentWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            textChange = true;
        }

        @Override
        public void afterTextChanged(Editable s) {
            setContentChanged();
        }
    };

    public static int dp2Px(Context context, float dpValues) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValues * scale + 0.5f);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_md_edit;
    }

    @Override
    public void initView() {
        rootPath = Environment.getExternalStorageDirectory().toString() + File.separator
                + Constants.TIMECAT_ROOT_PATH + File.separator;
        editorAction = new EditorAction(context, etContent);

        setHasOptionsMenu(true);
        configMain();

        if (!fromNote || fromFile) {
            //新建文件 / 操作文件
            if (fileContent != null) {
                etContent.setText(fileContent);
            }
            etContent.addTextChangedListener(contentWatcher_forOpenFromFile);
        } else {
            //新建笔记 / 操作笔记
            etContent.postDelayed(() -> {
                if (menu == null) {
                    return;
                }
                menu.findItem(R.id.save).setVisible(false);
                menu.findItem(R.id.rename).setVisible(false);
                menu.findItem(R.id.delete).setVisible(false);
            }, 256);
            etContent.addTextChangedListener(contentWatcher);

            /*
             * 步骤1：采用interval（）延迟发送
             * 注：此处主要展示无限次轮询，若要实现有限次轮询，仅需将interval（）改成intervalRange（）即可
             **/
            Observable.interval(0, 2, TimeUnit.SECONDS)
                    // 参数说明：
                    // 参数1 = 第1次延迟时间；
                    // 参数2 = 间隔时间数字；
                    // 参数3 = 时间单位；
                    // 该例子发送的事件特点：延迟2s后发送事件，每隔1秒产生1个数字（从0开始递增1，无限个）

                    /*
                     * 步骤2：每次发送数字前发送1次网络请求（doOnNext（）在执行Next事件前调用）
                     * 即每隔1秒产生1个数字前，就发送1次网络请求，从而实现轮询需求
                     **/
                    .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                    .doOnNext(integer -> {
                        if (textChange) {
                            textChange = false;
                            autoSaveToTimeCat();
                        }
                    })
                    .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Long>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mDisposable = d;
                        }

                        @Override
                        public void onNext(Long value) {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.i("对Error事件作出响应");
                        }

                        @Override
                        public void onComplete() {
                            LogUtil.i("对Complete事件作出响应");
                        }
                    });
        }
        etContent.requestFocus();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.edit_fragment_menu, menu);
        this.menu = menu;
        MenuTintUtils.tintAllIcons(menu, context);
        MenuItem renameItem = menu.findItem(R.id.rename);
        MenuItem deleteItem = menu.findItem(R.id.delete);
        renameItem.setEnabled(saved);
        deleteItem.setEnabled(saved);
        if (!fromFile && fromNote) {
            menu.findItem(R.id.save).setVisible(false);
            menu.findItem(R.id.rename).setVisible(false);
            menu.findItem(R.id.delete).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.clear_all) {// clear all the content in edittext
            editorAction.clearAll();

        } else if (i == R.id.md_docs) {// open the markdown cheatsheet fragment
            editorAction.checkDocs();

        } else if (i == R.id.statistics) {
            editorAction.statistics();

        } else if (i == R.id.save) {
            if (!StorageHelper.isExternalStorageWritable()) {
                ToastUtil.e(R.string.toast_message_sdcard_unavailable);
                return super.onOptionsItemSelected(item);
            }
            onSaveClick();

        } else if (i == R.id.rename) {
            onRenameClick();

        } else if (i == R.id.delete) {
            onDeleteClick();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String title;
            Uri uri;
            switch (requestCode) {
                case REQ_MENU_SORT:
                    addBottomMenus();
                    break;
                case 1:
                    final Uri selectedImage = data.getData();
                    AlertDialog.Builder inputDialog = new AlertDialog.Builder(context);
                    inputDialog.setTitle(R.string.dialog_title_insert_image);

                    LayoutInflater inflater = context.getLayoutInflater();
                    View view = inflater.inflate(R.layout.dialog_insert_image, null);
                    final EditText imageDisplayText = view.findViewById(R.id.image_display_text);
                    EditText imageUri = view.findViewById(R.id.image_uri);
                    imageUri.setText(selectedImage.getPath());

                    inputDialog.setView(view);
                    inputDialog.setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    inputDialog.setPositiveButton(R.string.dialog_btn_insert,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String displayText = imageDisplayText.getText().toString();
                                    editorAction.insertImage(displayText, selectedImage.getPath());
                                }
                            });
                    inputDialog.show();
                    break;
                case RequestCodes.REQUEST_TAKE_PHOTO:
//                    if (shouldCompressImage()) {
//                        AttachmentHelper.compressImage(context, new File(imgPath), onCompressListener);TODO 是否压缩
//                    } else {
//                        return
//                    }
                    String imgPath = AttachmentHelper.getFilePath();
                    uri = FileHelper.getUriFromFile(context, new File(imgPath));
                    title = FileHelper.getNameFromUri(context, uri);
                    etContent.addLinkEffect(MarkdownFormat.ATTACHMENT, title, uri.toString());
                    break;
                case RequestCodes.REQUEST_SELECT_IMAGE:
                case RequestCodes.REQUEST_FILES:
                    for (Uri u : AttachmentHelper.getUrisFromIntent(data)) {
                        uri = FileHelper
                                .getUriFromFile(context, FileHelper.createAttachmentFromUri(context, u));
                        title = FileHelper.getNameFromUri(context, u);
                        etContent.addLinkEffect(MarkdownFormat.LINK, title, uri.toString());
                    }
                    break;
            }
        } else {
            switch (requestCode) {
                case RequestCodes.REQUEST_SELECT_IMAGE:
                    ToastUtil.w(R.string.toast_does_not_select);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean needEventBus() {
        return true;
    }

    @Override
    public void onPause() {
        if (fromNote) {
            autoSaveToTimeCat();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        cancel();
        super.onDestroy();
    }

    /**
     * 取消订阅
     */
    public void cancel() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            LogUtils.w("====Rx定时器取消======");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(Boolean refresh) {
        if (refresh) {
            EventBus.getDefault().post(new RenderMarkdownEvent(etContent.getText().toString()));
        }
    }
    //endregion

    //region format
    public List<MarkdownFormat> getMarkdownFormats() {
        String mdStr = DEF.config().getNoteEditorMenuSort();
        if (!TextUtils.isEmpty(mdStr)) {
            String[] mds = mdStr.split(MarkdownFormat.ITEM_SORT_SPLIT);
            List<MarkdownFormat> markdownFormats = new LinkedList<>();
            for (String md : mds) {
                markdownFormats.add(MarkdownFormat.valueOf(md));
            }
            return markdownFormats;
        } else {
            return MarkdownFormat.defaultMarkdownFormats;
        }
    }

    public void setMarkdownFormats(List<MarkdownFormat> markdownFormats) {
        int size = markdownFormats.size();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            if (i == size - 1) {
                sb.append(markdownFormats.get(i).name());
            } else {
                sb.append(markdownFormats.get(i).name()).append(MarkdownFormat.ITEM_SORT_SPLIT);
            }
        }
        DEF.config().setNoteEditorMenuSort(sb.toString());
    }
    //endregion

    //region open from file
    private void onSaveClick() {
        if (!saved) {
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(context);
            saveDialog.setTitle(R.string.dialog_title_save_file);

            LayoutInflater inflater = context.getLayoutInflater();
            final View view = inflater.inflate(R.layout.dialog_save_file, null);
            final EditText fileNameET = view.findViewById(R.id.file_name);

            saveDialog.setView(view);
            saveDialog.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
            saveDialog.setPositiveButton(R.string.dialog_btn_save,
                    (dialog, which) -> {
                        fileName = fileNameET.getText().toString();
                        filePath = rootPath + fileName + ".md";
                        new SaveFileTask(context, filePath, fileName,
                                etContent.getText().toString(),
                                result -> saved = result).execute();// change saved value to true if save success
                    });

            saveDialog.show();
        } else {
            editorAction.update(filePath);
            ToastUtil.i(R.string.file_saved);
        }
    }

    private void onRenameClick() {
        if (saved) {
            AlertDialog.Builder renameDialog = new AlertDialog.Builder(context);
            renameDialog.setTitle(R.string.dialog_title_rename_file);

            LayoutInflater inflater = context.getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_save_file, null);
            final EditText fileNameET = view.findViewById(R.id.file_name);

            fileNameET.setText(fileName);
            fileNameET.setSelection(fileName.length());

            renameDialog.setView(view);
            renameDialog.setNegativeButton(R.string.cancel,
                    (dialog, which) -> dialog.cancel());
            renameDialog.setPositiveButton(R.string.dialog_btn_rename,
                    (dialog, which) -> {
                        fileName = fileNameET.getText().toString();
                        FileUtils.renameFile(context, new File(filePath),
                                new File(rootPath + fileName + ".md"));
                        filePath = rootPath + fileName + ".md";
                    });
            renameDialog.show();
        }
    }

    private void onDeleteClick() {
        // delete file, close fragment if success
        boolean result = FileUtils.deleteFile(new File(filePath));
        if (result) {
            ToastUtil.ok(R.string.toast_message_deleted);
            context.getSupportFragmentManager().popBackStack();
        } else {
            ToastUtil.e(R.string.toast_message_delete_error);
        }
    }

    // region Config main board
    private void configMain() {

        rlBottomEditors.setVisibility(View.GONE);

        int[] ids = new int[]{R.id.iv_redo, R.id.iv_undo, R.id.iv_insert_picture, R.id.iv_insert_link,
                R.id.iv_table};
        for (int id : ids) {
            view.findViewById(id).setOnClickListener(this::onFormatClick);
        }

        addBottomMenus();

        ivEnableFormat.setOnClickListener(v -> switchFormat());
        ivSetting.setOnClickListener(v -> {
            Intent intent = new Intent(context, MenuSortActivity.class);
            startActivityForResult(intent, REQ_MENU_SORT);
        });

        fssv.getFastScrollDelegate().setThumbSize(16, 40);
        fssv.getFastScrollDelegate().setThumbDynamicHeight(false);
        if (getContext() != null) {
            fssv.getFastScrollDelegate()
                    .setThumbDrawable(getDrawableCompact(R.drawable.fast_scroll_bar_dark));
        }

        lastSaveTime = System.currentTimeMillis();
    }

    private void setContentChanged() {
        long now = System.currentTimeMillis();
        if (now - lastSaveTime < 3 * 1000) {
            return;
        }

        lastSaveTime = now;
        autoSaveToTimeCat();
    }

    private void autoSaveToTimeCat() {
    }

    private void addBottomMenus() {
        llContainer.removeAllViews();
        int dp12 = dp2Px(getContext(), 9);
        List<MarkdownFormat> markdownFormats = getMarkdownFormats();
        for (MarkdownFormat markdownFormat : markdownFormats) {
            MDItemView mdItemView = new MDItemView(getContext());
            mdItemView.setMarkdownFormat(markdownFormat);
            mdItemView.setPadding(dp12, dp12, dp12, dp12);
            llContainer.addView(mdItemView);
            mdItemView.setOnClickListener(v -> {
                if (markdownFormat == MarkdownFormat.CHECKBOX
                        || markdownFormat == MarkdownFormat.CHECKBOX_OUTLINE) {
                    etContent.addCheckbox("", markdownFormat == MarkdownFormat.CHECKBOX);
                } else if (markdownFormat == MarkdownFormat.MATH_JAX) {
                    showMarkJaxEditor();
                } else {
                    etContent.addEffect(markdownFormat);
                }
            });
        }
    }

    private void onFormatClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_undo) {
            editorAction.undo();
        } else if (i == R.id.iv_redo) {
            editorAction.redo();
        } else if (i == R.id.iv_insert_picture) {
            showAttachmentPicker();
        } else if (i == R.id.iv_insert_link) {
            showLinkEditor();
        } else if (i == R.id.iv_table) {
            showTableEditor();
        }
    }

    private void switchFormat() {
        boolean rlBottomVisible = rlBottomEditors.getVisibility() == View.VISIBLE;
        rlBottomEditors.setVisibility(rlBottomVisible ? View.GONE : View.VISIBLE);
        ivEnableFormat.setImageDrawable(MDItemView.tintDrawable(
                getResources().getDrawable(R.drawable.ic_text_format_black_24dp),
                rlBottomVisible ? Color.WHITE : getResources().getColor(R.color.theme_color_primary)));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ivEnableFormat.getHeight() * (rlBottomVisible ? 1 : 2));
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rlBottom.setLayoutParams(params);
    }

    private void addImageLink() {
        LinkInputDialog.getInstance((title, link) ->
                etContent.addLinkEffect(MarkdownFormat.ATTACHMENT, title, link))
                .show(Objects.requireNonNull(getFragmentManager()), "Link Image");
    }

    private void showMarkJaxEditor() {
        MathJaxEditor.newInstance((exp, isSingleLine) ->
                etContent.addMathJax(exp, isSingleLine))
                .show(Objects.requireNonNull(getFragmentManager()), "MATH JAX EDITOR");
    }

    private void showTableEditor() {
        TableInputDialog.getInstance((rowsStr, colsStr) -> {
            int rows = StringUtils.parseInteger(rowsStr, 3);
            int cols = StringUtils.parseInteger(colsStr, 3);
            etContent.addTableEffect(rows, cols);
        }).show(Objects.requireNonNull(getFragmentManager()), "TABLE INPUT");
    }

    private void showLinkEditor() {
        LinkInputDialog.getInstance((title, link) ->
                etContent.addLinkEffect(MarkdownFormat.LINK, title, link))
                .show(Objects.requireNonNull(getFragmentManager()), "LINK INPUT");
    }

    private void showAttachmentPicker() {
        new AttachmentPickerDialog.Builder(this)
                .setRecordVisible(false)
                .setVideoVisible(false)
                .setAddLinkVisible(true)
                .setFilesVisible(true)
                .setOnAddNetUriSelectedListener(this::addImageLink)
                .build().show(Objects.requireNonNull(getFragmentManager()), "Attachment picker");
    }

    public Drawable getDrawableCompact(@DrawableRes int resId) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            return getContext().getDrawable(resId);
        } else {
            return getContext().getResources().getDrawable(resId);
        }
    }
    // endregion
}
