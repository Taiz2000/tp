package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.PersonListView;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

/**
 * Contains tests for EditPreviousCommand.
 */
public class EditPreviousCommandTest {

    private static final String LAST_COMMAND_TEXT = "list";

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        expectedModel = new ModelManager();
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        EditPreviousCommand command = new EditPreviousCommand();
        assertThrows(NullPointerException.class, () -> command.execute(null, PersonListView.KEPT_PERSONS));
    }


    @Test
    public void execute_noPreviousCommand_throwsCommandException() {
        assertCommandFailure(new EditPreviousCommand(), model, PersonListView.KEPT_PERSONS,
                EditPreviousCommand.MESSAGE_NO_PREVIOUS_COMMAND);
        assertCommandFailure(new EditPreviousCommand(), model, PersonListView.DELETED_PERSONS,
                EditPreviousCommand.MESSAGE_NO_PREVIOUS_COMMAND);
    }

    @Test
    public void execute_previousCommandExistsViewingKeptPersons_success() {
        model.setLastCommandText(LAST_COMMAND_TEXT);
        CommandResult expectedCommandResult = new CommandResult(
                String.format(EditPreviousCommand.MESSAGE_SUCCESS, LAST_COMMAND_TEXT),
                PersonListView.KEPT_PERSONS,
                false,
                false,
                LAST_COMMAND_TEXT);

        assertCommandSuccess(new EditPreviousCommand(), model, PersonListView.KEPT_PERSONS,
                expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_previousCommandExistsViewingDeletedPersons_success() {
        model.setLastCommandText(LAST_COMMAND_TEXT);
        CommandResult expectedCommandResult = new CommandResult(
                String.format(EditPreviousCommand.MESSAGE_SUCCESS, LAST_COMMAND_TEXT),
                PersonListView.DELETED_PERSONS,
                false,
                false,
                LAST_COMMAND_TEXT);

        assertCommandSuccess(new EditPreviousCommand(), model, PersonListView.DELETED_PERSONS,
                expectedCommandResult, expectedModel);
    }
}
