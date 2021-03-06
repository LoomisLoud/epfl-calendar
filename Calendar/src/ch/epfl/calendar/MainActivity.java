package ch.epfl.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import ch.epfl.calendar.apiInterface.UpdateDataFromDBInterface;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.data.PeriodType;
import ch.epfl.calendar.display.CourseDetailsActivity;
import ch.epfl.calendar.thirdParty.calendarViews.WeekView;
import ch.epfl.calendar.thirdParty.calendarViews.WeekViewEvent;

/**
 * The Main activity (calendar view) of the application.
 * 
 * @author lweingart
 * 
 */
public class MainActivity extends DefaultActionBarActivity implements
        WeekView.MonthChangeListener, WeekView.EventClickListener,
        WeekView.EventLongPressListener, UpdateDataFromDBInterface {

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private static final int HOUR_23 = 23;
    private static final int MINUTE_59 = 59;
    private static final int NB_DAY_IN_ONE_MONTH = 31;

    private static final int SIZE_COLUMN_GAP_DAY = 8;
    private static final int SIZE_FRONT_DAY = 12;
    private static final int SIZE_FRONT_EVENT_DAY = 12;
    private static final int SIZE_COLUMN_GAP_3DAYS = 8;
    private static final int SIZE_FRONT_3DAYS = 12;
    private static final int SIZE_FRONT_EVENT_3DAYS = 10;
    private static final int SIZE_COLUMN_GAP_WEEK = 2;
    private static final int SIZE_FRONT_WEEK = 7;
    private static final int SIZE_FRONT_EVENT_WEEK = 7;

    private static final int NUMBER_VISIBLE_DAYS_DAY = 1;
    private static final int NUMBER_VISIBLE_DAYS_3DAYS = 3;
    private static final int NUMBER_VISIBLE_DAYS_WEEK = 7;

    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;
    private List<WeekViewEvent> mListEvents = new ArrayList<WeekViewEvent>();
    private long mIdEvent = 0;
    private List<Course> mListCourses = new ArrayList<Course>();
    private List<Event> mListEventWithoutCourse = new ArrayList<Event>();
    private ProgressDialog mDialog;

    /**
     * The name of activity for the LOGs.
     */
    public static final String TAG = "MainActivity::";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        setContentView(R.layout.activity_main);
        super.setUdpateData(this);

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

        if (getAuthUtils().isAuthenticated(getApplicationContext())) {
            App.setCurrentUsername(TequilaAuthenticationAPI.getInstance()
                    .getUsername(this));
            App.setDBHelper(App.DATABASE_NAME + "_" + App.getCurrentUsername());
            // this.deleteDatabase(App.getDBHelper().getDatabaseName());
            updateListsFromDB();
        } else {
            mListCourses = new ArrayList<Course>();
            if (getNbOfAsyncTaskDB() <= 0) {
                populateCalendarFromISA();
            }
        }

        mWeekView.notifyDatasetChanged();
        activateRotation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean retour = super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem goToCalendarItem = (MenuItem) menu
                .findItem(R.id.action_calendar);
        goToCalendarItem.setVisible(false);
        this.invalidateOptionsMenu();

        return retour;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_today:
                mWeekView.goToToday();
                mWeekView.goToEight();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public List<WeekViewEvent> onMonthChange() {

        // Populate the week view with some events.
        mListEvents = new ArrayList<WeekViewEvent>();

        for (Course c : mListCourses) {
            for (Period p : c.getPeriods()) {
                mListEvents.add(new WeekViewEvent(mIdEvent++, c.getName(), p
                        .getStartDate(), p.getEndDate(), p.getType(), c
                        .getDescription()));
            }
            for (Event event : c.getEvents()) {
                addEvent(event);
            }

        }
        for (Event event : mListEventWithoutCourse) {
            addEvent(event);
        }

        return mListEvents;
    }

    @Override
    public void onEventClick(WeekViewEvent weekEvent, RectF eventRect) {
        if (weekEvent.getmType().equals(PeriodType.LECTURE)
                || weekEvent.getmType().equals(PeriodType.PROJECT)
                || weekEvent.getmType().equals(PeriodType.EXERCISES)) {
            String cours = weekEvent.getName();
            switchToCourseDetails(cours);
        } else {
            Event event = getDBQuester().getEvent(weekEvent.getId());
            switchToEditActivity(event);
        }
    }

    @Override
    public void onEventLongPress(final WeekViewEvent event, RectF eventRect) {
        if (event.getmType() == PeriodType.EXERCISES
                || event.getmType() == PeriodType.LECTURE
                || event.getmType() == PeriodType.PROJECT) {
            String cours = event.getName();
            switchToCourseDetails(cours);

        } else {

            AlertDialog.Builder choiceDialog = new AlertDialog.Builder(this);
            choiceDialog.setTitle("Action on Event");
            long id = event.getId();
            final Event eventFromDB = getDBQuester().getEvent(id);
            choiceDialog.setItems(R.array.choice_on_event,
                    new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    switchToEditActivity(eventFromDB);
                                    dialog.cancel();
                                    break;
                                case 1:
                                    if (eventFromDB.isAutomaticAddedBlock()) {
                                        getDBQuester().deleteBlock(eventFromDB);
                                    } else {
                                        getDBQuester().deleteEvent(eventFromDB);
                                    }
                                    updateFromDatabase();
                                    dialog.cancel();
                                    break;
                                case 2:
                                    if (event.getmType().equals(
                                            PeriodType.LECTURE)
                                            || event.getmType().equals(
                                                    PeriodType.PROJECT)
                                            || event.getmType().equals(
                                                    PeriodType.EXERCISES)) {
                                        String cours = event.getName();
                                        switchToCourseDetails(cours);
                                    } else {

                                        if (eventFromDB.getLinkedCourse()
                                                .equals(App.NO_COURSE)) {
                                            String description = event
                                                    .getmDescription();
                                            switchToEventDetail(
                                                    event.getName(),
                                                    description);
                                        } else {
                                            String coursName = eventFromDB
                                                    .getLinkedCourse();
                                            switchToCourseDetails(coursName);
                                        }

                                    }
                                    dialog.cancel();
                                    break;
                                default:
                                    break;
                            }

                        }
                    });

            choiceDialog.create();
            choiceDialog.show();
        }
    }

    @Override
    public void updateFromDatabase() {
        updateListsFromDB();
        mWeekView.notifyDatasetChanged();
    }

    private void updateListsFromDB() {
        mListCourses = getDBQuester().getAllCourses();
        mListEventWithoutCourse = getDBQuester().getAllEventsWithoutCourse();
    }

    private void addEvent(Event event) {
        int dayDuration = event.getEndDate().get(Calendar.DAY_OF_YEAR)
                - event.getStartDate().get(Calendar.DAY_OF_YEAR);
        int monthDuration = event.getEndDate().get(Calendar.MONTH)
                - event.getStartDate().get(Calendar.MONTH);
        int yearDuration = event.getEndDate().get(Calendar.YEAR)
                - event.getStartDate().get(Calendar.YEAR);

        if (dayDuration != 0
                && ((monthDuration >= 0 && yearDuration == 0) || (monthDuration <= 0 && yearDuration == 1))) {
            List<Calendar> startList = new ArrayList<Calendar>();
            Calendar start = (Calendar) event.getStartDate().clone();
            startList.add(start);
            if (yearDuration == 1) {
                Calendar cal = new GregorianCalendar();
                cal.set(cal.get(Calendar.YEAR), Calendar.DECEMBER,
                        NB_DAY_IN_ONE_MONTH);
                int nbDaysCurrentYear = cal.get(Calendar.DAY_OF_YEAR)
                        - event.getStartDate().get(Calendar.DAY_OF_YEAR);
                int nbDaysNextYear = event.getEndDate().get(
                        Calendar.DAY_OF_YEAR);
                dayDuration = nbDaysCurrentYear + nbDaysNextYear;
            }
            for (int i = 0; i <= dayDuration; i++) {
                Calendar end = event.getEndDate();
                if (i != dayDuration) {
                    end = (Calendar) startList.get(i).clone();
                    end.set(Calendar.HOUR_OF_DAY, HOUR_23);
                    end.set(Calendar.MINUTE, MINUTE_59);
                }

                mListEvents.add(new WeekViewEvent(event.getId(), event
                        .getName(), startList.get(i), end, PeriodType.DEFAULT,
                        event.getDescription()));

                Calendar newStart = (Calendar) startList.get(i).clone();
                newStart.add(Calendar.DAY_OF_YEAR, 1);
                newStart.set(Calendar.HOUR_OF_DAY, 0);
                newStart.set(Calendar.MINUTE, 0);
                startList.add(newStart);
            }
        } else {
            mListEvents.add(new WeekViewEvent(event.getId(), event.getName(),
                    event.getStartDate(), event.getEndDate(),
                    PeriodType.DEFAULT, event.getDescription()));
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
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        ArrayAdapter<String> arrayAdapter = new MySpinnerAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerList());

        ActionBar.OnNavigationListener mOnNavigationListener = new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int position, long itemId) {
                if (position == 0) {
                    mWeekView.setIsFirstDrawBis(true);
                    changeCalendarView(TYPE_DAY_VIEW, NUMBER_VISIBLE_DAYS_DAY,
                            SIZE_COLUMN_GAP_DAY, SIZE_FRONT_DAY,
                            SIZE_FRONT_EVENT_DAY);
                    return true;
                } else if (position == 1) {
                    mWeekView.setIsFirstDrawBis(true);
                    changeCalendarView(TYPE_THREE_DAY_VIEW,
                            NUMBER_VISIBLE_DAYS_3DAYS, SIZE_COLUMN_GAP_3DAYS,
                            SIZE_FRONT_3DAYS, SIZE_FRONT_EVENT_3DAYS);
                    return true;
                } else if (position == 2) {
                    mWeekView.setIsFirstDrawBis(true);
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

    private void switchToCourseDetails(String courseName) {
        Intent courseDetailsActivityIntent = new Intent(this,
                CourseDetailsActivity.class);

        courseDetailsActivityIntent.putExtra("course", courseName);
        startActivity(courseDetailsActivityIntent);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading course details");
        mDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        super.setUdpateData(this);
        mListCourses = getDBQuester().getAllCourses();
        mListEventWithoutCourse = getDBQuester().getAllEventsWithoutCourse();
        mWeekView.notifyDatasetChanged();
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}
