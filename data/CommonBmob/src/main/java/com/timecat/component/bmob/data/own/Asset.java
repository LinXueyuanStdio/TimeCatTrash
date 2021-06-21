package com.timecat.component.bmob.data.own;

import com.timecat.component.bmob.data.own.MiaoKey.Cat;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import cn.bmob.v3.datatype.BmobDate;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/4/19
 * @description null
 * @usage null
 */
public class Asset implements Serializable {

  public BmobDate recordTime;
  public long ownTimeAsset;//从recordTime开始的毫秒数

  public long currentOwnTimeAsset() {
    if (recordTime == null) {
      return 0;
    }
    if (getForever()) {
      return 0;//永久的锁血为 0
    }
    long currentTime = System.currentTimeMillis();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CANADA);
    try {
      Date date = sdf.parse(recordTime.getDate());
      long currentOwnTimeAsset = ownTimeAsset - (currentTime - date.getTime());
      if (currentOwnTimeAsset > 0) {
        return currentOwnTimeAsset;
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return 0;
  }


  //权限控制
  //下面是权限，开发喵在数据库生成 MiaoKey 的时候记得决定哪些权限可以用，可以用的设为 true
  @Cat
  private Integer cat = MiaoKey.CreatorCat;//类型，0，开发喵，1，造物喵，2，创世喵
  private Boolean Forever = false;//永久，可跳过validity有效期的判断逻辑
  private Boolean AppEnhanceNotify = false;//增强通知
  private Boolean NovelBackground = false;//小说自定义背景
  private Boolean MasterTheme = false;//主题
  private Boolean ProSource = false;//高级书源
  private Boolean ProGithub = false;//Github Pro
  private Boolean SyncTask = false;//同步[任务]
  private Boolean SyncNote = false;//同步[笔记]
  private Boolean SyncHabit = false;//同步[习惯]
  private Boolean DataManager = false;//数据控制中心

  public void setupPermission(MiaoKey miaoKey) {
    if (MiaoKey.isSuperCat(cat)) {
      //超级喵无法改权限
      return;
    }
    //没有超级喵权限的喵，也就是造物喵和时光猫，可以随便改
    this.cat = miaoKey.cat;
    //造物喵是基本单位，时间用完需要重置
    if (currentOwnTimeAsset() == 0) {//时间用完，重置
      this.Forever = false;
      this.AppEnhanceNotify = false;
      this.NovelBackground = false;
      this.MasterTheme = false;
      this.ProSource = false;
      this.ProGithub = false;
      this.SyncTask = false;
      this.SyncNote = false;
      this.SyncHabit = false;
      this.DataManager = false;
    }
    // 重置后再计算权限
    this.Forever = this.Forever || miaoKey.Forever;
    this.AppEnhanceNotify = this.AppEnhanceNotify || miaoKey.AppEnhanceNotify;
    this.NovelBackground = this.NovelBackground || miaoKey.NovelBackground;
    this.MasterTheme = this.MasterTheme || miaoKey.MasterTheme;
    this.ProSource = this.ProSource || miaoKey.ProSource;
    this.ProGithub = this.ProGithub || miaoKey.ProGithub;
    this.SyncTask = this.SyncTask || miaoKey.SyncTask;
    this.SyncNote = this.SyncNote || miaoKey.SyncNote;
    this.SyncHabit = this.SyncHabit || miaoKey.SyncHabit;
    this.DataManager = this.DataManager || miaoKey.DataManager;
  }

  public void useMiaoKey(MiaoKey miaoKey) {
    this.recordTime = miaoKey.startDate;
    this.ownTimeAsset = currentOwnTimeAsset() + miaoKey.validity;
    setupPermission(miaoKey);
  }

  /**
   * 是否有必要检查权限 如果是永久，返回 true 可用
   *
   * 如果在时间资产的有效期内，返回 true 可用
   *
   * 如果时间资产为 0，返回 false 不可用，所有权限都返回 false
   */
  public boolean isPermissionAvaliable() {
    return Forever || (currentOwnTimeAsset() > 0);
  }

  //region getter
  @NonNull
  public BmobDate getRecordTime() {
    return recordTime == null ? new BmobDate(new DateTime().withMillis(0).toDate()) : recordTime;
  }

  public Integer getCat() {
    return isPermissionAvaliable() ? cat : MiaoKey.TimeCat;
  }

  public Boolean getForever() {
    return Forever;
  }

  public Boolean getAppEnhanceNotify() {
    return isPermissionAvaliable() ? AppEnhanceNotify : false;
  }

  public Boolean getNovelBackground() {
    return isPermissionAvaliable() ? NovelBackground : false;
  }

  public Boolean getMasterTheme() {
    return isPermissionAvaliable() ? MasterTheme : false;
  }

  public Boolean getProSource() {
    return isPermissionAvaliable() ? ProSource : false;
  }

  public Boolean getProGithub() {
    return isPermissionAvaliable() ? ProGithub : false;
  }

  public Boolean getSyncTask() {
    return isPermissionAvaliable() ? SyncTask : false;
  }

  public Boolean getSyncNote() {
    return isPermissionAvaliable() ? SyncNote : false;
  }

  public Boolean getSyncHabit() {
    return isPermissionAvaliable() ? SyncHabit : false;
  }

  public Boolean getDataManager() {
    return isPermissionAvaliable() ? DataManager : false;
  }

  //endregion

  //region setter
  public void nextCat() {
    cat = (cat + 1) % MiaoKey.catNum;
  }

  public void setRecordTime(BmobDate recordTime) {
    this.recordTime = recordTime;
  }

  public void setOwnTimeAsset(long ownTimeAsset) {
    this.ownTimeAsset = ownTimeAsset;
  }

  public void setCat(Integer cat) {
    this.cat = cat;
  }

  public void setForever(Boolean forever) {
    Forever = forever;
  }

  public void setAppEnhanceNotify(Boolean appEnhanceNotify) {
    AppEnhanceNotify = appEnhanceNotify;
  }

  public void setNovelBackground(Boolean novelBackground) {
    NovelBackground = novelBackground;
  }

  public void setMasterTheme(Boolean masterTheme) {
    MasterTheme = masterTheme;
  }

  public void setProSource(Boolean proSource) {
    ProSource = proSource;
  }

  public void setProGithub(Boolean proGithub) {
    ProGithub = proGithub;
  }

  public void setSyncTask(Boolean syncTask) {
    SyncTask = syncTask;
  }

  public void setSyncNote(Boolean syncNote) {
    SyncNote = syncNote;
  }

  public void setSyncHabit(Boolean syncHabit) {
    SyncHabit = syncHabit;
  }

  public void setDataManager(Boolean dataManager) {
    DataManager = dataManager;
  }

  //endregion

  //region 描述
  public String describe() {
    return catName() + (isPermissionAvaliable() ? "" : "(已过期)") + ", " +
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

  public String catName() {
    if (cat == MiaoKey.DeveloperCat) {
      return "开发喵";
    }
    if (cat == MiaoKey.CreatorCat) {
      return "造物喵";
    }
    if (cat == MiaoKey.GenesisCat) {
      return "创世喵";
    }
    if (cat == MiaoKey.ForeverCat) {
      return "永恒喵";
    }
    if (cat == MiaoKey.LoveCat) {
      return "爱喵";
    }
    return "时光喵";
  }

  private String describeForever() {
    return yesOrNot(getForever()) + " " + "永久";
  }

  private String describeAppEnhanceNotify() {
    return yesOrNot(getAppEnhanceNotify()) + " " + "增强通知";
  }

  private String describeNovelBackground() {
    return yesOrNot(getNovelBackground()) + " " + "小说自定义背景";
  }

  private String describeMasterTheme() {
    return yesOrNot(getMasterTheme()) + " " + "主题";
  }

  private String describeProSource() {
    return yesOrNot(getProSource()) + " " + "高级书源";
  }

  private String describeProGithub() {
    return yesOrNot(getProGithub()) + " " + "Github Pro";
  }

  private String describeSyncTask() {
    return yesOrNot(getSyncTask()) + " " + "同步[任务]";
  }

  private String describeSyncNote() {
    return yesOrNot(getSyncNote()) + " " + "同步[笔记]";
  }

  private String describeSyncHabit() {
    return yesOrNot(getSyncHabit()) + " " + "同步[习惯]";
  }

  private String describeDataManager() {
    return yesOrNot(getDataManager()) + " " + "数据控制中心";
  }

  private String yesOrNot(Boolean b) {
    return b ? "✓" : "✗";
  }
  //endregion
}
