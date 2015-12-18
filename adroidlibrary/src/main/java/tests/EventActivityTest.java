package tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import ycp.adroidlibrary.EventActivity;
import ycp.adroidlibrary.R;

/**
 * Created by Juan on 12/18/2015.
 */
public class EventActivityTest extends ActivityInstrumentationTestCase2<EventActivity> {
    private EventActivity eventActivity;
    private Button saveButton;


    public EventActivityTest(){super(EventActivity.class);}

    @Override
    protected void setUp() throws Exception{
        super.setUp();

        eventActivity = getActivity();
        saveButton = (Button) eventActivity.findViewById(R.id.eNewButton);
    }

    public void testPreconditions(){
        assertNotNull("Edit is not null", eventActivity);
        assertNotNull("Edit first name is not null", saveButton);
    }
}
