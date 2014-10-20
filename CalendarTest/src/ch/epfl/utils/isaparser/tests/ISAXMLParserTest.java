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
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import ch.epfl.calendar.data.Course;
import ch.epfl.utils.isaparser.ISAXMLParser;

import android.util.Xml;

/**
 * @author AblionGE
 *
 */
public class ISAXMLParserTest extends TestCase {
    
    private Method skip;
    private Method readText;
    private Method readName;
    private Method readType;
    private Method readRoom;
    private Method readCourse;
    private Method readStudyPeriod;
    private Method readData;
    private InputStream xmlEndTag;
    private InputStream standardXml;
    private InputStream text;
    private InputStream standardReadName;
    private InputStream standardReadNameOther;
    private InputStream standardReadType;
    private InputStream standardReadTypeOther;
    private InputStream standardReadRoom;
    private InputStream standardReadRoomOther;
    private InputStream standardReadCourse;
    private InputStream standardReadCourseOther;
    private InputStream standardReadStudyPeriod;
    private InputStream standardReadStudyPeriodNull;
    private InputStream standardReadData;
    private InputStream standardReadDataNull;
    private InputStream standardInput;
    
    @Before
    public void setUp() throws NoSuchMethodException, UnsupportedEncodingException {
        skip = (ISAXMLParser.class).getDeclaredMethod("skip", new Class[] {XmlPullParser.class});
        skip.setAccessible(true);
        readText = (ISAXMLParser.class).getDeclaredMethod("readText", new Class[] {XmlPullParser.class});
        readText.setAccessible(true);
        readName = (ISAXMLParser.class).getDeclaredMethod("readName", new Class[] {XmlPullParser.class});
        readName.setAccessible(true);
        readType = (ISAXMLParser.class).getDeclaredMethod("readType", new Class[] {XmlPullParser.class});
        readType.setAccessible(true);
        readRoom = (ISAXMLParser.class).getDeclaredMethod("readRoom", new Class[] {XmlPullParser.class});
        readRoom.setAccessible(true);
        readCourse = (ISAXMLParser.class).getDeclaredMethod("readCourse", new Class[] {XmlPullParser.class});
        readCourse.setAccessible(true);
        readStudyPeriod = (ISAXMLParser.class).getDeclaredMethod("readStudyPeriod", new Class[] {XmlPullParser.class});
        readStudyPeriod.setAccessible(true);
        readData = (ISAXMLParser.class).getDeclaredMethod("readData", new Class[] {XmlPullParser.class});
        readData.setAccessible(true);
        xmlEndTag = new ByteArrayInputStream("<test></test><test></test>".getBytes("UTF-8"));
        standardXml = new ByteArrayInputStream("<test><default>bla bla</default></test>".getBytes("UTF-8"));
        standardReadName = new ByteArrayInputStream("<name><text>bla bla</text></name>".getBytes("UTF-8"));
        standardReadNameOther = new ByteArrayInputStream("<name><other>bla bla</other></name>".getBytes("UTF-8"));
        standardReadType = new ByteArrayInputStream("<type><text>bla bla</text></type>".getBytes("UTF-8"));
        standardReadTypeOther = new ByteArrayInputStream("<type><other>bla bla</other></type>".getBytes("UTF-8"));
        standardReadRoom = new ByteArrayInputStream("<room><name><text>bla bla</text></name></room>".getBytes("UTF-8"));
        standardReadRoomOther = 
                new ByteArrayInputStream("<room><other>bla bla</other></room>".getBytes("UTF-8"));
        standardReadCourse = 
                new ByteArrayInputStream("<course><name><text>bla bla</text></name></course>".getBytes("UTF-8"));
        standardReadCourseOther = 
                new ByteArrayInputStream("<course><other>bla bla</other></course>".getBytes("UTF-8"));
        standardReadStudyPeriod = 
                new ByteArrayInputStream(("<study-period><id>1808047617</id><date>13.10.2014</date>"
                        + "<duration>105</duration><day>1</day><startTime>14:15</startTime><endTime>16:00</endTime>"
                        + "<type><text lang=\"en\">Lecture</text><text lang=\"fr\">Cours</text></type> "
                        + "+ <course><id>2258712</id><name><text lang=\"fr\">Algorithms</text></name></course>"
                        + "<room><id>2192131</id><code>CO2</code><name><text lang=\"fr\">CO 2</text>"
                        + "</name></room></study-period>").getBytes("UTF-8"));
        standardReadStudyPeriodNull = new ByteArrayInputStream("<study-period></study-period>".getBytes("UTF-8"));
        standardReadData = 
                new ByteArrayInputStream(("<data><study-period><id>1808047617</id><date>13.10.2014</date>"
                        + "<duration>105</duration><day>1</day><startTime>14:15</startTime><endTime>16:00</endTime>"
                        + "<type><text lang=\"en\">Lecture</text><text lang=\"fr\">Cours</text></type> "
                        + "+ <course><id>2258712</id><name><text lang=\"fr\">Algorithms</text></name></course>"
                        + "<room><id>2192131</id><code>CO2</code><name><text lang=\"fr\">CO 2</text>"
                        + "</name></room></study-period></data>").getBytes("UTF-8"));
        standardReadDataNull = new ByteArrayInputStream("<data></data>".getBytes("UTF-8"));
        standardInput = new ByteArrayInputStream(("<data status=\"Termine\" date=\"20141017 16:08:36\""
                + " key=\"1864682915\" dateFin=\"19.10.2014\" dateDebut=\"13.10.2014\"><study-period>"
                + "<id>1808047617</id><date>13.10.2014</date>"
                + "<duration>105</duration><day>1</day><startTime>14:15</startTime><endTime>16:00</endTime>"
                + "<type><text lang=\"en\">Lecture</text><text lang=\"fr\">Cours</text></type> "
                + "+ <course><id>2258712</id><name><text lang=\"fr\">Algorithms</text></name></course>"
                + "<room><id>2192131</id><code>CO2</code><name><text lang=\"fr\">CO 2</text>"
                + "</name></room></study-period><study-period><id>1808048631</id><date>13.10.2014</date>"
                + "<duration>105</duration><day>1</day><startTime>16:15</startTime><endTime>18:00</endTime>"
                + "<type><text lang=\"en\">Exercises</text><text lang=\"fr\">Exercices</text></type>"
                + "<course><id>2258712</id><name><text lang=\"fr\">Algorithms</text></name></course>"
                + "<room><id>2189182</id><code>GCB331</code><name><text lang=\"fr\">GC B3 31</text></name></room>"
                + "<room><id>2189101</id><code>GCA331</code><name><text lang=\"fr\">GC A3 31</text></name></room>"
                + "<room><id>1614950371</id><code>GCD0386</code><name><text lang=\"fr\">GC D0 386</text></name></room>"
                + "<room><id>2189114</id><code>GCB330</code><name><text lang=\"fr\">GC B3 30</text></name></room>"
                + "</study-period><study-period><id>1808331964</id><date>14.10.2014</date><duration>105</duration>"
                + "<day>2</day><startTime>08:15</startTime><endTime>10:00</endTime><type><text lang=\"en\">"
                + "Lecture</text><text lang=\"fr\">Cours</text></type><course><id>24092923</id><name>"
                + "<text lang=\"fr\">Software engineering</text></name></course><room><id>4255362</id>"
                + "<code>BC02</code><name><text lang=\"fr\">BC 02</text></name></room><room><id>4255327</id>"
                + "<code>BC01</code><name><text lang=\"fr\">BC 01</text></name></room><room><id>4255386</id>"
                + "<code>BC03</code><name><text lang=\"fr\">BC 03</text></name></room><room><id>4255408</id>"
                + "<code>BC04</code><name><text lang=\"fr\">BC 04</text></name></room></study-period>"
                + "</data>").getBytes("UTF-8"));
        text = new ByteArrayInputStream("<start>bla</start>".getBytes("UTF-8"));
    }
    
