package seedu.address.logic.commands;

import static seedu.address.logic.commands.Command.requireViewingDeletedPersons;
import static seedu.address.logic.commands.Command.requireViewingKeptPersons;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.PersonListView;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Contains unit tests for Command.
 */
public class CommandTest {

    @Test
    public void requireViewingKeptPersons_whenViewingKept_doesNotThrow() throws Exception {
        requireViewingKeptPersons(PersonListView.KEPT_PERSONS);
    }

    @Test
    public void requireViewingKeptPersons_whenViewingDeleted_throwsCommandException() {
        // Using a lambda as an argument on a new line
        // CHECKSTYLE.OFF: SeparatorWrap
        assertThrows(CommandException.class, Command.MESSAGE_NOT_VIEWING_KEPT_PERSONS,
                () -> requireViewingKeptPersons(PersonListView.DELETED_PERSONS));
        // CHECKSTYLE.ON: SeparatorWrap
    }

    @Test
    public void requireViewingDeletedPersons_whenViewingDeleted_doesNotThrow() throws Exception {
        requireViewingDeletedPersons(PersonListView.DELETED_PERSONS);
    }

    @Test
    public void requireViewingDeletedPersons_whenViewingKept_throwsCommandException() {
        // Using a lambda as an argument on a new line
        // CHECKSTYLE.OFF: SeparatorWrap
        assertThrows(CommandException.class, Command.MESSAGE_NOT_VIEWING_DELETED_PERSONS,
                () -> requireViewingDeletedPersons(PersonListView.KEPT_PERSONS));
        // CHECKSTYLE.ON: SeparatorWrap
    }
}
