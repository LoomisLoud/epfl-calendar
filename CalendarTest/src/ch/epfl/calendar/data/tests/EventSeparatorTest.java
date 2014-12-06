package ch.epfl.calendar.data.tests;

import java.util.Calendar;
import java.util.Locale;

import junit.framework.TestCase;
import ch.epfl.calendar.data.EventSeparator;

/**
 * @author MatthiasLeroyEPFL
 *
 */
public class EventSeparatorTest extends TestCase {
    private static final int ADD_SIX_DAYS = 6;
    private static final int ADD_SEVEN_DAYS = 7;

    private EventSeparator mSeparator;
    private Calendar mDate;

    @Override
    protected void setUp() throws Exception {
        mDate = Calendar.getInstance();
        mSeparator = new EventSeparator(mDate);
        super.setUp();
    }

    public void testConstructor() {
        assertEquals(mSeparator.getmStart(), mDate);
    }

    public void testToString() {
        Calendar tomorrow = (Calendar) mDate.clone();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);

        Calendar sameWeek = (Calendar) mDate.clone();
        sameWeek.add(Calendar.DAY_OF_MONTH, ADD_SIX_DAYS);

        Calendar otherWeek = (Calendar) mDate.clone();
        otherWeek.add(Calendar.DAY_OF_MONTH, ADD_SEVEN_DAYS);

        EventSeparator tomorrowSeparator = new EventSeparator(tomorrow);
        EventSeparator sameWeekSeparator = new EventSeparator(sameWeek);
        EventSeparator otherWeekSeparator = new EventSeparator(otherWeek);

        assertEquals(mSeparator.toString(), "Today");
        assertEquals(tomorrowSeparator.toString(), "Tomorrow");
        assertEquals(sameWeekSeparator.toString(), sameWeek.getDisplayName(
                Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH));
        assertEquals(
                otherWeekSeparator.toString(),
                otherWeek.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT,
                        Locale.ENGLISH)
                        + " "
                        + otherWeek.get(Calendar.DAY_OF_MONTH)
                        + " "
                        + otherWeek.getDisplayName(Calendar.MONTH,
                                Calendar.SHORT, Locale.ENGLISH));

    }

}
