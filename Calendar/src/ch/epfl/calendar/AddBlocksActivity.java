package ch.epfl.calendar;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.calendar.data.Course;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

/**
 * @author LoomisLoud
 * 
 */
public class AddBlocksActivity extends Activity {

	private ListView mListView;
	private List<Course> mCourses = new ArrayList<Course>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_blocks);

		mListView = (ListView) findViewById(R.id.coursesListView);

		// Check whether we're recreating a previously destroyed instance
		if (savedInstanceState != null) {
			// Restore value of members from saved state
			// System.out.println("Loading courses in savedInstanceState");
			mCourses = savedInstanceState.getParcelableArrayList("coursesList");
			callbackAppEngine(mCourses);
		} else {
			// Retrieve course for first time
			// System.out.println("Retrieving courses for first time");
			retrieveCourse();
		}

	}
	
	@Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the activity state
        savedInstanceState.putParcelableArrayList("coursesList", new ArrayList<Course>(mCourses));
        //System.out.println("Saving state");
        
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_blocks, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
