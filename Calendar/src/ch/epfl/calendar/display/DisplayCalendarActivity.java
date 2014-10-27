package ch.epfl.calendar.display;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import ch.epfl.calendar.R;
import ch.epfl.calendar.data.Course;

public class DisplayCalendarActivity extends Activity {
    List<String> algorithmRoomCourse = new ArrayList<String>();

    Course algorithm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_calendar);
        algorithmRoomCourse.add("CO2");
       algorithm = new Course("Algorithm", "monday", "14h15", "16h", "Course",algorithmRoomCourse );
       
       Intent intent = new Intent(Intent.ACTION_INSERT);  
       intent.setType("vnd.android.cursor.item/event");  
       intent.putExtra(Events.TITLE, algorithm.getName());  
       intent.putExtra(Events.EVENT_LOCATION, algorithm.getPeriods().get(0).getRooms().get(0));  
       intent.putExtra(Events.DESCRIPTION, "Course");  
 
       GregorianCalendar beginTime = new GregorianCalendar(2014, 27, 10,
               algorithm.getPeriods().get(0).getHourStartTime(),algorithm.getPeriods().get(0).getMinuteStartTime());
       GregorianCalendar endTime = new GregorianCalendar(2014, 27, 10,
               algorithm.getPeriods().get(0).getHourEndTime(),algorithm.getPeriods().get(0).getMinuteEndTime());
       
       intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,  
               beginTime.getTimeInMillis());  
       intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,  
               endTime.getTimeInMillis());  
 
       intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);  
 
       intent.putExtra(Events.RRULE,  
               "FREQ=WEEKLY;COUNT=11;BYDAY=MO");  
 
       intent.putExtra(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);  
       intent.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);  
 
       startActivity(intent);  
 
    }
}
