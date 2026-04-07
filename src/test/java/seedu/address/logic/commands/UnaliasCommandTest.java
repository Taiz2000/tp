package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.PersonListView;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

/**
 * Reused from Codex suggestions upon providing specifications
 */
public class UnaliasCommandTest {

    @Test
    public void constructor_invalidAliasName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, AliasCommand.MESSAGE_INVALID_ALIAS_NAME, ()
                -> new UnaliasCommand("LS"));
    }

    @Test
    public void execute_existingAliasWhileViewingKeptPersons_success() {
        Model model = new ModelManager();
        model.setCommandAlias("ls", ListCommand.COMMAND_WORD);
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new UnaliasCommand("ls"), model,
                PersonListView.KEPT_PERSONS,
                String.format(UnaliasCommand.MESSAGE_SUCCESS, "ls"),
                PersonListView.KEPT_PERSONS, expectedModel);
    }

    @Test
    public void execute_existingAliasWhileViewingDeletedPersons_success() {
        Model model = new ModelManager();
        model.setCommandAlias("ls", ListCommand.COMMAND_WORD);
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new UnaliasCommand("ls"), model,
                PersonListView.DELETED_PERSONS,
                String.format(UnaliasCommand.MESSAGE_SUCCESS, "ls"),
                PersonListView.DELETED_PERSONS, expectedModel);
    }

    @Test
    public void execute_missingAlias_throwsCommandException() {
        Model model = new ModelManager();

        assertCommandFailure(new UnaliasCommand("ls"), model, PersonListView.KEPT_PERSONS,
                UnaliasCommand.MESSAGE_ALIAS_NOT_FOUND);
    }

    @Test
    public void equals() {
        UnaliasCommand firstCommand = new UnaliasCommand("ls");
        UnaliasCommand secondCommand = new UnaliasCommand("rm");

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(new UnaliasCommand("ls")));
        assertFalse(firstCommand.equals(1));
        assertFalse(firstCommand.equals(null));
        assertFalse(firstCommand.equals(secondCommand));
    }

    @Test
    public void toStringMethod() {
        UnaliasCommand unaliasCommand = new UnaliasCommand("ls");
        String expected = UnaliasCommand.class.getCanonicalName() + "{shortName=ls}";
        assertEquals(expected, unaliasCommand.toString());
    }
}
