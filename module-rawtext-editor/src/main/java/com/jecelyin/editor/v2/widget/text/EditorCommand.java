package com.jecelyin.editor.v2.widget.text;

import java.util.HashMap;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */

public class EditorCommand {
    String cmd;
    HashMap<String, Object> data;
    JsCallback callback;

    private EditorCommand() {}

    public static class Builder {
        private EditorCommand ec;

        public Builder(String cmd) {
            ec = new EditorCommand();
            ec.cmd = cmd;
            ec.data = new HashMap<>();
        }

        public Builder put(String key, Object value) {
            ec.data.put(key, value);
            return this;
        }

        public Builder callback(JsCallback callback) {
            ec.callback = callback;
            return this;
        }

        public EditorCommand build() {
            return ec;
        }
    }
}
