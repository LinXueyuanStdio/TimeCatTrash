package com.jecelyin.android.file_explorer;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class ExplorerException extends RuntimeException {
    public ExplorerException(String detailMessage) {
        super(detailMessage);
    }

    public ExplorerException(Throwable throwable) {
        super(throwable);
    }

    public ExplorerException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ExplorerException() {
    }
}
