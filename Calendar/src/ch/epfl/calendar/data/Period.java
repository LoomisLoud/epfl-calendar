/**
 * 
 */
package ch.epfl.calendar.data;

/**
 * A period is a date, a start time and an end time + the type (exercises, lesson)
 * of a course
 * @author AblionGE
 *
 */
public class Period {
    private String mDate;
    private String mStartTime;
    private String mEndTime;
    private String mType;
    
    public Period(String date, String startTime, String endTime, String type) {
        this.mDate = date;
        this.mStartTime = startTime;
        this.mEndTime = endTime;
        this.mType = type;
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
}
