package com.timecat.component.setting

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.media.AudioManager
import android.net.Uri
import android.os.Environment
import android.text.format.DateFormat
import com.timecat.extend.arms.BaseApplication
import com.timecat.identity.readonly.Constants
import com.timecat.identity.readonly.Constants.*
import java.text.SimpleDateFormat
import java.util.*

open class Config(val context: Context) {
    protected val prefs: SharedPreferences =
        context.getSharedPreferences("Prefs", Context.MODE_PRIVATE)

    companion object {
        fun newInstance(context: Context) = Config(context)
        fun obj() = Config(BaseApplication.getContext())

        const val SIDELOADING_UNCHECKED = 0
        const val SIDELOADING_TRUE = 1
        const val SIDELOADING_FALSE = 2

        // conflict resolving
        const val CONFLICT_SKIP = 1
        const val CONFLICT_OVERWRITE = 2
        const val CONFLICT_MERGE = 3
        const val CONFLICT_KEEP_BOTH = 4

        // sorting
        const val SORT_ORDER = "sort_order"
        const val SORT_FOLDER_PREFIX = "sort_folder_"       // storing folder specific values at using "Use for this folder only"
        const val SORT_BY_NAME = 1
        const val SORT_BY_DATE_MODIFIED = 2
        const val SORT_BY_SIZE = 4
        const val SORT_BY_DATE_TAKEN = 8
        const val SORT_BY_EXTENSION = 16
        const val SORT_BY_PATH = 32
        const val SORT_BY_NUMBER = 64
        const val SORT_BY_FIRST_NAME = 128
        const val SORT_BY_MIDDLE_NAME = 256
        const val SORT_BY_SURNAME = 512
        const val SORT_DESCENDING = 1024
        const val SORT_BY_TITLE = 2048
        const val SORT_BY_ARTIST = 4096
        const val SORT_BY_DURATION = 8192
        const val SORT_BY_RANDOM = 16384
        const val SORT_USE_NUMERIC_VALUE = 32768
        const val SORT_BY_FULL_NAME = 65536

        // security
        const val WAS_PROTECTION_HANDLED = "was_protection_handled"
        const val PROTECTION_NONE = -1
        const val PROTECTION_PATTERN = 0
        const val PROTECTION_PIN = 1
        const val PROTECTION_FINGERPRINT = 2
    }

    //<editor-fold desc="暴露到外部的方法">
    fun save(name: String, value: Boolean) = prefs.edit().putBoolean(name, value).apply()

    fun save(name: String, value: Long) = prefs.edit().putLong(name, value).apply()
    fun save(name: String, value: Float) = prefs.edit().putFloat(name, value).apply()
    fun save(name: String, value: Int) = prefs.edit().putInt(name, value).apply()
    fun save(name: String, value: String) = prefs.edit().putString(name, value).apply()
    fun save(name: String, value: Set<String>) = prefs.edit().putStringSet(name, value).apply()

    fun getBoolean(name: String, defaultValue: Boolean) = prefs.getBoolean(name, defaultValue)
    fun getInt(name: String, defaultValue: Int) = prefs.getInt(name, defaultValue)
    fun getLong(name: String, defaultValue: Long) = prefs.getLong(name, defaultValue)
    fun getFloat(name: String, defaultValue: Float) = prefs.getFloat(name, defaultValue)
    fun getString(name: String, defaultValue: String) =
        prefs.getString(name, defaultValue) ?: defaultValue

    fun getStringSet(name: String, defaultValue: Set<String>) =
        prefs.getStringSet(name, defaultValue)

    fun getBoolean(name: String) = prefs.getBoolean(name, false)
    fun getInt(name: String) = prefs.getInt(name, 0)
    fun getLong(name: String) = prefs.getLong(name, 0)
    fun getFloat(name: String) = prefs.getFloat(name, 0.0F)
    fun getString(name: String) = prefs.getString(name, null)
    fun getStringSet(name: String) = prefs.getStringSet(name, null)

    fun remove(name: String) = prefs.edit().remove(name).apply()
    operator fun contains(name: String): Boolean {
        val cr = context.contentResolver
        val uri =
            Uri.parse(Constants.CONTENT_URI + Constants.SEPARATOR + Constants.TYPE_CONTAIN + Constants.SEPARATOR + name)
        val rtn = cr.getType(uri)
        return if (rtn == null || rtn == Constants.NULL_STRING) {
            false
        } else {
            java.lang.Boolean.parseBoolean(rtn)
        }
    }

    fun clear() = prefs.edit().clear().apply()

    fun getAll(): Map<String, *> = prefs.all
    //</editor-fold>

