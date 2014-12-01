package ch.epfl.calendar.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.Toast;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.display.AppEngineDownloadInterface;
import ch.epfl.calendar.display.AppEngineTask;
import ch.epfl.calendar.display.AppEngineTask.AppEngineListener;

/**
 * This class is to complete a course get from isa by adding the information get
 * from the appEngine. To use it create a ConstructCourse and call
 * completeCourse on the ConstructCourse.
 * 
 * @author Maxime
 * 
 */
public final class ConstructListCourse {

    private static ConstructListCourse constructCourse;
    private ArrayList<AppEngineTask> mTasks;
    private AppEngineDownloadInterface mObjectActivity = null;
    private List<Course> mCourses;
    private int countOfCallbackCalls = 0;
    private int numberOfCourse = 0;

    private ConstructListCourse(AppEngineDownloadInterface objectActivity) {
        mObjectActivity = objectActivity;
        mTasks = new ArrayList<AppEngineTask>();
        mCourses = new ArrayList<Course>();
    }

    public static ConstructListCourse getInstance(
            AppEngineDownloadInterface objectActivity) {
        if (constructCourse == null) {
            return new ConstructListCourse(objectActivity);
        }
        return constructCourse;
    }

    public void completeCourse(List<Course> courses, Context context) {
        mCourses = new ArrayList<Course>();
        numberOfCourse = courses.size();
        for (Course course : courses) {
            mCourses.add(course);
            AppEngineTask task;
            task = new AppEngineTask(context, new AppEngineHandler());
            task.execute(course.getName());
            mTasks.add(task);
        }
    }

    private void callback() {
        countOfCallbackCalls = countOfCallbackCalls + 1;
        int countOfTask = 0;
        for (AppEngineTask task : mTasks) {
            countOfTask = countOfTask + 1;
            Course cours = task.getCourse();
            if (cours != null) {
                mCourses.get(mTasks.indexOf(task)).setCredits(
                        cours.getCredits());
                mCourses.get(mTasks.indexOf(task)).setTeacher(
                        cours.getTeacher());
                mCourses.get(mTasks.indexOf(task)).setDescription(
                        cours.getDescription());
            } else {
                mCourses.get(mTasks.indexOf(task)).setCredits(0);
                mCourses.get(mTasks.indexOf(task)).setTeacher(
                        "Can't find a teacher");
                mCourses.get(mTasks.indexOf(task)).setDescription(
                        "No description");
            }
            if (numberOfCourse == countOfCallbackCalls
                    && countOfTask == mTasks.size()) {
                mObjectActivity.callbackAppEngine(mCourses);
            }
        }
    }

    /**
     * 
     * @author Maxime
     * 
     */
    private class AppEngineHandler implements AppEngineListener {

        @Override
        public void onError(Context context, String msg) {

            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess() {
            callback();
        }

    }
}
