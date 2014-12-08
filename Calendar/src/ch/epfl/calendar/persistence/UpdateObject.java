package ch.epfl.calendar.persistence;

import android.content.ContentValues;

/**
 * 
 * @author AblionGE
 * 
 */
public class UpdateObject {

    private ContentValues mContent;
    private String mTable;
    private String mWhereClause;
    private String[] mWhereArgs;

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
