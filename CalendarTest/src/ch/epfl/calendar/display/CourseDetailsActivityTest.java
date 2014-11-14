/**
 * 
 */
package ch.epfl.calendar.display;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import junit.framework.TestCase;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

/**
 * @author LoomisLoud
 *
 */
public class CourseDetailsActivityTest extends TestCase {

    private static final float SIZE_OF_TITLE = 1.5f;

    public void testTitleToSpannable() throws NoSuchMethodException, IllegalAccessException, 
    	IllegalArgumentException, InvocationTargetException {
    	CourseDetailsActivity cs = new CourseDetailsActivity();
    	Method titleToSpannable = cs.getClass().getDeclaredMethod("titleToSpannable", String.class);
    	titleToSpannable.setAccessible(true);
    	
        SpannableString title = (SpannableString) titleToSpannable.invoke(cs, "title");
        
        StyleSpan[] styleSpan = title.getSpans(0, title.length(), StyleSpan.class);
        assertEquals(1, styleSpan.length);
        assertEquals(0, title.getSpanStart(styleSpan[0]));
        assertEquals(title.length(), title.getSpanEnd(styleSpan[0]));
        assertEquals(styleSpan[0].getStyle(), Typeface.BOLD);
        
        RelativeSizeSpan[] sizeSpan = title.getSpans(0,  title.length(), RelativeSizeSpan.class);
        assertEquals(1, sizeSpan.length);
        assertEquals(0, title.getSpanStart(sizeSpan[0]));
        assertEquals(title.length(), title.getSpanEnd(sizeSpan[0]));
        assertEquals(sizeSpan[0].getSizeChange(), SIZE_OF_TITLE);
    }
    
    public void testBodyToSpannable() throws NoSuchMethodException, IllegalAccessException, 
    	IllegalArgumentException, InvocationTargetException {
    	CourseDetailsActivity cs = new CourseDetailsActivity();
    	Method bodyToSpannable = cs.getClass().getDeclaredMethod("bodyToSpannable", String.class);
    	bodyToSpannable.setAccessible(true);
    	
        SpannableString body = (SpannableString) bodyToSpannable.invoke(cs, "title");
        
        StyleSpan[] styleSpan = body.getSpans(0, body.length(), StyleSpan.class);
        assertEquals(1, styleSpan.length);
        assertEquals(0, body.getSpanStart(styleSpan[0]));
        assertEquals(body.length(), body.getSpanEnd(styleSpan[0]));
        assertEquals(styleSpan[0].getStyle(), Typeface.BOLD);
    }
}
