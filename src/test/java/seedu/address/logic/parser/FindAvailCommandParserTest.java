package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindAvailCommand;
import seedu.address.model.person.VolunteerAvailability;
import seedu.address.model.person.predicates.PersonAvailableDuringPredicate;

public class FindAvailCommandParserTest {

    private FindAvailCommandParser parser = new FindAvailCommandParser();

    @Test
    public void parse_validArgs_returnsFindAvailCommand() {
        VolunteerAvailability query = VolunteerAvailability.fromString("MONDAY,14:00,17:00");
        PersonAvailableDuringPredicate predicate = new PersonAvailableDuringPredicate(query);
        FindAvailCommand expectedCommand = new FindAvailCommand(predicate);
        assertParseSuccess(parser, " MONDAY,14:00,17:00", expectedCommand);
    }

    @Test
    public void parse_validArgsLowerCase_returnsFindAvailCommand() {
        VolunteerAvailability query = VolunteerAvailability.fromString("monday,14:00,17:00");
        PersonAvailableDuringPredicate predicate = new PersonAvailableDuringPredicate(query);
        FindAvailCommand expectedCommand = new FindAvailCommand(predicate);
        assertParseSuccess(parser, " monday,14:00,17:00", expectedCommand);
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindAvailCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_whitespaceOnlyArgs_throwsParseException() {
        assertParseFailure(parser, "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindAvailCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidFormat_throwsParseException() {
        // Missing end time
        assertParseFailure(parser, " MONDAY,14:00",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindAvailCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDay_throwsParseException() {
        assertParseFailure(parser, " NOTADAY,14:00,17:00",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindAvailCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_startAfterEnd_throwsParseException() {
        assertParseFailure(parser, " MONDAY,17:00,14:00",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindAvailCommand.MESSAGE_USAGE));
    }
}
