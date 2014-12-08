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

    public CreateObject(ContentValues content, String nullColumnHack,
            String table) {
        this.mContent = content;
        this.mTable = table;
        this.mNullColumnHack = nullColumnHack;
    }

    protected ContentValues getContent() {
        return mContent;
    }

    protected String getTable() {
        return mTable;
    }

    protected String getNullColumnHack() {
        return mNullColumnHack;
    }
}
