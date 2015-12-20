package tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import ycp.adroidlibrary.GroupActivity;
import ycp.adroidlibrary.R;

/**
 * Created by Tyler on 12/18/15.
 */
public class GroupActivityTest extends ActivityInstrumentationTestCase2<GroupActivity> {
    private GroupActivity groupActivity;

    public GroupActivityTest(){super(GroupActivity.class);}

    @Override
    protected void setUp() throws Exception{
        super.setUp();

        groupActivity = getActivity();
    }

    public void testPreconditions(){
        assertNotNull("user_id is not null", groupActivity);
    }

//    public void testOnCreate() throws Exception {
//
//    }
//
//    public void testOnCreateOptionsMenu() throws Exception {
//
//    }
//
//    public void testOnOptionsItemSelected() throws Exception {
//
//    }
//
//    public void testOnGroupCreatePress() throws Exception {
//        createButton.performClick();
//        //assertThat(groupActivity, new StartedMatcher(GroupRegisterActivity.class));
//    }
}