    //<editor-fold desc="API">
    var baiduOcrSecretKey: String
        get() = getString(Constants.BAIDU_OCR_SECRET_KEY, "gGnYYoOBFPDZuiiurZB2ERFh0dulvHWs")
        set(value) = save(Constants.BAIDU_OCR_SECRET_KEY, value)
    var baiduOcrApiKey: String
        get() = getString(Constants.BAIDU_OCR_API_KEY, "q3pDSwFgw2aESWnCoosjZBK1")
        set(value) = save(Constants.BAIDU_OCR_API_KEY, value)
    var serverAddress: String
        get() = getString(Constants.SERVER_ADDRESS, "http://192.168.88.107:8000/")
        set(value) = save(Constants.SERVER_ADDRESS, value)
    //</editor-fold>

    //<editor-fold desc="模块">
    var openMiniAppModule: Boolean
        get() = getBoolean(Constants.OPEN_MINI_APP_MODULE, true)
        set(value) = save(Constants.OPEN_MINI_APP_MODULE, value)
    var openLabModule: Boolean
        get() = getBoolean(Constants.OPEN_LAB_MODULE, true)
        set(value) = save(Constants.OPEN_LAB_MODULE, value)
    var openMiaoKeyModule: Boolean
        get() = getBoolean(Constants.OPEN_MIAO_KEY_MODULE, true)
        set(value) = save(Constants.OPEN_MIAO_KEY_MODULE, value)
    //</editor-fold>

    //<editor-fold desc="首次启动">
    var firstHabitMultiView: Boolean
        get() = getBoolean(Constants.FIRST_HABIT_MULTI_VIEW, false)
        set(value) = save(Constants.FIRST_HABIT_MULTI_VIEW, value)
    //</editor-fold>

    //<editor-fold desc="存储">
    var screenshotPath: String
        get() = getString(
            Constants.SCREEN_SHOT_PATH,
            Environment.getExternalStorageDirectory().getPath() + "/timecat/image/screenshot/"
        )
        set(value) = save(Constants.SCREEN_SHOT_PATH, value)
    var bilibiliCover: String
        get() = getString(
            Constants.BILIBILI_COVER_PATH,
            Environment.getExternalStorageDirectory().getPath() + "/timecat/image/bilibili_cover/"
        )
        set(value) = save(Constants.BILIBILI_COVER_PATH, value)
    //</editor-fold>

    //<editor-fold desc="通知数据">
    var todayTasksCount: Int
        get() = getInt(Constants.TODAY_TASKS_COUNT, -1)
        set(value) = save(Constants.TODAY_TASKS_COUNT, value)
    var todayPhoneTime: Long
        get() = getLong(Constants.TODAY_PHONE_TIME, -1L)
        set(value) = save(Constants.TODAY_PHONE_TIME, value)
    //</editor-fold>

    //<editor-fold desc="通知">
    var customNotificationView: Int
        get() = getInt(Constants.CUSTOM_NOTIFICATION_VIEW, 0)
        set(value) = save(Constants.CUSTOM_NOTIFICATION_VIEW, value)

    var settingWhiteCustomNotificationView: Int
        get() = getInt(Constants.SETTING_WHITE_CUSTOM_NOTIFICATION_VIEW, 0)
        set(value) = save(Constants.SETTING_WHITE_CUSTOM_NOTIFICATION_VIEW, value)

    var enhanceNotificationStyle: Int
        get() = getInt(Constants.ENHANCE_NOTIFICATION_STYLE, 1)
        set(value) = save(Constants.ENHANCE_NOTIFICATION_STYLE, value)
    //</editor-fold>

    //<editor-fold desc="数据视图">
    var showSystemApp: Boolean
        get() = getBoolean(Constants.SHOW_SYSTEM_APPS, true)
        set(value) = save(Constants.SHOW_SYSTEM_APPS, value)

    //</editor-fold>

    //<editor-fold desc="星期视图">
    var preFilledWeeks: Int
        get() = getInt(Constants.PREFILLED_WEEKS, 1)
        set(value) = save(Constants.PREFILLED_WEEKS, value)

    var use24hourFormat: Boolean
        get() {
            val use24hourFormat = DateFormat.is24HourFormat(context)
            return getBoolean(Constants.USE_24_HOUR_FORMAT, use24hourFormat)
        }
        set(use24hourFormat) = save(Constants.USE_24_HOUR_FORMAT, use24hourFormat)

    var endWeeklyAt: Int
        get() = getInt(Constants.END_WEEKLY_AT, 24)
        set(endWeeklyAt) = save(Constants.END_WEEKLY_AT, endWeeklyAt)
    //</editor-fold>


    var showWeekNumbers: Boolean
        get() = prefs.getBoolean(WEEK_NUMBERS, false)
        set(showWeekNumbers) = prefs.edit().putBoolean(WEEK_NUMBERS, showWeekNumbers).apply()

    var startWeeklyAt: Int
        get() = prefs.getInt(START_WEEKLY_AT, 7)
        set(startWeeklyAt) = prefs.edit().putInt(START_WEEKLY_AT, startWeeklyAt).apply()

    var vibrateOnReminder: Boolean
        get() = prefs.getBoolean(VIBRATE, false)
        set(vibrate) = prefs.edit().putBoolean(VIBRATE, vibrate).apply()

