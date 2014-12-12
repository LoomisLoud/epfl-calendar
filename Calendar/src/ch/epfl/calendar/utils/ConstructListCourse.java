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
public class ConstructListCourse {

    private static ConstructListCourse mConstructCourse;
    private AppEngineTask mTask;
    private ArrayList<AppEngineTask> mTasks;
    private AppEngineDownloadInterface mObjectActivity = null;
    private List<Course> mCourses;
    private int mCountOfCallbackCalls = 0;
    private int mNumberOfCourse = 0;

    /**
     * @param objectActivity
     *            a class implementing {@link AppEngineDownloadInterface}
     * @return the instance of this class
     */
    public static ConstructListCourse getInstance(
            AppEngineDownloadInterface objectActivity) {
        if (mConstructCourse == null) {
            return new ConstructListCourse(objectActivity);
        }
        return mConstructCourse;
    }

    /**
     * Completes courses get from ISA with informations from AppEngine
     * 
     * @param courses
     *            the courses to complete
     * @param context
     *            the context of the {@link Activity} calling this method
     */
    public void completeCourse(List<Course> courses, Context context) {
        setCourses(new ArrayList<Course>());
        setTasks(new ArrayList<AppEngineTask>());
        mNumberOfCourse = courses.size();
        for (Course course : courses) {
            mCourses.add(course);
            mTask = new AppEngineTask(context, new AppEngineHandler());
            mTask.execute(course.getName());
            mTasks.add(mTask);
        }
    }

    public List<Course> getCourses() {
        return mCourses;
    }

    public List<AppEngineTask> getTasks() {
        return mTasks;
    }

    /**
     * Used for the tests
     * 
     * @param tasks
     */
    private void setTasks(ArrayList<AppEngineTask> tasks) {
        mTasks = tasks;
    }

    /**
     * Used for the tests
     * 
     * @param courses
     */
    private void setCourses(ArrayList<Course> courses) {
        mCourses = courses;
    }

    public ConstructListCourse(AppEngineDownloadInterface objectActivity) {
        mObjectActivity = objectActivity;
        mTasks = new ArrayList<AppEngineTask>();
        mCourses = new ArrayList<Course>();
    }

    private void callback() {
        mCountOfCallbackCalls = mCountOfCallbackCalls + 1;
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
            if (mNumberOfCourse == mCountOfCallbackCalls
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
