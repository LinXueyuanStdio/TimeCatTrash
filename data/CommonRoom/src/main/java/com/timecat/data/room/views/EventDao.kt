package com.timecat.data.room.views

import androidx.room.*
import com.timecat.component.setting.REGULAR_EVENT_TYPE_ID
import com.timecat.component.setting.SOURCE_CONTACT_ANNIVERSARY
import com.timecat.component.setting.SOURCE_CONTACT_BIRTHDAY
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.base.HABIT

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/7/27
 * @description 日历事件
 * @usage null
 */
@Dao
interface EventDao {
    @Query("SELECT * FROM view_event WHERE title LIKE :searchQuery OR content LIKE :searchQuery")
    fun getEventForSearch(searchQuery: String): List<Event>

    @Query("SELECT view_event.* FROM view_event LEFT JOIN import_record ON (import_record.source = \'$SOURCE_CONTACT_BIRTHDAY\' AND view_event.id = import_record.record_id)")
    fun getBirthdays(): List<Event>

    @Query("SELECT view_event.* FROM view_event LEFT JOIN import_record ON (import_record.source = \'$SOURCE_CONTACT_ANNIVERSARY\' AND view_event.id = import_record.record_id)")
    fun getAnniversaries(): List<Event>


    @Query("SELECT * FROM view_event WHERE event_type_id IN (:eventTypeIds)")
    fun getAllEventsWithTypes(eventTypeIds: List<Long>): List<Event>

    @Query("SELECT * FROM view_event WHERE id = :id")
    fun getEventWithId(id: Long): Event?

    @Query("SELECT * FROM view_event WHERE import_id = :importId")
    fun getEventWithImportId(importId: String): Event?

    @Query("SELECT * FROM view_event WHERE startTime <= :toTS AND (totalLength <= :toTS - :fromTS) AND subType != $HABIT")
    fun getOneTimeEventsFromTo(toTS: Long, fromTS: Long): List<Event>

    @Query("SELECT * FROM view_event WHERE id = :id AND startTime <= :toTS AND (totalLength >= :toTS - :fromTS) AND subType != $HABIT")
    fun getOneTimeEventFromToWithId(id: Long, toTS: Long, fromTS: Long): List<Event>

    @Query("SELECT * FROM view_event WHERE startTime <= :toTS AND (totalLength >= :toTS - :fromTS) AND startTime != 0 AND subType != $HABIT AND event_type_id IN (:eventTypeIds)")
    fun getOneTimeEventsFromToWithTypes(toTS: Long, fromTS: Long, eventTypeIds: List<Long>): List<Event>

    @Query("SELECT * FROM view_event WHERE (startTime + totalLength > :toTS) AND subType != $HABIT AND event_type_id IN (:eventTypeIds)")
    fun getOneTimeFutureEventsWithTypes(toTS: Long, eventTypeIds: List<Long>): List<Event>

    @Query("SELECT * FROM view_event WHERE startTime <= :toTS AND subType == $HABIT")
    fun getRepeatableEventsFromToWithTypes(toTS: Long): List<Event>

    @Query("SELECT * FROM view_event WHERE id = :id AND startTime <= :toTS AND subType == $HABIT")
    fun getRepeatableEventFromToWithId(id: Long, toTS: Long): List<Event>

    @Query("SELECT * FROM view_event WHERE startTime <= :toTS AND startTime != 0 AND subType == $HABIT AND event_type_id IN (:eventTypeIds)")
    fun getRepeatableEventsFromToWithTypes(toTS: Long, eventTypeIds: List<Long>): List<Event>

    @Query("SELECT * FROM view_event WHERE id IN (:ids) AND import_id != \"\"")
    fun getEventsByIdsWithImportIds(ids: List<Long>): List<Event>

    @Query("SELECT * FROM view_event WHERE title LIKE :searchQuery OR content LIKE :searchQuery")
    fun getEventsForSearch(searchQuery: String): List<Event>

    @Query("SELECT * FROM view_event WHERE import_id != \"\"")
    fun getEventsWithImportIds(): List<Event>

    @Query("SELECT * FROM view_event WHERE source = :source")
    fun getEventsFromCalDAVCalendar(source: String): List<Event>

    @Query("SELECT * FROM view_event WHERE id IN (:ids)")
    fun getEventsWithIds(ids: List<Long>): List<Event>

    @Query("SELECT id FROM view_event")
    fun getEventIds(): List<Long>

    @Query("SELECT id FROM view_event WHERE import_id = :importId")
    fun getEventIdWithImportId(importId: String): Long?

    @Query("SELECT id FROM view_event WHERE import_id LIKE :importId")
    fun getEventIdWithLastImportId(importId: String): Long?

    @Query("SELECT id FROM view_event WHERE event_type_id = :eventTypeId")
    fun getEventIdsByEventType(eventTypeId: Long): List<Long>

    @Query("SELECT id FROM view_event WHERE event_type_id IN (:eventTypeIds)")
    fun getEventIdsByEventType(eventTypeIds: List<Long>): List<Long>

    @Query("SELECT id FROM view_event WHERE source = :source AND import_id != \"\"")
    fun getCalDAVCalendarEvents(source: String): List<Long>

    @Query("UPDATE import_export SET event_type_id = $REGULAR_EVENT_TYPE_ID WHERE event_type_id = :eventTypeId")
    fun resetEventsWithType(eventTypeId: Long)

    @Query("UPDATE import_record SET import_id = :importId, source = :source WHERE record_id = :id")
    fun updateEventImportIdAndSource(importId: String, source: String, id: Long)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(event: RoomRecord): Long

    @Transaction
    fun insertOrUpdate(event: Event): Long {
        return insertOrUpdate(event.getRoomRecord())
    }

    @Query("DELETE FROM records WHERE id IN (:ids)")
    fun deleteEvents(ids: List<Long>)

    @Query("DELETE FROM import_record WHERE source = :source AND import_id = :importId")
    fun deleteBirthdayAnniversary(source: String, importId: String): Int
}