    @Test
    public void testParse() {
        //With null argument
        try {
            ISAXMLParser.parse(null);
            fail("prase : Fail test null pointer");
        } catch (NullPointerException e) {
            //waited exception
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail("parse : Fail test null pointer - XmlPullParserException");
        } catch (IOException e) {
            e.printStackTrace();
            fail("parse : Fail test null pointer - IOException");
        }
    }
    
    @Test
    public void testReadData() {
        XmlPullParser parser = Xml.newPullParser();
      //With Null argument
        try {
            readData.invoke(null, new Object[] {null});
            fail("readData : Fail test null pointer");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("readData : Fail test null pointer - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("readData : Fail test null pointer - Illegal Argument Exception");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof NullPointerException) {
                //waited exception
            } else {
                e.printStackTrace();
                fail("readData : Fail test null pointer - Wrong TargetException");
            }
        }
        
        //With null arguments
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadDataNull, null);
            parser.nextTag();
            @SuppressWarnings("unchecked")
            List<Course> courses = (List<Course>) readData.invoke(null, new Object[] {parser});
            assertEquals(0, courses.size());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("readData : Fail test standard input - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("readData : Fail test standard input - Illegal Argument Exception");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("readData : Fail test standard input - Wrong TargetException");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail("readData : Fail test standard input - XmlPullParserException");
        } catch (IOException e) {
            e.printStackTrace();
            fail("readData : Fail test standard input - Wrong TargetException");
        }
        
