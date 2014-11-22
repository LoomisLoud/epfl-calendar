/**
 *
 */
package ch.epfl.calendar.persistence;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.Period;

/**
 * Allow requests on the SQLite database of the app.
 *
 * @author lweingart
 *
 */
public interface DBQuester {

	/**
	 * @param db
	 * @return
	 * 			the list of all courses in the database.
	 */
	List<Course> getAllCourses(SQLiteDatabase db);

	/**
	 * @param db
	 * @param course
	 * @return
	 * 			the list of all periods in the course.
	 */
	List<Period> getAllPeriodsFromCourse(SQLiteDatabase db, Course course);

	/**
	 * @param db
	 * @param course
	 * @return
	 * 			the list of all events in the course.
	 */
	List<Event> getAllEventsFromCourse(SQLiteDatabase db, Course course);

	/**
	 * Store a course in the database.
	 *
	 * @param db
	 * @param course
	 */
	void storeCourse(SQLiteDatabase db, Course course);

	/**
	 * Store a list of courses in the database.
	 *
	 * @param db
	 * @param courses
	 */
	void storeCourses(SQLiteDatabase db, List<Course> courses);

	/**
	 * Store a list of events from a course in the database.
	 *
	 * @param db
	 * @param course
	 */
	void storeEventsFromCourse(SQLiteDatabase db, Course course);

	/**
	 * Store an event in the database.
	 *
	 * @param db
	 * @param event
	 */
	void storEvent(SQLiteDatabase db, Event event);

}
