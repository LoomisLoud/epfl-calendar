package ch.epfl.calendar.persistence;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
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

    private static final String ERROR_CREATE = "Unable to create a new event!";
    private static final String ERROR_DELETE = "Unable to delete a event!";
    private static final String ERROR_UPDATE = "Unable to update a event!";

    private static final String SUCCESS_CREATE = "Event successfully created!";
    private static final String SUCCESS_DELETE = "Event successfully deleted";
    private static final String SUCCESS_UPDATE = "Event successfully updated";

    private static EventDataSource mEventDataSource;

    public static EventDataSource getInstance() {
        if (EventDataSource.mEventDataSource == null) {
            EventDataSource.mEventDataSource = new EventDataSource();
        }
        return EventDataSource.mEventDataSource;
    }

    /**
     * Find all events.
     * 
     * @return
     */
    public ArrayList<Event> findAll() {
        SQLiteDatabase db = App.getDBHelper().getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                + EventTable.TABLE_NAME_EVENT + " ORDER BY _id ASC", null);
        ArrayList<Event> events = new ArrayList<Event>();

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor
                    .getColumnIndex(EventTable.COLUMN_NAME_NAME));
            String startDate = cursor.getString(cursor
                    .getColumnIndex(EventTable.COLUMN_NAME_STARTDATE));
            String endDate = cursor.getString(cursor
                    .getColumnIndex(EventTable.COLUMN_NAME_ENDDATE));
            String type = cursor.getString(cursor
                    .getColumnIndex(EventTable.COLUMN_NAME_TYPE));
            String course = cursor.getString(cursor
                    .getColumnIndex(EventTable.COLUMN_NAME_COURSE));

            events.add(new Event(name, startDate, endDate, type, course));
        }

        return events;
    }

    /**
     * Create an event.
     * 
     * @param obj
     * @throws SQLiteCalendarException
     */
    @Override
    public void create(Object obj) {
        Event event = (Event) obj;
        assert event != null;
        SQLiteDatabase db = App.getDBHelper().getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EventTable.COLUMN_NAME_NAME, event.getName());
        // TODO check the return value of Calendar toString method
        values.put(EventTable.COLUMN_NAME_STARTDATE, event.getStartDate()
                .toString());
        values.put(EventTable.COLUMN_NAME_ENDDATE, event.getEndDate()
                .toString());
        values.put(EventTable.COLUMN_NAME_TYPE, event.getType());
        values.put(EventTable.COLUMN_NAME_COURSE, event.getLinkedCourse());

        long rowId = db.insert(EventTable.TABLE_NAME_EVENT, null, values);
        if (rowId == -1) {
            Log.e(Logger.CALENDAR_SQL_ERROR, EventDataSource.ERROR_CREATE);
            throw new SQLiteCalendarException(EventDataSource.ERROR_CREATE);
        }

        Log.i(Logger.CALENDAR_SQL_SUCCES, EventDataSource.SUCCESS_CREATE);
    }

    /**
     * Update an event.
     * 
     * @param obj
     * @throws SQLiteCalendarException
     */
    @Override
    public void update(Object obj) {
        Event event = (Event) obj;
        assert event != null;
        SQLiteDatabase db = App.getDBHelper().getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EventTable.COLUMN_NAME_NAME, event.getName());
        // TODO check the return value of Calendar toString method
        values.put(EventTable.COLUMN_NAME_STARTDATE, event.getStartDate()
                .toString());
        values.put(EventTable.COLUMN_NAME_ENDDATE, event.getEndDate()
                .toString());
        values.put(EventTable.COLUMN_NAME_TYPE, event.getType());
        values.put(EventTable.COLUMN_NAME_COURSE, event.getLinkedCourse());

        long rowId = db.update(EventTable.TABLE_NAME_EVENT, values,
                EventTable.COLUMN_NAME_NAME + " = ?",
                new String[] {event.getName()});
        if (rowId == -1) {
            Log.e(Logger.CALENDAR_SQL_ERROR, EventDataSource.ERROR_UPDATE);
            throw new SQLiteCalendarException(EventDataSource.ERROR_UPDATE);
        }

        Log.i(Logger.CALENDAR_SQL_SUCCES, EventDataSource.SUCCESS_UPDATE);
    }

    /**
     * Delete an event.
     * 
     * @param obj
     * @throws SQLiteCalendarException
     */
    @Override
    public void delete(Object obj) {
        Event event = (Event) obj;
        assert event != null;
        SQLiteDatabase db = App.getDBHelper().getWritableDatabase();

        long rowId = db.delete(EventTable.TABLE_NAME_EVENT,
                EventTable.COLUMN_NAME_NAME + " = '" + event.getName() + "'",
                null);
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
