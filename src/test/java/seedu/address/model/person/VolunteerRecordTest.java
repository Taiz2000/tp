package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class VolunteerRecordTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new VolunteerRecord(null, LocalDateTime.now()));
        assertThrows(NullPointerException.class, () -> new VolunteerRecord(LocalDateTime.now(), null));
    }

    @Test
    public void constructor_startNotBeforeEnd_throwsIllegalArgumentException() {
        LocalDateTime timestamp = LocalDateTime.of(2026, 3, 20, 14, 0);
        LocalDateTime timestampPlusOne = timestamp.plusHours(1);
        assertThrows(IllegalArgumentException.class, () -> new VolunteerRecord(timestamp, timestamp));
        assertThrows(IllegalArgumentException.class, () -> new VolunteerRecord(timestampPlusOne, timestamp));
    }

    @Test
    public void equals() {
        LocalDateTime start1 = LocalDateTime.of(2026, 3, 20, 14, 0);
        LocalDateTime end1 = LocalDateTime.of(2026, 3, 20, 16, 0);

        LocalDateTime start2 = LocalDateTime.of(2026, 3, 21, 14, 0);
        LocalDateTime end2 = LocalDateTime.of(2026, 3, 20, 18, 0);
        LocalDateTime end3 = LocalDateTime.of(2026, 3, 21, 16, 0);

        VolunteerRecord record1 = new VolunteerRecord(start1, end1);
        VolunteerRecord record1Copy = new VolunteerRecord(start1, end1);
        VolunteerRecord record2 = new VolunteerRecord(start2, end3);
        VolunteerRecord record3 = new VolunteerRecord(start1, end2);

        // same object -> returns true
        assertTrue(record1.equals(record1));

        // same values -> returns true
        assertTrue(record1.equals(record1Copy));

        // null -> returns false
        assertFalse(record1.equals(null));

        // different types -> returns false
        assertFalse(record1.equals(5));

        // different start time -> returns false
        assertFalse(record1.equals(record2));

        // different end time -> returns false
        assertFalse(record1.equals(record3));
    }

    @Test
    public void hashCodeMethod() {
        LocalDateTime start = LocalDateTime.of(2026, 3, 20, 14, 0);
        LocalDateTime end = LocalDateTime.of(2026, 3, 20, 16, 0);

        VolunteerRecord record1 = new VolunteerRecord(start, end);
        VolunteerRecord record1Copy = new VolunteerRecord(start, end);

        assertEquals(record1.hashCode(), record1Copy.hashCode());
    }

    @Test
    public void toStringMethod() {
        LocalDateTime start = LocalDateTime.of(2026, 3, 20, 14, 5);
        LocalDateTime end = LocalDateTime.of(2026, 3, 20, 16, 30);
        VolunteerRecord record = new VolunteerRecord(start, end);

        assertEquals("2026-03-20 14:05 to 2026-03-20 16:30", record.toString());
    }

    @Test
    public void isValidRecord_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> VolunteerRecord.isValidRecord(null));
    }

    @Test
    public void parsingHelpers() {
        assertTrue(VolunteerRecord.isValidRecord("2026-03-20T14:00,2026-03-20T16:00"));
        assertFalse(VolunteerRecord.isValidRecord("2026-03-20 14:00,2026-03-20 16:00"));
        assertFalse(VolunteerRecord.isValidRecord("2026-03-20T16:00,2026-03-20T14:00"));

        VolunteerRecord parsed = VolunteerRecord.fromString("2026-03-20T14:00,2026-03-20T16:00");
        assertEquals(new VolunteerRecord(LocalDateTime.of(2026, 3, 20, 14, 0),
                LocalDateTime.of(2026, 3, 20, 16, 0)), parsed);
    }
}
