/**
 *
 */
package ch.epfl.calendar.test.persistence;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.content.ContentValues;
import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.calendar.App;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.persistence.CreateObject;
import ch.epfl.calendar.persistence.CreateRowDBTask;
import ch.epfl.calendar.persistence.DBQuester;
import ch.epfl.calendar.persistence.EventTable;
import ch.epfl.calendar.persistence.UpdateObject;
import ch.epfl.calendar.persistence.UpdateRowDBTask;
import ch.epfl.calendar.test.utils.MockActivity;

/**
 * @author AblionGE
 *
 */
public class UpdateRowDBTaskTest extends
        ActivityInstrumentationTestCase2<MockActivity> {

	private static final int NB_TASKS_3 = 3;
	private static final int NB_TASKS_6 = 6;

    private MockActivity mActivity;
    private Event mEvent = null;
    private DBQuester mDBQuester;
    private UpdateRowDBTask instance;

    public UpdateRowDBTaskTest() {
        super(MockActivity.class);
    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
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

        instance = new UpdateRowDBTask();

        populateTestDB();

        storeEvent(mEvent);
    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
	protected void tearDown() throws Exception {
        super.tearDown();

        mDBQuester = null;
        mEvent = null;
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.persistence.UpdateRowDBTask#doInBackground(ch.epfl.calendar.persistence.UpdateObject[])}
     * .
     *
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public final void testDoInBackgroundUpdateObjectArray()
        throws IllegalAccessException, IllegalArgumentException,
            NoSuchMethodException, InvocationTargetException {
        Method doInBackground;
        doInBackground = (UpdateRowDBTask.class).getDeclaredMethod(
                "doInBackground", UpdateObject[].class);
        doInBackground.setAccessible(true);
        App.getActionBar().addTask(NB_TASKS_3);
        doInBackground
                .invoke(instance,
                        new Object[] {new UpdateObject[] {createObjectToUpdate(mEvent)}});

        List<Event> events = mDBQuester.getAllEventsWithoutCourse();
        assertEquals(events.get(0).getName(), mEvent.getName());
        mActivity.finish();
    }

    private UpdateObject createObjectToUpdate(Event event) {
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

        mActivity.finish();

        return new UpdateObject(values, EventTable.TABLE_NAME_EVENT,
                EventTable.COLUMN_NAME_ID + " = ?",
                new String[] {String.valueOf(event.getId())});
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

        return new CreateObject(values, null, EventTable.TABLE_NAME_EVENT);
    }

    private void populateTestDB() throws Exception {
        mEvent = new Event("event1", "27.11.2014 08:00", "27.11.2014 18:00",
                null, App.NO_COURSE, "Event 1", false, DBQuester.NO_ID);
    }

    private void storeEvent(Event event) throws NoSuchMethodException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        Method doInBackground;
        CreateRowDBTask createTask = new CreateRowDBTask();
        doInBackground = (CreateRowDBTask.class).getDeclaredMethod(
                "doInBackground", CreateObject[].class);
        doInBackground.setAccessible(true);
        App.getActionBar().addTask(NB_TASKS_6);
        doInBackground
                .invoke(createTask,
                        new Object[] {new CreateObject[] {createObjectToStore(event)}});
    }

}
