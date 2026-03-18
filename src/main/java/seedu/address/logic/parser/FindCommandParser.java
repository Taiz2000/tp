package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MATCH_TYPE;

import java.util.List;
import java.util.Optional;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.predicates.PersonPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(" " + args, PREFIX_MATCH_TYPE);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_MATCH_TYPE);

        ParsedFindArgs parsedFindArgs = parseFindArgs(argMultimap);
        if (parsedFindArgs.keywords().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        PersonPredicate predicate = parsedFindArgs.matchType().createPredicate(parsedFindArgs.keywords());
        return new FindCommand(predicate);
    }

    private ParsedFindArgs parseFindArgs(ArgumentMultimap argMultimap) throws ParseException {
        Optional<String> matchTypeValue = argMultimap.getValue(PREFIX_MATCH_TYPE);
        String preamble = argMultimap.getPreamble().trim();
        FindMatchType matchType = FindMatchType.KEYWORD;

        if (matchTypeValue.isPresent()) {
            if (!preamble.isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }

            String matchTypeArgs = matchTypeValue.get().trim();
            if (matchTypeArgs.isEmpty()) {
                return new ParsedFindArgs(matchType, List.of());
            }

            List<String> matchTypeTokens = ParserUtil.tokenizeSpaceSeparated(matchTypeArgs);
            matchType = FindMatchType.fromToken(matchTypeTokens.get(0))
                    .orElseThrow(() -> new ParseException(
                            String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE)));

            if (matchTypeTokens.size() == 1) {
                return new ParsedFindArgs(matchType, List.of());
            }

            return new ParsedFindArgs(matchType,
                    matchTypeTokens.subList(1, matchTypeTokens.size()));
        }

        if (preamble.isEmpty()) {
            return new ParsedFindArgs(matchType, List.of());
        }

        return new ParsedFindArgs(matchType, ParserUtil.tokenizeSpaceSeparated(preamble));
    }

    /**
     * Stores the parsed match type and keywords extracted from a find command.
     */
    private static final class ParsedFindArgs {
        private final FindMatchType matchType;
        private final List<String> keywords;

        private ParsedFindArgs(FindMatchType matchType, List<String> keywords) {
            this.matchType = matchType;
            this.keywords = keywords;
        }

        private FindMatchType matchType() {
            return matchType;
        }

        private List<String> keywords() {
            return keywords;
        }
    }
}
