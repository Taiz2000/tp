package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AliasCommand;

/**
 * Reused from Codex suggestions upon providing specifications
 */
public class AliasCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AliasCommand.MESSAGE_USAGE);

    private final AliasCommandParser parser = new AliasCommandParser();

    @Test
    public void parse_validArgs_success() {
        assertParseSuccess(parser, "ls list", new AliasCommand("ls", "list"));
        assertParseSuccess(parser, "rb bin", new AliasCommand("rb", "bin"));
        assertParseSuccess(parser, "st stats", new AliasCommand("st", "stats"));
    }

    @Test
    public void parse_missingParts_failure() {
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "ls", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidAliasName_failure() {
        assertParseFailure(parser, "LS list", AliasCommand.MESSAGE_INVALID_ALIAS_NAME);
    }

    @Test
    public void parse_reservedAliasName_failure() {
        assertParseFailure(parser, "list help", AliasCommand.MESSAGE_RESERVED_ALIAS_NAME);
        assertParseFailure(parser, "bin help", AliasCommand.MESSAGE_RESERVED_ALIAS_NAME);
        assertParseFailure(parser, "stats help", AliasCommand.MESSAGE_RESERVED_ALIAS_NAME);
        assertParseFailure(parser, "editprev help", AliasCommand.MESSAGE_RESERVED_ALIAS_NAME);
    }

    @Test
    public void parse_invalidAliasTemplate_failure() {
        assertParseFailure(parser, "ls rm", AliasCommand.MESSAGE_INVALID_ALIAS_TEMPLATE);
        assertParseFailure(parser, "ls ls", AliasCommand.MESSAGE_INVALID_ALIAS_TEMPLATE);
        assertParseFailure(parser, "ss find m/ss meie", AliasCommand.MESSAGE_INVALID_ALIAS_TEMPLATE);
    }

    @Test
    public void parse_extraArguments_throwsParseException() {
        assertParseFailure(parser, "wipe clear now", AliasCommand.MESSAGE_INVALID_ALIAS_TEMPLATE);
    }

    @Test
    public void parse_reservedAliasTarget_failure() {
        assertParseFailure(parser, "aa alias", AliasCommand.MESSAGE_RESERVED_ALIAS_TARGET);
        assertParseFailure(parser, "aa aliases", AliasCommand.MESSAGE_RESERVED_ALIAS_TARGET);
        assertParseFailure(parser, "aa unalias", AliasCommand.MESSAGE_RESERVED_ALIAS_TARGET);
        assertParseFailure(parser, "aa editprev", AliasCommand.MESSAGE_RESERVED_ALIAS_TARGET);
    }
}
