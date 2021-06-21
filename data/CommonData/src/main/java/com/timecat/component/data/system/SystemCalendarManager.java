package com.timecat.component.data.system;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import androidx.annotation.NonNull;
import com.blankj.utilcode.util.LogUtils;
import com.timecat.component.setting.DEF;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/5/8
 * @description null
 * @usage null
 */
public class SystemCalendarManager {

  public interface SystemCalendarNameSpace {

    String SYSTEM_CALENDAR_ACCOUNT_ID = "system_calendar_account_key";
  }

  public static class Account {

    public String ACCOUNT_NAME = "时光猫";
    public String CALENDAR_DISPLAY_NAME = "时光猫";
    public String OWNER_ACCOUNT = "时光猫";
    public String NAME = "时光猫";
    public String ALLOWED_REMINDERS = "0,1,4";
    public String ALLOWED_AVAILABILITY = "0,1,2";
    public String ALLOWED_ATTENDEE_TYPES = "0,1,2,3";
    public String CALENDAR_TIME_ZONE = TimeZone.getDefault().getID();
    public int CALENDAR_COLOR = 0x03a9f4;
    public int VISIBLE = 1;
    public int CAN_MODIFY_TIME_ZONE = 1;
    public int SYNC_EVENTS = 1;
    public int CAN_ORGANIZER_RESPOND = 1;
    public int MAX_REMINDERS = 8;

    public Account() {
    }

    public Account(String ACCOUNT_NAME, String CALENDAR_DISPLAY_NAME, String OWNER_ACCOUNT,
        String NAME, String ALLOWED_REMINDERS, String ALLOWED_AVAILABILITY,
        String ALLOWED_ATTENDEE_TYPES, String CALENDAR_TIME_ZONE, int CALENDAR_COLOR, int VISIBLE,
        int CAN_MODIFY_TIME_ZONE, int SYNC_EVENTS, int CAN_ORGANIZER_RESPOND, int MAX_REMINDERS) {
      this.ACCOUNT_NAME = ACCOUNT_NAME;
      this.CALENDAR_DISPLAY_NAME = CALENDAR_DISPLAY_NAME;
      this.OWNER_ACCOUNT = OWNER_ACCOUNT;
      this.NAME = NAME;
      this.ALLOWED_REMINDERS = ALLOWED_REMINDERS;
      this.ALLOWED_AVAILABILITY = ALLOWED_AVAILABILITY;
      this.ALLOWED_ATTENDEE_TYPES = ALLOWED_ATTENDEE_TYPES;
      this.CALENDAR_TIME_ZONE = CALENDAR_TIME_ZONE;
      this.CALENDAR_COLOR = CALENDAR_COLOR;
      this.VISIBLE = VISIBLE;
      this.CAN_MODIFY_TIME_ZONE = CAN_MODIFY_TIME_ZONE;
      this.SYNC_EVENTS = SYNC_EVENTS;
      this.CAN_ORGANIZER_RESPOND = CAN_ORGANIZER_RESPOND;
      this.MAX_REMINDERS = MAX_REMINDERS;
    }
  }

  public static class SystemCalendarAccount {

    public long calID = 0;
    public String displayName = null;
    public String accountName = null;
    public int calendarColor = 0;
    public int calendarAccessLevel = 0;
    public int visible = 0;
    public String calendarTimezone = null;
    public int syncEvents = 0;
    public String ownerAccount = null;
    public int canOrganizerRespond = 0;
    public int maxReminders = 0;
    public String allowedReminders = null;
    public String allowedAvailability = null;
    public String allowedAttendeeTypes = null;
    public int isPrimary = 0;
    public String accountType = null;
    public String name = null;
    public int dirty = 0;

    public SystemCalendarAccount() {
    }

    public SystemCalendarAccount(long calID, String displayName, String accountName,
        int calendarColor,
        int calendarAccessLevel, int visible, String calendarTimezone, int syncEvents,
        String ownerAccount, int canOrganizerRespond, int maxReminders,
        String allowedReminders, String allowedAvailability, String allowedAttendeeTypes,
        int isPrimary,
        String accountType, String name, int dirty) {
      this.calID = calID;
      this.displayName = displayName;
      this.accountName = accountName;
      this.calendarColor = calendarColor;
      this.calendarAccessLevel = calendarAccessLevel;
      this.visible = visible;
      this.calendarTimezone = calendarTimezone;
      this.syncEvents = syncEvents;
      this.ownerAccount = ownerAccount;
      this.canOrganizerRespond = canOrganizerRespond;
      this.maxReminders = maxReminders;
      this.allowedReminders = allowedReminders;
      this.allowedAvailability = allowedAvailability;
      this.allowedAttendeeTypes = allowedAttendeeTypes;
      this.isPrimary = isPrimary;
      this.accountType = accountType;
      this.name = name;
      this.dirty = dirty;
    }
  }