    var storedView: Int
        get() = prefs.getInt(VIEW, MONTHLY_VIEW)
        set(view) = prefs.edit().putInt(VIEW, view).apply()

    var displayPastEvents: Int
        get() = prefs.getInt(DISPLAY_PAST_EVENTS, DAY_MINUTES)
        set(displayPastEvents) = prefs.edit().putInt(DISPLAY_PAST_EVENTS, displayPastEvents).apply()

    var displayEventTypes: Set<String>
        get() = prefs.getStringSet(DISPLAY_EVENT_TYPES, HashSet<String>())!!
        set(displayEventTypes) = prefs.edit().remove(DISPLAY_EVENT_TYPES)
            .putStringSet(DISPLAY_EVENT_TYPES, displayEventTypes).apply()

    var listWidgetViewToOpen: Int
        get() = prefs.getInt(LIST_WIDGET_VIEW_TO_OPEN, DAILY_VIEW)
        set(viewToOpenFromListWidget) = prefs.edit()
            .putInt(LIST_WIDGET_VIEW_TO_OPEN, viewToOpenFromListWidget).apply()

    var caldavSync: Boolean
        get() = prefs.getBoolean(CALDAV_SYNC, false)
        set(caldavSync) {
            prefs.edit().putBoolean(CALDAV_SYNC, caldavSync).apply()
        }

    var caldavSyncedCalendarIds: String
        get() = prefs.getString(CALDAV_SYNCED_CALENDAR_IDS, "")!!
        set(calendarIDs) = prefs.edit().putString(CALDAV_SYNCED_CALENDAR_IDS, calendarIDs).apply()

    var lastUsedCaldavCalendarId: Int
        get() = prefs.getInt(
            LAST_USED_CALDAV_CALENDAR,
            getSyncedCalendarIdsAsList().first().toInt()
        )
        set(calendarId) = prefs.edit().putInt(LAST_USED_CALDAV_CALENDAR, calendarId).apply()

    var lastUsedLocalEventTypeId: Long
        get() = prefs.getLong(LAST_USED_LOCAL_EVENT_TYPE_ID, REGULAR_EVENT_TYPE_ID)
        set(lastUsedLocalEventTypeId) = prefs.edit()
            .putLong(LAST_USED_LOCAL_EVENT_TYPE_ID, lastUsedLocalEventTypeId).apply()

    var reminderAudioStream: Int
        get() = prefs.getInt(REMINDER_AUDIO_STREAM, AudioManager.STREAM_ALARM)
        set(reminderAudioStream) = prefs.edit().putInt(REMINDER_AUDIO_STREAM, reminderAudioStream)
            .apply()

    var replaceDescription: Boolean
        get() = prefs.getBoolean(REPLACE_DESCRIPTION, false)
        set(replaceDescription) = prefs.edit().putBoolean(REPLACE_DESCRIPTION, replaceDescription)
            .apply()

    var showGrid: Boolean
        get() = prefs.getBoolean(SHOW_GRID, false)
        set(showGrid) = prefs.edit().putBoolean(SHOW_GRID, showGrid).apply()

    var loopReminders: Boolean
        get() = prefs.getBoolean(LOOP_REMINDERS, false)
        set(loopReminders) = prefs.edit().putBoolean(LOOP_REMINDERS, loopReminders).apply()

    var dimPastEvents: Boolean
        get() = prefs.getBoolean(DIM_PAST_EVENTS, true)
        set(dimPastEvents) = prefs.edit().putBoolean(DIM_PAST_EVENTS, dimPastEvents).apply()

    fun getSyncedCalendarIdsAsList() =
        caldavSyncedCalendarIds.split(",").filter { it.trim().isNotEmpty() }
            .map { Integer.parseInt(it) }.toMutableList() as ArrayList<Int>

    fun getDisplayEventTypessAsList() =
        displayEventTypes.map { it.toLong() }.toMutableList() as ArrayList<Long>

    fun addDisplayEventType(type: String) {
        addDisplayEventTypes(HashSet<String>(Arrays.asList(type)))
    }

    private fun addDisplayEventTypes(types: Set<String>) {
        val currDisplayEventTypes = HashSet<String>(displayEventTypes)
        currDisplayEventTypes.addAll(types)
        displayEventTypes = currDisplayEventTypes
    }

    fun removeDisplayEventTypes(types: Set<String>) {
        val currDisplayEventTypes = HashSet<String>(displayEventTypes)
        currDisplayEventTypes.removeAll(types)
        displayEventTypes = currDisplayEventTypes
    }

    var usePreviousEventReminders: Boolean
        get() = prefs.getBoolean(USE_PREVIOUS_EVENT_REMINDERS, true)
        set(usePreviousEventReminders) = prefs.edit()
            .putBoolean(USE_PREVIOUS_EVENT_REMINDERS, usePreviousEventReminders).apply()

