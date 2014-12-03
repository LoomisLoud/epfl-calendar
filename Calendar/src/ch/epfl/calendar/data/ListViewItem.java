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




    public void setmName(String mName) {
        
    }
    public Calendar getmStart() {
        return mDate;
    }

    public void setmStart(Calendar start) {
        mDate = start;
    }

    public Calendar getmEnd() {
        return null;
    }

    public void setmEnd(Calendar end) {
        
    }

    public PeriodType getmType() {
        return null;
    }

    public void setmType(PeriodType type) {
        
    }

    public int getmId() {
        return ID_SEPARATOR;
    }

    public void setmId(int id) {
       
    }

    public String getmLinkedCourse() {
        return "";
    }

    public void setmLinkedCourse(String linkedCourse) {
        
    }

    public String getmDescription() {
        return "";
    }

    public void setmDescription(String description) {
       
    }
    
    

}
