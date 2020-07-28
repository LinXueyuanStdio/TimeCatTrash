package com.jecelyin.editor.v2.highlight.jedit;

public class Catalog {
    public static final String DEFAULT_MODE_NAME = "Text";
    public static final HashMap<String, Mode> map = new HashMap<>();

    static {
@MAP@
    }

    public static Mode getModeByName(String name) {
        return map.get(name);
    }
}
