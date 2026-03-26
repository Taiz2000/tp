package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.AliasCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AliasCommand object.
 */
public class AliasCommandParser implements Parser<AliasCommand> {

    @Override
    public AliasCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ParserUtil.CommandComponents commandComponents = ParserUtil.parseCommandComponents(args)
                .orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, AliasCommand.MESSAGE_USAGE)));

        String shortName = commandComponents.getCommandWord();
        String template = commandComponents.getArguments().trim();
        if (template.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AliasCommand.MESSAGE_USAGE));
        }

        try {
            return new AliasCommand(shortName, template);
        } catch (IllegalArgumentException iae) {
            throw new ParseException(iae.getMessage(), iae);
        }
    }
}
