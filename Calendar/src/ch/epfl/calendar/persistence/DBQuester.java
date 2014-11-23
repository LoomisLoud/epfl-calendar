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
 * This class provides the methods to save and get informations from
 * the local SQLite database
 * @author AblionGE
 *
 */
public class DBQuester implements DatabaseInterface {

	private static final String SELECT = "SELECT ";
	private static final String FROM = " FROM ";
    private static final String SELECT_ALL_FROM = "SELECT * FROM ";
    private static final String WHERE = " WHERE ";
    private static final String ORDER_BY = "ORDER BY ";
    private static final String ASC = "ASC";
    private static final String CODE = "code ";
    private static final String EQUAL = " = ";
    private static final String NOT_EQUAL = " <> ";
    private static final String ID = "id ";
    private static final String UNDERSCORE_ID = "_id";
    private static final String NO_COURSE = "NoCourse";

	private static final int NO_ID = -1;

    /**
     * @see ch.epfl.calendar.persistence.DBQuester#getAllCourses(ch.epfl.calendar.persistence.DBHelper)
     */
    @Override
    public List<Course> getAllCourses() {
        SQLiteDatabase db = App.getDBHelper().getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL_FROM
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

                courses.add(new Course(courseName, periods, teacher, credits, code,
                        description, events));
            }
        }

        return courses;
    }

    /**
     * @see ch.epfl.calendar.persistence.DBQuester#getAllPeriodsFromCourse(
     * ch.epfl.calendar.persistence.DBHelper, ch.epfl.calendar.data.Course)
     */
    @Override
    public List<Period> getAllPeriodsFromCourse(String courseName) {
        SQLiteDatabase db = App.getDBHelper().getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL_FROM
                + PeriodTable.TABLE_NAME_PERIOD + WHERE + PeriodTable.COLUMN_NAME_COURSE
                + EQUAL + courseName + ORDER_BY + ID + ASC, null);
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
            }
        }

        return periods;
    }

    @Override
    public Event getEvent(int id) {
        SQLiteDatabase db = App.getDBHelper().getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL_FROM
                + EventTable.TABLE_NAME_EVENT + WHERE + EventTable.COLUMN_NAME_ID
                + EQUAL + id + ORDER_BY + UNDERSCORE_ID + ASC, null);
        Event event = null;

        if (cursor.moveToFirst()) {
            event = createEvent(cursor);
        }

        return event;
    }

    @Override
    public List<Event> getAllEvents() {
        SQLiteDatabase db = App.getDBHelper().getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL_FROM
                + EventTable.TABLE_NAME_EVENT + ORDER_BY + UNDERSCORE_ID + ASC, null);
        ArrayList<Event> events = new ArrayList<Event>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                events.add(createEvent(cursor));
            }
        }

        return events;
    }

    /**
     * @see ch.epfl.calendar.persistence.DBQuester#getAllEventsFromCourse(
     * ch.epfl.calendar.persistence.DBHelper, ch.epfl.calendar.data.Course)
     */
    @Override
    public List<Event> getAllEventsFromCourse(String course) {
        SQLiteDatabase db = App.getDBHelper().getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL_FROM
                + EventTable.TABLE_NAME_EVENT + WHERE + EventTable.COLUMN_NAME_COURSE
                + NOT_EQUAL + NO_COURSE + ORDER_BY + UNDERSCORE_ID + ASC, null);
        ArrayList<Event> events = new ArrayList<Event>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                events.add(createEvent(cursor));
            }
        }

        return events;
    }

    @Override
    public List<Event> getAllEventsWithoutCourse() {
        SQLiteDatabase db = App.getDBHelper().getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL_FROM
                + EventTable.TABLE_NAME_EVENT + WHERE + EventTable.COLUMN_NAME_COURSE
                + EQUAL + NO_COURSE + ORDER_BY + UNDERSCORE_ID + ASC, null);

        ArrayList<Event> events = new ArrayList<Event>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                events.add(createEvent(cursor));
            }
        }

        return events;
    }

    @Override
    public Course getCourse(String courseName) {
        SQLiteDatabase db = App.getDBHelper().getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL_FROM
                + CourseTable.TABLE_NAME_COURSE + WHERE + CourseTable.COLUMN_NAME_NAME
                + EQUAL + courseName + ORDER_BY + CODE + ASC, null);

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

        return course;

    }

    /**
     * @see ch.epfl.calendar.persistence.DBQuester#storeCourse(
     * ch.epfl.calendar.persistence.DBHelper, ch.epfl.calendar.data.Course)
     */
    @Override
    public void storeCourse(Course course) {
    	SQLiteDatabase db = App.getDBHelper().getReadableDatabase();
    	CourseDataSource cds = CourseDataSource.getInstance();

    	List<String> storedCourses = new ArrayList<String>();
        Cursor cursor = db.rawQuery(SELECT
        		+ CourseTable.COLUMN_NAME_NAME
        		+ FROM + CourseTable.TABLE_NAME_COURSE,
        		null);
        if (cursor.moveToFirst()) {
        	while (!cursor.isAfterLast()) {
				String courseName = cursor.getString(cursor.getColumnIndex(CourseTable.COLUMN_NAME_NAME));
				storedCourses.add(courseName);
			}
        }

        if (storedCourses.contains(course.getName())) {
			cds.update(course, null);
		} else {
			cds.create(course, null);
		}
    }

    /**
     * @see ch.epfl.calendar.persistence.DBQuester#storeCourses(
     * ch.epfl.calendar.persistence.DBHelper, java.util.List)
     */
    @Override
    public void storeCourses(List<Course> courses) {
    	SQLiteDatabase db = App.getDBHelper().getReadableDatabase();
    	CourseDataSource cds = CourseDataSource.getInstance();

    	List<String> storedCourses = new ArrayList<String>();
        Cursor cursor = db.rawQuery(SELECT
        		+ CourseTable.COLUMN_NAME_NAME
        		+ FROM + CourseTable.TABLE_NAME_COURSE,
        		null);
        if (cursor.moveToFirst()) {
        	while (!cursor.isAfterLast()) {
				String courseName = cursor.getString(cursor.getColumnIndex(CourseTable.COLUMN_NAME_NAME));
				storedCourses.add(courseName);
			}
        }

    	for (Course course : courses) {
            if (storedCourses.contains(course.getName())) {
    			cds.update(course, null);
    		} else {
    			cds.create(course, null);
    		}
		}
    }

    /**
     * @see ch.epfl.calendar.persistence.DBQuester#storeEventsFromCourse(
     * ch.epfl.calendar.persistence.DBHelper, ch.epfl.calendar.data.Course)
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
     * @see ch.epfl.calendar.persistence.DBQuester#storeEvent(
     * ch.epfl.calendar.persistence.DBHelper, ch.epfl.calendar.data.Event)
     */
    @Override
    public void storeEvent(Event event) {
    	EventDataSource eds = EventDataSource.getInstance();

		if (event.getId() == NO_ID) {
			eds.create(event, App.NO_COURSE);
		} else {
			eds.update(event, App.NO_COURSE);
		}
    }

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
        int id = cursor.getInt(cursor
                .getColumnIndex(EventTable.COLUMN_NAME_ID));
        return new Event(name, startDate, endDate, type, courseName, id);
    }
}
