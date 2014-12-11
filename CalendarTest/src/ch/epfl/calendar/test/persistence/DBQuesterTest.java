/**
 * 
 */
package ch.epfl.calendar.test.persistence;

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteException;
import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.calendar.App;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.persistence.DBQuester;
import ch.epfl.calendar.test.utils.MockActivity;

/**
 * Test of the DBQuester This test class extends
 * ActivityInstrumentationTestCase2 because an activity is needed to delete the
 * tests database and we have to use methods from DefaultActionBarActivity
 * 
 * @author AblionGE
 * 
 */
public class DBQuesterTest extends
        ActivityInstrumentationTestCase2<MockActivity> {

    private List<Course> mListCourses = null;
    private DBQuester mDBQuester;
    private MockActivity mActivity;

    private static final int SLEEP_TIME = 250;

    public DBQuesterTest() {
        super(MockActivity.class);
    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        // When the activity is MainActivity, it is important
        // to get the activity before call "setDBHelper"
        // because in MainActivity, the name of database
        // is changed in "onCreate()"
        mActivity = new MockActivity();

        App.setCurrentUsername("testUsername");
        App.setDBHelper("calendar_test.db");

        // We need to set up which activity is the current one (needed by
        // AsyncTask to be able to use callback functions
        App.setActionBar(mActivity);
        mActivity.setUdpateData(mActivity);

        mDBQuester = new DBQuester();

        mDBQuester.deleteAllTables();
        mDBQuester.createTables();

        populateTestDB();
    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();

        mDBQuester = null;
        mListCourses = null;
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.persistence.DBQuester#getAllCourses()}
     * {@link ch.epfl.calendar.persistence.DBQuester#storeCourses(java.util.List)}
     * .
     */
    public final void testStoreAndGetAllCourses() {
        mDBQuester.storeCourses(mListCourses);

        waitOnInsertionInDB();

        List<Course> coursesInDB = new ArrayList<Course>();
        coursesInDB = mDBQuester.getAllCourses();

        for (Course course : mListCourses) {
            assertTrue(coursesInDB.contains(course));
        }
        
        //delete a course and modify the other !
        Course course = coursesInDB.get(0);
        course.setTeacher("new Teacher");
        List<Course> newListCourses = new ArrayList<Course>();
        newListCourses.add(course);
        
        mDBQuester.storeCourses(newListCourses);
        
        waitOnInsertionInDB();
        
        coursesInDB = new ArrayList<Course>();
        coursesInDB = mDBQuester.getAllCourses();
        
        assertEquals(1, coursesInDB.size());
        assertTrue(coursesInDB.contains(course));
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.persistence.DBQuester#getAllCoursesNames()}.
     */
    public final void testGetAllCoursesNames() {
        mDBQuester.storeCourses(mListCourses);

        waitOnInsertionInDB();

        List<String> coursesInDB = new ArrayList<String>();
        coursesInDB = mDBQuester.getAllCoursesNames();

        for (Course course : mListCourses) {
            assertTrue(coursesInDB.contains(course.getName()));
        }
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.persistence.DBQuester#getAllPeriodsFromCourse(java.lang.String)}
     * .
     */
    public final void testGetAllPeriodsFromCourse() {
        mDBQuester.storeCourse(mListCourses.get(0));

        waitOnInsertionInDB();

        List<Period> periodsInDB = new ArrayList<Period>();
        periodsInDB = mDBQuester.getAllPeriodsFromCourse(mListCourses.get(0)
                .getName());

        List<Period> periods = mListCourses.get(0).getPeriods();

        for (Period p : periodsInDB) {
            assertTrue(periods.contains(p));
        }
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.persistence.DBQuester#getEvent(long)}.
     */
    public final void testStoreAndGetAndDeleteEvent() {
        Event event1 = new Event("event1", "27.11.2014 08:00",
                "27.11.2014 18:00", null, App.NO_COURSE, "Event 1", false,
                DBQuester.NO_ID);

        mDBQuester.storeEvent(event1);

        waitOnInsertionInDB();

        List<Event> eventsInDB = mDBQuester.getAllEvents();
        Event eventInDB = mDBQuester.getEvent(eventsInDB.get(0).getId());

        assertEquals(event1.toString(), eventInDB.toString());
        
        //Modify the event
        eventInDB.setDescription("new description");
        event1 = eventInDB;
        
        mDBQuester.storeEvent(eventInDB);
        
        waitOnInsertionInDB();
        
        eventInDB = mDBQuester.getEvent(eventsInDB.get(0).getId());

        assertEquals(event1.toString(), eventInDB.toString());

        mDBQuester.deleteEvent(eventInDB);

        assertNull(mDBQuester.getEvent(eventInDB.getId()));

    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.persistence.DBQuester#getAllEvents()}.
     */
    public final void testGetAllEvents() {
        Event event1 = new Event("event1", "27.11.2014 08:00",
                "27.11.2014 18:00", null, App.NO_COURSE, "Event 1", false,
                DBQuester.NO_ID);
        Event event2 = new Event("event2", "28.11.2014 08:00",
                "28.11.2014 18:00", null, mListCourses.get(0).getName(),
                "Event 2", true, DBQuester.NO_ID);
        mDBQuester.storeEvent(event1);
        mDBQuester.storeEvent(event2);

        waitOnInsertionInDB();

        List<Event> events = mDBQuester.getAllEvents();

        assertEquals(2, events.size());
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.persistence.DBQuester#getAllEventsFromCourse(java.lang.String)}
     * .
     */
    public final void testGetAllEventsFromCourse() {
        Event event1 = new Event("event1", "27.11.2014 08:00",
                "27.11.2014 18:00", null, App.NO_COURSE, "Event 1", false,
                DBQuester.NO_ID);
        Event event2 = new Event("event2", "28.11.2014 08:00",
                "28.11.2014 18:00", null, mListCourses.get(0).getName(),
                "Event 2", true, DBQuester.NO_ID);

        mDBQuester.storeEvent(event1);
        mDBQuester.storeEvent(event2);

        waitOnInsertionInDB();

        List<Event> events = mDBQuester.getAllEventsFromCourse(mListCourses
                .get(0).getName());

        assertEquals(1, events.size());
        assertEquals(event2.getName(), events.get(0).getName());
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.persistence.DBQuester#getAllEventsFromCourseBlock(java.lang.String)}
     * .
     */
    public final void testGetAllEventsFromCourseBlock() {
        Event event1 = new Event("event1", "27.11.2014 08:00",
                "27.11.2014 18:00", null, mListCourses.get(0).getName(),
                "Event 1", false, DBQuester.NO_ID);
        Event event2 = new Event("event2", "28.11.2014 08:00",
                "28.11.2014 18:00", null, mListCourses.get(0).getName(),
                "Event 2", true, DBQuester.NO_ID);

        mDBQuester.storeEvent(event1);
        mDBQuester.storeEvent(event2);

        waitOnInsertionInDB();

        List<Event> events = mDBQuester
                .getAllEventsFromCourseBlock(mListCourses.get(0).getName());

        assertEquals(1, events.size());
        assertEquals(event2.getName(), events.get(0).getName());
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.persistence.DBQuester#getAllEventsWithoutCourse()}
     * .
     */
    public final void testGetAllEventsWithoutCourse() {
        Event event1 = new Event("event1", "27.11.2014 08:00",
                "27.11.2014 18:00", null, App.NO_COURSE, "Event 1", false,
                DBQuester.NO_ID);
        Event event2 = new Event("event2", "28.11.2014 08:00",
                "28.11.2014 18:00", null, mListCourses.get(0).getName(),
                "Event 2", true, DBQuester.NO_ID);

        mDBQuester.storeEvent(event1);
        mDBQuester.storeEvent(event2);

        waitOnInsertionInDB();

        List<Event> events = mDBQuester.getAllEventsWithoutCourse();

        assertEquals(1, events.size());
        assertEquals(event1.getName(), events.get(0).getName());
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.persistence.DBQuester#getCourse(java.lang.String)}
     * and
     * {@link ch.epfl.calendar.persistence.DBQuester#storeCourse(ch.epfl.calendar.data.Course)}
     * .
     */
    public final void testStoreAndGetCourse() {
        mDBQuester.storeCourse(mListCourses.get(0));

        waitOnInsertionInDB();

        Course courseInDB = mDBQuester.getCourse(mListCourses.get(0).getName());

        assertEquals(mListCourses.get(0).toString(), courseInDB.toString());
        
      //Change a course and update it
        Course course = mListCourses.get(0);
        course.setDescription("new description");
        mDBQuester.storeCourse(course);
        
        waitOnInsertionInDB();
        
        courseInDB = mDBQuester.getCourse(course.getName());
        
        assertEquals(course.toString(), courseInDB.toString());
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.persistence.DBQuester#storeEventsFromCourse(ch.epfl.calendar.data.Course)}
     * .
     */
    public final void testStoreEventsFromCourse() {
        Course course = mListCourses.get(0);

        mDBQuester.storeEventsFromCourse(course);

        waitOnInsertionInDB();

        List<Event> events = mDBQuester
                .getAllEventsFromCourse(course.getName());

        assertEquals(2, events.size());
        
        //Update both with same description
        List<Event> newEvents = new ArrayList<Event>();
        
        for (Event event : events) {
            event.setDescription("new description");
            newEvents.add(event);
        }
        course.setEvents(newEvents);
        mDBQuester.storeEventsFromCourse(course);

        waitOnInsertionInDB();

        events = mDBQuester
                .getAllEventsFromCourse(course.getName());

        assertEquals(2, events.size());
        
        for (Event event : events) {
            assertEquals("new description", event.getDescription());
        }
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.persistence.DBQuester#deletePeriod(ch.epfl.calendar.data.Period)}
     * .
     */
    public final void testDeletePeriod() {
        Course course = mListCourses.get(0);

        mDBQuester.storeCourse(course);

        waitOnInsertionInDB();

        List<Period> periods = mDBQuester.getAllPeriodsFromCourse(course
                .getName());

        mDBQuester.deletePeriod(periods.get(0));

        List<Period> periodsAfterDelete = mDBQuester
                .getAllPeriodsFromCourse(course.getName());
        assertEquals(1, periodsAfterDelete.size());
    }
    
    /**
     * Test method for
     * {@link ch.epfl.calendar.persistence.DBQuester#deleteBlock(ch.epfl.calendar.data.Event)}.
     */
    public final void testDeleteBlock() {
        //Course2 has no events
        Event event1Course2 = new Event("event1", "01.11.2014 08:00",
                "01.11.2014 18:00", null, "TestCourse2", "Event 1", true,
                DBQuester.NO_ID);
        Event event2Course2 = new Event("event2", "08.11.2014 08:00",
                "08.11.2014 18:00", null, "TestCourse2", "Event 2", true,
                DBQuester.NO_ID);
        Event event3Course2 = new Event("event2", "09.11.2014 08:00",
                "09.11.2014 18:00", null, "TestCourse2", "Event 2", true,
                DBQuester.NO_ID);
        //Not a block
        Event event4Course2 = new Event("event2", "08.11.2014 08:00",
                "08.11.2014 18:00", null, "TestCourse2", "Event 2", false,
                DBQuester.NO_ID);
        List<Event> events = new ArrayList<Event>();
        events.add(event1Course2);
        events.add(event2Course2);
        
        mListCourses.get(1).setEvents(events);
        
        mDBQuester.storeEventsFromCourse(mListCourses.get(1));

        waitOnInsertionInDB();

        events = mDBQuester.getAllEventsFromCourse(mListCourses.get(1).getName());
        
        assertEquals(2, events.size());
        
        mDBQuester.deleteBlock(events.get(0));
        
        assertEquals(new ArrayList<Event>(), mDBQuester.getAllEventsFromCourse(mListCourses.get(1).getName()));
        
        //Test with more events
        events = new ArrayList<Event>();
        events.add(event1Course2);
        events.add(event2Course2);
        events.add(event3Course2);
        events.add(event4Course2);
        
        mListCourses.get(1).setEvents(events);
        
        mDBQuester.storeEventsFromCourse(mListCourses.get(1));

        waitOnInsertionInDB();

        events = mDBQuester.getAllEventsFromCourse(mListCourses.get(1).getName());
        
        assertEquals(4, events.size());
        
        mDBQuester.deleteBlock(events.get(0));
        
        assertEquals(2, mDBQuester.getAllEventsFromCourse(mListCourses.get(1).getName()).size());
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.persistence.DBQuester#deleteCourse(java.lang.String)}
     * .
     */
    public final void testDeleteCourse() {
        Course course = mListCourses.get(0);

        mDBQuester.storeCourse(course);

        waitOnInsertionInDB();

        mDBQuester.deleteCourse(course.getName());

        List<Course> courses = mDBQuester.getAllCourses();
        List<Period> periods = mDBQuester.getAllPeriodsFromCourse(course
                .getName());
        List<Event> events = mDBQuester.getAllEvents();

        assertEquals(new ArrayList<Course>(), courses);
        assertEquals(new ArrayList<Period>(), periods);
        assertEquals(new ArrayList<Event>(), events);
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.persistence.DBQuester#deleteAllTables()}.
     */
    public final void testDeleteAllTables() {
        mDBQuester.deleteAllTables();
        try {
            mDBQuester.getAllCourses();
        } catch (SQLiteException e) {
            // Expected
        }

        // Create Tables for next tests
        mDBQuester.createTables();
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.persistence.DBQuester#createTables()}.
     */
    public final void testCreateTables() {
        mDBQuester.deleteAllTables();

        try {
            mDBQuester.getAllCourses();
        } catch (SQLiteException e) {
            // Expected
        }

        mDBQuester.createTables();
        assertEquals(new ArrayList<Course>(), mDBQuester.getAllCourses());
    }

    private void populateTestDB() throws Exception {
        List<String> period1Course1Rooms = new ArrayList<String>();
        List<String> period2Course1Rooms = new ArrayList<String>();
        period1Course1Rooms.add("GCA 331");
        period1Course1Rooms.add("CO2");
        period2Course1Rooms.add("INF1");
        period2Course1Rooms.add("INF2");
        Period period1Course1 = new Period("Lecture", "27.11.2014 08:00",
                "27.11.2014 10:00", period1Course1Rooms, "1");
        Period period2Course1 = new Period("Exercise", "28.11.2014 08:00",
                "28.11.2014 10:00", period2Course1Rooms, "2");
        ArrayList<Period> periodsCourse1 = new ArrayList<Period>();
        periodsCourse1.add(period1Course1);
        periodsCourse1.add(period2Course1);
        Event event1Course1 = new Event("event1", "27.11.2014 08:00",
                "27.11.2014 18:00", null, "TestCourse1", "Event 1", false,
                DBQuester.NO_ID);
        Event event2Course1 = new Event("event2", "28.11.2014 08:00",
                "28.11.2014 18:00", null, "TestCourse1", "Event 2", true,
                DBQuester.NO_ID);
        List<Event> eventsCourse1 = new ArrayList<Event>();
        eventsCourse1.add(event1Course1);
        eventsCourse1.add(event2Course1);
        Course course1 = new Course("TestCourse1", periodsCourse1,
                "Pr. Testpr1", 200, "CS-321", "awesome course", eventsCourse1);

        List<String> period1Course2Rooms = new ArrayList<String>();
        List<String> period2Course2Rooms = new ArrayList<String>();
        period1Course2Rooms.add("GCB 332");
        period1Course2Rooms.add("INF119");
        period2Course2Rooms.add("INM202");
        period2Course2Rooms.add("INM203");
        Period period1Course2 = new Period("Lecture", "30.11.2014 08:00",
                "30.11.2014 10:00", period1Course2Rooms, "3");
        Period period2Course2 = new Period("Exercise", "02.11.2014 08:00",
                "02.11.2014 10:00", period2Course2Rooms, "4");
        ArrayList<Period> periodsCourse2 = new ArrayList<Period>();
        periodsCourse2.add(period1Course2);
        periodsCourse2.add(period2Course2);
        Course course2 = new Course("TestCourse2", periodsCourse2,
                "Pr. Testpr2", 5, "CS-000", "cool course", null);

        mListCourses = new ArrayList<Course>();
        mListCourses.add(course1);
        mListCourses.add(course2);
    }

    private void waitOnInsertionInDB() {
        while (mActivity.getNbOfAsyncTaskDB() > 0) {
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
                fail();
            }
        }
    }

}
