package com.jecelyin.editor.v2.io;

import android.os.AsyncTask;

import com.timecat.component.commonarms.core.BaseApplication;
import com.timecat.component.commonsdk.listener.OnResultCallback;
import com.timecat.component.commonsdk.utils.utils.IOUtils;
import com.timecat.component.commonsdk.utils.utils.RootShellRunner;
import com.timecat.component.commonsdk.utils.utils.SysUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class FileWriter extends AsyncTask<String, Void, Void> {
    private final String encoding;
    private final File file;
    private final static int BUFFER_SIZE = 16*1024;
    private final File backupFile;
    private final boolean root;
    private final boolean keepBackupFile;
    private FileWriteListener fileWriteListener;
    private Exception error;

    public interface FileWriteListener {
        public void onSuccess();
        public void onError(Exception e);
    }

    public FileWriter(boolean root, File file, String encoding, boolean keepBackupFile) {
        this.file = file;
        this.root = root;
        this.backupFile = makeBackupFile(file);
        this.encoding = encoding;
        this.keepBackupFile = keepBackupFile;
    }

    public void write(String text) {
        execute(text);
    }

    public void setFileWriteListener(FileWriteListener fileWriteListener) {
        this.fileWriteListener = fileWriteListener;
    }

    @Override
    protected Void doInBackground(String... params) {
        String text = params[0];
        try {
            if (root) {
                writeFileWithRoot(text);
            } else {
                writeFileWithoutRoot(text);
            }
        } catch (Exception e) {
            error = e;
        }

        return null;
    }

    private void writeFileWithRoot(final String text) throws Exception {
        final RootShellRunner runner = new RootShellRunner();
        runner.copy(file.getPath(), backupFile.getPath(), new OnResultCallback<Boolean>() {
            @Override
            public void onError(String msg) {
                error = new Exception(msg);
                runner.close();
            }

            @Override
            public void onSuccess(Boolean result) {
                File writableFile = new File(SysUtils.getAppStoragePath(BaseApplication.getContext()), file.getName());
                try {
                    writeFile(writableFile, text);

                    runner.move(writableFile.getPath(), file.getPath(), new OnResultCallback<Boolean>() {
                        @Override
                        public void onError(String msg) {
                            error = new Exception(msg);
                            onSuccess(false);
                        }

                        @Override
                        public void onSuccess(Boolean result) {
                            runner.close();
                        }
                    });
                } catch (Exception e) {
                    error = e;
                    runner.close();
                }

            }
        });
        runner.waitFor();
    }

    private void writeFileWithoutRoot(String text) throws Exception {
        if(backupFile.exists()) {
            if(!backupFile.delete()) {
                throw new IOException("Couldn't delete backup file " + backupFile);
            }
        }

        if(file.isFile() && !IOUtils.copyFile(file, backupFile)) {
            throw new IOException("Couldn't copy file " + file
                    + " to backup file " + backupFile);
        }

        writeFile(file, text);

        if(!keepBackupFile && backupFile.exists() && !backupFile.delete()) {
            throw new IOException("Couldn't remove backup file " + backupFile);
        }
    }

    private void writeFile(File f, String text) throws Exception {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), encoding), BUFFER_SIZE);
        char[] buffer = new char[BUFFER_SIZE]; //16kb
        int size = text.length();
        if (size > 0) {
            int start = 0, end = BUFFER_SIZE;
            for (;;) {
                end = Math.min(end, size);
                text.getChars(start, end, buffer, 0);

                bw.write(buffer, 0, end - start);
                start = end;

                if (end >= size)
                    break;

                end += BUFFER_SIZE;
            }
        }

        bw.close();
    }

    @Override
    protected void onPostExecute(Void v) {
        if(fileWriteListener == null)
            return;
        if(error == null)
            fileWriteListener.onSuccess();
        else
            fileWriteListener.onError(error);
    }

    private static File makeBackupFile(File file) {
        return new File(file.getParent(), file.getName() + ".bak");
    }

}
