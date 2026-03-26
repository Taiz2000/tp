package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    public static final Role EMPTY_ROLE = new Role("");
    public static final Notes EMPTY_NOTES = new Notes("");

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Role role;
    private final Notes notes;
    private final Set<Tag> tags = new HashSet<>();
    private final Set<VolunteerAvailability> availabilities = new HashSet<>();
    private final Set<VolunteerRecord> records = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        this(name, phone, email, address, EMPTY_ROLE,
                EMPTY_NOTES, tags, Collections.emptySet(), Collections.emptySet());
    }

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Role role, Notes notes, Set<Tag> tags) {
        this(name, phone, email, address, role, notes, tags, Collections.emptySet(), Collections.emptySet());
    }

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Role role, Notes notes, Set<Tag> tags,
            Set<VolunteerAvailability> availabilities, Set<VolunteerRecord> records) {
        requireAllNonNull(name, phone, email, address, role, notes, tags, availabilities, records);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.role = role;
        this.notes = notes;
        this.tags.addAll(tags);
        this.availabilities.addAll(availabilities);
        this.records.addAll(records);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Role getRole() {
        return role;
    }

    public Notes getNotes() {
        return notes;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable set of volunteer availabilities, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<VolunteerAvailability> getAvailabilities() {
        return Collections.unmodifiableSet(availabilities);
    }

    /**
     * Returns an immutable set of volunteer records, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<VolunteerRecord> getRecords() {
        return Collections.unmodifiableSet(records);
    }

    /**
     * Returns true if both persons have the same identity.
     * Two persons are considered the same if they share the same phone number,
     * or if their email addresses match case-insensitively.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && (otherPerson.getPhone().equals(getPhone())
                        || otherPerson.getEmail().value.equalsIgnoreCase(getEmail().value));
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && role.equals(otherPerson.role)
                && notes.equals(otherPerson.notes)
                && tags.equals(otherPerson.tags)
                && availabilities.equals(otherPerson.availabilities)
                && records.equals(otherPerson.records);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, role, notes, tags, availabilities, records);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("role", role)
                .add("notes", notes)
                .add("tags", tags)
                .add("availabilities", availabilities)
                .add("records", records)
                .toString();
    }

}
