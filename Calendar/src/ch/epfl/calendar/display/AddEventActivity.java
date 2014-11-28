package ch.epfl.calendar.display;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import ch.epfl.calendar.App;
import ch.epfl.calendar.DefaultActionBarActivity;
import ch.epfl.calendar.R;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.PeriodType;
import ch.epfl.calendar.persistence.DBQuester;

/**
 * @author LoomisLoud
 * 
 */
public class AddEventActivity extends DefaultActionBarActivity {

    private EditText mNameEvent;
    private EditText mDescriptionEvent;
    private DatePicker mStartEventDate;

    private TimePicker mStartEventHour;

    private DatePicker mEndEventDate;
    private TimePicker mEndEventHour;

    private int eventId = DBQuester.NO_ID;

    private String mLinkedCourse = App.NO_COURSE;
    private Spinner mSpinnerCourses;
    private List<String> mCoursesNames = new ArrayList<String>();
    private DBQuester mDB;

    private static final boolean IS_BLOCK = false;

    public static final int AUTH_ACTIVITY_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Intent startingIntent = getIntent();

        addEventActionBar();

        mNameEvent = (EditText) findViewById(R.id.name_event_text);
        mDescriptionEvent = (EditText) findViewById(R.id.description_event_text);

        mStartEventDate = (DatePicker) findViewById(R.id.start_event_picker_date);
        mStartEventHour = (TimePicker) findViewById(R.id.start_event_picker_hour);

        mEndEventDate = (DatePicker) findViewById(R.id.end_event_picker_date);
        mEndEventHour = (TimePicker) findViewById(R.id.end_event_picker_hour);

        mSpinnerCourses = (Spinner) findViewById(R.id.spinner_courses);

        mDB = new DBQuester();

        mCoursesNames = mDB.getAllCoursesNames();

        setView();
        initializeValue(startingIntent);
    }

    private void initializeSpinner(String linkCourses) {
        if (linkCourses.equals(App.NO_COURSE)) {
            mSpinnerCourses.setSelection(0);
        } else {
            int position = mCoursesNames.indexOf(linkCourses);
            mSpinnerCourses.setSelection(position);
        }
    }
    
    private void initializeValue(Intent intent) {
        if (intent.hasExtra("Id")) {
            eventId = intent.getIntExtra("Id", DBQuester.NO_ID);
            Event event = new DBQuester().getEvent(eventId);
            
            mNameEvent.setText(event.getName());
            mDescriptionEvent.setText(event.getmDescription());
            initializeSpinner(event.getLinkedCourse());
            
            int startYear = event.getStartDate().get(Calendar.YEAR);
            int startMonth = event.getStartDate().get(Calendar.MONTH);
            int startDay = event.getStartDate().get(Calendar.DAY_OF_MONTH);
            mStartEventDate.updateDate(startYear, startMonth, startDay);
            
            int startHour = event.getStartDate().get(Calendar.HOUR_OF_DAY);
            mStartEventHour.setCurrentHour(startHour);
            int startMinute = event.getStartDate().get(Calendar.MINUTE);
            mStartEventHour.setCurrentMinute(startMinute);
            
            int endYear = event.getEndDate().get(Calendar.YEAR);
            int endMonth = event.getEndDate().get(Calendar.MONTH);
            int endDay = event.getEndDate().get(Calendar.DAY_OF_MONTH);
            mEndEventDate.updateDate(endYear, endMonth, endDay);

            int endHour = event.getEndDate().get(Calendar.HOUR_OF_DAY);
            mEndEventHour.setCurrentHour(endHour);
            int endMinute = event.getEndDate().get(Calendar.MINUTE);
            mEndEventHour.setCurrentMinute(endMinute);
        }
    }

    private void addEventActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("New Event");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean retour = super.onCreateOptionsMenu(menu);
        MenuItem addEventItem = (MenuItem) menu.findItem(R.id.add_event);
        addEventItem.setVisible(false);
        this.invalidateOptionsMenu();
        return retour;
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

    private void transferData() {
        Calendar start = createCalendar(mStartEventDate.getYear(),
                mStartEventDate.getMonth(), mStartEventDate.getDayOfMonth(),
                mStartEventHour.getCurrentHour(),
                mStartEventHour.getCurrentMinute());
        Calendar end = createCalendar(mEndEventDate.getYear(),
                mEndEventDate.getMonth(), mEndEventDate.getDayOfMonth(),
                mEndEventHour.getCurrentHour(),
                mEndEventHour.getCurrentMinute());
        Event e = new Event(mNameEvent.getText().toString(),
                App.calendarToBasicFormatString(start),
                App.calendarToBasicFormatString(end),
                PeriodType.DEFAULT.toString(), mLinkedCourse, mDescriptionEvent
                        .getText().toString(), IS_BLOCK, eventId);
        DBQuester dbQuester = new DBQuester();
        dbQuester.storeEvent(e);
    }

    public void finishActivity(View v) {
        transferData();
        finish();
    }

    private void setView() {
        mCoursesNames.add(0, "No connection with courses");

        ArrayAdapter<String> coursesNameAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, mCoursesNames);

        coursesNameAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCourses.setAdapter(coursesNameAdapter);
        mSpinnerCourses.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                if (position != 0) {
                    mLinkedCourse = mCoursesNames.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mLinkedCourse = App.NO_COURSE;
            }

        });
    }
}
