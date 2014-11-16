/**
 *
 */
package ch.epfl.calendar.persistence;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ch.epfl.calendar.App;
import ch.epfl.calendar.data.Period;

/**
 * DAO for {@link Period}.
 *
 * @author lweingart
 *
 */
public class PeriodDataSource implements DAO {

	private static final String ERROR_CREATE 	= "Unable to create a new period!";
	private static final String ERROR_DELETE 	= "Unable to delete a period!";
	private static final String ERROR_UPDATE 	= "Unable to update a period!";

	private static final String SUCCESS_CREATE 	= "Period successfully created!";
	private static final String SUCCESS_DELETE 	= "Period successfully deleted";
	private static final String SUCCESS_UPDATE 	= "Period successfully updated";

	private static PeriodDataSource mPeriodDataSource;

	public static PeriodDataSource getInstance() {
		if (PeriodDataSource.mPeriodDataSource == null) {
			PeriodDataSource.mPeriodDataSource = new PeriodDataSource();
		}
		return PeriodDataSource.mPeriodDataSource;
	}

	public ArrayList<Period> findAll() {
		SQLiteDatabase db = App.getDBHelper().getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM "
				+ PeriodTable.TABLE_NAME_PERIOD
				+ "ORDER BY id ASC",
				null);
		ArrayList<Period> periods = new ArrayList<Period>();

		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				int id = cursor.getInt(
						cursor.getColumnIndex(PeriodTable.COLUMN_NAME_ID));
				String type = cursor.getString(
						cursor.getColumnIndex(PeriodTable.COLUMN_NAME_TYPE));
				// FIXME: find a suitable sql data type for date
				Calendar startDate = null;
				Calendar endDate = null;
				// FIXME: find a way to fill this arrayList
				ArrayList<String> rooms = null; //cursor.getString(
						//cursor.getColumnIndex(new String[] {PeriodTable.COLUMN_NAME_ROOMS}));
				periods.add(new Period(type, startDate, endDate, rooms));
			}
		}

		return periods;
	}

	/**
	 * Create a period.
	 *
	 * @param obj
	 * @throws SQLiteCalendarException
	 */
	@Override
	public void create(Object obj) throws SQLiteCalendarException {
		Period period = (Period) obj;
		assert period != null;
		SQLiteDatabase db = App.getDBHelper().getWritableDatabase();

		ContentValues values = new ContentValues();

	}

	/**
	 * Update a period.
	 *
	 * @param obj
	 * @throws SQLiteCalendarException
	 */
	@Override
	public void update(Object obj) throws SQLiteCalendarException {
		Period period = (Period) obj;
		assert period != null;
		SQLiteDatabase db = App.getDBHelper().getWritableDatabase();

	}

	/**
	 * Delete a period,
	 *
	 * @param obj
	 * @throws SQLiteCalendarException
	 */
	@Override
	public void delete(Object obj) throws SQLiteCalendarException {
		Period period = (Period) obj;
		assert period != null;
		SQLiteDatabase db = App.getDBHelper().getWritableDatabase();

	}

	/**
	 * Delete all periods..
	 */
	@Override
	public void deleteAll() {
		SQLiteDatabase db = App.getDBHelper().getWritableDatabase();
		db.delete(PeriodTable.TABLE_NAME_PERIOD, null, null);

	}

}
