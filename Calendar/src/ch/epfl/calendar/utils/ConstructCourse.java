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
public final class ConstructCourse {

    private static ConstructCourse constructCourse;
    private ArrayList<AppEngineTask> mTasks;
    private AppEngineDownloadInterface mObjectActivity = null;
    private List<Course> mCourses;

    private ConstructCourse(AppEngineDownloadInterface objectActivity) {
        mObjectActivity = objectActivity;
        mTasks = new ArrayList<AppEngineTask>();
        mCourses = new ArrayList<Course>();
    }

    public static ConstructCourse getInstance(
            AppEngineDownloadInterface objectActivity) {
        if (constructCourse == null) {
            return new ConstructCourse(objectActivity);
        }
        return constructCourse;
    }

    public void completeCourse(List<Course> courses, Context context) {
        
        for (Course course : courses) {
            mCourses.add(course);
            AppEngineTask task;
            task = new AppEngineTask(context, new AppEngineHandler());
            task.execute(course.getName());
            mTasks.add(task);
        }
    }

    private void callback() {
        for (AppEngineTask task : mTasks) {
            Course cours = task.getCourse();
            if (cours != null) {
                mCourses.get(mTasks.indexOf(task)).setCredits(cours.getCredits());
                mCourses.get(mTasks.indexOf(task)).setTeacher(cours.getTeacher());
            } else {
                mCourses.get(mTasks.indexOf(task)).setCredits(0);
                mCourses.get(mTasks.indexOf(task)).setTeacher("Can't find a teacher");
            }
            mObjectActivity.callbackAppEngine(mCourses);
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
