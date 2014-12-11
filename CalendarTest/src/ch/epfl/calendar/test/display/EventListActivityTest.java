/**
 * 
 */
package ch.epfl.calendar.test.display;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onData;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.longClick;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.doesNotExist;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isEnabled;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.calendar.App;
import ch.epfl.calendar.R;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.EventForList;
import ch.epfl.calendar.data.ListViewItem;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.data.PeriodType;
import ch.epfl.calendar.display.CustomAdapter;
import ch.epfl.calendar.display.EventListActivity;
import ch.epfl.calendar.persistence.DBQuester;
import ch.epfl.calendar.test.utils.MockActivity;
import ch.epfl.calendar.test.utils.Utils;

import com.google.android.apps.common.testing.testrunner.ActivityLifecycleMonitorRegistry;
import com.google.android.apps.common.testing.testrunner.Stage;
import com.google.common.collect.Iterables;

/**
 * @author AblionGE
 * 
 */
public class EventListActivityTest extends
        ActivityInstrumentationTestCase2<EventListActivity> {

    private DBQuester mDB;
    private EventListActivity mActivity;
    private MockActivity mMockActivity;
    private List<Course> mCourses;
    private List<Event> mEvents;

    private static final int SLEEP_TIME = 250;

    public EventListActivityTest() {
        super(EventListActivity.class);
    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        /*
         * SUPER DUPER IMPORTANT : class this next line in setUp in every test
         * class that need access to the database !
         */
        App.setDBHelper("calendar_test.db");
        getInstrumentation().getTargetContext().deleteDatabase(
                App.getDBHelper().getDatabaseName());

        mDB = new DBQuester();

        mDB.deleteAllTables();
        mDB.createTables();

        createCourses();
        mMockActivity = new MockActivity();
        App.setActionBar(mMockActivity);
        mMockActivity.setUdpateData(mMockActivity);
        mDB.storeCourses(mCourses);
        for (Event event : mEvents) {
            mDB.storeEvent(event);
        }
        waitOnInsertionInDB();
        DBQuester.close();

        mActivity = new EventListActivity();
    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        try {
            Utils.pressBack(getCurrentActivity());
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        super.tearDown();
        getInstrumentation().getTargetContext().deleteDatabase(
                App.getDBHelper().getDatabaseName());
    }

    public final void testNbOfElementsVisible() {
        setActivity();
        int nbOfElementsVisible = 8;
        for (int i = 0; i < nbOfElementsVisible; i++) {
            onData(is(instanceOf(ListViewItem.class)))
                    // Every entry in the ListView is a HashMap
                    .inAdapterView(withId(R.id.list_event_view)).atPosition(i)
                    .check(matches(isDisplayed()));

            onData(is(instanceOf(ListViewItem.class)))
                    // Every entry in the ListView is a HashMap
                    .inAdapterView(withId(R.id.list_event_view)).atPosition(i)
                    .check(matches(isEnabled()));
        }
    }

    public final void testClickOnCourse() {
        setActivity();
        // 2, 4, 5, 6, 8
        onData(is(instanceOf(ListViewItem.class)))
                // Every entry in the ListView is a HashMap
                .inAdapterView(withId(R.id.list_event_view)).atPosition(1)
                .perform(click()).check(doesNotExist());
    }

    public final void testLongClickOnCourse() {
        setActivity();

        onData(is(instanceOf(ListViewItem.class)))
                // Every entry in the ListView is a HashMap
                .inAdapterView(withId(R.id.list_event_view)).atPosition(1)
                .perform(longClick());
        onView(withText("Description")).perform(click()).check(doesNotExist());

    }

    public final void testClickOnEvent() {
        setActivity();
        // 2, 4, 5, 6, 8
        onData(is(instanceOf(ListViewItem.class)))
                // Every entry in the ListView is a HashMap
                .inAdapterView(withId(R.id.list_event_view)).atPosition(5)
                .perform(click()).check(doesNotExist());
    }

    public final void testLongClickOnEventEdit() {
        setActivity();

        onData(is(instanceOf(ListViewItem.class)))
                // Every entry in the ListView is a HashMap
                .inAdapterView(withId(R.id.list_event_view)).atPosition(5)
                .perform(longClick());
        onView(withText("Edit")).perform(click()).check(doesNotExist());
    }

    public final void testLongClickOnEventDelete() {
        setActivity();

        onData(is(instanceOf(ListViewItem.class)))
                // Every entry in the ListView is a HashMap
                .inAdapterView(withId(R.id.list_event_view)).atPosition(5)
                .perform(longClick());
        onView(withText("Delete")).perform(click());
        onData(is(instanceOf(ListViewItem.class)))
                // Every entry in the ListView is a HashMap
                .inAdapterView(withId(R.id.list_event_view)).atPosition(1)
                .check(matches(isDisplayed()));
    }

    public final void testSort() throws NoSuchMethodException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        Method sort;
        sort = (EventListActivity.class).getDeclaredMethod("sort", new Class[] {
            List.class
        });
        sort.setAccessible(true);

        List<EventForList> eventForList = createEventForListFromCourses(mCourses);

        sort.invoke(mActivity, new Object[] {
            eventForList
        });

        for (int i = 0; i < eventForList.size(); i++) {
            if (i < eventForList.size() - 1) {
                if (eventForList.get(i).getmStart()
                        .equals(eventForList.get(i + 1).getmStart())) {
                    if (!eventForList.get(i).getEnd()
                            .equals(eventForList.get(i + 1).getEnd())) {
                        assertTrue(eventForList.get(i).getEnd()
                                .before(eventForList.get(i + 1).getEnd()));
                    }
                } else {
                    assertTrue(eventForList.get(i).getmStart()
                            .before(eventForList.get(i + 1).getmStart()));
                }
            }
        }
    }

    public final void testStringToPeriodType() throws NoSuchMethodException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        mActivity.finish();
        Method stringToPeriodType;
        stringToPeriodType = (EventListActivity.class).getDeclaredMethod(
                "stringToPeriodType", new Class[] {
                    String.class
                });
        stringToPeriodType.setAccessible(true);

        String exercices = "exercices";
        String exercises = "exercises";
        String cours = "cours";
        String lecture = "lecture";
        String projet = "projet";
        String project = "project";
        String other = "other";

        assertEquals(PeriodType.EXERCISES,
                stringToPeriodType.invoke(mActivity, new Object[] {
                    exercices
                }));
        assertEquals(PeriodType.EXERCISES,
                stringToPeriodType.invoke(mActivity, new Object[] {
                    exercises
                }));
        assertEquals(PeriodType.LECTURE,
                stringToPeriodType.invoke(mActivity, new Object[] {
                    lecture
                }));
        assertEquals(PeriodType.LECTURE,
                stringToPeriodType.invoke(mActivity, new Object[] {
                    cours
                }));
        assertEquals(PeriodType.PROJECT,
                stringToPeriodType.invoke(mActivity, new Object[] {
                    projet
                }));
        assertEquals(PeriodType.PROJECT,
                stringToPeriodType.invoke(mActivity, new Object[] {
                    project
                }));
        assertEquals(PeriodType.DEFAULT,
                stringToPeriodType.invoke(mActivity, new Object[] {
                    other
                }));
    }

    @SuppressWarnings("unchecked")
    public final void testRemovePastEvents() throws NoSuchMethodException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        mActivity.finish();
        Method removePastEvents;
        removePastEvents = (EventListActivity.class).getDeclaredMethod(
                "removePastEvents", new Class[] {
                    List.class
                });
        removePastEvents.setAccessible(true);

        List<EventForList> eventForList = createEventForListFromCourses(mCourses);

        Object listViewEvents = removePastEvents.invoke(mActivity,
                new Object[] {
                    eventForList
                });

        assertEquals(4, ((List<ListViewItem>) listViewEvents).size());
    }

    @SuppressWarnings("unchecked")
    public final void testEventToEventForList() throws NoSuchMethodException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        mActivity.finish();
        Method eventToEventForList;
        eventToEventForList = (EventListActivity.class).getDeclaredMethod(
                "eventToEventForList", new Class[] {
                    List.class, List.class
                });
        eventToEventForList.setAccessible(true);

        List<ListViewItem> listViewEvents = (List<ListViewItem>) eventToEventForList
                .invoke(mActivity, new Object[] {
                    mCourses, mEvents
                });
        // One event and one period are deleted because they are in the past
        assertEquals(5, listViewEvents.size());
    }

    public final void testCreateAdapter() throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException {
        setActivity();
        Method createAdapter;
        createAdapter = (EventListActivity.class).getDeclaredMethod(
                "createAdapter", new Class[] {
                    List.class, CustomAdapter.class
                });
        createAdapter.setAccessible(true);

        List<EventForList> eventForList = createEventForListFromCourses(mCourses);

        CustomAdapter adapter = (CustomAdapter) createAdapter.invoke(mActivity,
                new Object[] {
                    eventForList, new CustomAdapter(mActivity)
                });
        // We have 5 eventForList :
        // 1 Header Item per day : 3
        // 1 Item per eventForList
        assertEquals(10, adapter.getCount());
    }

    private void createCourses() throws Exception {
        List<String> period1Course1Rooms = new ArrayList<String>();
        List<String> period2Course1Rooms = new ArrayList<String>();
        period1Course1Rooms.add("GCA 331");
        period1Course1Rooms.add("CO2");
        period2Course1Rooms.add("INF1");
        period2Course1Rooms.add("INF2");
        Period period1Course1 = new Period("Lecture", "27.11.2034 08:00",
                "27.11.2034 10:00", period1Course1Rooms, "1");
        Period period2Course1 = new Period("Exercise", "28.11.2034 08:00",
                "28.11.2034 10:00", period2Course1Rooms, "2");
        Period period3Course1 = new Period("Project", "29.11.2010 08:00",
                "29.11.2010 10:00", period2Course1Rooms, "3");
        ArrayList<Period> periodsCourse1 = new ArrayList<Period>();
        periodsCourse1.add(period1Course1);
        periodsCourse1.add(period2Course1);
        periodsCourse1.add(period3Course1);
        Course course1 = new Course("TestCourse1", periodsCourse1,
                "Pr. Testpr1", 200, "CS-321", "awesome course", null);

        List<String> period1Course2Rooms = new ArrayList<String>();
        List<String> period2Course2Rooms = new ArrayList<String>();
        period1Course2Rooms.add("GCB 332");
        period1Course2Rooms.add("INF119");
        period2Course2Rooms.add("INM202");
        period2Course2Rooms.add("INM203");
        Period period1Course2 = new Period("Lecture", "27.11.2034 08:00",
                "27.11.2034 17:00", period1Course2Rooms, "4");
        Period period2Course2 = new Period("Exercise", "02.11.2034 08:00",
                "02.11.2034 10:00", period2Course2Rooms, "5");
        ArrayList<Period> periodsCourse2 = new ArrayList<Period>();
        periodsCourse2.add(period1Course2);
        periodsCourse2.add(period2Course2);
        Course course2 = new Course("TestCourse2", periodsCourse2,
                "Pr. Testpr2", 5, "CS-000", "cool course", null);

        mCourses = new ArrayList<Course>();
        mCourses.add(course1);
        mCourses.add(course2);

        // Add events
        Event event1 = new Event("event1", "27.11.2034 08:00",
                "27.11.2034 18:00", "exercises", App.NO_COURSE, "Event 1",
                false, DBQuester.NO_ID);
        Event event2 = new Event("event2", "28.11.2014 08:00",
                "28.11.2014 18:00", "project", App.NO_COURSE, "Event 2", false,
                DBQuester.NO_ID);

        mEvents = new ArrayList<Event>();
        mEvents.add(event1);
        mEvents.add(event2);
    }

    private void waitOnInsertionInDB() {
        while (mMockActivity.getNbOfAsyncTaskDB() > 0) {
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    private List<EventForList> createEventForListFromCourses(
            List<Course> courses) {
        List<EventForList> eventForList = new ArrayList<EventForList>();
        // Create the EventForList objects
        for (Course c : courses) {
            for (Period p : c.getPeriods()) {
                eventForList.add(new EventForList(c.getName(),
                        p.getStartDate(), p.getEndDate(), p.getType(),
                        DBQuester.NO_ID, "", c.getDescription()));
            }
        }
        return eventForList;
    }

    private void setActivity() {
        mActivity = getActivity();

        // We need to set up which activity is the current one (needed by
        // AsyncTask to be able to use callback functions
        App.setActionBar(mActivity);
        mActivity.setUdpateData(mActivity);
    }

    public Activity getCurrentActivity() throws Throwable {
        getInstrumentation().waitForIdleSync();
        final Activity[] activity = new Activity[1];
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                java.util.Collection<Activity> activites = ActivityLifecycleMonitorRegistry
                        .getInstance().getActivitiesInStage(Stage.RESUMED);
                activity[0] = Iterables.getOnlyElement(activites);
            }
        });
        return activity[0];
    }

}
