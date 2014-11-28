package ch.epfl.calendar.persistence;

import android.content.ContentValues;

/**
 * 
 * @author AblionGE
 *
 */
public class CreateObject {

    private ContentValues mContent;
    private String mTable;
    private String mNullColumnHack;
    
    protected CreateObject(ContentValues content, String nullColumnHack, String table) {
        this.mContent = content;
        this.mTable = table;
        this.mNullColumnHack = nullColumnHack;
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

    protected String getNullColumnHack() {
        return mNullColumnHack;
    }

    protected void setmNullColumnHack(String nullColumnHack) {
        this.mNullColumnHack = nullColumnHack;
    }
}
