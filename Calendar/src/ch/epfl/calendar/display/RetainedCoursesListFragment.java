package ch.epfl.calendar.display;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import ch.epfl.calendar.data.Course;

/**
 * Class to holds the courses list to retain for recreation of activity handling (rotation etc.)
 * @author Enea Bell
 *
 */
public class RetainedCoursesListFragment extends Fragment{
    
    // data object we want to retain
    private List<Course> mCourses;
    
 // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setCourses(List<Course> courses) {
        this.mCourses = new ArrayList<Course>(courses);
    }

    public List<Course> getCourses() {
        return new ArrayList<Course>(mCourses);
    }
    
}
