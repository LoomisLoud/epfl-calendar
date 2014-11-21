package ch.epfl.calendar.apiInterface;

import java.util.List;

import ch.epfl.calendar.data.Course;

/**
 * This interface is needed to use AsyncTasks
 * It forces to implement a callback method.
 * @author AblionGE
 *
 */
public interface CalendarClientDownloadInterface {
    /**
     * 
     */
    void callbackDownload(boolean success, List<Course> courses);
}