    var defaultReminder1: Int
        get() = prefs.getInt(DEFAULT_REMINDER_1, 10)
        set(defaultReminder1) = prefs.edit().putInt(DEFAULT_REMINDER_1, defaultReminder1).apply()

    var defaultReminder2: Int
        get() = prefs.getInt(DEFAULT_REMINDER_2, REMINDER_OFF)
        set(defaultReminder2) = prefs.edit().putInt(DEFAULT_REMINDER_2, defaultReminder2).apply()

    var defaultReminder3: Int
        get() = prefs.getInt(DEFAULT_REMINDER_3, REMINDER_OFF)
        set(defaultReminder3) = prefs.edit().putInt(DEFAULT_REMINDER_3, defaultReminder3).apply()

    var pullToRefresh: Boolean
        get() = prefs.getBoolean(PULL_TO_REFRESH, false)
        set(pullToRefresh) = prefs.edit().putBoolean(PULL_TO_REFRESH, pullToRefresh).apply()

    var lastVibrateOnReminder: Boolean
        get() = prefs.getBoolean(LAST_VIBRATE_ON_REMINDER, vibrateOnReminder)
        set(lastVibrateOnReminder) = prefs.edit()
            .putBoolean(LAST_VIBRATE_ON_REMINDER, lastVibrateOnReminder).apply()

    var defaultStartTime: Int
        get() = prefs.getInt(DEFAULT_START_TIME, -1)
        set(defaultStartTime) = prefs.edit().putInt(DEFAULT_START_TIME, defaultStartTime).apply()

    var defaultDuration: Int
        get() = prefs.getInt(DEFAULT_DURATION, 0)
        set(defaultDuration) = prefs.edit().putInt(DEFAULT_DURATION, defaultDuration).apply()

    var defaultEventTypeId: Long
        get() = prefs.getLong(DEFAULT_EVENT_TYPE_ID, -1L)
        set(defaultEventTypeId) = prefs.edit().putLong(DEFAULT_EVENT_TYPE_ID, defaultEventTypeId)
            .apply()

    var allowChangingTimeZones: Boolean
        get() = prefs.getBoolean(ALLOW_CHANGING_TIME_ZONES, false)
        set(allowChangingTimeZones) = prefs.edit()
            .putBoolean(ALLOW_CHANGING_TIME_ZONES, allowChangingTimeZones).apply()

    var lastExportPath: String
        get() = prefs.getString(LAST_EXPORT_PATH, "")!!
        set(lastExportPath) = prefs.edit().putString(LAST_EXPORT_PATH, lastExportPath).apply()

    var exportPastEvents: Boolean
        get() = prefs.getBoolean(EXPORT_PAST_EVENTS, false)
        set(exportPastEvents) = prefs.edit().putBoolean(EXPORT_PAST_EVENTS, exportPastEvents)
            .apply()

    var weeklyViewItemHeightMultiplier: Float
        get() = prefs.getFloat(WEEKLY_VIEW_ITEM_HEIGHT_MULTIPLIER, 1f)
        set(weeklyViewItemHeightMultiplier) = prefs.edit()
            .putFloat(WEEKLY_VIEW_ITEM_HEIGHT_MULTIPLIER, weeklyViewItemHeightMultiplier).apply()

    var weeklyViewDays: Int
        get() = prefs.getInt(WEEKLY_VIEW_DAYS, 7)
        set(weeklyViewDays) = prefs.edit().putInt(WEEKLY_VIEW_DAYS, weeklyViewDays).apply()


    var appRunCount: Int
        get() = prefs.getInt(APP_RUN_COUNT, 0)
        set(appRunCount) = prefs.edit().putInt(APP_RUN_COUNT, appRunCount).apply()

    var lastVersion: Int
        get() = prefs.getInt(LAST_VERSION, 0)
        set(lastVersion) = prefs.edit().putInt(LAST_VERSION, lastVersion).apply()

    var navigationBarColor: Int
        get() = prefs.getInt(NAVIGATION_BAR_COLOR, INVALID_NAVIGATION_BAR_COLOR)
        set(navigationBarColor) = prefs.edit().putInt(NAVIGATION_BAR_COLOR, navigationBarColor).apply()

    var defaultNavigationBarColor: Int
        get() = prefs.getInt(DEFAULT_NAVIGATION_BAR_COLOR, INVALID_NAVIGATION_BAR_COLOR)
        set(defaultNavigationBarColor) = prefs.edit().putInt(DEFAULT_NAVIGATION_BAR_COLOR, defaultNavigationBarColor).apply()

    var lastHandledShortcutColor: Int
        get() = prefs.getInt(LAST_HANDLED_SHORTCUT_COLOR, 1)
        set(lastHandledShortcutColor) = prefs.edit().putInt(LAST_HANDLED_SHORTCUT_COLOR, lastHandledShortcutColor).apply()

    var customNavigationBarColor: Int
        get() = prefs.getInt(CUSTOM_NAVIGATION_BAR_COLOR, INVALID_NAVIGATION_BAR_COLOR)
        set(customNavigationBarColor) = prefs.edit().putInt(CUSTOM_NAVIGATION_BAR_COLOR, customNavigationBarColor).apply()

