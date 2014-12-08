package ch.epfl.calendar.display.tests;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.calendar.App;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.display.CourseDetailsActivity;
import ch.epfl.calendar.persistence.DBQuester;
import ch.epfl.calendar.persistence.LocalDatabaseInterface;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

/**
 * 
 * @author Enea Bell
 *
 */
public class CourseDetailsActivityInstrumentationTest extends 
    ActivityInstrumentationTestCase2<CourseDetailsActivity> {
    
    private CourseDetailsActivity mActivity;
    private LocalDatabaseInterface mDB;
    public static final String TEST = "test";

    public CourseDetailsActivityInstrumentationTest() {
        super(CourseDetailsActivity.class);
    }
    
    public void setUp() throws Exception {
        super.setUp();

        /*
         * SUPER DUPER IMPORTANT : class this next line in setUp in every test
         * class that need access to the database !
         */
        App.setDBHelper("calendar_test.db");
        getInstrumentation().getTargetContext().deleteDatabase(
                App.getDBHelper().getDatabaseName());

        mActivity = getActivity();

        // We need to set up which activity is the current one (needed by
        // AsyncTask to be able to use callback functions
        App.setActionBar(mActivity);
        mActivity.setUdpateData(mActivity);

        mDB = new DBQuester();

        mDB.deleteAllTables();
        mDB.createTables();

        populateTestDB();
    }
    
    
    
    public void tearDown() throws Exception {
        super.tearDown();
    }

    
    public void testActivityLaunchNothingInIntent() {
        assertNotNull(mActivity);
    }
    
    public void testCourseIntent() {
        
    }
    
    
    private void setIntentActivity() {
        //set intent to start activity
        Intent intent = new Intent(getInstrumentation().getTargetContext(), CourseDetailsActivity.class);
        //Foo foo = new Foo();
        //intent.putExtra("course", foo);
        setActivityIntent(intent);
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

        mDB.storeCourse(course1);
        mDB.storeCourse(course2);
    }
}
