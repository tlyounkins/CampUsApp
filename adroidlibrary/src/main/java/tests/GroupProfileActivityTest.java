package tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import ycp.adroidlibrary.R;
import ycp.adroidlibrary.GroupProfileActivity;

/**
 * Created by Tyler on 12/18/15.
 */
public class GroupProfileActivityTest extends ActivityInstrumentationTestCase2<GroupProfileActivity> {

    private GroupProfileActivity groupProfileActivity;
    private Button gEventsButton;
    private Button gProfApply;

    public GroupProfileActivityTest(){super(GroupProfileActivity.class);}

    @Override
    protected void setUp() throws Exception{
        super.setUp();

        groupProfileActivity = getActivity();
        gEventsButton = (Button) groupProfileActivity.findViewById(R.id.gEvents);
        gProfApply = (Button) groupProfileActivity.findViewById(R.id.gProfileApply);
    }

    public void testPreconditions(){
        assertNotNull("Edit is not null", groupProfileActivity);
        assertNotNull("Edit first name is not null", gEventsButton);
        assertNotNull("Edit last name is not null", gProfApply);
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
//    public void testOnEventPress() throws Exception {
//
//    }
//
//    public void testOnApplyPress() throws Exception {
//
//    }
}