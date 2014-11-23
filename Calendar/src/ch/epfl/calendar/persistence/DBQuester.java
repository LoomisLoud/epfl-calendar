/**
 * 
 */
package ch.epfl.calendar.persistence;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ch.epfl.calendar.App;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.Period;

/**
 * Implementation of the DBQuester interface
 * 
 * This class provides the methods to save and get informations from
 * the local SQLite database
 * @author AblionGE
 *
 */
public class DBQuester implements DatabaseInterface {

    /* (non-Javadoc)
     * @see ch.epfl.calendar.persistence.DBQuester#getAllCourses(ch.epfl.calendar.persistence.DBHelper)
     */
    @Override
    public List<Course> getAllCourses(DBHelper dbh) {
        SQLiteDatabase db = App.getDBHelper().getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                + CourseTable.TABLE_NAME_COURSE + " ORDER BY code ASC", null);
        ArrayList<Course> courses = new ArrayList<Course>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor
                        .getColumnIndex(CourseTable.COLUMN_NAME_NAME));
                // TODO find a way to select all corresponding period
                ArrayList<Period> periods = null;
                String teacher = cursor.getString(cursor
                        .getColumnIndex(CourseTable.COLUMN_NAME_TEACHER));
                int credits = cursor.getInt(cursor
                        .getColumnIndex(CourseTable.COLUMN_NAME_CREDITS));
                String code = cursor.getString(cursor
                        .getColumnIndex(CourseTable.COLUMN_NAME_CODE));
                String description = cursor.getString(cursor
                        .getColumnIndex(CourseTable.COLUMN_NAME_DESCRIPTION));
                // TODO find a way to select all corresponding events
                ArrayList<Event> events = new ArrayList<Event>();

                courses.add(new Course(name, periods, teacher, credits, code,
                        description, events));
            }
        }

        return courses;
    }

    /* (non-Javadoc)
     * @see ch.epfl.calendar.persistence.DBQuester#getAllPeriodsFromCourse(
     * ch.epfl.calendar.persistence.DBHelper, ch.epfl.calendar.data.Course)
     */
    @Override
    public List<Period> getAllPeriodsFromCourse(DBHelper dbh, Course course) {
        SQLiteDatabase db = App.getDBHelper().getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                + PeriodTable.TABLE_NAME_PERIOD + "ORDER BY id ASC", null);
        ArrayList<Period> periods = new ArrayList<Period>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String type = cursor.getString(cursor
                        .getColumnIndex(PeriodTable.COLUMN_NAME_TYPE));
                String startDate = cursor.getString(cursor
                        .getColumnIndex(PeriodTable.COLUMN_NAME_STARTDATE));
                String endDate = cursor.getString(cursor
                        .getColumnIndex(PeriodTable.COLUMN_NAME_ENDDATE));
                String roomsCSV = cursor.getString(cursor
                        .getColumnIndex(PeriodTable.COLUMN_NAME_ROOMS));
                ArrayList<String> rooms = App.parseFromCSVString(roomsCSV);

                periods.add(new Period(type, startDate, endDate, rooms));
            }
        }

        return periods;
    }

    /* (non-Javadoc)
     * @see ch.epfl.calendar.persistence.DBQuester#getAllEventsFromCourse(
     * ch.epfl.calendar.persistence.DBHelper, ch.epfl.calendar.data.Course)
     */
    @Override
    public List<Event> getAllEventsFromCourse(DBHelper dbh, Course course) {
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
            String courseName = cursor.getString(cursor
                    .getColumnIndex(EventTable.COLUMN_NAME_COURSE));

            events.add(new Event(name, startDate, endDate, type, courseName));
        }

        return events;
    }

    /* (non-Javadoc)
     * @see ch.epfl.calendar.persistence.DBQuester#storeCourse(
     * ch.epfl.calendar.persistence.DBHelper, ch.epfl.calendar.data.Course)
     */
    @Override
    public void storeCourse(DBHelper dbh, Course course) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see ch.epfl.calendar.persistence.DBQuester#storeCourses(
     * ch.epfl.calendar.persistence.DBHelper, java.util.List)
     */
    @Override
    public void storeCourses(DBHelper dbh, List<Course> courses) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see ch.epfl.calendar.persistence.DBQuester#storeEventsFromCourse(
     * ch.epfl.calendar.persistence.DBHelper, ch.epfl.calendar.data.Course)
     */
    @Override
    public void storeEventsFromCourse(DBHelper dbh, Course course) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see ch.epfl.calendar.persistence.DBQuester#storeEvent(
     * ch.epfl.calendar.persistence.DBHelper, ch.epfl.calendar.data.Event)
     */
    @Override
    public void storeEvent(DBHelper dbh, Event event) {
        // TODO Auto-generated method stub

    }

}
