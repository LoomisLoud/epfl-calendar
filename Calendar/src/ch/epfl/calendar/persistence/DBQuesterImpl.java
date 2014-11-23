/**
 * 
 */
package ch.epfl.calendar.persistence;

import java.util.List;

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
public class DBQuesterImpl implements DBQuester {

    /* (non-Javadoc)
     * @see ch.epfl.calendar.persistence.DBQuester#getAllCourses(ch.epfl.calendar.persistence.DBHelper)
     */
    @Override
    public List<Course> getAllCourses(DBHelper dbh) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see ch.epfl.calendar.persistence.DBQuester#getAllPeriodsFromCourse(
     * ch.epfl.calendar.persistence.DBHelper, ch.epfl.calendar.data.Course)
     */
    @Override
    public List<Period> getAllPeriodsFromCourse(DBHelper dbh, Course course) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see ch.epfl.calendar.persistence.DBQuester#getAllEventsFromCourse(
     * ch.epfl.calendar.persistence.DBHelper, ch.epfl.calendar.data.Course)
     */
    @Override
    public List<Event> getAllEventsFromCourse(DBHelper dbh, Course course) {
        // TODO Auto-generated method stub
        return null;
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
