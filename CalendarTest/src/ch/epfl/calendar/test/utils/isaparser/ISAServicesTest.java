package ch.epfl.calendar.test.utils.isaparser;

import junit.framework.TestCase;
import ch.epfl.calendar.utils.isaparser.ISAServices;
import ch.epfl.calendar.utils.isaparser.ISAServices.Sections;
import ch.epfl.calendar.utils.isaparser.ISAServices.Term;

/**
 * Can be run as a junit test instead of android (not using any android features)
 * @author Enea Bell
 *
 */
public class ISAServicesTest extends TestCase {

    public void testEnumSectionAndAcroynm() {
        Sections section = Sections.INFORMATIQUE;
        //Acronym should be "IN"
        String acronym = "IN";
        assertEquals(acronym, section.getAcronyme());
    }
    
    // Test overriding of toString in enum Term
    public void testEnumTermToString() {
        assertEquals("ETE/", Term.ETE.toString());
    }

    public void testCreateCorrectAddressForPlanINAndTerm() {
        String address = "https://isa.epfl.ch/services/plans/2013-2014/ETE/section/IN";
        String addressConstructed = ISAServices.getStudyPlansOfSection(Sections.INFORMATIQUE, Term.ETE);
        assertEquals("Assert Plan In and Term fail, construct" + addressConstructed, address, addressConstructed);
    }

    public void testCreateCorrectAddressForPlanIN() {
        String address = "https://isa.epfl.ch/services/plans/2013-2014/section/IN";
        String addressConstructed = ISAServices.getStudyPlansOfSection(Sections.INFORMATIQUE, null);
        assertEquals(address, addressConstructed);
    }

    public void testCreateCorrectAddressForCourseBooksINAndTerm() {
        String address = "https://isa.epfl.ch/services/books/2013-2014/ETE/section/IN";
        String addressConstructed = ISAServices.getCourseBookOfSection(Sections.INFORMATIQUE, Term.ETE);
        assertEquals(address, addressConstructed);
    }

    public void testCreateCorrectAddressForCourseBooksIN() {
        String address = "https://isa.epfl.ch/services/books/2013-2014/section/IN";
        String addressConstructed = ISAServices.getCourseBookOfSection(Sections.INFORMATIQUE);
        assertEquals(address, addressConstructed);
    }
}
