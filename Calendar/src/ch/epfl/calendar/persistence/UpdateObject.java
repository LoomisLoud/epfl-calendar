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
    
    protected UpdateObject(ContentValues content, String table, String whereClause, String[] whereArgs) {
        this.mContent = content;
        this.mTable = table;
        this.mWhereClause = whereClause;
        this.mWhereArgs = whereArgs;
    }

    protected ContentValues getContent() {
        return mContent;
    }

    protected void setContent(ContentValues content) {
        this.mContent = content;
    }

    protected String getTable() {
        return mTable;
    }

    protected void setTable(String table) {
        this.mTable = table;
    }

    protected String getWhereClause() {
        return mWhereClause;
    }

    protected void setWhereClause(String whereClause) {
        this.mWhereClause = whereClause;
    }

    protected String[] getWhereArgs() {
        return mWhereArgs;
    }

    protected void setWhereArgs(String[] whereArgs) {
        this.mWhereArgs = whereArgs;
    }
}
