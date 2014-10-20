/**
 * 
 */
package ch.epfl.utils.isaparser.tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import ch.epfl.utils.isaparser.ISAXMLParser;

import android.util.Xml;

/**
 * @author AblionGE
 *
 */
/**
 * @author AblionGE
 *
 */
public class ISAXMLParserTest extends TestCase {
    
    private Method skip;
    private InputStream xmlEndTag;
    private InputStream standardXml;
    
    @SuppressWarnings({ })
    @Before
    public void setUp() throws NoSuchMethodException, UnsupportedEncodingException {
        skip = (ISAXMLParser.class).getDeclaredMethod("skip", new Class[] {XmlPullParser.class});
        skip.setAccessible(true);
        xmlEndTag = new ByteArrayInputStream("<test></test><test></test>".getBytes("UTF-8"));
        standardXml = new ByteArrayInputStream("<test><default>bla bla</default></test>".getBytes("UTF-8"));
    }

    @Test
    public void testSkip() {
        //WithArgumentNull
        try {
            skip.invoke(null, new Object[] {null});
            fail("skip : Fail test null pointer");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("skip : Fail test null pointer - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("skip : Fail test null pointer - Illegal Argument Exception");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof NullPointerException) {
                //waited exception
            } else {
                e.printStackTrace();
                fail("skip : Fail test null pointer - Wrong TargetException");
            }
        }
        
        //With empty parser
        XmlPullParser parser = Xml.newPullParser();
        try {
            skip.invoke(null, new Object[] {parser});
            fail();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("skip : Fail test empty parser - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("skip : Fail test empty parser - Illegal Argument Exception");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof IllegalStateException) {
                //waited exception
            } else {
                e.printStackTrace();
                fail("skip : Fail test empty parser - Wrong TargetException");
            }
        }
        
        //With an IllegalStateException (not START_TAG)
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(xmlEndTag, null);
            //Go to the first tag
            parser.nextTag();
            //Pass through the START_TAG to have an END_TAG
            parser.nextTag();
            skip.invoke(null, parser);
            fail();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("skip : Fail test wrong start tag - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("skip : Fail test wrong start tag - Illegal Argument Exception");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail("skip : Fail test wrong start tag - XmlPullParserException");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof IllegalStateException) {
                //waited exception
            } else {
                e.printStackTrace();
                fail("skip : Fail test wrong start tag - Wrong TargetException");
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("skip : Fail test wrong start tag - IOException");
        }
        
        //With a standard input
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardXml, null);
            parser.nextTag();
            skip.invoke(null, new Object[] {parser});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("skip : Fail test standard input - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("skip : Fail test standard input - Illegal Argument Exception");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail("skip : Fail test standard input - XmlPullParserException");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("skip : Fail test standard input - Wrong TargetException");
        } catch (IOException e) {
            e.printStackTrace();
            fail("skip : Fail test standard input - IOException");
        }
    }

}
