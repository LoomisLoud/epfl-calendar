/**
 * 
 */
package ch.epfl.calendar.utils.isaparser.tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.GregorianCalendar;
import java.util.List;

import org.mockito.Mockito;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.PeriodType;
import ch.epfl.calendar.testing.utils.MockTestCase;
import ch.epfl.calendar.utils.isaparser.ISAXMLParser;
import ch.epfl.calendar.utils.isaparser.ParsingException;

import android.util.Xml;

/**
 * ISAXMLParserTest tests the behavior of each private/public methods using
 * introspection.
 * 
 * @author AblionGE
 *
 */
public class ISAXMLParserTest extends MockTestCase {
    
    private static Method skip;
    private static Method readText;
    private static Method readName;
    private static Method readType;
    private static Method readRoom;
    private static Method readCourse;
    private static Method readStudyPeriod;
    private static Method readData;
    private static InputStream xmlEndTag;
    private static InputStream standardXml;
    private static InputStream text;
    private static InputStream standardReadName;
    private static InputStream standardReadNameOther;
    private static InputStream standardReadType;
    private static InputStream standardReadTypeOther;
    private static InputStream standardReadRoom;
    private static InputStream standardReadRoomOther;
    private static InputStream standardReadCourse;
    private static InputStream standardReadCourseOther;
    private static InputStream standardReadStudyPeriod;
    private static InputStream standardReadStudyPeriodNull;
    private static InputStream standardReadStudyPeriodNullWithDates;
    private static InputStream standardReadStudyPeriodNullWithWrongDate;
    private static InputStream standardReadStudyPeriodNullWithWrongHour;
    private static InputStream standardReadData;
    private static InputStream standardReadDataNull;
    private static InputStream standardInput;
    private static final int YEAR = 2014;
    private static final int MONTH = 9;
    private static final int DAY = 13;
    private static final int HOUR_FOURTEEN = 14;
    private static final int HOUR_SIXTEEN = 16;
    private static final int MINUTE_FIFTEEN = 15;
    private static final int MINUTE_ZERO = 00;

