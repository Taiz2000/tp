package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AliasCommand;
import seedu.address.logic.commands.AliasesCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditPreviousCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.UnaliasCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.predicates.PersonContainsSubstringsPredicate;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.PersonBuilder;

/**
 * Some test cases in this class were adapted from Codex-generated test specifications.
 */
public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION = new AccessDeniedException("dummy access denied exception");

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        resetLogic();
    }

    private void resetLogic() {
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("rosterbolt.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, PersonListView.KEPT_PERSONS, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete 9";
        assertCommandException(deleteCommand, PersonListView.KEPT_PERSONS, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, PersonListView.KEPT_PERSONS,
                ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_aliasExpandedCommand_success() throws Exception {
        model.setCommandAlias("ls", ListCommand.COMMAND_WORD);
        assertCommandSuccess("ls", PersonListView.KEPT_PERSONS,
                ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_aliasExpandedCommandWithTrailingWhitespace_success() throws Exception {
        model.setCommandAlias("ls", ListCommand.COMMAND_WORD);
        assertCommandSuccess("ls   ", PersonListView.KEPT_PERSONS,
                ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_aliasExpandedCommandWithArguments_success() throws Exception {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.setCommandAlias("rm", DeleteCommand.COMMAND_WORD);
        resetLogic();

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setCommandAlias("rm", DeleteCommand.COMMAND_WORD);
        expectedModel.deletePerson(ALICE);

        assertCommandSuccess("rm 1", PersonListView.KEPT_PERSONS,
                DeleteCommand.buildSuccessMessage(List.of(ALICE)), expectedModel);
    }

    @Test
    public void execute_aliasExpandedCommandWithArgumentsAndTrailingWhitespace_success() throws Exception {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.setCommandAlias("rm", DeleteCommand.COMMAND_WORD);
        resetLogic();

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setCommandAlias("rm", DeleteCommand.COMMAND_WORD);
        expectedModel.deletePerson(ALICE);

        assertCommandSuccess("rm 1   ", PersonListView.KEPT_PERSONS,
                DeleteCommand.buildSuccessMessage(List.of(ALICE)), expectedModel);
    }

    @Test
    public void execute_aliasExpandedFindCommandWithArguments_success() throws Exception {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.setCommandAlias("ss", "find");
        resetLogic();

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setCommandAlias("ss", "find");
        expectedModel.updateFilteredKeptPersonList(
                new PersonContainsSubstringsPredicate(Collections.singletonList("meie")));

        assertCommandSuccess("ss m/ss meie", PersonListView.KEPT_PERSONS,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2), expectedModel);
    }

    @Test
    public void execute_aliasExpandedMetaCommand_success() throws Exception {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.setCommandAlias("wipe", ClearCommand.COMMAND_WORD);
        resetLogic();

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setCommandAlias("wipe", ClearCommand.COMMAND_WORD);
        new ClearCommand().execute(expectedModel, PersonListView.KEPT_PERSONS);

        assertCommandSuccess("wipe", PersonListView.KEPT_PERSONS,
                ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_validCommand_savesUserPrefs() throws Exception {
        model.setCommandAlias("ls", ListCommand.COMMAND_WORD);

        assertCommandSuccess(ListCommand.COMMAND_WORD, PersonListView.KEPT_PERSONS,
                ListCommand.MESSAGE_SUCCESS, model);

        UserPrefs readBack = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json")).readUserPrefs().get();
        assertEquals(Map.of("ls", ListCommand.COMMAND_WORD), readBack.getCommandAliases());
    }

    @Test
    public void execute_aliasCommandAndUseAlias_success() throws Exception {
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setCommandAlias("ls", ListCommand.COMMAND_WORD);

        assertCommandSuccess("alias ls list", PersonListView.DELETED_PERSONS,
                String.format(AliasCommand.MESSAGE_SUCCESS, "ls", ListCommand.COMMAND_WORD),
                expectedModel);
        assertCommandSuccess("ls", PersonListView.DELETED_PERSONS,
                ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_aliasesCommand_success() throws Exception {
        model.setCommandAlias("rm", DeleteCommand.COMMAND_WORD);
        model.setCommandAlias("ls", ListCommand.COMMAND_WORD);

        String expectedMessage = AliasesCommand.MESSAGE_ALIASES_HEADER
                + "\nls -> " + ListCommand.COMMAND_WORD
                + "\nrm -> " + DeleteCommand.COMMAND_WORD;
        assertCommandSuccess(AliasesCommand.COMMAND_WORD, PersonListView.KEPT_PERSONS,
                expectedMessage, model);
    }

    @Test
    public void execute_unaliasCommand_success() throws Exception {
        model.setCommandAlias("ls", ListCommand.COMMAND_WORD);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandSuccess("unalias ls", PersonListView.KEPT_PERSONS,
                String.format(UnaliasCommand.MESSAGE_SUCCESS, "ls"), expectedModel);
    }

    @Test
    public void execute_duplicateAliasCommand_throwsCommandException() {
        model.setCommandAlias("ls", ListCommand.COMMAND_WORD);
        assertCommandException("alias ls list", PersonListView.DELETED_PERSONS,
                AliasCommand.MESSAGE_DUPLICATE_ALIAS);
    }

    @Test
    public void execute_reservedAliasName_throwsParseException() {
        assertParseException("alias list help", PersonListView.KEPT_PERSONS,
                AliasCommand.MESSAGE_RESERVED_ALIAS_NAME);
        assertParseException("alias import help", PersonListView.KEPT_PERSONS,
                AliasCommand.MESSAGE_RESERVED_ALIAS_NAME);
        assertParseException("alias export help", PersonListView.KEPT_PERSONS,
                AliasCommand.MESSAGE_RESERVED_ALIAS_NAME);
        assertParseException("alias editprev help", PersonListView.KEPT_PERSONS,
                AliasCommand.MESSAGE_RESERVED_ALIAS_NAME);
    }

    @Test
    public void execute_invalidAliasTemplate_throwsParseException() {
        assertParseException("alias l ls", PersonListView.KEPT_PERSONS,
                AliasCommand.MESSAGE_INVALID_ALIAS_TEMPLATE);
        assertParseException("alias l find m/ss meie", PersonListView.KEPT_PERSONS,
                AliasCommand.MESSAGE_INVALID_ALIAS_TEMPLATE);
    }

    @Test
    public void execute_reservedAliasTarget_throwsParseException() {
        assertParseException("alias aa alias", PersonListView.KEPT_PERSONS,
                AliasCommand.MESSAGE_RESERVED_ALIAS_TARGET);
        assertParseException("alias aa aliases", PersonListView.KEPT_PERSONS,
                AliasCommand.MESSAGE_RESERVED_ALIAS_TARGET);
        assertParseException("alias aa unalias", PersonListView.KEPT_PERSONS,
                AliasCommand.MESSAGE_RESERVED_ALIAS_TARGET);
        assertParseException("alias aa editprev", PersonListView.KEPT_PERSONS,
                AliasCommand.MESSAGE_RESERVED_ALIAS_TARGET);
    }

    @Test
    public void constructor_invalidLoadedAliases_sanitizesAliasRegistry() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setCommandAliases(Map.of(
                "good", ListCommand.COMMAND_WORD,
                "chain", "good",
                "loop", "loop",
                "aa", EditPreviousCommand.COMMAND_WORD));
        model = new ModelManager(getTypicalAddressBook(), userPrefs);
        resetLogic();

        assertEquals(Map.of("good", ListCommand.COMMAND_WORD), model.getCommandAliases());
    }

    @Test
    public void consumeStartupMessage_afterSanitizingLoadedAliases_returnsOneTimeWarning() {
        UserPrefs userPrefs = new UserPrefs();
        Map<String, String> aliases = new LinkedHashMap<>();
        aliases.put("good", ListCommand.COMMAND_WORD);
        aliases.put("chain", "good");
        aliases.put("loop", "loop");
        aliases.put("aa", EditPreviousCommand.COMMAND_WORD);
        userPrefs.setCommandAliases(aliases);
        model = new ModelManager(getTypicalAddressBook(), userPrefs);
        resetLogic();

        String startupMessage = logic.consumeStartupMessage().orElseThrow();
        assertTrue(startupMessage.startsWith("Removed invalid aliases from preferences: "));
        assertTrue(startupMessage.contains("chain"));
        assertTrue(startupMessage.contains("loop"));
        assertTrue(startupMessage.contains("aa"));
        assertEquals(Optional.empty(), logic.consumeStartupMessage());
    }

    @Test
    public void consumeStartupMessage_withoutInvalidAliases_returnsEmpty() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setCommandAliases(Map.of("good", ListCommand.COMMAND_WORD));
        model = new ModelManager(getTypicalAddressBook(), userPrefs);
        resetLogic();

        assertEquals(Optional.empty(), logic.consumeStartupMessage());
    }

    @Test
    public void execute_afterSanitizingLoadedAliases_savesOnlyValidAliases() throws Exception {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setCommandAliases(Map.of(
                "good", ListCommand.COMMAND_WORD,
                "chain", "good",
                "loop", "loop",
                "meta", AliasCommand.COMMAND_WORD));
        model = new ModelManager(getTypicalAddressBook(), userPrefs);
        resetLogic();

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setCommandAlias("good", ListCommand.COMMAND_WORD);
        assertCommandSuccess(ListCommand.COMMAND_WORD, PersonListView.KEPT_PERSONS,
                ListCommand.MESSAGE_SUCCESS, expectedModel);

        UserPrefs readBack = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json")).readUserPrefs().get();
        assertEquals(Map.of("good", ListCommand.COMMAND_WORD), readBack.getCommandAliases());
    }

    @Test
    public void execute_editPreviousWithoutPreviousCommand_throwsCommandException() {
        assertCommandException(EditPreviousCommand.COMMAND_WORD, PersonListView.KEPT_PERSONS,
                EditPreviousCommand.MESSAGE_NO_PREVIOUS_COMMAND);
    }

    @Test
    public void execute_editPreviousListCommand_success() throws Exception {
        assertCommandSuccess(ListCommand.COMMAND_WORD, PersonListView.KEPT_PERSONS,
                ListCommand.MESSAGE_SUCCESS, model);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        CommandResult result = logic.execute(EditPreviousCommand.COMMAND_WORD, PersonListView.KEPT_PERSONS);

        assertEquals(String.format(EditPreviousCommand.MESSAGE_SUCCESS, ListCommand.COMMAND_WORD),
                result.getFeedbackToUser());
        assertEquals(Optional.of(ListCommand.COMMAND_WORD), result.getCommandTextToPopulate());
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_editPreviousDeleteCommand_success() throws Exception {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        resetLogic();

        String deleteCommand = DeleteCommand.COMMAND_WORD + " 1";
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.deletePerson(ALICE);
        assertCommandSuccess(deleteCommand,
                PersonListView.KEPT_PERSONS,
                DeleteCommand.buildSuccessMessage(List.of(ALICE)),
                expectedModel);

        CommandResult result = logic.execute(EditPreviousCommand.COMMAND_WORD, PersonListView.KEPT_PERSONS);
        assertEquals(String.format(EditPreviousCommand.MESSAGE_SUCCESS, deleteCommand),
                result.getFeedbackToUser());
        assertEquals(Optional.of(deleteCommand), result.getCommandTextToPopulate());
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_repeatedEditPrevious_success() throws Exception {
        assertCommandSuccess(ListCommand.COMMAND_WORD, PersonListView.DELETED_PERSONS,
                ListCommand.MESSAGE_SUCCESS, model);

        CommandResult firstResult = logic.execute(
                EditPreviousCommand.COMMAND_WORD, PersonListView.KEPT_PERSONS);
        assertEquals(String.format(EditPreviousCommand.MESSAGE_SUCCESS, ListCommand.COMMAND_WORD),
                firstResult.getFeedbackToUser());
        assertEquals(Optional.of(ListCommand.COMMAND_WORD), firstResult.getCommandTextToPopulate());

        CommandResult secondResult = logic.execute(
                EditPreviousCommand.COMMAND_WORD, PersonListView.KEPT_PERSONS);
        assertEquals(String.format(EditPreviousCommand.MESSAGE_SUCCESS, ListCommand.COMMAND_WORD),
                secondResult.getFeedbackToUser());
        assertEquals(Optional.of(ListCommand.COMMAND_WORD), secondResult.getCommandTextToPopulate());
    }

    @Test
    public void execute_storageThrowsIoException_returnsCommandResultWithSaveWarning() throws Exception {
        assertCommandSuccessWithStorageWarning(DUMMY_IO_EXCEPTION, String.format(
                LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_storageThrowsAdException_returnsCommandResultWithSaveWarning() throws Exception {
        assertCommandSuccessWithStorageWarning(DUMMY_AD_EXCEPTION, String.format(
                LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT, DUMMY_AD_EXCEPTION.getMessage()));
    }

    @Test
    public void getFilteredKeptPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredKeptPersonList().remove(0));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, PersonListView, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, PersonListView personListView,
            String expectedMessage, Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand, personListView);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, PersonListView, Class, String, Model)
     */
    private void assertParseException(String inputCommand, PersonListView personListView, String expectedMessage) {
        assertCommandFailure(inputCommand, personListView, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, PersonListView, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, PersonListView personListView, String expectedMessage) {
        assertCommandFailure(inputCommand, personListView, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, PersonListView, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, PersonListView personListView,
            Class<? extends Throwable> expectedException, String expectedMessage) {
        Model expectedModel = new ModelManager(model.getAddressBook(), model.getUserPrefs());
        assertCommandFailure(inputCommand, personListView, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, PersonListView, String, Model)
     */
    private void assertCommandFailure(String inputCommand, PersonListView personListView,
            Class<? extends Throwable> expectedException, String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand, personListView));
        assertEquals(expectedModel, model);
    }

    /**
     * Tests the Logic component's handling of an {@code IOException} thrown by the Storage component.
     *
     * @param e the exception to be thrown by the Storage component
     * @param expectedStorageMessage the storage warning message expected inside the returned command result
     */
    private void assertCommandSuccessWithStorageWarning(IOException e, String expectedStorageMessage)
            throws Exception {
        Path prefPath = temporaryFolder.resolve("ExceptionUserPrefs.json");

        // Inject LogicManager with an AddressBookStorage that throws the IOException e when saving
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(prefPath) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath)
                    throws IOException {
                throw e;
            }
        };

        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);

        logic = new LogicManager(model, storage);

        // Triggers the saveAddressBook method by executing an add command
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        Person expectedPerson = new PersonBuilder(AMY).withTags().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addPerson(expectedPerson);

        CommandResult result = logic.execute(addCommand, PersonListView.KEPT_PERSONS);

        assertTrue(result.getFeedbackToUser().contains(String.format(AddCommand.MESSAGE_SUCCESS,
                Messages.format(expectedPerson))));
        assertTrue(result.getFeedbackToUser().contains(expectedStorageMessage));
        assertTrue(result.getFeedbackToUser().contains(LogicManager.FILE_OPS_UNSAVED_WARNING));
        assertEquals(PersonListView.KEPT_PERSONS, result.getPersonListView());
        assertFalse(result.shouldShowHelp());
        assertFalse(result.shouldExit());
        assertEquals(Optional.empty(), result.getCommandTextToPopulate());
        assertEquals(addCommand, model.getLastCommandText());
        assertEquals(expectedModel, model);
    }
}
