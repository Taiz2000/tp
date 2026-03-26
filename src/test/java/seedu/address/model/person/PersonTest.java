package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonTest {
    private static final String VALID_ROLE_BOB = "Team Lead";
    private static final String VALID_NOTES_BOB = "Prefers morning shifts";

    @Test
    public void unmodifiableSets_modifySet_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().clear());
        assertThrows(UnsupportedOperationException.class, () -> person.getAvailabilities().remove(null));
        assertThrows(UnsupportedOperationException.class, () -> person.getRecords().remove(null));
    }

    @Test
    public void constructor_withRoleAndNotes_storesFields() {
        Person person = new PersonBuilder().withRole(VALID_ROLE_BOB).withNotes(VALID_NOTES_BOB).build();
        assertEquals(new Role(VALID_ROLE_BOB), person.getRole());
        assertEquals(new Notes(VALID_NOTES_BOB), person.getNotes());
    }

    @Test
    public void constructor_withoutRoleAndNotes_defaultsToEmptyValues() {
        Person person = new Person(new Name("Amy Bee"), new Phone("85355255"), new Email("amy@example.com"),
                new Address("123 Jurong West"), Collections.emptySet());
        assertEquals(Person.EMPTY_ROLE, person.getRole());
        assertEquals(Person.EMPTY_NOTES, person.getNotes());
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same phone, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withRole(VALID_ROLE_BOB).withNotes(VALID_NOTES_BOB)
                .withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // same email ignoring case, all other attributes different -> returns true
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(ALICE.getEmail().value.toUpperCase()).withAddress(VALID_ADDRESS_BOB)
                .withRole(VALID_ROLE_BOB).withNotes(VALID_NOTES_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // same email with a different phone -> returns true
        editedAlice = new PersonBuilder(ALICE)
                .withName("Noah Lim")
                .withPhone("93334444")
                .withEmail(ALICE.getEmail().value)
                .withAddress("28 Clementi Ave 3")
                .withRole("Event Coordinator")
                .withNotes("Available on weekends")
                .withTags("logistics")
                .build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // same phone and same email, all other attributes different -> returns true
        editedAlice = new PersonBuilder(ALICE)
                .withName("Maya Tan")
                .withAddress("91 Bedok North Ave 2")
                .withRole("Volunteer")
                .withNotes("Can mentor new joiners")
                .withTags("mentors")
                .build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // same name only, phone and email different -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // name differs in case, phone and email different -> returns false
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase())
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).build();
        assertFalse(BOB.isSamePerson(editedBob));

        // name has trailing spaces, phone and email different -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).build();
        assertFalse(BOB.isSamePerson(editedBob));

        // different phone and different email -> returns false
        editedBob = new PersonBuilder(BOB).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).build();
        assertFalse(BOB.isSamePerson(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));

        // different role -> returns false
        editedAlice = new PersonBuilder(ALICE).withRole(VALID_ROLE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different notes -> returns false
        editedAlice = new PersonBuilder(ALICE).withNotes(VALID_NOTES_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different availabilities -> returns false
        editedAlice = new PersonBuilder(ALICE)
                .withAvailabilities(new VolunteerAvailability(DayOfWeek.MONDAY, LocalTime.of(12, 0),
                        LocalTime.of(13, 0)))
                .build();
        assertFalse(ALICE.equals(editedAlice));

        // different records -> returns false
        editedAlice = new PersonBuilder(ALICE)
                .withRecords(new VolunteerRecord(LocalDateTime.of(2026, 3, 20, 9, 0),
                        LocalDateTime.of(2026, 3, 20, 12, 0)))
                .build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail() + ", address=" + ALICE.getAddress() + ", role=" + ALICE.getRole()
                + ", notes=" + ALICE.getNotes() + ", tags=" + ALICE.getTags()
                + ", availabilities=" + ALICE.getAvailabilities()
                + ", records=" + ALICE.getRecords() + "}";
        assertEquals(expected, ALICE.toString());
    }

    @Test
    public void hashCode_sameValues_sameHashCode() {
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertEquals(ALICE.hashCode(), aliceCopy.hashCode());
    }
}
