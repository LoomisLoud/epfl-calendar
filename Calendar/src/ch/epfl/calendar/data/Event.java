/**
 * 
 */
package ch.epfl.calendar.data;

import java.util.Calendar;

/**
 * @author gilbrechbuhler
 *
 */
public class Event {
    private String mName;
    private Calendar mStartDate;
    private Calendar mEndDate;
    private String mType; // Maybe Enum, we have to look
    // Next is optional (like there is nothing in 
    private String mLinkedCourse; // See if you want a string with the name of the course or the course itself.
    
    /**
     * @return the mName
     */
    public String getName() {
        return mName;
    }
    /**
     * @param mName the mName to set
     */
    public void setName(String mName) {
        this.mName = mName;
    }
    /**
     * @return the mStartDate
     */
    public Calendar getStartDate() {
        return (Calendar) mStartDate.clone();
    }
    /**
     * @param mStartDate the mStartDate to set
     */
    public void setStartDate(Calendar mStartDate) {
        this.mStartDate = (Calendar) mStartDate.clone();
    }
    /**
     * @return the mEndDate
     */
    public Calendar getEndDate() {
        return (Calendar) mEndDate.clone();
    }
    /**
     * @param mEndDate the mEndDate to set
     */
    public void setEndDate(Calendar mEndDate) {
        this.mEndDate = (Calendar) mEndDate.clone();
    }
    /**
     * @return the mType
     */
    public String getmType() {
        return mType;
    }
    /**
     * @param mType the mType to set
     */
    public void setmType(String mType) {
        this.mType = mType;
    }
    /**
     * @return the mLinkedCourse
     */
    public String getLinkedCourse() {
        return mLinkedCourse;
    }
    /**
     * @param mLinkedCourse the mLinkedCourse to set
     */
    public void setLinkedCourse(String mLinkedCourse) {
        this.mLinkedCourse = mLinkedCourse;
    }
}
