package com.timecat.module.editor.idea.widget;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.timecat.ui.block.temp.Def;


/**
 * Created by ywwynm on 2015/5/21. Database layer.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String TAG = "DBHelper";

    private static final String SQL_CREATE_TABLE_THINGS = "create table if not exists "
            + Def.Database.TABLE_THINGS + " ("
            + Def.Database.COLUMN_ID_THINGS + " integer primary key, "
            + Def.Database.COLUMN_TYPE_THINGS + " integer not null, "
            + Def.Database.COLUMN_STATE_THINGS + " integer not null, "
            + Def.Database.COLUMN_COLOR_THINGS + " integer, "
            + Def.Database.COLUMN_TITLE_THINGS + " text, "
            + Def.Database.COLUMN_CONTENT_THINGS + " text, "
            + Def.Database.COLUMN_ATTACHMENT_THINGS + " text, "
            + Def.Database.COLUMN_LOCATION_THINGS + " integer, "
            + Def.Database.COLUMN_CREATE_TIME_THINGS + " integer, "
            + Def.Database.COLUMN_UPDATE_TIME_THINGS + " integer, "
            + Def.Database.COLUMN_FINISH_TIME_THINGS + " integer"
            + ")";

    private static final String SQL_CREATE_TABLE_REMINDERS = "create table if not exists "
            + Def.Database.TABLE_REMINDERS + " ("
            + Def.Database.COLUMN_ID_REMINDERS + " integer primary key, "
            + Def.Database.COLUMN_NOTIFY_TIME_REMINDERS + " integer, "
            + Def.Database.COLUMN_STATE_REMINDERS + " integer, "
            + Def.Database.COLUMN_NOTIFY_MILLIS_REMINDERS + " integer, "
            + Def.Database.COLUMN_CREATE_TIME_REMINDERS + " integer, "
            + Def.Database.COLUMN_UPDATE_TIME_REMINDERS + " integer"
            + ")";

    private static final String SQL_CREATE_TABLE_HABITS = "create table if not exists "
            + Def.Database.TABLE_HABITS + " ("
            + Def.Database.COLUMN_ID_HABITS + " integer primary key, "
            + Def.Database.COLUMN_TYPE_HABITS + " integer, "
            + Def.Database.COLUMN_REMINDED_TIMES_HABITS + " integer, "
            + Def.Database.COLUMN_DETAIL_HABITS + " text, "
            + Def.Database.COLUMN_RECORD_HABITS + " text, "
            + Def.Database.COLUMN_INTERVAL_INFO_HABITS + " text, "
            + Def.Database.COLUMN_CREATE_TIME_HABITS + " integer, "
            + Def.Database.COLUMN_FIRST_TIME_HABITS + " integer"
            + ")";

    private static final String SQL_CREATE_TABLE_HABIT_REMINDERS = "create table if not exists "
            + Def.Database.TABLE_HABIT_REMINDERS + " ("
            + Def.Database.COLUMN_ID_HABIT_REMINDERS + " integer primary key, "
            + Def.Database.COLUMN_HABIT_ID_HABIT_REMINDERS + " integer, "
            + Def.Database.COLUMN_NOTIFY_TIME_HABIT_REMINDERS + " integer"
            + ")";

    private static final String SQL_CREATE_TABLE_HABIT_RECORDS = "create table if not exists "
            + Def.Database.TABLE_HABIT_RECORDS + " ("
            + Def.Database.COLUMN_ID_HABIT_RECORDS + " integer primary key, "
            + Def.Database.COLUMN_HABIT_ID_HABIT_RECORDS + " integer, "
            + Def.Database.COLUMN_HR_ID_HABIT_RECORDS + " integer, "
            + Def.Database.COLUMN_RECORD_TIME_HABIT_RECORDS + " integer, "
            + Def.Database.COLUMN_RECORD_YEAR_HABIT_RECORDS + " integer, "
            + Def.Database.COLUMN_RECORD_MONTH_HABIT_RECORDS + " integer, "
            + Def.Database.COLUMN_RECORD_WEEK_HABIT_RECORDS + " integer, "
            + Def.Database.COLUMN_RECORD_DAY_HABIT_RECORDS + " integer, "
            + Def.Database.COLUMN_TYPE_HABIT_RECORDS + " integer not null default 0"
            + ")";

    private static final String SQL_CREATE_TABLE_APP_WIDGET = "create table if not exists "
            + Def.Database.TABLE_APP_WIDGET + " ("
            + Def.Database.COLUMN_ID_APP_WIDGET + " integer primary key, "
            + Def.Database.COLUMN_THING_ID_APP_WIDGET + " integer not null, "
            + Def.Database.COLUMN_SIZE_APP_WIDGET + " integer not null, " /* added in version 3 */
            + Def.Database.COLUMN_ALPHA_APP_WIDGET
            + " integer not null default 100, " /* added in version 4 */
            + Def.Database.COLUMN_STYLE_APP_WIDGET
            + " integer not null default 0, " /* added in version 5 */
            + "foreign key("
            + Def.Database.COLUMN_THING_ID_APP_WIDGET
            + ") references "
            + Def.Database.COLUMN_ID_THINGS + "("
            + Def.Database.TABLE_THINGS
            + ")"
            + ")";

    // added on 2016/11/9
    private static final String SQL_CREATE_TABLE_DOING_RECORDS = "create table if not exists "
            + Def.Database.TABLE_DOING_RECORDS + " ("
            + Def.Database.COLUMN_ID_DOING + " integer primary key autoincrement, "
            + Def.Database.COLUMN_THING_ID_DOING + " integer not null, "
            + Def.Database.COLUMN_THING_TYPE_DOING + " integer not null, "
            + Def.Database.COLUMN_ADD5_TIMES_DOING + " integer not null, "
            + Def.Database.COLUMN_PLAYED_TIMES_DOING + " integer not null, "
            + Def.Database.COLUMN_TOTAL_PLAY_TIME_DOING + " integer not null, "
            + Def.Database.COLUMN_PREDICT_DOING_TIME_DOING + " integer not null, "
            + Def.Database.COLUMN_START_TIME_DOING + " integer not null, "
            + Def.Database.COLUMN_END_TIME_DOING + " integer not null, "
            + Def.Database.COLUMN_STOP_REASON_DOING + " integer not null, "
            + Def.Database.COLUMN_START_TYPE_DOING + " integer not null default 0, "
            + Def.Database.COLUMN_SHOULD_ASM_DOING + " integer not null default 0"
            + ")";

    private Context mContext;

    public DBHelper(Context context) {
        super(context, Def.Meta.DATABASE_NAME, null, Def.Meta.DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_APP_WIDGET);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion < oldVersion) {
            onUpgrade(db, newVersion, oldVersion);
            db.setVersion(oldVersion);
        }
    }

}
