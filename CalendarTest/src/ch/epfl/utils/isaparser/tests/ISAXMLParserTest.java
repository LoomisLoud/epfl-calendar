/**
 * 
 */
package ch.epfl.utils.isaparser.tests;

import org.junit.Test;
import org.xmlpull.v1.XmlPullParser;


import android.util.Xml;

/**
 * @author AblionGE
 *
 */
public class ISAXMLParserTest {

    @Test
    public void testSkipWithoutStartTag() {
        XmlPullParser parser = Xml.newPullParser();
        //skip(parser);
    }

}
