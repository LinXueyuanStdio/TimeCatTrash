package com.timecat.data.room.widget;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_widget", indices = {@Index("widgetId")})
public class NoteWidget {

  @PrimaryKey(autoGenerate = true)
  public int widgetId;

  public String noteUUID;

  public NoteWidget() {
  }

  public NoteWidget(int widgetId, String noteId) {
    this.widgetId = widgetId;
    this.noteUUID = noteId;
  }
}
