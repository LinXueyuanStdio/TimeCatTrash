package com.jecelyin.editor.v2.ui;

import android.os.Parcelable;

import com.timecat.component.commonbase.task.TaskListener;
import com.timecat.component.commonsdk.utils.override.L;
import com.jecelyin.editor.v2.R;
import com.jecelyin.editor.v2.utils.ExtGrep;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class EditorObjectProcessor {
    public static void process(Parcelable object, EditorDelegate editorDelegate) {
        if (object instanceof ExtGrep) {
            new FindInFilesProcessor(object, editorDelegate);
        }
    }

    private static class FindInFilesProcessor {
        private final EditorDelegate editorDelegate;
        ExtGrep grep;

        public FindInFilesProcessor(Parcelable object, EditorDelegate editorDelegate) {
            grep = (ExtGrep) object;
            this.editorDelegate = editorDelegate;
            find();
        }

        private void find() {
            editorDelegate.mEditText.setSearchResult(editorDelegate.getContext().getString(R.string.searching), grep.getPattern(), null);
            grep.execute(new TaskListener<List<ExtGrep.Result>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onSuccess(List<ExtGrep.Result> result) {
                    buildResults(result);
                }

                @Override
                public void onError(Exception e) {
                    L.e(e);
                }
            });
        }

        private void buildResults(List<ExtGrep.Result> results) {
            StringBuilder ssb = new StringBuilder();

            List<HashMap<String, Object>> data = new ArrayList<>();
            File file = null;
            for(ExtGrep.Result rs : results) {
                if(file == null || !rs.file.equals(file)) {
                    file = rs.file;
                    ssb.append("\n")
                    .append("[PATH]")
                    .append(file.getPath())
                    .append("[/PATH]")
//                    ssb.append(file.getPath(), new ForegroundColorSpan(findResultsPathColor), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    .append("\n");
                    data.add(null);
                    data.add(null);
                }
                //%[index$][标识]*[最小宽度][.精度]转换符
//                ssb.append(String.format("%1$4d  %2$s\n", rs.lineNumber, rs.line), new FileClickableSpan(editorDelegate, rs), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                int start = rs.matchStart;
                int end = rs.matchEnd;

                ssb.append(String.format("%1$4d\t", rs.lineNumber))
                   .append(rs.line.substring(0, start))
                   .append(rs.line.substring(start, end))
                   .append(rs.line.substring(end));
                ssb.append("\n");

                HashMap<String, Object> json = new HashMap<>();
                json.put("file", file.getPath());
                json.put("line", rs.lineNumber);
                json.put("column", rs.lineStartOffset);
                data.add(json);
            }

            if (ssb.length() == 0) {
                ssb.append(editorDelegate.getContext().getString(R.string.find_not_found));
            }

            editorDelegate.mEditText.setSearchResult(ssb.toString(), grep.getPattern(), data);
        }

    }

}
