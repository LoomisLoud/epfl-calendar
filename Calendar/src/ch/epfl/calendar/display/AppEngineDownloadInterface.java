package ch.epfl.calendar.display;

import java.util.List;

import ch.epfl.calendar.data.Course;

/**
 * An interface with a calback method to implement when data from AppEngine is fetched
 * @author Maxime
 *
 */
public interface AppEngineDownloadInterface {
    
    /**
     * The callback method to call after fetching informations from AppEngine.
     * @param mCourses
     */
    void callbackAppEngine(List<Course> mCourses);
}
