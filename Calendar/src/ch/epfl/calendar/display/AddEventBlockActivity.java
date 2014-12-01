package ch.epfl.calendar.display;

import java.util.Calendar;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import ch.epfl.calendar.App;
import ch.epfl.calendar.DefaultActionBarActivity;
import ch.epfl.calendar.R;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.data.PeriodType;
import ch.epfl.calendar.persistence.DBQuester;

/**
 * @author LoomisLoud
 * 
 */
public class AddEventBlockActivity extends DefaultActionBarActivity {

    private TimePicker mStartBlockEventHour;
    private TimePicker mEndBlockEventHour;
    private Spinner mSpinnerDays;
    private TextView mAskDay;
    private String mCourseName;
    private int mPosition;
    private Intent mIntent;
    private static final boolean IS_BLOCK = true;
    public final static int NUMBER_OF_DAYS = 7;

    private void setView() {
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

    private void storeWeeklyEvent(Intent i) {
        DBQuester dbQuester = new DBQuester();

        Calendar startEvent = createStartDateBlock(
                calendarDayFromArrayAdapterDay(mSpinnerDays
                        .getSelectedItemPosition()),
                mStartBlockEventHour.getCurrentHour(), mStartBlockEventHour
                        .getCurrentMinute());
        Calendar endEvent = createStartDateBlock(
                calendarDayFromArrayAdapterDay(mSpinnerDays
                        .getSelectedItemPosition()),
                mEndBlockEventHour.getCurrentHour(), mEndBlockEventHour
                        .getCurrentMinute());
        Calendar endDate = createEndDateBlock((Period) i
                .getParcelableExtra("period"));

        
        Event event = new Event("Do " + mCourseName + " homework",
                App.calendarToBasicFormatString(startEvent),
                App.calendarToBasicFormatString(endEvent),
                PeriodType.DEFAULT.toString(), mCourseName,
                "You have to work on " + mCourseName + " now", IS_BLOCK, DBQuester.NO_ID);
        dbQuester.storeEvent(event);

        while (endDate.getTimeInMillis() > endEvent.getTimeInMillis()) {
            startEvent.add(Calendar.DAY_OF_MONTH, NUMBER_OF_DAYS);
            endEvent.add(Calendar.DAY_OF_MONTH, NUMBER_OF_DAYS);

            Event e = new Event("Do " + mCourseName + " homework",
                    App.calendarToBasicFormatString(startEvent),
                    App.calendarToBasicFormatString(endEvent),
                    PeriodType.DEFAULT.toString(), mCourseName,
                    "You have to work on " + mCourseName + " now",
                    IS_BLOCK, DBQuester.NO_ID);
            dbQuester.storeEvent(e);
        }
    }

    private void transferAndStoreData() {
        Intent i = getIntent();

        storeWeeklyEvent(i);

        i.putExtra("courseName", mCourseName);
        i.putExtra("position", mPosition);

        setResult(RESULT_OK, i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        mAskDay = (TextView) findViewById(R.id.ask_block_day);
        mSpinnerDays = (Spinner) findViewById(R.id.spinner_week_days);
        mStartBlockEventHour = (TimePicker) findViewById(R.id.from_picker_hour);
        mEndBlockEventHour = (TimePicker) findViewById(R.id.to_picker_hour);

        mAskDay.setText(getString(R.string.choose_day_block) + " "
                + mCourseName + ": ");

        setView();
    }

    /**
     * Finishes the activity and stores the data in the DB
     * 
     * @param v
     */
    public void finishActivity(View v) {
        transferAndStoreData();
        finish();
    }
}
