package ch.epfl.calendar.data;

import java.util.Calendar;

/**
 * @author MatthiasLeroyEPFL
 *
 */
public class ListViewItem {
    private Calendar mDate;
    private final static int ID_SEPARATOR = -2;
    
    public ListViewItem(Calendar date) {
        mDate = date;
    }
    
    public String getmName() {
        return "";
    }
   
    public Calendar getmStart() {
        return mDate;
    }

    public void setmDate(Calendar date) {
        this.mDate = date;
    }

    public Calendar getmEnd() {
        return null;
    }

    public PeriodType getmType() {
        return null;
    }

    public int getmId() {
        return ID_SEPARATOR;
    }

   

    public String getmLinkedCourse() {
        return "";
    }

    
    public String getmDescription() {
        return "";
    }
}
