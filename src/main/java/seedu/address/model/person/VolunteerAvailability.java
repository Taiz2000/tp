package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Represents a Volunteer's availability in the address book.
 * Guarantees: immutable; details are present and not null.
 */
public class VolunteerAvailability {

    public static final String MESSAGE_CONSTRAINTS =
        "Availability must be in format DAY,HH:mm,HH:mm (e.g. MONDAY,14:00,17:00), "
                + "and start time must be before end time.";

    public final DayOfWeek dayOfWeek;
    public final LocalTime startTime;
    public final LocalTime endTime;

    /**
     * Constructs a {@code VolunteerAvailability}.
     *
     * @param dayOfWeek A valid day of the week.
     * @param startTime A valid start time.
     * @param endTime A valid end time.
     */
    public VolunteerAvailability(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        requireNonNull(dayOfWeek);
        requireNonNull(startTime);
        requireNonNull(endTime);
        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Returns true if {@code value} can be parsed as {@code DAY,HH:mm,HH:mm}
     * with start time before end time.
     */
    public static boolean isValidAvailability(String value) {
        requireNonNull(value);

        String[] parts = value.trim().split(",");
        if (parts.length != 3) {
            return false;
        }

        try {
            DayOfWeek.valueOf(parts[0].trim().toUpperCase());
            LocalTime start = LocalTime.parse(parts[1].trim());
            LocalTime end = LocalTime.parse(parts[2].trim());
            return start.isBefore(end);
        } catch (DateTimeParseException ex) {
            return false;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Parses {@code value} of format {@code DAY,HH:mm,HH:mm}.
     */
    public static VolunteerAvailability fromString(String value) {
        if (!isValidAvailability(value)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }

        String[] parts = value.trim().split(",");
        DayOfWeek day = DayOfWeek.valueOf(parts[0].trim().toUpperCase());
        LocalTime start = LocalTime.parse(parts[1].trim());
        LocalTime end = LocalTime.parse(parts[2].trim());
        return new VolunteerAvailability(day, start, end);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof VolunteerAvailability)) {
            return false;
        }

        VolunteerAvailability otherAvailability = (VolunteerAvailability) other;
        return dayOfWeek.equals(otherAvailability.dayOfWeek)
                && startTime.equals(otherAvailability.startTime)
                && endTime.equals(otherAvailability.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayOfWeek, startTime, endTime);
    }

    @Override
    public String toString() {
        return String.format("%s %s to %s", dayOfWeek, startTime, endTime);
    }
}
