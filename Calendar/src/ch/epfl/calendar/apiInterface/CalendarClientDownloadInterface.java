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
     * 
     * @param success true if the download on ISA was successfull, false otherwise.
     * @param courses The list of Course returned by ISA.
     */
    void callbackDownload(boolean success, List<Course> courses);
    
    /**
     * This function allows to logout the current user
     * @param isLogoutDoneByUser indicates if the the user is logged out intentionally or not
     */
    void logout(boolean isLogoutDoneByUser);
}
