package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RoleTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Role(null));
    }

    @Test
    public void isValidRole() {
        // null role
        assertThrows(NullPointerException.class, () -> Role.isValidRole(null));

        // valid roles
        assertTrue(Role.isValidRole(""));
        assertTrue(Role.isValidRole(" "));
        assertTrue(Role.isValidRole("Logistics Lead"));
        assertTrue(Role.isValidRole("Any symbols !@#$%^&*()"));
    }

    @Test
    public void equals() {
        Role role = new Role("Valid Role");

        // same values -> returns true
        assertTrue(role.equals(new Role("Valid Role")));

        // same object -> returns true
        assertTrue(role.equals(role));

        // null -> returns false
        assertFalse(role.equals(null));

        // different types -> returns false
        assertFalse(role.equals(5.0f));

        // different values -> returns false
        assertFalse(role.equals(new Role("Other Valid Role")));
    }

    @Test
    public void hashCode_sameValue_sameHashCode() {
        Role firstRole = new Role("Coordinator");
        Role secondRole = new Role("Coordinator");
        assertEquals(firstRole.hashCode(), secondRole.hashCode());
    }
}