    public void setUp() throws NoSuchMethodException, UnsupportedEncodingException {
        skip = (ISAXMLParser.class).getDeclaredMethod("skip", new Class[] {});
        skip.setAccessible(true);
        readText = (ISAXMLParser.class).getDeclaredMethod("readText", new Class[] {});
        readText.setAccessible(true);
        readName = (ISAXMLParser.class).getDeclaredMethod("readName", new Class[] {});
        readName.setAccessible(true);
        readType = (ISAXMLParser.class).getDeclaredMethod("readType", new Class[] {});
        readType.setAccessible(true);
        readRoom = (ISAXMLParser.class).getDeclaredMethod("readRoom", new Class[] {});
        readRoom.setAccessible(true);
        readCourse = (ISAXMLParser.class).getDeclaredMethod("readCourse", new Class[] {});
        readCourse.setAccessible(true);
        readStudyPeriod = (ISAXMLParser.class).getDeclaredMethod("readStudyPeriod", new Class[] {});
        readStudyPeriod.setAccessible(true);
        readData = (ISAXMLParser.class).getDeclaredMethod("readData", new Class[] {});
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
        standardReadStudyPeriodNullWithDates = new ByteArrayInputStream(("<study-period>"
                + "<date>13.10.2014</date><startTime>14:15</startTime>" 
        		+ "<endTime>16:00</endTime></study-period>").getBytes("UTF-8"));
        standardReadStudyPeriodNullWithWrongDate = new ByteArrayInputStream(("<study-period>"
                + "<date>13.10.2014.14</date><startTime>14:15</startTime>" 
        		+ "<endTime>16:00</endTime></study-period>").getBytes("UTF-8"));
        standardReadStudyPeriodNullWithWrongHour = new ByteArrayInputStream(("<study-period>"
                + "<date>13.10.2014</date><startTime>14:15:16</startTime>" 
        		+ "<endTime>16:00</endTime></study-period>").getBytes("UTF-8"));
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

    public void testParseWithNull() throws ParsingException {
        //With null argument
        try {
            new ISAXMLParser().parse(null);
            fail("parse : Fail test null pointer");
        } catch (NullPointerException e) {
            if (e.getMessage().equals("InputStream is null")) {
                //waited exception
            } else {
                fail("testParse wrap wrong NullPointerException");
            }
        }
    }

    public void testParseWithExceptions() throws IOException, XmlPullParserException {
        //Test to have an XmlPullParserException
        try {
            new ISAXMLParser().parse(new ByteArrayInputStream("".getBytes()));
        } catch (ParsingException e) {
            if (e.getMessage().equals("Parsing Error during XML Parsing")) {
                //Waited exception
            } else {
                fail();
            }
        }
        
        //Tests to have an IOException
        XmlPullParser mockParser = Mockito.mock(XmlPullParser.class);
        Mockito.doThrow(new IOException("ERROR")).when(mockParser).nextTag();
        
        InputStream in = new ByteArrayInputStream("<tag>salut</tag>".getBytes());
        try {
            new ISAXMLParser(mockParser).parse(in);
        } catch (ParsingException e) {
            if (e.getMessage().equals("IO Error during XML Parsing (nextTag())")) {
                //Waited exception
            } else {
                fail();
            }
        }
        
        in = Mockito.mock(InputStream.class);
        Mockito.doThrow(new IOException("ERROR")).when(in).close();
        try {
            new ISAXMLParser().parse(in);
        } catch (ParsingException e) {
            if (e.getMessage().equals("Closing IO Error during XML Parsing")) {
                //Waited exception
            } else {
                fail();
            }
        }
    }

    public void testParseWithCorrectInput() {
        List<Course> courses = new ISAXMLParser().parse(standardInput);
        assertEquals(2, courses.size());
    }

    public void testReadData() throws IllegalAccessException, IllegalArgumentException, 
        InvocationTargetException, ParsingException, XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();

      //With Null argument
        try {
            readData.invoke(new ISAXMLParser(), new Object[] {});
            fail("readData : Fail test null pointer");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof NullPointerException) {
                if (e.getTargetException().getMessage().equals("Parser is null")) {
                    //waited exception
                } else {
                    fail("readData wrong NullPointerException");
                }
            }
        }

