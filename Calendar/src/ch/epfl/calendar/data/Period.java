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

    private PeriodType mType;
    private Calendar mStartDate;
    private Calendar mEndDate;
    private List<String> mRooms;
    
    private static final int DATE_PARTS_LENGTH = 3;
    private static final int HOUR_PARTS_LENGTH = 2;
    private static final int DAY_MIN = 1;
    private static final int DAY_MAX = 31;
    private static final int MONTH_MIN = 1;
    private static final int MONTH_MAX = 12;
    private static final int YEAR_MIN = 1970;
    private static final int HOUR_MIN = 0;
    private static final int HOUR_MAX = 2;
    private static final int MINUTE_MIN = 0;
    private static final int MINUTE_MAX = 59;
    
    private static final String TYPE_EXERCISES_FR = "Exercices";
    private static final String TYPE_EXERCISES_EN = "Exercises";
    private static final String TYPE_PROJECT_FR = "Projet";
    private static final String TYPE_PROJECT_EN = "Project";
    private static final String TYPE_LECTURE_FR = "Cours";
    private static final String TYPE_LECTURE_EN = "Lecture";

    public Period(String date, String startTime, String endTime, String type,
            List<String> rooms) {
        this.mStartDate = createCalendar(date, startTime);
        this.mEndDate = createCalendar(date, endTime);
        if (this.mStartDate != null && this.mEndDate != null) {
            if (mStartDate.after(mEndDate)) {
                Calendar temp = mStartDate;
                mStartDate = mEndDate;
                mEndDate = temp;
            }
        }
        if (type != null) {
            setType(type);
        }
        this.mRooms = rooms;
    }
    
    private Calendar createCalendar(String date, String hourArg) {
        if (date != null && hourArg != null) {
            //Format of date : dd.mm.yyyy
            String[] dateParts = date.split("\\.");
            if (dateParts.length != DATE_PARTS_LENGTH) {
                //Exception catched in ISAXMLParser
                throw new IllegalArgumentException("Parsing date failed");
            }
            //Format of hour : hh:mm
            String[] timeParts = hourArg.split("\\:");
            if (timeParts.length != HOUR_PARTS_LENGTH) {
                throw new IllegalArgumentException("Parsing date failed");
            }
            int day = Integer.parseInt(dateParts[2]);
            int month = Integer.parseInt(dateParts[1])-1;
            int year = Integer.parseInt(dateParts[0]);
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);
            
            //Day
            if (day < DAY_MIN || day > DAY_MAX) {
                throw new IllegalArgumentException("Parsed date is impossible");
            }
            if (month < MONTH_MIN || month > MONTH_MAX) {
                throw new IllegalArgumentException("Parsed date is impossible");
            }
            if (year < YEAR_MIN) {
                throw new IllegalArgumentException("Parsed date is impossible");
            }
            if (minute < MINUTE_MIN || minute > MINUTE_MAX) {
                throw new IllegalArgumentException("Parsed date is impossible");
            }
            if (hour < HOUR_MIN || hour > HOUR_MAX) {
                throw new IllegalArgumentException("Parsed date is impossible");
            }
            return new GregorianCalendar(day,
                                        month,
                                        year,
                                        hour,
                                        minute);
        } else {
            throw new NullPointerException("Date or Hour is null in createCalendar()");
        }
    }

    /**
     * @return the mType
     */
    public PeriodType getType() {
        return mType;
    }

    /**
     * @param mType
     *            the mType to set
     */
    public void setType(PeriodType type) {
        if (type == null) {
            throw new NullPointerException();
        }
        this.mType = type;
    }
    
    public void setType(String type) {
        if (type == null) {
            throw new NullPointerException();
        }
        PeriodType periodType= null;
        if (type.equals(TYPE_EXERCISES_EN) || type.equals(TYPE_EXERCISES_FR)) {
            periodType = PeriodType.EXERCISES;
        } else if (type.equals(TYPE_PROJECT_EN) || type.equals(TYPE_PROJECT_FR)) {
            periodType = PeriodType.PROJECT;
        } else if (type.equals(TYPE_LECTURE_EN) || type.equals(TYPE_LECTURE_FR)) {
            periodType = PeriodType.LECTURE;
        } else {
            periodType = PeriodType.DEFAULT;
        }
        this.mType = periodType;
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
        if (room == null) {
            throw new NullPointerException();
        }
        this.mRooms = new ArrayList<String>(room);
    }

    public Calendar getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Calendar startDate) {
        if (startDate == null) {
            throw new NullPointerException();
        }
        this.mStartDate = startDate;
    }

    public Calendar getEndDate() {
        return mEndDate;

    }

    public void setEndDate(Calendar endDate) {
        if (endDate == null) {
            throw new NullPointerException();
        }
        this.mEndDate = endDate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "From " + mStartDate.get(Calendar.DATE) + "." + mStartDate.get(Calendar.MONTH) + "." 
                + mStartDate.get(Calendar.YEAR) + " at "
                + mStartDate.get(Calendar.HOUR_OF_DAY) + ":" + mStartDate.get(Calendar.MINUTE)
                + " to " + mEndDate.get(Calendar.DATE) + "."
                + mEndDate.get(Calendar.MONTH) + "." 
                + mEndDate.get(Calendar.YEAR) + " at "
                + mEndDate.get(Calendar.HOUR_OF_DAY) + ":" + mEndDate.get(Calendar.MINUTE) + " of type "
                + mType + " in rooms : " + mRooms + "\n";
    }
}
