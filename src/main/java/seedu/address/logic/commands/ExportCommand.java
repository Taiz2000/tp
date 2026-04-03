package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.PersonListView;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.csv.CsvWriterUtil;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Exports the active address book to a CSV file.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Exports all active volunteers in the address book to a CSV file.\n"
            + "Parameters: FILE_PATH\n"
            + "Example: " + COMMAND_WORD + " data/volunteers.csv";

    public static final String MESSAGE_SUCCESS = "Exported %1$d volunteers to %2$s";
    public static final String MESSAGE_EXPORT_FAILURE = "Could not export to %1$s: %2$s";

    private final Path filePath;

    /**
     * Creates an ExportCommand to export the active address book to the specified file path.
     *
     * @param filePath Path to the CSV file to be written.
     */
    public ExportCommand(Path filePath) {
        requireNonNull(filePath);
        this.filePath = filePath;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> persons = model.getKeptPersonList();

        try {
            CsvWriterUtil.writePersons(filePath, persons);
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_EXPORT_FAILURE, filePath, e.getMessage()));
        }

        return new CommandResult(
                String.format(MESSAGE_SUCCESS, persons.size(), filePath),
                PersonListView.KEPT_PERSONS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ExportCommand
                && filePath.equals(((ExportCommand) other).filePath));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("filePath", filePath)
                .toString();
    }
}
