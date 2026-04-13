package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_AVAILABILITY_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NOTES_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NOTES_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_RECORD_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.PersonListView;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 * Some test cases in this class were adapted from Codex-generated test specifications.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_notViewingKeptPersons_throwsCommandException() {
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        assertCommandFailure(editCommand, model, PersonListView.DELETED_PERSONS,
                Messages.MESSAGE_NOT_VIEWING_KEPT_PERSONS);
    }

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredKeptPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model,
                PersonListView.KEPT_PERSONS,
                expectedMessage,
                PersonListView.KEPT_PERSONS,
                expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredKeptPersonList().size());
        Person lastPerson = model.getFilteredKeptPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editCommand, model,
                PersonListView.KEPT_PERSONS,
                expectedMessage,
                PersonListView.KEPT_PERSONS,
                expectedModel);
    }

    @Test
    public void execute_roleAndNotesSpecifiedUnfilteredList_success() {
        Person personToEdit = model.getFilteredKeptPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personToEdit)
                .withRole(VALID_ROLE_BOB)
                .withNotes(VALID_NOTES_BOB)
                .build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withRole(VALID_ROLE_BOB)
                .withNotes(VALID_NOTES_BOB)
                .build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model,
                PersonListView.KEPT_PERSONS,
                expectedMessage,
                PersonListView.KEPT_PERSONS,
                expectedModel);
    }

    @Test
    public void execute_availabilityAndRecordsSpecifiedUnfilteredList_success() {
        Person personToEdit = model.getFilteredKeptPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personToEdit)
                .withAvailabilities(VALID_AVAILABILITY_AMY)
                .withRecords(VALID_RECORD_AMY)
                .build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withAvailabilities(VALID_AVAILABILITY_AMY)
                .withRecords(VALID_RECORD_AMY)
                .build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model,
                PersonListView.KEPT_PERSONS,
                expectedMessage,
                PersonListView.KEPT_PERSONS,
                expectedModel);
    }

    @Test
    public void execute_clearVolunteerSpecificFieldsUnfilteredList_success() {
        Person originalPerson = model.getFilteredKeptPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personToEdit = new PersonBuilder(originalPerson)
                .withRole(VALID_ROLE_AMY)
                .withNotes(VALID_NOTES_AMY)
                .withAvailabilities(VALID_AVAILABILITY_AMY)
                .withRecords(VALID_RECORD_AMY)
                .build();
        model.setPerson(originalPerson, personToEdit);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withRole("")
                .withNotes("")
                .withAvailabilities(new String[0])
                .withRecords(new String[0])
                .build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withRole("")
                .withNotes("")
                .withAvailabilities()
                .withRecords()
                .build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model,
                PersonListView.KEPT_PERSONS,
                expectedMessage,
                PersonListView.KEPT_PERSONS,
                expectedModel);
    }

    @Test
    public void execute_clearTagsUnfilteredList_success() {
        Person personToEdit = model.getFilteredKeptPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personToEdit).withTags().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withTags().build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model,
                PersonListView.KEPT_PERSONS,
                expectedMessage,
                PersonListView.KEPT_PERSONS,
                expectedModel);
    }

    @Test
    public void constructor_noFieldSpecified_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, EditCommand.MESSAGE_NOT_EDITED, () ->
                new EditCommand(INDEX_FIRST_PERSON, new EditPersonDescriptor()));
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredKeptPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredKeptPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model,
                PersonListView.KEPT_PERSONS,
                expectedMessage,
                PersonListView.KEPT_PERSONS,
                expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getFilteredKeptPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPerson = model.getFilteredKeptPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        // edit secondPerson into an exact duplicate of firstPerson
        EditPersonDescriptor duplicateDescriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        EditCommand duplicateEditCommand = new EditCommand(INDEX_SECOND_PERSON, duplicateDescriptor);

        assertCommandFailure(duplicateEditCommand, model, PersonListView.KEPT_PERSONS,
                EditCommand.MESSAGE_DUPLICATE_PERSON);

        // give secondPerson the same phone as firstPerson
        EditPersonDescriptor samePhoneDescriptor = new EditPersonDescriptor();
        samePhoneDescriptor.setPhone(firstPerson.getPhone());
        EditCommand samePhoneEditCommand = new EditCommand(INDEX_SECOND_PERSON, samePhoneDescriptor);

        assertCommandFailure(samePhoneEditCommand, model, PersonListView.KEPT_PERSONS,
                EditCommand.MESSAGE_DUPLICATE_PERSON);

        // give firstPerson the same email as secondPerson
        EditPersonDescriptor sameEmailDescriptor = new EditPersonDescriptor();
        sameEmailDescriptor.setEmail(secondPerson.getEmail());
        EditCommand sameEmailEditCommand = new EditCommand(INDEX_FIRST_PERSON, sameEmailDescriptor);

        assertCommandFailure(sameEmailEditCommand, model, PersonListView.KEPT_PERSONS,
                EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit person in filtered list into a duplicate in address book
        Person personInList = model.getAddressBook().getKeptPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder(personInList).build());

        assertCommandFailure(editCommand, model, PersonListView.KEPT_PERSONS,
                EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredKeptPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, PersonListView.KEPT_PERSONS,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getKeptPersonList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, PersonListView.KEPT_PERSONS,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand editCommand = new EditCommand(index, editPersonDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", editPersonDescriptor="
                + editPersonDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

}
