package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AVAILABILITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RECORD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.PersonListView;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.predicates.PersonContainsKeywordsPredicate;
import seedu.address.testutil.EditPersonDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 * Some test cases in this class were adapted from Codex-generated test specifications.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";
    public static final String VALID_ROLE_AMY = "Leader";
    public static final String VALID_ROLE_BOB = "Medic";
    public static final String VALID_NOTES_AMY = "Available on weekdays";
    public static final String VALID_NOTES_BOB = "Prefers outdoor events";
    public static final String VALID_AVAILABILITY_MONDAY_MORNING = "MONDAY,09:00,12:00";
    public static final String VALID_AVAILABILITY_TUESDAY_AFTERNOON = "TUESDAY,14:00,17:00";
    public static final String VALID_AVAILABILITY_AMY = VALID_AVAILABILITY_MONDAY_MORNING;
    public static final String VALID_AVAILABILITY_BOB = VALID_AVAILABILITY_TUESDAY_AFTERNOON;
    public static final String VALID_RECORD_AMY = "2026-03-20T09:00,2026-03-20T12:00";
    public static final String VALID_RECORD_BOB = "2026-03-21T14:00,2026-03-21T17:00";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;
    public static final String ROLE_DESC_AMY = " " + PREFIX_ROLE + VALID_ROLE_AMY;
    public static final String ROLE_DESC_BOB = " " + PREFIX_ROLE + VALID_ROLE_BOB;
    public static final String NOTES_DESC_AMY = " " + PREFIX_NOTES + VALID_NOTES_AMY;
    public static final String NOTES_DESC_BOB = " " + PREFIX_NOTES + VALID_NOTES_BOB;
    public static final String AVAILABILITY_DESC_AMY = " " + PREFIX_AVAILABILITY + VALID_AVAILABILITY_AMY;
    public static final String AVAILABILITY_DESC_BOB = " " + PREFIX_AVAILABILITY + VALID_AVAILABILITY_BOB;
    public static final String RECORD_DESC_AMY = " " + PREFIX_RECORD + VALID_RECORD_AMY;
    public static final String RECORD_DESC_BOB = " " + PREFIX_RECORD + VALID_RECORD_BOB;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags
    public static final String INVALID_AVAILABILITY_DESC = " " + PREFIX_AVAILABILITY + "MONDAY-0900-1200";
    public static final String INVALID_RECORD_DESC = " " + PREFIX_RECORD + "2026-03-20 09:00,2026-03-20 12:00";

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditPersonDescriptor DESC_AMY;
    public static final EditCommand.EditPersonDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withRole(VALID_ROLE_AMY).withNotes(VALID_NOTES_AMY)
                .withAvailabilities(VALID_AVAILABILITY_AMY)
                .withRecords(VALID_RECORD_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        DESC_BOB = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .withRole(VALID_ROLE_BOB).withNotes(VALID_NOTES_BOB)
                .withAvailabilities(VALID_AVAILABILITY_BOB)
                .withRecords(VALID_RECORD_BOB)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     *
     * @deprecated Use {@link #assertCommandSuccess(Command, Model, PersonListView, CommandResult, Model)} instead.
     */
    @Deprecated
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel, PersonListView.KEPT_PERSONS);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Executes the given {@code command} with the specified {@code personListView}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, PersonListView personListView,
            CommandResult expectedCommandResult, Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel, personListView);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     *
     * @deprecated Use {@link #assertCommandSuccess(Command, Model, PersonListView, String, PersonListView, Model)}
     *         instead.
     */
    @Deprecated
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, PersonListView.KEPT_PERSONS);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage} and PersonListView {@code expectedPersonListView}.
     *
     * @deprecated Use {@link #assertCommandSuccess(Command, Model, PersonListView, String, PersonListView, Model)}
     *         instead.
     */
    @Deprecated
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            PersonListView expectedPersonListView, Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, expectedPersonListView);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, PersonListView, CommandResult, Model)}
     * that takes a string {@code expectedMessage} and PersonListView {@code expectedPersonListView}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, PersonListView personListView,
            String expectedMessage, PersonListView expectedPersonListView,
            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, expectedPersonListView);
        assertCommandSuccess(command, actualModel, personListView, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered person list and selected person in {@code actualModel} remain unchanged
     *
     * @deprecated Use {@link #assertCommandFailure(Command, Model, PersonListView, String)} instead.
     */
    @Deprecated
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        UserPrefs expectedUserPrefs = new UserPrefs(actualModel.getUserPrefs());
        List<Person> expectedFilteredKeptList = new ArrayList<>(actualModel.getFilteredKeptPersonList());
        List<Person> expectedFilteredDeletedList = new ArrayList<>(actualModel.getFilteredDeletedPersonList());

        assertThrows(CommandException.class,
            expectedMessage, () -> command.execute(actualModel, PersonListView.KEPT_PERSONS));
        assertEquals(expectedAddressBook, actualModel.getAddressBook());
        assertEquals(expectedUserPrefs, actualModel.getUserPrefs());
        assertEquals(expectedFilteredKeptList, actualModel.getFilteredKeptPersonList());
        assertEquals(expectedFilteredDeletedList, actualModel.getFilteredDeletedPersonList());
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered person list and selected person in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, PersonListView personListView,
            String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        UserPrefs expectedUserPrefs = new UserPrefs(actualModel.getUserPrefs());
        List<Person> expectedFilteredKeptList = new ArrayList<>(actualModel.getFilteredKeptPersonList());
        List<Person> expectedFilteredDeletedList = new ArrayList<>(actualModel.getFilteredDeletedPersonList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel, personListView));
        assertEquals(expectedAddressBook, actualModel.getAddressBook());
        assertEquals(expectedUserPrefs, actualModel.getUserPrefs());
        assertEquals(expectedFilteredKeptList, actualModel.getFilteredKeptPersonList());
        assertEquals(expectedFilteredDeletedList, actualModel.getFilteredDeletedPersonList());
    }

    /**
     * Updates {@code model}'s filtered list to show only the person at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredKeptPersonList().size());

        Person person = model.getFilteredKeptPersonList().get(targetIndex.getZeroBased());
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredKeptPersonList(new PersonContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredKeptPersonList().size());
    }

    /**
     * Updates {@code model}'s filtered deleted list to show only the person at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showDeletedPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredDeletedPersonList().size());

        Person person = model.getFilteredDeletedPersonList().get(targetIndex.getZeroBased());
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredDeletedPersonList(new PersonContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredDeletedPersonList().size());
    }

    /**
     * Updates {@code model}'s filtered kept list to show no one.
     */
    public static void showNoPerson(Model model) {
        model.updateFilteredKeptPersonList(p -> false);

        assertTrue(model.getFilteredKeptPersonList().isEmpty());
    }

    /**
     * Updates {@code model}'s filtered deleted list to show no one.
     */
    public static void showNoDeletedPerson(Model model) {
        model.updateFilteredDeletedPersonList(p -> false);

        assertTrue(model.getFilteredDeletedPersonList().isEmpty());
    }

}
