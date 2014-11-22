package ch.epfl.calendar.persistence;

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.Period;

/**
 * Mock for the DBQuesterMock
 * @author AblionGE
 *
 */
public class DBQuesterMock implements DBQuester {

    @Override
    public List<Course> getAllCourses(SQLiteDatabase db) {
        return new ArrayList<Course>();
    }

    @Override
    public List<Period> getAllPeriodsFromCourse(SQLiteDatabase db, Course course) {
        return new ArrayList<Period>();
    }

    @Override
    public List<Event> getAllEventsFromCourse(SQLiteDatabase db, Course course) {
        return new ArrayList<Event>();
    }

    @Override
    public void storeCourse(SQLiteDatabase db, Course course) {
    }

    @Override
    public void storeCourses(SQLiteDatabase db, List<Course> courses) {
    }

    @Override
    public void storeEventsFromCourse(SQLiteDatabase db, Course course) {
    }

    @Override
    public void storEvent(SQLiteDatabase db, Event event) {
    }

}
