/**
 *
 */
package ch.epfl.calendar.utils.isaparser;

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
    private XmlPullParser mParser = null;

    public ISAXMLParser() {
        mParser = Xml.newPullParser();
    }

    public ISAXMLParser(XmlPullParser parser) {
        mParser = parser;
    }

    /**
     * Parse an InputStream
     * @param in
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    public List<Course> parse(InputStream in) {
        if (in == null) {
            throw new NullPointerException("InputStream is null");
        }
        try {
            mParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            mParser.setInput(in, null);
            mParser.nextTag();
            return readData();
        } catch (XmlPullParserException e) {
            throw new ParsingException("Parsing Error during XML Parsing");
        } catch (IOException e) {
            throw new ParsingException("IO Error during XML Parsing");
        } catch (IllegalArgumentException e) {
            throw new ParsingException(e.getMessage());
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                throw new ParsingException("Closing IO Error during XML Parsing");
            }
        }
    }

    /**
     * Read Data
     * @param mParser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private List<Course> readData() {
        if (mParser == null) {
            throw new NullPointerException("Parser is null");
        }
        List<Course> courses = new ArrayList<Course>();
        try {
            mParser.require(XmlPullParser.START_TAG, NMP, "data");
            while (mParser.next() != XmlPullParser.END_TAG) {
                boolean added = false;
                if (mParser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String nameParser = mParser.getName();
                // Starts by looking for the study-period tag
                if (nameParser.equals("study-period")) {
                    Course newCourse = readStudyPeriod();
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
                    skip();
                }
            }
            return courses;
        }  catch (XmlPullParserException e) {
            throw new ParsingException("Parsing Error during XML Parsing");
        } catch (IOException e) {
            throw new ParsingException("IO Error during XML Parsing");
        }
    }

    /**
     * Processes different tags to construct a Course Object.
     * @param mParser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private Course readStudyPeriod() throws XmlPullParserException, IOException {
        if (mParser == null) {
            throw new NullPointerException("Parser is null");
        }
        mParser.require(XmlPullParser.START_TAG, NMP, "study-period");
        String course = null;
        String date = null;
        String startTime = null;
        String endTime = null;
        String type = null;
        List<String> rooms = new ArrayList<String>();
        while (mParser.next() != XmlPullParser.END_TAG) {
            if (mParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nameParser = mParser.getName();
            if (nameParser.equals("course")) {
                course = readCourse();
            } else if (nameParser.equals("date")) {
                date = readText();
            } else if (nameParser.equals("startTime")) {
                startTime = readText();
            } else if (nameParser.equals("endTime")) {
                endTime = readText();
            } else if (nameParser.equals("type")) {
                type = readType();
            } else if (nameParser.equals("room")) {
                rooms.add(readRoom());
            } else {
                skip();
            }
        }
        return new Course(course, date, startTime, endTime, type, rooms, null);
    }


    /**
     * Processes course tags.
     * @param mParser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private String readCourse() throws XmlPullParserException, IOException {
        if (mParser == null) {
            throw new NullPointerException("Parser is null");
        }
        mParser.require(XmlPullParser.START_TAG, NMP, "course");
        String name = null;
        while (mParser.next() != XmlPullParser.END_TAG) {
            if (mParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nameParser = mParser.getName();
            if (nameParser.equals("name")) {
                name = readName();
            } else {
                skip();
            }
        }
        return name;
    }

    /**
     * Processes Type tags.
     * @param mParser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    //TODO : Manage different languages (en-fr)
    private String readType() throws XmlPullParserException, IOException {
        if (mParser == null) {
            throw new NullPointerException("Parser is null");
        }
        mParser.require(XmlPullParser.START_TAG, NMP, "type");
        String text = null;
        while (mParser.next() != XmlPullParser.END_TAG) {
            if (mParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nameParser = mParser.getName();
            if (nameParser.equals("text")) {
                text = readText();
            } else {
                skip();
            }
        }
        return text;
    }

    /**
     * Processes room tags.
     * @param mParser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private String readRoom() throws XmlPullParserException, IOException {
        if (mParser == null) {
            throw new NullPointerException("Parser is null");
        }
        mParser.require(XmlPullParser.START_TAG, NMP, "room");
        String name = null;
        while (mParser.next() != XmlPullParser.END_TAG) {
            if (mParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nameParser = mParser.getName();
            if (nameParser.equals("name")) {
                name = readName();
            } else {
                skip();
            }
        }
        return name;
    }



    /**
     * Processes text tags.
     * @param mParser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readName() throws IOException, XmlPullParserException {
        if (mParser == null) {
            throw new NullPointerException("Parser is null");
        }
        mParser.require(XmlPullParser.START_TAG, NMP, "name");
        String text = null;
        while (mParser.next() != XmlPullParser.END_TAG) {
            if (mParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nameParser = mParser.getName();
            if (nameParser.equals("text")) {
                text = readText();
            } else {
                skip();
            }
        }
        return text;
    }


    /**
     * Used for read the name
     * @param mParser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readText() throws IOException, XmlPullParserException {
        if (mParser == null) {
            throw new NullPointerException("Parser is null");
        }
        String result = null;
        if (mParser.next() == XmlPullParser.TEXT) {
            result = mParser.getText();
            mParser.nextTag();
        }
        return result;
    }

    /**
     * Skip tags don't needed
     * @param mParser
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void skip() throws XmlPullParserException, IOException {
        if (mParser == null) {
            throw new NullPointerException("Parser is null");
        }
        if (mParser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (mParser.next()) {
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
