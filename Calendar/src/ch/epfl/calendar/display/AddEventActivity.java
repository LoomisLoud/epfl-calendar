package ch.epfl.calendar.display;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import ch.epfl.calendar.App;
import ch.epfl.calendar.DefaultActionBarActivity;
import ch.epfl.calendar.R;
import ch.epfl.calendar.apiInterface.UpdateDataFromDBInterface;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.PeriodType;
import ch.epfl.calendar.persistence.DBQuester;

/**
 * @author LoomisLoud
 * 
 */
public class AddEventActivity extends DefaultActionBarActivity implements
        UpdateDataFromDBInterface {
    
    /**
     * The identifier of the {@link AuthenticationActivity}
     */
    public static final int AUTH_ACTIVITY_CODE = 1;
    
    private static final boolean IS_BLOCK = false;

    private EditText mNameEvent;
    private EditText mDescriptionEvent;
    private Spinner mSpinnerCourses;

    private int eventId = DBQuester.NO_ID;

    private String mLinkedCourse = App.NO_COURSE;
    private List<String> mCoursesNames = new ArrayList<String>();

    private Button mButtonStartDate;
    private Button mButtonStartHour;
    private Button mButtonEndDate;
    private Button mButtonEndHour;
    private Calendar mStartCalendar = Calendar.getInstance();
    private Calendar mEndCalendar = Calendar.getInstance();
    private DatePickerDialog mStartDatePickerDialog;
    private TimePickerDialog mStartTimePickerDialog;
    private DatePickerDialog mEndDatePickerDialog;
    private TimePickerDialog mEndTimePickerDialog;
    private DatePickerDialog.OnDateSetListener mStartDatePickerListener;
    private TimePickerDialog.OnTimeSetListener mStartTimePickerListener;
    private DatePickerDialog.OnDateSetListener mEndDatePickerListener;
    private TimePickerDialog.OnTimeSetListener mEndTimePickerListener;
    private static final String DATE_FORMAT = "MM/dd/yy";
    private static final String TIME_FORMAT = "hh:mm aa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setUdpateData(this);
        setContentView(R.layout.activity_add_event);

        Intent startingIntent = getIntent();

        addEventActionBar();

        mNameEvent = (EditText) findViewById(R.id.name_event_text);
        mDescriptionEvent = (EditText) findViewById(R.id.description_event_text);

        mSpinnerCourses = (Spinner) findViewById(R.id.spinner_courses);

        mButtonStartDate = (Button) findViewById(R.id.start_event_dialog_date);
        mButtonStartHour = (Button) findViewById(R.id.start_event_dialog_hour);
        mButtonEndDate = (Button) findViewById(R.id.end_event_dialog_date);
        mButtonEndHour = (Button) findViewById(R.id.end_event_dialog_hour);

        initializePickerDialog();
        initializeButton();

        mCoursesNames = getDBQuester().getAllCoursesNames();

        setView();
        initializeValue(startingIntent);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean retour = super.onCreateOptionsMenu(menu);
        MenuItem addEventItem = (MenuItem) menu.findItem(R.id.add_event);
        addEventItem.setVisible(false);
        this.invalidateOptionsMenu();
        return retour;
    }

    /**
     * Ends this activity
     * @param v
     */
    public void finishActivity(View v) {
        try {
            transferData();
        } catch (ReversedDatesException e) {
            Toast.makeText(AddEventActivity.this.getBaseContext(),
                    AddEventActivity.this.getString(R.string.reversed_dates),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        finish();
    }

    @Override
    public void updateFromDatabase() {
        mCoursesNames = getDBQuester().getAllCoursesNames();
    }

    private void initializePickerDialog() {
        createPickerListener();

        mStartDatePickerDialog = new DatePickerDialog(AddEventActivity.this,
                mStartDatePickerListener, mStartCalendar.get(Calendar.YEAR),
                mStartCalendar.get(Calendar.MONTH),
                mStartCalendar.get(Calendar.DAY_OF_MONTH));

        mStartTimePickerDialog = new TimePickerDialog(AddEventActivity.this,
                mStartTimePickerListener,
                mStartCalendar.get(Calendar.HOUR_OF_DAY),
                mStartCalendar.get(Calendar.MINUTE), false);

        mEndDatePickerDialog = new DatePickerDialog(AddEventActivity.this,
                mEndDatePickerListener, mEndCalendar.get(Calendar.YEAR),
                mEndCalendar.get(Calendar.MONTH),
                mEndCalendar.get(Calendar.DAY_OF_MONTH));

        mEndTimePickerDialog = new TimePickerDialog(AddEventActivity.this,
                mEndTimePickerListener, mEndCalendar.get(Calendar.HOUR_OF_DAY),
                mEndCalendar.get(Calendar.MINUTE), false);
    }

    private void initializeButton() {
        mButtonStartDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartDatePickerDialog.show();
            }
        });

        mButtonStartHour.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartTimePickerDialog.show();
            }
        });

        mButtonEndDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEndDatePickerDialog.show();
            }
        });

        mButtonEndHour.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEndTimePickerDialog.show();
            }
        });
    }

    private void createPickerListener() {
        mStartDatePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                    int dayOfMonth) {
                updateCalendarDateAndButtons(mStartCalendar, mButtonStartDate,
                        DATE_FORMAT, year, monthOfYear, dayOfMonth);
            }
        };

        mStartTimePickerListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                updateCalendarTimeAndButtons(mStartCalendar, mButtonStartHour,
                        TIME_FORMAT, hourOfDay, minute);
            }
        };

        mEndDatePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                    int dayOfMonth) {
                updateCalendarDateAndButtons(mEndCalendar, mButtonEndDate,
                        DATE_FORMAT, year, monthOfYear, dayOfMonth);
            }
        };

        mEndTimePickerListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                updateCalendarTimeAndButtons(mEndCalendar, mButtonEndHour,
                        TIME_FORMAT, hourOfDay, minute);
            }
        };
    }

    private void updateDateButton(Calendar calendar, Button button,
            String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        button.setText(sdf.format(calendar.getTime()));
    }

    private void updateTimeButton(Calendar calendar, Button button,
            String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        button.setText(sdf.format(calendar.getTime()));
    }

    private void updateCalendarDateAndButtons(Calendar calendar, Button button,
            String format, int year, int month, int day) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        updateDateButton(calendar, button, format);
    }

    private void updateCalendarTimeAndButtons(Calendar calendar, Button button,
            String format, int hour, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        updateTimeButton(calendar, button, format);
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
            mDescriptionEvent.setText(event.getDescription());
            initializeSpinner(event.getLinkedCourse());

            int startYear = event.getStartDate().get(Calendar.YEAR);
            int startMonth = event.getStartDate().get(Calendar.MONTH);
            int startDay = event.getStartDate().get(Calendar.DAY_OF_MONTH);
            mStartDatePickerDialog.updateDate(startYear, startMonth, startDay);
            updateCalendarDateAndButtons(mStartCalendar, mButtonStartDate,
                    DATE_FORMAT, startYear, startMonth, startDay);

            int startHour = event.getStartDate().get(Calendar.HOUR_OF_DAY);
            int startMinute = event.getStartDate().get(Calendar.MINUTE);
            mStartTimePickerDialog.updateTime(startHour, startMinute);
            updateCalendarTimeAndButtons(mStartCalendar, mButtonStartHour,
                    TIME_FORMAT, startHour, startMinute);

            int endYear = event.getEndDate().get(Calendar.YEAR);
            int endMonth = event.getEndDate().get(Calendar.MONTH);
            int endDay = event.getEndDate().get(Calendar.DAY_OF_MONTH);
            mEndDatePickerDialog.updateDate(endYear, endMonth, endDay);
            updateCalendarDateAndButtons(mEndCalendar, mButtonEndDate, DATE_FORMAT,
                    endYear, endMonth, endDay);

            int endHour = event.getEndDate().get(Calendar.HOUR_OF_DAY);
            int endMinute = event.getEndDate().get(Calendar.MINUTE);
            mEndTimePickerDialog.updateTime(endHour, endMinute);
            updateCalendarTimeAndButtons(mEndCalendar, mButtonEndHour, TIME_FORMAT,
                    endHour, endMinute);

        } else {
            mEndCalendar.set(Calendar.HOUR_OF_DAY,
                    mEndCalendar.get(Calendar.HOUR_OF_DAY) + 1);
            mEndTimePickerDialog.updateTime(
                    mEndCalendar.get(Calendar.HOUR_OF_DAY),
                    mEndCalendar.get(Calendar.MINUTE));
            updateDateButton(mStartCalendar, mButtonStartDate, DATE_FORMAT);
            updateTimeButton(mStartCalendar, mButtonStartHour, TIME_FORMAT);
            updateDateButton(mEndCalendar, mButtonEndDate, DATE_FORMAT);
            updateTimeButton(mEndCalendar, mButtonEndHour, TIME_FORMAT);
        }
    }

    private void addEventActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("New Event");
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
                } else {
                    mLinkedCourse = App.NO_COURSE;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mLinkedCourse = App.NO_COURSE;
            }

        });
    }

    private void transferData() throws ReversedDatesException {

        if (mEndCalendar.before(mStartCalendar)) {
            throw new ReversedDatesException();
        }

        Event e = new Event(mNameEvent.getText().toString(),
                App.calendarToBasicFormatString(mStartCalendar),
                App.calendarToBasicFormatString(mEndCalendar),
                PeriodType.DEFAULT.toString(), mLinkedCourse, mDescriptionEvent
                        .getText().toString(), IS_BLOCK, eventId);
        DBQuester dbQuester = new DBQuester();
        dbQuester.storeEvent(e);
    }
}
