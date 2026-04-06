package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.CommandUtil.requireViewingKeptPersons;

import seedu.address.logic.PersonListView;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Cleared all persons.";

    @Override
    public CommandResult execute(Model model, PersonListView personListView) throws CommandException {
        requireNonNull(model);
        requireViewingKeptPersons(personListView);
        model.deleteAllPersons();
        return new CommandResult(MESSAGE_SUCCESS, PersonListView.KEPT_PERSONS);
    }
}
