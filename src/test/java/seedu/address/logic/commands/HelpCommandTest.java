package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.HelpCommand.SHOWING_HELP_MESSAGE;

import org.junit.jupiter.api.Test;

import seedu.address.logic.PersonListView;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class HelpCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_viewingKeptPersons_success() {
        CommandResult expectedCommandResult = new CommandResult(
                SHOWING_HELP_MESSAGE, PersonListView.KEPT_PERSONS, true, false);
        assertCommandSuccess(new HelpCommand(), model, PersonListView.KEPT_PERSONS,
                expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_viewingDeletedPersons_success() {
        CommandResult expectedCommandResult = new CommandResult(
                SHOWING_HELP_MESSAGE, PersonListView.DELETED_PERSONS, true, false);
        assertCommandSuccess(new HelpCommand(), model, PersonListView.DELETED_PERSONS,
                expectedCommandResult, expectedModel);
    }
}
