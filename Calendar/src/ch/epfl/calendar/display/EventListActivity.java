package ch.epfl.calendar.display;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

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
 * The list of the events and periods (planning view)
 *
 * @author MatthiasLeroyEPFL
 */
public class EventListActivity extends DefaultActionBarActivity implements
        UpdateDataFromDBInterface {

    private static final int HEIGHT_DIVIDER = 10;
    private static final int SEPARATOR_ID = -2;
    private static final int HOUR_23 = 23;
    private static final int MINUTE_59 = 59;
    private static final int NB_DAY_IN_ONE_MONTH = 31;

    private ListView mListView;
    private Context context = this;
    private List<ListViewItem> mEventForList;
    private boolean editEvent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setUdpateData(this);
        setContentView(R.layout.activity_event_list);
        listEventActionBar();
        mListView = (ListView) findViewById(R.id.list_event_view);

        editEvent = true;
        mEventForList = new ArrayList<ListViewItem>();
        onResume();
        mListView.setDividerHeight(HEIGHT_DIVIDER);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                    int position, long arg3) {

                ListViewItem item = (ListViewItem) mListView
                        .getItemAtPosition(position);

                if (item.getId() != SEPARATOR_ID) {
                    if (item.getId() == DBQuester.NO_ID) {
                        switchToCourseDetails(item.getName());
                    } else {
                        editEvent = true;
                        switchToEditActivity(getDBQuester().getEvent(
                                item.getId()));

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

                if (item.getId() != SEPARATOR_ID) {
                    if (item.getId() != DBQuester.NO_ID) {
                        dialog.setNegativeButton("Delete",
                                new OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                            int which) {
                                        Event eventToDelete = getDBQuester()
                                                .getEvent(item.getId());
                                        if (eventToDelete
                                                .isAutomaticAddedBlock()) {
                                            getDBQuester().deleteBlock(
                                                    eventToDelete);
                                        } else {
                                            getDBQuester().deleteEvent(
                                                    eventToDelete);
                                        }
                                        onResume();
                                        List<ListViewItem> list = mEventForList;
                                        list.remove(item);
                                        CustomAdapter adap = new CustomAdapter(
                                                context);
                                        createAdapter(list, adap);
                                        mListView.setAdapter(adap);
                                        updateData();

                                        dialog.cancel();

                                    }
                                });
                        dialog.setPositiveButton("Edit", new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                editEvent = true;
                                switchToEditActivity(getDBQuester().getEvent(
                                        item.getId()));

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

                                    if (item.getId() == DBQuester.NO_ID) {
                                        switchToCourseDetails(item.getName());
                                    } else {
                                        if (item.getLinkedCourse().equals(
                                                App.NO_COURSE)) {
                                            String description = item
                                                    .getDescription();
                                            switchToEventDetail(item.getName(),
                                                    description);
                                        } else {
                                            String coursName = item
                                                    .getLinkedCourse();
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

    @Override
    public void switchToAddEventsActivity() {
        editEvent = true;
        Intent addEventsActivityIntent = new Intent(this,
                AddEventActivity.class);
        startActivity(addEventsActivityIntent);
    }

    /**
     * Switch to {@link AddEventBlockActivity}
     */
    @Override
	public void switchToAddBlockActivity() {
        editEvent = true;
        Intent blockActivityIntent = new Intent(this, AddBlocksActivity.class);
        startActivity(blockActivityIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (editEvent) {
            mEventForList = eventToEventForList(getDBQuester().getAllCourses(),
                    getDBQuester().getAllEvents());
            CustomAdapter editAdapter = new CustomAdapter(context);
            createAdapter(mEventForList, editAdapter);
            mListView.setAdapter(editAdapter);

            editEvent = false;

        }
    }

    @Override
    public void updateData() {
        editEvent = true;
        onResume();
    }

    private void listEventActionBar() {
        getActionBar().setTitle("Planning");
    }

    private void sort(List<EventForList> list) {
        Collections.sort(list, new Comparator<EventForList>() {

            @Override
            public int compare(EventForList e1, EventForList e2) {
                long l1 = e1.getmStart().getTimeInMillis();
                long l2 = e2.getmStart().getTimeInMillis();
                int comparator = l1 > l2 ? 1 : (l1 < l2 ? -1 : 0);
                if (comparator == 0) {
                    l1 = e1.getEnd().getTimeInMillis();
                    l2 = e2.getEnd().getTimeInMillis();
                    comparator = l1 > l2 ? 1 : (l1 < l2 ? -1 : 0);
                }
                return comparator;
            }
        });
    }

    private List<ListViewItem> removePastEvents(List<EventForList> list) {
        List<ListViewItem> result = new ArrayList<ListViewItem>();
        Calendar today = Calendar.getInstance();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getEnd().getTimeInMillis() > today
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
            addEvent(e, eventForList);
        }
        sort(eventForList);

        List<ListViewItem> result = removePastEvents(eventForList);

        return result;
    }

    private CustomAdapter createAdapter(List<ListViewItem> eventForList,
            CustomAdapter adapter) {
        if (eventForList.size() > 0) {
            adapter.addSectionHeaderItem(new EventSeparator(eventForList.get(0)
                    .getmStart()));
            adapter.addItem(eventForList.get(0));
            for (int i = 1; i < eventForList.size(); i++) {
                if (eventForList.get(i).getmStart().get(Calendar.DAY_OF_MONTH) != eventForList
                        .get(i - 1).getmStart().get(Calendar.DAY_OF_MONTH)) {
                    adapter.addSectionHeaderItem(new EventSeparator(
                            eventForList.get(i).getmStart()));
                }
                adapter.addItem(eventForList.get(i));
            }
        }
        return adapter;

    }

    private void addEvent(Event event, List<EventForList> list) {
        int dayDuration = event.getEndDate().get(Calendar.DAY_OF_MONTH)
                - event.getStartDate().get(Calendar.DAY_OF_MONTH);
        int monthDuration = event.getEndDate().get(Calendar.MONTH)
                - event.getStartDate().get(Calendar.MONTH);
        int yearDuration = event.getEndDate().get(Calendar.YEAR)
                - event.getStartDate().get(Calendar.YEAR);

        if (dayDuration != 0
                && ((monthDuration >= 0 && yearDuration == 0) || (monthDuration <= 0 && yearDuration == 1))) {
            List<Calendar> startList = new ArrayList<Calendar>();
            Calendar start = (Calendar) event.getStartDate().clone();
            startList.add(start);
            if (yearDuration == 1) {
                Calendar cal = new GregorianCalendar();
                cal.set(cal.get(Calendar.YEAR), Calendar.DECEMBER,
                        NB_DAY_IN_ONE_MONTH);
                int nbDaysCurrentYear = cal.get(Calendar.DAY_OF_YEAR)
                        - event.getStartDate().get(Calendar.DAY_OF_YEAR);
                int nbDaysNextYear = event.getEndDate().get(
                        Calendar.DAY_OF_YEAR);
                dayDuration = nbDaysCurrentYear + nbDaysNextYear;
            }
            for (int i = 0; i <= dayDuration; i++) {
                Calendar end = event.getEndDate();
                if (i != dayDuration) {
                    end = (Calendar) startList.get(i).clone();
                    end.set(Calendar.HOUR_OF_DAY, HOUR_23);
                    end.set(Calendar.MINUTE, MINUTE_59);
                }

                list.add(new EventForList(event.getName(), startList.get(i),
                        end, PeriodType.DEFAULT, event.getId(), event
                                .getLinkedCourse(), event.getDescription()));


                Calendar newStart = (Calendar) startList.get(i).clone();
                newStart.add(Calendar.DAY_OF_YEAR, 1);
                newStart.set(Calendar.HOUR_OF_DAY, 0);
                newStart.set(Calendar.MINUTE, 0);
                startList.add(newStart);
            }
        } else {
            list.add(new EventForList(event.getName(), event.getStartDate(),
                    event.getEndDate(), PeriodType.DEFAULT, event.getId(),
                    event.getLinkedCourse(), event.getDescription()));
        }
    }
}
