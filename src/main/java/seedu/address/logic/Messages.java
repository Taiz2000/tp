package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_UNKNOWN_PREFIX =
            "Unknown prefix detected: %1$s. Check that you are using only recognised prefixes (%2$s).";
    public static final String MESSAGE_DUPLICATE_FIELDS =
            "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_NOT_VIEWING_KEPT_PERSONS =
            "You must be viewing the working list of kept contacts to perform this command.";
    public static final String MESSAGE_NOT_VIEWING_DELETED_PERSONS =
            "You must be viewing the recycle bin of recently deleted contacts to perform this command.";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Address: ")
                .append(person.getAddress())
                .append("; Role: ")
                .append(person.getRole())
                .append("; Notes: ")
                .append(person.getNotes())
                .append("; Tags: ");
        person.getTags().forEach(tag -> builder.append(tag).append(" "));
        builder.append("; Availabilities: ");
        person.getAvailabilities().forEach(availability -> builder.append(availability).append(" "));
        builder.append("; Records: ");
        person.getRecords().forEach(record -> builder.append(record).append(" "));
        return builder.toString().trim();
    }
}
