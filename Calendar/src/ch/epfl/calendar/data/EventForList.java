package ch.epfl.calendar.data;

import java.util.Calendar;

public class EventForList {

    private String mEventName;
    private Calendar mStart;
    private Calendar mEnd;
    private PeriodType mType;
    
    public EventForList(String name,Calendar start, Calendar end, PeriodType type){
        mEventName=name;
        mStart=start;
        mEnd=end;
        mType=type;
    }
    
    public String getmEventName() {
        return mEventName;
    }

    public void setmEventName(String mEventName) {
        this.mEventName = mEventName;
    }

    public Calendar getmStart() {
        return mStart;
    }

    public void setmStart(Calendar mStart) {
        this.mStart = mStart;
    }

    public Calendar getmEnd() {
        return mEnd;
    }

    public void setmEnd(Calendar mEnd) {
        this.mEnd = mEnd;
    }

    public PeriodType getmType() {
        return mType;
    }

    public void setmType(PeriodType mType) {
        this.mType = mType;
    }
}
