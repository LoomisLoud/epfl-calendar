/**
 * 
 */
package ch.epfl.calendar.mock;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gilbrechbuhler
 *
 */
public class MockStudent {
    
    private List<MockCourse> mCourses;
    
    public MockStudent() {
        courses = new ArrayList<MockCourse>();
    }

    /**
     * @return the courses
     */
    public List<MockCourse> getCourses() {
        return courses;
    }

    /**
     * @param courses the courses to set
     */
    public void setCourses(List<MockCourse> courses) {
        this.courses = courses;
    }
    
    public void addCourses(MockCourse course) {
        this.courses.add(course);
    }
}
