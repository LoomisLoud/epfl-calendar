package ch.epfl.calendar.persistence;

import android.content.ContentValues;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import ch.epfl.calendar.App;
import ch.epfl.calendar.utils.Logger;

/**
 * 
 * @author AblionGE
 * 
 */
public class UpdateRowDBTask extends AsyncTask<UpdateObject, Void, Long> {

    private static final String ERROR_UPDATE = "Unable to update a new row!";
    private static final String SUCCESS_UPDATE = "Row Successfully update!";

    @Override
    protected Long doInBackground(UpdateObject... params) {
        try {
            UpdateObject object = params[0];

            String table = object.getTable();
            String whereClause = object.getWhereClause();
            String[] whereArgs = object.getWhereArgs();
            ContentValues values = object.getContent();
    
            SQLiteDatabase db = DBQuester.openDatabase();
    
            long rowId = -1;
            try {
                rowId = db.update(table, values, whereClause, whereArgs);
            } catch (SQLiteConstraintException e) {
                Log.e(Logger.CALENDAR_SQL_ERROR, ERROR_UPDATE);
                throw new SQLiteCalendarException(ERROR_UPDATE);
            }
            if (rowId == -1) {
                Log.e(Logger.CALENDAR_SQL_ERROR, ERROR_UPDATE);
                throw new SQLiteCalendarException(ERROR_UPDATE);
            }
    
            Log.i(Logger.CALENDAR_SQL_SUCCES, SUCCESS_UPDATE);
    
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
