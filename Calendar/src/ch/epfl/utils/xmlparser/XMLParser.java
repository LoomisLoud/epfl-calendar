/**
 * 
 */
package ch.epfl.utils.xmlparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import ch.epfl.calendar.data.Course;

import android.util.Xml;

/**
 * @author AblionGE
 *
 */
public class XMLParser {
    // We don't use namespaces
    private static final String mNameSpaces = null;
   
    /**
     * Parse an InputStream
     * @param in
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    public List<Course> parse(InputStream in) throws XmlPullParserException, IOException {
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
     * Read a Feed
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private List<Course> readData(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Course> entries = new ArrayList<Course>();

        parser.require(XmlPullParser.START_TAG, mNameSpaces, "data");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nameParser = parser.getName();
            // Starts by looking for the study-period tag
            if (nameParser.equals("study-period")) {
                entries.add(readStudyPeriod(parser));
            } else {
                skip(parser);
            }
        }  
        return entries;
    }
    
    /**
     * Processes different tags to construct a Course Object.
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private Course readStudyPeriod(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, mNameSpaces, "study-period");
        String course = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nameParser = parser.getName();
            if (nameParser.equals("course")) {
                course = readCourse(parser);
                
                //TODO : Get other field here !
                
            } else {
                skip(parser);
            }
        }
        return new Course(course);
    }

    
    /**
     * Processes course tags.
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private String readCourse(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, mNameSpaces, "course");
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
     * Processes text tags.
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, mNameSpaces, "name");
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
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    
    /**
     * Skip tag don't needed
     * @param parser
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
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
                    
            }
        }
    }
}
