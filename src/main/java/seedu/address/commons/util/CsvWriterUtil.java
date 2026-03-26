package seedu.address.commons.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.model.person.Person;
import seedu.address.model.person.VolunteerAvailability;
import seedu.address.model.person.VolunteerRecord;
import seedu.address.model.tag.Tag;

/**
 * Utility methods for writing persons to CSV.
 */
public class CsvWriterUtil {

    private static final String HEADER = "name,phone,email,address,role,notes,tags,availabilities,records";
    private static final String MULTI_VALUE_SEPARATOR = ";";

    private static final Comparator<Tag> TAG_COMPARATOR =
            Comparator.comparing(tag -> tag.tagName);

    private static final Comparator<VolunteerAvailability> AVAILABILITY_COMPARATOR =
            Comparator.comparing((VolunteerAvailability availability) -> availability.dayOfWeek)
                    .thenComparing(availability -> availability.startTime)
                    .thenComparing(availability -> availability.endTime);

    private static final Comparator<VolunteerRecord> RECORD_COMPARATOR =
            Comparator.comparing((VolunteerRecord record) -> record.startDateTime)
                    .thenComparing(record -> record.endDateTime);

    private CsvWriterUtil() {}

    /**
     * Writes the given persons to the specified CSV file.
     * Creates parent directories if needed and overwrites existing files.
     */
    public static void writePersons(Path filePath, Collection<Person> persons) throws IOException {
        Path parent = filePath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        List<String> lines = persons.stream()
                .map(CsvWriterUtil::buildPersonRow)
                .collect(Collectors.toList());
        lines.add(0, HEADER);

        Files.write(filePath, lines, StandardCharsets.UTF_8);
    }

    private static String buildPersonRow(Person person) {
        return String.join(",",
                escapeCsv(person.getName().fullName),
                escapeCsv(person.getPhone().value),
                escapeCsv(person.getEmail().value),
                escapeCsv(person.getAddress().value),
                escapeCsv(person.getRole().value),
                escapeCsv(person.getNotes().value),
                escapeCsv(serializeTags(person.getTags())),
                escapeCsv(serializeAvailabilities(person.getAvailabilities())),
                escapeCsv(serializeRecords(person.getRecords())));
    }

    private static String serializeTags(Collection<Tag> tags) {
        return tags.stream()
                .sorted(TAG_COMPARATOR)
                .map(tag -> tag.tagName)
                .collect(Collectors.joining(MULTI_VALUE_SEPARATOR));
    }

    private static String serializeAvailabilities(Collection<VolunteerAvailability> availabilities) {
        return availabilities.stream()
                .sorted(AVAILABILITY_COMPARATOR)
                .map(CsvWriterUtil::serializeAvailability)
                .collect(Collectors.joining(MULTI_VALUE_SEPARATOR));
    }

    private static String serializeRecords(Collection<VolunteerRecord> records) {
        return records.stream()
                .sorted(RECORD_COMPARATOR)
                .map(CsvWriterUtil::serializeRecord)
                .collect(Collectors.joining(MULTI_VALUE_SEPARATOR));
    }

    private static String serializeAvailability(VolunteerAvailability availability) {
        return availability.dayOfWeek + ","
                + availability.startTime + ","
                + availability.endTime;
    }

    private static String serializeRecord(VolunteerRecord record) {
        return record.startDateTime + ","
                + record.endDateTime;
    }

    /**
     * Escapes a CSV cell according to standard CSV rules.
     */
    private static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }

        boolean mustQuote = value.contains(",")
                || value.contains("\"")
                || value.contains("\n")
                || value.contains("\r");

        String escapedValue = value.replace("\"", "\"\"");

        return mustQuote ? "\"" + escapedValue + "\"" : escapedValue;
    }

    // package-private for testing if needed
    static String escapeCsvForTesting(String value) {
        return escapeCsv(value);
    }

    // package-private for testing if needed
    static String serializeAvailabilityForTesting(VolunteerAvailability availability) {
        return serializeAvailability(availability);
    }

    // package-private for testing if needed
    static String serializeRecordForTesting(VolunteerRecord record) {
        return serializeRecord(record);
    }
}
