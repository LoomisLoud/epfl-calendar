package ch.epfl.calendar.display.tests;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.mockito.Mockito;

import android.content.Context;
import ch.epfl.calendar.apiInterface.CalendarClientException;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.display.AppEngineTask;
import ch.epfl.calendar.display.AppEngineTask.AppEngineListener;
import ch.epfl.calendar.testing.utils.MockTestCase;

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

    public void setUp() throws CalendarClientException {

        listener = Mockito.mock(AppEngineListener.class);
        context = getInstrumentation().getTargetContext();
        instance = Mockito.spy(new AppEngineTask(context, listener));

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
            InvocationTargetException, CalendarClientException {

        Course returnedCourse = null;

        Method retrieveCourse;
        retrieveCourse = (AppEngineTask.class).getDeclaredMethod(
                "retrieveCourse", new Class[] { String.class });
        retrieveCourse.setAccessible(true);
        try {
            returnedCourse = (Course) retrieveCourse.invoke(instance,
                    COURSE_NAME);

        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof CalendarClientException) {
                if (e.getTargetException().getMessage().equals("errorWaited")) {
                    // Waited
                } else {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                    fail();
                }
            } else {
                fail();
            }
        }
        returnedCourse = (Course) retrieveCourse.invoke(instance, COURSE_NAME);

        assertEquals(COURSE_NAME, returnedCourse.getName());
        assertEquals(COURSE_CODE, returnedCourse.getCode());
        assertEquals(COURSE_CREDITS, returnedCourse.getCredits());
        assertEquals(COURSE_DESCRIPTION, returnedCourse.getDescription());
        assertEquals(COURSE_TEACHER, returnedCourse.getTeacher());

    }
}
