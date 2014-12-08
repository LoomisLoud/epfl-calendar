package ch.epfl.calendar.data;

import java.util.Calendar;

import ch.epfl.calendar.App;

/**
 * @author MatthiasLeroyEPFL
 * 
 */
public class EventForList extends ListViewItem {

    private static final int ZERO_MINUTE = 0;
    private static final int TEN_MINUTE = 10;
    
    private String mName;
    private Calendar mEnd;
    private PeriodType mType;
    private int mId;
    private String mLinkedCourse;
    private String mDescription;
    
    public EventForList(String name, Calendar start, Calendar end,
            PeriodType type, int id, String linkedCourse, String description) {
        super(start);
        mName = name;
        mEnd = end;
        mType = type;
        mId = id;
        mLinkedCourse = linkedCourse;
        mDescription = description;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String name) {
        name = mName;
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

    /*private String calendarToString(Calendar date) {
        String minute = "";
        if (date.get(Calendar.MINUTE) >= ZERO_MINUTE
                && date.get(Calendar.MINUTE) < TEN_MINUTE) {
            minute = "0" + date.get(Calendar.MINUTE);
        } else {
            minute = Integer.toString(date.get(Calendar.MINUTE));
        }

        String hour = Integer.toString(date.get(Calendar.HOUR_OF_DAY)) + ":"
                + minute;

        return hour;

    }*/

    public String toString() {

        if (mType == PeriodType.DEFAULT) {
            return App.calendarTo12HoursString(super.getmStart()) + "-"
                    + App.calendarTo12HoursString(mEnd) + "   " + mName;
        } else {

            return App.calendarTo12HoursString(super.getmStart()) + "-"
                    + App.calendarTo12HoursString(mEnd) + "   " + mName + ": " + mType;
        }
    }
}
