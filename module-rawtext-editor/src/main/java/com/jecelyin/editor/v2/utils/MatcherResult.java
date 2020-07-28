package com.jecelyin.editor.v2.utils;

import java.util.regex.Matcher;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */

public class MatcherResult {
    private final int start;
    private final int end;
    private final int groupCount;
    private final String[] groups;

    public MatcherResult(Matcher m) {
        start = m.start();
        end = m.end();
        groupCount = m.groupCount()+1;

        groups = new String[groupCount];
        for (int i = 0; i < groupCount; i++) {
            groups[i] = m.group(i);
        }
    }

    public int start() {
        return start;
    }

    public int end() {
        return end;
    }

    public int groupCount() {
        return groupCount;
    }

    public String group(int group) {
        return groups[group];
    }
}