  public static  class SystemCalendarEvent {

    public long id = 0;
    public long calID = 0;
    public String title = null;
    public String description = null;
    public String eventLocation = null;
    public int displayColor = 0;
    public int status = 0;
    public long start = 0;
    public long end = 0;
    public String duration = null;
    public String eventTimeZone = null;
    public String eventEndTimeZone = null;
    public int allDay = 0;
    public int accessLevel = 0;
    public int availability = 0;
    public int hasAlarm = 0;
    public String rrule = null;
    public String rdate = null;
    public int hasAttendeeData = 0;
    public int lastDate;
    public String organizer;
    public String isOrganizer;

    public List<SystemCalendarReminder> reminderList = new ArrayList<>();

    public SystemCalendarEvent() {
    }

    public SystemCalendarEvent(long id, long calID, String title, String description,
        String eventLocation,
        int displayColor, int status, long start, long end, String duration,
        String eventTimeZone, String eventEndTimeZone, int allDay, int accessLevel,
        int availability,
        int hasAlarm, String rrule, String rdate, int hasAttendeeData, int lastDate,
        String organizer, String isOrganizer) {
      this.id = id;
      this.calID = calID;
      this.title = title;
      this.description = description;
      this.eventLocation = eventLocation;
      this.displayColor = displayColor;
      this.status = status;
      this.start = start;
      this.end = end;
      this.duration = duration;
      this.eventTimeZone = eventTimeZone;
      this.eventEndTimeZone = eventEndTimeZone;
      this.allDay = allDay;
      this.accessLevel = accessLevel;
      this.availability = availability;
      this.hasAlarm = hasAlarm;
      this.rrule = rrule;
      this.rdate = rdate;
      this.hasAttendeeData = hasAttendeeData;
      this.lastDate = lastDate;
      this.organizer = organizer;
      this.isOrganizer = isOrganizer;
    }
  }

  public static class Event {

    public String TITLE = "";
    public String DESCRIPTION = "";
    public String EVENT_LOCATION = "";
    public String ORGANIZER = "";
    public String EVENT_TIMEZONE = TimeZone.getDefault().getID();
    public long CALENDAR_ID = -1;
    public long DTSTART = 0;
    public long DTEND = 0;
    public int ACCESS_LEVEL = CalendarContract.Events.ACCESS_DEFAULT;
    public int ALL_DAY = 1;
    public int STATUS = 1;
    public int HAS_ALARM = 1;
    public int HAS_ATTENDEE_DATA = 1;

    public int MINUTES = 5; //提前 5 分钟有提醒

    public Event() {
    }

    public Event(String TITLE, String DESCRIPTION, String EVENT_LOCATION, String ORGANIZER,
        String EVENT_TIMEZONE, long CALENDAR_ID, long DTSTART, long DTEND, int ACCESS_LEVEL,
        int ALL_DAY, int STATUS, int HAS_ALARM, int HAS_ATTENDEE_DATA, int MINUTES) {
      this.TITLE = TITLE;
      this.DESCRIPTION = DESCRIPTION;
      this.EVENT_LOCATION = EVENT_LOCATION;
      this.ORGANIZER = ORGANIZER;
      this.EVENT_TIMEZONE = EVENT_TIMEZONE;
      this.CALENDAR_ID = CALENDAR_ID;
      this.DTSTART = DTSTART;
      this.DTEND = DTEND;
      this.ACCESS_LEVEL = ACCESS_LEVEL;
      this.ALL_DAY = ALL_DAY;
      this.STATUS = STATUS;
      this.HAS_ALARM = HAS_ALARM;
      this.HAS_ATTENDEE_DATA = HAS_ATTENDEE_DATA;
      this.MINUTES = MINUTES;
    }
  }

  public static class SystemCalendarReminder {

    public long reminderId = 0;
    public long reminderEventID = 0;
    public int reminderMinute = 0;
    public int reminderMethod = 0;

    public SystemCalendarReminder() {
    }

