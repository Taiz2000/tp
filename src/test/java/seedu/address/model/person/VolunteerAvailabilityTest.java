package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

public class VolunteerAvailabilityTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        DayOfWeek monday = DayOfWeek.MONDAY;
        LocalTime noon = LocalTime.NOON;
        LocalTime midnight = LocalTime.MIDNIGHT;

        assertThrows(NullPointerException.class, () -> new VolunteerAvailability(null, noon, midnight));
        assertThrows(NullPointerException.class, () -> new VolunteerAvailability(monday, null, midnight));
        assertThrows(NullPointerException.class, () -> new VolunteerAvailability(monday, noon, null));
    }

    @Test
    public void constructor_startNotBeforeEnd_throwsIllegalArgumentException() {
        DayOfWeek monday = DayOfWeek.MONDAY;
        LocalTime twoPm = LocalTime.of(14, 0);
        LocalTime threePm = LocalTime.of(15, 0);

        assertThrows(IllegalArgumentException.class, () -> new VolunteerAvailability(monday, twoPm, twoPm));
        assertThrows(IllegalArgumentException.class, () -> new VolunteerAvailability(monday, threePm, twoPm));
    }

    @Test
    public void equals() {
        VolunteerAvailability availability1 =
                new VolunteerAvailability(DayOfWeek.MONDAY, LocalTime.of(14, 0), LocalTime.of(16, 0));
        VolunteerAvailability availability1Copy =
                new VolunteerAvailability(DayOfWeek.MONDAY, LocalTime.of(14, 0), LocalTime.of(16, 0));
        VolunteerAvailability availability2 =
                new VolunteerAvailability(DayOfWeek.TUESDAY, LocalTime.of(14, 0), LocalTime.of(16, 0));
        VolunteerAvailability availability3 =
                new VolunteerAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(16, 0));
        VolunteerAvailability availability4 =
                new VolunteerAvailability(DayOfWeek.MONDAY, LocalTime.of(14, 0), LocalTime.of(18, 0));

        // same object -> returns true
        assertTrue(availability1.equals(availability1));

        // same values -> returns true
        assertTrue(availability1.equals(availability1Copy));

        // null -> returns false
        assertFalse(availability1.equals(null));

        // different types -> returns false
        assertFalse(availability1.equals(5));

        // different day -> returns false
        assertFalse(availability1.equals(availability2));

        // different start time -> returns false
        assertFalse(availability1.equals(availability3));

        // different end time -> returns false
        assertFalse(availability1.equals(availability4));
    }

    @Test
    public void hashCodeMethod() {
        VolunteerAvailability availability1 =
                new VolunteerAvailability(DayOfWeek.MONDAY, LocalTime.of(14, 0), LocalTime.of(16, 0));
        VolunteerAvailability availability1Copy =
                new VolunteerAvailability(DayOfWeek.MONDAY, LocalTime.of(14, 0), LocalTime.of(16, 0));

        assertEquals(availability1.hashCode(), availability1Copy.hashCode());
    }

    @Test
    public void toStringMethod() {
        VolunteerAvailability availability1 =
                new VolunteerAvailability(DayOfWeek.MONDAY, LocalTime.of(14, 0), LocalTime.of(16, 0));
        assertEquals("MONDAY 14:00 to 16:00", availability1.toString());
    }

    @Test
    public void isValidAvailability_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> VolunteerAvailability.isValidAvailability(null));
    }

    @Test
    public void parsingHelpers() {
        assertTrue(VolunteerAvailability.isValidAvailability("MONDAY,14:00,16:00"));
        assertFalse(VolunteerAvailability.isValidAvailability("MONDAY,16:00,14:00"));
        assertFalse(VolunteerAvailability.isValidAvailability("MONDAY 14:00 16:00"));

        VolunteerAvailability parsed = VolunteerAvailability.fromString("MONDAY,14:00,16:00");
        assertEquals(new VolunteerAvailability(DayOfWeek.MONDAY, LocalTime.of(14, 0), LocalTime.of(16, 0)),
                parsed);
    }

    @Test
    public void isValidAvailability_caseInsensitiveDayName() {
        // lowercase day name
        assertTrue(VolunteerAvailability.isValidAvailability("monday,14:00,16:00"));

        // mixed case day name
        assertTrue(VolunteerAvailability.isValidAvailability("Monday,14:00,16:00"));
        assertTrue(VolunteerAvailability.isValidAvailability("tUeSdAy,09:00,12:00"));

        // all valid days in lowercase
        assertTrue(VolunteerAvailability.isValidAvailability("tuesday,14:00,16:00"));
        assertTrue(VolunteerAvailability.isValidAvailability("wednesday,14:00,16:00"));
        assertTrue(VolunteerAvailability.isValidAvailability("thursday,14:00,16:00"));
        assertTrue(VolunteerAvailability.isValidAvailability("friday,14:00,16:00"));
        assertTrue(VolunteerAvailability.isValidAvailability("saturday,14:00,16:00"));
        assertTrue(VolunteerAvailability.isValidAvailability("sunday,14:00,16:00"));

        // invalid day name regardless of case
        assertFalse(VolunteerAvailability.isValidAvailability("notaday,14:00,16:00"));
        assertFalse(VolunteerAvailability.isValidAvailability("MON,14:00,16:00"));
    }

    @Test
    public void fromString_caseInsensitiveDayName_parsesCorrectly() {
        VolunteerAvailability expected =
                new VolunteerAvailability(DayOfWeek.MONDAY, LocalTime.of(14, 0), LocalTime.of(16, 0));

        // lowercase
        assertEquals(expected, VolunteerAvailability.fromString("monday,14:00,16:00"));

        // mixed case
        assertEquals(expected, VolunteerAvailability.fromString("Monday,14:00,16:00"));
        assertEquals(expected, VolunteerAvailability.fromString("moNDay,14:00,16:00"));
    }
}
