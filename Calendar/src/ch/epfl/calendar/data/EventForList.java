package ch.epfl.calendar.data;

import java.util.Calendar;
import java.util.Locale;

/**
 * @author MatthiasLeroyEPFL
 *
 */
public class EventForList {

    private String mEventName;
    private Calendar mStart;
    private Calendar mEnd;
    private PeriodType mType;
    private int mId;
    private String mLinkedCourse;
    private String mDescription;

    public EventForList(String name, Calendar start, Calendar end,
            PeriodType type, int id, String linkedCourse, String description) {
        mEventName = name;
        mStart = start;
        mEnd = end;
        mType = type;
        mId = id;
        mLinkedCourse=linkedCourse;
        mDescription=description;
    }

    public String getmEventName() {
        return mEventName;
    }

    public void setmEventName(String eventName) {
        this.mEventName = eventName;
    }

    public Calendar getmStart() {
        return mStart;
    }

    public void setmStart(Calendar start) {
        this.mStart = start;
    }

    public Calendar getmEnd() {
        return mEnd;
    }

    public void setmEnd(Calendar end) {
        this.mEnd = end;
    }

    public PeriodType getmType() {
        return mType;
    }

    public void setmType(PeriodType type) {
        this.mType = type;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int id) {
        this.mId = id;
    }

    public String getmLinkedCourse() {
        return mLinkedCourse;
    }

    public void setmLinkedCourse(String linkedCourse) {
        this.mLinkedCourse = linkedCourse;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String description) {
        this.mDescription = description;
    }

    private String calendarToString(Calendar date, boolean sameday) {
        String minute = "";
        if (date.get(Calendar.MINUTE) == 0) {
            minute = "00";
        } else {
            minute = Integer.toString(date.get(Calendar.MINUTE));
        }

        String hour = Integer.toString(date.get(Calendar.HOUR_OF_DAY)) + ":"
                + minute;
        if (sameday) {
            return hour;
        } else {
            String year = Integer.toString(date.get(Calendar.YEAR));
            String month = date.getDisplayName(Calendar.MONTH, Calendar.SHORT,
                    Locale.ENGLISH);
            String day = date.getDisplayName(Calendar.DAY_OF_WEEK,
                    Calendar.SHORT, Locale.ENGLISH);

            return day + " " + date.get(Calendar.DAY_OF_MONTH) + " " + month
                    + " " + year + "\n " + hour;
        }
    }

    public String toString() {

        if (mType == PeriodType.DEFAULT) {
            return calendarToString(mStart, false) + "-"
                    + calendarToString(mEnd, true) + "   " + mEventName;
        } else {

            return calendarToString(mStart, false) + "-"
                    + calendarToString(mEnd, true) + "   " + mEventName + ": "
                    + mType;
        }
    }
}
