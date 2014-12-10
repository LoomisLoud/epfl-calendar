/**
 *
 */
package ch.epfl.calendar.persistence;

import java.util.List;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import ch.epfl.calendar.App;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.utils.Logger;

/**
 * DAO for {@link Course}.
 *
 * @author lweingart
 *
 */
public class CourseDataSource implements DAO {

    protected static final String ERROR_CREATE = "Unable to create a new course!";
    protected static final String ERROR_DELETE = "Unable to delete a course!";
    protected static final String ERROR_UPDATE = "Unable to update a course!";

    protected static final String SUCCESS_CREATE = "Course successfully created!";
    protected static final String SUCCESS_DELETE = "Course successfully deleted";
    protected static final String SUCCESS_UPDATE = "Course successfully updated";

    private static CourseDataSource mCourseDataSource;

    /**
     *
     * @return
     */
    public static CourseDataSource getInstance() {
        if (CourseDataSource.mCourseDataSource == null) {
            CourseDataSource.mCourseDataSource = new CourseDataSource();
        }
        return CourseDataSource.mCourseDataSource;
    }

    /**
     * Create a course.
     *
     * @param obj
     * @throws SQLiteCalendarException
     */
    @Override
    public void create(Object obj, String key) {
        Course course = (Course) obj;
        assert course != null;

        ContentValues values = new ContentValues();
        values.put(CourseTable.COLUMN_NAME_NAME, course.getName());
        values.put(CourseTable.COLUMN_NAME_TEACHER, course.getTeacher());
        values.put(CourseTable.COLUMN_NAME_CREDITS, course.getCredits());
        values.put(CourseTable.COLUMN_NAME_CODE, course.getCode());
        values.put(CourseTable.COLUMN_NAME_DESCRIPTION, course.getDescription());

        PeriodDataSource pds = PeriodDataSource.getInstance();
        List<Period> periods = course.getPeriods();
        for (Period period : periods) {
            pds.create(period, course.getName());
        }

        EventDataSource eds = EventDataSource.getInstance();
        List<Event> events = course.getEvents();
        for (Event event : events) {
            eds.create(event, course.getName());
        }

        CreateRowDBTask task = new CreateRowDBTask();
        CreateObject object = new CreateObject(values, null, CourseTable.TABLE_NAME_COURSE);
        task.execute(object);
    }

    /**
     * Update a course.
     *
     * @param obj
     * @throws SQLiteCalendarException
     */
    @Override
    public void update(Object obj, String key) {
        Course course = (Course) obj;
        assert course != null;

        ContentValues values = new ContentValues();
        values.put(CourseTable.COLUMN_NAME_NAME, course.getName());
        values.put(CourseTable.COLUMN_NAME_TEACHER, course.getTeacher());
        values.put(CourseTable.COLUMN_NAME_CREDITS, course.getCredits());
        values.put(CourseTable.COLUMN_NAME_CODE, course.getCode());
        values.put(CourseTable.COLUMN_NAME_DESCRIPTION, course.getDescription());

        PeriodDataSource pds = PeriodDataSource.getInstance();
        List<Period> periods = course.getPeriods();
        for (Period period : periods) {
            pds.update(period, course.getName());
        }

        EventDataSource eds = EventDataSource.getInstance();
        List<Event> events = course.getEvents();
        for (Event event : events) {
            eds.update(event, course.getName());
        }

        UpdateRowDBTask task = new UpdateRowDBTask();
        UpdateObject object = new UpdateObject(values, CourseTable.TABLE_NAME_COURSE,
                CourseTable.COLUMN_NAME_NAME + " = ?", new String[] {String.valueOf(course.getName())});
        task.execute(object);
    }

    /**
     * Delete a course.
     *
     * @param obj
     * @throws SQLiteCalendarException
     */
    @Override
    public void delete(Object obj, String key) {
        Course course = (Course) obj;
        assert course != null;
        SQLiteDatabase db = App.getDBHelper().getReadableDatabase();

        long rowId = db.delete(CourseTable.TABLE_NAME_COURSE,
                CourseTable.COLUMN_NAME_NAME + " = '" + course.getName() + "'",
                null);
        if (rowId == -1) {
            Log.e(Logger.CALENDAR_SQL_ERROR, CourseDataSource.ERROR_DELETE);
        }

        Log.i(Logger.CALENDAR_SQL_SUCCES, CourseDataSource.SUCCESS_DELETE);
    }

    /**
     * Delete all courses.
     */
    @Override
    public void deleteAll() {
        SQLiteDatabase db = App.getDBHelper().getReadableDatabase();
        db.delete(CourseTable.TABLE_NAME_COURSE, null, null);
        db.delete(PeriodTable.TABLE_NAME_PERIOD, null, null);
        db.delete(EventTable.TABLE_NAME_EVENT, null, null);
    }
}
