/**
 *
 */
package ch.epfl.calendar.persistence;

import java.util.List;

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
     * @param dbh
     * @return the list of all courses in the database.
     */
    List<Course> getAllCourses(DBHelper dbh);

    /**
     * @param dbh
     * @param course
     * @return the list of all periods in the course.
     */
    List<Period> getAllPeriodsFromCourse(DBHelper dbh, Course course);

    /**
     * @param dbh
     * @param course
     * @return the list of all events in the course.
     */
    List<Event> getAllEventsFromCourse(DBHelper dbh, Course course);

    /**
     * Store a course in the database.
     * 
     * @param dbh
     * @param course
     */
    void storeCourse(DBHelper dbh, Course course);

    /**
     * Store a list of courses in the database.
     * 
     * @param dbh
     * @param courses
     */
    void storeCourses(DBHelper dbh, List<Course> courses);

    /**
     * Store a list of events from a course in the database.
     * 
     * @param dbh
     * @param course
     */
    void storeEventsFromCourse(DBHelper dbh, Course course);

    /**
     * Store an event in the database.
     * 
     * @param dbh
     * @param event
     */
    void storeEvent(DBHelper dbh, Event event);

}
