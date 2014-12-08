/**
 * 
 */
package ch.epfl.calendar.persistence.tests;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.content.ContentValues;
import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.calendar.App;
import ch.epfl.calendar.MainActivity;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.persistence.CreateObject;
import ch.epfl.calendar.persistence.CreateRowDBTask;
import ch.epfl.calendar.persistence.DBQuester;
import ch.epfl.calendar.persistence.EventTable;
import ch.epfl.calendar.persistence.SQLiteCalendarException;

/**
 * @author AblionGE
 * 
 */
public class CreateRowDBTaskTest extends
        ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;
    private Event mEvent = null;
    private DBQuester mDBQuester;
    private CreateRowDBTask instance;

    private static final String ERROR_CREATE = "Unable to create a new row!";

    public CreateRowDBTaskTest() {
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

        App.setCurrentUsername("testUsername");
        // store the sessionID in the preferences
        TequilaAuthenticationAPI.getInstance().setSessionID(
                mActivity.getApplicationContext(), "sessionID");
        TequilaAuthenticationAPI.getInstance().setUsername(
                mActivity.getApplicationContext(),
                "testUsername");

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

        instance = new CreateRowDBTask();

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
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.persistence.CreateRowDBTask#doInBackground(ch.epfl.calendar.persistence.CreateObject[])}
     * .
     * 
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public final void testDoInBackgroundCreateObject()
        throws NoSuchMethodException, IllegalAccessException,
        IllegalArgumentException, InvocationTargetException {

        Method doInBackground;
        doInBackground = (CreateRowDBTask.class).getDeclaredMethod(
                "doInBackground", CreateObject[].class);
        doInBackground.setAccessible(true);
        App.getActionBar().addTask(6);
        doInBackground
                .invoke(instance,
                        new Object[] {new CreateObject[] {createObjectToStore(mEvent) } });

        List<Event> events = mDBQuester.getAllEventsWithoutCourse();
        assertEquals(mEvent.getName(), events.get(0).getName());
        // Add a second time the same course - should be an exception
        try {
            doInBackground = (CreateRowDBTask.class).getDeclaredMethod(
                    "doInBackground", CreateObject[].class);
            doInBackground.setAccessible(true);
            instance = new CreateRowDBTask();
            doInBackground.invoke(instance,
                    new Object[] {new CreateObject[] {createObjectToStore(mEvent)}});
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof SQLiteCalendarException) {
                if (e.getTargetException().getMessage().equals(ERROR_CREATE)) {
                    // Waited
                } else {
                    fail();
                }
            } else {
                fail();
            }
        }
        System.out.println(mActivity.getNbOfAsyncTaskDB());
    }

    private CreateObject createObjectToStore(Event event) {
        ContentValues values = new ContentValues();
        values.put(EventTable.COLUMN_NAME_NAME, event.getName());
        values.put(EventTable.COLUMN_NAME_STARTDATE,
                App.calendarToBasicFormatString(event.getStartDate()));
        values.put(EventTable.COLUMN_NAME_ENDDATE,
                App.calendarToBasicFormatString(event.getEndDate()));
        values.put(EventTable.COLUMN_NAME_TYPE, event.getType());
        values.put(EventTable.COLUMN_NAME_COURSE, event.getLinkedCourse());
        values.put(EventTable.COLUMN_NAME_DESCRIPTION, event.getDescription());
        values.put(EventTable.COLUMN_NAME_IS_BLOCK,
                App.boolToString(event.isAutomaticAddedBlock()));

        return new CreateObject(values, null,
                EventTable.TABLE_NAME_EVENT);
    }

    private void populateTestDB() throws Exception {
        mEvent = new Event("event1", "27.11.2014 08:00",
                "27.11.2014 18:00", null, App.NO_COURSE, "Event 1", false,
                DBQuester.NO_ID);
    }

}
