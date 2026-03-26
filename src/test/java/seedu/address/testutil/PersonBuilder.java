package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_ROLE = "";
    public static final String DEFAULT_NOTES = "";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Role role;
    private Notes notes;
    private Set<Tag> tags;
    private Set<VolunteerAvailability> availabilities;
    private Set<VolunteerRecord> records;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        role = new Role(DEFAULT_ROLE);
        notes = new Notes(DEFAULT_NOTES);
        tags = new HashSet<>();
        availabilities = new HashSet<>();
        records = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        role = personToCopy.getRole();
        notes = personToCopy.getNotes();
        tags = new HashSet<>(personToCopy.getTags());
        availabilities = new HashSet<>(personToCopy.getAvailabilities());
        records = new HashSet<>(personToCopy.getRecords());
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code Role} of the {@code Person} that we are building.
     */
    public PersonBuilder withRole(String role) {
        this.role = new Role(role);
        return this;
    }

    /**
     * Sets the {@code Notes} of the {@code Person} that we are building.
     */
    public PersonBuilder withNotes(String notes) {
        this.notes = new Notes(notes);
        return this;
    }

    /**
     * Parses the {@code availabilities} into a {@code Set<VolunteerAvailability>}
     * and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withAvailabilities(VolunteerAvailability ... availabilities) {
        this.availabilities = new HashSet<>(java.util.Arrays.asList(availabilities));
        return this;
    }

    /**
     * Parses the {@code availabilities} into a {@code Set<VolunteerAvailability>}
     * and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withAvailabilities(String ... availabilities) {
        this.availabilities = Stream.of(availabilities)
                .map(VolunteerAvailability::fromString)
                .collect(Collectors.toSet());
        return this;
    }

    /**
     * Parses the {@code records} into a {@code Set<VolunteerRecord>} and set it to the
     * {@code Person} that we are building.
     */
    public PersonBuilder withRecords(VolunteerRecord ... records) {
        this.records = new HashSet<>(java.util.Arrays.asList(records));
        return this;
    }

    /**
     * Parses the {@code records} into a {@code Set<VolunteerRecord>} and set it to the
     * {@code Person} that we are building.
     */
    public PersonBuilder withRecords(String ... records) {
        this.records = Stream.of(records)
                .map(VolunteerRecord::fromString)
                .collect(Collectors.toSet());
        return this;
    }

    public Person build() {
        return new Person(name, phone, email, address, role, notes, tags, availabilities, records);
    }

}
