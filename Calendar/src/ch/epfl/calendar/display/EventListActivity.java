package ch.epfl.calendar.display;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import ch.epfl.calendar.App;
import ch.epfl.calendar.DefaultActionBarActivity;
import ch.epfl.calendar.R;
import ch.epfl.calendar.apiInterface.UpdateDataFromDBInterface;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.EventForList;
import ch.epfl.calendar.data.EventSeparator;
import ch.epfl.calendar.data.ListViewItem;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.data.PeriodType;
import ch.epfl.calendar.persistence.DBQuester;

/**
 * @author MatthiasLeroyEPFL
 * 
 */
public class EventListActivity extends DefaultActionBarActivity implements
        UpdateDataFromDBInterface {

    private static final int HEIGHT_DIVIDER = 10;
    private static final int SEPARATOR_ID = -2;

    private ListView mListView;
    private Context context = this;
    private boolean editEvent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setUdpateData(this);
        setContentView(R.layout.activity_event_list);
        listEventActionBar();
        CustomAdapter customAdapter = new CustomAdapter(context);
        mListView = (ListView) findViewById(R.id.list_event_view);

        List<Course> course = getDBQuester().getAllCourses();
        List<Event> eventCreated = getDBQuester().getAllEvents();
        final List<ListViewItem> eventForList = eventToEventForList(course,
                eventCreated);

        createAdapter(eventForList, customAdapter);
        mListView.setAdapter(customAdapter);
        mListView.setDividerHeight(HEIGHT_DIVIDER);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                    int position, long arg3) {

                ListViewItem item = (ListViewItem) mListView
                        .getItemAtPosition(position);

                if (item.getmId() != SEPARATOR_ID) {
                    if (item.getmId() == DBQuester.NO_ID) {
                        switchToCourseDetails(item.getmName());
                    } else {
                        editEvent = true;
                        switchToEditActivity(getDBQuester().getEvent(
                                item.getmId()));

                    }
                }

            }
        });

        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    final int pos, long arg3) {

                final ListViewItem item = (ListViewItem) mListView
                        .getItemAtPosition(pos);
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage("What do you want to do ?");

                if (item.getmId() != SEPARATOR_ID) {
                    if (item.getmId() != DBQuester.NO_ID) {
                        dialog.setNegativeButton("Delete",
                                new OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                            int which) {
                                    	Event eventToDelete = getDBQuester().getEvent(
                                                item.getmId());
                                    	if (eventToDelete.isAutomaticAddedBlock()) {
                                    		getDBQuester().deleteBlock(eventToDelete);
                                    	} else {
                                            getDBQuester().deleteEvent(eventToDelete);
                                    	}
                                        List<ListViewItem> list = eventForList;
                                        list.remove(item);
                                        CustomAdapter adap = new CustomAdapter(
                                                context);
                                        createAdapter(list, adap);
                                        mListView.setAdapter(adap);

                                        dialog.cancel();

                                    }
                                });
                        dialog.setPositiveButton("Edit", new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                editEvent = true;
                                switchToEditActivity(getDBQuester().getEvent(
                                        item.getmId()));

                            }
                        });
                    }

                    dialog.setNeutralButton("Description",
                            new OnClickListener() {

                                /*
                                 * (non-Javadoc)
                                 * 
                                 * @see
                                 * android.content.DialogInterface.OnClickListener
                                 * #onClick (android.content.DialogInterface,
                                 * int)
                                 */
                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {

                                    if (item.getmId() == DBQuester.NO_ID) {
                                        switchToCourseDetails(item.getmName());
                                    } else {
                                        if (item.getmLinkedCourse().equals(
                                                App.NO_COURSE)) {
                                            String description = item
                                                    .getmDescription();
                                            switchToEventDetail(
                                                    item.getmName(),
                                                    description);
                                        } else {
                                            String coursName = item
                                                    .getmLinkedCourse();
                                            switchToCourseDetails(coursName);
                                        }
                                    }

                                    dialog.cancel();

                                }
                            });

                    dialog.create();
                    dialog.show();
                }
                return true;
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean retour = super.onCreateOptionsMenu(menu);
        MenuItem eventListItem = (MenuItem) menu
                .findItem(R.id.action_event_list);
        eventListItem.setVisible(false);
        this.invalidateOptionsMenu();
        return retour;
    }

    private void listEventActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("Planning");
    }

    private void sort(List<EventForList> list) {
        Collections.sort(list, new Comparator<EventForList>() {

            @Override
            public int compare(EventForList e1, EventForList e2) {
                long l1 = e1.getmStart().getTimeInMillis();
                long l2 = e2.getmStart().getTimeInMillis();
                int comparator = l1 > l2 ? 1 : (l1 < l2 ? -1 : 0);
                if (comparator == 0) {
                    l1 = e1.getmEnd().getTimeInMillis();
                    l2 = e2.getmEnd().getTimeInMillis();
                    comparator = l1 > l2 ? 1 : (l1 < l2 ? -1 : 0);
                }
                return comparator;
            }
        });
    }

    private PeriodType stringToPeriodType(String type) {
        if (type.equalsIgnoreCase("exercices")
                || type.equalsIgnoreCase("exercises")) {
            return PeriodType.EXERCISES;
        } else if (type.equalsIgnoreCase("cours")
                || type.equalsIgnoreCase("lecture")) {
            return PeriodType.LECTURE;
        } else if (type.equalsIgnoreCase("projet")
                || type.equalsIgnoreCase("project")) {
            return PeriodType.PROJECT;
        } else {
            return PeriodType.DEFAULT;
        }
    }

    private List<ListViewItem> removePastEvents(List<EventForList> list) {
        List<ListViewItem> result = new ArrayList<ListViewItem>();
        Calendar today = Calendar.getInstance();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getmStart().getTimeInMillis() > today
                    .getTimeInMillis()) {
                result.add(list.get(i));

            }
        }
        return result;
    }

    private void switchToCourseDetails(String courseName) {
        Intent courseDetailsActivityIntent = new Intent(context,
                CourseDetailsActivity.class);
        courseDetailsActivityIntent.putExtra("course", courseName);
        startActivity(courseDetailsActivityIntent);
    }

    private List<ListViewItem> eventToEventForList(List<Course> cours,
            List<Event> event) {
        List<EventForList> eventForList = new ArrayList<EventForList>();
        for (Course c : cours) {
            for (Period p : c.getPeriods()) {
                eventForList.add(new EventForList(c.getName(),
                        p.getStartDate(), p.getEndDate(), p.getType(),
                        DBQuester.NO_ID, "", c.getDescription()));
            }
        }
        for (Event e : event) {
            eventForList.add(new EventForList(e.getName(), e.getStartDate(), e
                    .getEndDate(), stringToPeriodType(e.getType()), e.getId(),
                    e.getLinkedCourse(), e.getDescription()));
        }
        sort(eventForList);

        List<ListViewItem> result = removePastEvents(eventForList);

        return result;
    }

    private CustomAdapter createAdapter(List<ListViewItem> eventForList,
            CustomAdapter adapter) {
        adapter.addSectionHeaderItem(new EventSeparator(eventForList.get(0)
                .getmStart()));
        adapter.addItem(eventForList.get(0));
        for (int i = 1; i < eventForList.size(); i++) {
            if (eventForList.get(i).getmStart().get(Calendar.DAY_OF_MONTH) != eventForList
                    .get(i - 1).getmStart().get(Calendar.DAY_OF_MONTH)) {
                adapter.addSectionHeaderItem(new EventSeparator(eventForList
                        .get(i).getmStart()));
            }
            adapter.addItem(eventForList.get(i));
        }
        return adapter;

    }

    @Override
    public void switchToAddEventsActivity() {
        editEvent = true;
        Intent addEventsActivityIntent = new Intent(this,
                AddEventActivity.class);
        startActivity(addEventsActivityIntent);
    }

    public void switchToAddBlockActivity() {
        editEvent = true;
        Intent blockActivityIntent = new Intent(this, AddBlocksActivity.class);
        startActivity(blockActivityIntent);
    }

    @Override
    protected void onResume() {
        if (editEvent) {

            List<ListViewItem> updatedEvent = eventToEventForList(
                    getDBQuester().getAllCourses(), getDBQuester()
                            .getAllEvents());
            CustomAdapter editAdapter = new CustomAdapter(context);
            createAdapter(updatedEvent, editAdapter);
            mListView.setAdapter(editAdapter);

            editEvent = false;

        }
        super.onResume();

    }

    @Override
    public void updateData() {
        onResume();
    }
}
