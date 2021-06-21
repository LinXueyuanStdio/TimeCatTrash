package com.timecat.component.bmob.data.own;

import android.text.TextUtils;

import androidx.annotation.IntDef;

import com.timecat.component.bmob.data._User;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/1/31
 * @description null
 * @usage null
 */
public class MiaoKey extends BmobObject {

  @IntDef({DeveloperCat, TimeCat, CreatorCat, GenesisCat, ForeverCat, LoveCat})
  @Retention(RetentionPolicy.SOURCE)
  public @interface Cat {

  }

  public static final int DeveloperCat = -1;//开发喵
  public static final int TimeCat = 0;//时光喵 不再需要，在MiaoKey表里找不到，说明是普通时光猫
  public static final int CreatorCat = 1;//造物喵
  public static final int GenesisCat = 2;//创世喵
  public static final int ForeverCat = 3;//永恒喵
  public static final int LoveCat = 4;//爱喵
  public static final int catNum = LoveCat + 1;//喵的种类数

  public String miaoKey;//key
  public _User generateBy;
  public _User user = null;
  @Cat
  public Integer cat = TimeCat;//类型，-1，开发喵，0. 时光猫 1，造物喵，2，创世喵
  public BmobDate startDate = null;//第一次开始使用的时间
  public Long validity = 2592000000L;//有效期，单位毫秒：ms

  //下面是权限，开发喵在数据库生成 MiaoKey 的时候记得决定哪些权限可以用，可以用的设为 true
  public Boolean Forever = false;//永久，可跳过validity有效期的判断逻辑
  public Boolean AppEnhanceNotify = false;//增强通知
  public Boolean NovelBackground = false;//小说自定义背景
  public Boolean MasterTheme = false;//主题
  public Boolean ProSource = false;//高级书源
  public Boolean ProGithub = false;//Github Pro
  public Boolean SyncTask = false;//同步[任务]
  public Boolean SyncNote = false;//同步[笔记]
  public Boolean SyncHabit = false;//同步[习惯]
  public Boolean DataManager = false;//数据控制中心

  public String catName() {
    if (cat == DeveloperCat) {
      return "开发喵";
    }
    if (cat == CreatorCat) {
      return "造物喵";
    }
    if (cat == GenesisCat) {
      return "创世喵";
    }
    if (cat == ForeverCat) {
      return "永恒喵";
    }
    if (cat == LoveCat) {
      return "爱喵";
    }
    return "时光喵";
  }

  /**
   * 是否在有效期内，永久的也返回 true
   */
  public boolean isAvaliable() {
    if (Forever) {
      return true;
    }
    if (startDate == null) {
      return false;
    }
    if (TextUtils.isEmpty(startDate.getDate())) {
      return false;
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CANADA);
    try {
      Date date = sdf.parse(startDate.getDate());
      return date.getTime() + validity > new Date().getTime();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static boolean isSuperCat(@Cat int cat) {
    //是否是下列超级权限中的任意一个
    return cat == MiaoKey.DeveloperCat
        || cat == MiaoKey.ForeverCat
        || cat == MiaoKey.LoveCat
        || cat == MiaoKey.GenesisCat;
  }

  public static String genRandomNum() {
    return genRandomNum(16);
  }

  /**
   * 生成随机密码
   *
   * @return 密码的字符串
   */
  public static String genRandomNum(int pwd_len) {
    int i; // 生成的随机数
    int count = 0; // 生成的密码的长度
    char[] str = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
        'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    StringBuilder pwd = new StringBuilder();
    Random r = new Random();
    while (count < pwd_len) {
      // 生成随机数，取绝对值，防止生成负数，
      i = Math.abs(r.nextInt(str.length - 1)); // 生成的数最大为密码长度-1
      if (i >= 0 && i < str.length) {
        pwd.append(str[i]);
        count++;
      }
    }
    return pwd.toString();
  }

  public void nextCat() {
    cat = (cat + 1) % catNum;
  }

  public String describe() {
    return catName() + ", " + describeStartDate() + "\n" +
        describeEndDate() + "\n" +
        describeForever() + "\n" +
        describeAppEnhanceNotify() + "\n" +
        describeNovelBackground() + "\n" +
        describeMasterTheme() + "\n" +
        describeProSource() + "\n" +
        describeProGithub() + "\n" +
        describeSyncTask() + "\n" +
        describeSyncNote() + "\n" +
        describeSyncHabit() + "\n" +
        describeDataManager();
  }

  private String describeStartDate() {
    return startDate == null ? "未使用" : "启用时间 " + startDate.getDate();
  }

  private String describeEndDate() {
    if (cat == CreatorCat) {
      return yesOrNot(true) + " 30天";
    }
    if (cat < 0 || cat > catNum) {//普通的时光猫
      return yesOrNot(false) + " 永久";
    }
    return yesOrNot(true) + (Forever ? " 永久" : " 30天");
  }

  private String describeForever() {
    return yesOrNot(Forever) + " " + "永久";
  }

  private String describeAppEnhanceNotify() {
    return yesOrNot(AppEnhanceNotify) + " " + "增强通知";
  }

  private String describeNovelBackground() {
    return yesOrNot(NovelBackground) + " " + "小说自定义背景";
  }

  private String describeMasterTheme() {
    return yesOrNot(MasterTheme) + " " + "主题";
  }

  private String describeProSource() {
    return yesOrNot(ProSource) + " " + "高级书源";
  }

  private String describeProGithub() {
    return yesOrNot(ProGithub) + " " + "Github Pro";
  }

  private String describeSyncTask() {
    return yesOrNot(SyncTask) + " " + "同步[任务]";
  }

  private String describeSyncNote() {
    return yesOrNot(SyncNote) + " " + "同步[笔记]";
  }

  private String describeSyncHabit() {
    return yesOrNot(SyncHabit) + " " + "同步[习惯]";
  }

  private String describeDataManager() {
    return yesOrNot(DataManager) + " " + "数据控制中心";
  }

  private String yesOrNot(Boolean b) {
    return b ? "✓" : "✗";
  }
}
