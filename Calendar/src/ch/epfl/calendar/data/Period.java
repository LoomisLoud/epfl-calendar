/**
 * 
 */
package ch.epfl.calendar.data;

import java.util.List;

/**
 * A period is a date, a start time and an end time + the type (exercises, lesson)
 * and the rooms of a course
 * @author AblionGE
 *
 */
public class Period {
    private String mDate;
    private String mStartTime;
    private String mEndTime;
    private String mType;
    private List<String> mRooms;
    
    public Period(String date, String startTime, String endTime,
            String type, List<String> rooms) {
        this.mDate = date;
        this.mStartTime = startTime;
        this.mEndTime = endTime;
        this.mType = type;
        this.mRooms = rooms;
    }
    

    /**
     * @return the mDate
     */
    public String getDate() {
        return mDate;
    }
    
    
    

    /**
     * @param mDate the mDate to set
     */
    public void setDate(String date) {
        this.mDate = date;
    }

    /**
     * @return the mStartTime
     */
    public String getStartTime() {
        return mStartTime;
    }
    public int getHourStartTime(){
        String tab[]=mStartTime.split(":");
        return Integer.parseInt(tab[0]);
    }
    public int getMinuteStartTime(){
        String tab[]=mStartTime.split(":");
        return Integer.parseInt(tab[1]);
    }

    /**
     * @param mStartTime the mStartTime to set
     */
    public void setStartTime(String startTime) {
        this.mStartTime = startTime;
    }

    /**
     * @return the mEndTime
     */
    public String getEndTime() {
        return mEndTime;
    }
   

    /**
     * @param mEndTime the mEndTime to set
     */
    public void setEndTime(String endTime) {
        this.mEndTime = endTime;
    }

    /**
     * @return the mType
     */
    public String getType() {
        return mType;
    }

    /**
     * @param mType the mType to set
     */
    public void setType(String type) {
        this.mType = type;
    }

    /**
     * @return the mRoom
     */
    public List<String> getRooms() {
        return mRooms;
    }

    /**
     * @param mRoom the mRoom to set
     */
    public void setRooms(List<String> room) {
        this.mRooms = room;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return mDate + " from " + mStartTime
                + " to " + mEndTime + " of type " + mType + "in rooms : "
                + mRooms;
    }
}
