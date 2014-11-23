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
public class DBQuesterMock implements DatabaseInterface {

    @Override
    public List<Course> getAllCourses() {
        return new ArrayList<Course>();
    }

    @Override
    public List<Period> getAllPeriodsFromCourse(String course) {
        return new ArrayList<Period>();
    }

    @Override
    public List<Event> getAllEventsFromCourse(String course) {
        return new ArrayList<Event>();
    }

    @Override
    public void storeCourse(Course course) {
    }

    @Override
    public void storeCourses(List<Course> courses) {
    }

    @Override
    public void storeEventsFromCourse(Course course) {
    }

    @Override
    public void storeEvent(Event event) {
    }

    @Override
    public Course getCourse(String courseName) {
        return null;
    }

    @Override
    public List<Event> getAllEvents() {
        return new ArrayList<Event>();
    }

    @Override
    public List<Event> getAllEventsWithoutCourse() {
        return new ArrayList<Event>();
    }

	@Override
	public Event getEvent(int id) {
		return null;
	}

}
