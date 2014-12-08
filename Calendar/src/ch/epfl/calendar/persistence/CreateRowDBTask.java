package ch.epfl.calendar.persistence;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;
import ch.epfl.calendar.App;
import ch.epfl.calendar.utils.Logger;

/**
 * This AsyncTask allows to access to the DB in an asynchronous way
 * 
 * @author AblionGE
 * 
 */
public class CreateRowDBTask extends AsyncTask<CreateObject, Void, Long> {

    private static final String ERROR_CREATE = "Unable to create a new row!";
    private static final String SUCCESS_CREATE = "Row Successfully created!";

    @Override
    protected Long doInBackground(CreateObject... params) {
        try {
            CreateObject object = params[0];
    
            String table = object.getTable();
            String nullCollumnHack = object.getNullColumnHack();
            ContentValues values = object.getContent();
    
            SQLiteDatabase db = DBQuester.openDatabase();
    
            long rowId = -1;
            try {
                rowId = db.insert(table, nullCollumnHack, values);
            } catch (SQLiteException e) {
                Log.e(Logger.CALENDAR_SQL_ERROR, ERROR_CREATE);
                throw new SQLiteCalendarException(ERROR_CREATE);
            }
            if (rowId == -1) {
                Log.e(Logger.CALENDAR_SQL_ERROR, ERROR_CREATE);
                throw new SQLiteCalendarException(ERROR_CREATE);
            }
    
            Log.i(Logger.CALENDAR_SQL_SUCCES, SUCCESS_CREATE);
    
            return rowId;
        } catch (SQLiteCalendarException e) {
            return (long) -1;
        }
    }

    @Override
    protected void onPostExecute(Long id) {
        App.getActionBar().asyncTaskStoreFinished();
    }
}
