package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Notes;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.person.VolunteerAvailability;
import seedu.address.model.person.VolunteerRecord;
import seedu.address.model.tag.Tag;

/**
 * Utility methods for reading persons from CSV.
 */
public class CsvReaderUtil {

    private static final String HEADER_NAME = "name";
    private static final String HEADER_PHONE = "phone";
    private static final String HEADER_EMAIL = "email";
    private static final String HEADER_ADDRESS = "address";
    private static final String HEADER_ROLE = "role";
    private static final String HEADER_NOTES = "notes";
    private static final String HEADER_TAGS = "tags";
    private static final String HEADER_AVAILABILITIES = "availabilities";
    private static final String HEADER_RECORDS = "records";

    private static final List<String> REQUIRED_HEADERS = List.of(
            HEADER_NAME, HEADER_PHONE, HEADER_EMAIL, HEADER_ADDRESS
    );

    private static final List<String> KNOWN_HEADERS = List.of(
            HEADER_NAME, HEADER_PHONE, HEADER_EMAIL, HEADER_ADDRESS,
            HEADER_ROLE, HEADER_NOTES, HEADER_TAGS, HEADER_AVAILABILITIES, HEADER_RECORDS
    );

    private static final String MULTI_VALUE_SEPARATOR = ";";

    private CsvReaderUtil() {}

    /**
     * Reads persons from the specified CSV file.
     *
     * @throws IOException if the file cannot be read
     * @throws IllegalArgumentException if the file is missing required headers or structurally invalid
     */
    public static CsvImportFileResult readPersons(Path filePath) throws IOException {
        requireNonNull(filePath);

        String content = Files.readString(filePath, StandardCharsets.UTF_8);
        List<List<String>> rows = parseCsv(content);

        if (rows.isEmpty()) {
            throw new IllegalArgumentException("missing required headers: name, phone, email, address");
        }

        int headerRowIndex = findFirstNonEmptyRowIndex(rows);
        if (headerRowIndex == -1) {
            throw new IllegalArgumentException("missing required headers: name, phone, email, address");
        }

        List<String> headerRow = rows.get(headerRowIndex);
        Map<String, Integer> headerMap = parseHeaderRow(headerRow);

        List<CsvImportRowSuccess> validRows = new ArrayList<>();
        List<CsvImportRowError> invalidRows = new ArrayList<>();

        for (int i = headerRowIndex + 1; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            int rowNumber = i + 1; // 1-based row number

            if (isBlankRow(row)) {
                continue;
            }

            try {
                Person person = parsePerson(row, headerMap);
                validRows.add(new CsvImportRowSuccess(rowNumber, person));
            } catch (RowParseException e) {
                invalidRows.add(new CsvImportRowError(rowNumber, e.getMessage()));
            }
        }

        return new CsvImportFileResult(validRows, invalidRows);
    }

    private static Map<String, Integer> parseHeaderRow(List<String> headerRow) {
        Map<String, Integer> headerMap = new LinkedHashMap<>();

        for (int i = 0; i < headerRow.size(); i++) {
            String normalized = normalizeHeader(headerRow.get(i));
            if (normalized.isEmpty()) {
                continue;
            }

            if (headerMap.containsKey(normalized)) {
                throw new IllegalArgumentException("duplicate header: " + normalized);
            }

            headerMap.put(normalized, i);
        }

        for (String requiredHeader : REQUIRED_HEADERS) {
            if (!headerMap.containsKey(requiredHeader)) {
                throw new IllegalArgumentException("missing required headers: name, phone, email, address");
            }
        }

        return headerMap;
    }

    private static Person parsePerson(List<String> row, Map<String, Integer> headerMap) throws RowParseException {
        Name name = parseRequiredName(getCell(row, headerMap, HEADER_NAME));
        Phone phone = parseRequiredPhone(getCell(row, headerMap, HEADER_PHONE));
        Email email = parseRequiredEmail(getCell(row, headerMap, HEADER_EMAIL));
        Address address = parseRequiredAddress(getCell(row, headerMap, HEADER_ADDRESS));

        Role role = parseOptionalRole(getCell(row, headerMap, HEADER_ROLE));
        Notes notes = parseOptionalNotes(getCell(row, headerMap, HEADER_NOTES));

        java.util.Set<Tag> tags = parseOptionalTags(getCell(row, headerMap, HEADER_TAGS));
        java.util.Set<VolunteerAvailability> availabilities =
                parseOptionalAvailabilities(getCell(row, headerMap, HEADER_AVAILABILITIES));
        java.util.Set<VolunteerRecord> records =
                parseOptionalRecords(getCell(row, headerMap, HEADER_RECORDS));

        return new Person(name, phone, email, address, role, notes, tags, availabilities, records);
    }

    private static Name parseRequiredName(String value) throws RowParseException {
        if (isBlank(value)) {
            throw new RowParseException("missing name");
        }
        try {
            return ParserUtil.parseName(value);
        } catch (ParseException e) {
            throw new RowParseException("invalid name");
        }
    }

    private static Phone parseRequiredPhone(String value) throws RowParseException {
        if (isBlank(value)) {
            throw new RowParseException("missing phone");
        }
        try {
            return ParserUtil.parsePhone(value);
        } catch (ParseException e) {
            throw new RowParseException("invalid phone");
        }
    }

