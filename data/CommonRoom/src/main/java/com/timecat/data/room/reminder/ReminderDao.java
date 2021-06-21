package com.timecat.data.room.reminder;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Reminder reminder);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(Reminder... reminder);

    @Query("SELECT * FROM Reminder WHERE id = :uid LIMIT 1")
    Reminder getByID(long uid);

    @Query("SELECT * FROM Reminder")
    List<Reminder> getAll();

    @Update
    int update(Reminder reminder);

    @Query("DELETE FROM Reminder WHERE id = :reminderId")
    void delete(long reminderId);
}
