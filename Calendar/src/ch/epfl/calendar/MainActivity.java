package ch.epfl.calendar;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import ch.epfl.calendar.apiInterface.UpdateDataFromDBInterface;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.data.PeriodType;
import ch.epfl.calendar.display.CourseDetailsActivity;
import ch.epfl.calendar.display.EventDetailActivity;
import ch.epfl.calendar.persistence.DBQuester;
import ch.epfl.calendar.thirdParty.calendarViews.WeekView;
import ch.epfl.calendar.thirdParty.calendarViews.WeekViewEvent;

/**
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
    private List<Event> mListEventWithoutCourse = new ArrayList<Event>();
    private ProgressDialog mDialog;

    private DBQuester mDB;
    

    public static final String TAG = "MainActivity::";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        mDB = new DBQuester();

        // Used for destroy the database
        this.deleteDatabase(App.DATABASE_NAME);
        updateListsFromDB();

        if (mListCourses.isEmpty()) {
            populateCalendarFromISA();
        } else {
            mWeekView.notifyDatasetChanged();
        }
    }

    private void updateListsFromDB() {
        mListCourses = mDB.getAllCourses();
        mListEventWithoutCourse = mDB.getAllEventsWithoutCourse();
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
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_today:
                mWeekView.goToToday();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    // FIXME : What do we have to do with this method ????
    private void switchToEventDetail(String name, String description) {
        Intent eventDetailActivityIntent = new Intent(this,
                EventDetailActivity.class);

        eventDetailActivityIntent.putExtra("description", new String[] { name,
                description });
        startActivity(eventDetailActivityIntent);
    }

    @Override
    public List<WeekViewEvent> onMonthChange() {

        // Populate the week view with some events.
        mMListEvents = new ArrayList<WeekViewEvent>();

        for (Course c : mListCourses) {
            for (Period p : c.getPeriods()) {
                mMListEvents.add(new WeekViewEvent(mIdEvent++, getEventTitle(c,
                        p), p.getStartDate(), p.getEndDate(), p.getType(), c
                        .getDescription()));
            }
            for (Event event : c.getEvents()) {
                mMListEvents.add(new WeekViewEvent(event.getId(), event
                        .getName(), event.getStartDate(), event.getEndDate(),
                        PeriodType.DEFAULT, event.getmDescription()));
            }

        }
        for (Event event : mListEventWithoutCourse) {
            mMListEvents.add(new WeekViewEvent(event.getId(), event.getName(),
                    event.getStartDate(), event.getEndDate(),
                    PeriodType.DEFAULT, event.getmDescription()));
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
    public void onEventClick(WeekViewEvent weekEvent, RectF eventRect) {
        if (weekEvent.getmType().equals(PeriodType.LECTURE)
                || weekEvent.getmType().equals(PeriodType.PROJECT)
                || weekEvent.getmType().equals(PeriodType.EXERCISES)) {
            String cours = weekEvent.getName().split("\n")[0];
            switchToCourseDetails(cours);
        } else {
            Event event = new DBQuester().getEvent(weekEvent.getId());
            switchToEditActivity(event);
        }
    }

    /*
     * @Override public void onEventClick(WeekViewEvent weekEvent, RectF
     * eventRect) { if (weekEvent.getmType().equals(PeriodType.LECTURE) ||
     * weekEvent.getmType().equals(PeriodType.PROJECT) ||
     * weekEvent.getmType().equals(PeriodType.EXERCISES)) { String cours =
     * weekEvent.getName().split("\n")[0]; switchToCourseDetails(cours); } else
     * { Event event = new DBQuester().getEvent(weekEvent.getId()); if
     * (event.getLinkedCourse().equals(App.NO_COURSE)) { String description =
     * weekEvent.getmDescription(); switchToEventDetail(event.getName(),
     * description); } else { String coursName = event.getLinkedCourse();
     * switchToCourseDetails(coursName); } } }
     */

    @Override
    public void onEventLongPress(final WeekViewEvent event, RectF eventRect) {
        if (event.getmType() == PeriodType.EXERCISES
                || event.getmType() == PeriodType.LECTURE
                || event.getmType() == PeriodType.PROJECT) {
            Toast.makeText(this, "You can not delete this event",
                    Toast.LENGTH_LONG).show();

        } else {

            AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
            deleteDialog.setTitle("Delete Event");
            deleteDialog.setCancelable(false);
            deleteDialog.setMessage("Do you really want to delete this event");
            deleteDialog.setPositiveButton("Yes", new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    long id = event.getId();
                    Event eventFromDB = mDB.getEvent(id);
                    mDB.deleteEvent(eventFromDB);
                    updateListsFromDB();
                    mWeekView.notifyDatasetChanged();
                    dialog.cancel();

                }
            });
            deleteDialog.setNegativeButton("No", new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            });
            deleteDialog.create();
            deleteDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTH_ACTIVITY_CODE && resultCode == RESULT_OK) {
            mListCourses = new ArrayList<Course>();
            populateCalendarFromISA();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
      
        super.onResume();
        super.setUdpateData(this);
        mListCourses = mDB.getAllCourses();
        mListEventWithoutCourse = mDB.getAllEventsWithoutCourse();
        mWeekView.notifyDatasetChanged();
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public void updateData() {
        updateListsFromDB();
        mWeekView.notifyDatasetChanged();
    }
}