    private static Email parseRequiredEmail(String value) throws RowParseException {
        if (isBlank(value)) {
            throw new RowParseException("missing email");
        }
        try {
            return ParserUtil.parseEmail(value);
        } catch (ParseException e) {
            throw new RowParseException("invalid email");
        }
    }

    private static Address parseRequiredAddress(String value) throws RowParseException {
        if (isBlank(value)) {
            throw new RowParseException("missing address");
        }
        try {
            return ParserUtil.parseAddress(value);
        } catch (ParseException e) {
            throw new RowParseException("invalid address");
        }
    }

    private static Role parseOptionalRole(String value) throws RowParseException {
        if (isBlank(value)) {
            return Person.EMPTY_ROLE;
        }
        try {
            return ParserUtil.parseRole(value);
        } catch (ParseException e) {
            throw new RowParseException("invalid role");
        }
    }

    private static Notes parseOptionalNotes(String value) throws RowParseException {
        if (isBlank(value)) {
            return Person.EMPTY_NOTES;
        }
        try {
            return ParserUtil.parseNotes(value);
        } catch (ParseException e) {
            throw new RowParseException("invalid notes");
        }
    }

    private static java.util.Set<Tag> parseOptionalTags(String value) throws RowParseException {
        if (isBlank(value)) {
            return Collections.emptySet();
        }

        java.util.Set<Tag> tags = new java.util.HashSet<>();
        for (String part : splitMultiValueCell(value)) {
            try {
                tags.add(ParserUtil.parseTag(part));
            } catch (ParseException e) {
                throw new RowParseException("invalid tag");
            }
        }
        return tags;
    }

    private static java.util.Set<VolunteerAvailability> parseOptionalAvailabilities(String value)
            throws RowParseException {
        if (isBlank(value)) {
            return Collections.emptySet();
        }

        java.util.Set<VolunteerAvailability> availabilities = new java.util.HashSet<>();
        for (String part : splitMultiValueCell(value)) {
            try {
                availabilities.add(ParserUtil.parseVolunteerAvailability(part));
            } catch (ParseException e) {
                throw new RowParseException("invalid availability");
            }
        }
        return availabilities;
    }

    private static java.util.Set<VolunteerRecord> parseOptionalRecords(String value)
            throws RowParseException {
        if (isBlank(value)) {
            return Collections.emptySet();
        }

        java.util.Set<VolunteerRecord> records = new java.util.HashSet<>();
        for (String part : splitMultiValueCell(value)) {
            try {
                records.add(ParserUtil.parseVolunteerRecord(part));
            } catch (ParseException e) {
                throw new RowParseException("invalid record");
            }
        }
        return records;
    }

    private static List<String> splitMultiValueCell(String value) {
        return Arrays.stream(value.split(MULTI_VALUE_SEPARATOR, -1))
                .map(String::trim)
                .filter(part -> !part.isEmpty())
                .toList();
    }

    private static String getCell(List<String> row, Map<String, Integer> headerMap, String headerName) {
        Integer index = headerMap.get(headerName);
        if (index == null || index >= row.size()) {
            return "";
        }
        return row.get(index);
    }

    private static String normalizeHeader(String header) {
        return header == null ? "" : header.trim().toLowerCase();
    }

    private static boolean isBlankRow(List<String> row) {
        return row.stream().allMatch(CsvReaderUtil::isBlank);
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static int findFirstNonEmptyRowIndex(List<List<String>> rows) {
        for (int i = 0; i < rows.size(); i++) {
            if (!isBlankRow(rows.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Parses CSV content into rows and cells, supporting quoted cells.
     */
    static List<List<String>> parseCsv(String content) {
        List<List<String>> rows = new ArrayList<>();
        List<String> currentRow = new ArrayList<>();
        StringBuilder currentCell = new StringBuilder();

        boolean inQuotes = false;

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if (c == '"') {
                if (inQuotes && i + 1 < content.length() && content.charAt(i + 1) == '"') {
                    currentCell.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                currentRow.add(currentCell.toString());
                currentCell.setLength(0);
            } else if ((c == '\n' || c == '\r') && !inQuotes) {
                if (c == '\r' && i + 1 < content.length() && content.charAt(i + 1) == '\n') {
                    i++;
                }
                currentRow.add(currentCell.toString());
                rows.add(new ArrayList<>(currentRow));
                currentRow.clear();
                currentCell.setLength(0);
            } else {
                currentCell.append(c);
            }
        }

        currentRow.add(currentCell.toString());
        rows.add(new ArrayList<>(currentRow));
        return rows;
    }

    private static class RowParseException extends Exception {
        RowParseException(String message) {
            super(message);
        }
    }

    /**
     * Represents the parsed result of an imported CSV file.
     * Contains successfully parsed rows and rows that were rejected as invalid.
     */
    public static class CsvImportFileResult {
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

        public List<CsvImportRowSuccess> getValidRows() {
            return validRows;
        }

        public List<CsvImportRowError> getInvalidRows() {
            return invalidRows;
        }
    }

    /**
     * Represents a CSV row that was successfully parsed into a {@code Person}.
     */
    public static class CsvImportRowSuccess {
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

        public int getRowNumber() {
            return rowNumber;
        }

        public Person getPerson() {
            return person;
        }
    }

    /**
     * Represents a CSV row that could not be imported, together with the reason.
     */
    public static class CsvImportRowError {
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

        public int getRowNumber() {
            return rowNumber;
        }

        public String getReason() {
            return reason;
        }
    }
}
