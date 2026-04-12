package seedu.address.logic.parser;

import java.nio.file.Path;

import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ImportCommand object.
 */
public class ImportCommandParser implements Parser<ImportCommand> {

    @Override
    public ImportCommand parse(String args) throws ParseException {
        Path filePath = ParserUtil.parseFilePath(args, ImportCommand.MESSAGE_USAGE);
        return new ImportCommand(filePath);
    }
}
