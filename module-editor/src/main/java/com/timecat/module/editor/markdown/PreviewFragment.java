package com.timecat.module.editor.markdown;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.FileIOUtils;
import com.timecat.component.alert.ToastUtil;
import com.timecat.component.commonsdk.utils.override.LogUtil;
import com.timecat.component.data.model.events.RenderMarkdownEvent;
import com.timecat.component.readonly.RouterHub;
import com.timecat.component.ui.utils.MenuTintUtils;
import com.timecat.module.editor.R;
import com.timecat.module.editor.R2;
import com.timecat.res.block.RenderEvent;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import my.shouheng.palmmarkdown.MarkdownViewer;
import my.shouheng.palmmarkdown.tools.Callback;
import my.shouheng.palmmarkdown.tools.FileHelper;
import my.shouheng.palmmarkdown.tools.Invoker;
import my.shouheng.palmmarkdown.tools.Message;
import my.shouheng.palmmarkdown.tools.ModelHelper;
import my.shouheng.palmmarkdown.tools.PrintUtils;
import my.shouheng.palmmarkdown.tools.ScreenShotHelper;
import my.shouheng.palmmarkdown.utils.Constants;

import static com.timecat.self.data.base.NoteBodyKt.RENDER_TYPE_MARKDOWN;

@Route(path = RouterHub.EDITOR_MarkdownFragment)
public class PreviewFragment extends BaseEditorFragment {

    @BindView(R2.id.md_view)
    MarkdownViewer mdView;
    @BindView(R2.id.title_et)
    TextView title_et;
    private String content = "";
    private String title = "";

    @Override
    public int getLayoutId() {
        return R.layout.fragment_md_preview;
    }

    @Override
    public void initView() {
        setHasOptionsMenu(true);
        configViews();
    }

    @Subscribe
    public void onRenderEvent(RenderEvent e) {
        if (e.getRenderType() == RENDER_TYPE_MARKDOWN) {
            title = e.getTitle();
            content = e.getContent();
            LogUtil.e("render markdown \ntitle: " + title + "\ncontent: " + content);
            title_et.setText(title);
            mdView.parseMarkdown(content);
        }
    }

    @Subscribe
    public void onContentChangedEvent(RenderMarkdownEvent event) {
        title = event.title;
        content = event.content;
        mdView.parseMarkdown(content);
        title_et.setText(title);
    }

