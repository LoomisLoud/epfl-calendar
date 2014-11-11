package ch.epfl.calendar.utils;

import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import ch.epfl.calendar.apiInterface.AppEngineClient;
import ch.epfl.calendar.apiInterface.CalendarClientException;
import ch.epfl.calendar.apiInterface.DatabaseInterface;
import ch.epfl.calendar.data.Course;

/**
 * This class is to complete a course get from isa by adding the information
 * get from the appEngine.
 * Call ConstructCourse.getInstance(yourCourse) to complete it.
 * 
 * @author Maxime
 * 
 */
public final class ConstructCourse {

    private static Course mCourse = null;

    private ConstructCourse(Course course) {
        try {
            mCourse = completeCourse(course);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Course getInstance(Course course) {

        new ConstructCourse(course);

        return mCourse;
    }

    public Course completeCourse(Course course) throws InterruptedException,
            ExecutionException {
        return new DownloadCourseTask().execute(course).get();

    }

    /**
     * @author Maxime
     * 
     */
    private class DownloadCourseTask extends AsyncTask<Course, Void, Course> {

        @Override
        protected Course doInBackground(Course... course) {
            return retrieveCourse(course[0]);
        }

        private Course retrieveCourse(Course course) {
            DatabaseInterface appEngineClient;

            try {
                appEngineClient = new AppEngineClient(
                        "http://versatile-hull-742.appspot.com");
                course.setCredits(appEngineClient.getCourseByName(
                        course.getName()).getCredits());
                course.setTeacher(appEngineClient.getCourseByName(
                        course.getName()).getTeacher());

            } catch (CalendarClientException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return course;
        }
    }
}
