/**
 * 
 */
package ch.epfl.calendar.apiInterface;

import java.util.List;

import ch.epfl.calendar.mock.MockCourse;
import ch.epfl.calendar.mock.MockStudent;

/**
 * @author gilbrechbuhler
 *
 */
public class CalendarClient implements CalendarClientInterface {

    /* (non-Javadoc)
     * @see ch.epfl.calendar.apiInterface.CalendarClientInterface#login(java.lang.String, java.lang.String)
     */
    @Override
    public void login(String username, String password) {
    }

    /* (non-Javadoc)
     * @see ch.epfl.calendar.apiInterface.CalendarClientInterface#getCoursesFromStudent(ch.epfl.calendar.mock.MockStudent)
     */
    @Override
    public List<MockCourse> getCoursesFromStudent(MockStudent student) {
        MockStudent studentTest = new MockStudent();
        
        MockCourse course1 = new MockCourse("Cours 1", "Pr. Professorson", "Super course 1", "10-12",  10);
        MockCourse course2 = new MockCourse("Cours 2", "Pr. Test", "Super course 2", "14-16", 8);
        MockCourse course3 = new MockCourse("Cours 3", "Pr. Super", "Super course 3", "8-10", 5);
        MockCourse course4 = new MockCourse("Cours 4", "Pr. Testtest", "Super course 4" "8-9", 4);
        MockCourse course5 = new MockCourse("Cours 5", "Pr. SuperTest", "Super course 5", "10-11", 2);
    }

}
