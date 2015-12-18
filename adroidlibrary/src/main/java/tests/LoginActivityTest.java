package tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import ycp.adroidlibrary.LoginActivity;
import ycp.adroidlibrary.R;

/**
 * Created by Juan on 12/18/2015.
 */
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private LoginActivity loginActivity;
    private Button emailSigninButton;

    public LoginActivityTest(){super(LoginActivity.class);}

    @Override
    protected void setUp() throws Exception{
        super.setUp();

        loginActivity = getActivity();
        emailSigninButton = (Button) loginActivity.findViewById(R.id.eNewButton);
    }

    public void testPreconditions(){
        assertNotNull("Edit is not null", loginActivity);
        assertNotNull("Edit first name is not null", emailSigninButton);
    }
}
