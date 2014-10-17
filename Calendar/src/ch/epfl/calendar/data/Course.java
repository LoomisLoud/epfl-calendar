package ch.epfl.calendar.data;

/**
 * A course of EPFL
 * @author AblionGE
 *
 */
public class Course {
    private String mName;
    private String mRoom;
    private String mStartDate;
    private String mEndDate;
    
    public Course(String name, String room, String startDate, String endDate) {
        this.mName = name;
        this.mRoom = room;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
    }
    
    //TODO : DELETE !!! ???
    public Course(String name) {
        this.mName = name;
        this.mRoom = null;
        this.mStartDate = null;
        this.mEndDate = null;
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
    public String getRoom() {
        return mRoom;
    }

    /**
     * @param mRoom the mRoom to set
     */
    public void setRoom(String room) {
        this.mRoom = room;
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
}