    // hidden folder visibility protection
    var isHiddenPasswordProtectionOn: Boolean
        get() = prefs.getBoolean(PASSWORD_PROTECTION, false)
        set(isHiddenPasswordProtectionOn) = prefs.edit().putBoolean(PASSWORD_PROTECTION, isHiddenPasswordProtectionOn).apply()

    var hiddenPasswordHash: String
        get() = prefs.getString(PASSWORD_HASH, "")!!
        set(hiddenPasswordHash) = prefs.edit().putString(PASSWORD_HASH, hiddenPasswordHash).apply()

    var hiddenProtectionType: Int
        get() = prefs.getInt(PROTECTION_TYPE, PROTECTION_PATTERN)
        set(hiddenProtectionType) = prefs.edit().putInt(PROTECTION_TYPE, hiddenProtectionType).apply()

    // whole app launch protection
    var isAppPasswordProtectionOn: Boolean
        get() = prefs.getBoolean(APP_PASSWORD_PROTECTION, false)
        set(isAppPasswordProtectionOn) = prefs.edit().putBoolean(APP_PASSWORD_PROTECTION, isAppPasswordProtectionOn).apply()

    var appPasswordHash: String
        get() = prefs.getString(APP_PASSWORD_HASH, "")!!
        set(appPasswordHash) = prefs.edit().putString(APP_PASSWORD_HASH, appPasswordHash).apply()

    var appProtectionType: Int
        get() = prefs.getInt(APP_PROTECTION_TYPE, PROTECTION_PATTERN)
        set(appProtectionType) = prefs.edit().putInt(APP_PROTECTION_TYPE, appProtectionType).apply()

    // file delete and move protection
    var isDeletePasswordProtectionOn: Boolean
        get() = prefs.getBoolean(DELETE_PASSWORD_PROTECTION, false)
        set(isDeletePasswordProtectionOn) = prefs.edit().putBoolean(DELETE_PASSWORD_PROTECTION, isDeletePasswordProtectionOn).apply()

    var deletePasswordHash: String
        get() = prefs.getString(DELETE_PASSWORD_HASH, "")!!
        set(deletePasswordHash) = prefs.edit().putString(DELETE_PASSWORD_HASH, deletePasswordHash).apply()

    var deleteProtectionType: Int
        get() = prefs.getInt(DELETE_PROTECTION_TYPE, PROTECTION_PATTERN)
        set(deleteProtectionType) = prefs.edit().putInt(DELETE_PROTECTION_TYPE, deleteProtectionType).apply()

    // folder locking
    fun addFolderProtection(path: String, hash: String, type: Int) {
        prefs.edit()
            .putString("$PROTECTED_FOLDER_HASH$path", hash)
            .putInt("$PROTECTED_FOLDER_TYPE$path", type)
            .apply()
    }

    fun removeFolderProtection(path: String) {
        prefs.edit()
            .remove("$PROTECTED_FOLDER_HASH$path")
            .remove("$PROTECTED_FOLDER_TYPE$path")
            .apply()
    }

    fun isFolderProtected(path: String) = getFolderProtectionType(path) != PROTECTION_NONE

    fun getFolderProtectionHash(path: String) = prefs.getString("$PROTECTED_FOLDER_HASH$path", "") ?: ""

    fun getFolderProtectionType(path: String) = prefs.getInt("$PROTECTED_FOLDER_TYPE$path", PROTECTION_NONE)

    var keepLastModified: Boolean
        get() = prefs.getBoolean(KEEP_LAST_MODIFIED, true)
        set(keepLastModified) = prefs.edit().putBoolean(KEEP_LAST_MODIFIED, keepLastModified).apply()

    var useEnglish: Boolean
        get() = prefs.getBoolean(USE_ENGLISH, false)
        set(useEnglish) {
            wasUseEnglishToggled = true
            prefs.edit().putBoolean(USE_ENGLISH, useEnglish).commit()
        }

    var wasUseEnglishToggled: Boolean
        get() = prefs.getBoolean(WAS_USE_ENGLISH_TOGGLED, false)
        set(wasUseEnglishToggled) = prefs.edit().putBoolean(WAS_USE_ENGLISH_TOGGLED, wasUseEnglishToggled).apply()

    var wasSharedThemeEverActivated: Boolean
        get() = prefs.getBoolean(WAS_SHARED_THEME_EVER_ACTIVATED, false)
        set(wasSharedThemeEverActivated) = prefs.edit().putBoolean(WAS_SHARED_THEME_EVER_ACTIVATED, wasSharedThemeEverActivated).apply()

    var isUsingSharedTheme: Boolean
        get() = prefs.getBoolean(IS_USING_SHARED_THEME, false)
        set(isUsingSharedTheme) = prefs.edit().putBoolean(IS_USING_SHARED_THEME, isUsingSharedTheme).apply()

