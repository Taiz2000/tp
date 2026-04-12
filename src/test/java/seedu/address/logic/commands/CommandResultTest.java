package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.logic.PersonListView;

public class CommandResultTest {
    @Test
    public void equals() {
        CommandResult commandResult = new CommandResult("feedback", PersonListView.KEPT_PERSONS);

        // same values -> returns true
        assertTrue(commandResult.equals(new CommandResult("feedback", PersonListView.KEPT_PERSONS)));
        assertTrue(commandResult.equals(new CommandResult("feedback", PersonListView.KEPT_PERSONS, false, false)));

        // same object -> returns true
        assertTrue(commandResult.equals(commandResult));

        // null -> returns false
        assertFalse(commandResult.equals(null));

        // different types -> returns false
        assertFalse(commandResult.equals(0.5f));

        // different feedbackToUser value -> returns false
        assertFalse(commandResult.equals(new CommandResult("different", PersonListView.KEPT_PERSONS)));

        // different personListView value -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", PersonListView.DELETED_PERSONS, false, false)));

        // different showHelp value -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", PersonListView.KEPT_PERSONS, true, false)));

        // different exit value -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", PersonListView.KEPT_PERSONS, false, true)));

        // different commandTextToPopulate value -> returns false
        assertFalse(commandResult.equals(new CommandResult(
                "feedback", PersonListView.KEPT_PERSONS, false, false, "list")));
    }

    @Test
    public void hashcode() {
        CommandResult commandResult = new CommandResult("feedback", PersonListView.KEPT_PERSONS);

        // same values -> returns same hashcode
        assertEquals(commandResult.hashCode(),
                new CommandResult("feedback", PersonListView.KEPT_PERSONS).hashCode());

        // different feedbackToUser value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(),
                new CommandResult("different", PersonListView.KEPT_PERSONS).hashCode());

        // different showBin value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(),
                new CommandResult("feedback", PersonListView.DELETED_PERSONS, false, false).hashCode());

        // different showHelp value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(),
                new CommandResult("feedback", PersonListView.KEPT_PERSONS, true, false).hashCode());

        // different exit value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(),
                new CommandResult("feedback", PersonListView.KEPT_PERSONS, false, true).hashCode());

        // different commandTextToPopulate value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(),
                new CommandResult("feedback", PersonListView.KEPT_PERSONS, false, false, "list").hashCode());
    }

    @Test
    public void toStringMethod() {
        CommandResult commandResult = new CommandResult("feedback", PersonListView.KEPT_PERSONS);
        String expected = CommandResult.class.getCanonicalName()
                + "{feedbackToUser=" + commandResult.getFeedbackToUser()
                + ", personListView=" + commandResult.getPersonListView()
                + ", shouldShowHelp=" + commandResult.shouldShowHelp()
                + ", shouldExit=" + commandResult.shouldExit()
                + ", commandTextToPopulate=" + commandResult.getCommandTextToPopulate().orElse(null) + "}";
        assertEquals(expected, commandResult.toString());
    }
}
