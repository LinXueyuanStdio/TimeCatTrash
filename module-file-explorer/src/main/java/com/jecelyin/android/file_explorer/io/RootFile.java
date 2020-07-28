package com.jecelyin.android.file_explorer.io;

import android.os.Parcel;

import com.jecelyin.android.file_explorer.listener.FileListResultListener;
import com.timecat.component.commonsdk.listener.OnResultCallback;
import com.timecat.component.commonsdk.utils.utils.FileInfo;
import com.timecat.component.commonsdk.utils.utils.RootShellRunner;

import java.io.File;
import java.util.List;

import com.timecat.component.commonbase.listeners.BoolResultListener;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class RootFile extends LocalFile {
    private static final String TAG = RootFile.class.getName();
    private FileInfo fileInfo;

    private RootFile(String path) {
        super(path);
    }

    public static void obtain(final String pathname, final OnResultCallback<RootFile> callback) {
        final RootShellRunner runner = new RootShellRunner();
        runner.listFileInfo(pathname, new OnResultCallback<List<FileInfo>>() {
            @Override
            public void onError(String error) {
                callback.onSuccess(new RootFile(pathname));
            }

            @Override
            public void onSuccess(List<FileInfo> files) {
                if (!files.isEmpty()) {
                    FileInfo fileInfo = files.get(0);
                    callback.onSuccess(new RootFile(pathname, fileInfo));
                } else {
                    callback.onSuccess(new RootFile(pathname));
                }
            }
        });
    }

    private RootFile(String pathname, FileInfo fileInfo) {
        super(pathname);
        this.fileInfo = fileInfo;
    }

    @Override
    public boolean isDirectory() {
        return fileInfo != null ? fileInfo.isDirectory : super.isDirectory();
    }

    @Override
    public boolean isFile() {
        return fileInfo != null ? !fileInfo.isDirectory : super.isFile();
    }

    @Override
    public long lastModified() {
        return fileInfo != null ? fileInfo.lastModified : super.lastModified();
    }

    @Override
    public long length() {
        return fileInfo != null ? fileInfo.size : super.length();
    }

    @Override
    public String getAbsolutePath() {
        return fileInfo != null && fileInfo.isSymlink ? fileInfo.linkedPath : (new File(getPath()).getAbsolutePath());
    }

    @Override
    public boolean exists() {
        return fileInfo != null;
    }

    @Override
    public void delete(final BoolResultListener listener) {
        RootShellRunner runner = new RootShellRunner();
        runner.delete(getPath(), new OnResultCallback<Boolean>() {
            @Override
            public void onError(String error) {
                listener.onResult(false);
            }

            @Override
            public void onSuccess(Boolean result) {
                listener.onResult(result);
            }
        });
    }

    @Override
    public void listFiles(final FileListResultListener listener) {
        RootShellRunner runner = new RootShellRunner();
        runner.listFileInfo(getPath(), new OnResultCallback<List<FileInfo>>() {
            @Override
            public void onError(String error) {
                listener.onError(error);
            }

            @Override
            public void onSuccess(List<FileInfo> result) {
                int size = result.size();
                RootFile[] results = new RootFile[size];
                String path = getPath();
                for (int i = 0; i < size; i++) {
                    FileInfo fileInfo = result.get(i);
                    results[i] = new RootFile(path + "/" + fileInfo.name, fileInfo);
                }
                listener.onResult(results);
            }
        });
    }

    @Override
    public void mkdirs(final BoolResultListener listener) {
        new RootShellRunner().mkdirs(getPath(), new OnResultCallback<Boolean>() {
            @Override
            public void onError(String error) {
                listener.onResult(false);
            }

            @Override
            public void onSuccess(Boolean result) {
                listener.onResult(result);
            }
        });
    }

    @Override
    public void renameTo(JecFile dest, final BoolResultListener listener) {
        new RootShellRunner().rename(getPath(), dest.getPath(), new OnResultCallback<Boolean>() {
            @Override
            public void onError(String error) {
                listener.onResult(false);
            }

            @Override
            public void onSuccess(Boolean result) {
                listener.onResult(result);
            }
        });
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getPath());
    }

    protected RootFile(Parcel in) {
        this(in.readString());
    }

    public static final Creator<RootFile> CREATOR = new Creator<RootFile>() {
        @Override
        public RootFile createFromParcel(Parcel source) {
            return new RootFile(source);
        }

        @Override
        public RootFile[] newArray(int size) {
            return new RootFile[size];
        }
    };
}
