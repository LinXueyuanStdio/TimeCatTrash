package com.jecelyin.editor.v2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.jecelyin.editor.v2.common.Command;
import com.jecelyin.editor.v2.ui.EditorActivity;
import com.jecelyin.editor.v2.ui.EditorDelegate;
import com.jecelyin.editor.v2.ui.dialog.ChangeThemeDialog;
import com.jecelyin.editor.v2.view.menu.MenuFactory;
import com.jecelyin.editor.v2.widget.text.JsCallback;
import com.jecelyin.editor.v2.widget.text.OnTextChangeListener;
import com.timecat.component.alert.ToastUtil;
import com.timecat.component.commonsdk.utils.override.LogUtil;
import com.timecat.component.commonsdk.utils.string.StringUtil;
import com.timecat.component.commonsdk.utils.string.TimeUtil;
import com.timecat.component.commonsdk.utils.utils.SysUtils;
import com.timecat.component.readonly.RouterHub;
import com.timecat.component.data.database.DB;
import com.timecat.component.setting.DEF;
import com.timecat.component.data.model.DBModel.DBNote;
import com.timecat.component.data.model.DBModel.DBUser;
import com.timecat.component.data.model.events.RenderMarkdownEvent;
import com.timecat.component.data.service.WindowService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import my.shouheng.palmmarkdown.MenuSortActivity;
import my.shouheng.palmmarkdown.dialog.AttachmentPickerDialog;
import my.shouheng.palmmarkdown.dialog.LinkInputDialog;
import my.shouheng.palmmarkdown.dialog.MathJaxEditor;
import my.shouheng.palmmarkdown.dialog.TableInputDialog;
import my.shouheng.palmmarkdown.tools.AttachmentHelper;
import my.shouheng.palmmarkdown.tools.FileHelper;
import my.shouheng.palmmarkdown.tools.MarkdownFormat;
import my.shouheng.palmmarkdown.tools.RequestCodes;
import my.shouheng.palmmarkdown.utils.StringUtils;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/2/21
 * @description null
 * @usage null
 */
@Route(path = RouterHub.EDITOR_RawTextEditorActivity)
public class RawTextEditorActivity extends EditorActivity implements OnTextChangeListener {

  String title;
  String content;
  @Autowired
  Long id = -1L;
  DBNote note_toUpdate;
  DBNote new_note;
  private boolean updateNote = false;
  private final int REQ_MENU_SORT = 0x0101;
  private boolean textChange;
  private Disposable mDisposable;
  @Nullable
  @Autowired(name = RouterHub.GLOBAL_WindowServiceImpl)
  WindowService windowService;

  @Override
  public int getLayoutRes() {
    return R.layout.activity_editor_rawtext;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    ARouter.getInstance().inject(this);
    super.onCreate(savedInstanceState);
    if (id != -1) {
      note_toUpdate = DB.notes().findById(id);
      if (note_toUpdate == null) {
        ToastUtil.e("惊了！找不到对象！");
        LogUtil.e("惊了！找不到对象！");
        finish();
        return;
      }
    }
    final String version = SysUtils.getVersionName(this);
    mVersionTextView.setText("纯文本编辑器" + version);
    mToolbar.postDelayed(() -> {
      findViewById(R.id.header_rl).setLayoutParams(new LinearLayout.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT, mToolbar.getHeight()));
    }, 50);
    mToolbar.postDelayed(() -> {
      if (getTabManager() != null
          && getTabManager().getEditorAdapter() != null
          && getTabManager().getEditorAdapter().getCurrentEditorDelegate() != null
          && getTabManager().getEditorAdapter().getCurrentEditorDelegate().mEditText != null) {
        getTabManager()
            .getEditorAdapter()
            .getCurrentEditorDelegate()
            .mEditText
            .addTextChangedListener(RawTextEditorActivity.this);
      }
    }, 3 * 1000);
    if (note_toUpdate != null) {
      refreshViewByNote(note_toUpdate);
    } else {
      new_note = new DBNote();
      new_note.setTitle(getString(R.string.new_filename));
      new_note.setContent("# " + getString(R.string.new_filename) + "\n\n");
    }
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
        .subscribeOn(Schedulers.io())
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

