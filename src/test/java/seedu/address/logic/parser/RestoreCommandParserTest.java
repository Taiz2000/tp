package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.RestoreCommand;

/**
 * Tests for {@link RestoreCommandParser}.
 */
public class RestoreCommandParserTest {

    private final RestoreCommandParser parser = new RestoreCommandParser();

    @Test
    public void parse_validArgs_returnsRestoreCommand() {
        assertParseSuccess(parser, "1",
                new RestoreCommand(List.of(INDEX_FIRST_PERSON)));
        assertParseSuccess(parser, "   1   2     ",
                new RestoreCommand(List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON)));
        assertParseSuccess(parser, "  1   1  2     ",
                new RestoreCommand(List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON)));
    }

    @Test
    public void parse_unsortedArgs_returnsRestoreCommandWithSortedIndices() {
        assertParseSuccess(parser, "2 1",
                new RestoreCommand(List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON)));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RestoreCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "  1     a  ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RestoreCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RestoreCommand.MESSAGE_USAGE));
    }
}

