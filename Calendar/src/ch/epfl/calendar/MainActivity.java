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
import ch.epfl.calendar.apiInterface.CalendarClientInterface;
import ch.epfl.calendar.apiInterface.CalendarClientDownloadInterface;
import ch.epfl.calendar.authentication.AuthenticationActivity;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.data.PeriodType;
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
        WeekView.EventLongPressListener, CalendarClientDownloadInterface {

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
    private List<WeekViewEvent> mMListEvents = new ArrayList<WeekViewEvent>();
    private long mIdEvent = 0;
    private List<Course> mListCourses = new ArrayList<Course>();
    private ProgressDialog mDialog;

    private Activity mThisActivity;

    public static final String TAG = "MainActivity::";
    public static final int AUTH_ACTIVITY_CODE = 1;
    public static final int ADD_EVENT_ACTIVITY_CODE = 2;

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

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            // System.out.println("Loading courses in savedInstanceState");
            mListCourses = savedInstanceState
                    .getParcelableArrayList("listCourses");
        } else {
            // Retrieve course for first time
            // System.out.println("Retrieving courses for first time");
            // TODO : At the beginning of the application, we "logout" the user
            // TequilaAuthenticationAPI.getInstance().clearStoredData(mThisActivity);

            if (!GlobalPreferences.isAuthenticated(mThisActivity)) {
                switchToAuthenticationActivity();
            } else {
                mListCourses = new ArrayList<Course>();
                populateCalendar();

            }

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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the activity state
        savedInstanceState.putParcelableArrayList("listCourses",
                new ArrayList<Course>(mListCourses));
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
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
        startActivityForResult(addEventsActivityIntent, ADD_EVENT_ACTIVITY_CODE);
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
            // populateCalendar();
            // mWeekView.notifyDatasetChanged();
            return true;
        case R.id.action_logout:
            logout();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    public Calendar createCalendar(int year, int month, int day, int hour,
            int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);

        return calendar;
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        // Populate the week view with some events.

        for (Course c : mListCourses) {
            for (Period p : c.getPeriods()) {
                mMListEvents.add(new WeekViewEvent(mIdEvent,
                        getEventTitle(c, p), p.getStartDate(), p.getEndDate(),
                        p.getType()));

            }
            mIdEvent++;
        }
        return mMListEvents;
    }

    private String getEventTitle(Course c, Period p) {
        String result = c.getName() + "\n";
        int i = p.getRooms().size();
        for (String r : p.getRooms()) {
            if (i > 1) {
                result += r + ",";
            } else {
                result += r;
            }
            i--;
        }
        return result + "\n" + p.getType();
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        String cours = event.getName().split("\n")[0];
        switchToCourseDetails(cours);
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
            mListCourses = new ArrayList<Course>();
            populateCalendar();
        }

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_EVENT_ACTIVITY_CODE && resultCode == RESULT_OK) {
            String name = data.getExtras().get("nameInfo").toString();
            int startYear = data.getExtras().getInt("startYear");
            int startMonth = data.getExtras().getInt("startMonth");
            int startDay = data.getExtras().getInt("startDay");
            int startHour = data.getExtras().getInt("startHour");
            int startMinute = data.getExtras().getInt("startMinute");

            int endYear = data.getExtras().getInt("endYear");
            int endMonth = data.getExtras().getInt("endMonth");
            int endDay = data.getExtras().getInt("endDay");
            int endHour = data.getExtras().getInt("endHour");
            int endMinute = data.getExtras().getInt("endMinute");

            Calendar start = createCalendar(startYear, startMonth, startDay,
                    startHour, startMinute);
            Calendar end = createCalendar(endYear, endMonth, endDay, endHour,
                    endMinute);
            System.out.println(start.toString());
            mMListEvents.removeAll(mMListEvents);

            mMListEvents.add(new WeekViewEvent(mIdEvent++, name, start, end,
                    PeriodType.DEFAULT));

            mWeekView.notifyDatasetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    protected void populateCalendar() {
        CalendarClientInterface cal = new CalendarClient(mThisActivity, this);
        cal.getISAInformations();
    }

    private void logout() {
        TequilaAuthenticationAPI.getInstance().clearStoredData(mThisActivity);
        switchToAuthenticationActivity();
    }

    @Override
    public void callbackDownload(List<Course> courses) {
        mListCourses = courses;
        mWeekView.notifyDatasetChanged();
    }

}
