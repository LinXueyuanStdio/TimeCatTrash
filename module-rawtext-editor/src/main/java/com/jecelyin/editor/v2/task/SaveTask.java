package com.jecelyin.editor.v2.task;

import android.content.Context;

import com.timecat.component.commonsdk.utils.override.L;
import com.timecat.component.alert.UIUtils;
import com.jecelyin.editor.v2.Pref;
import com.jecelyin.editor.v2.R;
import com.jecelyin.editor.v2.common.SaveListener;
import com.jecelyin.editor.v2.io.FileWriter;
import com.jecelyin.editor.v2.ui.Document;
import com.jecelyin.editor.v2.ui.EditorDelegate;
import com.jecelyin.editor.v2.widget.text.JsCallback;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class SaveTask {
    private final WeakReference<Context> contextWR;
    private final WeakReference<EditorDelegate> editorDelegateWR;
    private final WeakReference<Document> documentWR;
    private boolean writing = false;
    private boolean isCluster;

    public SaveTask(Context context, EditorDelegate editorDelegate, Document document) {
        this.contextWR = new WeakReference<Context>(context);
        this.editorDelegateWR = new WeakReference<EditorDelegate>(editorDelegate);
        this.documentWR = new WeakReference<Document>(document);
    }

    public boolean isWriting() {
        return writing;
    }

    public void save(boolean isCluster, SaveListener listener) {
        if(writing)
            return;

        Document document = documentWR.get();
        EditorDelegate editorDelegate = editorDelegateWR.get();
        if (document == null || editorDelegate == null)
            return;
        if(!document.isChanged()) {
//            if(!isCluster)
//                UIUtils.toast(context, R.string.no_change);
            return;
        }
        this.isCluster = isCluster;
        File file = document.getFile();
        if(file == null) {
            editorDelegate.startSaveFileSelectorActivity();
            return;
        }
        saveTo(file, document.getEncoding(), listener);
    }

    public void saveTo(final File file, final String encoding) {
        saveTo(file, encoding, null);
    }

    /**
     *
     * @param file 如果是Root处理，保存成功后要回写到原始文件
     * @param encoding
     * @param listener
     */
    private void saveTo(final File file, final String encoding, final SaveListener listener) {
        if (editorDelegateWR.get() == null || contextWR.get() == null || documentWR.get() == null || file == null)
            return;
        boolean root = documentWR.get().isRoot();
        writing = true;
        final FileWriter fileWriter = new FileWriter(root, file, encoding, Pref.getInstance(contextWR.get()).isKeepBackupFile());
        fileWriter.setFileWriteListener(new FileWriter.FileWriteListener() {
            @Override
            public void onSuccess() {
                writing = false;

                if (documentWR.get() == null || contextWR.get() == null || editorDelegateWR.get() == null)
                    return;
                documentWR.get().onSaveSuccess(file, encoding);
                if(!isCluster) {
                    UIUtils.toast(contextWR.get(), R.string.save_success);
                } else {
                    editorDelegateWR.get().getMainActivity().doNextCommand();
                }
                if(listener != null)
                    listener.onSaved();
            }

            @Override
            public void onError(Exception e) {
                writing = false;
                L.e(e);
                if (contextWR.get() != null)
                    UIUtils.alert(contextWR.get(), e.getMessage());
            }
        });
        editorDelegateWR.get().getText(new JsCallback<String>() {
            @Override
            public void onCallback(String data) {
                fileWriter.write(data);
            }
        });

    }
}
