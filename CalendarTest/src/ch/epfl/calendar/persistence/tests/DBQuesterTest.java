/**
 * 
 */
package ch.epfl.calendar.persistence.tests;

import java.util.ArrayList;
import java.util.List;

import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.calendar.App;
import ch.epfl.calendar.MainActivity;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.persistence.DBQuester;

/**
 * Test of the DBQuester This test class extends
 * ActivityInstrumentationTestCase2 because an activity is needed to delete the
 * tests database
 * 
 * @author AblionGE
 * 
 */
public class DBQuesterTest extends
        ActivityInstrumentationTestCase2<MainActivity> {

    private List<Course> mListCourses = null;
    private DBQuester mDBQuester;
    private MainActivity mActivity;
    
    private static final int SLEEP_TIME = 250;

    public DBQuesterTest() {
        super(MainActivity.class);
    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        App.setDBHelper("calendar_test.db");
        getInstrumentation().getTargetContext().deleteDatabase(
                App.getDBHelper().getDatabaseName());

        mActivity = getActivity();

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

        getInstrumentation().getTargetContext().deleteDatabase(
                App.getDBHelper().getDatabaseName());
        mDBQuester = null;
        mListCourses = null;
    }

//    /**
//     * Test method for
//     * {@link ch.epfl.calendar.persistence.DBQuester#getAllCourses()}.
//     */
//    public final void testGetAllCourses() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link ch.epfl.calendar.persistence.DBQuester#getAllCoursesNames()}.
//     */
//    public final void testGetAllCoursesNames() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link ch.epfl.calendar.persistence.DBQuester#getAllPeriodsFromCourse(java.lang.String)}
//     * .
//     */
//    public final void testGetAllPeriodsFromCourse() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link ch.epfl.calendar.persistence.DBQuester#getEvent(long)}.
//     */
//    public final void testGetEvent() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link ch.epfl.calendar.persistence.DBQuester#getAllEvents()}.
//     */
//    public final void testGetAllEvents() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link ch.epfl.calendar.persistence.DBQuester#getAllEventsFromCourse(java.lang.String)}
//     * .
//     */
//    public final void testGetAllEventsFromCourse() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link ch.epfl.calendar.persistence.DBQuester#getAllEventsFromCourseBlock(java.lang.String)}
//     * .
//     */
//    public final void testGetAllEventsFromCourseBlock() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link ch.epfl.calendar.persistence.DBQuester#getAllEventsWithoutCourse()}
//     * .
//     */
//    public final void testGetAllEventsWithoutCourse() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link ch.epfl.calendar.persistence.DBQuester#getEventWithRowId(long)}.
//     */
//    public final void testGetEventWithRowId() {
//        fail("Not yet implemented"); // TODO
//    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.persistence.DBQuester#getCourse(java.lang.String)}
     * and
     * {@link ch.epfl.calendar.persistence.DBQuester#storeCourses(java.util.List)}
     * .
     */
    public final void testStoreAndGetCourse() {
        mDBQuester.storeCourse(mListCourses.get(0));

        waitOnInsertionInDB();
        
        Course courseInDB = mDBQuester.getCourse(mListCourses.get(0).getName());

        assertEquals(mListCourses.get(0).toString(), courseInDB.toString());
    }

//    /**
//     * Test method for
//     * {@link ch.epfl.calendar.persistence.DBQuester#storeCourse(ch.epfl.calendar.data.Course)}
//     * .
//     */
//    public final void testStoreCourse() {
//        fail("Not yet implemented"); // TODO
//    }
//
//     /**
//     * Test method for
//     * {@link
//     ch.epfl.calendar.persistence.DBQuester#storeCourses(java.util.List)}
//     * .
//     */
//     public final void testStoreCourses() {
//     fail("Not yet implemented"); // TODO
//     }
//
//    /**
//     * Test method for
//     * {@link ch.epfl.calendar.persistence.DBQuester#storeEventsFromCourse(ch.epfl.calendar.data.Course)}
//     * .
//     */
//    public final void testStoreEventsFromCourse() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link ch.epfl.calendar.persistence.DBQuester#storeEvent(ch.epfl.calendar.data.Event)}
//     * .
//     */
//    public final void testStoreEvent() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link ch.epfl.calendar.persistence.DBQuester#deleteEvent(ch.epfl.calendar.data.Event)}
//     * .
//     */
//    public final void testDeleteEvent() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link ch.epfl.calendar.persistence.DBQuester#deletePeriod(ch.epfl.calendar.data.Period)}
//     * .
//     */
//    public final void testDeletePeriod() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link ch.epfl.calendar.persistence.DBQuester#deleteCourse(java.lang.String)}
//     * .
//     */
//    public final void testDeleteCourse() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link ch.epfl.calendar.persistence.DBQuester#deleteAllTables()}.
//     */
//    public final void testDeleteAllTables() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link ch.epfl.calendar.persistence.DBQuester#createTables()}.
//     */
//    public final void testCreateTables() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for {@link ch.epfl.calendar.persistence.DBQuester#close()}.
//     */
//    public final void testClose() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link ch.epfl.calendar.persistence.DBQuester#openDatabase()}.
//     */
//    public final void testOpenDatabase() {
//        fail("Not yet implemented"); // TODO
//    }

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
        Course course1 = new Course("TestCourse1", periodsCourse1,
                "Pr. Testpr1", 200, "CS-321", "awesome course", null);

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
