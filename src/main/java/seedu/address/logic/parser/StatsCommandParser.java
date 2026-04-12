package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import seedu.address.logic.commands.StatsCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.statistics.StatisticsCategory;

/**
 * Parses input arguments and creates a new StatsCommand object.
 */
public class StatsCommandParser implements Parser<StatsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the StatsCommand
     * and returns a StatsCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public StatsCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, StatsCommand.MESSAGE_USAGE));
        }

        List<String> tokens = ParserUtil.tokenizeSpaceSeparated(trimmedArgs);
        if (tokens.size() != 1) {
            throw new ParseException(StatsCommand.MESSAGE_EXTRA_ARGUMENTS);
        }

        StatisticsCategory category = StatisticsCategory.fromToken(tokens.get(0))
                .orElseThrow(() -> new ParseException(
                        String.format(StatsCommand.MESSAGE_UNKNOWN_CATEGORY, tokens.get(0))));

        return new StatsCommand(category);
    }
}
