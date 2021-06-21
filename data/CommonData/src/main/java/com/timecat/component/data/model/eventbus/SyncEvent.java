package com.timecat.component.data.model.eventbus;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/3/20
 * @description null
 * @usage null
 */
public class SyncEvent {
  private boolean isSucceed;
  private String msg;

  public SyncEvent(boolean isSucceed) {
    this.isSucceed = isSucceed;
  }

  public SyncEvent(boolean isSucceed, String msg) {
    this.isSucceed = isSucceed;
    this.msg = msg;
  }

  public boolean isSucceed() {
    return isSucceed;
  }

  public String getMsg() {
    return msg;
  }
}
