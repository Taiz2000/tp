package seedu.address.commons.util;

import seedu.address.model.person.Person;

/**
 * Represents a CSV row that was successfully parsed into a {@code Person}.
 */
public class CsvImportRowSuccess {
    private final int rowNumber;
    private final Person person;

    /**
     * Creates a {@code CsvImportRowSuccess} for the given row number and parsed person.
     *
     * @param rowNumber The 1-based row number in the CSV file.
     * @param person The person parsed from that row.
     */
    public CsvImportRowSuccess(int rowNumber, Person person) {
        this.rowNumber = rowNumber;
        this.person = person;
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
     * Returns the person parsed from the row.
     *
     * @return Parsed person.
     */
    public Person getPerson() {
        return person;
    }
}
