package ch.epfl.calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import ch.epfl.calendar.display.AddEventActivity;
import ch.epfl.calendar.display.CoursesListActivity;

/**
 * @author fouchepi
 *
 */
public class DefaultActionBarActivity extends Activity {
    
    private Activity mThisActivity;
    public static final int ADD_EVENT_ACTIVITY_CODE = 2;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_action_bar);
        mThisActivity = this;
        defaultActionBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.default_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_courses_list:
                switchToCoursesList();
                return true;
            case R.id.action_settings:
                switchToCreditsActivity();
                return true;
            case R.id.add_event:
                switchToAddEventsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private void defaultActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
    }
    
    private void switchToCoursesList() {
        Intent coursesListActivityIntent = new Intent(this,
                CoursesListActivity.class);
        startActivity(coursesListActivityIntent);
    }
    
    private void switchToAddEventsActivity() {
        Intent addEventsActivityIntent = new Intent(this,
                AddEventActivity.class);
        startActivityForResult(addEventsActivityIntent, ADD_EVENT_ACTIVITY_CODE);
    }
    
    private void switchToCreditsActivity() {
        Intent i = new Intent(mThisActivity, CreditsActivity.class);
        startActivity(i);
    }
    
}