    var wasCustomThemeSwitchDescriptionShown: Boolean
        get() = prefs.getBoolean(WAS_CUSTOM_THEME_SWITCH_DESCRIPTION_SHOWN, false)
        set(wasCustomThemeSwitchDescriptionShown) = prefs.edit().putBoolean(WAS_CUSTOM_THEME_SWITCH_DESCRIPTION_SHOWN, wasCustomThemeSwitchDescriptionShown).apply()

    var wasSharedThemeForced: Boolean
        get() = prefs.getBoolean(WAS_SHARED_THEME_FORCED, false)
        set(wasSharedThemeForced) = prefs.edit().putBoolean(WAS_SHARED_THEME_FORCED, wasSharedThemeForced).apply()

    var showInfoBubble: Boolean
        get() = prefs.getBoolean(SHOW_INFO_BUBBLE, true)
        set(showInfoBubble) = prefs.edit().putBoolean(SHOW_INFO_BUBBLE, showInfoBubble).apply()

    var lastConflictApplyToAll: Boolean
        get() = prefs.getBoolean(LAST_CONFLICT_APPLY_TO_ALL, true)
        set(lastConflictApplyToAll) = prefs.edit().putBoolean(LAST_CONFLICT_APPLY_TO_ALL, lastConflictApplyToAll).apply()

    var lastConflictResolution: Int
        get() = prefs.getInt(LAST_CONFLICT_RESOLUTION, CONFLICT_SKIP)
        set(lastConflictResolution) = prefs.edit().putInt(LAST_CONFLICT_RESOLUTION, lastConflictResolution).apply()

    var sorting: Int
        get() = prefs.getInt(SORT_ORDER, SORT_BY_NAME)
        set(sorting) = prefs.edit().putInt(SORT_ORDER, sorting).apply()

    fun saveCustomSorting(path: String, value: Int) {
        if (path.isEmpty()) {
            sorting = value
        } else {
            prefs.edit().putInt(SORT_FOLDER_PREFIX + path.toLowerCase(), value).apply()
        }
    }

    fun getFolderSorting(path: String) = prefs.getInt(SORT_FOLDER_PREFIX + path.toLowerCase(), sorting)

    fun removeCustomSorting(path: String) {
        prefs.edit().remove(SORT_FOLDER_PREFIX + path.toLowerCase()).apply()
    }

    fun hasCustomSorting(path: String) = prefs.contains(SORT_FOLDER_PREFIX + path.toLowerCase())

    var hadThankYouInstalled: Boolean
        get() = prefs.getBoolean(HAD_THANK_YOU_INSTALLED, false)
        set(hadThankYouInstalled) = prefs.edit().putBoolean(HAD_THANK_YOU_INSTALLED, hadThankYouInstalled).apply()

    var skipDeleteConfirmation: Boolean
        get() = prefs.getBoolean(SKIP_DELETE_CONFIRMATION, false)
        set(skipDeleteConfirmation) = prefs.edit().putBoolean(SKIP_DELETE_CONFIRMATION, skipDeleteConfirmation).apply()

    var enablePullToRefresh: Boolean
        get() = prefs.getBoolean(ENABLE_PULL_TO_REFRESH, true)
        set(enablePullToRefresh) = prefs.edit().putBoolean(ENABLE_PULL_TO_REFRESH, enablePullToRefresh).apply()

    var scrollHorizontally: Boolean
        get() = prefs.getBoolean(SCROLL_HORIZONTALLY, false)
        set(scrollHorizontally) = prefs.edit().putBoolean(SCROLL_HORIZONTALLY, scrollHorizontally).apply()

    var preventPhoneFromSleeping: Boolean
        get() = prefs.getBoolean(PREVENT_PHONE_FROM_SLEEPING, true)
        set(preventPhoneFromSleeping) = prefs.edit().putBoolean(PREVENT_PHONE_FROM_SLEEPING, preventPhoneFromSleeping).apply()

    var lastUsedViewPagerPage: Int
        get() = prefs.getInt(LAST_USED_VIEW_PAGER_PAGE, 0)
        set(lastUsedViewPagerPage) = prefs.edit().putInt(LAST_USED_VIEW_PAGER_PAGE, lastUsedViewPagerPage).apply()

    var use24HourFormat: Boolean
        get() = prefs.getBoolean(USE_24_HOUR_FORMAT, DateFormat.is24HourFormat(context))
        set(use24HourFormat) = prefs.edit().putBoolean(USE_24_HOUR_FORMAT, use24HourFormat).apply()

    var isSundayFirst: Boolean
        get() {
            val isSundayFirst = Calendar.getInstance(Locale.getDefault()).firstDayOfWeek == Calendar.SUNDAY
            return prefs.getBoolean(SUNDAY_FIRST, isSundayFirst)
        }
        set(sundayFirst) = prefs.edit().putBoolean(SUNDAY_FIRST, sundayFirst).apply()

