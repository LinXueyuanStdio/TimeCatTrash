package com.timecat.data.room;

import android.content.Context;

import androidx.room.Room;
import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.timecat.data.room.record.RecordDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/20
 * @description null
 * @usage null
 */
@RunWith(AndroidJUnit4.class)
public class MigrationTest {
    private static final String TEST_DB = "migration-test.db";

    @Rule
    public MigrationTestHelper helper;
    private RecordDao recordDao;
    private TimeCatRoomDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, TimeCatRoomDatabase.class).build();
        recordDao = db.recordDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    public MigrationTest() {
        helper = new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
                TimeCatRoomDatabase.class.getCanonicalName(),
                new FrameworkSQLiteOpenHelperFactory());
    }

    @Test
    public void migrate1To2() throws IOException {
        SupportSQLiteDatabase db = helper.createDatabase(TEST_DB, 2);

        // db has schema version 1. insert some data using SQL queries.
        // You cannot use DAO classes because they expect the latest schema.
        insertAndClose(db,1, "1");
        // Re-open the database with version 2 and provide
        // MIGRATION_1_2 as the migration process.
        db = helper.runMigrationsAndValidate(TEST_DB, 3, true, TimeCatRoomDatabase.MIGRATION_2_3);
        insertAndClose(db,2, "2");
        db = helper.runMigrationsAndValidate(TEST_DB, 4, true, TimeCatRoomDatabase.MIGRATION_3_4);
        insertAndClose(db,3, "3");
        db = helper.runMigrationsAndValidate(TEST_DB, 5, true, TimeCatRoomDatabase.MIGRATION_4_5);


        // MigrationTestHelper automatically verifies the schema changes,
        // but you need to validate that the data was migrated properly.
    }

    private void insertAndClose(SupportSQLiteDatabase db, int id, String uuid) throws IOException {
        db.execSQL("INSERT INTO records" +
                " (`id`, `name`, `title`, `content`, `uuid`, `mtime`," +
                " `coverImageUrl`, `is_dummy`, `type`, `subType`," +
                " `createTime`, `updateTime`, `finishTime`, `deleteTime`," +
                " `archiveTime`, `pinTime`, `lockTime`, `blockTime`, `startTime`," +
                " `totalLength`, `label`, `status`, `theme`, `color`, `miniShowType`, " +
                "`render_type`, `order`, `tags`, `topics`, `parent`, " +
                "`lifeCycles_type`, `lifeCycles_start`, `lifeCycles_totalLen`, `lifeCycles_cycles`, " +
                "`ext_ext`, `attachmentItems_attachmentItems`) " +
                "VALUES" +
                " ("+id+",'s','s','s',"+uuid+",1," +
                "'s',1,1,1," +
                "1,1,1,1," +
                "1,1,1,1,1," +
                "1,1,1,1,1,1," +
                "1,1,'s','s','s'," +
                "1,1,1,'s'," +
                "'s','s'" +
                ")");

        // Prepare for the next version.
        db.close();
    }
}
