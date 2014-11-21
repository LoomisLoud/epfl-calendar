/**
 *
 */
package ch.epfl.calendar.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * A period is a date, a start time and an end time + the type (exercises,
 * lesson) and the rooms of a course
 *
 * @author AblionGE
 *
 */

public class Period {

    private PeriodType mType;
    private Calendar mStartDate;
    private Calendar mEndDate;
    private List<String> mRooms;

    private static final int DATE_PARTS_LENGTH = 3;
    private static final int HOUR_PARTS_LENGTH = 2;
    private static final int ZERO_INDEX = 0;
    private static final int END_DATE_INDEX = 10;
    private static final int START_TIME_INDEX = 11;
    private static final int DAY_MIN = 1;
    private static final int DAY_MAX = 31;
    // In a gregorian java calendar, a month starts at 0 and ends at 11
    private static final int MONTH_MIN = 0;
    private static final int MONTH_MAX = 11;
    private static final int YEAR_MIN = 1970;
    private static final int HOUR_MIN = 0;
    private static final int HOUR_MAX = 23;
    private static final int MINUTE_MIN = 0;
    private static final int MINUTE_MAX = 59;
    private static final int DOUBLE_DIGIT = 10;

    private static final String TYPE_EXERCISES_FR = "Exercices";
    private static final String TYPE_EXERCISES_EN = "Exercises";
    private static final String TYPE_PROJECT_FR = "Projet";
    private static final String TYPE_PROJECT_EN = "Project";
    private static final String TYPE_LECTURE_FR = "Cours";
    private static final String TYPE_LECTURE_EN = "Lecture";

    /**
     * Constuct the period object.
     * date format must be of format dd.mm.yyyy
     * startTime and endTime must be of format hh:mm
     * @param date
     * @param startTime
     * @param endTime
     * @param type
     * @param rooms
     */
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

    /**
     * Construc the period object.
     * startDate and endDate must be of format 'dd.mm.yyyy hh:mm'
     * @param type
     * @param startDate
     * @param endDate
     * @param rooms
     */
    public Period(String type, String startDate, String endDate, List<String> rooms) {
    	this(startDate.substring(ZERO_INDEX, END_DATE_INDEX),
    		 startDate.substring(START_TIME_INDEX),
    		 endDate.substring(START_TIME_INDEX),
    		 type,
    		 rooms);
    }

    private Calendar createCalendar(String date, String hourArg) {
        if (date != null && hourArg != null) {
            // Format of date : dd.mm.yyyy
            String[] dateParts = date.split("\\.");
            if (dateParts.length != DATE_PARTS_LENGTH) {
                // Exception catched in ISAXMLParser
                throw new IllegalArgumentException("Parsing date failed");
            }
            // Format of hour : hh:mm
            String[] timeParts = hourArg.split("\\:");
            if (timeParts.length != HOUR_PARTS_LENGTH) {
                throw new IllegalArgumentException("Parsing date failed");
            }
            int year = Integer.parseInt(dateParts[2]);
            int month = Integer.parseInt(dateParts[1]) - 1;
            int day = Integer.parseInt(dateParts[0]);
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

            // Day
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
            return new GregorianCalendar(year, month, day, hour, minute);
        } else {
            throw new NullPointerException(
                    "Date or Hour is null in createCalendar()");
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
        PeriodType periodType = null;
        if (type.equalsIgnoreCase(TYPE_EXERCISES_EN)
                || type.equalsIgnoreCase(TYPE_EXERCISES_FR)) {
            periodType = PeriodType.EXERCISES;
        } else if (type.equalsIgnoreCase(TYPE_PROJECT_EN)
                || type.equalsIgnoreCase(TYPE_PROJECT_FR)) {
            periodType = PeriodType.PROJECT;
        } else if (type.equalsIgnoreCase(TYPE_LECTURE_EN)
                || type.equalsIgnoreCase(TYPE_LECTURE_FR)) {
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
     * @param mRoom
     *            the mRoom to set
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
        String periodType;
        switch (mType) {
            case LECTURE:
                periodType = "L";
                break;
            case PROJECT:
                periodType = "P";
                break;
            case EXERCISES:
                periodType = "E";
                break;
            case DEFAULT:
                periodType = "D";
                break;
            default:
                periodType = "D";
                break;
        }

        String day = mStartDate.getDisplayName(Calendar.DAY_OF_WEEK,
                Calendar.SHORT, Locale.ENGLISH);
        int startHour = mStartDate.get(Calendar.HOUR_OF_DAY);
        int startMinute = mStartDate.get(Calendar.MINUTE);
        int endHour = mEndDate.get(Calendar.HOUR_OF_DAY);
        int endMinute = mEndDate.get(Calendar.MINUTE);
        String startHourToString = Integer.toString(startHour);
        String startMinuteToString = Integer.toString(startMinute);
        String endHourToString = Integer.toString(endHour);
        String endMinuteToString = Integer.toString(endMinute);
        if (isSingleDigit(startHour)) {
            startHourToString = "0" + startHour;
        }
        if (isSingleDigit(startMinute)) {
            startMinuteToString = "0" + startMinute;
        }
        if (isSingleDigit(endHour)) {
            endHourToString = "0" + endHour;
        }
        if (isSingleDigit(endMinute)) {
            endMinuteToString = "0" + endMinute;
        }

        return periodType + " : " + day + " " + startHourToString + ":"
                + startMinuteToString + "-" + endHourToString + ":"
                + endMinuteToString;
    }

    private boolean isSingleDigit(int number) {
        return number < DOUBLE_DIGIT;
    }
}
