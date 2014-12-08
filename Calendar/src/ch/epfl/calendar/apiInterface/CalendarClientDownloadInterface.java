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
     * The callback is the function called when CalendarClient has finished
     * to get informations online
     * @param success
     * @param courses
     */
    void callbackDownload(boolean success, List<Course> courses);
    
    /**
     * This function allows to logout the current user
     * @param isLogoutDoneByUser indicates if the the user is logged out
     * intentionally or not
     */
    void logout(boolean isLogoutDoneByUser);
}