    @Override
    public boolean needEventBus() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.preview_fragment_menu, menu);
        MenuTintUtils.tintAllIcons(menu, context);
        MenuItem searchItem = menu.findItem(R.id.action_find);
        initSearchView((SearchView) searchItem.getActionView());
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.menu_item_export_md) {
            outText(false);

        } else if (i == R.id.menu_item_export_html) {
            outHtml(false);

        } else if (i == R.id.menu_item_export_jpg) {
            createWebCapture(mdView, file -> ToastUtil
                    .i(String.format(getString(R.string.text_file_saved_to), file.getPath())));

        } else if (i == R.id.menu_item_export_pdf) {
            PrintUtils.print(getContext(), mdView, title);

        } else if (i == R.id.menu_item_share_md) {
            ModelHelper.share(getContext(), title, content, new ArrayList<>());

        } else if (i == R.id.menu_item_share_html) {
            outHtml(true);

        } else if (i == R.id.menu_item_share_jpg) {
            createWebCapture(mdView,
                    file -> ModelHelper.shareFile(getContext(), file, Constants.MIME_TYPE_IMAGE));

        }
        return super.onOptionsItemSelected(item);
    }

    private void configViews() {
        if (Build.VERSION.SDK_INT >= 21) {
            WebView.enableSlowWholeDocumentDraw();
        }
        mdView.getFastScrollDelegate()
                .setThumbDrawable(getResources().getDrawable(R.drawable.fast_scroll_bar_dark));
        mdView.getFastScrollDelegate().setThumbSize(16, 40);
        mdView.getFastScrollDelegate().setThumbDynamicHeight(false);
        mdView.setHtmlResource(false);
//        mdView.parseMarkdown(collection.getContent());TODO
//        mdView.setOnImageClickedListener((url, urls) -> {
//            List<Attachment> attachments = new ArrayList<>();
//            Attachment clickedAttachment = null;
//            for (String u : urls) {
//                Attachment attachment = getAttachmentFormUrl(u);
//                attachments.add(attachment);
//                if (u.equals(url)) clickedAttachment = attachment;
//            }
//            AttachmentHelper.resolveClickEvent(getContext(), clickedAttachment, attachments, collection.getTitle());
//        });
//        mdView.setOnAttachmentClickedListener(url -> {
//            if (!TextUtils.isEmpty(url)){
//                Uri uri = Uri.parse(url);
//
//                // Open the http or https link from chrome tab.
//                if (SCHEME_HTTPS.equalsIgnoreCase(uri.getScheme())
//                        || SCHEME_HTTP.equalsIgnoreCase(uri.getScheme())) {
//                    IntentUtils.openWebPage(getContext(), url);
//                    return;
//                }
//
//                // Open the files of given format.
//                if (url.endsWith(_3GP) || url.endsWith(_MP4)) {
//                    startActivity(uri, VIDEO_MIME_TYPE);
//                } else if (url.endsWith(_PDF)) {
//                    startActivity(uri, PDF_MIME_TYPE);
//                } else {
//                    OpenResolver.newInstance(mimeType ->
//                                    startActivity(uri, mimeType.mimeType)
//                                            ).show(getFragmentManager(), "OPEN RESOLVER");
//                }
//            }
//        });
    }

    private void outHtml(boolean isShare) {
        mdView.outHtml(html -> {
            File exDir = FileHelper.getHtmlExportDir();
            File outFile = new File(exDir,
                    FileHelper.getDefaultFileName(Constants.EXPORTED_HTML_EXTENSION));
            FileIOUtils.writeFileFromString(outFile, html);
            if (isShare) {
                // Share, do share option
                ModelHelper.shareFile(getContext(), outFile, Constants.MIME_TYPE_HTML);
            } else {
                // Not share, just show a message
                ToastUtil.i(String.format(getString(R.string.text_file_saved_to), outFile.getPath()));
            }
        });
    }

    private void outText(boolean isShare) {
        File exDir = FileHelper.getTextExportDir();
        File outFile = new File(exDir,
                FileHelper.getDefaultFileName(Constants.EXPORTED_TEXT_EXTENSION));
        FileIOUtils.writeFileFromString(outFile, content);
        if (isShare) {
            // Share, do share option
            ModelHelper.shareFile(getContext(), outFile, Constants.MIME_TYPE_FILES);
        } else {
            // Not share, just show a message
            ToastUtil.i(String.format(getString(R.string.text_file_saved_to), outFile.getPath()));
        }
    }

    private void initSearchView(SearchView searchView) {
        if (searchView != null) {
            searchView.setQueryHint(getString(R.string.text_find_in_page));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    mdView.findAllAsync(query);
                    ((AppCompatActivity) getActivity()).startSupportActionMode(new ActionModeCallback());
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
    }

    // region Capture
    protected void createScreenCapture(final RecyclerView recyclerView) {
        if (recyclerView.getAdapter() == null || recyclerView.getAdapter().getItemCount() == 0) {
            ToastUtil.w(R.string.empty_list_to_capture);
            return;
        }
        if (getActivity() == null) {
            return;
        }
        doCapture(recyclerView);
    }

    protected void createScreenCapture(final RecyclerView recyclerView, final int itemHeight) {
        if (recyclerView.getAdapter() == null || recyclerView.getAdapter().getItemCount() == 0) {
            ToastUtil.w(R.string.empty_list_to_capture);
            return;
        }
        if (getActivity() == null) {
            return;
        }
        doCapture(recyclerView, itemHeight);
    }

    protected void createWebCapture(WebView webView, FileHelper.OnSavedToGalleryListener listener) {
        assert getActivity() != null;
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle(R.string.capturing);
        pd.setCancelable(false);
        pd.show();

        new Handler().postDelayed(() -> doCapture(webView, pd, listener), 500);
    }

    protected void onGetScreenCutFile(File file) {
    }

    private void doCapture(RecyclerView recyclerView) {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage(getString(R.string.capturing));
        new Invoker<>(new Callback<File>() {
            @Override
            public void onBefore() {
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            public Message<File> onRun() {
                Message<File> message = new Message<>();
                Bitmap bitmap = ScreenShotHelper.shotRecyclerView(recyclerView);
                boolean succeed = FileHelper
                        .saveImageToGallery(getContext(), bitmap, true, message::setObj);
                message.setSucceed(succeed);
                return message;
            }

            @Override
            public void onAfter(Message<File> message) {
                pd.dismiss();
                if (message.isSucceed()) {
                    ToastUtil
                            .i(String.format(getString(R.string.text_file_saved_to), message.getObj().getPath()));
                    onGetScreenCutFile(message.getObj());
                } else {
                    ToastUtil.e(R.string.failed_to_create_file);
                }
            }
        }).start();
    }

    private void doCapture(RecyclerView recyclerView, int itemHeight) {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle(R.string.capturing);
        new Invoker<>(new Callback<File>() {
            @Override
            public void onBefore() {
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            public Message<File> onRun() {
                Message<File> message = new Message<>();
                Bitmap bitmap = ScreenShotHelper.shotRecyclerView(recyclerView, itemHeight);
                boolean succeed = FileHelper
                        .saveImageToGallery(getContext(), bitmap, true, message::setObj);
                message.setSucceed(succeed);
                return message;
            }

            @Override
            public void onAfter(Message<File> message) {
                pd.dismiss();
                if (message.isSucceed()) {
                    ToastUtil
                            .i(String.format(getString(R.string.text_file_saved_to), message.getObj().getPath()));
                    onGetScreenCutFile(message.getObj());
                } else {
                    ToastUtil.e(R.string.failed_to_create_file);
                }
            }
        }).start();
    }

    private void doCapture(WebView webView, ProgressDialog pd,
                           FileHelper.OnSavedToGalleryListener listener) {
        ScreenShotHelper.shotWebView(context, webView, listener);
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(R.menu.note_find_action, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            int i = menuItem.getItemId();
            if (i == R.id.action_close) {
                actionMode.finish();

            } else if (i == R.id.action_next) {
                mdView.findNext(true);

            } else if (i == R.id.action_last) {
                mdView.findNext(false);

            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mdView.clearMatches();
        }
    }
    // endregion
}
