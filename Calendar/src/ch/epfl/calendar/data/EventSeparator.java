package ch.epfl.calendar.data;

import java.util.Calendar;
import java.util.Locale;

/**
 * @author MatthiasLeroyEPFL
 *
 */
public class EventSeparator extends ListViewItem {

    public EventSeparator(Calendar date) {
        super(date);

    }

    public String toString() {
        String day = super.getmStart().getDisplayName(Calendar.DAY_OF_WEEK,
                Calendar.SHORT, Locale.ENGLISH);
        int dayOfMonth = super.getmStart().get(Calendar.DAY_OF_MONTH);
        String month =  super.getmStart().getDisplayName(Calendar.MONTH,
                Calendar.SHORT, Locale.ENGLISH);
        return day+" "+dayOfMonth+" "+month;
    }

}
