/**
 *
 */
package ch.epfl.calendar.persistence;

import android.content.ContentValues;
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

    private static final String ERROR_CREATE = "Unable to create a new period!";
    private static final String ERROR_DELETE = "Unable to delete a period!";
    private static final String ERROR_UPDATE = "Unable to update a period!";

    private static final String SUCCESS_CREATE = "Period successfully created!";
    private static final String SUCCESS_DELETE = "Period successfully deleted";
    private static final String SUCCESS_UPDATE = "Period successfully updated";

    private static PeriodDataSource mPeriodDataSource;

    public static PeriodDataSource getInstance() {
        if (PeriodDataSource.mPeriodDataSource == null) {
            PeriodDataSource.mPeriodDataSource = new PeriodDataSource();
        }
        return PeriodDataSource.mPeriodDataSource;
    }

    /**
     * Create a period.
     *
     * @param obj
     * @throws SQLiteCalendarException
     */
    @Override
    public void create(Object obj, String key) throws SQLiteCalendarException {
        Period period = (Period) obj;
        assert period != null;
        SQLiteDatabase db = App.getDBHelper().getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PeriodTable.COLUMN_NAME_ID, period.getId());
        values.put(PeriodTable.COLUMN_NAME_TYPE, period.getType().toString());
        values.put(PeriodTable.COLUMN_NAME_STARTDATE, App.calendarToBasicFormatString(period.getStartDate()));
        values.put(PeriodTable.COLUMN_NAME_ENDDATE, App.calendarToBasicFormatString(period.getEndDate()));
        String roomsCSV = App.csvStringFromList(period.getRooms());
        values.put(PeriodTable.COLUMN_NAME_ROOMS, roomsCSV);
        values.put(PeriodTable.COLUMN_NAME_COURSE, key);

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
    public void update(Object obj, String key) throws SQLiteCalendarException {
        Period period = (Period) obj;
        assert period != null;
        SQLiteDatabase db = App.getDBHelper().getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PeriodTable.COLUMN_NAME_ID, period.getId());
        values.put(PeriodTable.COLUMN_NAME_TYPE, period.getType().toString());
        values.put(PeriodTable.COLUMN_NAME_STARTDATE, App.calendarToBasicFormatString(period.getStartDate()));
        values.put(PeriodTable.COLUMN_NAME_ENDDATE, App.calendarToBasicFormatString(period.getEndDate()));
        String roomsCSV = App.csvStringFromList(period.getRooms());
        values.put(PeriodTable.COLUMN_NAME_ROOMS, roomsCSV);
        values.put(PeriodTable.COLUMN_NAME_COURSE, key);

        long rowId = db.update(PeriodTable.TABLE_NAME_PERIOD, values,
                PeriodTable.COLUMN_NAME_ID + " = ?",
                new String[] {String.valueOf(period.getId())});
        if (rowId == -1) {
            Log.e(Logger.CALENDAR_SQL_ERROR, PeriodDataSource.ERROR_UPDATE);
            throw new SQLiteCalendarException(PeriodDataSource.ERROR_UPDATE);
        }

        Log.i(Logger.CALENDAR_SQL_SUCCES, PeriodDataSource.SUCCESS_UPDATE);
    }

    /**
     * Delete a period,
     *
     * @param obj
     * @throws SQLiteCalendarException
     */
    @Override
    public void delete(Object obj, String key) throws SQLiteCalendarException {
        Period period = (Period) obj;
        assert period != null;
        SQLiteDatabase db = App.getDBHelper().getWritableDatabase();

        long rowId = db.delete(
                PeriodTable.TABLE_NAME_PERIOD,
                PeriodTable.COLUMN_NAME_ID + " = " + period.getId(),
                null);
        if (rowId == -1) {
            Log.e(Logger.CALENDAR_SQL_ERROR, PeriodDataSource.ERROR_DELETE);
            throw new SQLiteCalendarException(PeriodDataSource.ERROR_DELETE);
        }

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
