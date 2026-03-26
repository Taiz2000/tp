package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class CsvReaderUtilTest {

    @TempDir
    public Path tempDir;

    @Test
    public void readPersons_validCsv_returnsParsedPersons() throws Exception {
        Path inputFile = writeCsvFile("volunteers.csv",
                "name,phone,email,address,role,notes,tags,availabilities,records",
                "Alice Pauline,94351253,alice@example.com,\"123, Jurong West Ave 6\",Leader,Experienced,"
                        + "friend;driver,\"MONDAY,09:00,12:00;TUESDAY,14:00,17:00\","
                        + "\"2026-03-20T09:00,2026-03-20T12:00\"");

        CsvImportFileResult result = CsvReaderUtil.readPersons(inputFile);

        assertEquals(1, result.getValidRows().size());
        assertEquals(0, result.getInvalidRows().size());
        assertEquals("Alice Pauline", result.getValidRows().get(0).getPerson().getName().fullName);
    }

    @Test
    public void readPersons_caseInsensitiveHeaders_parsesSuccessfully() throws Exception {
        Path inputFile = writeCsvFile("volunteers.csv",
                "Name,Phone,Email,Address,Role,Notes,Tags,Availabilities,Records",
                "Alice Pauline,94351253,alice@example.com,123 Jurong West Ave 6,,,,,");

        CsvImportFileResult result = CsvReaderUtil.readPersons(inputFile);

        assertEquals(1, result.getValidRows().size());
        assertEquals(0, result.getInvalidRows().size());
    }

    @Test
    public void readPersons_missingRequiredHeaders_throwsException() throws Exception {
        Path inputFile = writeCsvFile("volunteers.csv",
                "name,phone,email",
                "Alice Pauline,94351253,alice@example.com");

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> CsvReaderUtil.readPersons(inputFile));

        assertEquals("missing required headers: name, phone, email, address", exception.getMessage());
    }

    @Test
    public void readPersons_invalidRow_recordsRowError() throws Exception {
        Path inputFile = writeCsvFile("volunteers.csv",
                "name,phone,email,address,role,notes,tags,availabilities,records",
                "Alice Pauline,,alice@example.com,123 Jurong West Ave 6,,,,,");

        CsvImportFileResult result = CsvReaderUtil.readPersons(inputFile);

        assertEquals(0, result.getValidRows().size());
        assertEquals(1, result.getInvalidRows().size());
        assertEquals(2, result.getInvalidRows().get(0).getRowNumber());
        assertEquals("missing phone", result.getInvalidRows().get(0).getReason());
    }

    private Path writeCsvFile(String fileName, String... lines) throws Exception {
        Path inputFile = tempDir.resolve(fileName);
        Files.writeString(inputFile, String.join(System.lineSeparator(), lines));
        return inputFile;
    }
}
