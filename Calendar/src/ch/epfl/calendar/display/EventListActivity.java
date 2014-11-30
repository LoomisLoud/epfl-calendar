package ch.epfl.calendar.display;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ch.epfl.calendar.App;
import ch.epfl.calendar.R;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.EventForList;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.data.PeriodType;
import ch.epfl.calendar.persistence.DBQuester;

/**
 * @author MatthiasLeroyEPFL
 * 
 */
public class EventListActivity extends Activity {

    private ListView mListView;
    private DBQuester mDbQuester;
    private Context context = this;
    private static final int HEIGHT_DIVIDER = 10;
    private ArrayAdapter<EventForList> adapter = null;
    private boolean editEvent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        mListView = (ListView) findViewById(R.id.list_event_view);

        mDbQuester = new DBQuester();
        List<Course> course = mDbQuester.getAllCourses();
        List<Event> eventCreated = mDbQuester.getAllEvents();
        final List<EventForList> eventForList = eventToEventForList(course,
                eventCreated);

        adapter = new ArrayAdapter<EventForList>(this,
                android.R.layout.simple_list_item_1, eventForList);

        mListView.setAdapter(adapter);
        mListView.setDividerHeight(HEIGHT_DIVIDER);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                    int position, long arg3) {
                EventForList event = (EventForList) mListView
                        .getItemAtPosition(position);
                if (event.getmId() == DBQuester.NO_ID) {
                    switchToCourseDetails(event.getmEventName());
                } else {
                    switchToEditActivity(mDbQuester.getEvent(event.getmId()));

                }

            }
        });

        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    final int pos, long arg3) {

                final EventForList event = (EventForList) mListView
                        .getItemAtPosition(pos);
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage("What do you want to do ?");

                if (event.getmId() != DBQuester.NO_ID) {
                    dialog.setNegativeButton("Delete", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDbQuester.deleteEvent(mDbQuester.getEvent(event
                                    .getmId()));
                            List<EventForList> list = eventForList;
                            list.remove(event);
                            adapter = new ArrayAdapter<EventForList>(context,
                                    android.R.layout.simple_list_item_1, list);
                            mListView.setAdapter(adapter);

                            dialog.cancel();

                        }
                    });
                }
                dialog.setNeutralButton("Description", new OnClickListener() {

                    /*
                     * (non-Javadoc)
                     * 
                     * @see
                     * android.content.DialogInterface.OnClickListener#onClick
                     * (android.content.DialogInterface, int)
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (event.getmId() == DBQuester.NO_ID) {
                            switchToCourseDetails(event.getmEventName());
                        } else {
                            if (event.getmLinkedCourse().equals(App.NO_COURSE)) {
                                String description = event.getmDescription();
                                switchToEventDetail(event.getmEventName()
                                        + " : " + description);
                            } else {
                                String coursName = event.getmLinkedCourse();
                                switchToCourseDetails(coursName);
                            }
                        }

                        dialog.cancel();

                    }
                });

                dialog.create();
                dialog.show();
                return true;
            }
        });

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

    private List<EventForList> removePastEvents(List<EventForList> list) {
        List<EventForList> result = new ArrayList<EventForList>();
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

    private void switchToEventDetail(String description) {
        Intent eventDetailActivityIntent = new Intent(this,
                EventDetailActivity.class);

        eventDetailActivityIntent.putExtra("description", description);
        startActivity(eventDetailActivityIntent);
    }

    private void switchToEditActivity(Event event) {
        editEvent = true;
        Intent editActivityIntent = new Intent(this, AddEventActivity.class);
        editActivityIntent.putExtra("Id", event.getId());

        startActivity(editActivityIntent);
    }

    private List<EventForList> eventToEventForList(List<Course> cours,
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
                    e.getLinkedCourse(), e.getmDescription()));
        }
        sort(eventForList);
        List<EventForList> result = removePastEvents(eventForList);
        return result;
    }

    @Override
    protected void onResume() {
        if (editEvent) {
            System.out.println("cccccccccccc");
            List<EventForList> updatedEvent = eventToEventForList(
                    mDbQuester.getAllCourses(), mDbQuester.getAllEvents());
            adapter = new ArrayAdapter<EventForList>(context,
                    android.R.layout.simple_list_item_1, updatedEvent);
            mListView.setAdapter(adapter);
            editEvent = false;
        }
        super.onResume();

    }
}
