package com.timecat.component.data.model.eventbus;

import com.jess.arms.utils.LogUtils;
import com.timecat.component.data.model.StringUtils;

public class RenderMarkdownEvent {

  public String content;
  public String title;

  public RenderMarkdownEvent(String content) {
    LogUtils.debugInfo("RenderMarkdownEvent");
    if (content == null) {
      this.content = "";
      title = "";
      return;
    }
    this.content = content;
    this.title = StringUtils.getShortText(content);
  }

  public RenderMarkdownEvent(String title, String content) {
    this.title = title;
    this.content = content;
  }
}
