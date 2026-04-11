package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_PREFIX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AVAILABILITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MATCH_TYPE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.FindMatchType.FUZZY_TOKEN;
import static seedu.address.logic.parser.FindMatchType.KEYWORD_TOKEN;
import static seedu.address.logic.parser.FindMatchType.SUBSTRING_TOKEN;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.VolunteerAvailability;
import seedu.address.model.person.predicates.CombinedAndPersonPredicate;
import seedu.address.model.person.predicates.PersonAvailableDuringPredicate;
import seedu.address.model.person.predicates.PersonContainsFuzzyKeywordsPredicate;
import seedu.address.model.person.predicates.PersonContainsKeywordsPredicate;
import seedu.address.model.person.predicates.PersonContainsSubstringsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new PersonContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);
        assertParseSuccess(parser, PREFIX_MATCH_TYPE + KEYWORD_TOKEN + " Alice Bob", expectedFindCommand);

        FindCommand expectedSubstringFindCommand =
                new FindCommand(new PersonContainsSubstringsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, PREFIX_MATCH_TYPE + SUBSTRING_TOKEN + " Alice Bob", expectedSubstringFindCommand);

        FindCommand expectedFuzzyFindCommand =
                new FindCommand(new PersonContainsFuzzyKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, PREFIX_MATCH_TYPE + FUZZY_TOKEN + " Alice Bob", expectedFuzzyFindCommand);

        // multiple whitespaces between search terms
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);
        assertParseSuccess(parser, " \n " + PREFIX_MATCH_TYPE + KEYWORD_TOKEN + " \n \t Alice  \t Bob  \t",
                expectedFindCommand);
        assertParseSuccess(parser, " \n " + PREFIX_MATCH_TYPE + SUBSTRING_TOKEN + " \n \t Alice  \t Bob  \t",
                expectedSubstringFindCommand);
        assertParseSuccess(parser, " \n " + PREFIX_MATCH_TYPE + FUZZY_TOKEN + " \n \t Alice  \t Bob  \t",
                expectedFuzzyFindCommand);
    }

    @Test
    public void parse_examplesFromDocumentation_returnsFindCommand() {
        // FindCommand MESSAGE_USAGE examples
        assertParseSuccess(parser, PREFIX_MATCH_TYPE + KEYWORD_TOKEN + " alice bob charlie",
                new FindCommand(new PersonContainsKeywordsPredicate(Arrays.asList("alice", "bob", "charlie"))));
        assertParseSuccess(parser, PREFIX_MATCH_TYPE + SUBSTRING_TOKEN + " ali",
                new FindCommand(new PersonContainsSubstringsPredicate(Arrays.asList("ali"))));
        assertParseSuccess(parser, PREFIX_MATCH_TYPE + FUZZY_TOKEN + " meyr",
                new FindCommand(new PersonContainsFuzzyKeywordsPredicate(Arrays.asList("meyr"))));

        // User Guide examples
        assertParseSuccess(parser, "John",
                new FindCommand(new PersonContainsKeywordsPredicate(Arrays.asList("John"))));
        assertParseSuccess(parser, "alex david",
                new FindCommand(new PersonContainsKeywordsPredicate(Arrays.asList("alex", "david"))));
        assertParseSuccess(parser, PREFIX_MATCH_TYPE + KEYWORD_TOKEN + " John",
                new FindCommand(new PersonContainsKeywordsPredicate(Arrays.asList("John"))));
        assertParseSuccess(parser, PREFIX_MATCH_TYPE + SUBSTRING_TOKEN + " ali",
                new FindCommand(new PersonContainsSubstringsPredicate(Arrays.asList("ali"))));
        assertParseSuccess(parser, PREFIX_MATCH_TYPE + FUZZY_TOKEN + " michigan",
                new FindCommand(new PersonContainsFuzzyKeywordsPredicate(Arrays.asList("michigan"))));
    }

    @Test
    public void parse_availabilityOnly_returnsFindCommand() {
        VolunteerAvailability query = VolunteerAvailability.fromString("MONDAY,14:00,17:00");
        PersonAvailableDuringPredicate predicate = new PersonAvailableDuringPredicate(query);
        FindCommand expectedCommand = new FindCommand(predicate);
        assertParseSuccess(parser, " " + PREFIX_AVAILABILITY + "MONDAY,14:00,17:00", expectedCommand);
    }

    @Test
    public void parse_searchTermsAndAvailability_returnsFindCommand() {
        VolunteerAvailability query = VolunteerAvailability.fromString("MONDAY,14:00,17:00");
        PersonAvailableDuringPredicate availPredicate = new PersonAvailableDuringPredicate(query);
        PersonContainsKeywordsPredicate textPredicate =
                new PersonContainsKeywordsPredicate(Arrays.asList("Alice"));
        FindCommand expectedCommand = new FindCommand(
                new CombinedAndPersonPredicate(List.of(textPredicate, availPredicate)));
        assertParseSuccess(parser, PREFIX_AVAILABILITY + "MONDAY,14:00,17:00 Alice", expectedCommand);
    }

    @Test
    public void parse_matchTypeSearchTermsAndAvailability_returnsFindCommand() {
        VolunteerAvailability query = VolunteerAvailability.fromString("TUESDAY,09:00,12:00");
        PersonAvailableDuringPredicate availPredicate = new PersonAvailableDuringPredicate(query);
        PersonContainsKeywordsPredicate textPredicate =
                new PersonContainsKeywordsPredicate(Arrays.asList("Bob", "Charlie"));
        FindCommand expectedCommand = new FindCommand(
                new CombinedAndPersonPredicate(List.of(textPredicate, availPredicate)));
        assertParseSuccess(parser, PREFIX_MATCH_TYPE + KEYWORD_TOKEN + " "
                + PREFIX_AVAILABILITY + "TUESDAY,09:00,12:00 Bob Charlie", expectedCommand);
    }

    @Test
    public void parse_availabilityBeforeMatchType_returnsFindCommand() {
        // va/ before m/ — search terms trail after m/ (the last prefix)
        VolunteerAvailability query = VolunteerAvailability.fromString("TUESDAY,09:00,12:00");
        PersonAvailableDuringPredicate availPredicate = new PersonAvailableDuringPredicate(query);
        PersonContainsKeywordsPredicate textPredicate =
                new PersonContainsKeywordsPredicate(Arrays.asList("Bob", "Charlie"));
        FindCommand expectedCommand = new FindCommand(
                new CombinedAndPersonPredicate(List.of(textPredicate, availPredicate)));
        assertParseSuccess(parser, PREFIX_AVAILABILITY + "TUESDAY,09:00,12:00 "
                + PREFIX_MATCH_TYPE + KEYWORD_TOKEN + " Bob Charlie", expectedCommand);
    }

    @Test
    public void parse_matchTypeSubstringAndAvailability_returnsFindCommand() {
        VolunteerAvailability query = VolunteerAvailability.fromString("WEDNESDAY,10:00,15:00");
        PersonAvailableDuringPredicate availPredicate = new PersonAvailableDuringPredicate(query);
        PersonContainsSubstringsPredicate textPredicate =
                new PersonContainsSubstringsPredicate(Arrays.asList("ali"));
        FindCommand expectedCommand = new FindCommand(
                new CombinedAndPersonPredicate(List.of(textPredicate, availPredicate)));
        assertParseSuccess(parser, PREFIX_MATCH_TYPE + SUBSTRING_TOKEN + " "
                + PREFIX_AVAILABILITY + "WEDNESDAY,10:00,15:00 ali", expectedCommand);
    }

    @Test
    public void parse_matchTypeFuzzyAndAvailability_returnsFindCommand() {
        VolunteerAvailability query = VolunteerAvailability.fromString("FRIDAY,08:00,12:00");
        PersonAvailableDuringPredicate availPredicate = new PersonAvailableDuringPredicate(query);
        PersonContainsFuzzyKeywordsPredicate textPredicate =
                new PersonContainsFuzzyKeywordsPredicate(Arrays.asList("meyr"));
        FindCommand expectedCommand = new FindCommand(
                new CombinedAndPersonPredicate(List.of(textPredicate, availPredicate)));
        assertParseSuccess(parser, PREFIX_MATCH_TYPE + FUZZY_TOKEN + " "
                + PREFIX_AVAILABILITY + "FRIDAY,08:00,12:00 meyr", expectedCommand);
    }

    @Test
    public void parse_matchTypeWithAvailabilityButNoSearchTerms_throwsParseException() {
        // m/kw with availability but no search terms should fail
        assertParseFailure(parser, PREFIX_MATCH_TYPE + KEYWORD_TOKEN + " "
                        + PREFIX_AVAILABILITY + "MONDAY,14:00,17:00",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidAvailability_throwsParseException() {
        // Invalid day
        assertParseFailure(parser, " " + PREFIX_AVAILABILITY + "NOTADAY,14:00,17:00",
                VolunteerAvailability.MESSAGE_CONSTRAINTS);

        // Start after end
        assertParseFailure(parser, " " + PREFIX_AVAILABILITY + "MONDAY,17:00,14:00",
                VolunteerAvailability.MESSAGE_CONSTRAINTS);

        // Empty availability value
        assertParseFailure(parser, " " + PREFIX_AVAILABILITY,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));

        // Invalid time format
        assertParseFailure(parser, " " + PREFIX_AVAILABILITY + "MONDAY,25:00,17:00",
                VolunteerAvailability.MESSAGE_CONSTRAINTS);

        // Missing end time
        assertParseFailure(parser, " " + PREFIX_AVAILABILITY + "MONDAY,14:00",
                VolunteerAvailability.MESSAGE_CONSTRAINTS);

        // Non-time garbage
        assertParseFailure(parser, " " + PREFIX_AVAILABILITY + "MONDAY,abc,def",
                VolunteerAvailability.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_matchTypeNotFirstToken_throwsParseException() {
        assertParseFailure(parser, "Alice " + PREFIX_MATCH_TYPE + KEYWORD_TOKEN + " Bob",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_unsupportedMatchType_throwsParseException() {
        assertParseFailure(parser, PREFIX_MATCH_TYPE + "regex Alice Bob",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingSearchTerms_throwsParseException() {
        assertParseFailure(parser, PREFIX_MATCH_TYPE + KEYWORD_TOKEN,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        assertParseFailure(parser, PREFIX_MATCH_TYPE + SUBSTRING_TOKEN,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        assertParseFailure(parser, PREFIX_MATCH_TYPE + FUZZY_TOKEN,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyMatchType_throwsParseException() {
        // m/ present but no match type token
        assertParseFailure(parser, " " + PREFIX_MATCH_TYPE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));

        // m/ empty with availability — still an error
        assertParseFailure(parser, " " + PREFIX_MATCH_TYPE + " "
                        + PREFIX_AVAILABILITY + "MONDAY,14:00,17:00",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_unknownPrefix_failure() {
        assertParseFailure(parser, "n/John",
                String.format(MESSAGE_UNKNOWN_PREFIX, "n/", "m/, va/"));
    }

    @Test
    public void parse_abbreviationInValue_success() {
        // "s/o" abbreviation should not be flagged as unknown prefix
        FindCommand expectedCommand =
                new FindCommand(new PersonContainsKeywordsPredicate(Arrays.asList("s/o")));
        assertParseSuccess(parser, "s/o", expectedCommand);
    }

}
