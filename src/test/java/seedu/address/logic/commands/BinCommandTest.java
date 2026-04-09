package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showDeletedPersonAtIndex;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.PersonListView;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class BinCommandTest {

    private Model model;
    private Model expectedModel;
    private CommandResult expectedCommandResult = new CommandResult(
            BinCommand.MESSAGE_SUCCESS, PersonListView.DELETED_PERSONS, false, false);

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_viewingAllKeptPersons_switchesToDeletedPersons() {
        assertCommandSuccess(new BinCommand(), model, PersonListView.KEPT_PERSONS,
                expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_viewingFilteredKeptPersons_switchesToDeletedPersons() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        // Kept persons list should stay filtered
        assertCommandSuccess(new BinCommand(), model, PersonListView.KEPT_PERSONS,
                expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_viewingAllDeletedPersons_staysOnDeletedPersons() {
        assertCommandSuccess(new BinCommand(), model, PersonListView.DELETED_PERSONS,
                expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_viewingFilteredDeletedPersons_showsAllDeletedPersons() {
        showDeletedPersonAtIndex(model, INDEX_FIRST_PERSON);

        // Deleted persons list becomes unfiltered
        assertCommandSuccess(new BinCommand(), model, PersonListView.DELETED_PERSONS,
                expectedCommandResult, expectedModel);
    }
}
