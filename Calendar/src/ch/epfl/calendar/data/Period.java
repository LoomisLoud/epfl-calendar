/**
 * 
 */
package ch.epfl.calendar.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * A period is a date, a start time and an end time + the type (exercises,
 * lesson) and the rooms of a course
 * 
 * @author AblionGE
 * 
 */
public class Period{
    private String mType;
    private Calendar mStartDate;
    private Calendar mEndDate;
    private List<String> mRooms;

    public Period(String date, String startTime, String endTime, String type,
            List<String> rooms) {
        this.mStartDate = createCalendar(date, startTime);
        this.mEndDate = createCalendar(date, endTime);
        this.mType = type;
        this.mRooms = rooms;
    }
    
    //FIXME : Check exception etc
    private Calendar createCalendar(String date, String hour) {
        if (date != null && hour != null) {
            //Format of date : dd.mm.yyyy
            String[] dateParts = date.split("\\.");
            for (String s : dateParts) {
                System.out.println(s);
            }
            //Format of hour : hh:mm
            String[] timeParts = hour.split("\\:");
            return new GregorianCalendar(Integer.parseInt(dateParts[2]),
                                        Integer.parseInt(dateParts[1]),
                                        Integer.parseInt(dateParts[0]),
                                        Integer.parseInt(timeParts[0]),
                                        Integer.parseInt(timeParts[1]));
        } else {
            return null;
        }
    }

    /**
     * @return the mType
     */
    public String getType() {
        return mType;
    }

    /**
     * @param mType
     *            the mType to set
     */
    public void setType(String type) {
        this.mType = type;
    }

    /**
     * @return the mRoom
     */
    public List<String> getRooms() {
        return new ArrayList<String>(mRooms);
    }

    /**
     * @param mRoom the mRoom to set
     */
    public void setRooms(List<String> room) {
        this.mRooms = new ArrayList<String>(room);
    }

    public Calendar getmStartDate() {
        return mStartDate;
    }

    public void setmStartDate(Calendar startDate) {
        this.mStartDate = startDate;
    }

    public Calendar getmEndDate() {
        return mEndDate;
    }

    public void setmEndDate(Calendar endDate) {
        this.mEndDate = endDate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "From " + mStartDate.toString() + " to " + mEndDate.toString() + " of type "
                + mType + " in rooms : " + mRooms + "\n";
    }
}
