package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredKeptPersons;
    private final FilteredList<Person> filteredDeletedPersons;
    private final SortedList<Person> sortedPersons;
    private Comparator<Person> sortComparator;
    private String lastCommandText;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredKeptPersons = new FilteredList<>(this.addressBook.getKeptPersonList());
        filteredDeletedPersons = new FilteredList<>(this.addressBook.getDeletedPersonList());
        sortedPersons = new SortedList<>(filteredKeptPersons);
        sortComparator = null;
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    @Override
    public Map<String, String> getCommandAliases() {
        return userPrefs.getCommandAliases();
    }

    @Override
    public boolean hasCommandAlias(String shortName) {
        requireNonNull(shortName);
        return userPrefs.getCommandAliases().containsKey(shortName);
    }

    @Override
    public void setCommandAlias(String shortName, String template) {
        requireAllNonNull(shortName, template);
        userPrefs.setCommandAlias(shortName, template);
    }

    @Override
    public void removeCommandAlias(String shortName) {
        requireNonNull(shortName);
        userPrefs.removeCommandAlias(shortName);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasKeptPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.deletePerson(target);
    }

    @Override
    public void restorePerson(Person target) {
        addressBook.restorePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredKeptPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setKeptPerson(target, editedPerson);
    }

    @Override
    public void deleteAllPersons() {
        addressBook.deleteAllPersons();
    }

    //=========== Filtered Person List Accessors =============================================================

    @Override
    public ObservableList<Person> getFilteredKeptPersonList() {
        return sortedPersons;
    }

    @Override
    public ObservableList<Person> getKeptPersonList() {
        return addressBook.getKeptPersonList();
    }

    @Override
    public ObservableList<Person> getFilteredDeletedPersonList() {
        return filteredDeletedPersons;
    }

    @Override
    public void updateFilteredKeptPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredKeptPersons.setPredicate(predicate);
    }

    @Override
    public void updateFilteredDeletedPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredDeletedPersons.setPredicate(predicate);
    }

    @Override
    public void updateSortedPersonList(Comparator<Person> comparator) {
        sortComparator = comparator;
        sortedPersons.setComparator(comparator);
    }

    @Override
    public String getLastCommandText() {
        return lastCommandText;
    }

    @Override
    public void setLastCommandText(String commandText) {
        this.lastCommandText = commandText;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredKeptPersons.equals(otherModelManager.filteredKeptPersons)
                && filteredDeletedPersons.equals(otherModelManager.filteredDeletedPersons)
                && Objects.equals(sortComparator, otherModelManager.sortComparator);
    }

}
