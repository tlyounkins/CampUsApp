package tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ycp.adroidlibrary.Dashboard;
import ycp.adroidlibrary.EditActivity;
import ycp.adroidlibrary.R;

/**
 * Created by Juan on 12/18/2015.
 */
public class EditActivityTest extends ActivityInstrumentationTestCase2<EditActivity> {
    private EditActivity editActivity;
    private Button confirmButton;
    private EditText firstName;
    private EditText lastName;
    public EditActivityTest(){super(EditActivity.class);}

    @Override
    protected void setUp() throws Exception{
        super.setUp();

        editActivity = getActivity();
        confirmButton = (Button) editActivity.findViewById(R.id.editConfirmButton);
        firstName = (EditText) editActivity.findViewById(R.id.editFirst);
        lastName= (EditText) editActivity.findViewById(R.id.editLast);
    }

    public void testPreconditions(){
        assertNotNull("Edit is not null", editActivity);
        assertNotNull("Edit first name is not null", firstName);
        assertNotNull("Edit last name is not null", lastName);
    }
}
