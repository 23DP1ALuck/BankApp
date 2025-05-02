package services;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValidatorTest {
    private static final Validator validator = new Validator();
    @Test
    public void testAssertThrows_validatePass() {
        assertThrows(Exception.class, () -> validator.validatePass("short"));
        assertThrows(Exception.class, () -> validator.validatePass("nonumbers"));
        assertThrows(Exception.class, () -> validator.validatePass("12345678"));
        assertThrows(Exception.class, () -> validator.validatePass(""));
        assertThrows(Exception.class, () -> validator.validatePass("abcd"));
    }

    @Test
    public void testAssertThrows_validateUsername() {
        assertThrows(Exception.class, () -> validator.validateUsername("a.b"));
        assertThrows(Exception.class, () -> validator.validateUsername(".username"));
        assertThrows(Exception.class, () -> validator.validateUsername("username_"));
        assertThrows(Exception.class, () -> validator.validateUsername("user..name"));
        assertThrows(Exception.class, () -> validator.validateUsername("u"));
    }

}