package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.sort.SortAttribute;
import seedu.address.model.person.sort.SortOrder;

/**
 * Parses input arguments and creates a new {@code ListCommand} object.
 */
public class ListCommandParser implements Parser<ListCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListCommand
     * and returns a ListCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListCommand parse(String args) throws ParseException {
        List<String> tokens = ParserUtil.tokenizeSpaceSeparated(args);
        if (tokens.isEmpty()) {
            return new ListCommand();
        }

        if (tokens.size() > 2) {
            throw getInvalidCommandFormatParseException();
        }

        SortAttribute attribute = SortAttribute.fromToken(tokens.get(0))
                .orElseThrow(this::getInvalidCommandFormatParseException);

        SortOrder order = SortOrder.ASC;
        if (tokens.size() == 2) {
            order = SortOrder.fromToken(tokens.get(1))
                    .orElseThrow(this::getInvalidCommandFormatParseException);
        }

        return new ListCommand(attribute, order);
    }

    private ParseException getInvalidCommandFormatParseException() {
        return new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
    }
}
