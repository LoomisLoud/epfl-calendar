package ch.epfl.calendar.display;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.ActionBar;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import ch.epfl.calendar.App;
import ch.epfl.calendar.DefaultActionBarActivity;
import ch.epfl.calendar.R;
import ch.epfl.calendar.apiInterface.UpdateDataFromDBInterface;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.data.PeriodType;
import ch.epfl.calendar.persistence.DBQuester;

/**
 * @author LoomisLoud
 * 
 */
public class AddEventBlockActivity extends DefaultActionBarActivity implements
        UpdateDataFromDBInterface {
	
    private Button mButtonEndHour;
    private Button mButtonStartHour;
    private TimePickerDialog mStartTimePickerDialog;
    private TimePickerDialog mEndTimePickerDialog;
    private TimePickerDialog.OnTimeSetListener mStartTimePickerListener;
    private TimePickerDialog.OnTimeSetListener mEndTimePickerListener;
    private Calendar mStartCalendar = Calendar.getInstance();
    private Calendar mEndCalendar = Calendar.getInstance();
    private Spinner mSpinnerDays;
    private String mCourseName;
    private int mPosition;
    private Intent mIntent;
    private static final boolean IS_BLOCK = true;
    
    /**
     * The number of days in a wekk
     */
    public final static int NUMBER_OF_DAYS = 7;
    
    /**
     * 23
     */
    public static final int ELEVEN_O_CLOCK = 23;
    
    /**
     * 59
     */
    public static final int LAST_MINUTE = 59;
    
    /**
     * The format used to display hours on screen. aa parses to AM or PM
     */
    public static final String TIME_FORMAT = "hh:mm aa";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setUdpateData(this);
        setContentView(R.layout.activity_add_event_block);

        addEventActionBar();

        mIntent = getIntent();
        mCourseName = "";
        if (mIntent.hasExtra("courseName")) {
            mCourseName = mIntent.getStringExtra("courseName");
        }
        mPosition = -1;
        if (mIntent.hasExtra("position")) {
            mPosition = mIntent.getIntExtra("position", -1);
        }

        mSpinnerDays = (Spinner) findViewById(R.id.spinner_week_days);
        mButtonStartHour = (Button) findViewById(R.id.start_event_dialog_hour);
        mButtonEndHour = (Button) findViewById(R.id.end_event_dialog_hour);

        initializePickerDialog();
        initializeButton();

        setView();
    }

    /**
     * Finishes the activity and stores the data in the DB
     * 
     * @param v
     */
    public void finishActivity(View v) {
        try {
            transferAndStoreData();
        } catch (ReversedDatesException e) {
            Toast.makeText(AddEventBlockActivity.this.getBaseContext(),
                    AddEventBlockActivity.this.getString(R.string.reversed_dates),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        finish();
    }

    // This method has to be defined, even if it does nothing !
    @Override
    public void updateFromDatabase() {
        // Do nothing
    }

    private void initializePickerDialog() {
        createPickerListener();

        mStartTimePickerDialog = new TimePickerDialog(AddEventBlockActivity.this,
                mStartTimePickerListener,
                mStartCalendar.get(Calendar.HOUR_OF_DAY),
                mStartCalendar.get(Calendar.MINUTE), false);

        mEndTimePickerDialog = new TimePickerDialog(AddEventBlockActivity.this,
                mEndTimePickerListener, mEndCalendar.get(Calendar.HOUR_OF_DAY),
                mEndCalendar.get(Calendar.MINUTE), false);
    }
    
    private void initializeButton() {
        mButtonStartHour.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartTimePickerDialog.show();
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
        mStartTimePickerListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                updateCalendarTimeAndButtons(mStartCalendar, mButtonStartHour,
                        TIME_FORMAT, hourOfDay, minute);
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
    
    private void updateTimeButton(Calendar calendar, Button button,
            String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        button.setText(sdf.format(calendar.getTime()));
    }
    
    private void updateCalendarTimeAndButtons(Calendar calendar, Button button,
            String format, int hour, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        updateTimeButton(calendar, button, format);
    }
    
    private void setView() {
    	mEndCalendar.set(Calendar.HOUR_OF_DAY,
                mEndCalendar.get(Calendar.HOUR_OF_DAY) + 1);
        mEndTimePickerDialog.updateTime(
                mEndCalendar.get(Calendar.HOUR_OF_DAY),
                mEndCalendar.get(Calendar.MINUTE));
        updateTimeButton(mStartCalendar, mButtonStartHour, TIME_FORMAT);
        updateTimeButton(mEndCalendar, mButtonEndHour, TIME_FORMAT);
        
        ArrayAdapter<CharSequence> mSpinnerDaysAdapter = ArrayAdapter
                .createFromResource(this, R.array.week_days,
                        android.R.layout.simple_spinner_item);
        mSpinnerDaysAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerDays.setAdapter(mSpinnerDaysAdapter);
    }

    private void addEventActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("New Homework Block");
    }

    private Calendar createStartDateBlock(int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        while (calendar.get(Calendar.DAY_OF_WEEK) != day) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return calendar;
    }

    private Calendar createEndDateBlock(Period lastPeriod) {
        return lastPeriod.getEndDate();
    }

    private int calendarDayFromArrayAdapterDay(int day) {
        return ((day + 1) % NUMBER_OF_DAYS) + 1;
    }

    private void storeWeeklyEvent(Intent i) throws ReversedDatesException {
        DBQuester dbQuester = new DBQuester();

        Calendar startEvent = createStartDateBlock(
                calendarDayFromArrayAdapterDay(mSpinnerDays
                        .getSelectedItemPosition()),
                mStartCalendar.get(Calendar.HOUR_OF_DAY), mStartCalendar
                        .get(Calendar.MINUTE));
        Calendar endEvent = createStartDateBlock(
                calendarDayFromArrayAdapterDay(mSpinnerDays
                        .getSelectedItemPosition()),
                mEndCalendar.get(Calendar.HOUR_OF_DAY), mEndCalendar
                        .get(Calendar.MINUTE));
        Calendar lastPeriod = createEndDateBlock((Period) i.getParcelableExtra("period"));
        Calendar endDate = lastPeriod;
        endDate.set(Calendar.WEEK_OF_YEAR, lastPeriod.get(Calendar.WEEK_OF_YEAR));
        endDate.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        endDate.set(Calendar.HOUR_OF_DAY, ELEVEN_O_CLOCK);
        endDate.set(Calendar.MINUTE, LAST_MINUTE);
        
        if (endEvent.before(startEvent)) {
            throw new ReversedDatesException();
        }

        while (endDate.compareTo(endEvent) > 0) {
            Event e = new Event("Do " + mCourseName + " homework",
                    App.calendarToBasicFormatString(startEvent),
                    App.calendarToBasicFormatString(endEvent),
                    PeriodType.DEFAULT.toString(), mCourseName,
                    "You have to work on " + mCourseName + " now", IS_BLOCK,
                    DBQuester.NO_ID);
            dbQuester.storeEvent(e);
            
            startEvent.add(Calendar.DAY_OF_MONTH, NUMBER_OF_DAYS);
            endEvent.add(Calendar.DAY_OF_MONTH, NUMBER_OF_DAYS);
        }
    }

    private void transferAndStoreData() throws ReversedDatesException {
        Intent i = getIntent();

        storeWeeklyEvent(i);

        i.putExtra("courseName", mCourseName);
        i.putExtra("position", mPosition);

        setResult(RESULT_OK, i);
    }
}
