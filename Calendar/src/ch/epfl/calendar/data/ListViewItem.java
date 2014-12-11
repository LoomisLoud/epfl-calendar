package ch.epfl.calendar.data;

import java.util.Calendar;

/**
 * An item of a {@link ListView}
 * @author MatthiasLeroyEPFL
 *
 */
public class ListViewItem {
   
    private final static int ID_SEPARATOR = -2;
    private Calendar mDate;
    
    /**
     * Create a new {@link ListViewItem} with the date date.
     * @param date the date of the item
     */
    public ListViewItem(Calendar date) {
        mDate = date;
    }
    
    /**
     * @return returns an empty String
     */
    public String getName() {
        return "";
    }
   
    /**
     * @return the start date (date given in constructor)
     */
    public Calendar getmStart() {
        return mDate;
    }

    /**
     * Set the date of the item
     * @param date the new date of the item
     */
    public void setmDate(Calendar date) {
        this.mDate = date;
    }

    /**
     * 
     * @return null
     */
    public Calendar getEnd() {
        return null;
    }

    /**
     * 
     * @return null
     */
    public PeriodType getType() {
        return null;
    }

    /**
     * 
     * @return an ID separator
     */
    public int getId() {
        return ID_SEPARATOR;
    }

    /**
     * 
     * @return an empty string
     */
    public String getLinkedCourse() {
        return "";
    }

    /**
     * 
     * @return an empty string
     */
    public String getDescription() {
        return "";
    }
}
