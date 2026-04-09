package seedu.address.logic.commands;

import seedu.address.logic.PersonListView;
import seedu.address.model.Model;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";

    public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Exiting RosterBolt as requested ...";

    @Override
    public CommandResult execute(Model model, PersonListView personListView) {
        return new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT, personListView, false, true);
    }

}
