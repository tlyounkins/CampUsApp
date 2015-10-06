package Model;

import junit.framework.TestCase;

import org.junit.Before;

/**
 * Created by Mike on 10/6/2015.
 *
 * Tests the User class
 */
public class UserTest extends TestCase {
    User tester;

    @Before
    public void setUp(){
        tester = new User();
    }

    public void testGettersSetters(){

        tester.setUsername("User");
        tester.setAge(20);
        tester.setEmail("User@gmail.com");
        tester.setLocation("York");
        tester.setPassword("hunter2");
        tester.setSchool("York College");
        tester.setDob(1, 2, 1985);

        // Username
        assertEquals("Username must be User", "User", tester.getUsername());
        // Age
        assertEquals("Age must be 20", 20, tester.getAge());
        // Email
        assertEquals("Email must be User@gmail.com", "User@gmail.com", tester.getEmail());
        // Location
        assertEquals("Location must be York", "York", tester.getLocation());
        // Password
        assertEquals("Password must be hunter2", "hunter2", tester.getPassword());
        // School
        assertEquals("School must be York College", "York College", tester.getSchool());
        // Month
        assertEquals("Month must be 01", 1, tester.getDobMonth());
        // Day
        assertEquals("Day must be 02", 2, tester.getDobDay());
        // Year
        assertEquals("Year must be 1985", 1985, tester.getDobYear());
    }
}