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
public interface LocalDatabaseInterface {

    /**
     * @return the list of all courses in the database.
     */
    List<Course> getAllCourses();

    /**
     * @return the list of names of all courses in the database
     */
    List<String> getAllCoursesNames();

    /**
     * Get a Course from the database
     * 
     * @param courseName
     * @return the course from the database
     */
    Course getCourse(String courseName);

    /**
     * @param course
     * @return the list of all periods in the course.
     */
    List<Period> getAllPeriodsFromCourse(String courseName);

    /**
     * 
     * @param id
     * @return The event that has id
     */
    Event getEvent(long id);

    /**
     * 
     * @return the list of all events in the database
     */
    List<Event> getAllEvents();

    /**
     * @param course
     * @return the list of all events in the course.
     */
    List<Event> getAllEventsFromCourse(String courseName);
    
    /**
     * A block event is an event added for working weekly
     * corresponding to the credits
     * @param courseName
     * @return the list of all blocks events of a Course
     */
    List<Event> getAllEventsFromCourseBlock(String courseName);

    /**
     * 
     * @return the list of all events that are not related to a course
     */
    List<Event> getAllEventsWithoutCourse();

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
     * @return
     */
    void storeEvent(Event event);

    /**
     * Delete an event
     * 
     * @param event
     */
    void deleteEvent(Event event);

}
