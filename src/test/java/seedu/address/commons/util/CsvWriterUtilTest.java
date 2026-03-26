package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.person.VolunteerAvailability;
import seedu.address.model.person.VolunteerRecord;
import seedu.address.testutil.PersonBuilder;

public class CsvWriterUtilTest {

    @TempDir
    public Path tempDir;

    @Test
    public void writePersons_writesHeaderAndPersonRows() throws Exception {
        Path outputFile = tempDir.resolve("volunteers.csv");

        var person = new PersonBuilder().withName("Alice Pauline")
                .withPhone("94351253")
                .withEmail("alice@example.com")
                .withAddress("123, Jurong West Ave 6, #08-111")
                .withRole("Leader")
                .withNotes("Experienced volunteer")
                .build();

        CsvWriterUtil.writePersons(outputFile, List.of(person));

        List<String> lines = Files.readAllLines(outputFile);
        assertEquals("name,phone,email,address,role,notes,tags,availabilities,records", lines.get(0));
        assertTrue(lines.get(1).contains("Alice Pauline"));
        assertTrue(lines.get(1).contains("\"123, Jurong West Ave 6, #08-111\""));
    }

    @Test
    public void escapeCsvForTesting_valueContainsQuote_quotesEscaped() {
        assertEquals("\"hello \"\"world\"\"\"",
                CsvWriterUtil.escapeCsvForTesting("hello \"world\""));
    }

    @Test
    public void serializeAvailabilityForTesting_returnsCanonicalFormat() {
        VolunteerAvailability availability = VolunteerAvailability.fromString("MONDAY,09:00,12:00");
        assertEquals("MONDAY,09:00,12:00",
                CsvWriterUtil.serializeAvailabilityForTesting(availability));
    }

    @Test
    public void serializeRecordForTesting_returnsCanonicalFormat() {
        VolunteerRecord record = VolunteerRecord.fromString("2026-03-20T09:00,2026-03-20T12:00");
        assertEquals("2026-03-20T09:00,2026-03-20T12:00",
                CsvWriterUtil.serializeRecordForTesting(record));
    }
}
