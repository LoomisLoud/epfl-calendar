/**
 * 
 */
package ch.epfl.utils.isaparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import ch.epfl.calendar.data.Course;

import android.util.Xml;

/**
 * XML Parser for ISA timetable
 * @author AblionGE
 *
 */
public class ISAXMLParser {
    // We don't use namespaces
    private static final String NMP = null;
   
    /**
     * Parse an InputStream
     * @param in
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    public static List<Course> parse(InputStream in) throws XmlPullParserException, IOException {
        if (in == null) {
            throw new NullPointerException("InputStream is null");
        }
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readData(parser);
        } finally {
            in.close();
        }
    }
    
    /**
     * Read Data
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static List<Course> readData(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser == null) {
            throw new NullPointerException("Parser is null");
        }
        List<Course> courses = new ArrayList<Course>();

        parser.require(XmlPullParser.START_TAG, NMP, "data");
        while (parser.next() != XmlPullParser.END_TAG) {
            boolean added = false;
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nameParser = parser.getName();
            // Starts by looking for the study-period tag
            if (nameParser.equals("study-period")) {
                Course newCourse = readStudyPeriod(parser);
                for (Course course: courses) {
                    if (course.getName().equals(newCourse.getName())) {
                        course.addPeriod(newCourse.getPeriods().get(0));
                        added = true;
                    }
                }
                if (!added) {
                    courses.add(newCourse);
                }
            } else {
                skip(parser);
            }
        }  
        return courses;
    }
    
    /**
     * Processes different tags to construct a Course Object.
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static Course readStudyPeriod(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser == null) {
            throw new NullPointerException("Parser is null");
        }
        parser.require(XmlPullParser.START_TAG, NMP, "study-period");
        String course = null;
        String date = null;
        String startTime = null;
        String endTime = null;
        String type = null;
        List<String> rooms = new ArrayList<String>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nameParser = parser.getName();
            if (nameParser.equals("course")) {
                course = readCourse(parser);
            } else if (nameParser.equals("date")) {
                date = readDate(parser);
            } else if (nameParser.equals("startTime")) {
                startTime = readStartTime(parser);
            } else if (nameParser.equals("endTime")) {
                endTime = readEndTime(parser);
            } else if (nameParser.equals("type")) {
                type = readType(parser);
            } else if (nameParser.equals("room")) {
                rooms.add(readRoom(parser));
            } else {
                skip(parser);
            }
        }
        return new Course(course, date, startTime, endTime, type, rooms);
    }

    
    /**
     * Processes course tags.
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static String readCourse(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser == null) {
            throw new NullPointerException("Parser is null");
        }
        parser.require(XmlPullParser.START_TAG, NMP, "course");
        String name = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nameParser = parser.getName();
            if (nameParser.equals("name")) {
                name = readName(parser);
            } else {
                skip(parser);
            }
        }
        return name;
    }
    
    /**
     * Processes date tags.
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static String readDate(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser == null) {
            throw new NullPointerException("Parser is null");
        }
        String date = "";
        if (parser.next() == XmlPullParser.TEXT) {
            date = parser.getText();
            parser.nextTag();
        }
        return date;
    }
    
    /**
     * Processes StartTime tags.
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static String readStartTime(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser == null) {
            throw new NullPointerException("Parser is null");
        }
        String startTime = "";
        if (parser.next() == XmlPullParser.TEXT) {
            startTime = parser.getText();
            parser.nextTag();
        }
        return startTime;
    }
    
    /**
     * Processes EndTime tags.
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static String readEndTime(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser == null) {
            throw new NullPointerException("Parser is null");
        }
        String endTime = "";
        if (parser.next() == XmlPullParser.TEXT) {
            endTime = parser.getText();
            parser.nextTag();
        }
        return endTime;
    }
    
    /**
     * Processes Type tags.
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    //TODO : Manage different languages (en-fr)
    private static String readType(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser == null) {
            throw new NullPointerException("Parser is null");
        }
        parser.require(XmlPullParser.START_TAG, NMP, "type");
        String text = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nameParser = parser.getName();
            if (nameParser.equals("text")) {
                text = readText(parser);
            } else {
                skip(parser);
            }
        }
        return text;
    }
    
    /**
     * Processes room tags.
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    //FIXME : Get the name(GC B3 31) instead of code (GCB331)
    private static String readRoom(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser == null) {
            throw new NullPointerException("Parser is null");
        }
        parser.require(XmlPullParser.START_TAG, NMP, "room");
        String name = "";
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nameParser = parser.getName();
            if (nameParser.equals("name")) {
                name = readName(parser);
            } else {
                skip(parser);
            }
        }
        return name;
    }

    
    
    /**
     * Processes text tags.
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private static String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        if (parser == null) {
            throw new NullPointerException("Parser is null");
        }
        parser.require(XmlPullParser.START_TAG, NMP, "name");
        String text = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nameParser = parser.getName();
            if (nameParser.equals("text")) {
                text = readText(parser);
            } else {
                skip(parser);
            }
        }
        return text;
    }


    /**
     * Used for read the name
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        if (parser == null) {
            throw new NullPointerException("Parser is null");
        }
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    
    /**
     * Skip tags don't needed
     * @param parser
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser == null) {
            throw new NullPointerException("Parser is null");
        }
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
                default:
                    //value not considered
            }
        }
    }
}
