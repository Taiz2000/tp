package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Represents a Volunteer's specific volunteering record in the address book.
 * Guarantees: immutable; details are present and not null.
 */
public class VolunteerRecord {

    public static final String MESSAGE_CONSTRAINTS =
            "Volunteer record must be in format yyyy-MM-ddTHH:mm,yyyy-MM-ddTHH:mm "
                    + "(e.g. 2026-03-20T14:00,2026-03-20T17:00), and start must be before end.";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public final LocalDateTime startDateTime;
    public final LocalDateTime endDateTime;

    /**
     * Constructs a {@code VolunteerRecord}.
     *
     * @param startDateTime A valid start date and time.
     * @param endDateTime A valid end date and time.
     */
    public VolunteerRecord(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        requireNonNull(startDateTime);
        requireNonNull(endDateTime);
        if (!startDateTime.isBefore(endDateTime)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    /**
     * Returns true if {@code value} can be parsed as
     * {@code yyyy-MM-ddTHH:mm,yyyy-MM-ddTHH:mm} and start is before end.
     */
    public static boolean isValidRecord(String value) {
        requireNonNull(value);

        String[] parts = value.trim().split(",");
        if (parts.length != 2) {
            return false;
        }

        try {
            LocalDateTime start = LocalDateTime.parse(parts[0].trim());
            LocalDateTime end = LocalDateTime.parse(parts[1].trim());
            return start.isBefore(end);
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    /**
     * Parses {@code value} of format {@code yyyy-MM-ddTHH:mm,yyyy-MM-ddTHH:mm}.
     */
    public static VolunteerRecord fromString(String value) {
        if (!isValidRecord(value)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }

        String[] parts = value.trim().split(",");
        LocalDateTime start = LocalDateTime.parse(parts[0].trim());
        LocalDateTime end = LocalDateTime.parse(parts[1].trim());
        return new VolunteerRecord(start, end);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof VolunteerRecord)) {
            return false;
        }

        VolunteerRecord otherRecord = (VolunteerRecord) other;
        return startDateTime.equals(otherRecord.startDateTime)
                && endDateTime.equals(otherRecord.endDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDateTime, endDateTime);
    }

    @Override
    public String toString() {
        return String.format("%s to %s",
                startDateTime.format(FORMATTER),
                endDateTime.format(FORMATTER));
    }
}
