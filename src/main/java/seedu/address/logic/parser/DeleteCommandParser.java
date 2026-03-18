package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        List<String> tokens = ParserUtil.tokenizeSpaceSeparated(args);
        if (tokens.isEmpty()) {
            throwInvalidCommandFormatParseException();
        }

        List<Index> indices = new ArrayList<>();
        for (String oneBasedIndexString : tokens) {
            try {
                Index index = ParserUtil.parseIndex(oneBasedIndexString);
                indices.add(index);
            } catch (ParseException pe) {
                throwInvalidCommandFormatParseException();
            }
        }

        List<Index> sortedDistinctIndices = indices.stream()
                .distinct()
                .sorted()
                .toList();
        return new DeleteCommand(sortedDistinctIndices);
    }

    private void throwInvalidCommandFormatParseException() throws ParseException {
        throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

}
