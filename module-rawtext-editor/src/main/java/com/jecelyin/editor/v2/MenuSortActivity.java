package com.jecelyin.editor.v2;

import android.text.TextUtils;
import com.timecat.component.setting.DEF;
import java.util.LinkedList;
import java.util.List;
import my.shouheng.palmmarkdown.tools.MarkdownFormat;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/2/21
 * @description null
 * @usage null
 */
public class MenuSortActivity extends my.shouheng.palmmarkdown.MenuSortActivity {
  @Override
  public List<MarkdownFormat> getMarkdownFormats() {
    String mdStr = DEF.config().getNoteEditorMenuSort();
    if (!TextUtils.isEmpty(mdStr)) {
      String[] mds = mdStr.split(MarkdownFormat.ITEM_SORT_SPLIT);
      List<MarkdownFormat> markdownFormats = new LinkedList<>();
      for (String md : mds) {
        markdownFormats.add(MarkdownFormat.valueOf(md));
      }
      return markdownFormats;
    } else {
      return MarkdownFormat.defaultMarkdownFormats;
    }
  }

  @Override
  public void setMarkdownFormats(List<MarkdownFormat> markdownFormats) {
    int size = markdownFormats.size();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < size; i++) {
      if (i == size - 1) {
        sb.append(markdownFormats.get(i).name());
      } else {
        sb.append(markdownFormats.get(i).name()).append(MarkdownFormat.ITEM_SORT_SPLIT);
      }
    }
    DEF.config().setNoteEditorMenuSort(sb.toString());
  }
}
