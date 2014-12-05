package ch.epfl.calendar.data;

import java.util.Calendar;
import java.util.Locale;

/**
 * @author MatthiasLeroyEPFL
 * 
 */
public class EventSeparator extends ListViewItem {
    
    private static final int NUMBER_OF_DAY_IN_WEEK = 7;
    private static final int NUMBER_ONE = 1;

    public EventSeparator(Calendar date) {
        super(date);

    }

    public String toString() {
        Calendar today = Calendar.getInstance();
        int difDay = super.getmStart().get(Calendar.DAY_OF_MONTH)
                - today.get(Calendar.DAY_OF_MONTH);
        int difMonth = super.getmStart().get(Calendar.MONTH)
                - today.get(Calendar.MONTH);
        int difYear = super.getmStart().get(Calendar.YEAR)
                - today.get(Calendar.YEAR);
        if (difMonth == 0 && difYear == 0) {
            if (difDay == 0) {
                return "Today";
            } else if (difDay == 1) {
                return "Tomorrow";
            } else if (difDay > NUMBER_ONE && difDay < NUMBER_OF_DAY_IN_WEEK) {
                today.add(Calendar.DAY_OF_MONTH, difDay);
                return today.getDisplayName(Calendar.DAY_OF_WEEK,
                        Calendar.LONG, Locale.ENGLISH);
            } else {
                return date();
            }
        } else {
            return date();
        }

    }

    private String date() {

        String day = super.getmStart().getDisplayName(Calendar.DAY_OF_WEEK,
                Calendar.SHORT, Locale.ENGLISH);
        int dayOfMonth = super.getmStart().get(Calendar.DAY_OF_MONTH);
        String month = super.getmStart().getDisplayName(Calendar.MONTH,
                Calendar.SHORT, Locale.ENGLISH);
        return day + " " + dayOfMonth + " " + month;

    }

}
