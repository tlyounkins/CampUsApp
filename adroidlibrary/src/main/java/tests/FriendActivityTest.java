package tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import ycp.adroidlibrary.FriendActivity;
import ycp.adroidlibrary.R;

/**
 * Created by Juan on 12/18/2015.
 */
public class FriendActivityTest extends ActivityInstrumentationTestCase2<FriendActivity> {
    private FriendActivity friendActivity;

    public FriendActivityTest(){super(FriendActivity.class);}

    @Override
    protected void setUp() throws Exception{
        super.setUp();

        friendActivity = getActivity();
    }

    public void testPreconditions(){
        assertNotNull("Edit is not null", friendActivity);
    }
}
