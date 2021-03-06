package ch.epfl.calendar.persistence;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import ch.epfl.calendar.App;
import ch.epfl.calendar.utils.Logger;

/**
 * Allows to update rows in the database in an asynchronous way
 * @author AblionGE
 * 
 */
public class UpdateRowDBTask extends AsyncTask<UpdateObject, Void, Long> {

    private static final String ERROR_UPDATE = "Unable to update a new row!";
    private static final String SUCCESS_UPDATE = "Row Successfully update!";

    @Override
    protected Long doInBackground(UpdateObject... params) {
        UpdateObject object = params[0];

        String table = object.getTable();
        String whereClause = object.getWhereClause();
        String[] whereArgs = object.getWhereArgs();
        ContentValues values = object.getContent();

        SQLiteDatabase db = DBQuester.openDatabase();

        long rowId = -1;
        rowId = db.update(table, values, whereClause, whereArgs);
        if (rowId == -1) {
            Log.e(Logger.CALENDAR_SQL_ERROR, ERROR_UPDATE);
        }

        Log.i(Logger.CALENDAR_SQL_SUCCES, SUCCESS_UPDATE);

        return rowId;
    }

    @Override
    protected void onPostExecute(Long id) {
        App.getActionBar().asyncTaskStoreFinished();
    }

}
