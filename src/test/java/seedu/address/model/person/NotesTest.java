package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NotesTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Notes(null));
    }

    @Test
    public void isValidNotes() {
        // null notes
        assertThrows(NullPointerException.class, () -> Notes.isValidNotes(null));

        // valid notes
        assertTrue(Notes.isValidNotes(""));
        assertTrue(Notes.isValidNotes("Available on weekends only"));
        assertTrue(Notes.isValidNotes("Prefers phone contact.\nCan cover late shifts."));
    }

    @Test
    public void equals() {
        Notes notes = new Notes("Valid notes");

        // same values -> returns true
        assertTrue(notes.equals(new Notes("Valid notes")));

        // same object -> returns true
        assertTrue(notes.equals(notes));

        // null -> returns false
        assertFalse(notes.equals(null));

        // different types -> returns false
        assertFalse(notes.equals(5.0f));

        // different values -> returns false
        assertFalse(notes.equals(new Notes("Other valid notes")));
    }

    @Test
    public void hashCode_sameValue_sameHashCode() {
        Notes firstNotes = new Notes("Some notes");
        Notes secondNotes = new Notes("Some notes");
        assertEquals(firstNotes.hashCode(), secondNotes.hashCode());
    }
}
