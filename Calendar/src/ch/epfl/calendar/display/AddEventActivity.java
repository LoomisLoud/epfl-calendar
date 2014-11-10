package ch.epfl.calendar.display;


import ch.epfl.calendar.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class AddEventActivity extends Activity {
    
    private EditText nameEvent;
    private DatePicker startEventDate;
    private TimePicker startEventHour;
    private DatePicker endEventDate;
    private TimePicker endEventHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        nameEvent = (EditText) findViewById(R.id.name_event_text);
        
        startEventDate = (DatePicker) findViewById(R.id.start_event_picker_date);
        startEventHour = (TimePicker) findViewById(R.id.start_event_picker_hour);
        
        endEventDate = (DatePicker) findViewById(R.id.end_event_picker_date);
        endEventHour = (TimePicker) findViewById(R.id.end_event_picker_hour);
        
    }
    public void finishActivity(View v) {
        
        finish();
    }

   

}