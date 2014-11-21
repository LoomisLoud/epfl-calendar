package ch.epfl.calendar.testing.utils;

import android.test.InstrumentationTestCase;

/**
 * This Class comes from the SwEngQuizAppTest
 * @author AblionGE
 *
 */
public class MockTestCase extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // XXX: Hack required to make Mockito work on Android
        System.setProperty("dexmaker.dexcache",
                getInstrumentation().getTargetContext().getCacheDir().getPath());
    }

}