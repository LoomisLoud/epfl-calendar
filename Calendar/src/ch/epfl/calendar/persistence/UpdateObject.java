package ch.epfl.calendar.persistence;

import android.content.ContentValues;

/**
 * Used by {@link AsyncTask} to update objects in database
 * 
 * @author AblionGE
 * 
 */
public class UpdateObject {

    private ContentValues mContent;
    private String mTable;
    private String mWhereClause;
    private String[] mWhereArgs;

    /**
     * 
     * @param content the content to update
     * @param table the table to update
     * @param whereClause the where clause to find the row to update
     * @param whereArgs the where arguments to find the row tou update
     */
    public UpdateObject(ContentValues content, String table,
            String whereClause, String[] whereArgs) {
        this.mContent = content;
        this.mTable = table;
        this.mWhereClause = whereClause;
        this.mWhereArgs = whereArgs;
    }

    protected ContentValues getContent() {
        return mContent;
    }

    protected String getTable() {
        return mTable;
    }

    protected String getWhereClause() {
        return mWhereClause;
    }

    protected String[] getWhereArgs() {
        return mWhereArgs;
    }
}
