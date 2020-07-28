package com.jecelyin.editor.v2.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.jecelyin.editor.v2.BaseActivity;
import com.jecelyin.editor.v2.Pref;
import com.jecelyin.editor.v2.R;
import com.jecelyin.editor.v2.adapter.MainMenuAdapter;
import com.jecelyin.editor.v2.common.Command;
import com.jecelyin.editor.v2.common.SaveListener;
import com.jecelyin.editor.v2.task.ClusterCommand;
import com.jecelyin.editor.v2.ui.dialog.ChangeThemeDialog;
import com.jecelyin.editor.v2.ui.dialog.CharsetsDialog;
import com.jecelyin.editor.v2.ui.dialog.GotoLineDialog;
import com.jecelyin.editor.v2.ui.dialog.InsertDateTimeDialog;
import com.jecelyin.editor.v2.ui.dialog.LangListDialog;
import com.jecelyin.editor.v2.ui.dialog.RunDialog;
import com.jecelyin.editor.v2.ui.dialog.WrapCharDialog;
import com.jecelyin.editor.v2.ui.settings.RawTextSettingsActivity;
import com.jecelyin.editor.v2.view.TabViewPager;
import com.jecelyin.editor.v2.view.menu.MenuDef;
import com.jecelyin.editor.v2.view.menu.MenuFactory;
import com.jecelyin.editor.v2.view.menu.MenuItemInfo;
import com.jecelyin.editor.v2.widget.SymbolBarLayout;
import com.jecelyin.editor.v2.widget.TranslucentDrawerLayout;
import com.jecelyin.editor.v2.widget.text.JsCallback;
import com.timecat.component.alert.UIUtils;
import com.timecat.component.commonsdk.utils.override.L;
import com.timecat.component.commonsdk.utils.utils.IOUtils;
import com.timecat.component.commonsdk.utils.utils.SysUtils;
import com.timecat.component.data.DBHelper;
import com.timecat.component.readonly.RouterHub;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import my.shouheng.palmmarkdown.tools.MarkdownFormat;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class EditorActivity extends BaseActivity
        implements MenuItem.OnMenuItemClickListener,
                   RawEditorAction,
                   FolderChooserDialog.FolderCallback,
                   SharedPreferences.OnSharedPreferenceChangeListener,
                   SymbolBarLayout.OnSymbolCharClickListener {

    private static final String TAG = EditorActivity.class.getName();
    private static final int RC_OPEN_FILE = 1;
    private final static int RC_SAVE = 3;
    private static final int RC_PERMISSION_STORAGE = 2;
    private static final int RC_SETTINGS = 5;

    private static final int MODE_PICK_FILE = 1;
    private static final int MODE_PICK_PATH = 2;

    public Toolbar mToolbar;
    LinearLayout mLoadingLayout;
    TabViewPager mTabPager;
    RecyclerView mMenuRecyclerView;
    TranslucentDrawerLayout mDrawerLayout;
    RecyclerView mTabRecyclerView;
    public TextView mVersionTextView;
    public SymbolBarLayout mSymbolBarLayout;

    private TabManager tabManager;

    private Pref pref;
    private ClusterCommand clusterCommand;
    //    TabDrawable tabDrawable;
    private MenuManager menuManager;
    private FolderChooserDialog.FolderCallback findFolderCallback;
    private long mExitTime;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        try {
            super.onRestoreInstanceState(savedInstanceState);
        } catch (Exception e) {
            L.d(e); //ignore exception: Unmarshalling unknown type code 7602281 at offset 58340
        }
    }

    private void requestWriteExternalStoragePermission() {
        final String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            UIUtils.showConfirmDialog(this, null, getString(R.string.need_to_enable_read_storage_permissions), new UIUtils.OnClickCallback() {
                @Override
                public void onOkClick() {
                    ActivityCompat.requestPermissions(EditorActivity.this, permissions, RC_PERMISSION_STORAGE);
                }

                @Override
                public void onCancelClick() {
                    finish();
                }
            });
        } else {
            ActivityCompat.requestPermissions(this, permissions, RC_PERMISSION_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Write external store permission requires a restart
        for (int i = 0; i < permissions.length; i++) {
            //Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permissions[i])
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                requestWriteExternalStoragePermission();
                return;
            }
        }
        start();
    }

    public int getLayoutRes() {
        return R.layout.main_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = Pref.getInstance(this);
        MenuManager.init(this);

        setContentView(getLayoutRes());

        L.d(TAG, "onCreate");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mLoadingLayout = (LinearLayout) findViewById(R.id.loading_layout);
        mTabPager = (TabViewPager) findViewById(R.id.tab_pager);
        mMenuRecyclerView = (RecyclerView) findViewById(R.id.menuRecyclerView);
        mDrawerLayout = (TranslucentDrawerLayout) findViewById(R.id.drawer_layout);
        mTabRecyclerView = (RecyclerView) findViewById(R.id.tabRecyclerView);
        mVersionTextView = (TextView) findViewById(R.id.versionTextView);

        mSymbolBarLayout = (SymbolBarLayout) findViewById(R.id.symbolBarLayout);
        mSymbolBarLayout.setOnSymbolCharClickListener((SymbolBarLayout.OnSymbolCharClickListener) getFragmentActivity());

//        if(!AppUtils.verifySign(getContext())) {
//            UIUtils.showConfirmDialog(getContext(), getString(R.string.verify_sign_failure), new UIUtils.OnClickCallback() {
//                @Override
//                public void onOkClick() {
//                    SysUtils.startWebView(getContext(), "https://github.com/LinXueyuanStdio/mempool/releases");
//                }
//            });
//        }

        setStatusBarColor(mDrawerLayout);

        bindPreferences();
        setScreenOrientation();

        mDrawerLayout.setEnabled(false);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);

        final String version = SysUtils.getVersionName(this);
        mVersionTextView.setText(version);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                ) {
            requestWriteExternalStoragePermission();
        } else {
            start();

        }
    }

    private void bindPreferences() {
        mDrawerLayout.setKeepScreenOn(pref.isKeepScreenOn());
        mSymbolBarLayout.setVisibility(pref.isReadOnly() ? View.GONE : View.VISIBLE);

        onSharedPreferenceChanged(null, Pref.KEY_PREF_ENABLE_DRAWERS);
        pref.registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * 注意registerOnSharedPreferenceChangeListener的listeners是使用WeakHashMap引用的
     * 不能直接registerOnSharedPreferenceChangeListener(new ...) 否则可能监听不起作用
     *
     * @param sharedPreferences
     * @param key
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (mToolbar == null)
            return;
        switch (key) {
            case Pref.KEY_KEEP_SCREEN_ON:
                mToolbar.setKeepScreenOn(sharedPreferences.getBoolean(key, false));
                break;
            case Pref.KEY_ENABLE_HIGHLIGHT:
                Command command = new Command(Command.CommandEnum.ENABLE_HIGHLIGHT);
                command.object = pref.isHighlight();
                doClusterCommand(command);
                break;
            case Pref.KEY_SCREEN_ORIENTATION:
                setScreenOrientation();
                break;
            case Pref.KEY_PREF_ENABLE_DRAWERS:
                mDrawerLayout.setDrawerLockMode(pref.isEnabledDrawers() ? TranslucentDrawerLayout.LOCK_MODE_UNDEFINED : TranslucentDrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
                break;
            case Pref.KEY_READ_ONLY:
                mSymbolBarLayout.setVisibility(pref.isReadOnly() ? View.GONE : View.VISIBLE);
                break;
        }
    }

    private void setScreenOrientation() {
        int orgi = pref.getScreenOrientation();

        if (Pref.SCREEN_ORIENTATION_AUTO == orgi) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else if (Pref.SCREEN_ORIENTATION_LANDSCAPE == orgi) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (Pref.SCREEN_ORIENTATION_PORTRAIT == orgi) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void start() {
        ViewGroup parent = (ViewGroup) mLoadingLayout.getParent();
        if (parent != null) {
            parent.removeView(mLoadingLayout);
        }

//                inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mTabPager.setVisibility(View.VISIBLE);

        initUI();
    }

    private void initUI() {
        mMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTabRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDrawerLayout.setEnabled(true);

        initToolbar();

        if (menuManager == null) {
            menuManager = new MenuManager();
            MainMenuAdapter adapter = new MainMenuAdapter(this);
            mMenuRecyclerView.setAdapter(adapter);
            adapter.setMenuItemClickListener(this);
        }

        //系统可能会随时杀掉后台运行的Activity，如果这一切发生，那么系统就会调用onCreate方法，而不调用onNewIntent方法
        processIntent();
    }

    private void initToolbar() {


        Resources res = getResources();

        mToolbar.setNavigationIcon(R.drawable.ic_drawer_raw);
        mToolbar.setNavigationContentDescription(R.string.tab);

        Menu menu = mToolbar.getMenu();
        List<MenuItemInfo> menuItemInfos = MenuFactory.getInstance(this).getToolbarIcon();
        for (MenuItemInfo item : menuItemInfos) {
            MenuItem menuItem = menu.add(MenuDef.GROUP_TOOLBAR, item.getItemId(), Menu.NONE, item.getTitleResId());
            menuItem.setIcon(MenuManager.makeToolbarNormalIcon(res, item.getIconResId()));

            //menuItem.setShortcut()
            menuItem.setOnMenuItemClickListener(this);
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        MenuItem menuItem = menu.add(MenuDef.GROUP_TOOLBAR, R.id.m_menu, Menu.NONE, getString(R.string.more_menu));
        menuItem.setIcon(R.drawable.ic_right_menu);
        menuItem.setOnMenuItemClickListener(this);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        tabManager = new TabManager(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processIntent();
    }

    private void processIntent() {
        try {
            if (!processIntentImpl()) {
                UIUtils.alert(getContext(), getString(R.string.cannt_handle_intent_x, getIntent().toString()));
            }
        } catch (Throwable e) {
            L.e(e);
            UIUtils.alert(getContext(), getString(R.string.handle_intent_x_error, getIntent().toString() + "\n" + e.getMessage()));
        }
    }

    private boolean processIntentImpl() throws Throwable {
        Intent intent = getIntent();
        L.d("intent=" + intent);
        if (intent == null)
            return true; //pass hint

        String action = intent.getAction();
        // action == null if change theme
        if (action == null || Intent.ACTION_MAIN.equals(action)) {
            return true;
        }

        if (Intent.ACTION_VIEW.equals(action) || Intent.ACTION_EDIT.equals(action)) {
            if (intent.getScheme().equals("content")) {
                try {
                    InputStream attachment = getContentResolver().openInputStream(intent.getData());
                    String text = IOUtils.toString(attachment);
                    openText(text);
                } catch (Exception e) {
                    UIUtils.toast(this, getString(R.string.cannt_open_external_file_x, e.getMessage()));
                } catch (OutOfMemoryError e) {
                    UIUtils.toast(this, R.string.out_of_memory_error);
                }

                return true;
            } else if (intent.getScheme().equals("file")) {
                Uri mUri = intent.getData();
                String file = mUri != null ? mUri.getPath() : null;
                if (!TextUtils.isEmpty(file)) {
                    openFile(file);
                    return true;
                }
            }

        } else if (Intent.ACTION_SEND.equals(action) && intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            CharSequence text = extras.getCharSequence(Intent.EXTRA_TEXT);

            if (text != null) {
                openText(text);
                return true;
            } else {
                Object stream = extras.get(Intent.EXTRA_STREAM);
                if (stream != null && stream instanceof Uri) {
                    openFile(((Uri) stream).getPath());
                    return true;
                }
            }
        }

        return false;
    }

    private void onMenuClick(int id) {
        Command.CommandEnum commandEnum;

        closeMenu();

        if (id == R.id.m_new) {
            tabManager.newTab();

        } else if (id == R.id.m_open) {
            ARouter.getInstance().build(RouterHub.FILE_EXPLORER_FileExplorerActivity)
                .withString("destFile", null)
                .withInt("mode", MODE_PICK_FILE)
                .navigation(this, RC_OPEN_FILE);
        } else if (id == R.id.m_goto_line) {
            new GotoLineDialog(this).show();
        } else if (id == R.id.m_history) {
            RecentFilesManager rfm = new RecentFilesManager(this);
            rfm.setOnFileItemClickListener(new RecentFilesManager.OnFileItemClickListener() {
                @Override
                public void onClick(DBHelper.RecentFileItem item) {
                    openFile(item.path, item.encoding, item.line, item.column);
                }
            });
            rfm.show(getContext());
        } else if (id == R.id.m_wrap_line) {
            pref.setWordWrap(!pref.isWordWrap());
            doClusterCommand(new Command(Command.CommandEnum.WRAP_LINE));
        } else if (id == R.id.m_render) {
            EditorDelegate editorDelegate = getTabManager().getEditorAdapter().getCurrentEditorDelegate();
            if (editorDelegate != null) {
                editorDelegate.getText(new JsCallback<String>() {
                    @Override
                    public void onCallback(String text) {
                        showRenderActivity(text);
                    }
                });
            }
        } else if (id == R.id.m_wrap) {
            new WrapCharDialog(this).show();

        } else if (id == R.id.m_highlight) {
            new LangListDialog(this).show();

        } else if (id == R.id.m_menu) {
            hideSoftInput();

            mDrawerLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDrawerLayout.openDrawer(GravityCompat.END);
                }
            }, 200);


        } else if (id == R.id.m_save_all) {
            commandEnum = Command.CommandEnum.SAVE;
            Command command = new Command(commandEnum);
            command.args.putBoolean(EditorDelegate.KEY_CLUSTER, true);
            command.object = new SaveListener() {

                @Override
                public void onSaved() {
                    doNextCommand();
                }
            };
            doClusterCommand(command);

        } else if (id == R.id.m_theme) {
            changeTheme();
        } else if (id == R.id.m_fullscreen) {
            boolean fullscreenMode = pref.isFullScreenMode();
            pref.setFullScreenMode(!fullscreenMode);
            UIUtils.toast(this, fullscreenMode
                    ? R.string.disabled_fullscreen_mode_message
                    : R.string.enable_fullscreen_mode_message);

        } else if (id == R.id.m_readonly) {
            pref.setReadOnly(!pref.isReadOnly());
//                mDrawerLayout.setHideBottomDrawer(readOnly);
            doClusterCommand(new Command(Command.CommandEnum.READONLY_MODE));

        } else if (id == R.id.m_encoding) {
            new CharsetsDialog(this).show();

        } else if (id == R.id.m_color) {
            if (ensureNotReadOnly()) {
                insertColorText();
            }

        } else if (id == R.id.m_datetime) {
            if (ensureNotReadOnly()) {
                new InsertDateTimeDialog(this).show();
            }

        } else if (id == R.id.m_run) {
            new RunDialog(this).show();

        } else if (id == R.id.m_settings) {
            RawTextSettingsActivity.startActivity(this, RC_SETTINGS);

        } else if (id == R.id.m_exit) {
            if (tabManager != null)
                tabManager.closeAllTabAndExitApp();

        } else {
            commandEnum = MenuFactory.getInstance(this).idToCommandEnum(id);
            if (commandEnum != Command.CommandEnum.NONE)
                doCommand(new Command(commandEnum));
        }
    }

    private boolean ensureNotReadOnly() {
        boolean readOnly = pref.isReadOnly();
        if (readOnly) {
            UIUtils.toast(this, R.string.readonly_mode_not_support_this_action);
            return false;
        }
        return true;
    }

    private void hideSoftInput() {
        doCommand(new Command(Command.CommandEnum.HIDE_SOFT_INPUT));
    }

    private void showSoftInput() {
        doCommand(new Command(Command.CommandEnum.SHOW_SOFT_INPUT));
    }

    private EditorDelegate getCurrentEditorDelegate() {
        if (tabManager == null || tabManager.getEditorAdapter() == null)
            return null;
        return tabManager.getEditorAdapter().getCurrentEditorDelegate();
    }
    @NonNull
    public String getFile(Intent it) {
        return it.getStringExtra("file");
    }

    /**
     * @return null时为自动检测
     */
    @Nullable
    public String getFileEncoding(Intent it) {
        return it.getStringExtra("encoding");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case RC_OPEN_FILE:
                if (data == null)
                    break;

                openFile(getFile(data), getFileEncoding(data), 0, 0);
                break;
            case RC_SAVE:
                String file = getFile(data);
                String encoding = getFileEncoding(data);
                tabManager.getEditorAdapter().getCurrentEditorDelegate().saveTo(new File(file), encoding);
                break;
            case RC_SETTINGS:
//                if (RawTextSettingsActivity.isTranslateAction(data)) {
//                    new LocalTranslateTask(this).execute();
//                }
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        onMenuClick(item.getItemId());
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mDrawerLayout != null) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    return true;
                }
                if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
                    return true;
                }
            }
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                UIUtils.toast(getContext(), R.string.press_again_will_exit);
                mExitTime = System.currentTimeMillis();
                return true;
            } else {
                return tabManager == null || tabManager.closeAllTabAndExitApp();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * @param menuResId
     * @param status    {@link com.jecelyin.editor.v2.view.menu.MenuDef#STATUS_NORMAL}, {@link com.jecelyin.editor.v2.view.menu.MenuDef#STATUS_DISABLED}
     */
    @Override
    public void setMenuStatus(@IdRes int menuResId, int status) {
        MenuItem menuItem = mToolbar.getMenu().findItem(menuResId);
        if (menuItem == null) {
            throw new RuntimeException("Can't find a menu item");
        }
        boolean enable = status != MenuDef.STATUS_DISABLED;
        if (menuItem.isEnabled() == enable) {
            return;
        }
        Drawable icon = menuItem.getIcon();
        if (!enable) {
            menuItem.setEnabled(false);
            menuItem.setIcon(MenuManager.makeToolbarDisabledIcon(icon));
        } else {
            menuItem.setEnabled(true);
            if (menuItem.getGroupId() == MenuDef.GROUP_TOOLBAR) {
                menuItem.setIcon(MenuManager.makeToolbarNormalIcon(icon));
            } else {
                menuItem.setIcon(MenuManager.makeMenuNormalIcon(icon));
            }
        }
    }

    public void openText(CharSequence content) {
        if (TextUtils.isEmpty(content))
            return;
        tabManager.newTab(content);
    }

    public void openFile(String file) {
        openFile(file, null, 0, 0);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        onMenuClick(R.id.m_menu);
        return false;
    }

    @Override
    public void onFolderSelection(@NonNull FolderChooserDialog dialog, @NonNull File file) {
        if (findFolderCallback != null) {
            findFolderCallback.onFolderSelection(dialog, file);
        }
    }

    @Override
    public void onFolderChooserDismissed(@NonNull FolderChooserDialog dialog) {

    }

    public void setFindFolderCallback(FolderChooserDialog.FolderCallback findFolderCallback) {
        this.findFolderCallback = findFolderCallback;
    }

    /**
     * 需要手动回调 {@link #doNextCommand}
     *
     * @param command
     */
    private void doClusterCommand(Command command) {
        clusterCommand = tabManager.getEditorAdapter().makeClusterCommand();
        clusterCommand.setCommand(command);
        clusterCommand.doNextCommand();
    }

    @Override
    public void closeMenu() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        }
    }

    @Override
    public void doNextCommand() {
        if (clusterCommand == null)
            return;
        clusterCommand.doNextCommand();
    }

    @Override
    public void doCommand(Command command) {
        clusterCommand = null;

        EditorDelegate editorDelegate = getCurrentEditorDelegate();
        if (editorDelegate != null) {
            editorDelegate.doCommand(command);

            if (command.what == Command.CommandEnum.CHANGE_MODE) {
                mToolbar.setTitle(editorDelegate.getToolbarText());
            }
        }
    }

    @Override
    public void startPickPathActivity(String path, String filename, String encoding) {
        ARouter.getInstance().build(RouterHub.FILE_EXPLORER_FileExplorerActivity)
            .withString("destFile", path)
            .withString("filename", filename)
            .withInt("mode", MODE_PICK_PATH)
            .withString("encoding", encoding)
            .navigation(this, RC_SAVE);
    }

    @Override
    public void openFile(String file, String encoding, int line, int column) {
        if (TextUtils.isEmpty(file))
            return;

        if (!tabManager.newTab(new File(file), line, column, encoding))
            return;
        DBHelper.getInstance(this).addRecentFile(file, encoding, line, column);
    }

    public void insertText(CharSequence text) {
        if (text == null)
            return;
        Command c = new Command(Command.CommandEnum.INSERT_TEXT);
        c.object = text;
        doCommand(c);
    }

    @Override
    public TabManager getTabManager() {
        return tabManager;
    }

    @Override
    public String getCurrentLang() {
        EditorDelegate editorDelegate = getCurrentEditorDelegate();
        if (editorDelegate == null)
            return null;

        return editorDelegate.getModeName();
    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return this;
    }

    @Override
    public void setSymbolVisibility(boolean b) {
        if (pref.isReadOnly())
            return;
        mSymbolBarLayout.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    public void showRenderActivity(String markdownText) {
        Toast.makeText(this, markdownText, Toast.LENGTH_SHORT).show();
    }

    public void changeTheme() {
        new ChangeThemeDialog(getContext()).show();
    }

    public void insertColorText() {
        ColorPickerDialogBuilder.with(this)
            .setTitle("插入颜色值")
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .density(12)
            .setPositiveButton(android.R.string.ok, (dialog, selectedColor, allColors) -> {
                String strColor = String.format("#%08X", selectedColor);
                insertText(strColor);
            })
            .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss())
            .showColorEdit(true)
            .setColorEditTextColor(ContextCompat.getColor(this, android.R.color.holo_blue_bright))
            .build().show();
    }

    public int getRenderType() {return 1;}

    @Override
    public void onClick(View v, String text) {
        insertText(text);
    }

    @Override
    public List<MarkdownFormat> getMarkdownFormats() {
        return MarkdownFormat.defaultMarkdownFormats;
    }

    @Override
    public void showTableEditor() {

    }

    @Override
    public void showLinkEditor() {

    }

    @Override
    public void showAttachmentPicker() {

    }

    @Override
    public void redo() {

    }

    @Override
    public void undo() {

    }

    @Override
    public void onClickFormat(MarkdownFormat markdownFormat) {
//        if (markdownFormat == MarkdownFormat.CHECKBOX || markdownFormat == MarkdownFormat.CHECKBOX_OUTLINE) {
//            etContent.addCheckbox("", markdownFormat == MarkdownFormat.CHECKBOX);
//        } else if (markdownFormat == MarkdownFormat.MATH_JAX) {
//            showMarkJaxEditor();
//        } else {
//            etContent.addEffect(markdownFormat);
//        }
    }

    @Override
    public void onClickSetting() {

    }

    @Override
    public void onClickTvSetting() {

    }
}
