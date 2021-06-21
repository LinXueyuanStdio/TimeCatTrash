package com.timecat.component.commonarms.core;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/4/7
 * @description null
 * @usage null
 */
public class BaseApplication extends  com.jess.arms.base.BaseApplication {

  private static BaseApplication context;

  @Override
  public void onCreate() {
    context = this;
    super.onCreate();
  }

  public static BaseApplication getContext() {
    return context;
  }

  public static BaseApplication getInstance() {
    return context;
  }
}
