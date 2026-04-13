package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.UnaliasCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnaliasCommand object.
 */
public class UnaliasCommandParser implements Parser<UnaliasCommand> {

    @Override
    public UnaliasCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnaliasCommand.MESSAGE_USAGE));
        }
        if (trimmedArgs.contains(" ")) {
            throw new ParseException(UnaliasCommand.MESSAGE_EXTRA_ARGUMENTS);
        }

        try {
            return new UnaliasCommand(trimmedArgs);
        } catch (IllegalArgumentException iae) {
            throw new ParseException(iae.getMessage(), iae);
        }
    }
}
