package seedu.address.logic.parser;

import java.nio.file.Path;

import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ExportCommand object.
 */
public class ExportCommandParser implements Parser<ExportCommand> {

    @Override
    public ExportCommand parse(String args) throws ParseException {
        Path filePath = ParserUtil.parseFilePath(args, ExportCommand.MESSAGE_USAGE);
        return new ExportCommand(filePath);
    }
}
