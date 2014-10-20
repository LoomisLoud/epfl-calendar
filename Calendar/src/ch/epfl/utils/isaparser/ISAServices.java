package ch.epfl.utils.isaparser;

/**
 * Contains adress of differents services of isa for request.
 * 
 * @author Enea Bell
 *
 */
public class ISAServices {

    private static final String ISA_SERVICES = "https://isa.epfl.ch/services/";
    private static final String YEAR_2013_2014 = "2013-2014/";
    private static final String PLANS = "plans/";
    private static final String COURSE_BOOKS = "books/";
    private static final String  COURSE = "course/";
    private static final String SECTIONS = "section/";
    // sections from http://bachelor.epfl.ch/compare
    /**
     * Enum sections with their acronyme. Added getter to inner class to return acronym of a section's name.
     * @author Enea Bell
     */
    public enum Sections {
        ARCHITECTURE ("AR"), GENIE_CIVILE("GC"), ENVIRONNEMENT("SIE"), CHIMIE("CGC"), MATHEMATIQUE("MA"), 
        PHYSIQUE("PH"), ELECTRONIQUE("EL"), GENIE_MECANIQUE("GM"), MATERIAUX("MX"), MICROTECHNIQUE("MT"),
        INFORMATIQUE("IN"), SYSTEME_COMMUNICATION("SC"), SCIENCE_VIE("STV");
        
        private String mAcronyme;
        Sections(String acronyme) {
            this.mAcronyme = acronyme;
        }
        public String getAcronyme() {
            return mAcronyme;
        }
    }
    
    /**
     * 
     * @author Enea Bell
     *
     */
    public enum Term {
        ETE, HIVER;
        
        @Override
        // add slash for construction web address.
        public String toString() {
            return super.toString() + "/";
        }
    }
    
    /**
     * Overload {@link #getStudyPlansOfSection(Sections, Term)}.
     * @param section
     * @return
     */
    public static String getStudyPlansOfSection(Sections section) {
        return ISAServices.getStudyPlansOfSection(section, null);
    }
    
    /**
     * Template of address : :services/plans/:year/:term/section/:acronym. Year is default one in class (current).
     * @param section Name of Section wanted.
     * @param term If not given, whole year given.
     * @return address to request the study plans of sections given.
     */
    public static String getStudyPlansOfSection(Sections section, Term term) {
        if (term != null) {
            return ISA_SERVICES + PLANS + YEAR_2013_2014 + term.toString() + SECTIONS + section.getAcronyme();
        }
        return ISA_SERVICES + PLANS + YEAR_2013_2014 + SECTIONS + section.getAcronyme();
    }
    
    /**
     * Overload {@link ISAServices#getCourseBookOfSection(Sections, Term)}.
     * @param section
     * @return
     */
    public static String getCourseBookOfSection(Sections section) {
        return ISAServices.getCourseBookOfSection(section, null);
    }
    
    /**
     * Template of address : :services/book/:year/:term/section/:acronym. Year is default one in class (current).
     * @param section Name of Section wanted.
     * @param term If not given, whole year given.
     * @return address to request the course book of sections given.
     */
    public static String getCourseBookOfSection(Sections section, Term term) {
        if (term != null) {
            return ISA_SERVICES + COURSE_BOOKS + YEAR_2013_2014 + term.toString() + SECTIONS + section.getAcronyme();
        }
        return ISA_SERVICES + COURSE_BOOKS + YEAR_2013_2014 + SECTIONS + section.getAcronyme(); 
    }
    
    /**
     * Template of address :  /books/:year/course/:cccccc , cccc = course code.
     * @return
     */
    //TODO ADD ARG CS-106 etc
    public static String getCourseDetailByCourseCode() {
        // TODO add course code.
        return ISA_SERVICES + COURSE_BOOKS + YEAR_2013_2014 + COURSE;
    }
}
