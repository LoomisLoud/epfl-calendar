package ch.epfl.calendar.persistence;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import ch.epfl.calendar.App;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.utils.Logger;

/**
 * DAO for {@link Event}
 * 
 * @author lweingart
 * 
 */
public class EventDataSource implements DAO {

    private static final String ERROR_DELETE = "Unable to delete a event!";
    private static final String SUCCESS_DELETE = "Event successfully deleted";
    private static EventDataSource mEventDataSource;

    public static EventDataSource getInstance() {
        if (EventDataSource.mEventDataSource == null) {
            EventDataSource.mEventDataSource = new EventDataSource();
        }
        return EventDataSource.mEventDataSource;
    }

    /**
     * Create an event. If no specific, then use null
     * 
     * @param obj
     * @throws SQLiteCalendarException
     */
    @Override
    public void create(Object obj, String key) {
        if (key == null) {
            key = App.NO_COURSE;
        }
        Event event = (Event) obj;
        assert event != null;

        ContentValues values = new ContentValues();
        values.put(EventTable.COLUMN_NAME_NAME, event.getName());
        values.put(EventTable.COLUMN_NAME_STARTDATE,
                App.calendarToBasicFormatString(event.getStartDate()));
        values.put(EventTable.COLUMN_NAME_ENDDATE,
                App.calendarToBasicFormatString(event.getEndDate()));
        values.put(EventTable.COLUMN_NAME_TYPE, event.getType());
        values.put(EventTable.COLUMN_NAME_COURSE, event.getLinkedCourse());
        values.put(EventTable.COLUMN_NAME_DESCRIPTION, event.getDescription());
        values.put(EventTable.COLUMN_NAME_IS_BLOCK,
                App.boolToString(event.isAutomaticAddedBlock()));

        CreateRowDBTask task = new CreateRowDBTask();
        CreateObject object = new CreateObject(values, null,
                EventTable.TABLE_NAME_EVENT);
        try {
            task.execute(object);
        } catch (SQLiteCalendarException e) {
            Log.e("SQLiteCalendarException : ", "An error occured when trying to create " + object.toString());
        }
    }

    /**
     * Update an event. If no specific key, then use null
     * 
     * @param obj
     * @throws SQLiteCalendarException
     */
    @Override
    public void update(Object obj, String key) {
        if (key == null) {
            key = App.NO_COURSE;
        }
        Event event = (Event) obj;
        assert event != null;

        ContentValues values = new ContentValues();
        values.put(EventTable.COLUMN_NAME_NAME, event.getName());
        values.put(EventTable.COLUMN_NAME_STARTDATE,
                App.calendarToBasicFormatString(event.getStartDate()));
        values.put(EventTable.COLUMN_NAME_ENDDATE,
                App.calendarToBasicFormatString(event.getEndDate()));
        values.put(EventTable.COLUMN_NAME_TYPE, event.getType());
        values.put(EventTable.COLUMN_NAME_COURSE, event.getLinkedCourse());
        values.put(EventTable.COLUMN_NAME_DESCRIPTION, event.getDescription());
        values.put(EventTable.COLUMN_NAME_IS_BLOCK,
                App.boolToString(event.isAutomaticAddedBlock()));

        UpdateRowDBTask task = new UpdateRowDBTask();
        UpdateObject object = new UpdateObject(values,
                EventTable.TABLE_NAME_EVENT,
                EventTable.COLUMN_NAME_ID + " = ?",
                new String[] {String.valueOf(event.getId())});
        try {
            task.execute(object);
        } catch (SQLiteCalendarException e) {
            Log.e("SQLiteCalendarException : ", "An error occured when trying to update " + object.toString());
        }
    }

    /**
     * Delete an event.
     * 
     * @param obj
     * @throws SQLiteCalendarException
     */
    @Override
    public void delete(Object obj, String key) {
        Event event = (Event) obj;
        assert event != null;
        SQLiteDatabase db = App.getDBHelper().getWritableDatabase();

        long rowId = db.delete(EventTable.TABLE_NAME_EVENT,
                EventTable.COLUMN_NAME_ID + " = '" + event.getId() + "'", null);
        if (rowId == -1) {
            Log.e(Logger.CALENDAR_SQL_ERROR, EventDataSource.ERROR_DELETE);
            throw new SQLiteCalendarException(EventDataSource.ERROR_DELETE);
        }

        Log.i(Logger.CALENDAR_SQL_SUCCES, EventDataSource.SUCCESS_DELETE);
    }

    /**
     * Delete all events.
     */
    @Override
    public void deleteAll() {
        SQLiteDatabase db = App.getDBHelper().getWritableDatabase();
        db.delete(EventTable.TABLE_NAME_EVENT, null, null);

    }

}
