package ch.epfl.calendar.test.data;

import java.util.Calendar;

import junit.framework.TestCase;
import ch.epfl.calendar.data.EventForList;
import ch.epfl.calendar.data.PeriodType;

/**
 * @author MatthiasLeroyEPFL
 * 
 */
public class EventForListTest extends TestCase {
    private static final int ID = 5;
    private static final int END_HOUR = 12;
    private static final int START_HOUR = 11;
    private static final int END_MINUTE = 15;
    private static final int START_MINUTE = 0;

    private Calendar start;
    private Calendar end;
    private EventForList event;

    @Override
    protected void setUp() throws Exception {
        start = Calendar.getInstance();
        start.set(Calendar.HOUR_OF_DAY, START_HOUR);
        start.set(Calendar.MINUTE, START_MINUTE);
        end = (Calendar) start.clone();
        end.set(Calendar.HOUR_OF_DAY, END_HOUR);
        end.add(Calendar.MINUTE, END_MINUTE);
        event = new EventForList("Football", start, end, PeriodType.DEFAULT,
                ID, "", "Football game");
        super.setUp();
    }

    public void testConstructor() {

        assertEquals(event.getName(), "Football");
        assertEquals(start.getTimeInMillis(), event.getmStart()
                .getTimeInMillis());
        assertEquals(end.getTimeInMillis(), event.getEnd().getTimeInMillis());
        assertEquals(PeriodType.DEFAULT, event.getType());
        assertEquals(ID, event.getId());
        assertEquals("", event.getLinkedCourse());
        assertEquals("Football game", event.getDescription());
    }

    public void testToString() {
        assertEquals(event.toString(), "11:00 AM-12:15 PM   Football");
    }

}
