package com.jecelyin.android.file_explorer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jecelyin.android.file_explorer.adapter.FileListPagerAdapter;
import com.jecelyin.android.file_explorer.io.JecFile;
import com.jecelyin.android.file_explorer.io.LocalFile;
import com.jecelyin.android.file_explorer.listener.OnClipboardDataChangedListener;
import com.jecelyin.android.file_explorer.util.FileListSorter;
import com.jecelyin.android.file_explorer.widget.FileNameEditText;
import com.jecelyin.editor.v2.FullScreenActivity;
import com.jecelyin.editor.v2.Pref;
import com.timecat.component.ui.utils.MenuTintUtils;
import com.timecat.component.alert.UIUtils;
import com.timecat.component.commonsdk.utils.utils.IOUtils;
import com.timecat.component.commonsdk.utils.utils.SysUtils;
import com.timecat.component.commonsdk.utils.utils.command.ShellDaemon;
import com.timecat.component.readonly.RouterHub;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
@Route(path = RouterHub.FILE_EXPLORER_FileExplorerActivity)
public class FileExplorerActivity extends FullScreenActivity implements View.OnClickListener,
    OnClipboardDataChangedListener {

  private FileListPagerAdapter adapter;
  private static final int MODE_PICK_FILE = 1;
  private static final int MODE_PICK_PATH = 2;
  @Autowired
  int mode =MODE_PICK_FILE;
  @Autowired
  String destPath;
  @Autowired
  String filename;
  @Autowired
  String encoding;
  private String fileEncoding = null;
  private String lastPath;
  private FileClipboard fileClipboard;
  private MenuItem pasteMenu;
  FileNameEditText filenameEditText;
  TextView fileEncodingTextView;

  public static void startPickFileActivity(Activity activity, String destFile, int requestCode) {
    Intent it = new Intent(activity, FileExplorerActivity.class);
    it.putExtra("mode", MODE_PICK_FILE);
    it.putExtra("dest_file", destFile);
    activity.startActivityForResult(it, requestCode);
  }

  public static void startPickPathActivity(Activity activity, String destFile, String filename,
      String encoding, int requestCode) {
    Intent it = new Intent(activity, FileExplorerActivity.class);
    it.putExtra("mode", MODE_PICK_PATH);
    it.putExtra("dest_file", destFile);
    it.putExtra("filename", filename);
    it.putExtra("encoding", encoding);
    activity.startActivityForResult(it, requestCode);
  }

  @NonNull
  public static String getFile(Intent it) {
    return it.getStringExtra("file");
  }

  /**
   * @return null时为自动检测
   */
  @Nullable
  public static String getFileEncoding(Intent it) {
    return it.getStringExtra("encoding");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    ShellDaemon.getShell().reset();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ARouter.getInstance().inject(this);
    setContentView(R.layout.file_explorer_activity);

    Toolbar toolbar = findViewById(R.id.toolbar);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle(mode == MODE_PICK_FILE ? R.string.open_file : R.string.save_file);
    lastPath = Pref.getInstance(this).getLastOpenPath();
    if (TextUtils.isEmpty(lastPath)) {
      lastPath = SysUtils.getInternalStorageDirectory();
    }

    filenameEditText = findViewById(R.id.filename_editText);
    if (!TextUtils.isEmpty(destPath)) {
      File dest = new File(destPath);
      lastPath = dest.isFile() ? dest.getParent() : dest.getPath();
      filenameEditText.setText(dest.getName());
    } else {
      filenameEditText.setText(
          TextUtils.isEmpty(filename) ? getString(R.string.untitled_file_name) : filename + ".txt");
    }

    fileEncodingTextView = findViewById(R.id.file_encoding_textView);
    TextView saveBtn = findViewById(R.id.save_btn);
    initPager();
    saveBtn.setOnClickListener(this);
    fileEncodingTextView.setOnClickListener(this);

    fileEncoding = encoding;
    if (TextUtils.isEmpty(encoding)) {
      encoding = getString(R.string.auto_detection_encoding);
    }
    fileEncodingTextView.setText(encoding);
    LinearLayout filenameLayout = findViewById(R.id.filename_layout);

    filenameLayout.setVisibility(mode == MODE_PICK_FILE ? View.GONE : View.VISIBLE);

    getFileClipboard().setOnClipboardDataChangedListener(this);
  }

  private void initPager() {
    adapter = new FileListPagerAdapter(getSupportFragmentManager());

    JecFile path = new LocalFile(lastPath);
    adapter.addPath(path);
    ViewPager viewPager = findViewById(R.id.viewPager);

    viewPager.setAdapter(adapter);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.explorer_menu, menu);
    MenuTintUtils.tintAllIcons(menu, this);

    Pref pref = Pref.getInstance(this);
    menu.findItem(R.id.show_hidden_files_menu).setChecked(pref.isShowHiddenFiles());
    pasteMenu = menu.findItem(R.id.paste_menu);

    int sortId;
    switch (pref.getFileSortType()) {
      case FileListSorter.SORT_DATE:
        sortId = R.id.sort_by_datetime_menu;
        break;
      case FileListSorter.SORT_SIZE:
        sortId = R.id.sort_by_size_menu;
        break;
      case FileListSorter.SORT_TYPE:
        sortId = R.id.sort_by_type_menu;
        break;
      default:
        sortId = R.id.sort_by_name_menu;
        break;
    }

    menu.findItem(sortId).setChecked(true);

    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Pref pref = Pref.getInstance(this);
    int id = item.getItemId();
    if (id == R.id.show_hidden_files_menu) {
      item.setChecked(!item.isChecked());
      pref.setShowHiddenFiles(item.isChecked());
    } else if (id == R.id.sort_by_name_menu) {
      item.setChecked(true);
      pref.setFileSortType(FileListSorter.SORT_NAME);
    } else if (id == R.id.sort_by_datetime_menu) {
      item.setChecked(true);
      pref.setFileSortType(FileListSorter.SORT_DATE);
    } else if (id == R.id.sort_by_size_menu) {
      item.setChecked(true);
      pref.setFileSortType(FileListSorter.SORT_SIZE);
    } else if (id == R.id.sort_by_type_menu) {
      item.setChecked(true);
      pref.setFileSortType(FileListSorter.SORT_TYPE);
    }
    return super.onOptionsItemSelected(item);
  }

  boolean onSelectFile(JecFile file) {
    if (file.isFile()) {
      if (mode == MODE_PICK_FILE) {
        Intent it = new Intent();
        it.putExtra("file", file.getPath());
        it.putExtra("encoding", fileEncoding);
        setResult(RESULT_OK, it);
        finish();
      } else {
        filenameEditText.setText(file.getName());
      }

      return true;
    } else if (file.isDirectory()) {
      lastPath = file.getPath();
    }

    return false;
  }

  @Override
  public void onClick(View v) {
    int id = v.getId();
    if (id == R.id.save_btn) {
      onSave();
    } else if (id == R.id.file_encoding_textView) {
      onShowEncodingList();
    }
  }

  private void onShowEncodingList() {
    SortedMap m = Charset.availableCharsets();
    Set k = m.keySet();

    int selected = 0;
    String[] names = new String[m.size() + 1];
    names[0] = getString(R.string.auto_detection_encoding);
    Iterator iterator = k.iterator();
    int i = 1;
    while (iterator.hasNext()) {
      String n = (String) iterator.next();
      if (n.equals(fileEncoding)) {
        selected = i;
      }
      names[i++] = n;
    }

    try {
      new MaterialDialog.Builder(this)
          .items(names)
          .itemsCallbackSingleChoice(selected, new MaterialDialog.ListCallbackSingleChoice() {
            @Override
            public boolean onSelection(MaterialDialog materialDialog, View view, int i,
                CharSequence charSequence) {
              fileEncodingTextView.setText(charSequence);
              if (i > 0) {
                fileEncoding = charSequence.toString();
              }
              return true;
            }
          })
          .show();
    } catch (Exception e) {
      // android.view.WindowLeaked: Activity com.jecelyin.android.file_explorer.FileExplorerActivity has leaked window com.android.internal.policy.impl.PhoneWindow$DecorView{65b370e8 V.E..... R.....ID 0,0-684,1280} that was originally added here
    }
  }

  private void onSave() {
    String fileName = filenameEditText.getText().toString().trim();
    if (TextUtils.isEmpty(fileName)) {
      filenameEditText.setError(getString(R.string.can_not_be_empty));
      return;
    }
    if (IOUtils.isInvalidFilename(fileName)) {
      filenameEditText.setError(getString(R.string.illegal_filename));
      return;
    }
    if (TextUtils.isEmpty(lastPath)) {
      filenameEditText.setError(getString(R.string.unknown_path));
      return;
    }

    File f = new File(lastPath);
    if (f.isFile()) {
      f = f.getParentFile();
    }

    final File newFile = new File(f, fileName);
    if (newFile.exists()) {
      UIUtils.showConfirmDialog(getContext(), getString(R.string.override_file_prompt, fileName),
          new UIUtils.OnClickCallback() {
            @Override
            public void onOkClick() {
              saveAndFinish(newFile);
            }
          });
    } else {
      saveAndFinish(newFile);
    }
  }

  private void saveAndFinish(File file) {
    Intent it = new Intent();
    it.putExtra("file", file.getPath());
    it.putExtra("encoding", fileEncoding);
    setResult(RESULT_OK, it);
    finish();
  }

  public FileClipboard getFileClipboard() {
    if (fileClipboard == null) {
      fileClipboard = new FileClipboard();
    }

    return fileClipboard;
  }

  @Override
  public void onClipboardDataChanged() {
    if (pasteMenu == null) {
      return;
    }

    pasteMenu.setVisible(getFileClipboard().canPaste());
  }
}
