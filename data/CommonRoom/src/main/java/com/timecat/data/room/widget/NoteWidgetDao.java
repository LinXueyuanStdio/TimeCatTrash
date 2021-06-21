package com.timecat.data.room.widget;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteWidgetDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  long insert(NoteWidget widget);

  @Delete
  void delete(NoteWidget tag);

  @Query("SELECT * FROM note_widget WHERE widgetId = :uid LIMIT 1")
  NoteWidget getByID(int uid);

  @Query("SELECT * FROM note_widget WHERE noteUUID = :uuid")
  List<NoteWidget> getByNote(String uuid);

  @Query("SELECT * FROM note_widget")
  List<NoteWidget> getAll();
}
