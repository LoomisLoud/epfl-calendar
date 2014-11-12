package ch.epfl.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import ch.epfl.calendar.apiInterface.CalendarClient;
import ch.epfl.calendar.apiInterface.CalendarClientException;
import ch.epfl.calendar.apiInterface.CalendarClientInterface;
import ch.epfl.calendar.authentication.AuthenticationActivity;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.display.AddEventActivity;
import ch.epfl.calendar.display.CourseDetailsActivity;
import ch.epfl.calendar.display.CoursesListActivity;
import ch.epfl.calendar.thirdParty.calendarViews.WeekView;
import ch.epfl.calendar.thirdParty.calendarViews.WeekViewEvent;
import ch.epfl.calendar.utils.GlobalPreferences;

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

    private static final int SIZE_COLUMN_GAP_DAY = 8;
    private static final int SIZE_FRONT_DAY = 12;
    private static final int SIZE_FRONT_EVENT_DAY = 12;
    private static final int SIZE_COLUMN_GAP_3DAYS = 8;
    private static final int SIZE_FRONT_3DAYS = 12;
    private static final int SIZE_FRONT_EVENT_3DAYS = 12;
    private static final int SIZE_COLUMN_GAP_WEEK = 2;
    private static final int SIZE_FRONT_WEEK = 7;
    private static final int SIZE_FRONT_EVENT_WEEK = 7;

    private static final int NUMBER_VISIBLE_DAYS_DAY = 1;
    private static final int NUMBER_VISIBLE_DAYS_3DAYS = 3;
    private static final int NUMBER_VISIBLE_DAYS_WEEK = 7;

    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;
    private List<Course> listCourses = null;
    private ProgressDialog mDialog;

    public static final String TAG = "MainActivity::";
    public static final int AUTH_ACTIVITY_CODE = 1;

    private Activity mThisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mThisActivity = this;

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

        actionBarMainActivity();

        // TODO : At the beginning of the application, we "logout" the user
        TequilaAuthenticationAPI.getInstance().clearSessionID(mThisActivity);

        if (!GlobalPreferences.isAuthenticated(mThisActivity)) {
            switchToAuthenticationActivity();
        } else {
            listCourses = populateCalendar();
        }
    }

    private ArrayList<String> spinnerList() {
        ArrayList<String> spinnerList = new ArrayList<String>();
        spinnerList.add("Day");
        spinnerList.add("3 Days");
        spinnerList.add("Week");
        return spinnerList;
    }

    private void actionBarMainActivity() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        ArrayAdapter<String> arrayAdapter = new MySpinnerAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerList());

        ActionBar.OnNavigationListener mOnNavigationListener = new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int position, long itemId) {
                if (position == 0) {
                    changeCalendarView(TYPE_DAY_VIEW, NUMBER_VISIBLE_DAYS_DAY,
                            SIZE_COLUMN_GAP_DAY, SIZE_FRONT_DAY,
                            SIZE_FRONT_EVENT_DAY);
                    return true;
                } else if (position == 1) {
                    changeCalendarView(TYPE_THREE_DAY_VIEW,
                            NUMBER_VISIBLE_DAYS_3DAYS, SIZE_COLUMN_GAP_3DAYS,
                            SIZE_FRONT_3DAYS, SIZE_FRONT_EVENT_3DAYS);
                    return true;
                } else if (position == 2) {
                    changeCalendarView(TYPE_WEEK_VIEW,
                            NUMBER_VISIBLE_DAYS_WEEK, SIZE_COLUMN_GAP_WEEK,
                            SIZE_FRONT_WEEK, SIZE_FRONT_EVENT_WEEK);
                    return true;
                } else {
                    return false;
                }
            }
        };
        
        actionBar.setListNavigationCallbacks(arrayAdapter,
                mOnNavigationListener);
    }

    private void changeCalendarView(int typeView, int numberVisibleDays,
            int sizeColumnGap, int sizeFront, int sizeFrontEvent) {
        if (mWeekViewType != typeView) {
            mWeekViewType = typeView;
            mWeekView.setNumberOfVisibleDays(numberVisibleDays);
            // Lets change some dimensions to best fit the view.
            mWeekView.setColumnGap((int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, sizeColumnGap, getResources()
                            .getDisplayMetrics()));
            mWeekView.setTextSize((int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP, sizeFront, getResources()
                            .getDisplayMetrics()));
            mWeekView.setEventTextSize((int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP, sizeFrontEvent, getResources()
                            .getDisplayMetrics()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void switchToCoursesList() {
        Intent coursesListActivityIntent = new Intent(this,
                CoursesListActivity.class);
        startActivity(coursesListActivityIntent);
    }

    private void switchToCourseDetails(String courseName) {
        Intent courseDetailsActivityIntent = new Intent(this,
                CourseDetailsActivity.class);

        courseDetailsActivityIntent.putExtra("course", courseName);
        startActivity(courseDetailsActivityIntent);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Charging course details");
        mDialog.show();
    }

    private void switchToAddEventsActivity() {
        Intent addEventsActivityIntent = new Intent(this,
                AddEventActivity.class);
        startActivity(addEventsActivityIntent);
    }

    private void switchToAuthenticationActivity() {
        Intent displayAuthenticationActivtyIntent = new Intent(mThisActivity,
                AuthenticationActivity.class);
        mThisActivity.startActivityForResult(
                displayAuthenticationActivtyIntent, AUTH_ACTIVITY_CODE);
    }

    private void switchToCreditsActivity() {
        Intent i = new Intent(mThisActivity, CreditsActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_courses_list:
                switchToCoursesList();
                return true;
            case R.id.action_settings:
                switchToCreditsActivity();
                return true;
            case R.id.add_event:
                switchToAddEventsActivity();
                return true;
            case R.id.action_today:
                mWeekView.goToToday();
                return true;
            case R.id.action_update_activity:
                populateCalendar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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

        int idEvent = 0;
        for (Course c : listCourses) {

            for (Period p : c.getPeriods()) {
                events.add(new WeekViewEvent(idEvent, c.getName(), p
                        .getStartDate(), p.getEndDate()));
            }
            idEvent++;
        }
        return events;
    }

    /*
     * private String getEventTitle(Calendar time) { return
     * String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY),
     * time.get(Calendar.MINUTE), time.get(Calendar.MONTH) + 1,
     * time.get(Calendar.DAY_OF_MONTH)); }
     */

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        switchToCourseDetails(event.getName());
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(MainActivity.this,
                "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTH_ACTIVITY_CODE && resultCode == RESULT_OK) {
            listCourses = populateCalendar();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public List<Course> populateCalendar() {
        CalendarClientInterface cal = new CalendarClient(mThisActivity);
        List<Course> courses = new ArrayList<Course>();

        try {
            courses = cal.getISAInformations();
        } catch (CalendarClientException e) {
            // TODO catch exceptions and manage
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}
