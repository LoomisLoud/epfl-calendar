package ch.epfl.calendar.display;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ch.epfl.calendar.R;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.EventForList;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.data.PeriodType;
import ch.epfl.calendar.persistence.DBQuester;

public class EventListActivity extends Activity {

    private ListView mListView;
    private DBQuester mDbQuester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        mListView = (ListView) findViewById(R.id.list_event_view);

        mDbQuester = new DBQuester();
        List<Course> course = mDbQuester.getAllCourses();
        List<Event> eventCreated = mDbQuester.getAllEvents();
        List<EventForList> event = new ArrayList<EventForList>();
        for (Course c : course) {
            for (Period p : c.getPeriods()) {
                event.add(new EventForList(c.getName(), p.getStartDate(), p
                        .getEndDate(), p.getType()));
            }
        }

        for (Event e : eventCreated) {
            event.add(new EventForList(e.getName(), e.getStartDate(), e
                    .getEndDate(), stringToPeriodType(e.getType())));
        }
        sort(event);
       event= removePastEvents(event);

        String eventTab[] = new String[event.size()];

        for (int i = 0; i < event.size(); i++) {
            eventTab[i] = createString(event, i);

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, eventTab);

        mListView.setAdapter(adapter);
        mListView.setDividerHeight(10);

    }

    private String calendarToString(Calendar date, boolean sameday) {
        String minute = "";
        if (date.get(Calendar.MINUTE) == 0) {
            minute = "00";
        } else {
            minute = Integer.toString(date.get(Calendar.MINUTE));
        }

        String hour = Integer.toString(date.get(Calendar.HOUR_OF_DAY)) + ":"
                + minute;
        if (sameday) {
            return hour;
        } else {
            String year = Integer.toString(date.get(Calendar.YEAR));
            String month = date.getDisplayName(Calendar.MONTH, Calendar.SHORT,
                    Locale.ENGLISH);
            String day = date.getDisplayName(Calendar.DAY_OF_WEEK,
                    Calendar.SHORT, Locale.ENGLISH);

            return day + " " + date.get(Calendar.DAY_OF_MONTH) + " " + month
                    + " " + year + "\n " + hour;
        }
    }

    private void sort(List<EventForList> list) {
        Collections.sort(list, new Comparator<EventForList>() {

            @Override
            public int compare(EventForList e1, EventForList e2) {
                long l1 = e1.getmStart().getTimeInMillis();
                long l2 = e2.getmStart().getTimeInMillis();
                int comparator = l1 > l2 ? 1 : (l1 < l2 ? -1 : 0);
                if (comparator == 0) {
                    l1 = e1.getmEnd().getTimeInMillis();
                    l2 = e2.getmEnd().getTimeInMillis();
                    comparator = l1 > l2 ? 1 : (l1 < l2 ? -1 : 0);
                }
                return comparator;
            }
        });
    }

    private String createString(List<EventForList> event, int indexEvent) {
        return calendarToString(event.get(indexEvent).getmStart(), false) + "-"
                + calendarToString(event.get(indexEvent).getmEnd(), true)
                + "   " + event.get(indexEvent).getmEventName() + ": "
                + event.get(indexEvent).getmType();
    }

    private PeriodType stringToPeriodType(String type) {
        if (type.equalsIgnoreCase("exercices")
                || type.equalsIgnoreCase("exercises")) {
            return PeriodType.EXERCISES;
        } else if (type.equalsIgnoreCase("cours")
                || type.equalsIgnoreCase("lecture")) {
            return PeriodType.LECTURE;
        } else if (type.equalsIgnoreCase("projet")
                || type.equalsIgnoreCase("project")) {
            return PeriodType.PROJECT;
        } else {
            return PeriodType.DEFAULT;
        }
    }

    private List<EventForList> removePastEvents(List<EventForList> list) {
       List<EventForList> result = new ArrayList<EventForList>();
        Calendar today = Calendar.getInstance();
        for (int i=0; i<list.size();i++) {
            if (list.get(i).getmStart().getTimeInMillis() > today.getTimeInMillis()) {
                result.add(list.get(i));
                
            }
        }
        return result;
    }
}
