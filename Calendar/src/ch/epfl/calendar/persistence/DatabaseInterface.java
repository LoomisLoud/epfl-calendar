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
public interface DatabaseInterface {

    /**
     * @return the list of all courses in the database.
     */
    List<Course> getAllCourses();

    /**
     * @param course
     * @return the list of all periods in the course.
     */
    List<Period> getAllPeriodsFromCourse(String courseName);

    /**
     * @param course
     * @return the list of all events in the course.
     */
    List<Event> getAllEventsFromCourse(String courseName);

    /**
     * Store a course in the database.
     *
     * @param course
     */
    void storeCourse(Course course);

    /**
     * Store a list of courses in the database.
     *
     * @param courses
     */
    void storeCourses(List<Course> courses);

    /**
     * Store a list of events from a course in the database.
     *
     * @param course
     */
    void storeEventsFromCourse(Course course);

    /**
     * Store an event in the database.
     *
     * @param event
     */
    void storeEvent(Event event);

}
