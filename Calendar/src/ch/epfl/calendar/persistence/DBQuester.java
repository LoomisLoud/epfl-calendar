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
 * Implementation of the Database interface
 * 
 * This class provides the methods to save and get informations from the local
 * SQLite database
 * 
 * @author AblionGE
 * 
 */
public class DBQuester implements LocalDatabaseInterface {

    private static final String SELECT = "SELECT ";
    private static final String FROM = " FROM ";
    private static final String SELECT_ALL_FROM = "SELECT * FROM ";
    private static final String WHERE = " WHERE ";
    private static final String ORDER_BY = " ORDER BY ";
    private static final String ASC = "ASC";
    private static final String CODE = "code ";
    private static final String EQUAL = " = ";
    private static final String INTERSECT = " INTERSECT ";
    private static final String ID = "id ";
    private static final String UNDERSCORE_ID = "_id";
    private static final String NO_COURSE = "NoCourse";

    private static SQLiteDatabase mDB = null;

    public static final int NO_ID = -1;

    private Event createEvent(Cursor cursor) {
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
        String description = cursor.getString(cursor
                .getColumnIndex(EventTable.COLUMN_NAME_DESCRIPTION));
        int id = cursor
                .getInt(cursor.getColumnIndex(EventTable.COLUMN_NAME_ID));
        boolean isBlock = App.stringToBool(cursor.getString(cursor
                .getColumnIndex(EventTable.COLUMN_NAME_IS_BLOCK)));
        return new Event(name, startDate, endDate, type, courseName,
                description, isBlock, id);
    }

    private void closeCursor(Cursor cursor) {
        cursor.close();
    }

