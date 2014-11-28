package ch.epfl.calendar.thirdParty.calendarViews;
/**
 * Code imported from:
 * https://github.com/alamkanak/Android-Week-View/tree/master/library/src/main/java/com/alamkanak/weekview
 */
import java.util.Calendar;

import android.graphics.Color;
import ch.epfl.calendar.data.PeriodType;

/**
 * @author LoomisLoud
 *
 */
public class WeekViewEvent {
    
    private static final String COLOR_BLUE = "#33B5E5";
    //private static final String COLOR_MAGENTA = "#AA66CC";
    private static final String COLOR_GREEN = "#99CC00";
    private static final String COLOR_RED = "#FF4444";
    private static final String COLOR_ORANGE = "#FFBB33";
    
    private long mId;
    private Calendar mStartTime;
    private Calendar mEndTime;
    private String mName;
    private String mDescription;
    private PeriodType mType;
    private int mColor;

    public WeekViewEvent() {

    }

    /**
     * Initializes the event for week view.
     * 
     * @param name
     *            Name of the event.
     * @param startYear
     *            Year when the event starts.
     * @param startMonth
     *            Month when the event starts.
     * @param startDay
     *            Day when the event starts.
     * @param startHour
     *            Hour (in 24-hour format) when the event starts.
     * @param startMinute
     *            Minute when the event starts.
     * @param endYear
     *            Year when the event ends.
     * @param endMonth
     *            Month when the event ends.
     * @param endDay
     *            Day when the event ends.
     * @param endHour
     *            Hour (in 24-hour format) when the event ends.
     * @param endMinute
     *            Minute when the event ends.
     */
    public WeekViewEvent(long id, String name, int startYear, int startMonth,
            int startDay, int startHour, int startMinute, int endYear,
            int endMonth, int endDay, int endHour, int endMinute) {
        this.mId = id;

        this.mStartTime = Calendar.getInstance();
        this.mStartTime.set(Calendar.YEAR, startYear);
        this.mStartTime.set(Calendar.MONTH, startMonth - 1);
        this.mStartTime.set(Calendar.DAY_OF_MONTH, startDay);
        this.mStartTime.set(Calendar.HOUR_OF_DAY, startHour);
        this.mStartTime.set(Calendar.MINUTE, startMinute);

        this.mEndTime = Calendar.getInstance();
        this.mEndTime.set(Calendar.YEAR, endYear);
        this.mEndTime.set(Calendar.MONTH, endMonth - 1);
        this.mEndTime.set(Calendar.DAY_OF_MONTH, endDay);
        this.mEndTime.set(Calendar.HOUR_OF_DAY, endHour);
        this.mEndTime.set(Calendar.MINUTE, endMinute);

        this.mName = name;
    }

    /**
     * Initializes the event for week view.
     * 
     * @param name
     *            Name of the event.
     * @param startTime
     *            The time when the event starts.
     * @param endTime
     *            The time when the event ends.
     */
    public WeekViewEvent(long id, String name, Calendar startTime,
            Calendar endTime, PeriodType type, String description) {
        this.mId = id;
        this.mName = name;
        this.mStartTime = startTime;
        this.mEndTime = endTime;
        this.mType = type;
        this.mDescription=description;
        setColorWithType(type);

    }

    private void setColorWithType(PeriodType type) {
        if (type == PeriodType.LECTURE) {
            mColor = Color.parseColor(COLOR_BLUE);
        } else if (type == PeriodType.EXERCISES) {
            mColor = Color.parseColor(COLOR_GREEN);
        } else if (type == PeriodType.PROJECT) {
            mColor = Color.parseColor(COLOR_RED);
        } else {
            mColor = Color.parseColor(COLOR_ORANGE);
        }
    }

    public Calendar getStartTime() {
        return mStartTime;
    }

    public void setStartTime(Calendar startTime) {
        this.mStartTime = startTime;
    }

    public Calendar getEndTime() {
        return mEndTime;
    }

    public void setEndTime(Calendar endTime) {
        this.mEndTime = endTime;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }
    
    public PeriodType getmType() {
        return mType;
    }

    public void setmType(PeriodType mType) {
        this.mType = mType;
    }
    
    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }
}
