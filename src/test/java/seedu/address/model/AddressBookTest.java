package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.testutil.PersonBuilder;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getKeptPersonList());
    }

    @Test
    public void constructor_withCopy() {
        AddressBook addressBook = new AddressBook(getTypicalAddressBook());
        assertEquals(getTypicalAddressBook(), addressBook);
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        AddressBookStub newData = new AddressBookStub(newPersons, Collections.emptyList());

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void resetData_withEmailDuplicateIgnoringCase_throwsDuplicatePersonException() {
        Person duplicateByEmail = new PersonBuilder(ALICE)
                .withPhone(VALID_PHONE_AMY)
                .withEmail(ALICE.getEmail().value.toUpperCase())
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, duplicateByEmail);
        AddressBookStub newData = new AddressBookStub(newPersons, Collections.emptyList());

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void resetData_withSameNameDifferentPhoneAndEmail_success() {
        Person sameNameDifferentContacts = new PersonBuilder(ALICE)
                .withPhone(VALID_PHONE_AMY)
                .withEmail(VALID_EMAIL_AMY)
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, sameNameDifferentContacts);
        AddressBookStub newData = new AddressBookStub(newPersons, Collections.emptyList());

        addressBook.resetData(newData);
        assertEquals(newPersons, addressBook.getKeptPersonList());
    }

    @Test
    public void resetData_withDuplicateDeletedPersons_throwsDuplicatePersonException() {
        List<Person> newDeletedPersons = Arrays.asList(ALICE, ALICE);
        AddressBookStub newData = new AddressBookStub(Collections.emptyList(), newDeletedPersons);

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasKeptPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasKeptPerson(null));
    }

    @Test
    public void hasKeptPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasKeptPerson(ALICE));
    }

    @Test
    public void hasKeptPerson_personInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.hasKeptPerson(ALICE));
    }

    @Test
    public void hasKeptPerson_personWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(addressBook.hasKeptPerson(editedAlice));
    }

    @Test
    public void hasKeptPerson_personWithSameNameButDifferentPhoneAndEmail_returnsFalse() {
        addressBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).build();
        assertFalse(addressBook.hasKeptPerson(editedAlice));
    }

    @Test
    public void hasDeletedPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasDeletedPerson(null));
    }

    @Test
    public void hasDeletedPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasDeletedPerson(ALICE));
    }

    @Test
    public void hasDeletedPerson_personInAddressBook_returnsTrue() {
        addressBook.addDeletedPerson(ALICE);
        assertTrue(addressBook.hasDeletedPerson(ALICE));

        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(addressBook.hasDeletedPerson(aliceCopy));
    }

    // This is an integration test.
    // For more extensive testing of UniquePersonList#add, see UniquePersonListTest.
    @Test
    public void addPerson() {
        addressBook.addPerson(ALICE);
        AddressBook expectedAddressBook = new AddressBook();
        expectedAddressBook.setKeptPersons(List.of(ALICE));
        assertEquals(expectedAddressBook, addressBook);
    }

    // This is an integration test.
    // For more extensive testing of UniquePersonList#setPerson, see UniquePersonListTest.
    @Test
    public void setKeptPerson() {
        addressBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        addressBook.setKeptPerson(ALICE, editedAlice);
        AddressBook expectedAddressBook = new AddressBook();
        expectedAddressBook.setKeptPersons(List.of(editedAlice));
        assertEquals(expectedAddressBook, addressBook);
    }

    // For more extensive testing of UniquePersonList#remove, see UniquePersonListTest.
    // For more extensive testing of DeletedPersonList#add, see DeletedPersonListTest.
    @Test
    public void deletePerson() {
        AddressBook expectedAddressBook = new AddressBook();
        expectedAddressBook.setDeletedPersons(List.of(ALICE));

        addressBook.addPerson(ALICE);
        addressBook.deletePerson(ALICE);
        assertEquals(expectedAddressBook, addressBook);

        // Deleting the same person again should not add it to the deleted persons list again.
        addressBook.addPerson(ALICE);
        addressBook.deletePerson(ALICE);
        assertEquals(expectedAddressBook, addressBook);
    }

    @Test
    public void deleteAllPersons_noKeptPersons_success() {
        addressBook.deleteAllPersons();
        assertEquals(new AddressBook(), addressBook);
    }

    @Test
    public void deleteAllPersons_withKeptPersons_success() {
        AddressBook expectedAddressBook = new AddressBook();
        expectedAddressBook.setDeletedPersons(List.of(ALICE, BOB));

        addressBook.addPerson(ALICE);
        addressBook.addPerson(BOB);
        addressBook.deleteAllPersons();
        assertEquals(expectedAddressBook, addressBook);
    }

    @Test
    public void addDeletedPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.addDeletedPerson(null));
    }

    @Test
    public void addDeletedPerson_validPerson_success() {
        AddressBook expectedAddressBook = new AddressBook();
        expectedAddressBook.setDeletedPersons(List.of(ALICE));
        addressBook.addDeletedPerson(ALICE);
        assertEquals(expectedAddressBook, addressBook);
    }

    @Test
    public void getKeptPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getKeptPersonList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName()
                + "{keptPersons=" + addressBook.getKeptPersonList() + ", "
                + "deletedPersons=" + addressBook.getDeletedPersonList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> keptPersons = FXCollections.observableArrayList();
        private final ObservableList<Person> deletedPersons = FXCollections.observableArrayList();

        AddressBookStub(Collection<Person> keptPersons, Collection<Person> deletedPersons) {
            this.keptPersons.setAll(keptPersons);
            this.deletedPersons.setAll(deletedPersons);
        }

        @Override
        public ObservableList<Person> getKeptPersonList() {
            return keptPersons;
        }

        @Override
        public ObservableList<Person> getDeletedPersonList() {
            return deletedPersons;
        }
    }

}
