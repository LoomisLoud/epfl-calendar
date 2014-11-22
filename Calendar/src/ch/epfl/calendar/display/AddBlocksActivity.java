package ch.epfl.calendar.display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import ch.epfl.calendar.R;
import ch.epfl.calendar.apiInterface.CalendarClient;
import ch.epfl.calendar.apiInterface.CalendarClientDownloadInterface;
import ch.epfl.calendar.apiInterface.CalendarClientInterface;
import ch.epfl.calendar.authentication.AuthenticationActivity;
import ch.epfl.calendar.data.Block;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.utils.ConstructListCourse;

/**
 * @author LoomisLoud
 * 
 */
public class AddBlocksActivity extends Activity implements
		CalendarClientDownloadInterface, AppEngineDownloadInterface {

	public static final int AUTH_ACTIVITY_CODE = 1;
	private ListView mListView;
	private List<Course> mCourses = new ArrayList<Course>();
	private TextView mGreeter;
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_blocks);
		
		intent = new Intent(this, AddEventBlockActivity.class);
		mGreeter = (TextView) findViewById(R.id.greeter);
		mListView = (ListView) findViewById(R.id.credits_blocks_list);

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
		savedInstanceState.putParcelableArrayList("coursesList",
				new ArrayList<Course>(mCourses));
		// System.out.println("Saving state");

		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(savedInstanceState);
	}

	private void retrieveCourse() {
		CalendarClientInterface calendarClient = new CalendarClient(this, this);
		calendarClient.getISAInformations();
	}

	private void retrieveCourseInfo(List<Course> coursesList) {

		ConstructListCourse constructCourse = ConstructListCourse
				.getInstance(this);
		constructCourse.completeCourse(coursesList, this);

	}

	public void callbackAppEngine(List<Course> coursesList) {

		ArrayList<Map<String, String>> blockListAdapter = new ArrayList<Map<String, String>>();
		ArrayList<Block> blockList = constructBlockList(coursesList);
		
		for (Block b : blockList) {
			Map<String, String> blockMap = new HashMap<String, String>();

			blockMap.put("Block name", b.getCourse().getName());
			blockMap.put("Remaining credits", b.creditsToString());

			blockListAdapter.add(blockMap);
		}

		final List<Map<String, String>> finalBlockList = blockListAdapter;

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, finalBlockList,
				android.R.layout.simple_list_item_2, new String[] {
					"Block name", "Remaining credits" }, new int[] {
						android.R.id.text1, android.R.id.text2 });

		mListView.setAdapter(simpleAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				intent.putExtra("courseName", finalBlockList.get(position).get("Block name"));
				startActivity(intent);
			}

		});

		if (simpleAdapter.isEmpty()) {
			mGreeter.setText(getString(R.string.greeter_no_more_blocks));
		}
	}

	private ArrayList<Block> constructBlockList(List<Course> courseList) {
		ArrayList<Block> list = new ArrayList<Block>();
		for (Course c : courseList) {
			list.add(new Block(c, c.getCredits()));
		}
		return list;
	}

	/**
	 * FIXME : NOT CLEAN TO DO THIS WAY
	 */
	private void switchToAuthenticationActivity() {
		Intent displayAuthenticationActivtyIntent = new Intent(this,
				AuthenticationActivity.class);
		this.startActivityForResult(displayAuthenticationActivtyIntent,
				AUTH_ACTIVITY_CODE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == AUTH_ACTIVITY_CODE && resultCode == RESULT_OK) {
			this.mCourses = new ArrayList<Course>();
			retrieveCourse();
		}
		super.onActivityResult(requestCode, resultCode, data);
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