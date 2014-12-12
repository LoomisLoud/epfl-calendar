package ch.epfl.calendar.test.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;

import android.content.Context;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.display.AppEngineDownloadInterface;
import ch.epfl.calendar.display.AppEngineTask;
import ch.epfl.calendar.display.AppEngineTask.AppEngineListener;
import ch.epfl.calendar.utils.ConstructListCourse;

/**
 * 
 * @author AblionGE
 *
 */
public class ConstructListCourseTest extends ch.epfl.calendar.test.utils.MockTestCase {

    private static final String COURSE_NAME = "Modélisation mathématique et computationnelle en biologie";
    private static final String COURSE_CODE = "BIO-341";
    private static final int COURSE_CREDITS = 4;
    private static final String COURSE_DESCRIPTION = "This course introduces dynamical systems theory for "
            + "modeling simple biological networks. Qualitative analysis of non-linear dynamical models will "
            + "be developed in conjunction with simulations. The focus is on applications to "
            + "biological networks.";
    private static final String COURSE_TEACHER = "Naef";

    private ConstructListCourse instance;
    private AppEngineTask task1;
    private Context context;
    private AppEngineListener listener;
    private AppEngineDownloadInterface appEngineDownloadInterface;
    private List<AppEngineTask> tasks;
    private List<Course> courses;

    public void setUp() {

        instance = Mockito.spy(ConstructListCourse
                .getInstance(appEngineDownloadInterface));

        context = getInstrumentation().getTargetContext();

        listener = Mockito.mock(AppEngineListener.class);

        task1 = new AppEngineTask(context, listener);

        Course cours1 = new Course(COURSE_NAME);

        tasks = new ArrayList<AppEngineTask>();
        courses = new ArrayList<Course>();

        courses.add(cours1);
        

    }

    public void testCallback() throws NoSuchMethodException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {

        Course course1 = new Course(COURSE_CODE, COURSE_NAME,
                COURSE_DESCRIPTION, COURSE_TEACHER, COURSE_CREDITS);
        
        Method callback;
        callback = (ConstructListCourse.class).getDeclaredMethod("callback",
                new Class[] {});

        Method setCourse;
        setCourse = (AppEngineTask.class).getDeclaredMethod("setCourse",
                Course.class);

        Method setCourses;
        setCourses = (ConstructListCourse.class).getDeclaredMethod("setCourses",
               ArrayList.class);

        Method setTasks;
        setTasks = (ConstructListCourse.class).getDeclaredMethod("setTasks",
                ArrayList.class);
        
        callback.setAccessible(true);
        setCourse.setAccessible(true);
        setCourses.setAccessible(true);
        setTasks.setAccessible(true);

        setCourse.invoke(task1, course1);
        
        tasks.add(task1);
        setTasks.invoke(instance, tasks);
       
        setCourses.invoke(instance, courses);

        callback.invoke(instance, new Object[] {});

        assertEquals(COURSE_CREDITS, instance.getCourses().get(0).getCredits());
        assertEquals(COURSE_TEACHER, instance.getCourses().get(0).getTeacher());
        assertEquals(COURSE_DESCRIPTION, instance.getCourses().get(0)
                .getDescription());

    }
    
    public void testCallbackWithCourseNull() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Course course1 = null;
        
        Method callback;
        callback = (ConstructListCourse.class).getDeclaredMethod("callback",
                new Class[] {});

        Method setCourse;
        setCourse = (AppEngineTask.class).getDeclaredMethod("setCourse",
                Course.class);

        Method setCourses;
        setCourses = (ConstructListCourse.class).getDeclaredMethod("setCourses",
               ArrayList.class);

        Method setTasks;
        setTasks = (ConstructListCourse.class).getDeclaredMethod("setTasks",
                ArrayList.class);
        
        callback.setAccessible(true);
        setCourse.setAccessible(true);
        setCourses.setAccessible(true);
        setTasks.setAccessible(true);

        setCourse.invoke(task1, course1);
        
        tasks.add(task1);
        setTasks.invoke(instance, tasks);
       
        setCourses.invoke(instance, courses);

        callback.invoke(instance, new Object[] {});

        assertEquals(0, instance.getCourses().get(0).getCredits());
        assertEquals("Can't find a teacher", instance.getCourses().get(0).getTeacher());
        assertEquals("No description", instance.getCourses().get(0)
                .getDescription());
    }
}
