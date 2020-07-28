package com.jecelyin.android.file_explorer;

import android.content.Context;
import android.text.TextUtils;

import com.jecelyin.android.file_explorer.io.JecFile;
import com.jecelyin.android.file_explorer.listener.OnClipboardDataChangedListener;
import com.jecelyin.android.file_explorer.listener.OnClipboardPasteFinishListener;
import com.jecelyin.android.file_explorer.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

import com.timecat.component.commonbase.task.JecAsyncTask;
import com.timecat.component.commonbase.task.TaskResult;
import com.timecat.component.alert.UIUtils;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */

public class FileClipboard {
    private List<JecFile> clipList = new ArrayList<>();
    private boolean isCopy;
    private OnClipboardDataChangedListener onClipboardDataChangedListener;

    public boolean canPaste() {
        return !clipList.isEmpty();
    }

    public void setData(boolean isCopy, List<JecFile> data) {
        this.isCopy = isCopy;
        clipList.clear();
        clipList.addAll(data);
        if (onClipboardDataChangedListener != null)
            onClipboardDataChangedListener.onClipboardDataChanged();
    }

    public void paste(Context context, JecFile currentDirectory, OnClipboardPasteFinishListener listener) {
        if (!canPaste())
            return;

        ProgressDialog dlg = new ProgressDialog(context);
        PasteTask task = new PasteTask(listener);
        task.setProgress(dlg);
        task.execute(currentDirectory);
    }

    public void showPasteResult(Context context, int count, String error) {
        if (TextUtils.isEmpty(error)) {
            UIUtils.toast(context, R.string.x_items_completed, count);
        } else {
            UIUtils.toast(context, R.string.x_items_completed_and_error_x, count, error);
        }
    }

    private class PasteTask extends JecAsyncTask<JecFile, JecFile, Integer> {
        private final OnClipboardPasteFinishListener listener;
        private StringBuilder errorMsg = new StringBuilder();

        public PasteTask(OnClipboardPasteFinishListener listener) {
            this.listener = listener;
        }

        @Override
        protected void onProgressUpdate(JecFile... values) {
            getProgress().setMessage(values[0].getPath());
        }

        @Override
        protected void onRun(TaskResult<Integer> taskResult, JecFile... params) throws Exception {
            JecFile currentDirectory = params[0];
            int count = 0;
            for (JecFile file : clipList) {
                publishProgress(file);
                try {
                    if (file.isDirectory()) {
                        FileUtils.copyDirectory(file, currentDirectory, !isCopy);
                    } else {
                        FileUtils.copyFile(file, currentDirectory.newFile(file.getName()), !isCopy);
                    }
                    count++;
                } catch (Exception e) {
                    errorMsg.append(e.getMessage()).append("\n");
                }
            }
            clipList.clear();
            taskResult.setResult(count);
        }

        @Override
        protected void onSuccess(Integer integer) {
            if (listener != null) {
                listener.onFinish(integer, errorMsg.toString());
            }
        }

        @Override
        protected void onError(Exception e) {
            if (listener != null) {
                listener.onFinish(0, e.getMessage());
            }
        }
    }

    public void setOnClipboardDataChangedListener(OnClipboardDataChangedListener onClipboardDataChangedListener) {
        this.onClipboardDataChangedListener = onClipboardDataChangedListener;
    }
}
