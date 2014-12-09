package ch.epfl.calendar.testing.utils;

import ch.epfl.calendar.DefaultActionBarActivity;
import ch.epfl.calendar.apiInterface.UpdateDataFromDBInterface;

/**
 * This class is simply a mock !
 * @author AblionGE
 * 
 */
public class MockActivity extends DefaultActionBarActivity implements
        UpdateDataFromDBInterface {

    @Override
    public void updateData() {
        // Do Nothing
        
    }

}
