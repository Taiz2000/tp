package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
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
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String role;
    private final String notes;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final List<JsonAdaptedVolunteerAvailability> availabilities = new ArrayList<>();
    private final List<JsonAdaptedVolunteerRecord> records = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("address") String address,
            @JsonProperty("role") String role,
            @JsonProperty("notes") String notes,
            @JsonProperty("tags") List<JsonAdaptedTag> tags,
            @JsonProperty("availabilities") List<JsonAdaptedVolunteerAvailability> availabilities,
            @JsonProperty("records") List<JsonAdaptedVolunteerRecord> records) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.role = role;
        this.notes = notes;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (availabilities != null) {
            this.availabilities.addAll(availabilities);
        }
        if (records != null) {
            this.records.addAll(records);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        role = source.getRole().value;
        notes = source.getNotes().value;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        availabilities.addAll(source.getAvailabilities().stream()
                .map(JsonAdaptedVolunteerAvailability::new)
                .collect(Collectors.toList()));
        records.addAll(source.getRecords().stream()
                .map(JsonAdaptedVolunteerRecord::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        final List<VolunteerAvailability> personAvailabilities = new ArrayList<>();
        for (JsonAdaptedVolunteerAvailability availability : availabilities) {
            personAvailabilities.add(availability.toModelType());
        }

        final List<VolunteerRecord> personRecords = new ArrayList<>();
        for (JsonAdaptedVolunteerRecord record : records) {
            personRecords.add(record.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final String roleToUse = role == null ? Person.EMPTY_ROLE.value : role;
        if (!Role.isValidRole(roleToUse)) {
            throw new IllegalValueException(Role.MESSAGE_CONSTRAINTS);
        }
        final Role modelRole = new Role(roleToUse);

        final String notesToUse = notes == null ? Person.EMPTY_NOTES.value : notes;
        if (!Notes.isValidNotes(notesToUse)) {
            throw new IllegalValueException(Notes.MESSAGE_CONSTRAINTS);
        }
        final Notes modelNotes = new Notes(notesToUse);

        final Set<Tag> modelTags = new HashSet<>(personTags);
        final Set<VolunteerAvailability> modelAvailabilities = new HashSet<>(personAvailabilities);
        final Set<VolunteerRecord> modelRecords = new HashSet<>(personRecords);
        return new Person(modelName, modelPhone, modelEmail, modelAddress,
                modelRole, modelNotes, modelTags, modelAvailabilities, modelRecords);
    }

}
