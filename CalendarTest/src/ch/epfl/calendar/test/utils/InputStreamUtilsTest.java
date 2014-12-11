/**
 * 
 */
package ch.epfl.calendar.test.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;
import ch.epfl.calendar.utils.InputStreamUtils;

/**
 * @author gilbrechbuhler
 * 
 */
public class InputStreamUtilsTest extends TestCase {

    private static final String TEST_STRING = "test";
    private static final String UTF8_ENCODING = "UTF-8";
    private static final String ISO_8859_1_ENCODING = "ISO-8859-1";
    private static final String BAD_ENCODING = "This is a bad encoding name";

    /**
     * Tests {@link InputStreamUtils.readInputStream}
     */
    public void testReadInputStreamWorking()
        throws UnsupportedEncodingException, IOException {
        InputStream is1 = new ByteArrayInputStream(TEST_STRING.getBytes(UTF8_ENCODING));
        InputStream is2 = new ByteArrayInputStream(TEST_STRING.getBytes(ISO_8859_1_ENCODING));

        String returnedStringUTF8 = InputStreamUtils.readInputStream(is1, UTF8_ENCODING);
        String returnedStringISO = InputStreamUtils.readInputStream(is2, ISO_8859_1_ENCODING);

        assertEquals(TEST_STRING, returnedStringUTF8);
        assertEquals(TEST_STRING, returnedStringISO);
    }

    public void testReadInputStreamBadEncoding() throws IOException {
        InputStream is1;
        try {
            is1 = new ByteArrayInputStream(TEST_STRING.getBytes(UTF8_ENCODING));
            InputStreamUtils.readInputStream(is1, BAD_ENCODING);

            fail("Exception for bad encoding should be raised.");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        try {
            is1 = new ByteArrayInputStream(TEST_STRING.getBytes(UTF8_ENCODING));
            InputStreamUtils.readInputStream(is1, "");

            fail("Exception for bad encoding should be raised.");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void testReadInputStreamNullInputStream() {
        try {
            InputStreamUtils.readInputStream(null, UTF8_ENCODING);
            fail("Exception for bad IO should be raised");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void testReadInputStreamNullEncoding() {
        try {
            InputStream is = new ByteArrayInputStream(TEST_STRING.getBytes(UTF8_ENCODING));
            InputStreamUtils.readInputStream(is, null);
            
            fail("IOException for null encoding should be raised");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
