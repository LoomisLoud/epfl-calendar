package ch.epfl.calendar.display;

import java.util.ArrayList;
import java.util.Calendar;
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
import ch.epfl.calendar.apiInterface.UpdateDataFromDBInterface;
import ch.epfl.calendar.data.Block;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.persistence.DBQuester;

/**
 * @author LoomisLoud
 * 
 */
public class AddBlocksActivity extends DefaultActionBarActivity implements
        UpdateDataFromDBInterface {

    public static final int AUTH_ACTIVITY_CODE = 1;
    public static final int BLOCK_ACTIVITY_CODE = 2;
    private static final int NUMBER_OF_DAYS = 7;
    private ListView mListView;
    private List<Course> mCourses = new ArrayList<Course>();
    private ArrayList<Block> blockList;
    private List<Period> periodList = new ArrayList<Period>();
    private TextView mGreeter;
    private Intent intentToEventCreation;
    private SimpleAdapter simpleAdapter;
    private final ArrayList<Map<String, String>> blockListAdapter = new ArrayList<Map<String, String>>();
    private DBQuester mDB;

    private void addEventActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("List of Blocks");
    }

    private void updateDataSet() {
        blockListAdapter.clear();
        for (Block b : blockList) {
            if (b.getRemainingCredits() > 0.00) {
                Map<String, String> blockMap = new HashMap<String, String>();
                ArrayList<Period> periods = new ArrayList<Period>(b.getCourse()
                        .getPeriods());

                blockMap.put("Block name", b.getCourse().getName());
                blockMap.put("Remaining credits", b.creditsToString());

                periodList.add(periods.get(periods.size() - 1));

                blockListAdapter.add(blockMap);
            }
        }
    }

    private void createAdapterAndListView() {
        updateDataSet();

        simpleAdapter = new SimpleAdapter(
                this,
                blockListAdapter,
                android.R.layout.simple_list_item_2,
                new String[] {
                    "Block name",
                    "Remaining credits"
                },
                new int[] {
                    android.R.id.text1, android.R.id.text2
                });

        mListView.setAdapter(simpleAdapter);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                intentToEventCreation.putExtra("position", position);
                intentToEventCreation.putExtra("courseName", blockListAdapter
                        .get(position).get("Block name"));
                intentToEventCreation.putExtra("period",
                        periodList.get(position));
                startActivityForResult(intentToEventCreation,
                        BLOCK_ACTIVITY_CODE);
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

    private void updateCredits(Intent data) {
        if (data.hasExtra("courseName")) {
            final String course = data.getStringExtra("courseName");
            final int position = data.getIntExtra("position", -1);
            final ArrayList<Event> eventList = new ArrayList<Event>(
                    mDB.getAllEventsFromCourseBlock(course));

            if (!eventList.isEmpty()) {
                double timeToRemove = 0;
                Calendar today = Calendar.getInstance();
                Calendar nextWeek = Calendar.getInstance();
                nextWeek.add(Calendar.DAY_OF_MONTH, NUMBER_OF_DAYS);
                for (Event e : eventList) {
                    if (e.getEndDate().compareTo(today) > 0
                            && e.getStartDate().compareTo(nextWeek) < 0) {
                        timeToRemove += e.getHours();
                    }
                }
                final Block currentBlock = blockList.get(position);
                blockList.get(position).setRemainingCredits(
                        currentBlock.getRemainingCredits() - timeToRemove);
                updateDataSet();
                simpleAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateCreditsOnMain() {
        // TODO for each course, get its eventslist, pick only this week's
        // eventlist,
        final ArrayList<Event> eventList = new ArrayList<Event>();
        final Calendar today = Calendar.getInstance();
        final Calendar nextWeek = Calendar.getInstance();
        nextWeek.add(Calendar.DAY_OF_MONTH, NUMBER_OF_DAYS);
        for (int position = 0; position < blockList.size(); position++) {
            eventList.clear();
            eventList.addAll(mDB.getAllEventsFromCourseBlock(blockList
                    .get(position).getCourse().getName()));
            Block currentBlock = blockList.get(position);
            double timeToRemove = 0;
            if (!eventList.isEmpty()) {
                for (Event e : eventList) {
                    if (e.getEndDate().compareTo(today) > 0
                            && e.getStartDate().compareTo(nextWeek) < 0) {
                        timeToRemove += e.getHours();
                    }
                }
                blockList.get(position).setRemainingCredits(
                        currentBlock.getRemainingCredits() - timeToRemove);
            }
        }
        updateDataSet();
        simpleAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setUdpateData(this);

        setContentView(R.layout.activity_add_blocks);
        addEventActionBar();

        mDB = new DBQuester();
        intentToEventCreation = new Intent(this, AddEventBlockActivity.class);

        mGreeter = (TextView) findViewById(R.id.greeter);
        mListView = (ListView) findViewById(R.id.credits_blocks_list);

        updateData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BLOCK_ACTIVITY_CODE && resultCode == RESULT_OK) {
            updateCredits(data);
        }
    }

    @Override
    public void updateData() {
        mCourses = mDB.getAllCourses();
        blockList = constructBlockList(mCourses);
        createAdapterAndListView();
        updateCreditsOnMain();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean retour = super.onCreateOptionsMenu(menu);
        MenuItem addEventBlockItem = (MenuItem) menu
                .findItem(R.id.add_event_block);
        addEventBlockItem.setVisible(false);
        this.invalidateOptionsMenu();
        return retour;
    }
}
