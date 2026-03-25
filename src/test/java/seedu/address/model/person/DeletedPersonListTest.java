package seedu.address.model.person;

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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.testutil.PersonBuilder;

public class DeletedPersonListTest {

    private final DeletedPersonList deletedPersonList = new DeletedPersonList();

    @Test
    public void contains_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> deletedPersonList.contains(null));
    }

    @Test
    public void contains_personNotInList_returnsFalse() {
        assertFalse(deletedPersonList.contains(ALICE));
    }

    @Test
    public void contains_personInList_returnsTrue() {
        deletedPersonList.add(ALICE);
        assertTrue(deletedPersonList.contains(ALICE));
    }

    @Test
    public void contains_personWithSameIdentityFieldsInList_returnsFalse() {
        deletedPersonList.add(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertFalse(deletedPersonList.contains(editedAlice));
    }

    @Test
    public void contains_personWithSameNameButDifferentPhoneAndEmail_returnsFalse() {
        deletedPersonList.add(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).build();
        assertFalse(deletedPersonList.contains(editedAlice));
    }

    @Test
    public void add_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> deletedPersonList.add(null));
    }

    @Test
    public void add_duplicatePerson_doesNothing() {
        deletedPersonList.add(ALICE);
        deletedPersonList.add(ALICE);
        DeletedPersonList expectedDeletedPersonList = new DeletedPersonList();
        expectedDeletedPersonList.add(ALICE);
        assertEquals(expectedDeletedPersonList, deletedPersonList);
    }

    @Test
    public void add_differentPersons_success() {
        deletedPersonList.add(ALICE);
        assertTrue(deletedPersonList.contains(ALICE));

        Person duplicateByEmail = new PersonBuilder(ALICE)
                .withPhone(VALID_PHONE_AMY)
                .withEmail(ALICE.getEmail().value.toUpperCase())
                .build();
        deletedPersonList.add(duplicateByEmail);
        assertTrue(deletedPersonList.contains(duplicateByEmail));

        Person duplicateByPhone = new PersonBuilder(ALICE)
                .withName("Noah Lim")
                .withEmail("noah.lim@example.com")
                .build();
        deletedPersonList.add(duplicateByPhone);
        assertTrue(deletedPersonList.contains(duplicateByPhone));

        Person duplicateByPhoneAndEmail = new PersonBuilder(ALICE)
                .withName("Sarah Teo")
                .withAddress("31 Bukit Batok St 11")
                .withRole("Event Volunteer")
                .withNotes("Prefers evening shifts")
                .withTags("events")
                .build();
        deletedPersonList.add(duplicateByPhoneAndEmail);
        assertTrue(deletedPersonList.contains(duplicateByPhoneAndEmail));
    }

    @Test
    public void setPerson_nullTargetPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> deletedPersonList.setPerson(null, ALICE));
    }

    @Test
    public void setPerson_nullEditedPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> deletedPersonList.setPerson(ALICE, null));
    }

    @Test
    public void setPerson_targetPersonNotInList_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> deletedPersonList.setPerson(ALICE, ALICE));
    }

    @Test
    public void setPerson_editedPersonIsSamePerson_success() {
        deletedPersonList.add(ALICE);
        deletedPersonList.setPerson(ALICE, ALICE);
        DeletedPersonList expectedDeletedPersonList = new DeletedPersonList();
        expectedDeletedPersonList.add(ALICE);
        assertEquals(expectedDeletedPersonList, deletedPersonList);
    }

    @Test
    public void setPerson_editedPersonHasDifferentIdentity_success() {
        deletedPersonList.add(ALICE);
        deletedPersonList.setPerson(ALICE, BOB);
        DeletedPersonList expectedDeletedPersonList = new DeletedPersonList();
        expectedDeletedPersonList.add(BOB);
        assertEquals(expectedDeletedPersonList, deletedPersonList);
    }

    @Test
    public void setPerson_editedPersonHasNonUniqueIdentity_deletesTarget() {
        deletedPersonList.add(ALICE);
        deletedPersonList.add(BOB);
        deletedPersonList.setPerson(ALICE, BOB);
        DeletedPersonList expectedDeletedPersonList = new DeletedPersonList();
        expectedDeletedPersonList.add(BOB);
        assertEquals(expectedDeletedPersonList, deletedPersonList);
    }

    @Test
    public void remove_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> deletedPersonList.remove(null));
    }

    @Test
    public void remove_personDoesNotExist_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> deletedPersonList.remove(ALICE));
    }

    @Test
    public void remove_existingPerson_removesPerson() {
        deletedPersonList.add(ALICE);
        deletedPersonList.remove(ALICE);
        DeletedPersonList expectedDeletedPersonList = new DeletedPersonList();
        assertEquals(expectedDeletedPersonList, deletedPersonList);
    }

    @Test
    public void setPersons_nullUniquePersonList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> deletedPersonList.setPersons((DeletedPersonList) null));
    }

    @Test
    public void setPersons_uniquePersonList_replacesOwnListWithProvidedUniquePersonList() {
        deletedPersonList.add(ALICE);
        DeletedPersonList expectedDeletedPersonList = new DeletedPersonList();
        expectedDeletedPersonList.add(BOB);
        deletedPersonList.setPersons(expectedDeletedPersonList);
        assertEquals(expectedDeletedPersonList, deletedPersonList);
    }

    @Test
    public void setPersons_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> deletedPersonList.setPersons((List<Person>) null));
    }

    @Test
    public void setPersons_list_replacesOwnListWithProvidedList() {
        deletedPersonList.add(ALICE);
        List<Person> personList = Collections.singletonList(BOB);
        deletedPersonList.setPersons(personList);
        DeletedPersonList expectedDeletedPersonList = new DeletedPersonList();
        expectedDeletedPersonList.add(BOB);
        assertEquals(expectedDeletedPersonList, deletedPersonList);
    }

    @Test
    public void setPersons_listWithDuplicatePersons_throwsDuplicatePersonException() {
        List<Person> listWithDuplicatePersons = Arrays.asList(ALICE, ALICE);
        assertThrows(DuplicatePersonException.class, () -> deletedPersonList.setPersons(listWithDuplicatePersons));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> deletedPersonList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(deletedPersonList.asUnmodifiableObservableList().toString(), deletedPersonList.toString());
    }
}
