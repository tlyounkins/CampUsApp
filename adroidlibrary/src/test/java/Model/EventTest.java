package Model;

import junit.framework.TestCase;

import org.junit.Before;

/**
 * Created by Juan on 10/8/2015.
 */
public class EventTest extends TestCase {
    Event eventTest;

    @Before
    public void setUp(){
        eventTest = new Event();
    }

    public void testGettersSetters(){
        eventTest.setId(0);
        eventTest.setStart_time(1000);
        eventTest.setEnd_time(1200);
        eventTest.setDate(101166);
        eventTest.setLocation("YCP Campus");
        eventTest.setDescription("This is a test");
        eventTest.setParticipants("bob");

        // Event
        assertEquals("id should be 0", 0 ,eventTest.getId());
        assertEquals("Event starts at 1000", 1000, eventTest.getStart_time());
        assertEquals("Event ends at 1000", 1200, eventTest.getEnd_time());
        assertEquals("Event should be on 101166", 101166, eventTest.getDate());
        assertEquals("Event should be located at YCP Campus", "YCP Campus", eventTest.getLocation());
        assertEquals("Event is described as a test","This is a test", eventTest.getDescription());
        assertEquals("Event should have bob", "bob", eventTest.getParticipants());
    }
}