    public SystemCalendarReminder(long reminderId,
        long reminderEventID, int reminderMinute, int reminderMethod) {
      this.reminderId = reminderId;
      this.reminderEventID = reminderEventID;
      this.reminderMinute = reminderMinute;
      this.reminderMethod = reminderMethod;
    }
  }

  public static long getCalendarId(Context context) {
    long calendarAccountId = DEF.setting().getLong(
        SystemCalendarNameSpace.SYSTEM_CALENDAR_ACCOUNT_ID, -1);
    if (calendarAccountId > 0) {
      return calendarAccountId;
    }

    calendarAccountId = queryTimeCatAccountId(context);
    if (calendarAccountId > 0) {
      return calendarAccountId;
    }

    calendarAccountId = insertAccount(context, new Account());
    return calendarAccountId;
  }

  /**
   * 插入一条新的日历账户
   */
  public static long insertAccount(@NonNull Context context, @NonNull Account account) {
    ContentResolver cr = context.getContentResolver();
    ContentValues values = new ContentValues();
    // 在添加账户时，如果账户类型不存在系统中，则可能该新增记录会被标记为脏数据而被删除
    // 设置为ACCOUNT_TYPE_LOCAL可以保证在不存在账户类型时，该新增数据不会被删除
    values.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
    values.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
        CalendarContract.Calendars.CAL_ACCESS_OWNER);
    values.put(CalendarContract.Calendars.NAME, account.NAME);
    values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, account.CALENDAR_DISPLAY_NAME);
    values.put(CalendarContract.Calendars.ACCOUNT_NAME, account.ACCOUNT_NAME);
    values.put(CalendarContract.Calendars.OWNER_ACCOUNT, account.OWNER_ACCOUNT);
    values.put(CalendarContract.Calendars.ALLOWED_REMINDERS, account.ALLOWED_REMINDERS);
    values.put(CalendarContract.Calendars.ALLOWED_AVAILABILITY, account.ALLOWED_AVAILABILITY);
    values.put(CalendarContract.Calendars.ALLOWED_ATTENDEE_TYPES, account.ALLOWED_ATTENDEE_TYPES);
    values.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, account.CALENDAR_TIME_ZONE);
    values.put(CalendarContract.Calendars.CALENDAR_COLOR, account.CALENDAR_COLOR);
    values.put(CalendarContract.Calendars.VISIBLE, account.VISIBLE);
    values.put(CalendarContract.Calendars.CAN_MODIFY_TIME_ZONE, account.CAN_MODIFY_TIME_ZONE);
    values.put(CalendarContract.Calendars.SYNC_EVENTS, account.SYNC_EVENTS);
    values.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, account.CAN_ORGANIZER_RESPOND);
    values.put(CalendarContract.Calendars.MAX_REMINDERS, account.MAX_REMINDERS);

    Uri uri = CalendarContract.Calendars.CONTENT_URI;
    //        values.put(CalendarContract.Calendars.IS_PRIMARY, 1);
    //        values.put(CalendarContract.Calendars.DIRTY, 1);

    // 修改或添加ACCOUNT_NAME只能由syn-adapter调用
    // 对uri设置CalendarContract.CALLER_IS_SYNCADAPTER为true，即标记当前操作为syn_adapter操作；
    // 再设置CalendarContract.CALLER_IS_SYNCADAPTER为true时
    // 必须带上参数ACCOUNT_NAME和ACCOUNT_TYPE，
    // 至于这两个参数的值，可以随意填，不影响最终结果；
    uri = uri.buildUpon()
        .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
        .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "时光猫")
        .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,
            CalendarContract.ACCOUNT_TYPE_LOCAL)
        .build();

    Uri accountUri;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (PackageManager.PERMISSION_GRANTED == context
          .checkSelfPermission("android.permission.WRITE_CALENDAR")) {
        accountUri = cr.insert(uri, values);
      } else {
        LogUtils.e("请授予读取日历的权限");
        return -1;
      }
    } else {
      accountUri = cr.insert(uri, values);
    }
    if (accountUri == null) {
      LogUtils.e("accountUri == null");
      return -1;
    }
    // get the event ID that is the last element in the Uri
    long accountID = Long.parseLong(accountUri.getLastPathSegment());
    DEF.setting().putLong(SystemCalendarNameSpace.SYSTEM_CALENDAR_ACCOUNT_ID, accountID);
    return accountID;
  }

  /**
   * 删除日历账户
   */
  public static String deleteAccount(@NonNull Context context) {
    ContentResolver cr = context.getContentResolver();
    Uri uri = CalendarContract.Calendars.CONTENT_URI;
    StringBuilder sb = new StringBuilder();
    int deletedCount = 0;

    String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
        + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?))";
    String[] selectionArgs = new String[]{"测试AccountName", CalendarContract.ACCOUNT_TYPE_LOCAL};

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (PackageManager.PERMISSION_GRANTED == context
          .checkSelfPermission("android.permission.WRITE_CALENDAR")) {
        deletedCount = cr.delete(uri, selection, selectionArgs);
      } else {
        return "请授予编辑日历的权限";
      }
    } else {
      deletedCount = cr.delete(uri, selection, selectionArgs);
    }
    sb.append("删除日历账户成功！\n");
    sb.append("删除账户数:" + deletedCount).append("\n");
    sb.append("\n");

    return sb.toString();
  }

  /**
   * 更新日历账户
   */
  public static String updateAccount(@NonNull Context context) {
    ContentResolver cr = context.getContentResolver();
    Uri uri = CalendarContract.Calendars.CONTENT_URI;
    StringBuilder sb = new StringBuilder();
    int updatedCount = 0;

    String accountName = "更新测试AccountName";

    ContentValues values = new ContentValues();
    values.put(CalendarContract.Calendars.ACCOUNT_NAME, accountName);
    // 在添加账户时，如果账户类型不存在系统中，则可能该新增记录会被标记为脏数据而被删除，
    // 设置为ACCOUNT_TYPE_LOCAL可以保证在不存在账户类型时，该新增数据不会被删除；
    values.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
    values.put(CalendarContract.Calendars.NAME, "更新name");
    values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "更新测试账户");
    values.put(CalendarContract.Calendars.CALENDAR_COLOR, 0X55FF55);
    values.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
        CalendarContract.Calendars.CAL_ACCESS_OWNER);
    values.put(CalendarContract.Calendars.VISIBLE, 1);
    values.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, TimeZone.getDefault().getID());
    values.put(CalendarContract.Calendars.CAN_MODIFY_TIME_ZONE, 1);
    values.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
    values.put(CalendarContract.Calendars.OWNER_ACCOUNT, "更新测试OwnerAccount");
    values.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 1);
    values.put(CalendarContract.Calendars.MAX_REMINDERS, 8);
    values.put(CalendarContract.Calendars.ALLOWED_REMINDERS, "0,1,4");
    values.put(CalendarContract.Calendars.ALLOWED_AVAILABILITY, "0,1,2");
    values.put(CalendarContract.Calendars.ALLOWED_ATTENDEE_TYPES, "0,1,2,3");
    values.put(CalendarContract.Calendars.IS_PRIMARY, 1);
    values.put(CalendarContract.Calendars.DIRTY, 1);

    String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
        + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?))";
    String[] selectionArgs = new String[]{"测试AccountName", CalendarContract.ACCOUNT_TYPE_LOCAL};

    //修改或添加ACCOUNT_NAME只能由syn-adapter调用，对uri设置CalendarContract.CALLER_IS_SYNCADAPTER为true，即标记当前操作为syn_adapter操作；
    //再设置CalendarContract.CALLER_IS_SYNCADAPTER为true时，必须带上参数ACCOUNT_NAME和ACCOUNT_TYPE，至于这两个参数的值，可以随意填，不影响最终结果；
    uri = uri.buildUpon().appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
        .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "Test")
        .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,
            CalendarContract.ACCOUNT_TYPE_LOCAL).build();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (PackageManager.PERMISSION_GRANTED == context
          .checkSelfPermission("android.permission.WRITE_CALENDAR")) {
        updatedCount = cr.update(uri, values, selection, selectionArgs);
      } else {
        return "请授予编辑日历的权限";
      }
    } else {
      updatedCount = cr.update(uri, values, selection, selectionArgs);
    }

    sb.append("更新日历账户成功！\n");
    sb.append("更新账户数:" + updatedCount).append("\n");
    sb.append("\n");

    return sb.toString();
  }

  /**
   * 查询所有的日历账户
   */
  public static List<SystemCalendarAccount> queryAllAccount(@NonNull Context context) {
    List<SystemCalendarAccount> accountList = new ArrayList<>();
    String[] EVENT_PROJECTION = new String[]{
        CalendarContract.Calendars._ID,                      // 0
        CalendarContract.Calendars.ACCOUNT_NAME,             // 1
        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,    // 2
        CalendarContract.Calendars.CALENDAR_COLOR,           // 3
        CalendarContract.Calendars.CALENDAR_COLOR_KEY,       // 4
        CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,    // 5
        CalendarContract.Calendars.VISIBLE,                  // 6
        CalendarContract.Calendars.CALENDAR_TIME_ZONE,       // 7
        CalendarContract.Calendars.SYNC_EVENTS,              // 8
        CalendarContract.Calendars.OWNER_ACCOUNT,            // 9
        CalendarContract.Calendars.CAN_ORGANIZER_RESPOND,    // 10
        CalendarContract.Calendars.CAN_MODIFY_TIME_ZONE,     // 11
        CalendarContract.Calendars.MAX_REMINDERS,            // 12
        CalendarContract.Calendars.ALLOWED_REMINDERS,        // 13
        CalendarContract.Calendars.ALLOWED_AVAILABILITY,     // 14
        CalendarContract.Calendars.ALLOWED_ATTENDEE_TYPES,   // 15
        CalendarContract.Calendars.IS_PRIMARY,               // 16
        CalendarContract.Calendars.ACCOUNT_TYPE,             // 17
        CalendarContract.Calendars.NAME,                     // 18
        CalendarContract.Calendars.DIRTY                     // 19

    };
    // Run query
    Cursor cur = null;
    ContentResolver cr = context.getContentResolver();
    Uri uri = CalendarContract.Calendars.CONTENT_URI;

    //        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
    //                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?))";
    //        String[] selectionArgs = new String[]{"sampleuser@gmail.com", "com.google"};

    String selection = null;
    String[] selectionArgs = null;

    // Submit the query and get a Cursor object back.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (PackageManager.PERMISSION_GRANTED == context
          .checkSelfPermission("android.permission.READ_CALENDAR")) {
        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
      } else {
        LogUtils.e("请授予读取日历的权限");
        return accountList;
      }
    } else {
      cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
    }

    if (cur == null) {
      return accountList;
    }
    // Use the cursor to step through the returned records
    while (cur.moveToNext()) {
      // Get the field values
      SystemCalendarAccount account = new SystemCalendarAccount();
      account.calID = cur.getLong(0);
      account.accountName = cur.getString(1);
      account.displayName = cur.getString(2);
      account.calendarColor = cur.getInt(3);
      account.calendarAccessLevel = cur.getInt(5);
      account.visible = cur.getInt(6);
      account.calendarTimezone = cur.getString(7);
      account.syncEvents = cur.getInt(8);
      account.ownerAccount = cur.getString(9);
      account.canOrganizerRespond = cur.getInt(10);
      account.maxReminders = cur.getInt(12);
      account.allowedReminders = cur.getString(13);
      account.allowedAvailability = cur.getString(14);
      account.allowedAttendeeTypes = cur.getString(15);
      account.isPrimary = cur.getInt(16);
      account.accountType = cur.getString(17);
      account.name = cur.getString(18);
      account.dirty = cur.getInt(19);
    }
    cur.close();
    return accountList;
  }


  /**
   * 查询所有的日历账户
   */
  public static long queryTimeCatAccountId(@NonNull Context context) {
    long calendarAccountId = -1;
    String[] EVENT_PROJECTION = new String[]{
        CalendarContract.Calendars._ID,                      // 0
        CalendarContract.Calendars.ACCOUNT_NAME,             // 1

    };
    // Run query
    Cursor cur = null;
    ContentResolver cr = context.getContentResolver();
    Uri uri = CalendarContract.Calendars.CONTENT_URI;

    String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
        + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?))";
    String[] selectionArgs = new String[]{"时光猫", CalendarContract.ACCOUNT_TYPE_LOCAL};

    // Submit the query and get a Cursor object back.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (PackageManager.PERMISSION_GRANTED == context
          .checkSelfPermission("android.permission.READ_CALENDAR")) {
        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
      } else {
        LogUtils.e("请授予读取日历的权限");
        return calendarAccountId;
      }
    } else {
      cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
    }

    if (cur == null) {
      return calendarAccountId;
    }
    // Use the cursor to step through the returned records
    while (cur.moveToNext()) {
      // Get the field values
      if ("时光猫".equals(cur.getString(1))) {
        calendarAccountId = cur.getLong(0);
      }
    }
    cur.close();
    return calendarAccountId;
  }

  /**
   * 插入日历事件及提醒
   *
   * @param calID
   *     插入日历事件的账户
   */
  public static long insertEvent(@NonNull Context context, long calID, @NonNull Event event) {
    /*
    以下是针对插入一个新的事件的一些规则：
    1.  必须包含CALENDAR_ID和DTSTART字段
    2.  必须包含EVENT_TIMEZONE字段。使用getAvailableIDs()方法获得系统已安装的时区ID列表。注意如果通过INSTERT类型Intent对象来插入事件，那么这个规则不适用，因为在INSERT对象的场景中会提供一个默认的时区；
    3.  对于非重复发生的事件，必须包含DTEND字段；
    4.  对重复发生的事件，必须包含一个附加了RRULE或RDATE字段的DURATIION字段。注意，如果通过INSERT类型的Intent对象来插入一个事件，这个规则不适用。因为在这个Intent对象的应用场景中，你能够把RRULE、DTSTART和DTEND字段联合在一起使用，并且Calendar应用程序能够自动的把它转换成一个持续的时间。
     */
    StringBuilder sb = new StringBuilder();

    String title = "测试日历事件";

    ContentResolver cr = context.getContentResolver();
    ContentValues values = new ContentValues();
    values.put(CalendarContract.Events.TITLE, event.TITLE);
    values.put(CalendarContract.Events.DESCRIPTION, event.DESCRIPTION);
    values.put(CalendarContract.Events.EVENT_LOCATION, event.EVENT_LOCATION);
    values.put(CalendarContract.Events.ORGANIZER, event.ORGANIZER);
    values.put(CalendarContract.Events.EVENT_TIMEZONE, event.EVENT_TIMEZONE);
    values.put(CalendarContract.Events.CALENDAR_ID, event.CALENDAR_ID);
    values.put(CalendarContract.Events.DTSTART, event.DTSTART);
    values.put(CalendarContract.Events.DTEND, event.DTEND);
    values.put(CalendarContract.Events.ACCESS_LEVEL, event.ACCESS_LEVEL);
    values.put(CalendarContract.Events.ALL_DAY, event.ALL_DAY);
    values.put(CalendarContract.Events.STATUS, event.STATUS);
    values.put(CalendarContract.Events.HAS_ALARM, event.HAS_ALARM);
    values.put(CalendarContract.Events.HAS_ATTENDEE_DATA, event.HAS_ATTENDEE_DATA);

    Uri uri = CalendarContract.Events.CONTENT_URI;
    Uri eventUri;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (PackageManager.PERMISSION_GRANTED == context
          .checkSelfPermission("android.permission.WRITE_CALENDAR")) {
        eventUri = cr.insert(uri, values);
      } else {
        LogUtils.e("请授予读取日历的权限");
        return -1;
      }
    } else {
      eventUri = cr.insert(uri, values);
    }

    if (eventUri == null) {
      LogUtils.e("accountUri == null");
      return -1;
    }
    // get the event ID that is the last element in the Uri
    long eventID = Long.parseLong(eventUri.getLastPathSegment());

    // 事件提醒的设定
    ContentValues reminderValues = new ContentValues();
    reminderValues.put(CalendarContract.Reminders.EVENT_ID, eventID);
    reminderValues.put(CalendarContract.Reminders.MINUTES, event.MINUTES);// 提前10分钟有提醒
    reminderValues
        .put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);// 提醒方式
    Uri reminderUri = cr.insert(CalendarContract.Reminders.CONTENT_URI, reminderValues);

    if (reminderUri != null) {
      long reminderID = Long.parseLong(reminderUri.getLastPathSegment());

      sb.append("添加日历事件成功！\n");
      sb.append("eventID:" + eventID).append("\n");
      sb.append("reminderID:" + reminderID).append("\n");
      sb.append("calID:" + calID).append("\n");
      sb.append("title:" + event.TITLE).append("\n");
      sb.append("\n");
      //TODO DEF.setting().putLong(SystemCalendarNameSpace.SYSTEM_CALENDAR_ACCOUNT_ID, eventID);
      LogUtils.e(sb.toString());
    }
    return eventID;
  }

  /**
   * 删除指定ID的日历事件及其提醒
   */
  public static String deleteEvent(@NonNull Context context, long eventId) {
    ContentResolver cr = context.getContentResolver();
    Uri uri = CalendarContract.Events.CONTENT_URI;
    StringBuilder sb = new StringBuilder();
    int deletedCount = 0;

    String selection = "(" + CalendarContract.Events._ID + " = ?)";
    String[] selectionArgs = new String[]{String.valueOf(eventId)};

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (PackageManager.PERMISSION_GRANTED == context
          .checkSelfPermission("android.permission.WRITE_CALENDAR")) {
        deletedCount = cr.delete(uri, selection, selectionArgs);
      } else {
        return "请授予编辑日历的权限";
      }
    } else {
      deletedCount = cr.delete(uri, selection, selectionArgs);
    }

    String reminderSelection = "(" + CalendarContract.Reminders.EVENT_ID + " = ?)";
    String[] reminderSelectionArgs = new String[]{String.valueOf(eventId)};

    int deletedReminderCount = cr
        .delete(CalendarContract.Reminders.CONTENT_URI, reminderSelection, reminderSelectionArgs);

    sb.append("删除日历事件成功！\n");
    sb.append("删除事件数:" + deletedCount).append("\n");
    sb.append("删除事件提醒数:" + deletedReminderCount).append("\n");
    sb.append("\n");

    return sb.toString();
  }

  /**
   * 更新指定ID的日历事件及其提醒
   */
  public static String updateEvent(@NonNull Context context, long eventID) {
    ContentResolver cr = context.getContentResolver();
    Uri uri = CalendarContract.Events.CONTENT_URI;
    StringBuilder sb = new StringBuilder();
    int updatedCount = 0;

    String title = "更新测试日历事件";

    long startMillis = 0;
    long endMillis = 0;
    Calendar beginTime = Calendar.getInstance();
    beginTime.set(2016, 4, 29, 16, 15);
    startMillis = beginTime.getTimeInMillis();
    Calendar endTime = Calendar.getInstance();
    endTime.set(2016, 4, 29, 16, 20);
    endMillis = endTime.getTimeInMillis();

    ContentValues values = new ContentValues();
    //        values.put(CalendarContract.Events.CALENDAR_ID, calID);
    values.put(CalendarContract.Events.DTSTART, startMillis);
    values.put(CalendarContract.Events.DTEND, endMillis);
    values.put(CalendarContract.Events.TITLE, title);
    values.put(CalendarContract.Events.DESCRIPTION, "更新有一次Android进阶讲座，快去职学堂APP");
    values.put(CalendarContract.Events.EVENT_LOCATION, "更新新模范马路");
    values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
    values.put(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_DEFAULT);
    values.put(CalendarContract.Events.ALL_DAY, 1);
    values.put(CalendarContract.Events.ORGANIZER, "499083701@qq.com");
    values.put(CalendarContract.Events.STATUS, 1);
    values.put(CalendarContract.Events.HAS_ALARM, 1);
    values.put(CalendarContract.Events.HAS_ATTENDEE_DATA, 1);

    String selection = "(" + CalendarContract.Events._ID + " = ?)";
    String[] selectionArgs = new String[]{String.valueOf(eventID)};

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (PackageManager.PERMISSION_GRANTED == context
          .checkSelfPermission("android.permission.WRITE_CALENDAR")) {
        updatedCount = cr.update(uri, values, selection, selectionArgs);
      } else {
        return "请授予编辑日历的权限";
      }
    } else {
      updatedCount = cr.update(uri, values, selection, selectionArgs);
    }

    // 事件提醒的设定
    ContentValues reminderValues = new ContentValues();
    reminderValues.put(CalendarContract.Reminders.MINUTES, 20);// 提前10分钟有提醒
    reminderValues
        .put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);// 提醒方式

    String reminderSelection = "(" + CalendarContract.Reminders.EVENT_ID + " = ?)";
    String[] reminderSelectionArgs = new String[]{String.valueOf(eventID)};

    int updatedReminderCount = cr
        .update(CalendarContract.Reminders.CONTENT_URI, reminderValues, reminderSelection,
            reminderSelectionArgs);

    sb.append("更新日历事件成功！\n");
    sb.append("更新事件数:" + updatedCount).append("\n");
    sb.append("更新事件提醒数:" + updatedReminderCount).append("\n");
    sb.append("\n");

    return sb.toString();
  }

  /**
   * 查询指定日历账户下的日历事件及提醒
   */
  public static List<SystemCalendarEvent> queryAllEvent(@NonNull Context context, long calendarID) {
    List<SystemCalendarEvent> eventList = new ArrayList<>();
    String[] EVENT_PROJECTION = new String[]{
        CalendarContract.Events.CALENDAR_ID,            // 0
        CalendarContract.Events.TITLE,                  // 1
        CalendarContract.Events.DESCRIPTION,            // 2
        CalendarContract.Events.EVENT_LOCATION,         // 3
        CalendarContract.Events.DISPLAY_COLOR,          // 4
        CalendarContract.Events.STATUS,                 // 5
        CalendarContract.Events.DTSTART,                // 6
        CalendarContract.Events.DTEND,                  // 7
        CalendarContract.Events.DURATION,               // 8
        CalendarContract.Events.EVENT_TIMEZONE,         // 9
        CalendarContract.Events.EVENT_END_TIMEZONE,     // 10
        CalendarContract.Events.ALL_DAY,                // 11
        CalendarContract.Events.ACCESS_LEVEL,           // 12
        CalendarContract.Events.AVAILABILITY,           // 13
        CalendarContract.Events.HAS_ALARM,              // 14
        CalendarContract.Events.RRULE,                  // 15
        CalendarContract.Events.RDATE,                  // 16
        CalendarContract.Events.HAS_ATTENDEE_DATA,      // 17
        CalendarContract.Events.LAST_DATE,              // 18
        CalendarContract.Events.ORGANIZER,              // 19
        CalendarContract.Events.IS_ORGANIZER,           // 20
        CalendarContract.Events._ID                     // 21
    };
    // Run query
    Cursor cur = null;
    ContentResolver cr = context.getContentResolver();
    Uri uri = CalendarContract.Events.CONTENT_URI;

    StringBuilder sb = new StringBuilder();

    String selection = "(" + CalendarContract.Events.CALENDAR_ID + " = ?)";
    String[] selectionArgs = new String[]{String.valueOf(calendarID)};

    //        String selection = null;
    //        String[] selectionArgs = null;

    // Submit the query and get a Cursor object back.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (PackageManager.PERMISSION_GRANTED == context
          .checkSelfPermission("android.permission.READ_CALENDAR")) {
        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
      } else {
        LogUtils.e("请授予读取日历的权限");
        return eventList;
      }
    } else {
      cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
    }

    if (cur == null) {
      return eventList;
    }
    // Use the cursor to step through the returned records
    while (cur.moveToNext()) {
      SystemCalendarEvent event = new SystemCalendarEvent();
      // Get the field values
      event.id = cur.getLong(21);
      event.calID = cur.getLong(0);
      event.title = cur.getString(1);
      event.description = cur.getString(2);
      event.eventLocation = cur.getString(3);
      event.displayColor = cur.getInt(4);
      event.status = cur.getInt(5);
      event.start = cur.getLong(6);
      event.end = cur.getLong(7);
      event.duration = cur.getString(8);
      event.eventTimeZone = cur.getString(9);
      event.eventEndTimeZone = cur.getString(10);
      event.allDay = cur.getInt(11);
      event.accessLevel = cur.getInt(12);
      event.availability = cur.getInt(13);
      event.hasAlarm = cur.getInt(14);
      event.rrule = cur.getString(15);
      event.rdate = cur.getString(16);
      event.hasAttendeeData = cur.getInt(17);
      event.lastDate = cur.getInt(18);
      event.organizer = cur.getString(19);
      event.isOrganizer = cur.getString(20);

      String[] REMINDER_PROJECTION = new String[]{
          CalendarContract.Reminders._ID,           // 0
          CalendarContract.Reminders.EVENT_ID,      // 1
          CalendarContract.Reminders.MINUTES,       // 2
          CalendarContract.Reminders.METHOD,        // 3
      };
      String reminderSelection = "(" + CalendarContract.Reminders.EVENT_ID + " = ?)";
      String[] reminderSelectionArgs = new String[]{String.valueOf(event.id)};

      Cursor reminderCur = cr.query(CalendarContract.Reminders.CONTENT_URI,
          REMINDER_PROJECTION, reminderSelection, reminderSelectionArgs, null);

      if (reminderCur == null) {
        continue;
      }
      while (reminderCur.moveToNext()) {
        SystemCalendarReminder reminder = new SystemCalendarReminder();
        reminder.reminderId = reminderCur.getLong(0);
        reminder.reminderEventID = reminderCur.getLong(1);
        reminder.reminderMinute = reminderCur.getInt(2);
        reminder.reminderMethod = reminderCur.getInt(3);
        event.reminderList.add(reminder);
      }
      reminderCur.close();
    }
    cur.close();
    return eventList;
  }
}
