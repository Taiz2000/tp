package seedu.address.commons.util;

/**
 * Represents a CSV row that could not be imported, together with the reason.
 */
public class CsvImportRowError {
    private final int rowNumber;
    private final String reason;

    /**
     * Creates a {@code CsvImportRowError} for the given row number and error reason.
     *
     * @param rowNumber The 1-based row number in the CSV file.
     * @param reason The reason the row could not be imported.
     */
    public CsvImportRowError(int rowNumber, String reason) {
        this.rowNumber = rowNumber;
        this.reason = reason;
    }

    /**
     * Returns the 1-based row number in the CSV file.
     *
     * @return Row number.
     */
    public int getRowNumber() {
        return rowNumber;
    }

    /**
     * Returns the reason the row could not be imported.
     *
     * @return Error reason.
     */
    public String getReason() {
        return reason;
    }
}
