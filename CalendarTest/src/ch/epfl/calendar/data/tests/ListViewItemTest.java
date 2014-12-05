package ch.epfl.calendar.data.tests;

import java.util.Calendar;
import ch.epfl.calendar.data.ListViewItem;
import junit.framework.TestCase;

/**
 * @author MatthiasLeroyEPFL
 *
 */
public class ListViewItemTest extends TestCase {

    private static final int ID = -2;
    private static final int DAY = 7;
    private static final int MONTH = 4;
    private static final int HOUR = 15;
    
    private Calendar mDate;
    private ListViewItem mItem;

    @Override
    protected void setUp() throws Exception {
        mDate = Calendar.getInstance();
        mItem = new ListViewItem(mDate);
        super.setUp();
    }

    public void testConstructor() {
        assertEquals(mItem.getmStart(), mDate);
    }

    public void testGetter() {

        assertEquals(mItem.getmDescription(), "");
        assertEquals(mItem.getmId(), ID);
        assertEquals(mItem.getmLinkedCourse(), "");
        assertEquals(mItem.getmName(), "");
        assertEquals(mItem.getmEnd(), null);
        assertEquals(mItem.getmType(), null);
    }

    public void testSetter() {
        Calendar otherDate = (Calendar) mDate.clone();
        otherDate.set(Calendar.DAY_OF_MONTH, DAY);
        otherDate.set(Calendar.MONTH, MONTH);
        otherDate.set(Calendar.HOUR_OF_DAY, HOUR);

        mItem.setmDate(otherDate);
        assertEquals(mItem.getmStart(), otherDate);
    }

}