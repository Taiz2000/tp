package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.PersonListView;
import seedu.address.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Cleared all persons.";

    @Override
    public CommandResult execute(Model model, PersonListView personListView) {
        requireNonNull(model);
        model.deleteAllPersons();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
