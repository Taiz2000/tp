package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.PersonListView;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.csv.CsvImportFileResult;
import seedu.address.logic.csv.CsvImportRowError;
import seedu.address.logic.csv.CsvImportRowSuccess;
import seedu.address.logic.csv.CsvReaderUtil;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Imports volunteers from a CSV file into the active address book.
 */
public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Imports volunteers from a CSV file.\n"
            + "Parameters: FILE_PATH\n"
            + "Example: " + COMMAND_WORD + " data/volunteers.csv";

    public static final String MESSAGE_IMPORT_FAILURE = "Import failed: %1$s";

    private final Path filePath;

    /**
     * Creates an {@code ImportCommand} to import volunteers from the specified CSV file.
     *
     * @param filePath Path to the CSV file to be imported.
     */
    public ImportCommand(Path filePath) {
        requireNonNull(filePath);
        this.filePath = filePath;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        CsvImportFileResult result;
        try {
            result = CsvReaderUtil.readPersons(filePath);
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_IMPORT_FAILURE,
                    "could not read file " + filePath));
        } catch (IllegalArgumentException e) {
            throw new CommandException(String.format(MESSAGE_IMPORT_FAILURE, e.getMessage()));
        }

        int importedCount = 0;
        List<CsvImportRowError> invalidRows = new ArrayList<>(result.getInvalidRows());
        List<CsvImportRowError> duplicateRows = new ArrayList<>();
        List<Person> personsImportedThisRun = new ArrayList<>();

        for (CsvImportRowSuccess successRow : result.getValidRows()) {
            Person person = successRow.getPerson();

            if (hasDuplicatePerson(model, personsImportedThisRun, person)) {
                duplicateRows.add(new CsvImportRowError(
                        successRow.getRowNumber(),
                        "duplicate"));
            } else {
                model.addPerson(person);
                personsImportedThisRun.add(person);
                importedCount++;
            }
        }

        return new CommandResult(
                buildSummaryMessage(importedCount, duplicateRows, invalidRows),
                PersonListView.KEPT_PERSONS);
    }

    private boolean hasDuplicatePerson(Model model, List<Person> personsImportedThisRun, Person person) {
        return model.hasPerson(person)
                || personsImportedThisRun.stream().anyMatch(importedPerson -> importedPerson.isSamePerson(person));
    }

    private String buildSummaryMessage(int importedCount,
                                       List<CsvImportRowError> duplicateRows,
                                       List<CsvImportRowError> invalidRows) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Imported %1$d volunteers from %2$s.", importedCount, filePath));

        if (!duplicateRows.isEmpty() || !invalidRows.isEmpty()) {
            sb.append("\n")
                    .append("Duplicate rows: ").append(duplicateRows.size())
                    .append(", Invalid rows: ").append(invalidRows.size());

            if (!duplicateRows.isEmpty()) {
                sb.append("\n")
                        .append("Duplicate row details: ")
                        .append(formatRowErrors(duplicateRows));
            }

            if (!invalidRows.isEmpty()) {
                sb.append("\n")
                        .append("Invalid row details: ")
                        .append(formatRowErrors(invalidRows));
            }
        }

        return sb.toString();
    }

    private String formatRowErrors(List<CsvImportRowError> rowErrors) {
        List<String> parts = rowErrors.stream()
                .map(error -> error.getRowNumber() + " (" + error.getReason() + ")")
                .toList();
        return String.join(", ", parts);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ImportCommand
                && filePath.equals(((ImportCommand) other).filePath));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("filePath", filePath)
                .toString();
    }
}
