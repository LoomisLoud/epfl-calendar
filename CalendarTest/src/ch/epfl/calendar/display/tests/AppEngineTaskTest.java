package ch.epfl.calendar.display.tests;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.mockito.Mockito;

import android.content.Context;
import ch.epfl.calendar.apiInterface.AppEngineDatabaseInterface;
import ch.epfl.calendar.apiInterface.CalendarClientException;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.display.AppEngineTask;
import ch.epfl.calendar.display.AppEngineTask.AppEngineListener;
import ch.epfl.calendar.testing.utils.MockTestCase;

/**
 * AppEngineTaskTest
 * 
 * @author AblionGE
 * 
 */
public class AppEngineTaskTest extends MockTestCase {
    private static final String COURSE_NAME = "Modélisation mathématique et computationnelle en biologie";
    private static final String COURSE_CODE = "BIO-341";
    private static final int COURSE_CREDITS = 4;
    private static final String COURSE_DESCRIPTION = "This course introduces dynamical systems theory for "
            + "modeling simple biological networks. Qualitative analysis of non-linear dynamical models will "
            + "be developed in conjunction with simulations. The focus is on applications to "
            + "biological networks.";
    private static final String COURSE_TEACHER = "Naef";

    private AppEngineListener listener = null;
    private Context context = null;
    private AppEngineTask instance = null;
    private AppEngineDatabaseInterface appEngineInterface;

    public void setUp() throws CalendarClientException {

        listener = Mockito.mock(AppEngineListener.class);
        context = getInstrumentation().getTargetContext();
        instance = Mockito.spy(new AppEngineTask(context, listener));
        appEngineInterface = Mockito.mock(AppEngineDatabaseInterface.class);

    }

    public void testDoInBackGround() throws NoSuchMethodException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        Course returnedCourse = null;

        Method doInBackground;
        doInBackground = (AppEngineTask.class).getDeclaredMethod(
                "doInBackground", String[].class);
        doInBackground.setAccessible(true);
        returnedCourse = (Course) doInBackground.invoke(instance,
                new Object[] { new String[] { COURSE_NAME } });

        assertEquals(COURSE_NAME, returnedCourse.getName());
        assertEquals(COURSE_CODE, returnedCourse.getCode());
        assertEquals(COURSE_CREDITS, returnedCourse.getCredits());
        assertEquals(COURSE_DESCRIPTION, returnedCourse.getDescription());
        assertEquals(COURSE_TEACHER, returnedCourse.getTeacher());
    }

    public void testRetrieveCourse() throws NoSuchMethodException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {

        Method retrieveCourse;
        retrieveCourse = (AppEngineTask.class).getDeclaredMethod(
                "retrieveCourse", new Class[] {
                    String.class
                });
        retrieveCourse.setAccessible(true);
        Course returnedCourse = (Course) retrieveCourse.invoke(instance,
                    COURSE_NAME);
        
        assertEquals(COURSE_NAME, returnedCourse.getName());
    }

    public void testRetrieveCourseWithException() throws NoSuchMethodException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, CalendarClientException {

        Course returnedCourse = null;

        Method retrieveCourse;
        retrieveCourse = (AppEngineTask.class).getDeclaredMethod(
                "retrieveCourse", new Class[] {
                    String.class
                });
        retrieveCourse.setAccessible(true);
        Mockito.doReturn(appEngineInterface).when(instance)
                .getAppEngineClient();
        Mockito.doThrow(new CalendarClientException()).when(appEngineInterface)
                .getCourseByName(COURSE_NAME);
        returnedCourse = (Course) retrieveCourse.invoke(instance, COURSE_NAME);
        assertEquals(COURSE_NAME, returnedCourse.getName());
    }

    public void testOnPostExecuteWhenError() throws NoSuchMethodException,
            CalendarClientException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        Method retrieveCourse;
        retrieveCourse = (AppEngineTask.class).getDeclaredMethod(
                "retrieveCourse", new Class[] {
                    String.class
                });
        retrieveCourse.setAccessible(true);
        Mockito.doReturn(appEngineInterface).when(instance)
                .getAppEngineClient();
        Mockito.doThrow(new CalendarClientException()).when(appEngineInterface)
                .getCourseByName(COURSE_NAME);
        Course returnedCourse = (Course) retrieveCourse.invoke(instance,
                COURSE_NAME);

        Method onPostExecute;
        onPostExecute = (AppEngineTask.class).getDeclaredMethod(
                "onPostExecute", new Class[] {
                    Course.class
                });
        onPostExecute.setAccessible(true);

        Mockito.doNothing()
                .when(listener)
                .onError(context,
                        "Can't retrieve : " + returnedCourse.getName());

        onPostExecute.invoke(instance, returnedCourse);
    }

    public void testOnPostExecuteWhenSuccess() throws NoSuchMethodException,
            CalendarClientException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        Method retrieveCourse;
        retrieveCourse = (AppEngineTask.class).getDeclaredMethod(
                "retrieveCourse", new Class[] {
                    String.class
                });
        retrieveCourse.setAccessible(true);
        Course returnedCourse = (Course) retrieveCourse.invoke(instance,
                COURSE_NAME);

        Method onPostExecute;
        onPostExecute = (AppEngineTask.class).getDeclaredMethod(
                "onPostExecute", new Class[] {
                    Course.class
                });
        onPostExecute.setAccessible(true);

        Mockito.doNothing()
                .when(listener)
                .onSuccess();

        onPostExecute.invoke(instance, returnedCourse);
    }
}