  @Override
  protected void onDestroy() {
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

  @Override
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void showRenderActivity(String markdownText) {
    if (windowService != null) {
      windowService.showMarkdownRenderApp(markdownText);
    }
    mToolbar
        .postDelayed(() -> EventBus.getDefault().post(new RenderMarkdownEvent(markdownText)), 256);
  }

  @Override
  public void changeTheme() {
    new ChangeThemeDialog(getContext()).show();
  }

  @Override
  public FragmentActivity getFragmentActivity() {
    return this;
  }

  @Override
  public int getRenderType() {
    return 1;
  }

  @Override
  public void setSymbolVisibility(boolean b) {
    super.setSymbolVisibility(b);
  }

  private void refreshViewByNote(DBNote note) {
    updateNote = true;
    openText(note.getContent());
    mToolbar.postDelayed(() -> {
      EditorDelegate editorDelegate = getTabManager().getEditorAdapter().getCurrentEditorDelegate();
      if (editorDelegate != null) {
        editorDelegate.setReadNote(true);
        editorDelegate.setTitle(note.getTitle());
      }
    }, 256);

  }

  private void onUpdateNote() {
    if (!checkTittleAndContent(title, content)) {
      return;
    }
    note_toUpdate.setRawtext(true);
    note_toUpdate.setRender_type(DBNote.TYPE_MARKDOWN);
    note_toUpdate.setTitle(title);
    note_toUpdate.setContent(content);
    note_toUpdate.setUpdate_datetime(TimeUtil.formatGMTDate(new Date()));
    note_toUpdate.setUser(DB.users().getActive());
    DB.notes().updateAndFireEvent(note_toUpdate);
  }

  private void onCreateNote() {
    if (!checkTittleAndContent(title, content)) {
      return;
    }
    if (new_note == null) {
      new_note = new DBNote();
    }
    DBUser activeUser = DB.users().getActive();
    new_note.setRawtext(true);
    new_note.setRender_type(DBNote.TYPE_MARKDOWN);
    new_note.setTitle(title);
    new_note.setContent(content);
    new_note.setCreated_datetime(TimeUtil.formatGMTDate(new Date()));
    new_note.setUpdate_datetime(new_note.getCreated_datetime());
    List<Integer> CardStackViewDataList = new ArrayList<>();
    int[] CardStackViewData = getResources().getIntArray(R.array.card_stack_view_data);
    for (int aCardStackViewData : CardStackViewData) {
      CardStackViewDataList.add(aCardStackViewData);
    }
    Random random = new Random();
    int randomColor = random.nextInt(CardStackViewDataList.size());
    new_note.setColor(CardStackViewDataList.get(randomColor));
    new_note.setUser(activeUser);

    DB.notes().safeSaveDBNoteAndFireEvent(new_note);
  }

  @Override
  public void onTextChanged() {
    textChange = true;
    EditorDelegate editorDelegate = getTabManager().getEditorAdapter().getCurrentEditorDelegate();
    if (editorDelegate != null) {
      mToolbar.post(() -> {
        editorDelegate.getText(new JsCallback<String>() {
          @Override
          public void onCallback(String text) {
            content = text;
          }
        });
        editorDelegate.mEditText.getLineText(0, 50, new JsCallback<String>() {
          @Override
          public void onCallback(String data) {
            title = data;
          }
        });
        editorDelegate.setWordCount(content == null ? 0 : content.length());
        editorDelegate.setTitle(title);
        mToolbar.setTitle(editorDelegate.getToolbarText());
        getTabManager().onDocumentChanged(getTabManager().getCurrentTab());
      });
      mToolbar.postDelayed(() -> {
        editorDelegate.getText(new JsCallback<String>() {
          @Override
          public void onCallback(String text) {
            content = text;
          }
        });
        editorDelegate.mEditText.getLineText(0, 50, new JsCallback<String>() {
          @Override
          public void onCallback(String data) {
            title = data;
          }
        });
        editorDelegate.setWordCount(content == null ? 0 : content.length());
        editorDelegate.setTitle(title);
        mToolbar.setTitle(editorDelegate.getToolbarText());
        getTabManager().onDocumentChanged(getTabManager().getCurrentTab());
      }, 50);
    }
  }

  private void autoSaveToTimeCat() {
    if (updateNote) {
      onUpdateNote();
    } else {
      onCreateNote();
    }
  }

  private boolean checkTittleAndContent(String title, String content) {
    if (title == null || title.length() <= 0) {
      ToastUtil.e("标题太短，无法保存\n（默认第一行为标题）");
      return false;
    }

    if (content == null || content.length() <= 0) {
      ToastUtil.e("内容太短，无法保存\n（默认第一行为标题）");
      return false;
    }
    return true;
  }

  @Override
  public void finish() {
    LogUtil.i("save before finish 已保存");
    autoSaveToTimeCat();
    super.finish();
  }


  @Override
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

  public void addImageLink() {
    LinkInputDialog.getInstance((title, imgUri) -> {
      imgUri = TextUtils.isEmpty(imgUri) ? "" : imgUri;
      String result = "\n![" + title + "](" + imgUri + ")\n";
      insertText(result);
    })
        .show(Objects.requireNonNull(getSupportFragmentManager()), "Link Image");

  }

  public void showMarkJaxEditor() {
    MathJaxEditor.newInstance((exp, isSingleLine) -> {
      if (isSingleLine) {
        String result = "\n\n$$ " + exp + " $$\n\n";
        insertText(result);
      } else {
        String result = " $ " + exp + " $ ";
        insertText(result);
      }
    })
        .show(Objects.requireNonNull(getSupportFragmentManager()), "MATH JAX EDITOR");
  }

  @Override
  public void showTableEditor() {
    LogUtil.w("click");
    TableInputDialog.getInstance((rowsStr, colsStr) -> {
      int rows = StringUtils.parseInteger(rowsStr, 3);
      int cols = StringUtils.parseInteger(colsStr, 3);
      StringBuilder sb = new StringBuilder();
      int i;
      sb.append("\n\n");
      sb.append("|");
      for (i = 0; i < cols; i++) {
        sb.append(" HEADER |");
      }

      sb.append("\n|");
      for (i = 0; i < cols; i++) {
        sb.append(":----------:|");
      }

      sb.append("\n");
      for (int i2 = 0; i2 < rows; i2++) {
        sb.append("|");
        for (i = 0; i < cols; i++) {
          sb.append("            |");
        }
        sb.append("\n");
      }
      sb.append("\n\n");

      String result = sb.toString();
      insertText(result);
    }).show(Objects.requireNonNull(getSupportFragmentManager()), "TABLE INPUT");
  }

  @Override
  public void showLinkEditor() {
    LogUtil.w("click");
    LinkInputDialog.getInstance((title, link) -> {
      String result = title == null ?
          (link == null ? "[]()" : "[](" + link + ")") :
          (link == null ? "[" + title + "]()" : "[" + title + "](" + link + ")");
      insertText(result);
    })
        .show(Objects.requireNonNull(getSupportFragmentManager()), "LINK INPUT");
  }

  @Override
  public void showAttachmentPicker() {
    new AttachmentPickerDialog.Builder()
        .setRecordVisible(false)
        .setVideoVisible(false)
        .setAddLinkVisible(true)
        .setFilesVisible(true)
        .setOnAddNetUriSelectedListener(this::addImageLink)
        .build().show(Objects.requireNonNull(getSupportFragmentManager()), "Attachment picker");

  }

  @Override
  public void redo() {
    Command.CommandEnum commandEnum = MenuFactory.getInstance(this).idToCommandEnum(R.id.m_redo);
    doCommand(new Command(commandEnum));
  }

  @Override
  public void undo() {
    LogUtil.w("undo");
    Command.CommandEnum commandEnum = MenuFactory.getInstance(this).idToCommandEnum(R.id.m_undo);
    doCommand(new Command(commandEnum));
  }

  @Override
  public void onClickFormat(MarkdownFormat markdownFormat) {
    String result;
    if (markdownFormat == MarkdownFormat.CHECKBOX
        || markdownFormat == MarkdownFormat.CHECKBOX_OUTLINE) {
//            getTabManager().getEditorAdapter().getCurrentEditorDelegate().mEditText.select
//            etContent.addCheckbox("", markdownFormat == MarkdownFormat.CHECKBOX);
      result = "- [ ] ";
      insertText(result);
    } else if (markdownFormat == MarkdownFormat.MATH_JAX) {
      showMarkJaxEditor();
    } else {
      if (MarkdownFormat.H1 == markdownFormat) {
        result = "# ";
      } else if (MarkdownFormat.H2 == markdownFormat) {
        result = "## ";
      } else if (MarkdownFormat.H3 == markdownFormat) {
        result = "### ";
      } else if (MarkdownFormat.H4 == markdownFormat) {
        result = "#### ";
      } else if (MarkdownFormat.H5 == markdownFormat) {
        result = "##### ";
      } else if (MarkdownFormat.H6 == markdownFormat) {
        result = "###### ";
      } else if (MarkdownFormat.INDENT == markdownFormat) {
        result = "    ";
      } else if (MarkdownFormat.DEDENT == markdownFormat) {
        result = "";
      } else if (MarkdownFormat.QUOTE == markdownFormat) {
        result = "> ";
      } else if (MarkdownFormat.BOLD == markdownFormat) {
        result = "**";
      } else if (MarkdownFormat.ITALIC == markdownFormat) {
        result = "";
      } else if (MarkdownFormat.CODE_BLOCK == markdownFormat) {
        result = "```\n\n```";
      } else if (MarkdownFormat.STRIKE == markdownFormat) {
        result = "";
      } else if (MarkdownFormat.HORIZONTAL_LINE == markdownFormat) {
        result = "-------";
      } else if (MarkdownFormat.XML == markdownFormat) {
        result = "";
      } else if (MarkdownFormat.LINK == markdownFormat) {
        result = "";
      } else if (MarkdownFormat.TABLE == markdownFormat) {
        result = "";
      } else if (MarkdownFormat.NORMAL_LIST == markdownFormat) {
        result = "- ";
      } else if (MarkdownFormat.NUMBER_LIST == markdownFormat) {
        result = "1. ";
      } else if (MarkdownFormat.SUB_SCRIPT == markdownFormat) {
        result = "";
      } else if (MarkdownFormat.SUPER_SCRIPT == markdownFormat) {
        result = "";
      } else if (MarkdownFormat.MARK == markdownFormat) {
        result = "";
      } else {
        result = "";
      }
      if (!StringUtil.isEmpty(result)) {
        insertText(result);
      }
    }
  }

  @Override
  public void onClickSetting() {
    Intent intent = new Intent(this, MenuSortActivity.class);
    startActivityForResult(intent, REQ_MENU_SORT);
  }

  @Override
  public void onClickTvSetting() {
    new MaterialDialog.Builder(this)
        .title(R.string.custom_symbol_list)
        .positiveText(R.string.confirm)
        .input("", Pref.getInstance(this).getSymbol(), false, new MaterialDialog.InputCallback() {
          @Override
          public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
            String[] strings = TextUtils.split(input.toString(), "\n");
            StringBuilder sb = new StringBuilder();
            for (String string : strings) {
              string = string.trim();
              if (string.isEmpty()) {
                continue;
              }
              if (sb.length() > 0) {
                sb.append("\n");
              }
              sb.append(string);
            }
            String result = sb.toString();
            Pref.getInstance(RawTextEditorActivity.this).setSymbol(result);
            if (mSymbolBarLayout != null) {
              mSymbolBarLayout.addBottomMenusTv();
            }
          }
        })
        .show();

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == Activity.RESULT_OK) {
      String title;
      Uri uri;
      String result;
      switch (requestCode) {
        case REQ_MENU_SORT:
          if (mSymbolBarLayout != null) {
            mSymbolBarLayout.addBottomMenus();
          }
          break;
        case RequestCodes.REQUEST_TAKE_PHOTO:
//                    if (shouldCompressImage()) {
//                        AttachmentHelper.compressImage(context, new File(imgPath), onCompressListener);TODO 是否压缩
//                    } else {
//                        return
//                    }
          String imgPath = AttachmentHelper.getFilePath();
          uri = FileHelper.getUriFromFile(this, new File(imgPath));
          String imgUri = uri.toString();
          title = FileHelper.getNameFromUri(this, uri);
          imgUri = TextUtils.isEmpty(imgUri) ? "" : imgUri;

          result = "\n\n![" + title + "](" + imgUri + ")\n\n";
          insertText(result);
          break;
        case RequestCodes.REQUEST_SELECT_IMAGE:
        case RequestCodes.REQUEST_FILES:
          for (Uri u : AttachmentHelper.getUrisFromIntent(data)) {
            uri = FileHelper.getUriFromFile(this, FileHelper.createAttachmentFromUri(this, u));
            String link = uri.toString();
            title = FileHelper.getNameFromUri(this, u);
            result = title == null ?
                (link == null ? "[]()" : "[](" + link + ")") :
                (link == null ? "[" + title + "]()" : "[" + title + "](" + link + ")");
            insertText(result);
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
}

