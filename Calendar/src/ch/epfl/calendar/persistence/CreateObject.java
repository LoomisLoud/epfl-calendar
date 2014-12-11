package ch.epfl.calendar.persistence;

import android.content.ContentValues;

/**
 * This class serves as a gateway to pass objects to {@link AsyncTask} working on the local database
 * @author AblionGE
 * 
 */
public class CreateObject {

    private ContentValues mContent;
    private String mTable;
    private String mNullColumnHack;

    /**
     * Create an instance of this class.
     * @param content values to store in database
     * @param nullColumnHack null
     * @param table the table in which to add the values
     */
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
