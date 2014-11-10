package ch.epfl.calendar.display;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;
import ch.epfl.calendar.R;

/**
 * This Activity shows the calendar, the principal feature of our app
 * @author AblionGE
 *
 */
public class DisplayCalendarActivity extends Activity {
    private CalendarView mCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_calendar);
        initializeCalendar();
    }

    /**
     *  Initializes the Calendar by setting colors and default components
     *
     */
    public void initializeCalendar() {
        mCalendar = (CalendarView) findViewById(R.id.calendar);

        // sets whether to show the week number.
        mCalendar.setShowWeekNumber(false);

        // sets the first day of week according to Calendar.
        // here we set Monday as the first day of the Calendar
        mCalendar.setFirstDayOfWeek(2);

        //The background color for the selected week.
        mCalendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));
        
        //sets the color for the dates of an unfocused month. 
        mCalendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));
    
        //sets the color for the separator line between weeks.
        mCalendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));
        
        //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
        mCalendar.setSelectedDateVerticalBar(R.color.darkgreen);
        
        //sets the listener to be notified upon selected date change.
        mCalendar.setOnDateChangeListener(new OnDateChangeListener() {
                       //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Toast.makeText(getApplicationContext(), day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
            }
        });
    }
}
