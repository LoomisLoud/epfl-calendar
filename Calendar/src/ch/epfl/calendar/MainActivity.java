package ch.epfl.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ch.epfl.calendar.display.AddEventActivity;
import ch.epfl.calendar.display.CoursesListActivity;
import ch.epfl.calendar.display.WeekView;
import ch.epfl.calendar.display.WeekViewEvent;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author lweingart
 * 
 */
public class MainActivity extends Activity implements
        WeekView.MonthChangeListener, WeekView.EventClickListener,
        WeekView.EventLongPressListener {

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // *************************************************

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide
        // the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // *************************************************

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        final ArrayList<String> spinnerList = new ArrayList<String>();
        spinnerList.add("Day");
        spinnerList.add("3 Days");
        spinnerList.add("Week");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerList) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view
                        .findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                return view;
            }

            public View getDropDownView(int position, View convertView,
                    ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view
                        .findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                return view;
            }
        };

        ActionBar.OnNavigationListener mOnNavigationListener = new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int position, long itemId) {
                if (position == 0) {
                    if (mWeekViewType != TYPE_DAY_VIEW) {
                        mWeekViewType = TYPE_DAY_VIEW;
                        mWeekView.setNumberOfVisibleDays(1);

                        // Lets change some dimensions to best fit the view.
                        mWeekView.setColumnGap((int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
                                        .getDisplayMetrics()));
                        mWeekView.setTextSize((int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_SP, 12, getResources()
                                        .getDisplayMetrics()));
                        mWeekView.setEventTextSize((int) TypedValue
                                .applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
                                        getResources().getDisplayMetrics()));
                    }
                    return true;
                } else if (position == 1) {
                    if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                        mWeekViewType = TYPE_THREE_DAY_VIEW;
                        mWeekView.setNumberOfVisibleDays(3);

                        // Lets change some dimensions to best fit the view.
                        mWeekView.setColumnGap((int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
                                        .getDisplayMetrics()));
                        mWeekView.setTextSize((int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_SP, 12, getResources()
                                        .getDisplayMetrics()));
                        mWeekView.setEventTextSize((int) TypedValue
                                .applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
                                        getResources().getDisplayMetrics()));
                    }
                    return true;
                } else if (position == 2) {
                    if (mWeekViewType != TYPE_WEEK_VIEW) {
                        mWeekViewType = TYPE_WEEK_VIEW;
                        mWeekView.setNumberOfVisibleDays(7);

                        // Lets change some dimensions to best fit the view.
                        mWeekView.setColumnGap((int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 2, getResources()
                                        .getDisplayMetrics()));
                        mWeekView.setTextSize((int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_SP, 7, getResources()
                                        .getDisplayMetrics()));
                        mWeekView.setEventTextSize((int) TypedValue
                                .applyDimension(TypedValue.COMPLEX_UNIT_SP, 7,
                                        getResources().getDisplayMetrics()));
                    }
                    return true;
                } else {
                    return false;
                }
            }
        };

        actionBar.setListNavigationCallbacks(arrayAdapter,
                mOnNavigationListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void switchToCoursesList() {
        Intent coursesListActivityIntent = new Intent(this,
                CoursesListActivity.class);
        startActivity(coursesListActivityIntent);
    }

    public void switchToAddEventsActivity() {
        Intent AddEventsActivityIntent = new Intent(this,
                AddEventActivity.class);
        startActivity(AddEventsActivityIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_courses_list:
            switchToCoursesList();
            return true;
        case R.id.action_settings:
            Toast.makeText(this, "Not yet implemented", Toast.LENGTH_SHORT)
                    .show();
            return true;
        case R.id.add_event:
            switchToAddEventsActivity();
            return true;
        case R.id.action_today:
            mWeekView.goToToday();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    // *****************************************************

    public Calendar createCalendar(int year, int month, int day, int hour,
            int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.YEAR, year);

        return calendar;
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        // Populate the week view with some events.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
            Calendar startTime = Calendar.getInstance();

            startTime.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            startTime.set(Calendar.HOUR_OF_DAY, 8);
            startTime.set(Calendar.MINUTE, 15);
            startTime.set(Calendar.MONTH, newMonth - 1);
            startTime.set(Calendar.YEAR, newYear);
            Calendar endTime = (Calendar) startTime.clone();
            endTime.set(Calendar.HOUR_OF_DAY, 10);
            endTime.set(Calendar.MINUTE, 0);
            endTime.set(Calendar.MONTH, newMonth - 1);
            WeekViewEvent event = new WeekViewEvent(1, "Analysis", startTime,
                    endTime);
            event.setColor(getResources().getColor(R.color.event_color_01));
            events.add(event);

            startTime = Calendar.getInstance();
            startTime.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            startTime.set(Calendar.HOUR_OF_DAY, 14);
            startTime.set(Calendar.MINUTE, 15);
            startTime.set(Calendar.MONTH, newMonth - 1);
            startTime.set(Calendar.YEAR, newYear);
            endTime = (Calendar) startTime.clone();
            endTime.set(Calendar.HOUR_OF_DAY, 16);
            endTime.set(Calendar.MINUTE, 0);
            endTime.set(Calendar.MONTH, newMonth - 1);
            event = new WeekViewEvent(10, "Algorithm", startTime, endTime);
            event.setColor(getResources().getColor(R.color.event_color_02));
            events.add(event);

            startTime = Calendar.getInstance();
            startTime.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
            startTime.set(Calendar.HOUR_OF_DAY, 8);
            startTime.set(Calendar.MINUTE, 15);
            startTime.set(Calendar.MONTH, newMonth - 1);
            startTime.set(Calendar.YEAR, newYear);
            endTime = (Calendar) startTime.clone();
            endTime.set(Calendar.HOUR_OF_DAY, 10);
            endTime.set(Calendar.MINUTE, 0);

            event = new WeekViewEvent(10, "Sweng", startTime, endTime);
            event.setColor(getResources().getColor(R.color.event_color_03));
            events.add(event);

            startTime = Calendar.getInstance();
            startTime.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
            startTime.set(Calendar.HOUR_OF_DAY, 10);
            startTime.set(Calendar.MINUTE, 15);
            startTime.set(Calendar.MONTH, newMonth - 1);
            startTime.set(Calendar.YEAR, newYear);
            endTime = (Calendar) startTime.clone();
            endTime.set(Calendar.HOUR_OF_DAY, 13);
            endTime.set(Calendar.MINUTE, 0);
            endTime.set(Calendar.MONTH, newMonth - 1);
            event = new WeekViewEvent(2, "Physics I", startTime, endTime);
            event.setColor(getResources().getColor(R.color.event_color_02));
            events.add(event);

            startTime = Calendar.getInstance();
            startTime.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
            startTime.set(Calendar.HOUR_OF_DAY, 13);
            startTime.set(Calendar.MINUTE, 15);
            startTime.set(Calendar.MONTH, newMonth - 1);
            startTime.set(Calendar.YEAR, newYear);
            endTime = (Calendar) startTime.clone();
            endTime.set(Calendar.HOUR_OF_DAY, 15);
            endTime.set(Calendar.MINUTE, 0);
            event = new WeekViewEvent(3, "SHS", startTime, endTime);
            event.setColor(getResources().getColor(R.color.event_color_03));
            events.add(event);

            startTime = Calendar.getInstance();
            startTime.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
            startTime.set(Calendar.HOUR_OF_DAY, 10);
            startTime.set(Calendar.MINUTE, 15);
            startTime.set(Calendar.MONTH, newMonth - 1);
            startTime.set(Calendar.YEAR, newYear);
            endTime = (Calendar) startTime.clone();
            endTime.set(Calendar.HOUR_OF_DAY, 12);
            endTime.set(Calendar.MINUTE, 0);
            event = new WeekViewEvent(4, "Securité des réseaux", startTime,
                    endTime);
            event.setColor(getResources().getColor(R.color.event_color_04));
            events.add(event);

            startTime = Calendar.getInstance();
            startTime.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
            startTime.set(Calendar.HOUR_OF_DAY, 11);
            startTime.set(Calendar.MINUTE, 15);
            startTime.set(Calendar.MONTH, newMonth - 1);
            startTime.set(Calendar.YEAR, newYear);
            endTime = (Calendar) startTime.clone();
            endTime.set(Calendar.HOUR_OF_DAY, 13);
            endTime.set(Calendar.MINUTE, 0);
            event = new WeekViewEvent(5, "Physics II", startTime, endTime);
            event.setColor(getResources().getColor(R.color.event_color_01));
            events.add(event);

            startTime = Calendar.getInstance();
            startTime.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
            startTime.set(Calendar.HOUR_OF_DAY, 13);
            startTime.set(Calendar.MINUTE, 15);
            startTime.set(Calendar.MONTH, newMonth - 1);
            startTime.set(Calendar.YEAR, newYear);
            endTime = (Calendar) startTime.clone();
            endTime.set(Calendar.HOUR_OF_DAY, 15);
            endTime.set(Calendar.MINUTE, 0);
            event = new WeekViewEvent(5, "Algorithm", startTime, endTime);
            event.setColor(getResources().getColor(R.color.event_color_02));
            events.add(event);
            
        return events;
    }

    private String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d",
                time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE),
                time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(MainActivity.this, "Clicked " + event.getName(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(MainActivity.this,
                "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT)
                .show();
    }
}
