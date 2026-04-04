package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.Command.MESSAGE_NOT_VIEWING_DELETED_PERSONS;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showDeletedPersonAtIndex;
import static seedu.address.logic.commands.RestoreCommand.MESSAGE_DUPLICATE_PERSONS_TO_RESTORE;
import static seedu.address.logic.commands.RestoreCommand.MESSAGE_PERSON_ALREADY_IN_KEPT_PERSONS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.PersonListView;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code RestoreCommand}.
 */
public class RestoreCommandTest {

    private Model model;

    /**
     * Sets up the model with some deleted persons.
     * ALICE and BENSON are moved to the deleted list.
     */
    private void setUpModelWithDeletedPersons() {
        AddressBook addressBook = getTypicalAddressBook();
        addressBook.deletePerson(ALICE);
        addressBook.deletePerson(BENSON);
        model = new ModelManager(addressBook, new UserPrefs());
    }

    @Test
    public void execute_notViewingDeletedPersons_throwsCommandException() {
        setUpModelWithDeletedPersons();

        RestoreCommand restoreCommand = new RestoreCommand(List.of(INDEX_FIRST_PERSON));
        assertCommandFailure(restoreCommand, model, PersonListView.KEPT_PERSONS,
                MESSAGE_NOT_VIEWING_DELETED_PERSONS);
    }

    @Test
    public void execute_personAlreadyInKeptList_throwsCommandException() {
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(ALICE);
        addressBook.addDeletedPerson(ALICE);
        addressBook.addDeletedPerson(BENSON);
        model = new ModelManager(addressBook, new UserPrefs());

        RestoreCommand restoreCommand = new RestoreCommand(List.of(INDEX_FIRST_PERSON));
        assertCommandFailure(restoreCommand, model, PersonListView.DELETED_PERSONS,
                MESSAGE_PERSON_ALREADY_IN_KEPT_PERSONS);
    }