    var wasAlarmWarningShown: Boolean
        get() = prefs.getBoolean(WAS_ALARM_WARNING_SHOWN, false)
        set(wasAlarmWarningShown) = prefs.edit().putBoolean(WAS_ALARM_WARNING_SHOWN, wasAlarmWarningShown).apply()

    var wasReminderWarningShown: Boolean
        get() = prefs.getBoolean(WAS_REMINDER_WARNING_SHOWN, false)
        set(wasReminderWarningShown) = prefs.edit().putBoolean(WAS_REMINDER_WARNING_SHOWN, wasReminderWarningShown).apply()

    var useSameSnooze: Boolean
        get() = prefs.getBoolean(USE_SAME_SNOOZE, true)
        set(useSameSnooze) = prefs.edit().putBoolean(USE_SAME_SNOOZE, useSameSnooze).apply()

    var snoozeTime: Int
        get() = prefs.getInt(SNOOZE_TIME, 10)
        set(snoozeDelay) = prefs.edit().putInt(SNOOZE_TIME, snoozeDelay).apply()

    var vibrateOnButtonPress: Boolean
        get() = prefs.getBoolean(VIBRATE_ON_BUTTON_PRESS, false)
        set(vibrateOnButton) = prefs.edit().putBoolean(VIBRATE_ON_BUTTON_PRESS, vibrateOnButton).apply()

    var yourAlarmSounds: String
        get() = prefs.getString(YOUR_ALARM_SOUNDS, "")!!
        set(yourAlarmSounds) = prefs.edit().putString(YOUR_ALARM_SOUNDS, yourAlarmSounds).apply()

    var isUsingModifiedAppIcon: Boolean
        get() = prefs.getBoolean(IS_USING_MODIFIED_APP_ICON, false)
        set(isUsingModifiedAppIcon) = prefs.edit().putBoolean(IS_USING_MODIFIED_APP_ICON, isUsingModifiedAppIcon).apply()

    var appId: String
        get() = prefs.getString(APP_ID, "")!!
        set(appId) = prefs.edit().putString(APP_ID, appId).apply()

    var initialWidgetHeight: Int
        get() = prefs.getInt(INITIAL_WIDGET_HEIGHT, 0)
        set(initialWidgetHeight) = prefs.edit().putInt(INITIAL_WIDGET_HEIGHT, initialWidgetHeight).apply()

    var widgetIdToMeasure: Int
        get() = prefs.getInt(WIDGET_ID_TO_MEASURE, 0)
        set(widgetIdToMeasure) = prefs.edit().putInt(WIDGET_ID_TO_MEASURE, widgetIdToMeasure).apply()

    var wasOrangeIconChecked: Boolean
        get() = prefs.getBoolean(WAS_ORANGE_ICON_CHECKED, false)
        set(wasOrangeIconChecked) = prefs.edit().putBoolean(WAS_ORANGE_ICON_CHECKED, wasOrangeIconChecked).apply()

    var wasAppOnSDShown: Boolean
        get() = prefs.getBoolean(WAS_APP_ON_SD_SHOWN, false)
        set(wasAppOnSDShown) = prefs.edit().putBoolean(WAS_APP_ON_SD_SHOWN, wasAppOnSDShown).apply()

    var wasBeforeAskingShown: Boolean
        get() = prefs.getBoolean(WAS_BEFORE_ASKING_SHOWN, false)
        set(wasBeforeAskingShown) = prefs.edit().putBoolean(WAS_BEFORE_ASKING_SHOWN, wasBeforeAskingShown).apply()

    var wasBeforeRateShown: Boolean
        get() = prefs.getBoolean(WAS_BEFORE_RATE_SHOWN, false)
        set(wasBeforeRateShown) = prefs.edit().putBoolean(WAS_BEFORE_RATE_SHOWN, wasBeforeRateShown).apply()

    var wasInitialUpgradeToProShown: Boolean
        get() = prefs.getBoolean(WAS_INITIAL_UPGRADE_TO_PRO_SHOWN, false)
        set(wasInitialUpgradeToProShown) = prefs.edit().putBoolean(WAS_INITIAL_UPGRADE_TO_PRO_SHOWN, wasInitialUpgradeToProShown).apply()

    var wasAppIconCustomizationWarningShown: Boolean
        get() = prefs.getBoolean(WAS_APP_ICON_CUSTOMIZATION_WARNING_SHOWN, false)
        set(wasAppIconCustomizationWarningShown) = prefs.edit().putBoolean(WAS_APP_ICON_CUSTOMIZATION_WARNING_SHOWN, wasAppIconCustomizationWarningShown).apply()

    var appSideloadingStatus: Int
        get() = prefs.getInt(APP_SIDELOADING_STATUS, SIDELOADING_UNCHECKED)
        set(appSideloadingStatus) = prefs.edit().putInt(APP_SIDELOADING_STATUS, appSideloadingStatus).apply()

    var dateFormat: String
        get() = prefs.getString(DATE_FORMAT, getDefaultDateFormat())!!
        set(dateFormat) = prefs.edit().putString(DATE_FORMAT, dateFormat).apply()

