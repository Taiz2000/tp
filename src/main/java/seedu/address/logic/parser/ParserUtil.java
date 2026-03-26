package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Notes;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.person.VolunteerAvailability;
import seedu.address.model.person.VolunteerRecord;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Tokenizes {@code input} into space-separated tokens.
     */
    public static List<String> tokenizeSpaceSeparated(String input) {
        requireNonNull(input);
        String trimmedInput = input.trim();
        if (trimmedInput.isEmpty()) {
            return List.of();
        }
        return Arrays.asList(trimmedInput.split("\\s+"));
    }

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String} into a {@code Role}.
     */
    public static Role parseRole(String role) throws ParseException {
        requireNonNull(role);
        String trimmedRole = role.trim();
        if (!Role.isValidRole(trimmedRole)) {
            throw new ParseException(Role.MESSAGE_CONSTRAINTS);
        }
        return new Role(trimmedRole);
    }

    /**
     * Parses a {@code String} into {@code Notes}.
     */
    public static Notes parseNotes(String notes) throws ParseException {
        requireNonNull(notes);
        String trimmedNotes = notes.trim();
        if (!Notes.isValidNotes(trimmedNotes)) {
            throw new ParseException(Notes.MESSAGE_CONSTRAINTS);
        }
        return new Notes(trimmedNotes);
    }

    /**
     * Parses a {@code String} into {@code VolunteerAvailability}.
     */
    public static VolunteerAvailability parseVolunteerAvailability(String availability) throws ParseException {
        requireNonNull(availability);
        String trimmedAvailability = availability.trim();
        if (!VolunteerAvailability.isValidAvailability(trimmedAvailability)) {
            throw new ParseException(VolunteerAvailability.MESSAGE_CONSTRAINTS);
        }
        return VolunteerAvailability.fromString(trimmedAvailability);
    }

    /**
     * Parses {@code Collection<String>} into a {@code Set<VolunteerAvailability>}.
     */
    public static Set<VolunteerAvailability> parseVolunteerAvailabilities(Collection<String> availabilities)
            throws ParseException {
        requireNonNull(availabilities);
        final Set<VolunteerAvailability> availabilitySet = new HashSet<>();
        for (String availability : availabilities) {
            availabilitySet.add(parseVolunteerAvailability(availability));
        }
        return availabilitySet;
    }

    /**
     * Parses a {@code String} into {@code VolunteerRecord}.
     */
    public static VolunteerRecord parseVolunteerRecord(String record) throws ParseException {
        requireNonNull(record);
        String trimmedRecord = record.trim();
        if (!VolunteerRecord.isValidRecord(trimmedRecord)) {
            throw new ParseException(VolunteerRecord.MESSAGE_CONSTRAINTS);
        }
        return VolunteerRecord.fromString(trimmedRecord);
    }

    /**
     * Parses {@code Collection<String>} into a {@code Set<VolunteerRecord>}.
     */
    public static Set<VolunteerRecord> parseVolunteerRecords(Collection<String> records) throws ParseException {
        requireNonNull(records);
        final Set<VolunteerRecord> recordSet = new HashSet<>();
        for (String record : records) {
            recordSet.add(parseVolunteerRecord(record));
        }
        return recordSet;
    }

}