    @Test
    public void execute_onePersonAlreadyInKeptListFromMultiple_throwsCommandException() {
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(ALICE);
        addressBook.addDeletedPerson(ALICE);
        addressBook.addDeletedPerson(BENSON);
        model = new ModelManager(addressBook, new UserPrefs());

        RestoreCommand restoreCommand = new RestoreCommand(List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));
        assertCommandFailure(restoreCommand, model, PersonListView.DELETED_PERSONS,
                MESSAGE_PERSON_ALREADY_IN_KEPT_PERSONS);
    }

    @Test
    public void execute_duplicateIdentityInRestoreTargets_throwsCommandException() {
        AddressBook addressBook = new AddressBook();
        Person deletedAlice = new PersonBuilder(ALICE).withRole("Leader").build();
        Person duplicateIdentityAlice = new PersonBuilder(ALICE).withRole("Medic").build();
        addressBook.addDeletedPerson(deletedAlice);
        addressBook.addDeletedPerson(duplicateIdentityAlice);
        model = new ModelManager(addressBook, new UserPrefs());

        RestoreCommand restoreCommand = new RestoreCommand(List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));
        assertCommandFailure(restoreCommand, model, PersonListView.DELETED_PERSONS,
                MESSAGE_DUPLICATE_PERSONS_TO_RESTORE);
    }

    @Test
    public void execute_validIndexListUnfilteredList_success() {
        setUpModelWithDeletedPersons();
        Person firstPersonToRestore = model.getFilteredDeletedPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPersonToRestore = model.getFilteredDeletedPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        RestoreCommand restoreCommand = new RestoreCommand(List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));

        String expectedMessage = RestoreCommand.buildSuccessMessage(
                List.of(firstPersonToRestore, secondPersonToRestore));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.restorePerson(firstPersonToRestore);
        expectedModel.restorePerson(secondPersonToRestore);

        assertCommandSuccess(restoreCommand, model, PersonListView.DELETED_PERSONS,
                expectedMessage, PersonListView.DELETED_PERSONS, expectedModel);
    }

    @Test
    public void execute_invalidIndexListUnfilteredList_throwsCommandException() {
        setUpModelWithDeletedPersons();
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredDeletedPersonList().size() + 1);
        RestoreCommand restoreCommand = new RestoreCommand(List.of(outOfBoundIndex));
        assertCommandFailure(restoreCommand, model, PersonListView.DELETED_PERSONS,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        RestoreCommand restoreCommandMultiple = new RestoreCommand(List.of(INDEX_FIRST_PERSON, outOfBoundIndex));
        assertCommandFailure(restoreCommandMultiple, model, PersonListView.DELETED_PERSONS,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // ensures that model state is unchanged after failed command
        assertEquals(model, expectedModel);
    }

    @Test
    public void execute_validIndexListFilteredList_success() {
        setUpModelWithDeletedPersons();
        showDeletedPersonAtIndex(model, INDEX_FIRST_PERSON);
        Person personToRestore = model.getFilteredDeletedPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        RestoreCommand restoreCommand = new RestoreCommand(List.of(INDEX_FIRST_PERSON));

        String expectedMessage = RestoreCommand.buildSuccessMessage(List.of(personToRestore));
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showDeletedPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);
        expectedModel.restorePerson(personToRestore);

        assertCommandSuccess(restoreCommand, model, PersonListView.DELETED_PERSONS,
                expectedMessage, PersonListView.DELETED_PERSONS, expectedModel);
    }

    @Test
    public void execute_validIndexListFilteredListMultipleIndices_success() {
        setUpModelWithDeletedPersons();
        Person firstPersonToRestore = model.getFilteredDeletedPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPersonToRestore = model.getFilteredDeletedPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Predicate<Person> firstTwoPersons =
                p -> p.equals(firstPersonToRestore) || p.equals(secondPersonToRestore);
        model.updateFilteredDeletedPersonList(firstTwoPersons);
        assertEquals(2, model.getFilteredDeletedPersonList().size());

        RestoreCommand restoreCommand = new RestoreCommand(List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));

        String expectedMessage = RestoreCommand.buildSuccessMessage(
                List.of(firstPersonToRestore, secondPersonToRestore));
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredDeletedPersonList(firstTwoPersons);
        expectedModel.restorePerson(firstPersonToRestore);
        expectedModel.restorePerson(secondPersonToRestore);

        assertCommandSuccess(restoreCommand, model, PersonListView.DELETED_PERSONS,
                expectedMessage, PersonListView.DELETED_PERSONS, expectedModel);
    }

    @Test
    public void execute_invalidIndexListFilteredList_throwsCommandException() {
        setUpModelWithDeletedPersons();
        showDeletedPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getKeptPersonList().size());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showDeletedPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        RestoreCommand restoreCommand = new RestoreCommand(List.of(outOfBoundIndex));
        assertCommandFailure(restoreCommand, model, PersonListView.DELETED_PERSONS,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        RestoreCommand restoreCommandMultiple = new RestoreCommand(List.of(INDEX_FIRST_PERSON, outOfBoundIndex));
        assertCommandFailure(restoreCommandMultiple, model, PersonListView.DELETED_PERSONS,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // ensures that model state is unchanged after failed command
        assertEquals(model, expectedModel);
    }

    @Test
    public void execute_invalidIndexListEmptyFilteredList_throwsCommandException() {
        setUpModelWithDeletedPersons();
        model.updateFilteredDeletedPersonList(p -> false);

        RestoreCommand restoreCommand = new RestoreCommand(List.of(INDEX_FIRST_PERSON));
        assertCommandFailure(restoreCommand, model, PersonListView.DELETED_PERSONS,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals_indexListConstructor() {
        RestoreCommand restoreFirstCommand = new RestoreCommand(List.of(INDEX_FIRST_PERSON));
        RestoreCommand restoreSecondCommand = new RestoreCommand(List.of(INDEX_SECOND_PERSON));

        // same object -> returns true
        assertTrue(restoreFirstCommand.equals(restoreFirstCommand));

        // same values -> returns true
        RestoreCommand restoreFirstCommandCopy = new RestoreCommand(List.of(INDEX_FIRST_PERSON));
        assertTrue(restoreFirstCommand.equals(restoreFirstCommandCopy));

        // different types -> returns false
        assertFalse(restoreFirstCommand.equals(1));

        // null -> returns false
        assertFalse(restoreFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(restoreFirstCommand.equals(restoreSecondCommand));
    }

    @Test
    public void toStringMethod() {
        List<Index> targetIndices = List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        RestoreCommand restoreCommand = new RestoreCommand(targetIndices);
        String expected = RestoreCommand.class.getCanonicalName() + "{targetIndices=" + targetIndices + "}";
        assertEquals(expected, restoreCommand.toString());
    }

    @Test
    public void buildSuccessMessageMethod() {
        setUpModelWithDeletedPersons();
        Person firstPerson = model.getFilteredDeletedPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPerson = model.getFilteredDeletedPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        String expectedMessage = RestoreCommand.MESSAGE_RESTORE_PERSON_SUCCESS_PREFIX
                + RestoreCommand.MESSAGE_RESTORE_PERSON_SUCCESS_DELIMITER
                + Messages.format(firstPerson)
                + RestoreCommand.MESSAGE_RESTORE_PERSON_SUCCESS_DELIMITER
                + Messages.format(secondPerson);
        String actualMessage = RestoreCommand.buildSuccessMessage(List.of(firstPerson, secondPerson));

        assertEquals(expectedMessage, actualMessage);
    }
}

