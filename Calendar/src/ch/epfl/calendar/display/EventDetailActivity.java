package ch.epfl.calendar.display;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import ch.epfl.calendar.DefaultActionBarActivity;
import ch.epfl.calendar.R;
import ch.epfl.calendar.apiInterface.UpdateDataFromDBInterface;

/**
 * @author Maxime
 * 
 */
public class EventDetailActivity extends DefaultActionBarActivity implements
        UpdateDataFromDBInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setUdpateData(this);
        setContentView(R.layout.activity_event_detail);

        Intent startIntent = getIntent();

        String description = startIntent.getStringExtra("description");

        TextView textView = (TextView) findViewById(R.id.eventDescription);
        textView.setText(description);

    }

    @Override
    public void updateData() {
        // Do nothing

    }
}
