package seedu.address.commons.util;

import java.util.List;

/**
 * Represents the parsed result of an imported CSV file.
 * Contains successfully parsed rows and rows that were rejected as invalid.
 */
public class CsvImportFileResult {
    private final List<CsvImportRowSuccess> validRows;
    private final List<CsvImportRowError> invalidRows;

    /**
     * Creates a {@code CsvImportFileResult} with the given valid and invalid rows.
     *
     * @param validRows Rows that were successfully parsed into persons.
     * @param invalidRows Rows that could not be parsed, together with their error reasons.
     */
    public CsvImportFileResult(List<CsvImportRowSuccess> validRows, List<CsvImportRowError> invalidRows) {
        this.validRows = List.copyOf(validRows);
        this.invalidRows = List.copyOf(invalidRows);
    }

    /**
     * Returns the rows that were successfully parsed into persons.
     *
     * @return Successfully parsed rows.
     */
    public List<CsvImportRowSuccess> getValidRows() {
        return validRows;
    }

    /**
     * Returns the rows that could not be parsed, together with their error reasons.
     *
     * @return Invalid rows.
     */
    public List<CsvImportRowError> getInvalidRows() {
        return invalidRows;
    }
}
