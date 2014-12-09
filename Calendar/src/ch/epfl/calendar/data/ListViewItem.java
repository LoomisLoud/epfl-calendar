package ch.epfl.calendar.data;

import java.util.Calendar;

/**
 * @author MatthiasLeroyEPFL
 *
 */
public class ListViewItem {
   
    private final static int ID_SEPARATOR = -2;
    private Calendar mDate;
    
    public ListViewItem(Calendar date) {
        mDate = date;
    }
    
    public String getName() {
        return "";
    }
   
    public Calendar getmStart() {
        return mDate;
    }

    public void setmDate(Calendar date) {
        this.mDate = date;
    }

    public Calendar getEnd() {
        return null;
    }

    public PeriodType getType() {
        return null;
    }

    public int getId() {
        return ID_SEPARATOR;
    }

   

    public String getLinkedCourse() {
        return "";
    }

    
    public String getDescription() {
        return "";
    }
}
