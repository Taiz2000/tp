package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MATCH_TYPE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.FindMatchType.FUZZY_TOKEN;
import static seedu.address.logic.parser.FindMatchType.KEYWORD_TOKEN;
import static seedu.address.logic.parser.FindMatchType.SUBSTRING_TOKEN;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
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

        // multiple whitespaces between keywords
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
    public void parse_missingKeywords_throwsParseException() {
        assertParseFailure(parser, PREFIX_MATCH_TYPE + KEYWORD_TOKEN,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        assertParseFailure(parser, PREFIX_MATCH_TYPE + SUBSTRING_TOKEN,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        assertParseFailure(parser, PREFIX_MATCH_TYPE + FUZZY_TOKEN,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

}
