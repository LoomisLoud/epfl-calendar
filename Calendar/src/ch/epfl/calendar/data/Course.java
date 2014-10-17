package ch.epfl.calendar.data;

import java.util.List;

/**
 * A course of EPFL with its informations :
 *  - Name
 *  - Rooms
 *  - Date
 *  - StartDate(hour)
 *  - EndDate(hour)
 *  - Type (Course, Exercises,...)
 * @author AblionGE
 *
 */
public class Course {
    private String mName;
    private List<String> mRooms;
    private String mDate;
    private String mStartDate;
    private String mEndDate;
    private String mType;
    
    public Course(String name, List<String> rooms, String date, String startDate,
            String endDate, String type) {
        this.mName = name;
        this.mRooms = rooms;
        this.mDate = date;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        this.mType = type;
    }
    
    //FIXME : DELETE !!! ???
    public Course(String name) {
        this.mName = name;
        this.mRooms = null;
        this.mDate = null;
        this.mStartDate = null;
        this.mEndDate = null;
        this.mType = null;
    }

    /**
     * @return the mName
     */
    public String getName() {
        return mName;
    }

    /**
     * @param mName the mName to set
     */
    public void setName(String name) {
        this.mName = name;
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
     * @return the mStartDate
     */
    public String getStartDate() {
        return mStartDate;
    }

    /**
     * @param mStartDate the mStartDate to set
     */
    public void setStartDate(String startDate) {
        this.mStartDate = startDate;
    }

    /**
     * @return the mEndDate
     */
    public String getEndDate() {
        return mEndDate;
    }

    /**
     * @param mEndDate the mEndDate to set
     */
    public void setEndDate(String endDate) {
        this.mEndDate = endDate;
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
