/**
 * 
 */
package ch.epfl.calendar.mock;

/**
 * @author gilbrechbuhler
 *
 */
public class MockCourse {
    private String mTitle;
    private String mProfessor;
    private String mDescription;
    private String mSchedule;
    private int mCredits;
    
    public MockCourse(String title, String professor, String description, String schedule, int credits) {
        mTitle = title;
        mProfessor = professor;
        mDescription = description;
        mCredits = credits;
        mSchedule = schedule;
    }

    /**
     * @return the mTitle
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * @param mTitle the mTitle to set
     */
    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    /**
     * @return the mProfessor
     */
    public String getProfessor() {
        return mProfessor;
    }

    /**
     * @param mProfessor the mProfessor to set
     */
    public void setProfessor(String mProfessor) {
        this.mProfessor = mProfessor;
    }

    /**
     * @return the mDescription
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * @param mDescription the mDescription to set
     */
    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    /**
     * @return the mCredits
     */
    public int getCredits() {
        return mCredits;
    }

    /**
     * @param mCredits the mCredits to set
     */
    public void setCredits(int mCredits) {
        this.mCredits = mCredits;
    }

    /**
     * @return the mSchedule
     */
    public String getSchedule() {
        return mSchedule;
    }

    /**
     * @param mSchedule the mSchedule to set
     */
    public void setSchedule(String mSchedule) {
        this.mSchedule = mSchedule;
    }
    
    
}