    private fun getDefaultDateFormat(): String {
        val format = DateFormat.getDateFormat(context)
        val pattern = (format as SimpleDateFormat).toLocalizedPattern()
        return when (pattern.toLowerCase().replace(" ", "")) {
            "dd/mm/y" -> DATE_FORMAT_TWO
            "mm/dd/y" -> DATE_FORMAT_THREE
            "y-mm-dd" -> DATE_FORMAT_FOUR
            "dmmmmy" -> DATE_FORMAT_FIVE
            "mmmmdy" -> DATE_FORMAT_SIX
            "mm-dd-y" -> DATE_FORMAT_SEVEN
            "dd-mm-y" -> DATE_FORMAT_EIGHT
            else -> DATE_FORMAT_ONE
        }
    }

    var wasOTGHandled: Boolean
        get() = prefs.getBoolean(WAS_OTG_HANDLED, false)
        set(wasOTGHandled) = prefs.edit().putBoolean(WAS_OTG_HANDLED, wasOTGHandled).apply()

    var wasUpgradedFromFreeShown: Boolean
        get() = prefs.getBoolean(WAS_UPGRADED_FROM_FREE_SHOWN, false)
        set(wasUpgradedFromFreeShown) = prefs.edit().putBoolean(WAS_UPGRADED_FROM_FREE_SHOWN, wasUpgradedFromFreeShown).apply()

    var wasRateUsPromptShown: Boolean
        get() = prefs.getBoolean(WAS_RATE_US_PROMPT_SHOWN, false)
        set(wasRateUsPromptShown) = prefs.edit().putBoolean(WAS_RATE_US_PROMPT_SHOWN, wasRateUsPromptShown).apply()

    var wasAppRated: Boolean
        get() = prefs.getBoolean(WAS_APP_RATED, false)
        set(wasAppRated) = prefs.edit().putBoolean(WAS_APP_RATED, wasAppRated).apply()

    var wasSortingByNumericValueAdded: Boolean
        get() = prefs.getBoolean(WAS_SORTING_BY_NUMERIC_VALUE_ADDED, false)
        set(wasSortingByNumericValueAdded) = prefs.edit().putBoolean(WAS_SORTING_BY_NUMERIC_VALUE_ADDED, wasSortingByNumericValueAdded).apply()

    var wasFolderLockingNoticeShown: Boolean
        get() = prefs.getBoolean(WAS_FOLDER_LOCKING_NOTICE_SHOWN, false)
        set(wasFolderLockingNoticeShown) = prefs.edit().putBoolean(WAS_FOLDER_LOCKING_NOTICE_SHOWN, wasFolderLockingNoticeShown).apply()

    var lastRenamePatternUsed: String
        get() = prefs.getString(LAST_RENAME_PATTERN_USED, "")!!
        set(lastRenamePatternUsed) = prefs.edit().putString(LAST_RENAME_PATTERN_USED, lastRenamePatternUsed).apply()

    var lastExportedSettingsFolder: String
        get() = prefs.getString(LAST_EXPORTED_SETTINGS_FOLDER, "")!!
        set(lastExportedSettingsFolder) = prefs.edit().putString(LAST_EXPORTED_SETTINGS_FOLDER, lastExportedSettingsFolder).apply()

    var lastExportedSettingsFile: String
        get() = prefs.getString(LAST_EXPORTED_SETTINGS_FILE, "")!!
        set(lastExportedSettingsFile) = prefs.edit().putString(LAST_EXPORTED_SETTINGS_FILE, lastExportedSettingsFile).apply()

    // notify the users about new SMS Messenger and Voice Recorder released
    var wasMessengerRecorderShown: Boolean
        get() = prefs.getBoolean(WAS_MESSENGER_RECORDER_SHOWN, false)
        set(wasMessengerRecorderShown) = prefs.edit().putBoolean(WAS_MESSENGER_RECORDER_SHOWN, wasMessengerRecorderShown).apply()

    var startNameWithSurname: Boolean
        get() = prefs.getBoolean(START_NAME_WITH_SURNAME, false)
        set(startNameWithSurname) = prefs.edit().putBoolean(START_NAME_WITH_SURNAME, startNameWithSurname).apply()

    var widgetBgColor: Int
        get() = prefs.getInt(WIDGET_BG_COLOR, DEFAULT_WIDGET_BG_COLOR)
        set(widgetBgColor) = prefs.edit().putInt(WIDGET_BG_COLOR, widgetBgColor).apply()

    var widgetTextColor: Int
        get() = prefs.getInt(WIDGET_TEXT_COLOR, Color.BLACK)
        set(widgetTextColor) = prefs.edit().putInt(WIDGET_TEXT_COLOR, widgetTextColor).apply()

    var fontSize: Int
        get() = prefs.getInt(FONT_SIZE, 16)
        set(size) = prefs.edit().putInt(FONT_SIZE, size).apply()
}
