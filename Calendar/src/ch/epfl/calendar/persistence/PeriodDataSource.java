/**
 *
 */
package ch.epfl.calendar.persistence;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ch.epfl.calendar.App;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.utils.Logger;

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
				// TODO check what to do with the id (autoincrement ?)
//				int id = cursor.getInt(
//						cursor.getColumnIndex(PeriodTable.COLUMN_NAME_ID));
				String type = cursor.getString(
						cursor.getColumnIndex(PeriodTable.COLUMN_NAME_TYPE));
				String startDate = cursor.getString(
						cursor.getColumnIndex(PeriodTable.COLUMN_NAME_STARTDATE));
				String endDate = cursor.getString(
						cursor.getColumnIndex(PeriodTable.COLUMN_NAME_ENDDATE));
				String roomsCSV = cursor.getString(
						cursor.getColumnIndex(PeriodTable.COLUMN_NAME_ROOMS));
				ArrayList<String> rooms = App.parseFromCSVString(roomsCSV);

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
		values.put(PeriodTable.COLUMN_NAME_TYPE, period.getType().toString());
		// TODO check the return value of Calendar toString method
		values.put(PeriodTable.COLUMN_NAME_STARTDATE, period.getStartDate().toString());
		values.put(PeriodTable.COLUMN_NAME_ENDDATE, period.getEndDate().toString());
		String roomsCSV = App.csvStringFromList(period.getRooms());
		values.put(PeriodTable.COLUMN_NAME_ROOMS, roomsCSV);
		// TODO check how to store this foreign key value
//		values.put(PeriodTable.COLUMN_NAME_COURSE_ID, ???);

		long rowId = db.insert(PeriodTable.TABLE_NAME_PERIOD, null, values);
		if (rowId == -1) {
			Log.e(Logger.CALENDAR_SQL_ERROR, PeriodDataSource.ERROR_CREATE);
			throw new SQLiteCalendarException(PeriodDataSource.ERROR_CREATE);
		}

		Log.i(Logger.CALENDAR_SQL_SUCCES, PeriodDataSource.SUCCESS_CREATE);
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

		ContentValues values = new ContentValues();
		values.put(PeriodTable.COLUMN_NAME_TYPE, period.getType().toString());
		// TODO check the return value of Calendar toString method
		values.put(PeriodTable.COLUMN_NAME_STARTDATE, period.getStartDate().toString());
		values.put(PeriodTable.COLUMN_NAME_ENDDATE, period.getEndDate().toString());
		String roomsCSV = App.csvStringFromList(period.getRooms());
		values.put(PeriodTable.COLUMN_NAME_ROOMS, roomsCSV);
		// TODO check how to store this foreign key value
//		values.put(PeriodTable.COLUMN_NAME_COURSE_ID, ???);

		// TODO create id attribute and getter in Period class
//		long rowId = db.update(
//				PeriodTable.TABLE_NAME_PERIOD,
//				values,
//				PeriodTable.COLUMN_NAME_ID + " = ?",
//				new String[] {String.valueOf(period.getId())});
//		if (rowId == -1) {
//			Log.e(Logger.CALENDAR_SQL_ERROR, PeriodDataSource.ERROR_UPDATE);
//			throw new SQLiteCalendarException(PeriodDataSource.ERROR_UPDATE);
//		}

		Log.i(Logger.CALENDAR_SQL_SUCCES, PeriodDataSource.SUCCESS_UPDATE);
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

		// TODO create id attribute and getter in Period class
//		long rowId = db.delete(
//				PeriodTable.TABLE_NAME_PERIOD,
//				PeriodTable.COLUMN_NAME_ID + " = '" + period.getId() + "'",
//				null);
//		if (rowId == -1) {
//			Log.e(Logger.CALENDAR_SQL_ERROR, PeriodDataSource.ERROR_DELETE);
//			throw new SQLiteCalendarException(PeriodDataSource.ERROR_DELETE);
//		}

		Log.i(Logger.CALENDAR_SQL_SUCCES, PeriodDataSource.SUCCESS_DELETE);

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