        //With empty XML
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadDataNull, null);
            parser.nextTag();
            @SuppressWarnings("unchecked")
            List<Course> courses = (List<Course>) readData.invoke(new ISAXMLParser(parser), new Object[] {});
            assertEquals(0, courses.size());
        } finally { }

        //With standard input with 1 Course
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadData, null);
            parser.nextTag();
            @SuppressWarnings("unchecked")
            List<Course> courses = (List<Course>) readData.invoke(new ISAXMLParser(parser), new Object[] {});
            assertEquals(1, courses.size());
        } finally { }

      //With standard input with 2 Course (3 periods)
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardInput, null);
            parser.nextTag();
            @SuppressWarnings("unchecked")
            List<Course> courses = (List<Course>) readData.invoke(new ISAXMLParser(parser), new Object[] {});
            assertEquals(2, courses.size());
            assertEquals(2, courses.get(0).getPeriods().size());
        } finally { }

        //With throwing Exceptions
        //XmlPullParserException
        XmlPullParser mockParser = Mockito.mock(XmlPullParser.class);
        Mockito.doThrow(new XmlPullParserException("ERROR")).when(mockParser).next();

        try {
            readData.invoke(new ISAXMLParser(mockParser), new Object[] {});
            
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof ParsingException) {
                if (e.getTargetException().getMessage().equals("Parsing Error during XML Parsing")) {
                    //waited exception
                } else {
                    fail();
                }
            }
        }

        //IOException
        mockParser = Mockito.mock(XmlPullParser.class);
        Mockito.doThrow(new IOException("ERROR")).when(mockParser).next();
        try {
            readData.invoke(new ISAXMLParser(mockParser), new Object[] {});
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof ParsingException) {
                if (e.getTargetException().getMessage().equals("IO Error during XML Parsing")) {
                    //waited exception
                } else {
                    fail();
                }
            }
        }
    }

    public void testReadStudyPeriod() throws IllegalAccessException, IllegalArgumentException,
        InvocationTargetException, XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        
      //With Null argument
        try {
            readStudyPeriod.invoke(new ISAXMLParser(), new Object[] {});
            fail("readStudyPeriod : Fail test null pointer");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof NullPointerException) {
                if (e.getTargetException().getMessage().equals("Parser is null")) {
                    //waited exception
                } else {
                    fail("readStudyPeriod wrong NullPointerException");
                }
            }
        }
        
        //With null arguments
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadStudyPeriodNull, null);
            parser.nextTag();
            readStudyPeriod.invoke(new ISAXMLParser(parser), new Object[] {});
            fail("It doesn't fail with null Period");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof NullPointerException) {
                if (e.getTargetException().getMessage().equals("Date or Hour is null in createCalendar()")) {
                    //waited exception
                } else {
                    fail("Wrong NullPointerException");
                }
            }
        }
        
        //With null argument but correct date !
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadStudyPeriodNullWithDates, null);
            parser.nextTag();
            Course course = (Course) readStudyPeriod.invoke(new ISAXMLParser(parser), new Object[] {});
            assertEquals(0, course.getCredits());
            assertNull(course.getTeacher());
            assertNull(course.getName());
            assertNotNull(course.getPeriods());
            assertNotNull(course.getPeriods().get(0).getStartDate());
            assertNotNull(course.getPeriods().get(0).getEndDate());
            assertNull(course.getPeriods().get(0).getType());
            assertNotNull(course.getPeriods().get(0).getRooms());
            assertEquals(0, course.getPeriods().get(0).getRooms().size());
        } finally { }
        
      //With null arguments but wrong date
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadStudyPeriodNullWithWrongDate, null);
            parser.nextTag();
            readStudyPeriod.invoke(new ISAXMLParser(parser), new Object[] {});
            fail("It doesn't fail with wrong Date");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof IllegalArgumentException) {
                System.out.println(e.getTargetException().getMessage());
                if (e.getTargetException().getMessage().equals("Parsing date failed")) {
                    //waited exception
                } else {
                    fail("Wrong IllegalArgumentException");
                }
            }
        }
        
      //With null arguments but wrong hour
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadStudyPeriodNullWithWrongHour, null);
            parser.nextTag();
            readStudyPeriod.invoke(new ISAXMLParser(parser), new Object[] {});
            fail("It doesn't fail with wrong Hour");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof IllegalArgumentException) {
                if (e.getTargetException().getMessage().equals("Parsing date failed")) {
                    //waited exception
                } else {
                    fail("Wrong IllegalArgumentException");
                }
            }
        }
        
        //With standard input
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadStudyPeriod, null);
            parser.nextTag();
            Course course = (Course) readStudyPeriod.invoke(new ISAXMLParser(parser), new Object[] {});
            assertEquals(0, course.getCredits());
            assertNull(course.getTeacher());
            assertEquals("Algorithms", course.getName());
            assertNotNull(course.getPeriods());
            //Month is current month - 1
            assertEquals(new GregorianCalendar(YEAR, MONTH, DAY, HOUR_FOURTEEN, MINUTE_FIFTEEN),
            		course.getPeriods().get(0).getStartDate());
            assertEquals(new GregorianCalendar(YEAR, MONTH, DAY, HOUR_SIXTEEN, MINUTE_ZERO), 
            		course.getPeriods().get(0).getEndDate());
            assertEquals(PeriodType.LECTURE, course.getPeriods().get(0).getType());
            assertNotNull(course.getPeriods().get(0).getRooms());
            assertEquals(1, course.getPeriods().get(0).getRooms().size());
            assertEquals("CO 2", course.getPeriods().get(0).getRooms().get(0));
        } finally { }
    }

    public void testReadCourse() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        
      //With Null argument
        try {
            readCourse.invoke(new ISAXMLParser(), new Object[] {});
            fail("readCourse : Fail test null pointer");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof NullPointerException) {
                if (e.getTargetException().getMessage().equals("Parser is null")) {
                    //waited exception
                } else {
                    fail("readCourse wrong NullPointerException");
                }
            }
        }
        
        //With standard input
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadCourse, null);
            parser.nextTag();
            assertEquals("bla bla", readCourse.invoke(new ISAXMLParser(parser), new Object[] {}));
        } finally { }
        
        //Without <name></name>
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadCourseOther, null);
            parser.nextTag();
            assertNull(readCourse.invoke(new ISAXMLParser(parser), new Object[] {}));
        } finally { }
    }

    public void testReadRoom() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
        XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        
      //With Null argument
        try {
            readRoom.invoke(new ISAXMLParser(), new Object[] {});
            fail("readRoom : Fail test null pointer");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof NullPointerException) {
                if (e.getTargetException().getMessage().equals("Parser is null")) {
                    //waited exception
                } else {
                    fail("readRoom wrong NullPointerException");
                }
            }
        }
        
        //With standard input
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadRoom, null);
            parser.nextTag();
            assertEquals("bla bla", readRoom.invoke(new ISAXMLParser(parser), new Object[] {}));
        } finally { }
        
        //Without <name></name>
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadRoomOther, null);
            parser.nextTag();
            assertNull(readRoom.invoke(new ISAXMLParser(parser), new Object[] {}));
        } finally { }
    }

    public void testReadType() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
        XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        
      //With Null argument
        try {
            readType.invoke(new ISAXMLParser(), new Object[] {});
            fail("readType : Fail test null pointer");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof NullPointerException) {
                if (e.getTargetException().getMessage().equals("Parser is null")) {
                    //waited exception
                } else {
                    fail("readType wrong NullPointerException");
                }
            }
        }
        
        //With standard input
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadType, null);
            parser.nextTag();
            assertEquals("bla bla", readType.invoke(new ISAXMLParser(parser), new Object[] {}));
        } finally { }
        
        //Without <type></type>
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadTypeOther, null);
            parser.nextTag();
            assertNull(readType.invoke(new ISAXMLParser(parser), new Object[] {}));
        } finally { }
    }

    public void testReadName() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
    XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        
      //With Null argument
        try {
            readName.invoke(new ISAXMLParser(), new Object[] {});
            fail("readName : Fail test null pointer");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof NullPointerException) {
                if (e.getTargetException().getMessage().equals("Parser is null")) {
                    //waited exception
                } else {
                    fail("readName wrong NullPointerException");
                }
            }
        }
        
        //With standard input
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadName, null);
            parser.nextTag();
            assertEquals("bla bla", readName.invoke(new ISAXMLParser(parser), new Object[] {}));
        } finally { }
        
        //Without <text></text>
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardReadNameOther, null);
            parser.nextTag();
            assertNull(readName.invoke(new ISAXMLParser(parser), new Object[] {}));
        } finally { }
    }

    public void testReadText() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
    XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        
        //With Null argument
        try {
            readText.invoke(new ISAXMLParser(), new Object[] {});
            fail("readText : Fail test null pointer");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof NullPointerException) {
                if (e.getTargetException().getMessage().equals("Parser is null")) {
                    //waited exception
                } else {
                    fail("readText wrong NullPointerException");
                }
            }
        }

        //With a TAG
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardXml, null);
            parser.nextTag();
            assertNull(readText.invoke(new ISAXMLParser(parser), new Object[] {}));
        } finally { }
        
        //With TEXT
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(text, null);
            parser.nextTag();
            assertEquals("bla", readText.invoke(new ISAXMLParser(parser), new Object[] {}));
        } finally { }
    }

    public void testSkip() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
    XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();

        //WithArgumentNull
        try {
            skip.invoke(new ISAXMLParser(), new Object[] {});
            fail("skip : Fail test null pointer");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof NullPointerException) {
                if (e.getTargetException().getMessage().equals("Parser is null")) {
                    //waited exception
                } else {
                    fail("skip wrong NullPointerException");
                }
            }
        }
        
        //With empty parser
        try {
            skip.invoke(new ISAXMLParser(parser), new Object[] {});
            fail();
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof IllegalStateException) {
                //waited exception
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
            skip.invoke(new ISAXMLParser(parser), new Object[] {});
            fail();
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof IllegalStateException) {
                //waited exception
            }
        }
        
        //With a standard input
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(standardXml, null);
            parser.nextTag();
            skip.invoke(new ISAXMLParser(parser), new Object[] {});
        } finally { }
    }

}
