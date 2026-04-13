package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_NOT_VIEWING_KEPT_PERSONS;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showNoPerson;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.PersonListView;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_notViewingKeptPersons_throwsCommandException() {
        DeleteCommand deleteCommand = new DeleteCommand(List.of(INDEX_FIRST_PERSON));
        assertCommandFailure(deleteCommand, model, PersonListView.DELETED_PERSONS,
                MESSAGE_NOT_VIEWING_KEPT_PERSONS);
    }

    @Test
    public void execute_validIndexListUnfilteredList_success() {
        Person firstPersonToDelete = model.getFilteredKeptPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPersonToDelete = model.getFilteredKeptPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));

        String expectedMessage = DeleteCommand.buildSuccessMessage(List.of(firstPersonToDelete, secondPersonToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(firstPersonToDelete);
        expectedModel.deletePerson(secondPersonToDelete);

        assertCommandSuccess(deleteCommand, model, PersonListView.KEPT_PERSONS,
                expectedMessage, PersonListView.KEPT_PERSONS, expectedModel);
    }

    @Test
    public void execute_invalidIndexListUnfilteredList_throwsCommandException() {
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredKeptPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(List.of(outOfBoundIndex));
        assertCommandFailure(deleteCommand, model, PersonListView.KEPT_PERSONS,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        DeleteCommand deleteCommandMultiple = new DeleteCommand(List.of(INDEX_FIRST_PERSON, outOfBoundIndex));
        assertCommandFailure(deleteCommandMultiple, model, PersonListView.KEPT_PERSONS,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // ensures that model state is unchanged after failed command
        assertEquals(model, expectedModel);
    }

    @Test
    public void execute_validIndexListFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getFilteredKeptPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(List.of(INDEX_FIRST_PERSON));

        String expectedMessage = DeleteCommand.buildSuccessMessage(List.of(personToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, PersonListView.KEPT_PERSONS,
                expectedMessage, PersonListView.KEPT_PERSONS, expectedModel);
    }

    @Test
    public void execute_validIndexListFilteredListMultipleIndices_success() {
        Person firstPersonToDelete = model.getFilteredKeptPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPersonToDelete = model.getFilteredKeptPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        model.updateFilteredKeptPersonList(p -> p.equals(firstPersonToDelete) || p.equals(secondPersonToDelete));
        assertEquals(2, model.getFilteredKeptPersonList().size());

        DeleteCommand deleteCommand = new DeleteCommand(List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));

        String expectedMessage = DeleteCommand.buildSuccessMessage(List.of(firstPersonToDelete, secondPersonToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredKeptPersonList(
                p -> p.equals(firstPersonToDelete) || p.equals(secondPersonToDelete));
        expectedModel.deletePerson(firstPersonToDelete);
        expectedModel.deletePerson(secondPersonToDelete);

        assertCommandSuccess(deleteCommand, model, PersonListView.KEPT_PERSONS,
                expectedMessage, PersonListView.KEPT_PERSONS, expectedModel);
    }

    @Test
    public void execute_invalidIndexListFilteredList_throwsCommandException() {
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getKeptPersonList().size());

        DeleteCommand deleteCommand = new DeleteCommand(List.of(outOfBoundIndex));
        assertCommandFailure(deleteCommand, model, PersonListView.KEPT_PERSONS,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        DeleteCommand deleteCommandMultiple = new DeleteCommand(List.of(INDEX_FIRST_PERSON, outOfBoundIndex));
        assertCommandFailure(deleteCommandMultiple, model, PersonListView.KEPT_PERSONS,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // ensures that model state is unchanged after failed command
        assertEquals(model, expectedModel);
    }

    @Test
    public void execute_invalidIndexListEmptyFilteredList_throwsCommandException() {
        showNoPerson(model);

        DeleteCommand deleteCommand = new DeleteCommand(List.of(INDEX_FIRST_PERSON));
        assertCommandFailure(deleteCommand, model, PersonListView.KEPT_PERSONS,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexListFilteredListNonContiguous_success() {
        model.updateFilteredKeptPersonList(person -> person.getName().fullName.equals("Alice Pauline")
                || person.getName().fullName.equals("Fiona Kunz")
                || person.getName().fullName.equals("George Best"));
        assertEquals(3, model.getFilteredKeptPersonList().size());

        Person firstPersonToDelete = model.getFilteredKeptPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPersonToRemain = model.getFilteredKeptPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Person thirdPersonToDelete = model.getFilteredKeptPersonList().get(INDEX_THIRD_PERSON.getZeroBased());

        DeleteCommand deleteCommand = new DeleteCommand(List.of(INDEX_FIRST_PERSON, INDEX_THIRD_PERSON));

        String expectedMessage = DeleteCommand.buildSuccessMessage(List.of(firstPersonToDelete, thirdPersonToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredKeptPersonList(model.getFilteredKeptPersonList()::contains);
        expectedModel.deletePerson(firstPersonToDelete);
        expectedModel.deletePerson(thirdPersonToDelete);

        assertCommandSuccess(deleteCommand, model, PersonListView.KEPT_PERSONS,
                expectedMessage, PersonListView.KEPT_PERSONS, expectedModel);
    }

    @Test
    public void equals_indexListConstructor() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(List.of(INDEX_FIRST_PERSON));
        DeleteCommand deleteSecondCommand = new DeleteCommand(List.of(INDEX_SECOND_PERSON));

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(List.of(INDEX_FIRST_PERSON));
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        List<Index> targetIndices = List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        DeleteCommand deleteCommand = new DeleteCommand(targetIndices);
        String expected = DeleteCommand.class.getCanonicalName() + "{targetIndices=" + targetIndices + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    @Test
    public void buildSuccessMessageMethod() {
        Person firstPerson = model.getFilteredKeptPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPerson = model.getFilteredKeptPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        String expectedMessage = DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS_PREFIX
                + DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS_DELIMITER
                + Messages.format(firstPerson)
                + DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS_DELIMITER
                + Messages.format(secondPerson);
        String actualMessage = DeleteCommand.buildSuccessMessage(List.of(firstPerson, secondPerson));

        assertEquals(expectedMessage, actualMessage);
    }
}
