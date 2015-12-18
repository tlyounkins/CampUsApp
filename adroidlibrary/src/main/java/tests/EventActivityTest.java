package tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import ycp.adroidlibrary.EditActivity;
import ycp.adroidlibrary.EventActivity;
import ycp.adroidlibrary.R;

/**
 * Created by Juan on 12/18/2015.
 */
public class EventActivityTest extends ActivityInstrumentationTestCase2<EventActivity> {
    private EventActivity eventActivity;
    private Button confirmButton;
    private EditText firstName;
    private EditText lastName;

    public EventActivityTest(){super(EventActivity.class);}

    @Override
    protected void setUp() throws Exception{
        super.setUp();

        eventActivity = getActivity();
        confirmButton = (Button) eventActivity.findViewById(R.id.editConfirmButton);
        firstName = (EditText) eventActivity.findViewById(R.id.editFirst);
        lastName= (EditText) eventActivity.findViewById(R.id.editLast);
    }

    public void testPreconditions(){
        assertNotNull("Edit is not null", eventActivity);
        assertNotNull("Edit first name is not null", firstName);
        assertNotNull("Edit last name is not null", lastName);
    }
}
