package ch.epfl.calendar.display;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import ch.epfl.calendar.R;


/**
 * @author Maxime
 *
 */
public class EventDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        
        Intent startIntent = getIntent();
        
        String description = startIntent.getStringExtra("description");
        
        TextView textView = (TextView) findViewById(R.id.eventDescription);
        textView.setText(description);
        
        
    }
}
