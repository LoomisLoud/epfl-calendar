package ch.epfl.calendar.data.tests;

import java.util.Calendar;

import ch.epfl.calendar.data.EventForList;
import ch.epfl.calendar.data.PeriodType;
import junit.framework.TestCase;

public class EventForListTest extends TestCase {
    private Calendar start;
    private Calendar end;
    private EventForList event;

    @Override
    protected void setUp() throws Exception {
        start = Calendar.getInstance();
        start.set(Calendar.HOUR_OF_DAY, 11);
        start.set(Calendar.MINUTE, 0);
        end = (Calendar) start.clone();
        end.set(Calendar.HOUR_OF_DAY, 12);
        end.add(Calendar.MINUTE, 15);
        event = new EventForList("Football", start, end, PeriodType.DEFAULT, 5,
                "", "Football game");
        super.setUp();
    }

    public void testConstructor() {
        

        assertEquals(event.getmName(), "Football");
        assertEquals(start.getTimeInMillis(), event.getmStart()
                .getTimeInMillis());
        assertEquals(end.getTimeInMillis(), event.getmEnd().getTimeInMillis());
        assertEquals(PeriodType.DEFAULT, event.getmType());
        assertEquals(5, event.getmId());
        assertEquals("", event.getmLinkedCourse());
        assertEquals("Football game", event.getmDescription());
    }

   
    public void testToString(){
        assertEquals(event.toString(),"11:00-12:15   Football" );
    }

}
