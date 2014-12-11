package ch.epfl.calendar.test.persistence;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.calendar.App;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.persistence.DBQuester;
import ch.epfl.calendar.persistence.EventDataSource;
import ch.epfl.calendar.persistence.EventTable;
import ch.epfl.calendar.test.utils.MockActivity;

/**
 * EventDataSource test class.
 *
 * @author lweingart
 *
 */
public class EventDataSourceTest extends
		ActivityInstrumentationTestCase2<MockActivity> {

	private SQLiteDatabase mDb;
	private List<Event> mEventsList = null;
	private EventDataSource mEds;
	private MockActivity mActivity;

    private static final int SLEEP_TIME = 250;
    private static final int NB_OBJECTS_IN_DB = 2;

	public EventDataSourceTest() {
		super(MockActivity.class);
	}

    @Override
    public void setUp() throws Exception {
        super.setUp();

        mActivity = new MockActivity();

        App.setDBHelper("Calendar_test.db");

        App.setActionBar(mActivity);
        mActivity.setUdpateData(mActivity);

        DBQuester mDBQuester = new DBQuester();
        mDBQuester.deleteAllTables();
        mDBQuester.createTables();

        mEds = EventDataSource.getInstance();
        mActivity.addTask(NB_OBJECTS_IN_DB);
        createEventList();
        mEds.create(mEventsList.get(0), null);
        mEds.create(mEventsList.get(1), null);
        waitOnInsertionInDB();
        mDb = App.getDBHelper().getReadableDatabase();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testDeleteAll() {
    	mEds.deleteAll();
    	String events = EventTable.TABLE_NAME_EVENT;
    	Cursor cursor = mDb.rawQuery("SELECT * FROM " + events, null);
    	assertFalse(cursor.moveToFirst());

    }

    private void createEventList() {
        Event event1 = new Event("event1", "27.11.2014 08:00",
                "27.11.2014 18:00", null, "EventTestCourse1", "Event 1", false,
                DBQuester.NO_ID);
        Event event2 = new Event("event2", "28.11.2014 08:00",
                "28.11.2014 18:00", null, "EventTestCourse1", "Event 2", true,
                DBQuester.NO_ID);
        mEventsList = new ArrayList<Event>();
        mEventsList.add(event1);
        mEventsList.add(event2);
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
