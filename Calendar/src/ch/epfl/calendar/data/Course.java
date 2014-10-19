package ch.epfl.calendar.data;

import java.util.ArrayList;
import java.util.List;

/**
 * A course of EPFL with its informations :
 *  - Name
 *  - Rooms
 *  - Date
 *  - Period classes
 * @author AblionGE
 *
 */
public class Course {
    private String mName;
    private List<String> mRooms;
    private List<Period> mPeriods;
    private String mTeacher;
    private int mCredits;
    
    public Course(String name, List<String> rooms, String date, String startTime,
            String endTime, String type) {
        this.mName = name;
        this.mRooms = rooms;
        this.mPeriods = new ArrayList<Period>();
        this.addPeriod(new Period(date, startTime, endTime, type));
    }
    
    //FIXME : DELETE !!! ???
    public Course(String name) {
        this.mName = name;
        this.mRooms = null;
        this.mPeriods = new ArrayList<Period>();
    }

    /**
     * Add a period to the current list of periods
     * @param period
     */
    public void addPeriod(Period period) {
        this.mPeriods.add(period);
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
     * @return the mPeriods
     */
    public List<Period> getPeriods() {
        return mPeriods;
    }

    /**
     * @param mPeriods the mPeriods to set
     */
    public void setPeriods(List<Period> periods) {
        this.mPeriods = periods;
    }

    /**
     * @return the mCredits
     */
    public int getCredits() {
        return mCredits;
    }

    /**
     * @param mCredits the mCredits to set
     */
    public void setCredits(int credits) {
        this.mCredits = credits;
    }

    /**
     * @return the mTeacher
     */
    public String getTeacher() {
        return mTeacher;
    }

    /**
     * @param mTeacher the mTeacher to set
     */
    public void setTeacher(String teacher) {
        this.mTeacher = teacher;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return mName + ", Rooms : " + mRooms + ", Periods : "
                + mPeriods + ", Teacher : " + mTeacher + ", nb Credits : "
                + mCredits;
    }
}
