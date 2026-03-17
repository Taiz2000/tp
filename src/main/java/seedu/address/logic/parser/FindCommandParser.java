package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MATCH_TYPE;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.predicate.PersonContainsKeywordsPredicate;

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

        List<String> keywords = parseKeywords(argMultimap);
        if (keywords.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return new FindCommand(new PersonContainsKeywordsPredicate(keywords));
    }

    private List<String> parseKeywords(ArgumentMultimap argMultimap) throws ParseException {
        Optional<String> matchTypeValue = argMultimap.getValue(PREFIX_MATCH_TYPE);
        String preamble = argMultimap.getPreamble().trim();

        if (matchTypeValue.isPresent()) {
            if (!preamble.isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }

            String matchTypeArgs = matchTypeValue.get().trim();
            if (matchTypeArgs.isEmpty()) {
                return List.of();
            }

            String[] matchTypeTokens = matchTypeArgs.split("\\s+");
            FindMatchType.fromToken(matchTypeTokens[0])
                    .orElseThrow(() -> new ParseException(
                            String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE)));

            if (matchTypeTokens.length == 1) {
                return List.of();
            }

            return Arrays.asList(Arrays.copyOfRange(matchTypeTokens, 1, matchTypeTokens.length));
        }

        if (preamble.isEmpty()) {
            return List.of();
        }

        return Arrays.asList(preamble.split("\\s+"));
    }

}
