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
 * Exports kept volunteers to a CSV file based on the current view.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Exports kept volunteers to a CSV file.\n"
            + "When viewing the contact list, exports the currently displayed kept contacts, "
            + "so active find filters are applied.\n"
            + "When viewing the recycle bin, exports the full kept contact list instead; "
            + "deleted contacts are never exported and the view switches back to the contact list.\n"
            + "Parameters: FILE_PATH\n"
            + "Example: " + COMMAND_WORD + " data/volunteers.csv";

    public static final String MESSAGE_SUCCESS = "Exported %1$d volunteers to %2$s";
    public static final String MESSAGE_EXPORT_FAILURE = "Could not export to %1$s: %2$s";

    private final Path filePath;

    /**
     * Creates an ExportCommand to export kept volunteers to the specified file path.
     *
     * @param filePath Path to the CSV file to be written.
     */
    public ExportCommand(Path filePath) {
        requireNonNull(filePath);
        this.filePath = filePath;
    }

    @Override
    public CommandResult execute(Model model, PersonListView personListView) throws CommandException {
        requireNonNull(model);

        boolean isViewingDeletedPersons = personListView == PersonListView.DELETED_PERSONS;
        List<Person> persons = isViewingDeletedPersons
                ? model.getKeptPersonList()
                : model.getFilteredKeptPersonList();
        PersonListView resultView = isViewingDeletedPersons ? PersonListView.KEPT_PERSONS : personListView;

        try {
            CsvWriterUtil.writePersons(filePath, persons);
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_EXPORT_FAILURE, filePath, e.getMessage()));
        }

        return new CommandResult(
                String.format(MESSAGE_SUCCESS, persons.size(), filePath),
                resultView);
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
