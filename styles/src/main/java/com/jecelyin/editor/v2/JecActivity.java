package com.jecelyin.editor.v2;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.jecelyin.styles.R;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/2/19
 * @description null
 * @usage null
 */
public class JecActivity extends AppCompatActivity {

  private boolean isAttached;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//
//    if (SysUtils.isMonkeyRunner(this)) {//Monkey Test TODO 能不能删？貌似去掉影响不大...
//      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//          WindowManager.LayoutParams.FLAG_FULLSCREEN);
//      hideStatusBar();
//    }
  }

  protected void hideStatusBar() {
    View decorView = getWindow().getDecorView();
    // Hide the status bar.
    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
    decorView.setSystemUiVisibility(uiOptions);
  }

  @Override
  public void setSupportActionBar(@Nullable Toolbar toolbar) {
    super.setSupportActionBar(toolbar);
    setStatusBarColor(null);
  }

  protected void setStatusBarColor(ViewGroup drawerLayout) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      return;
    }
    if (isFullScreenMode()) {
      return;
    }
    TypedArray a = getTheme().obtainStyledAttributes(new int[]{R.attr.colorPrimary});
    int color = a.getColor(0, Color.TRANSPARENT);
    a.recycle();

    if (drawerLayout != null) {
      setColorForDrawerLayout(this, drawerLayout, color, 0);
    } else {
      setColor(this, color, 0);
    }
  }

  protected boolean isFullScreenMode() {
    return false;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      // 当setDisplayHomeAsUpEnabled(true)时，提供返回支持
      case android.R.id.home:
        onBackPressed();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  public Context getContext() {
    return this;
  }

  @Override
  public void onAttachedToWindow() {
    super.onAttachedToWindow();
    isAttached = true;
  }

  @Override
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    isAttached = false;
  }

  public boolean isAttached() {
    return isAttached;
  }

  public boolean isDetached() {
    return !isAttached;
  }

  /**
   * 为DrawerLayout 布局设置状态栏变色
   *
   * @param activity 需要设置的activity
   * @param drawerLayout ViewGroup
   * @param color 状态栏颜色值
   * @param statusBarAlpha 状态栏透明度
   */
  public static void setColorForDrawerLayout(Activity activity, ViewGroup drawerLayout, int color,
      int statusBarAlpha) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
    } else {
      activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }
    // 生成一个状态栏大小的矩形
    // 添加 statusBarView 到布局中
    ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
    if (contentLayout.getChildCount() > 0 && contentLayout.getChildAt(0) instanceof StatusBarView) {
      contentLayout.getChildAt(0).setBackgroundColor(calculateStatusColor(color, statusBarAlpha));
    } else {
      StatusBarView statusBarView = createStatusBarView(activity, color);
      contentLayout.addView(statusBarView, 0);
    }
    // 内容布局不是 LinearLayout 时,设置padding top
    if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
      contentLayout.getChildAt(1).setPadding(0, getStatusBarHeight(activity), 0, 0);
    }
    // 设置属性
    ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
    drawerLayout.setFitsSystemWindows(false);
    contentLayout.setFitsSystemWindows(false);
    contentLayout.setClipToPadding(true);
    drawer.setFitsSystemWindows(false);

    addTranslucentView(activity, statusBarAlpha);
  }

  /**
   * 添加半透明矩形条
   *
   * @param activity 需要设置的 activity
   * @param statusBarAlpha 透明值
   */
  private static void addTranslucentView(Activity activity, int statusBarAlpha) {
    ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
    if (contentView.getChildCount() > 1) {
      contentView.getChildAt(1).setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0));
    } else {
      contentView.addView(createTranslucentStatusBarView(activity, statusBarAlpha));
    }
  }

  /**
   * 生成一个和状态栏大小相同的彩色矩形条
   *
   * @param activity 需要设置的 activity
   * @param color 状态栏颜色值
   * @return 状态栏矩形条
   */
  private static StatusBarView createStatusBarView(Activity activity, int color) {
    // 绘制一个和状态栏一样高的矩形
    StatusBarView statusBarView = new StatusBarView(activity);
    LinearLayout.LayoutParams params =
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            getStatusBarHeight(activity));
    statusBarView.setLayoutParams(params);
    statusBarView.setBackgroundColor(color);
    return statusBarView;
  }

  /**
   * 创建半透明矩形 View
   *
   * @param alpha 透明值
   * @return 半透明 View
   */
  private static StatusBarView createTranslucentStatusBarView(Activity activity, int alpha) {
    // 绘制一个和状态栏一样高的矩形
    StatusBarView statusBarView = new StatusBarView(activity);
    LinearLayout.LayoutParams params =
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            getStatusBarHeight(activity));
    statusBarView.setLayoutParams(params);
    statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
    return statusBarView;
  }


  /**
   * 设置状态栏颜色
   *
   * @param activity 需要设置的activity
   * @param color 状态栏颜色值
   * @param statusBarAlpha 状态栏透明度
   */
  public static void setColor(Activity activity, int color, int statusBarAlpha) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      activity.getWindow().setStatusBarColor(calculateStatusColor(color, statusBarAlpha));
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
      int count = decorView.getChildCount();
      if (count > 0 && decorView.getChildAt(count - 1) instanceof StatusBarView) {
        decorView.getChildAt(count - 1)
            .setBackgroundColor(calculateStatusColor(color, statusBarAlpha));
      } else {
        StatusBarView statusView = createStatusBarView(activity, color, statusBarAlpha);
        decorView.addView(statusView);
      }
      setRootView(activity);
    }
  }

  public static class StatusBarView extends View {

    public StatusBarView(Context context, AttributeSet attrs) {
      super(context, attrs);
    }

    public StatusBarView(Context context) {
      super(context);
    }
  }

  /**
   * 生成一个和状态栏大小相同的半透明矩形条
   *
   * @param activity 需要设置的activity
   * @param color 状态栏颜色值
   * @param alpha 透明值
   * @return 状态栏矩形条
   */
  private static StatusBarView createStatusBarView(Activity activity, int color, int alpha) {
    // 绘制一个和状态栏一样高的矩形
    StatusBarView statusBarView = new StatusBarView(activity);
    LinearLayout.LayoutParams params =
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            getStatusBarHeight(activity));
    statusBarView.setLayoutParams(params);
    statusBarView.setBackgroundColor(calculateStatusColor(color, alpha));
    return statusBarView;
  }

  /**
   * 获取状态栏高度
   *
   * @param context context
   * @return 状态栏高度
   */
  private static int getStatusBarHeight(Context context) {
    // 获得状态栏高度
    int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
    return context.getResources().getDimensionPixelSize(resourceId);
  }

  /**
   * 计算状态栏颜色
   *
   * @param color color值
   * @param alpha alpha值
   * @return 最终的状态栏颜色
   */
  private static int calculateStatusColor(int color, int alpha) {
    float a = 1 - alpha / 255f;
    int red = color >> 16 & 0xff;
    int green = color >> 8 & 0xff;
    int blue = color & 0xff;
    red = (int) (red * a + 0.5);
    green = (int) (green * a + 0.5);
    blue = (int) (blue * a + 0.5);
    return 0xff << 24 | red << 16 | green << 8 | blue;
  }


  /**
   * 设置根布局参数
   */
  private static void setRootView(Activity activity) {
    ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content))
        .getChildAt(0);
    rootView.setFitsSystemWindows(true);
    rootView.setClipToPadding(true);
  }
}
