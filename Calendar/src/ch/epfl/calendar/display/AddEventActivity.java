package ch.epfl.calendar.display;


import ch.epfl.calendar.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * @author LoomisLoud
 *
 */
public class AddEventActivity extends Activity {
    
    //private EditText mNameEvent;
    //private DatePicker mStartEventDate;
    //private TimePicker mStartEventHour;
    //private DatePicker mEndEventDate;
    //private TimePicker mEndEventHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        //mNameEvent = (EditText) findViewById(R.id.name_event_text);
        //
        //mStartEventDate = (DatePicker) findViewById(R.id.start_event_picker_date);
        //mStartEventHour = (TimePicker) findViewById(R.id.start_event_picker_hour);
        //
        //mEndEventDate = (DatePicker) findViewById(R.id.end_event_picker_date);
        //mEndEventHour = (TimePicker) findViewById(R.id.end_event_picker_hour);
        
    }
    public void finishActivity(View v) {
        
        finish();
    }

   

}