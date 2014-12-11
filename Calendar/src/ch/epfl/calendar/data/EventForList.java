package ch.epfl.calendar.data;

import java.util.Calendar;

import ch.epfl.calendar.App;

/**
 * An adaptor to have an {@link Event} easily displayable in the planning view
 * @author MatthiasLeroyEPFL
 * 
 */
public class EventForList extends ListViewItem {
    
    private String mName;
    private Calendar mEnd;
    private PeriodType mType;
    private int mId;
    private String mLinkedCourse;
    private String mDescription;
    
    /**
     * Create a new {@link EventForList}
     * @param name the name of the event
     * @param start the start time of the event
     * @param end the end time of the event
     * @param type the type of the event
     * @param id the ID of the event
     * @param linkedCourse the {@link Course} to which the event is related
     * @param description the description of the event
     */
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

    /**
     * @return the name of the event
     */
    public String getName() {
        return mName;
    }

    /**
     * Set the name of the event
     * @param name the new name of the event
     */
    public void setName(String name) {
        name = mName;
    }

    /**
     * @return the end time of the event
     */
    public Calendar getEnd() {
        return mEnd;
    }

    /**
     * Sets the end time of the event
     * @param end the new end time of the event
     */
    public void setEnd(Calendar end) {
        this.mEnd = end;
    }

    /**
     * @return the type of the event
     */
    public PeriodType getType() {
        return mType;
    }

    /**
     * Sets the type of the event
     * @param type the new {@link PeriodType} of the event
     */
    public void setType(PeriodType type) {
        this.mType = type;
    }

    /**
     * @return the ID of the event in database
     */
    public int getId() {
        return mId;
    }

    /**
     * Set the ID of the event in database
     * @param id the new ID of the event
     */
    public void setId(int id) {
        this.mId = id;
    }

    /**
     * @return the name of the course to which the event is linked.
     */
    public String getLinkedCourse() {
        return mLinkedCourse;
    }

    /**
     * Set the name of the {@link Course} to which the event is linked
     * @param linkedCourse the new course to be linked to this event
     */
    public void setLinkedCourse(String linkedCourse) {
        this.mLinkedCourse = linkedCourse;
    }

    /**
     * @return the description of the event
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * Sets the description of the event
     * @param description the new decsription of the event
     */
    public void setDescription(String description) {
        this.mDescription = description;
    }

    @Override
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
