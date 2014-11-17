package ch.epfl.calendar.display;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import ch.epfl.calendar.R;

/**
 * @author LoomisLoud
 * 
 */
public class AddEventActivity extends Activity {

    private EditText mNameEvent;
    private DatePicker mStartEventDate;

    private TimePicker mStartEventHour;

    private DatePicker mEndEventDate;
    private TimePicker mEndEventHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        mNameEvent = (EditText) findViewById(R.id.name_event_text);
        //
        mStartEventDate = (DatePicker) findViewById(R.id.start_event_picker_date);
        mStartEventHour = (TimePicker) findViewById(R.id.start_event_picker_hour);
        //
        mEndEventDate = (DatePicker) findViewById(R.id.end_event_picker_date);
        mEndEventHour = (TimePicker) findViewById(R.id.end_event_picker_hour);

    }

    private void transferData() {
        Intent i = getIntent();
        i.putExtra("nameInfo", mNameEvent.getText().toString());

        i.putExtra("startYear", mStartEventDate.getYear());
        i.putExtra("startMonth", mStartEventDate.getMonth());
        i.putExtra("startDay", mStartEventDate.getDayOfMonth());
        i.putExtra("startHour", mStartEventHour.getCurrentHour());
        i.putExtra("startMinutes", mStartEventHour.getCurrentMinute());

        i.putExtra("endYear", mEndEventDate.getYear());
        i.putExtra("endMonth", mEndEventDate.getMonth());
        i.putExtra("endDay", mEndEventDate.getDayOfMonth());
        i.putExtra("endHour", mEndEventHour.getCurrentHour());
        i.putExtra("endMinutes", mEndEventHour.getCurrentMinute());

        setResult(RESULT_OK, i);
    }

    public void finishActivity(View v) {
        transferData();
        finish();
    }
}