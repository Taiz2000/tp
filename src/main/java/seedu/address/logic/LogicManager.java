package seedu.address.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.AliasCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.EditPreviousCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";
    public static final String MESSAGE_INVALID_ALIASES_REMOVED_ON_STARTUP =
            "Removed invalid aliases from preferences: %1$s";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final AddressBookParser addressBookParser;
    private Optional<String> startupMessage = Optional.empty();

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        addressBookParser = new AddressBookParser();
        sanitizeLoadedAliases();
    }

    @Override
    public CommandResult execute(String commandText, PersonListView personListView)
            throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        String expandedCommandText = expandAlias(commandText);
        Command command = addressBookParser.parseCommand(expandedCommandText);
        CommandResult commandResult = command.execute(model, personListView);

        // EditPreviousCommand preserves the current view instead of switching.
        if (command instanceof EditPreviousCommand) {
            commandResult = replacePersonListView(commandResult, personListView);
        }

        try {
            storage.saveAddressBook(model.getAddressBook());
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }

        if (!(command instanceof EditPreviousCommand)) {
            model.setLastCommandText(commandText);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }

    @Override
    public ObservableList<Person> getFilteredKeptPersonList() {
        return model.getFilteredKeptPersonList();
    }

    @Override
    public ObservableList<Person> getFilteredDeletedPersonList() {
        return model.getFilteredDeletedPersonList();
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }

    @Override
    public Optional<String> consumeStartupMessage() {
        Optional<String> message = startupMessage;
        startupMessage = Optional.empty();
        return message;
    }

    private String expandAlias(String commandText) {
        return ParserUtil.parseCommandComponents(commandText)
                .map(commandComponents -> expandAlias(commandText, commandComponents))
                .orElse(commandText);
    }

    private String expandAlias(String commandText, ParserUtil.CommandComponents commandComponents) {
        String targetCommandWord = model.getCommandAliases().get(commandComponents.getCommandWord());
        if (targetCommandWord == null) {
            return commandText;
        }

        return targetCommandWord + commandComponents.getArguments();
    }

    private void sanitizeLoadedAliases() {
        List<String> invalidAliases = model.getCommandAliases().entrySet().stream()
                .filter(entry -> !AliasCommand.isValidAliasName(entry.getKey())
                        || !AliasCommand.isValidAliasTemplate(entry.getValue())
                        || !AliasCommand.isAllowedAliasTarget(entry.getValue()))
                .map(entry -> entry.getKey())
                .toList();

        invalidAliases.forEach(model::removeCommandAlias);
        if (!invalidAliases.isEmpty()) {
            startupMessage = Optional.of(String.format(MESSAGE_INVALID_ALIASES_REMOVED_ON_STARTUP,
                    String.join(", ", invalidAliases)));
        }
    }

    /**
     * Returns a new {@code CommandResult} with the same fields but a different {@code PersonListView}.
     */
    private static CommandResult replacePersonListView(CommandResult result, PersonListView personListView) {
        return new CommandResult(
                result.getFeedbackToUser(),
                personListView,
                result.shouldShowHelp(),
                result.shouldExit(),
                result.getCommandTextToPopulate().orElse(null));
    }
}
