package com.timecat.component.greendao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.timecat.component.greendao.model.AppInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "APP_INFO".
*/
public class AppInfoDao extends AbstractDao<AppInfo, Long> {

    public static final String TABLENAME = "APP_INFO";

    /**
     * Properties of entity AppInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PackageName = new Property(1, String.class, "packageName", false, "PACKAGE_NAME");
        public final static Property AppType = new Property(2, int.class, "appType", false, "APP_TYPE");
        public final static Property EnableState = new Property(3, int.class, "enableState", false, "ENABLE_STATE");
        public final static Property Title = new Property(4, String.class, "title", false, "TITLE");
    }


    public AppInfoDao(DaoConfig config) {
        super(config);
    }
    
    public AppInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"APP_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"PACKAGE_NAME\" TEXT," + // 1: packageName
                "\"APP_TYPE\" INTEGER NOT NULL ," + // 2: appType
                "\"ENABLE_STATE\" INTEGER NOT NULL ," + // 3: enableState
                "\"TITLE\" TEXT);"); // 4: title
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"APP_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, AppInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String packageName = entity.getPackageName();
        if (packageName != null) {
            stmt.bindString(2, packageName);
        }
        stmt.bindLong(3, entity.getAppType());
        stmt.bindLong(4, entity.getEnableState());
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(5, title);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, AppInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String packageName = entity.getPackageName();
        if (packageName != null) {
            stmt.bindString(2, packageName);
        }
        stmt.bindLong(3, entity.getAppType());
        stmt.bindLong(4, entity.getEnableState());
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(5, title);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public AppInfo readEntity(Cursor cursor, int offset) {
        AppInfo entity = new AppInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // packageName
            cursor.getInt(offset + 2), // appType
            cursor.getInt(offset + 3), // enableState
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // title
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, AppInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPackageName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAppType(cursor.getInt(offset + 2));
        entity.setEnableState(cursor.getInt(offset + 3));
        entity.setTitle(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(AppInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(AppInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(AppInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}