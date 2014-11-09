package ch.epfl.calendar.display;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.calendar.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddEventActivity extends Activity {
    private EditText nameEvent;
    private EditText startEvent;
    private EditText endEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        nameEvent = (EditText) findViewById(R.id.name_event_text);
        startEvent = (EditText) findViewById(R.id.start_event_text);
        endEvent = (EditText) findViewById(R.id.end_event_text);
        
        

    }

    public void finishActivity(View v) {
        
        finish();
    }

   

}