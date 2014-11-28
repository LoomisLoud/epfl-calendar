package ch.epfl.calendar.persistence;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import ch.epfl.calendar.App;
import ch.epfl.calendar.DefaultActionBarActivity;
import ch.epfl.calendar.R;
import ch.epfl.calendar.utils.Logger;

/**
 * This AsyncTask allows to access to the DB in an asynchronous way
 * 
 * @author AblionGE
 * 
 */
public class CreateRowDBTask extends AsyncTask<CreateObject, Void, Long> {

    private Context mContext = null;
    private DatabaseUploadInterface mDBUpload;
    private LocalDatabaseInterface mLocalDBI;
    private ProgressDialog mDialog;
    
    private static Context ACTIVITY = App.getAppContext();
    
    private static final String ERROR_CREATE = "Unable to create a new row!";
    private static final String ERROR_DELETE = "Unable to delete a row!";
    private static final String ERROR_UPDATE = "Unable to update a row!";

    private static final String SUCCESS_DELETE = "Row successfully deleted!";
    private static final String SUCCESS_UPDATE = "Row successfully updated!";
    private static final String SUCCESS_CREATE = "Row Successfully created!";

    public CreateRowDBTask(Context context, DatabaseUploadInterface dbUpload,
            LocalDatabaseInterface localDBI) {
        mContext = context;
        mDBUpload = dbUpload;
        mLocalDBI = localDBI;
    }

    @Override
    protected void onPreExecute() {
        
    }
    
    @Override
    protected Long doInBackground(CreateObject... params) {
        CreateObject object = params[0];
        
        String table = object.getTable();
        String nullCollumnHack = object.getNullColumnHack();
        ContentValues values = object.getContent();
        
        SQLiteDatabase db = App.getDBHelper().getWritableDatabase();
        
        long rowId = db.insert(table, nullCollumnHack, values);
        if (rowId == -1) {
            Log.e(Logger.CALENDAR_SQL_ERROR, ERROR_CREATE);
            throw new SQLiteCalendarException(ERROR_CREATE);
        }

        Log.i(Logger.CALENDAR_SQL_SUCCES, SUCCESS_CREATE);

        return rowId;
    }

    @Override
    protected void onPostExecute(Long id) {
//        if (mDialog != null) {
//            mDialog.dismiss();
//        }
        App.getActionBar().asyncTaskStoreFinished();
        //do something with id
    }
}
