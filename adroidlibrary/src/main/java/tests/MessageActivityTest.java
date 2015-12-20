package tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import ycp.adroidlibrary.MessageActivity;
import ycp.adroidlibrary.R;

/**
 * Created by Juan on 12/18/2015.
 */
public class MessageActivityTest extends ActivityInstrumentationTestCase2<MessageActivity> {
    private MessageActivity messageActivity;
    private Button sendButton;

    public MessageActivityTest(){super(MessageActivity.class);}

    @Override
    protected void setUp() throws Exception{
        super.setUp();

        messageActivity = getActivity();
        sendButton = (Button) messageActivity.findViewById(R.id.messageSendButton);
    }

    public void testPreconditions(){
        assertNotNull("Edit is not null", messageActivity);
        assertNotNull("Edit first name is not null", sendButton);
    }

}
