package com.jecelyin.android.file_explorer;

import android.os.AsyncTask;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class FileProcessTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    public void updateProgress(int value) {

    }
}
