package ch.epfl.calendar.persistence.tests;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.calendar.App;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.display.CoursesListActivity;
import ch.epfl.calendar.persistence.CourseDataSource;
import ch.epfl.calendar.persistence.CourseTable;
import ch.epfl.calendar.persistence.DBQuester;

/**
 * Test of CourseDataSource class
 *
 * @author lweingart
 *
 */
public class CourseDataSourceTest extends
		ActivityInstrumentationTestCase2<CoursesListActivity> {

	private SQLiteDatabase mDb;
	private List<Course> mCoursesList = null;
	private CourseDataSource mCds;
	private CoursesListActivity mActivity;

    private static final int SLEEP_TIME = 250;
    private static final int NB_OBJECTS_IN_DB = 8;
	private static final int CREDITS_NUMBER = 42;


	public CourseDataSourceTest() {
		super(CoursesListActivity.class);
	}

    @Override
    public void setUp() throws Exception {
        super.setUp();

        mActivity = getActivity();

        App.setDBHelper("calendar_test.db");
        getInstrumentation().getTargetContext().deleteDatabase(
                App.getDBHelper().getDatabaseName());

        App.setActionBar(mActivity);
        mActivity.setUdpateData(mActivity);

        DBQuester mDBQuester = new DBQuester();
        mDBQuester.deleteAllTables();
        mDBQuester.createTables();

        mCds = CourseDataSource.getInstance();
        mActivity.addTask(NB_OBJECTS_IN_DB);
        createCourseList();
        mCds.create(mCoursesList.get(0), null);
        mCds.create(mCoursesList.get(1), null);
        waitOnInsertionInDB();
        mDb = App.getDBHelper().getReadableDatabase();

    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
//        mDb.close();
    }

    public void testDeleteAll() {
    	mCds.deleteAll();
    	String courses = CourseTable.TABLE_NAME_COURSE;
    	Cursor cursor = mDb.rawQuery("SELECT * FROM " + courses, null);
    	assertFalse(cursor.moveToFirst());
    }

    private void createCourseList() throws Exception {
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
                "27.11.2014 18:00", null, "EventTestCourse1", "Event 1", false,
                DBQuester.NO_ID);
        Event event2Course1 = new Event("event2", "28.11.2014 08:00",
                "28.11.2014 18:00", null, "EventTestCourse1", "Event 2", true,
                DBQuester.NO_ID);
        List<Event> eventsCourse1 = new ArrayList<Event>();
        eventsCourse1.add(event1Course1);
        eventsCourse1.add(event2Course1);
        Course course1 = new Course("TestCourse1", periodsCourse1,
                "Pr. Testpr1", CREDITS_NUMBER, "CS-321", "awesome course", eventsCourse1);

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
                "Pr. Testpr2", CREDITS_NUMBER, "CS-000", "cool course", null);

        mCoursesList = new ArrayList<Course>();
        mCoursesList.add(course1);
        mCoursesList.add(course2);
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
