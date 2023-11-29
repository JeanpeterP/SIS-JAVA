package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roles.User;

class UserTest {

    private User user;

    // Since User is abstract, we create an anonymous class for testing
    @BeforeEach
    void setUp() {
        user = new User("U001", "John Doe", "johndoe", "password123") {};
    }

    @Test
    void testGetId() {
        assertEquals("U001", user.getId(), "getId should return the correct ID");
    }

    @Test
    void testGetName() {
        assertEquals("John Doe", user.getName(), "getName should return the correct name");
    }

    @Test
    void testGetUsername() {
        assertEquals("johndoe", user.getUsername(), "getUsername should return the correct username");
    }

    @Test
    void testGetPassword() {
        assertEquals("password123", user.getPassword(), "getPassword should return the correct password");
    }

    @Test
    void testSetId() {
        user.setId("U002");
        assertEquals("U002", user.getId(), "setId should update the ID");
    }

    @Test
    void testSetName() {
        user.setName("Jane Doe");
        assertEquals("Jane Doe", user.getName(), "setName should update the name");
    }

    @Test
    void testSetUsername() {
        user.setUsername("janedoe");
        assertEquals("janedoe", user.getUsername(), "setUsername should update the username");
    }

    @Test
    void testSetPassword() {
        user.setPassword("newpassword123");
        assertEquals("newpassword123", user.getPassword(), "setPassword should update the password");
    }

    @Test
    void testCheckPasswordWithCorrectPassword() {
        assertTrue(user.checkPassword("password123"), "checkPassword should return true for the correct password");
    }

    @Test
    void testCheckPasswordWithIncorrectPassword() {
        assertFalse(user.checkPassword("wrongpassword"), "checkPassword should return false for an incorrect password");
    }
}
