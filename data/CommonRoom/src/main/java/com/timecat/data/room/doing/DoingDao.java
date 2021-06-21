package com.timecat.data.room.doing;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DoingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(DoingRecord doingRecord);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(DoingRecord... doingRecord);

    @Query("SELECT * FROM DoingRecord")
    List<DoingRecord> getAll();

    @Query("SELECT * FROM DoingRecord WHERE thingId = :recordId")
    List<DoingRecord> getAll(long recordId);
}
