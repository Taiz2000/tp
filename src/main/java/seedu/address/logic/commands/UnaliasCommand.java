package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.PersonListView;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Removes a command alias.
 */
public class UnaliasCommand extends Command {

    public static final String COMMAND_WORD = "unalias";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes a command alias.\n"
            + "Parameters: SHORT\n"
            + "Example: " + COMMAND_WORD + " ls";
    public static final String MESSAGE_EXTRA_ARGUMENTS = "Too many arguments provided!\n" + MESSAGE_USAGE;
    public static final String MESSAGE_SUCCESS = "Alias removed: %1$s";
    public static final String MESSAGE_ALIAS_NOT_FOUND = "This alias does not exist.";

    private final String shortName;

    /**
     * Creates an UnaliasCommand.
     */
    public UnaliasCommand(String shortName) {
        requireNonNull(shortName);
        if (!AliasCommand.isValidAliasName(shortName)) {
            throw new IllegalArgumentException(AliasCommand.MESSAGE_INVALID_ALIAS_NAME);
        }
        this.shortName = shortName;
    }

    @Override
    public CommandResult execute(Model model, PersonListView personListView) throws CommandException {
        requireNonNull(model);

        if (!model.hasCommandAlias(shortName)) {
            throw new CommandException(MESSAGE_ALIAS_NOT_FOUND);
        }

        model.removeCommandAlias(shortName);
        return new CommandResult(String.format(MESSAGE_SUCCESS, shortName), personListView);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UnaliasCommand)) {
            return false;
        }

        UnaliasCommand otherUnaliasCommand = (UnaliasCommand) other;
        return shortName.equals(otherUnaliasCommand.shortName);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("shortName", shortName)
                .toString();
    }
}
