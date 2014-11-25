package ch.epfl.calendar.display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import ch.epfl.calendar.DefaultActionBarActivity;
import ch.epfl.calendar.R;
import ch.epfl.calendar.apiInterface.CalendarClient;
import ch.epfl.calendar.apiInterface.CalendarClientInterface;
import ch.epfl.calendar.data.Block;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.utils.ConstructListCourse;

/**
 * @author LoomisLoud
 * 
 */
public class AddBlocksActivity extends DefaultActionBarActivity implements AppEngineDownloadInterface {

	public static final int AUTH_ACTIVITY_CODE = 1;
	public static final int BLOCK_ACTIVITY_CODE = 2;
	public static final int HUNDRED = 100;
	private ListView mListView;
	private List<Course> mCourses = new ArrayList<Course>();
	private List<Block> blockList = new ArrayList<Block>();
	private List<Period> periodList = new ArrayList<Period>();
	private TextView mGreeter;
	private Intent intentToEventCreation;
	private SimpleAdapter simpleAdapter;

	private void retrieveCourse() {
		CalendarClientInterface calendarClient = new CalendarClient(this, this);
		calendarClient.getISAInformations();
	}

	private void addEventActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("List of Blocks");
    }

	private void retrieveCourseInfo(List<Course> coursesList) {
	
		ConstructListCourse constructCourse = ConstructListCourse
				.getInstance(this);
		constructCourse.completeCourse(coursesList, this);
	
	}

	private ArrayList<Block> constructBlockList(List<Course> courseList) {
		ArrayList<Block> list = new ArrayList<Block>();
		for (Course c : courseList) {
			list.add(new Block(c, c.getCredits()));
		}
		return list;
	}

	private void updateCredits(Intent data) {
		if (data.hasExtra("courseName")) {
			String course = data.getStringExtra("courseName");
			int startHour = data.getIntExtra("startHour", -1);
			int endHour = data.getIntExtra("endHour", -1);
			int startMinutes = data.getIntExtra("startMinutes", -1);
			int endMinutes = data.getIntExtra("endMinutes", -1);
			int timeToRemove = (endHour + (endMinutes % HUNDRED))
					- (startHour + (startMinutes % HUNDRED));
	
			for (int i = 0; i < blockList.size(); i++) {
				if (blockList.get(i).isBlockOf(course)) {
					Block tempBlock = blockList.get(i);
					blockList.get(i).setRemainingCredits(
							tempBlock.getRemainingCredits() - timeToRemove);
				}
			}
			createAdapterAndListView(createListForAdapter());
		}
	}

	private ArrayList<Map<String, String>> createListForAdapter() {
		ArrayList<Map<String, String>> blockListAdapter = new ArrayList<Map<String, String>>();
	
		for (Block b : blockList) {
			if (b.getRemainingCredits() > 0.00) {
				Map<String, String> blockMap = new HashMap<String, String>();
				ArrayList<Period> periods = new ArrayList<Period>(b.getCourse().getPeriods());
				
				blockMap.put("Block name", b.getCourse().getName());
				blockMap.put("Remaining credits", b.creditsToString());
				
				
				periodList.add(periods.get(periods.size() - 1));
				
				blockListAdapter.add(blockMap);
			}
		}
	
		return blockListAdapter;
	}

	private void createAdapterAndListView(
			final List<Map<String, String>> finalBlockList) {
		simpleAdapter = new SimpleAdapter(this, finalBlockList,
				android.R.layout.simple_list_item_2, new String[] {
					"Block name", "Remaining credits" }, new int[] {
						android.R.id.text1, android.R.id.text2 });
	
		mListView.setAdapter(simpleAdapter);
	
		mListView.setOnItemClickListener(new OnItemClickListener() {
	
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
	
				intentToEventCreation.putExtra("courseName", finalBlockList
						.get(position).get("Block name"));
				intentToEventCreation.putExtra("period", periodList.get(position));
				
				startActivityForResult(intentToEventCreation,
						BLOCK_ACTIVITY_CODE);
			}
	
		});
	
		if (simpleAdapter.isEmpty()) {
			mGreeter.setText(getString(R.string.greeter_no_more_blocks));
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_blocks);

		intentToEventCreation = new Intent(this, AddEventBlockActivity.class);

		mGreeter = (TextView) findViewById(R.id.greeter);
		mListView = (ListView) findViewById(R.id.credits_blocks_list);

		addEventActionBar();
		
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == AUTH_ACTIVITY_CODE && resultCode == RESULT_OK) {
			this.mCourses = new ArrayList<Course>();
			retrieveCourse();
		}
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == BLOCK_ACTIVITY_CODE && resultCode == RESULT_OK) {
			updateCredits(data);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save the activity state
		savedInstanceState.putParcelableArrayList("coursesList",
				new ArrayList<Course>(mCourses));
		// System.out.println("Saving state");

		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(savedInstanceState);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean retour = super.onCreateOptionsMenu(menu);
        MenuItem addEventBlockItem = (MenuItem) menu.findItem(R.id.add_event_block);
        addEventBlockItem.setVisible(false);
        this.invalidateOptionsMenu();
        return retour;
    }

	public void callbackAppEngine(List<Course> coursesList) {

		blockList = constructBlockList(coursesList);

		final List<Map<String, String>> finalBlockList = createListForAdapter();

		createAdapterAndListView(finalBlockList);
	}

	@Override
	public void callbackDownload(boolean success, List<Course> courses) {
		if (success) {
			this.mCourses = courses;
			retrieveCourseInfo(mCourses);
		} else {
			switchToAuthenticationActivity();
		}
	}
}