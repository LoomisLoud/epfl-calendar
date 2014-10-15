package ch.epfl.calendar;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * 
 * @author lameAppInc
 * 
 */
public class CoursesListActivity extends Activity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_list);

        mListView = (ListView) findViewById(R.id.coursesListView);

        ArrayList<String> coursesListArray = new ArrayList<String>();
        coursesListArray.add("Cours 1");
        coursesListArray.add("Cours 2");
        coursesListArray.add("Cours 3");
        
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, coursesListArray);

        mListView.setAdapter(adapter);
    }
}
