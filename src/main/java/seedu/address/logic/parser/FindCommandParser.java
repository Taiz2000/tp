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
 * Parses input arguments and creates a new {@code FindCommand}.
 *
 * <p>Both prefixes are optional and may appear in any order.
 * Search terms always trail after the last prefix (or stand alone when no prefix is used).
 *
 * <h3>Accepted forms</h3>
 * <ul>
 *   <li>{@code find alice bob}             — keyword search only</li>
 *   <li>{@code find m/ss ali}              — substring search only</li>
 *   <li>{@code find va/MONDAY,14:00,17:00} — availability filter only</li>
 *   <li>{@code find m/kw va/MONDAY,14:00,17:00 alice} — both (AND)</li>
 *   <li>{@code find va/MONDAY,14:00,17:00 m/kw alice} — both (AND, swapped prefix order)</li>
 * </ul>
 *
 * <h3>Validation rules</h3>
 * <ul>
 *   <li>At least one of search terms or {@code va/} must be present.</li>
 *   <li>If {@code m/} is present, at least one search term must also be present
 *       (a match type without search terms is meaningless).</li>
 *   <li>No text may appear before the first prefix (preamble must be empty
 *       when any prefix is used).</li>
 * </ul>
 *
 * <h3>How ArgumentTokenizer assigns trailing content</h3>
 * <p>{@code ArgumentTokenizer} splits the input by prefix positions. Each prefix's
 * value spans from just after the prefix to just before the next prefix (or end of input).
 * This means trailing search terms are always attached to whichever prefix appears last.
 * For example:
 * <ul>
 *   <li>{@code m/kw va/MONDAY,14:00,17:00 alice}
 *       → m/="kw", va/="MONDAY,14:00,17:00 alice"</li>
 *   <li>{@code va/MONDAY,14:00,17:00 m/kw alice}
 *       → va/="MONDAY,14:00,17:00", m/="kw alice"</li>
 * </ul>
 * <p>The parser extracts each prefix's primary token (match type or availability),
 * then collects any remaining tokens as search terms.
 */
public class FindCommandParser implements Parser<FindCommand> {

    @Override
    public FindCommand parse(String args) throws ParseException {
        ParsedFindArgs parsed = parseArgs(args);
        validate(parsed);
        PersonPredicate predicate = buildPredicate(parsed);
        return new FindCommand(predicate);
    }

