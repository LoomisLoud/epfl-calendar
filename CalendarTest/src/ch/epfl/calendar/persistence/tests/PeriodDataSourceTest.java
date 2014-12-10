package ch.epfl.calendar.persistence.tests;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.calendar.App;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.display.CoursesListActivity;
import ch.epfl.calendar.persistence.DBQuester;
import ch.epfl.calendar.persistence.PeriodDataSource;
import ch.epfl.calendar.persistence.PeriodTable;


/**
 * PeriodDataSource test class.
 *
 * @author lweingart
 *
 */
public class PeriodDataSourceTest extends
		ActivityInstrumentationTestCase2<CoursesListActivity>  {

	private CoursesListActivity mActivity;
	private SQLiteDatabase mDb;
	private PeriodDataSource mPds;
	private List<Period> mPeriodList = null;

	private static final int SLEEP_TIME = 250;
	private static final int NB_OBJECTS_IN_DB = 2;

	public PeriodDataSourceTest() {
		super(CoursesListActivity.class);
	}

    @Override
    public void setUp() throws Exception {
        super.setUp();

        mActivity = getActivity();

        App.setDBHelper("Calendar_test.db");
        getInstrumentation().getTargetContext().deleteDatabase(
                App.getDBHelper().getDatabaseName());

        App.setActionBar(mActivity);
        mActivity.setUdpateData(mActivity);

        DBQuester dBQuester = new DBQuester();
        dBQuester.deleteAllTables();
        dBQuester.createTables();

        mPds = PeriodDataSource.getInstance();
        mActivity.addTask(NB_OBJECTS_IN_DB);
        createPeriodList();
        mPds.create(mPeriodList.get(0), null);
        mPds.create(mPeriodList.get(1), null);
        waitOnInsertionInDB();
        mDb = App.getDBHelper().getReadableDatabase();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testDeleteAll() {
    	mPds.deleteAll();
    	String periods = PeriodTable.TABLE_NAME_PERIOD;
    	Cursor cursor = mDb.rawQuery("SELECT * FROM " + periods, null);
    	assertFalse(cursor.moveToFirst());
    }

    private void createPeriodList() {
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
        mPeriodList = new ArrayList<Period>();
        mPeriodList.add(period1Course1);
        mPeriodList.add(period2Course1);
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
