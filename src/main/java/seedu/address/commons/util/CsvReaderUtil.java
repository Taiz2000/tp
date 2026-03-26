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

    private static final String MULTI_VALUE_SEPARATOR = ";";

    private CsvReaderUtil() {}

    /**
     * Reads persons from the specified CSV file.
     *
     * @throws IOException if the file cannot be read
     * @throws IllegalArgumentException if required headers are missing or duplicated
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
            int rowNumber = i + 1;

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
            String normalizedHeader = normalizeHeader(headerRow.get(i));
            if (normalizedHeader.isEmpty()) {
                continue;
            }

            if (headerMap.containsKey(normalizedHeader)) {
                throw new IllegalArgumentException("duplicate header: " + normalizedHeader);
            }

            headerMap.put(normalizedHeader, i);
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
        CsvParsingState state = new CsvParsingState();

        for (int i = 0; i < content.length(); i++) {
            char currentChar = content.charAt(i);

            if (currentChar == '"') {
                i = handleQuoteCharacter(content, i, state);
            } else if (currentChar == ',' && !state.inQuotes) {
                handleDelimiter(state);
            } else if ((currentChar == '\n' || currentChar == '\r') && !state.inQuotes) {
                i = handleLineBreak(content, i, rows, state);
            } else {
                state.currentCell.append(currentChar);
            }
        }

        finishLastRow(rows, state);
        return rows;
    }

    private static int handleQuoteCharacter(String content, int index, CsvParsingState state) {
        if (state.inQuotes && index + 1 < content.length() && content.charAt(index + 1) == '"') {
            state.currentCell.append('"');
            return index + 1;
        }

        state.inQuotes = !state.inQuotes;
        return index;
    }

    private static void handleDelimiter(CsvParsingState state) {
        state.currentRow.add(state.currentCell.toString());
        state.currentCell.setLength(0);
    }

    private static int handleLineBreak(String content, int index, List<List<String>> rows, CsvParsingState state) {
        if (content.charAt(index) == '\r' && index + 1 < content.length() && content.charAt(index + 1) == '\n') {
            index++;
        }

        state.currentRow.add(state.currentCell.toString());
        rows.add(new ArrayList<>(state.currentRow));
        state.currentRow.clear();
        state.currentCell.setLength(0);

        return index;
    }

    private static void finishLastRow(List<List<String>> rows, CsvParsingState state) {
        state.currentRow.add(state.currentCell.toString());
        rows.add(new ArrayList<>(state.currentRow));
    }

    private static class CsvParsingState {
        private final List<String> currentRow = new ArrayList<>();
        private final StringBuilder currentCell = new StringBuilder();
        private boolean inQuotes;
    }

    private static class RowParseException extends Exception {
        RowParseException(String message) {
            super(message);
        }
    }
}
