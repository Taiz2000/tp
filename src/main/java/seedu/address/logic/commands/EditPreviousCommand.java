package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.PersonListView;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Loads the last successfully executed command into the command box for editing.
 */
public class EditPreviousCommand extends Command {

    public static final String COMMAND_WORD = "editprev";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Loads the last successfully executed command, excluding editprev, into the command box "
            + "for editing.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_NO_PREVIOUS_COMMAND = "There is no previous command to edit.";
    public static final String MESSAGE_SUCCESS = "Loaded previous command for editing: %1$s";

    @Override
    public CommandResult execute(Model model, PersonListView personListView) throws CommandException {
        requireNonNull(model);

        String lastCommandText = model.getLastCommandText();
        if (lastCommandText == null) {
            throw new CommandException(MESSAGE_NO_PREVIOUS_COMMAND);
        }

        return new CommandResult(
                String.format(MESSAGE_SUCCESS, lastCommandText),
                PersonListView.KEPT_PERSONS,
                false,
                false,
                lastCommandText);
    }
}
