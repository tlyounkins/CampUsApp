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
    private EditText bio;
    private EditText major;
    private EditText home;
    private EditText gender;

    public EditActivityTest(){super(EditActivity.class);}

    @Override
    protected void setUp() throws Exception{
        super.setUp();

        editActivity = getActivity();
        confirmButton = (Button) editActivity.findViewById(R.id.editConfirmButton);
        firstName = (EditText) editActivity.findViewById(R.id.editFirst);
        lastName= (EditText) editActivity.findViewById(R.id.editLast);
        bio = (EditText) editActivity.findViewById(R.id.editBio);
        major = (EditText) editActivity.findViewById(R.id.editMajor);
        home = (EditText) editActivity.findViewById(R.id.editHome);
        gender = (EditText) editActivity.findViewById(R.id.editGender);
    }

    public void testPreconditions(){
        assertNotNull("Edit is not null", editActivity);
        assertNotNull("Edit first name is not null", firstName);
        assertNotNull("Edit last name is not null", lastName);
        assertNotNull("Edit first name is not null", bio);
        assertNotNull("Edit last name is not null", major);
        assertNotNull("Edit first name is not null", home);
        assertNotNull("Edit last name is not null", gender);
    }
}
