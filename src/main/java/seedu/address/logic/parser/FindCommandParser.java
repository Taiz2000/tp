package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AVAILABILITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MATCH_TYPE;

import java.util.List;
import java.util.Optional;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.VolunteerAvailability;
import seedu.address.model.person.predicates.CombinedAndPersonPredicate;
import seedu.address.model.person.predicates.PersonAvailableDuringPredicate;
import seedu.address.model.person.predicates.PersonContainsFieldsPredicate;
import seedu.address.model.person.predicates.PersonPredicate;

/**
 * Parses input arguments and creates a new FindCommand object.
 *
 * <p>Accepted formats:
 * <ul>
 *   <li>{@code find KEYWORD [MORE_KEYWORDS]}</li>
 *   <li>{@code find m/MATCH_TYPE KEYWORD [MORE_KEYWORDS]}</li>
 *   <li>{@code find va/DAY,HH:mm,HH:mm [KEYWORD ...]}</li>
 *   <li>{@code find m/MATCH_TYPE va/DAY,HH:mm,HH:mm [KEYWORD ...]}</li>
 * </ul>
 */
public class FindCommandParser implements Parser<FindCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                " " + args, PREFIX_MATCH_TYPE, PREFIX_AVAILABILITY);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_MATCH_TYPE, PREFIX_AVAILABILITY);

        ParsedFindArgs parsed = parseFindArgs(argMultimap);

        boolean hasMatchType = argMultimap.getValue(PREFIX_MATCH_TYPE).isPresent();
        boolean hasKeywords = !parsed.keywords().isEmpty();
        boolean hasAvailability = parsed.availability().isPresent();

        if (!hasKeywords && !hasAvailability) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        if (hasMatchType && !hasKeywords) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        PersonPredicate predicate = buildPredicate(parsed);
        return new FindCommand(predicate);
    }

    /**
     * Parses the argument multimap into match type, availability, and keywords.
     *
     * <p>Keywords are extracted from the trailing content of the last prefix present,
     * or from the preamble when no prefixes are used.
     */
    private ParsedFindArgs parseFindArgs(ArgumentMultimap argMultimap) throws ParseException {
        Optional<String> matchTypeValue = argMultimap.getValue(PREFIX_MATCH_TYPE);
        Optional<String> availValue = argMultimap.getValue(PREFIX_AVAILABILITY);
        String preamble = argMultimap.getPreamble().trim();

        boolean hasMatchType = matchTypeValue.isPresent();
        boolean hasAvail = availValue.isPresent();

        // Preamble must be empty when any prefix is used
        if (!preamble.isEmpty() && (hasMatchType || hasAvail)) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        FindMatchType matchType = parseMatchType(matchTypeValue, hasAvail);
        Optional<VolunteerAvailability> availability = Optional.empty();
        List<String> keywords = List.of();

        if (hasAvail) {
            // va/ present: first token is availability, rest are keywords
            AvailabilityAndKeywords availResult = parseAvailabilityValue(availValue.get());
            availability = Optional.of(availResult.availability());
            keywords = availResult.keywords();
        } else if (hasMatchType) {
            // m/ only: keywords trail after match type token (existing behaviour)
            keywords = parseKeywordsFromMatchType(matchTypeValue.get());
        } else {
            // No prefixes: keywords from preamble
            if (!preamble.isEmpty()) {
                keywords = ParserUtil.tokenizeSpaceSeparated(preamble);
            }
        }

        return new ParsedFindArgs(matchType, availability, keywords);
    }

    /**
     * Extracts the match type from the m/ value. When va/ is also present,
     * the m/ value must contain exactly the match type token and nothing else.
     */
    private FindMatchType parseMatchType(Optional<String> matchTypeValue,
            boolean hasAvail) throws ParseException {
        if (matchTypeValue.isEmpty()) {
            return FindMatchType.KEYWORD;
        }

        String trimmed = matchTypeValue.get().trim();
        if (trimmed.isEmpty()) {
            return FindMatchType.KEYWORD;
        }

        List<String> tokens = ParserUtil.tokenizeSpaceSeparated(trimmed);
        FindMatchType matchType = FindMatchType.fromToken(tokens.get(0))
                .orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE)));

        if (hasAvail) {
            // When va/ is present, m/ must only contain the match type token
            if (tokens.size() > 1) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
            return matchType;
        }

        // When va/ is absent, keywords may trail after match type — handled separately
        return matchType;
    }

    /**
     * Extracts keywords from the m/ value after the match type token.
     * Used only when va/ is absent.
     */
    private List<String> parseKeywordsFromMatchType(String matchTypeRaw) {
        String trimmed = matchTypeRaw.trim();
        if (trimmed.isEmpty()) {
            return List.of();
        }
        List<String> tokens = ParserUtil.tokenizeSpaceSeparated(trimmed);
        if (tokens.size() <= 1) {
            return List.of();
        }
        return tokens.subList(1, tokens.size());
    }

    /**
     * Parses the va/ value into an availability (first token) and trailing keywords.
     */
    private AvailabilityAndKeywords parseAvailabilityValue(String availRaw) throws ParseException {
        String trimmed = availRaw.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        List<String> tokens = ParserUtil.tokenizeSpaceSeparated(trimmed);
        String availToken = tokens.get(0);

        if (!VolunteerAvailability.isValidAvailability(availToken)) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        VolunteerAvailability availability = VolunteerAvailability.fromString(availToken);
        List<String> keywords = tokens.size() > 1
                ? tokens.subList(1, tokens.size())
                : List.of();

        return new AvailabilityAndKeywords(availability, keywords);
    }

    /**
     * Builds the appropriate predicate from parsed arguments.
     */
    private PersonPredicate buildPredicate(ParsedFindArgs parsed) {
        boolean hasKeywords = !parsed.keywords().isEmpty();
        boolean hasAvailability = parsed.availability().isPresent();

        if (hasKeywords && hasAvailability) {
            PersonContainsFieldsPredicate textPredicate =
                    PersonContainsFieldsPredicateFactory.createPredicate(
                            parsed.matchType(), parsed.keywords());
            PersonAvailableDuringPredicate availPredicate =
                    new PersonAvailableDuringPredicate(parsed.availability().get());
            return new CombinedAndPersonPredicate(List.of(textPredicate, availPredicate));
        }

        if (hasAvailability) {
            return new PersonAvailableDuringPredicate(parsed.availability().get());
        }

        return PersonContainsFieldsPredicateFactory.createPredicate(
                parsed.matchType(), parsed.keywords());
    }

    /**
     * Stores all parsed arguments from a find command.
     */
    private static final class ParsedFindArgs {
        private final FindMatchType matchType;
        private final Optional<VolunteerAvailability> availability;
        private final List<String> keywords;

        private ParsedFindArgs(FindMatchType matchType, Optional<VolunteerAvailability> availability,
                List<String> keywords) {
            this.matchType = matchType;
            this.availability = availability;
            this.keywords = keywords;
        }

        private FindMatchType matchType() {
            return matchType;
        }

        private Optional<VolunteerAvailability> availability() {
            return availability;
        }

        private List<String> keywords() {
            return keywords;
        }
    }

    /**
     * Holds the parsed availability and trailing keywords from a va/ value.
     */
    private static final class AvailabilityAndKeywords {
        private final VolunteerAvailability availability;
        private final List<String> keywords;

        private AvailabilityAndKeywords(VolunteerAvailability availability, List<String> keywords) {
            this.availability = availability;
            this.keywords = keywords;
        }

        private VolunteerAvailability availability() {
            return availability;
        }

        private List<String> keywords() {
            return keywords;
        }
    }
}
