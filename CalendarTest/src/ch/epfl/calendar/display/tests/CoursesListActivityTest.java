/**
 * 
 */
package ch.epfl.calendar.display.tests;


import java.util.ArrayList;
import java.util.List;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.*;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.*;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.calendar.App;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.display.CoursesListActivity;

import ch.epfl.calendar.persistence.DBQuester;
import ch.epfl.calendar.testing.utils.TestsHelper;

/**
 * @author gilbrechbuhler
 *
 */
public class CoursesListActivityTest extends ActivityInstrumentationTestCase2<CoursesListActivity> {
    
    private DBQuester mDB;
    
    public CoursesListActivityTest() {
        super(CoursesListActivity.class);
        mDB = new DBQuester();
    }
    
    /*public void setUp() throws Exception {
        super.setUp();
        populateTestDB();
        getActivity();
    }
    
    public void testSizeListView() {
        onData(is(instanceOf(String.class)))
        .inAdapterView(withId(getIdByName("coursesListView")))
        .atPosition(2)
        .perform(scrollTo());
    }
    
    private int getIdByName(String name) {
        Context context = getInstrumentation().getTargetContext();
        int result = context.getResources().getIdentifier(name, "id", context.getPackageName());
        assertTrue("id for name not found: " + name, result != 0);
        return result;
    }
    
    private void populateTestDB() throws Exception {
        mDB.deleteAllDB();
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
        Course course1 = new Course("TestCourse1", periodsCourse1, "Pr. Testpr1", 200, "CS-321", "awesome course", null);
        
        List<String> period1Course2Rooms = new ArrayList<String>();
        List<String> period2Course2Rooms = new ArrayList<String>();
        period1Course2Rooms.add("GCB 332");
        period1Course2Rooms.add("INF119");
        period2Course2Rooms.add("INM202");
        period2Course2Rooms.add("INM203");
        Period period1Course2 = new Period("Lecture", "30.11.2014 08:00", "30.11.2014 10:00", 
            period1Course2Rooms, "1");
        Period period2Course2 = new Period("Exercise", "02.11.2014 08:00", "02.11.2014 10:00", 
                period2Course2Rooms, "2");
        ArrayList<Period> periodsCourse2 = new ArrayList<Period>();
        periodsCourse2.add(period1Course2);
        periodsCourse2.add(period2Course2);
        Course course2 = new Course("TestCourse2", periodsCourse2, "Pr. Testpr2", 5, "CS-000", "cool course", null);
        
        mDB.storeCourse(course1);
        mDB.storeCourse(course2);
    }*/
}
