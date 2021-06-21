package com.timecat.component.data.model.eventbus;

import android.content.Intent;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/4/22
 * @description null
 * @usage null
 */
public class OnActivityResultEvent {
  public int requestCode;
  public int resultCode;
  public Intent data;

  public OnActivityResultEvent(int requestCode, int resultCode, Intent data) {
    this.requestCode = requestCode;
    this.resultCode = resultCode;
    this.data = data;
  }
}
