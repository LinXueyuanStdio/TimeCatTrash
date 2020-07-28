package com.jecelyin.editor.v2.io;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.jecelyin.editor.v2.R;
import com.jecelyin.editor.v2.common.ReadFileListener;
import com.timecat.component.commonsdk.listener.OnResultCallback;
import com.timecat.component.commonsdk.utils.override.L;
import com.timecat.component.commonsdk.utils.utils.RootShellRunner;
import com.timecat.component.commonsdk.utils.utils.SysUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class FileReader extends AsyncTask<Void, Void, StringBuilder> {
    private final boolean root;
    private final ReadFileListener listener;
    private final Context context;
    private StringBuilder ssb = null;
    private File file;
    private String encoding;
    private int lineNumber;
//    private int BUFFER_SIZE = 8192;
    private final static int BUFFER_SIZE = 16*1024;
    private File rootFile;
    private Throwable error;

    public FileReader(Context context, File file, String encodingName, boolean root, ReadFileListener listener) {
        this.context = context;
        this.file = file;
        this.encoding = encodingName;
        this.root = root;
        this.listener = listener;
    }

    public void start() {
        if (!root) {
            execute();
            return;
        }
        rootFile = new File(SysUtils.getAppStoragePath(context), file.getName() + ".root");
        if (rootFile.exists())
            rootFile.delete();

        final RootShellRunner runner = new RootShellRunner();
        runner.copy(file.getPath(), rootFile.getPath(), "a+r", new OnResultCallback<Boolean>() {
            @Override
            public void onError(String error) {
                runner.close();
                listener.onDone(null, null, new Exception(error));
            }

            @Override
            public void onSuccess(Boolean result) {
                runner.close();
                if (!result) {
                    listener.onDone(null, null, new Exception(context.getString(R.string.read_file_exception)));
                    return;
                }

                execute();
            }
        });
    }

    @Override
    protected void onPreExecute() {
        listener.onStart();
    }

    @Override
    protected StringBuilder doInBackground(Void... params) {
        try {
            return read();
        } catch (Throwable t) {
            error = t;
            return null;
        }
    }

    @Override
    protected void onPostExecute(StringBuilder StringBuilder) {
        if (rootFile != null)
            rootFile.delete();
        listener.onDone(StringBuilder, encoding, error);
    }

    private StringBuilder read() throws Exception, OutOfMemoryError {
        File f = rootFile != null && rootFile.isFile() ? rootFile : file;
        if(TextUtils.isEmpty(encoding))
            encoding = FileEncodingDetector.detectEncoding(f);

        L.d(file.getPath()+" encoding is "+encoding);
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(f), encoding));

        char[] buf = new char[BUFFER_SIZE];
        int len;
        ssb = new StringBuilder(BUFFER_SIZE * 4);
        while ((len = reader.read(buf, 0, BUFFER_SIZE)) != -1) {
            ssb.append(buf, 0, len);
        }

        lineNumber = reader.getLineNumber() + 1;
        reader.close();

        return ssb;
    }

}
