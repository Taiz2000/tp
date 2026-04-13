package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AliasCommand;
import seedu.address.logic.commands.UnaliasCommand;

public class UnaliasCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnaliasCommand.MESSAGE_USAGE);

    private final UnaliasCommandParser parser = new UnaliasCommandParser();

    @Test
    public void parse_validArgs_success() {
        assertParseSuccess(parser, "ls", new UnaliasCommand("ls"));
    }

    @Test
    public void parse_missingParts_failure() {
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_extraArguments_throwsParseException() {
        assertParseFailure(parser, "ls extra", UnaliasCommand.MESSAGE_EXTRA_ARGUMENTS);
    }

    @Test
    public void parse_invalidAliasName_failure() {
        assertParseFailure(parser, "LS", AliasCommand.MESSAGE_INVALID_ALIAS_NAME);
    }
}
