package tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import ycp.adroidlibrary.R;
import ycp.adroidlibrary.RegisterActivity;

/**
 * Created by Tyler on 12/18/15.
 */
public class RegisterActivityTest extends ActivityInstrumentationTestCase2<RegisterActivity> {

    private RegisterActivity registerActivity;
    private Button regDoneButton;

    public RegisterActivityTest(){super(RegisterActivity.class);}

    @Override
    protected void setUp() throws Exception{
        super.setUp();

        registerActivity = getActivity();
        regDoneButton = (Button) registerActivity.findViewById(R.id.registerDoneButton);
    }

    public void testPreconditions(){
        assertNotNull("Edit is not null", registerActivity);
        assertNotNull("Edit first name is not null", regDoneButton);
    }

//    public void testOnCreate() throws Exception {
//
//    }
//
//    public void testOnRegisterClick() throws Exception {
//
//    }
}