    /**
     * @see ch.epfl.calendar.persistence.DBQuester#getAllCourses(ch.epfl.calendar.persistence.DBHelper)
     */
    @Override
    public List<Course> getAllCourses() {
        openDatabase();
        Cursor cursor = mDB.rawQuery(SELECT_ALL_FROM
                + CourseTable.TABLE_NAME_COURSE + ORDER_BY + CODE + ASC, null);
        ArrayList<Course> courses = new ArrayList<Course>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String courseName = cursor.getString(cursor
                        .getColumnIndex(CourseTable.COLUMN_NAME_NAME));
                List<Period> periods = getAllPeriodsFromCourse(courseName);
                String teacher = cursor.getString(cursor
                        .getColumnIndex(CourseTable.COLUMN_NAME_TEACHER));
                int credits = cursor.getInt(cursor
                        .getColumnIndex(CourseTable.COLUMN_NAME_CREDITS));
                String code = cursor.getString(cursor
                        .getColumnIndex(CourseTable.COLUMN_NAME_CODE));
                String description = cursor.getString(cursor
                        .getColumnIndex(CourseTable.COLUMN_NAME_DESCRIPTION));
                List<Event> events = getAllEventsFromCourse(courseName);

                courses.add(new Course(courseName, periods, teacher, credits,
                        code, description, events));
                cursor.moveToNext();
            }
        }

        closeCursor(cursor);

        return courses;
    }

    @Override
    public List<String> getAllCoursesNames() {
        openDatabase();
        Cursor cursor = mDB.rawQuery(SELECT + CourseTable.COLUMN_NAME_NAME
                + FROM + CourseTable.TABLE_NAME_COURSE + ORDER_BY + CODE + ASC,
                null);
        ArrayList<String> coursesNames = new ArrayList<String>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String courseName = cursor.getString(cursor
                        .getColumnIndex(CourseTable.COLUMN_NAME_NAME));

                coursesNames.add(courseName);
                cursor.moveToNext();
            }
        }

        closeCursor(cursor);

        return coursesNames;
    }

    /**
     * @see ch.epfl.calendar.persistence.DBQuester#getAllPeriodsFromCourse(ch.epfl.calendar.persistence.DBHelper,
     *      ch.epfl.calendar.data.Course)
     */
    @Override
    public List<Period> getAllPeriodsFromCourse(String courseName) {
        openDatabase();
        Cursor cursor = mDB.rawQuery(SELECT_ALL_FROM
                + PeriodTable.TABLE_NAME_PERIOD + WHERE
                + PeriodTable.COLUMN_NAME_COURSE + EQUAL + "\"" + courseName
                + "\"" + ORDER_BY + "\"" + ID + "\"" + ASC, null);
        ArrayList<Period> periods = new ArrayList<Period>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor
                        .getColumnIndex(PeriodTable.COLUMN_NAME_ID));
                String type = cursor.getString(cursor
                        .getColumnIndex(PeriodTable.COLUMN_NAME_TYPE));
                String startDate = cursor.getString(cursor
                        .getColumnIndex(PeriodTable.COLUMN_NAME_STARTDATE));
                String endDate = cursor.getString(cursor
                        .getColumnIndex(PeriodTable.COLUMN_NAME_ENDDATE));
                String roomsCSV = cursor.getString(cursor
                        .getColumnIndex(PeriodTable.COLUMN_NAME_ROOMS));
                ArrayList<String> rooms = App.parseFromCSVString(roomsCSV);

                periods.add(new Period(type, startDate, endDate, rooms, id));
                cursor.moveToNext();
            }
        }

        closeCursor(cursor);

        return periods;
    }

    @Override
    public Event getEvent(long id) {
        openDatabase();
        Cursor cursor = mDB.rawQuery(SELECT_ALL_FROM
                + EventTable.TABLE_NAME_EVENT + WHERE
                + EventTable.COLUMN_NAME_ID + EQUAL + id + ORDER_BY + "\""
                + UNDERSCORE_ID + "\"" + ASC, null);
        Event event = null;

        if (cursor.moveToFirst()) {
            event = createEvent(cursor);
            cursor.moveToNext();
        }

        closeCursor(cursor);

        return event;
    }

    @Override
    public List<Event> getAllEvents() {
        openDatabase();
        Cursor cursor = mDB.rawQuery(SELECT_ALL_FROM
                + EventTable.TABLE_NAME_EVENT + ORDER_BY + "\"" + UNDERSCORE_ID
                + "\"" + ASC, null);
        ArrayList<Event> events = new ArrayList<Event>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                events.add(createEvent(cursor));
                cursor.moveToNext();
            }
        }

        closeCursor(cursor);

        return events;
    }

    /**
     * @see ch.epfl.calendar.persistence.DBQuester#getAllEventsFromCourse(ch.epfl.calendar.persistence.DBHelper,
     *      ch.epfl.calendar.data.Course)
     */
    @Override
    public List<Event> getAllEventsFromCourse(String courseName) {
        openDatabase();
        Cursor cursor = mDB.rawQuery(SELECT_ALL_FROM
                + EventTable.TABLE_NAME_EVENT + WHERE
                + EventTable.COLUMN_NAME_COURSE + EQUAL + "\"" + courseName
                + "\"" + ORDER_BY + "\"" + UNDERSCORE_ID + "\"" + ASC, null);
        ArrayList<Event> events = new ArrayList<Event>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                events.add(createEvent(cursor));
                cursor.moveToNext();
            }
        }

        closeCursor(cursor);

        return events;
    }

    @Override
    public List<Event> getAllEventsFromCourseBlock(String courseName) {
        openDatabase();
        Cursor cursor = mDB.rawQuery(SELECT_ALL_FROM
                + EventTable.TABLE_NAME_EVENT + WHERE
                + EventTable.COLUMN_NAME_COURSE + EQUAL + "\"" + courseName
                + "\"" + INTERSECT + SELECT_ALL_FROM
                + EventTable.TABLE_NAME_EVENT + WHERE
                + EventTable.COLUMN_NAME_IS_BLOCK + EQUAL + "\"" + App.TRUE
                + "\"", null);
        ArrayList<Event> events = new ArrayList<Event>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                events.add(createEvent(cursor));
                cursor.moveToNext();
            }
        }

        closeCursor(cursor);

        return events;
    }

    @Override
    public List<Event> getAllEventsWithoutCourse() {
        openDatabase();
        Cursor cursor = mDB.rawQuery(SELECT_ALL_FROM
                + EventTable.TABLE_NAME_EVENT + WHERE
                + EventTable.COLUMN_NAME_COURSE + EQUAL + "\"" + NO_COURSE
                + "\"" + ORDER_BY + "\"" + UNDERSCORE_ID + "\"" + ASC, null);

        ArrayList<Event> events = new ArrayList<Event>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                events.add(createEvent(cursor));
                cursor.moveToNext();
            }
        }

        closeCursor(cursor);

        return events;
    }

    public Event getEventWithRowId(long id) {
        openDatabase();
        Cursor cursor = mDB.rawQuery(SELECT_ALL_FROM
                + EventTable.TABLE_NAME_EVENT + WHERE
                + EventTable.COLUMN_NAME_ID + EQUAL + id, null);

        Event event = null;
        cursor.moveToFirst();
        createEvent(cursor);

        closeCursor(cursor);

        return event;
    }

    @Override
    public Course getCourse(String courseName) {
        openDatabase();
        Cursor cursor = mDB.rawQuery(SELECT_ALL_FROM
                + CourseTable.TABLE_NAME_COURSE + WHERE
                + CourseTable.COLUMN_NAME_NAME + EQUAL + "\"" + courseName
                + "\"" + ORDER_BY + "\"" + CODE + "\"" + ASC, null);

        Course course = null;
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor
                    .getColumnIndex(CourseTable.COLUMN_NAME_NAME));
            List<Period> periods = getAllPeriodsFromCourse(courseName);
            String teacher = cursor.getString(cursor
                    .getColumnIndex(CourseTable.COLUMN_NAME_TEACHER));
            int credits = cursor.getInt(cursor
                    .getColumnIndex(CourseTable.COLUMN_NAME_CREDITS));
            String code = cursor.getString(cursor
                    .getColumnIndex(CourseTable.COLUMN_NAME_CODE));
            String description = cursor.getString(cursor
                    .getColumnIndex(CourseTable.COLUMN_NAME_DESCRIPTION));
            List<Event> events = getAllEventsFromCourse(courseName);

            course = new Course(name, periods, teacher, credits, code,
                    description, events);
        }

        closeCursor(cursor);

        return course;
    }

    /**
     * @see ch.epfl.calendar.persistence.DBQuester#storeCourse(ch.epfl.calendar.persistence.DBHelper,
     *      ch.epfl.calendar.data.Course)
     */
    @Override
    public void storeCourse(Course course) {
        openDatabase();
        CourseDataSource cds = CourseDataSource.getInstance();

        List<String> storedCourses = new ArrayList<String>();
        Cursor cursor = mDB.rawQuery(SELECT + CourseTable.COLUMN_NAME_NAME
                + FROM + CourseTable.TABLE_NAME_COURSE, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String courseName = cursor.getString(cursor
                        .getColumnIndex(CourseTable.COLUMN_NAME_NAME));
                storedCourses.add(courseName);
                cursor.moveToNext();
            }
        }

        if (storedCourses.contains(course.getName())) {
            cds.update(course, null);
        } else {
            cds.create(course, null);
        }

        closeCursor(cursor);
    }

    /**
     * @see ch.epfl.calendar.persistence.DBQuester#storeCourses(ch.epfl.calendar.persistence.DBHelper,
     *      java.util.List)
     */
    @Override
    public void storeCourses(List<Course> courses) {
        openDatabase();
        CourseDataSource cds = CourseDataSource.getInstance();

        List<String> storedCourses = new ArrayList<String>();
        Cursor cursor = mDB.rawQuery(SELECT + CourseTable.COLUMN_NAME_NAME
                + FROM + CourseTable.TABLE_NAME_COURSE, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String courseName = cursor.getString(cursor
                        .getColumnIndex(CourseTable.COLUMN_NAME_NAME));
                storedCourses.add(courseName);
                cursor.moveToNext();
            }
        }

        List<String> newCoursesNames = new ArrayList<String>();
        for (Course course : courses) {
            newCoursesNames.add(course.getName());
        }
        // Delete Courses you don't have anymore
        for (String coursesStored : storedCourses) {
            if (!newCoursesNames.contains(coursesStored)) {
                deleteCourse(coursesStored);
            }
        }

        // Create or Update new Courses
        for (Course course : courses) {
            if (storedCourses.contains(course.getName())) {
                cds.update(course, null);
            } else {
                cds.create(course, null);
            }
        }

        closeCursor(cursor);
    }

    /**
     * @see ch.epfl.calendar.persistence.DBQuester#storeEventsFromCourse(ch.epfl.calendar.persistence.DBHelper,
     *      ch.epfl.calendar.data.Course)
     */
    @Override
    public void storeEventsFromCourse(Course course) {
        EventDataSource eds = EventDataSource.getInstance();

        List<Event> events = course.getEvents();
        for (Event event : events) {
            if (event.getId() == NO_ID) {
                eds.create(event, course.getName());
            } else {
                eds.update(event, course.getName());
            }
        }
    }

    /**
     * @see ch.epfl.calendar.persistence.DBQuester#storeEvent(ch.epfl.calendar.persistence.DBHelper,
     *      ch.epfl.calendar.data.Event)
     */
    @Override
    public void storeEvent(Event event) {
        EventDataSource eds = EventDataSource.getInstance();

        if (event.getId() == NO_ID) {

            eds.create(event, event.getLinkedCourse());
        } else {
            eds.update(event, event.getLinkedCourse());
        }

    }

    @Override
    public void deleteEvent(Event event) {
        EventDataSource eds = EventDataSource.getInstance();

        eds.delete(event, null);
    }

    @Override
    public void deletePeriod(Period period) {
        PeriodDataSource pds = PeriodDataSource.getInstance();

        pds.delete(period, null);
    }

    @Override
    public void deleteCourse(String courseName) {
        Course course = getCourse(courseName);
        for (Period period : course.getPeriods()) {
            deletePeriod(period);
        }

        for (Event event : course.getEvents()) {
            deleteEvent(event);
        }

        CourseDataSource cds = CourseDataSource.getInstance();
        cds.delete(course, null);
    }

    @Override
    public void deleteAllTables() {
        close();
        SQLiteDatabase db = openDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + PeriodTable.TABLE_NAME_PERIOD);
        db.execSQL("DROP TABLE IF EXISTS " + EventTable.TABLE_NAME_EVENT);
        db.execSQL("DROP TABLE IF EXISTS " + CourseTable.TABLE_NAME_COURSE);
        close();
    }

    @Override
    public void createTables() {
        SQLiteDatabase db = openDatabase();
        CourseTable.onCreate(db);
        PeriodTable.onCreate(db);
        EventTable.onCreate(db);
        close();
    }

    public static void close() {
        if (mDB != null) {
            mDB.close();
        }
        mDB = null;
    }

    public static SQLiteDatabase openDatabase() {
        if (mDB == null) {
            mDB = App.getDBHelper().getReadableDatabase();
        }
        return mDB;
    }
}
