package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListCommand;
import seedu.address.model.person.sort.SortAttribute;
import seedu.address.model.person.sort.SortOrder;

public class ListCommandParserTest {

    private final ListCommandParser parser = new ListCommandParser();

    @Test
    public void parse_emptyArgs_returnsListCommand() {
        assertParseSuccess(parser, "", new ListCommand());
        assertParseSuccess(parser, "   ", new ListCommand());
    }

    @Test
    public void parse_validArgs_returnsListCommand() {
        assertParseSuccess(parser, "name", new ListCommand(SortAttribute.NAME, SortOrder.ASC));
        assertParseSuccess(parser, "email desc", new ListCommand(SortAttribute.EMAIL, SortOrder.DESC));
        assertParseSuccess(parser, "  NaMe   dEsC  ", new ListCommand(SortAttribute.NAME, SortOrder.DESC));
        assertParseSuccess(parser, "vr desc", new ListCommand(SortAttribute.VR, SortOrder.DESC));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "asc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "name ascending",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "unknown",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "name desc extra",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
    }
}
