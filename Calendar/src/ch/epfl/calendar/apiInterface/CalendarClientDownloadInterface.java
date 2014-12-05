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
     * This method is called by the class implementing it when the download from ISA is finished.
     */
    void callbackDownload(boolean success, List<Course> courses);
}
