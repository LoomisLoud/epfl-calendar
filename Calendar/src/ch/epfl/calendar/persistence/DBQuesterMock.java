package ch.epfl.calendar.persistence;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.Period;

/**
 * Mock for the DBQuesterMock
 * 
 * @author AblionGE
 * 
 */
public class DBQuesterMock implements DBQuester {

    @Override
    public List<Course> getAllCourses(DBHelper dbh) {
        return new ArrayList<Course>();
    }

    @Override
    public List<Period> getAllPeriodsFromCourse(DBHelper dbh, Course course) {
        return new ArrayList<Period>();
    }

    @Override
    public List<Event> getAllEventsFromCourse(DBHelper dbh, Course course) {
        return new ArrayList<Event>();
    }

    @Override
    public void storeCourse(DBHelper dbh, Course course) {
    }

    @Override
    public void storeCourses(DBHelper dbh, List<Course> courses) {
    }

    @Override
    public void storeEventsFromCourse(DBHelper dbh, Course course) {
    }

    @Override
    public void storeEvent(DBHelper dbh, Event event) {
    }

}
