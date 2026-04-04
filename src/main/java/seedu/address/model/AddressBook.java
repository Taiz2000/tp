package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.Objects;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.DeletedPersonList;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

/**
 * Wraps all data at the address-book level
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList keptPersons;
    private final DeletedPersonList deletedPersons;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        keptPersons = new UniquePersonList();
        deletedPersons = new DeletedPersonList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the kept person list with {@code keptPersons}.
     * {@code keptPersons} must not contain duplicate persons with respect to identity fields.
     */
    public void setKeptPersons(List<Person> keptPersons) {
        requireAllNonNull(keptPersons);
        this.keptPersons.setPersons(keptPersons);
    }

    /**
     * Replaces the contents of the deleted person list with {@code deletedPersons}.
     * {@code deletedPersons} must not contain duplicate persons with all fields equal.
     */
    public void setDeletedPersons(List<Person> deletedPersons) {
        requireAllNonNull(deletedPersons);
        this.deletedPersons.setPersons(deletedPersons);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setKeptPersons(newData.getKeptPersonList());
        setDeletedPersons(newData.getDeletedPersonList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the list of kept persons.
     */
    public boolean hasKeptPerson(Person person) {
        requireNonNull(person);
        return keptPersons.contains(person);
    }

    /**
     * Returns true if a person equal to {@code person} exists in the list of deleted persons.
     */
    public boolean hasDeletedPerson(Person person) {
        requireNonNull(person);
        return deletedPersons.contains(person);
    }

    /**
     * Adds a person to the list of kept persons in the address book.
     * The person must not already exist in the list of kept persons.
     */
    public void addPerson(Person p) {
        requireNonNull(p);
        assert !hasKeptPerson(p) : "Person is already in the list of kept persons";
        keptPersons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list of kept persons with {@code editedPerson}.
     * {@code target} must exist in the list of kept persons.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the list.
     */
    public void setKeptPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);
        assert hasKeptPerson(target) : "Target person is not in the list of kept persons";
        keptPersons.setPerson(target, editedPerson);
    }

    /**
     * Moves {@code key} from the list of kept persons to the list of deleted persons.
     * {@code key} must exist in the list of kept persons.
     * If {@code key} already exists in the list of deleted persons, it will not be added again.
     */
    public void deletePerson(Person key) {
        requireNonNull(key);
        assert hasKeptPerson(key) : "Person to delete is not in the list of kept persons";
        keptPersons.remove(key);
        deletedPersons.add(key);
    }

    /**
     * Moves {@code key} from the list of deleted persons to the list of kept persons.
     * {@code key} must exist in the list of deleted persons.
     * {@code key} must not already exist in the list of kept persons.
     */
    public void restorePerson(Person key) {
        requireNonNull(key);
        assert hasDeletedPerson(key) : "Person to restore is not in the list of deleted persons";
        assert !hasKeptPerson(key) : "Person to restore already exists in the list of kept persons";
        deletedPersons.remove(key);
        keptPersons.add(key);
    }

    /**
     * Adds a person directly to the list of deleted persons in the address book.
     * If the person already exists in the list of deleted persons, it will not be added again.
     */
    public void addDeletedPerson(Person p) {
        requireNonNull(p);
        deletedPersons.add(p);
    }

    /**
     * Deletes all persons in the list of kept persons, and adds them to the list of deleted persons.
     * Persons that already exist in the list of deleted persons will not be added again.
     */
    public void deleteAllPersons() {
        for (Person person : keptPersons) {
            addDeletedPerson(person);
        }
        setKeptPersons(List.of());
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("keptPersons", keptPersons)
                .add("deletedPersons", deletedPersons)
                .toString();
    }

    @Override
    public ObservableList<Person> getKeptPersonList() {
        return keptPersons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Person> getDeletedPersonList() {
        return deletedPersons.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return keptPersons.equals(otherAddressBook.keptPersons)
                && deletedPersons.equals(otherAddressBook.deletedPersons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keptPersons, deletedPersons);
    }
}