    /**
     * Parses the raw argument string into a {@code ParsedFindArgs}.
     *
     * <p>The method:
     * <ol>
     *   <li>Tokenizes the input, splitting by {@code m/} and {@code va/} prefixes.</li>
     *   <li>Rejects any text appearing before the first prefix (preamble).</li>
     *   <li>Extracts the match type from {@code m/} (first token of its value).</li>
     *   <li>Extracts the availability from {@code va/} (first token of its value).</li>
     *   <li>Collects search terms from trailing tokens of whichever prefix is last,
     *       or from the preamble if no prefix is used.</li>
     * </ol>
     */
    private ParsedFindArgs parseArgs(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                " " + args, PREFIX_MATCH_TYPE, PREFIX_AVAILABILITY);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_MATCH_TYPE, PREFIX_AVAILABILITY);
        argMultimap.verifyNoUnknownPrefixes("m/, va/");

        Optional<String> rawMatchType = argMultimap.getValue(PREFIX_MATCH_TYPE);
        Optional<String> rawAvail = argMultimap.getValue(PREFIX_AVAILABILITY);
        String preamble = argMultimap.getPreamble().trim();
        boolean hasAnyPrefix = rawMatchType.isPresent() || rawAvail.isPresent();

        // Rule: no free-standing text before the first prefix.
        if (!preamble.isEmpty() && hasAnyPrefix) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        FindMatchType matchType = parseMatchType(rawMatchType);
        VolunteerAvailability availability = parseAvailability(rawAvail);
        List<String> searchTerms = collectSearchTerms(rawMatchType, rawAvail, preamble);

        return new ParsedFindArgs(matchType, availability, searchTerms);
    }

    /**
     * Extracts the match type from the {@code m/} value.
     *
     * @return the parsed {@code FindMatchType}, or {@code null} if {@code m/} was not provided.
     * @throws ParseException if {@code m/} is present but empty or contains an invalid token.
     */
    private FindMatchType parseMatchType(Optional<String> rawMatchType) throws ParseException {
        if (rawMatchType.isEmpty()) {
            return null;
        }

        String trimmed = rawMatchType.get().trim();

        // m/ was provided but empty (e.g. "find m/ va/...")
        if (trimmed.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        // First token is the match type; any remaining tokens are search terms (handled later).
        String matchTypeToken = ParserUtil.tokenizeSpaceSeparated(trimmed).get(0);
        return FindMatchType.fromToken(matchTypeToken)
                .orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE)));
    }

    /**
     * Extracts the availability from the {@code va/} value.
     *
     * @return the parsed {@code VolunteerAvailability}, or {@code null} if {@code va/} was not provided.
     * @throws ParseException if {@code va/} is present but empty or contains an invalid availability.
     */
    private VolunteerAvailability parseAvailability(Optional<String> rawAvail) throws ParseException {
        if (rawAvail.isEmpty()) {
            return null;
        }

        String trimmed = rawAvail.get().trim();

        // va/ was provided but empty (e.g. "find va/")
        if (trimmed.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        // First token is the availability; any remaining tokens are search terms (handled later).
        String availToken = ParserUtil.tokenizeSpaceSeparated(trimmed).get(0);
        if (!VolunteerAvailability.isValidAvailability(availToken)) {
            throw new ParseException(VolunteerAvailability.MESSAGE_CONSTRAINTS);
        }
        return VolunteerAvailability.fromString(availToken);
    }

    /**
     * Collects search terms from the appropriate source.
     *
     * <p>Search terms come from exactly one of three places:
     * <ol>
     *   <li>Trailing tokens after the match type in {@code m/} (when {@code m/} is the last prefix).</li>
     *   <li>Trailing tokens after the availability in {@code va/} (when {@code va/} is the last prefix).</li>
     *   <li>The preamble (when no prefix is used, e.g. {@code find alice bob}).</li>
     * </ol>
     *
     * <p>Each prefix's value has one primary token (the match type or availability string).
     * Any tokens after that primary token are search terms. Since {@code ArgumentTokenizer}
     * assigns all trailing content to the last prefix, only the last prefix can have search terms.
     */
    private List<String> collectSearchTerms(Optional<String> rawMatchType, Optional<String> rawAvail,
            String preamble) {
        // Try m/ trailing tokens: skip first token (match type), rest are search terms.
        List<String> matchTypeTokens = ParserUtil.tokenizeOptionalValue(rawMatchType);
        if (matchTypeTokens.size() > 1) {
            return matchTypeTokens.subList(1, matchTypeTokens.size());
        }

        // Try va/ trailing tokens: skip first token (availability), rest are search terms.
        List<String> availTokens = ParserUtil.tokenizeOptionalValue(rawAvail);
        if (availTokens.size() > 1) {
            return availTokens.subList(1, availTokens.size());
        }

        // No prefix — search terms come from preamble (e.g. "find alice bob").
        if (!preamble.isEmpty()) {
            return ParserUtil.tokenizeSpaceSeparated(preamble);
        }

        return List.of();
    }

    /**
     * Validates the parsed arguments.
     *
     * @throws ParseException if neither search terms nor availability is present,
     *     or if a match type is specified without any search terms.
     */
    private void validate(ParsedFindArgs parsed) throws ParseException {
        boolean hasSearchTerms = !parsed.searchTerms.isEmpty();
        boolean hasAvailability = parsed.availability != null;
        boolean hasMatchType = parsed.matchType != null;

        if (!hasSearchTerms && !hasAvailability) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        if (hasMatchType && !hasSearchTerms) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Builds the appropriate predicate based on which arguments are present.
     *
     * <ul>
     *   <li>Search terms + availability → {@code CombinedAndPersonPredicate} (both must match).</li>
     *   <li>Availability only → {@code PersonAvailableDuringPredicate}.</li>
     *   <li>Search terms only → text predicate via {@code PersonContainsFieldsPredicateFactory}.</li>
     * </ul>
     *
     * <p>When no explicit match type was provided, defaults to {@code FindMatchType.KEYWORD}.
     */
    private PersonPredicate buildPredicate(ParsedFindArgs parsed) {
        FindMatchType matchType = parsed.matchType != null
                ? parsed.matchType
                : FindMatchType.KEYWORD;
        boolean hasSearchTerms = !parsed.searchTerms.isEmpty();
        boolean hasAvailability = parsed.availability != null;

        if (hasSearchTerms && hasAvailability) {
            PersonContainsFieldsPredicate textPredicate =
                    PersonContainsFieldsPredicateFactory.createPredicate(matchType, parsed.searchTerms);
            PersonAvailableDuringPredicate availPredicate =
                    new PersonAvailableDuringPredicate(parsed.availability);
            return new CombinedAndPersonPredicate(List.of(textPredicate, availPredicate));
        }

        if (hasAvailability) {
            return new PersonAvailableDuringPredicate(parsed.availability);
        }

        return PersonContainsFieldsPredicateFactory.createPredicate(matchType, parsed.searchTerms);
    }

    /**
     * Holds the parsed components of a find command.
     *
     * <p>Each field directly reflects what the user typed:
     * <ul>
     *   <li>{@code matchType} is {@code null} when {@code m/} was not provided.</li>
     *   <li>{@code availability} is {@code null} when {@code va/} was not provided.</li>
     *   <li>{@code searchTerms} is empty when no search terms were provided.</li>
     * </ul>
     */
    private static final class ParsedFindArgs {
        final FindMatchType matchType;
        final VolunteerAvailability availability;
        final List<String> searchTerms;

        ParsedFindArgs(FindMatchType matchType, VolunteerAvailability availability,
                List<String> searchTerms) {
            this.matchType = matchType;
            this.availability = availability;
            this.searchTerms = searchTerms;
        }
    }
}
