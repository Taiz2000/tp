package seedu.address.model.person;

import java.util.Iterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A list of persons. Supports a minimal set of list operations.
 */
public abstract class PersonList implements Iterable<Person> {

    protected final ObservableList<Person> internalList = FXCollections.observableArrayList();
    protected final ObservableList<Person> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent person as the given argument.
     */
    public abstract boolean contains(Person toCheck);

    /**
     * Adds a person to the list.
     * Extensions of this class may check if the person being added already exists in the list,
     * and handle that case accordingly.
     */
    public abstract void add(Person toAdd);

    /**
     * Replaces the person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the list.
     * Extensions of this class may check if {@code editedPerson} already exists in the list,
     * and handle that case accordingly.
     */
    public abstract void setPerson(Person target, Person editedPerson);

    /**
     * Removes the equivalent person from the list.
     * The person must exist in the list.
     */
    public abstract void remove(Person toRemove);

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Person> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Person> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonList)) {
            return false;
        }

        PersonList otherUniquePersonList = (PersonList) other;
        return internalList.equals(otherUniquePersonList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }
}
