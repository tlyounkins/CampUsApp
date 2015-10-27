package Model;

import junit.framework.TestCase;

import org.junit.Before;

import java.util.Calendar;

/**
 * Created by Mike on 10/6/2015.
 *
 * Unit tests for the message class
 */
public class MessageTest extends TestCase{
    Message tester;

    @Before
    public void setUp(){
        tester = new Message();
    }

    public void testGetterSetters(){
        tester.setId(1);
        tester.setSender("User");
        tester.setRecipient("User2");
        tester.setBody("I'm a message! Hello!");

        // Sender
        assertEquals("Sender must be User", "User", tester.getSender());
        // Receiver
        assertEquals("Receiver must be User2", "User2", tester.getRecipient());
        // Body
        assertEquals("Body does not match", "I'm a message! Hello!", tester.getBody());
        // id
        assertEquals("id does not match", 1, tester.getId());
    }

}