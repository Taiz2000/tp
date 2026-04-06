package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.PersonListView;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_notViewingKeptPersons_throwsCommandException() {
        Model model = new ModelManager();
        assertCommandFailure(new ClearCommand(), model, PersonListView.DELETED_PERSONS,
                Messages.MESSAGE_NOT_VIEWING_KEPT_PERSONS);
    }

    @Test
    public void execute_emptyAddressBook_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, PersonListView.KEPT_PERSONS,
                ClearCommand.MESSAGE_SUCCESS, PersonListView.KEPT_PERSONS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_success() {
        AddressBook actualAddressBook = new AddressBook();
        actualAddressBook.setKeptPersons(List.of(ALICE, BENSON));
        actualAddressBook.setDeletedPersons(List.of(ALICE, CARL));
        Model model = new ModelManager(actualAddressBook, new UserPrefs());

        AddressBook expectedAddressBook = new AddressBook();
        expectedAddressBook.setDeletedPersons(List.of(ALICE, CARL, BENSON));
        Model expectedModel = new ModelManager(expectedAddressBook, new UserPrefs());

        assertCommandSuccess(new ClearCommand(), model, PersonListView.KEPT_PERSONS,
                ClearCommand.MESSAGE_SUCCESS, PersonListView.KEPT_PERSONS, expectedModel);
    }

}
