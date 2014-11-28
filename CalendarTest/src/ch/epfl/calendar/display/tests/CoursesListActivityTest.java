/**
 * 
 */
package ch.epfl.calendar.display.tests;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.*;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.calendar.App;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.display.CoursesListActivity;

import ch.epfl.calendar.persistence.DBQuester;

/**
 * @author gilbrechbuhler
 *
 */
public class CoursesListActivityTest extends ActivityInstrumentationTestCase2<CoursesListActivity> {
    
    private static final int N_LIST_VIEW_ELEMENTS = 2;
    
    private DBQuester mDB;
    public static final String test = "test";
    
    public CoursesListActivityTest() {
        super(CoursesListActivity.class);
    }
    
    public void setUp() throws Exception {
        super.setUp();
        /*
         * SUPER DUPER IMPORTANT : class this next line in setUp in every test class that need access to the database !
         */
        App.setDBHelper("calendar_test.db"); 
        mDB = new DBQuester();
        populateTestDB();
        getActivity();
    }
    
    public void tearDown() throws Exception {
        super.tearDown();
        /*
         * SUPER DUPER IMPORTANT : call this next line in tearDown to delete test database. It needs to be done in each
         * test class that needs test database.
         */
        getInstrumentation().getTargetContext().deleteDatabase(App.getDBHelper().getDatabaseName()); // Delete database
    }
    
    public void testSizeListView() {
        onData(is(instanceOf(HashMap.class))) // Every entry in the ListView is a HashMap
            .inAdapterView(withId(getIdByName("coursesListView")))
            .atPosition(N_LIST_VIEW_ELEMENTS - 1)
            .perform(); // Empty perform will fail if no element at this position (index starts at 0)
    }
    
    private int getIdByName(String name) {
        Context context = getInstrumentation().getTargetContext();
        int result = context.getResources().getIdentifier(name, "id", context.getPackageName());
        assertTrue("id for name not found: " + name, result != 0);
        return result;
    }
    
    private void populateTestDB() throws Exception {
        List<String> period1Course1Rooms = new ArrayList<String>();
        List<String> period2Course1Rooms = new ArrayList<String>();
        period1Course1Rooms.add("GCA 331");
        period1Course1Rooms.add("CO2");
        period2Course1Rooms.add("INF1");
        period2Course1Rooms.add("INF2");
        Period period1Course1 = new Period("Lecture", "27.11.2014 08:00", "27.11.2014 10:00", 
            period1Course1Rooms, "1");
        Period period2Course1 = new Period("Exercise", "28.11.2014 08:00", "28.11.2014 10:00", 
            period2Course1Rooms, "2");
        ArrayList<Period> periodsCourse1 = new ArrayList<Period>();
        periodsCourse1.add(period1Course1);
        periodsCourse1.add(period2Course1);
        Course course1 = new Course("TestCourse1", periodsCourse1, "Pr. Testpr1", 200, "CS-321", 
            "awesome course", null);
        
        List<String> period1Course2Rooms = new ArrayList<String>();
        List<String> period2Course2Rooms = new ArrayList<String>();
        period1Course2Rooms.add("GCB 332");
        period1Course2Rooms.add("INF119");
        period2Course2Rooms.add("INM202");
        period2Course2Rooms.add("INM203");
        Period period1Course2 = new Period("Lecture", "30.11.2014 08:00", "30.11.2014 10:00", 
            period1Course2Rooms, "3");
        Period period2Course2 = new Period("Exercise", "02.11.2014 08:00", "02.11.2014 10:00", 
            period2Course2Rooms, "4");
        ArrayList<Period> periodsCourse2 = new ArrayList<Period>();
        periodsCourse2.add(period1Course2);
        periodsCourse2.add(period2Course2);
        Course course2 = new Course("TestCourse2", periodsCourse2, "Pr. Testpr2", 5, "CS-000", "cool course", null);
        
        mDB.storeCourse(course1);
        mDB.storeCourse(course2);
    }
}