        //With standard input with 1 Course
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadData, null);
            parser.nextTag();
            @SuppressWarnings("unchecked")
            List<Course> courses = (List<Course>) readData.invoke(null, new Object[] {parser});
            assertEquals(1, courses.size());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("readData : Fail test standard input - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("readData : Fail test standard input - Illegal Argument Exception");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("readData : Fail test standard input - Wrong TargetException");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail("readData : Fail test standard input - XmlPullParserException");
        } catch (IOException e) {
            e.printStackTrace();
            fail("readData : Fail test standard input - Wrong TargetException");
        }
        
      //With standard input with 2 Course (3 periods)
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardInput, null);
            parser.nextTag();
            @SuppressWarnings("unchecked")
            List<Course> courses = (List<Course>) readData.invoke(null, new Object[] {parser});
            assertEquals(2, courses.size());
            assertEquals(2, courses.get(0).getPeriods().size());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("readData : Fail test standard input - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("readData : Fail test standard input - Illegal Argument Exception");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("readData : Fail test standard input - Wrong TargetException");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail("readData : Fail test standard input - XmlPullParserException");
        } catch (IOException e) {
            e.printStackTrace();
            fail("readData : Fail test standard input - Wrong TargetException");
        }
    }
    
    
    @Test
    public void testReadStudyPeriod() {
        XmlPullParser parser = Xml.newPullParser();
      //With Null argument
        try {
            readStudyPeriod.invoke(null, new Object[] {null});
            fail("readStudyPeriod : Fail test null pointer");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("readStudyPeriod : Fail test null pointer - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("readStudyPeriod : Fail test null pointer - Illegal Argument Exception");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof NullPointerException) {
                //waited exception
            } else {
                e.printStackTrace();
                fail("readStudyPeriod : Fail test null pointer - Wrong TargetException");
            }
        }
        
        //With null arguments
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadStudyPeriodNull, null);
            parser.nextTag();
            Course course = (Course) readStudyPeriod.invoke(null, new Object[] {parser});
            assertEquals(0, course.getCredits());
            assertNull(course.getTeacher());
            assertNull(course.getName());
            assertNotNull(course.getPeriods());
            assertNull(course.getPeriods().get(0).getDate());
            assertNull(course.getPeriods().get(0).getEndTime());
            assertNull(course.getPeriods().get(0).getStartTime());
            assertNull(course.getPeriods().get(0).getType());
            assertNotNull(course.getPeriods().get(0).getRooms());
            assertEquals(0, course.getPeriods().get(0).getRooms().size());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("readStudyPeriod : Fail test standard input - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("readStudyPeriod : Fail test standard input - Illegal Argument Exception");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("readStudyPeriod : Fail test standard input - Wrong TargetException");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail("readStudyPeriod : Fail test standard input - XmlPullParserException");
        } catch (IOException e) {
            e.printStackTrace();
            fail("readStudyPeriod : Fail test standard input - Wrong TargetException");
        }
        
        //With standard input
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadStudyPeriod, null);
            parser.nextTag();
            Course course = (Course) readStudyPeriod.invoke(null, new Object[] {parser});
            assertEquals(0, course.getCredits());
            assertNull(course.getTeacher());
            assertEquals("Algorithms", course.getName());
            assertNotNull(course.getPeriods());
            assertEquals("13.10.2014", course.getPeriods().get(0).getDate());
            assertEquals("14:15", course.getPeriods().get(0).getStartTime());
            assertEquals("16:00", course.getPeriods().get(0).getEndTime());
            assertEquals("Cours", course.getPeriods().get(0).getType());
            assertNotNull(course.getPeriods().get(0).getRooms());
            assertEquals(1, course.getPeriods().get(0).getRooms().size());
            assertEquals("CO 2", course.getPeriods().get(0).getRooms().get(0));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("readStudyPeriod : Fail test standard input - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("readStudyPeriod : Fail test standard input - Illegal Argument Exception");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("readStudyPeriod : Fail test standard input - Wrong TargetException");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail("readStudyPeriod : Fail test standard input - XmlPullParserException");
        } catch (IOException e) {
            e.printStackTrace();
            fail("readStudyPeriod : Fail test standard input - Wrong TargetException");
        }
    }
    
    @Test
    public void testReadCourse() {
        XmlPullParser parser = Xml.newPullParser();
      //With Null argument
        try {
            readCourse.invoke(null, new Object[] {null});
            fail("readCourse : Fail test null pointer");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("readCourse : Fail test null pointer - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("readCourse : Fail test null pointer - Illegal Argument Exception");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof NullPointerException) {
                //waited exception
            } else {
                e.printStackTrace();
                fail("readCourse : Fail test null pointer - Wrong TargetException");
            }
        }
        
        //With standard input
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadCourse, null);
            parser.nextTag();
            assertEquals("bla bla", readCourse.invoke(null, new Object[] {parser}));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("readCourse : Fail test standard input - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("readCourse : Fail test standard input - Illegal Argument Exception");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("readCourse : Fail test standard input - Wrong TargetException");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail("readCourse : Fail test standard input - XmlPullParserException");
        } catch (IOException e) {
            e.printStackTrace();
            fail("readCourse : Fail test standard input - Wrong TargetException");
        }
        
        //Without <name></name>
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadCourseOther, null);
            parser.nextTag();
            assertNull(readCourse.invoke(null, new Object[] {parser}));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("readCourse : Fail test standard input without <name> - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("readCourse : Fail test standard input without <name> - Illegal Argument Exception");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("readCourse : Fail test standard input without <name> - Wrong TargetException");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail("readCourse : Fail test standard input without <name> - XmlPullParserException");
        } catch (IOException e) {
            e.printStackTrace();
            fail("readCourse : Fail test standard input without <name> - Wrong TargetException");
        }
    }
    
    @Test
    public void testReadRoom() {
        XmlPullParser parser = Xml.newPullParser();
      //With Null argument
        try {
            readRoom.invoke(null, new Object[] {null});
            fail("readRoom : Fail test null pointer");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("readRoom : Fail test null pointer - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("readRoom : Fail test null pointer - Illegal Argument Exception");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof NullPointerException) {
                //waited exception
            } else {
                e.printStackTrace();
                fail("readRoom : Fail test null pointer - Wrong TargetException");
            }
        }
        
        //With standard input
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadRoom, null);
            parser.nextTag();
            assertEquals("bla bla", readRoom.invoke(null, new Object[] {parser}));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("readRoom : Fail test standard input - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("readRoom : Fail test standard input - Illegal Argument Exception");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("readRoom : Fail test standard input - Wrong TargetException");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail("readRoom : Fail test standard input - XmlPullParserException");
        } catch (IOException e) {
            e.printStackTrace();
            fail("readRoom : Fail test standard input - Wrong TargetException");
        }
        
        //Without <name></name>
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadRoomOther, null);
            parser.nextTag();
            assertNull(readRoom.invoke(null, new Object[] {parser}));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("readRoom : Fail test standard input without <name> - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("readRoom : Fail test standard input without <name> - Illegal Argument Exception");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("readRoom : Fail test standard input without <name> - Wrong TargetException");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail("readRoom : Fail test standard input without <name> - XmlPullParserException");
        } catch (IOException e) {
            e.printStackTrace();
            fail("readRoom : Fail test standard input without <name> - Wrong TargetException");
        }
    }
    
    @Test
    public void testReadType() {
        XmlPullParser parser = Xml.newPullParser();
      //With Null argument
        try {
            readType.invoke(null, new Object[] {null});
            fail("readType : Fail test null pointer");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("readType : Fail test null pointer - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("readType : Fail test null pointer - Illegal Argument Exception");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof NullPointerException) {
                //waited exception
            } else {
                e.printStackTrace();
                fail("readType : Fail test null pointer - Wrong TargetException");
            }
        }
        
        //With standard input
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadType, null);
            parser.nextTag();
            assertEquals("bla bla", readType.invoke(null, new Object[] {parser}));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("readType : Fail test standard input - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("readType : Fail test standard input - Illegal Argument Exception");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("readType : Fail test standard input - Wrong TargetException");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail("readType : Fail test standard input - XmlPullParserException");
        } catch (IOException e) {
            e.printStackTrace();
            fail("readType : Fail test standard input - Wrong TargetException");
        }
        
        //Without <type></type>
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadTypeOther, null);
            parser.nextTag();
            assertNull(readType.invoke(null, new Object[] {parser}));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("readType : Fail test standard input without <type> - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("readType : Fail test standard input without <type> - Illegal Argument Exception");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("readType : Fail test standard input without <type> - Wrong TargetException");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail("readType : Fail test standard input without <type> - XmlPullParserException");
        } catch (IOException e) {
            e.printStackTrace();
            fail("readType : Fail test standard input without <type> - Wrong TargetException");
        }
    }

    @Test
    public void testReadName() {
        XmlPullParser parser = Xml.newPullParser();
      //With Null argument
        try {
            readName.invoke(null, new Object[] {null});
            fail("readName : Fail test null pointer");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("readName : Fail test null pointer - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("readName : Fail test null pointer - Illegal Argument Exception");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof NullPointerException) {
                //waited exception
            } else {
                e.printStackTrace();
                fail("readName : Fail test null pointer - Wrong TargetException");
            }
        }
        
        //With standard input
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadName, null);
            parser.nextTag();
            assertEquals("bla bla", readName.invoke(null, new Object[] {parser}));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("readName : Fail test standard input - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("readName : Fail test standard input - Illegal Argument Exception");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("readName : Fail test standard input - Wrong TargetException");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail("readName : Fail test standard input - XmlPullParserException");
        } catch (IOException e) {
            e.printStackTrace();
            fail("readName : Fail test standard input - Wrong TargetException");
        }
        
        //Without <text></text>
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadNameOther, null);
            parser.nextTag();
            assertNull(readName.invoke(null, new Object[] {parser}));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("readName : Fail test standard input without <text> - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("readName : Fail test standard input without <text> - Illegal Argument Exception");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("readName : Fail test standard input without <text> - Wrong TargetException");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail("readName : Fail test standard input without <text> - XmlPullParserException");
        } catch (IOException e) {
            e.printStackTrace();
            fail("readName : Fail test standard input without <text> - Wrong TargetException");
        }
    }
    
    @Test
    public void testReadText() {
        //With Null argument
        try {
            readText.invoke(null, new Object[] {null});
            fail("readText : Fail test null pointer");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("readText : Fail test null pointer - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("readText : Fail test null pointer - Illegal Argument Exception");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof NullPointerException) {
                //waited exception
            } else {
                e.printStackTrace();
                fail("readText : Fail test null pointer - IOException");
            }
        }
        
        XmlPullParser parser = Xml.newPullParser();
        //With a TAG
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardXml, null);
            parser.nextTag();
            assertNull(readText.invoke(null, new Object[] {parser}));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("readText : Fail test Tag input - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("readText : Fail test Tag input - IllegalArgumentException");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail("readText : Fail test Tag input - XmlPullParserException");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("readText : Fail test Tag input - Wrong TargetException");
        } catch (IOException e) {
            e.printStackTrace();
            fail("readText : Fail test Tag input - IOException");
        }
        
        //With TEXT
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(text, null);
            parser.nextTag();
            assertEquals("bla", readText.invoke(null, new Object[] {parser}));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("readText : Fail test text input - IllegalAccessException");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("readText : Fail test text input - IllegalArgumentException");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail("readText : Fail test text input - XmlPullParserException");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("readText : Fail test text input - Wrong TargetException");
        } catch (IOException e) {
            e.printStackTrace();
            fail("readText : Fail test text input - IOException");
        }
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
            fail("skip : Fail test null pointer - IllegalArgumentException");
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
            fail("skip : Fail test empty parser - IllegalArgumentException");
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
            fail("skip : Fail test wrong start tag - IllegalArgumentException");
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
            fail("skip : Fail test standard input - IllegalArgumentException");
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
