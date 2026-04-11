package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.predicates.PersonContainsKeywordsPredicate;
import seedu.address.model.person.sort.PersonSortComparator;
import seedu.address.model.person.sort.SortAttribute;
import seedu.address.model.person.sort.SortOrder;
import seedu.address.testutil.AddressBookBuilder;

/**
 * Reused from Codex suggestions upon providing specifications
 */

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        userPrefs.setCommandAlias("ls", "list");
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        userPrefs.setCommandAlias("rm", "delete");
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void commandAliases_modifyAliasRegistry_success() {
        modelManager.setCommandAlias("ls", "list");
        assertTrue(modelManager.hasCommandAlias("ls"));
        assertEquals(Map.of("ls", "list"), modelManager.getCommandAliases());

        modelManager.removeCommandAlias("ls");
        assertFalse(modelManager.hasCommandAlias("ls"));
        assertEquals(Map.of(), modelManager.getCommandAliases());
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void restorePerson_deletedPersonMovesBackToKeptPersons() {
        AddressBook addressBook = new AddressBook();
        addressBook.addDeletedPerson(ALICE);
        modelManager.setAddressBook(addressBook);

        modelManager.restorePerson(ALICE);

        assertTrue(modelManager.hasPerson(ALICE));
        assertTrue(modelManager.getFilteredDeletedPersonList().isEmpty());
    }

    @Test
    public void getFilteredKeptPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredKeptPersonList().remove(0));
    }

    @Test
    public void getFilteredDeletedPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredDeletedPersonList().remove(0));
    }

    @Test
    public void updateFilteredKeptPersonList_nullPredicate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.updateFilteredKeptPersonList(null));
    }

    @Test
    public void updateSortedPersonList_nullComparator_resetsSorting() {
        modelManager.addPerson(ALICE);
        modelManager.addPerson(BENSON);
        modelManager.updateSortedPersonList(new PersonSortComparator(SortAttribute.NAME, SortOrder.DESC));
        assertEquals(Arrays.asList(BENSON, ALICE), modelManager.getFilteredKeptPersonList());

        modelManager.updateSortedPersonList(null);
        assertEquals(Arrays.asList(ALICE, BENSON), modelManager.getFilteredKeptPersonList());
    }

    @Test
    public void addPerson_filteredListResetsToShowAll() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        modelManager = new ModelManager(addressBook, new UserPrefs());
        modelManager.updateFilteredKeptPersonList(new PersonContainsKeywordsPredicate(
                Arrays.asList("Alice")));

        modelManager.addPerson(CARL);

        assertEquals(Arrays.asList(ALICE, BENSON, CARL), modelManager.getFilteredKeptPersonList());
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        AddressBook deletedAddressBook = new AddressBookBuilder()
                .withDeletedPerson(ALICE).withDeletedPerson(BENSON).build();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList for kept persons -> returns false
        String[] searchTerms = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredKeptPersonList(new PersonContainsKeywordsPredicate(Arrays.asList(searchTerms)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // both filteredLists are same -> returns true
        modelManager.setAddressBook(deletedAddressBook);
        modelManager.updateFilteredKeptPersonList(new PersonContainsKeywordsPredicate(Arrays.asList(searchTerms)));
        assertTrue(modelManager.equals(new ModelManager(deletedAddressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredKeptPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different sortedList -> returns false
        modelManager.updateSortedPersonList(new PersonSortComparator(SortAttribute.NAME, SortOrder.ASC));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateSortedPersonList(null);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }
}
