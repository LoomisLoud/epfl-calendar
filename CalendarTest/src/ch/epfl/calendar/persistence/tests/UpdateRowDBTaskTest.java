/**
 * 
 */
package ch.epfl.calendar.persistence.tests;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.calendar.App;
import ch.epfl.calendar.MainActivity;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.persistence.CourseTable;
import ch.epfl.calendar.persistence.CreateObject;
import ch.epfl.calendar.persistence.CreateRowDBTask;
import ch.epfl.calendar.persistence.DBQuester;
import ch.epfl.calendar.persistence.EventDataSource;
import ch.epfl.calendar.persistence.PeriodDataSource;
import ch.epfl.calendar.persistence.SQLiteCalendarException;
import ch.epfl.calendar.persistence.UpdateObject;
import ch.epfl.calendar.persistence.UpdateRowDBTask;

/**
 * @author AblionGE
 * 
 */
public class UpdateRowDBTaskTest extends
        ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;
    private List<Course> mListCourses = null;
    private DBQuester mDBQuester;
    private UpdateRowDBTask instance;

    private static final int SLEEP_TIME = 250;
    private static final String ERROR_UPDATE = "Unable to update a new row!";

    public UpdateRowDBTaskTest() {
        super(MainActivity.class);
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
        mActivity = getActivity();

        App.setDBHelper("calendar_test.db");
        getInstrumentation().getTargetContext().deleteDatabase(
                App.getDBHelper().getDatabaseName());

        // We need to set up which activity is the current one (needed by
        // AsyncTask to be able to use callback functions
        App.setActionBar(mActivity);
        mActivity.setUdpateData(mActivity);

        mDBQuester = new DBQuester();

        mDBQuester.deleteAllTables();
        mDBQuester.createTables();

        instance = new UpdateRowDBTask();
        

        populateTestDB();
        
        storeCourse(mListCourses.get(1));
    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();

        mActivity = getActivity();
        App.setDBHelper("calendar_test.db");

        while (mActivity.getNbOfAsyncTaskDB() > 0) {
            mActivity.asyncTaskStoreFinished();
        }

        getInstrumentation().getTargetContext().deleteDatabase(
                App.getDBHelper().getDatabaseName());
        mDBQuester = null;
        mListCourses = null;
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.persistence.UpdateRowDBTask#doInBackground(ch.epfl.calendar.persistence.UpdateObject[])}
     * .
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     */
    public final void testDoInBackgroundUpdateObjectArray()
        throws IllegalAccessException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
        Method doInBackground;
        doInBackground = (UpdateRowDBTask.class).getDeclaredMethod(
                "doInBackground", UpdateObject[].class);
        doInBackground.setAccessible(true);
        App.getActionBar().addTask(3);
        doInBackground
                .invoke(instance,
                        new Object[] {new UpdateObject[] {createObjectToUpdate(mListCourses
                                .get(1)) } });

        List<Course> courses = mDBQuester.getAllCourses();
        assertEquals(courses.get(0).getName(), mListCourses.get(1).getName());
        // Update an object that doesn't exist in DB
        try {
            doInBackground = (UpdateRowDBTask.class).getDeclaredMethod(
                    "doInBackground", UpdateObject[].class);
            doInBackground.setAccessible(true);
            instance = new UpdateRowDBTask();
            doInBackground.invoke(instance,
                    new Object[] {new UpdateObject[] {createObjectToUpdate(mListCourses.get(0))}});
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof SQLiteCalendarException) {
                if (e.getTargetException().getMessage().equals(ERROR_UPDATE)) {
                    // Waited
                } else {
                    fail();
                }
            } else {
                fail();
            }
        }
    }

    private UpdateObject createObjectToUpdate(Course course) {
        ContentValues values = new ContentValues();
        values.put(CourseTable.COLUMN_NAME_NAME, course.getName());
        values.put(CourseTable.COLUMN_NAME_TEACHER, course.getTeacher());
        values.put(CourseTable.COLUMN_NAME_CREDITS, course.getCredits());
        values.put(CourseTable.COLUMN_NAME_CODE, course.getCode());
        values.put(CourseTable.COLUMN_NAME_DESCRIPTION, course.getDescription());

        PeriodDataSource pds = PeriodDataSource.getInstance();
        List<Period> periods = course.getPeriods();
        for (Period period : periods) {
            pds.update(period, course.getName());
        }

        EventDataSource eds = EventDataSource.getInstance();
        List<Event> events = course.getEvents();
        for (Event event : events) {
            eds.update(event, course.getName());
        }

        return new UpdateObject(values, CourseTable.TABLE_NAME_COURSE,
                CourseTable.COLUMN_NAME_NAME + " = ?",
                new String[] {String.valueOf(course.getName())});
    }
    
    private CreateObject createObjectToStore(Course course) {
        ContentValues values = new ContentValues();
        values.put(CourseTable.COLUMN_NAME_NAME, course.getName());
        values.put(CourseTable.COLUMN_NAME_TEACHER, course.getTeacher());
        values.put(CourseTable.COLUMN_NAME_CREDITS, course.getCredits());
        values.put(CourseTable.COLUMN_NAME_CODE, course.getCode());
        values.put(CourseTable.COLUMN_NAME_DESCRIPTION, course.getDescription());

        PeriodDataSource pds = PeriodDataSource.getInstance();
        List<Period> periods = course.getPeriods();
        for (Period period : periods) {
            pds.create(period, course.getName());
        }

        EventDataSource eds = EventDataSource.getInstance();
        List<Event> events = course.getEvents();
        for (Event event : events) {
            eds.create(event, course.getName());
        }

        return new CreateObject(values, null, CourseTable.TABLE_NAME_COURSE);
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
    
    private void storeCourse(Course course)
        throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method doInBackground;
        CreateRowDBTask createTask= new CreateRowDBTask();
        doInBackground = (CreateRowDBTask.class).getDeclaredMethod(
                "doInBackground", CreateObject[].class);
        doInBackground.setAccessible(true);
        App.getActionBar().addTask(6);
        doInBackground
                .invoke(createTask,
                        new Object[] {new CreateObject[] {createObjectToStore(course) } });
    }

}
