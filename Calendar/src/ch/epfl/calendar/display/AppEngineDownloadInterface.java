package ch.epfl.calendar.display;

import java.util.List;

import ch.epfl.calendar.data.Course;

/**
 * @author Maxime
 *
 */
public interface AppEngineDownloadInterface {
    
    void callbackAppEngine(List<Course> mCourses);
}
