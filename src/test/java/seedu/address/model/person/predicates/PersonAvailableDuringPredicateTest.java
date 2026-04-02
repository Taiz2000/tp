package seedu.address.model.person.predicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.model.person.VolunteerAvailability;
import seedu.address.testutil.PersonBuilder;

public class PersonAvailableDuringPredicateTest {

    @Test
    public void test_availabilityFullyCoversQuery_returnsTrue() {
        // Volunteer available 13:00-18:00, query 14:00-17:00
        VolunteerAvailability query = VolunteerAvailability.fromString("MONDAY,14:00,17:00");
        PersonAvailableDuringPredicate predicate = new PersonAvailableDuringPredicate(query);

        Person person = new PersonBuilder()
                .withAvailabilities("MONDAY,13:00,18:00")
                .build();
        assertTrue(predicate.test(person));
    }

    @Test
    public void test_exactMatch_returnsTrue() {
        // Volunteer available exactly during query period
        VolunteerAvailability query = VolunteerAvailability.fromString("MONDAY,14:00,17:00");
        PersonAvailableDuringPredicate predicate = new PersonAvailableDuringPredicate(query);

        Person person = new PersonBuilder()
                .withAvailabilities("MONDAY,14:00,17:00")
                .build();
        assertTrue(predicate.test(person));
    }

    @Test
    public void test_volunteerStartsAfterQueryStart_returnsFalse() {
        // Volunteer available 15:00-18:00, query 14:00-17:00
        VolunteerAvailability query = VolunteerAvailability.fromString("MONDAY,14:00,17:00");
        PersonAvailableDuringPredicate predicate = new PersonAvailableDuringPredicate(query);

        Person person = new PersonBuilder()
                .withAvailabilities("MONDAY,15:00,18:00")
                .build();
        assertFalse(predicate.test(person));
    }

    @Test
    public void test_volunteerEndsBeforeQueryEnd_returnsFalse() {
        // Volunteer available 13:00-16:00, query 14:00-17:00
        VolunteerAvailability query = VolunteerAvailability.fromString("MONDAY,14:00,17:00");
        PersonAvailableDuringPredicate predicate = new PersonAvailableDuringPredicate(query);

        Person person = new PersonBuilder()
                .withAvailabilities("MONDAY,13:00,16:00")
                .build();
        assertFalse(predicate.test(person));
    }

    @Test
    public void test_differentDay_returnsFalse() {
        // Volunteer available on Tuesday, query on Monday
        VolunteerAvailability query = VolunteerAvailability.fromString("MONDAY,14:00,17:00");
        PersonAvailableDuringPredicate predicate = new PersonAvailableDuringPredicate(query);

        Person person = new PersonBuilder()
                .withAvailabilities("TUESDAY,13:00,18:00")
                .build();
        assertFalse(predicate.test(person));
    }

    @Test
    public void test_multipleAvailabilitiesOneCovering_returnsTrue() {
        // One availability does not cover, but another does
        VolunteerAvailability query = VolunteerAvailability.fromString("MONDAY,14:00,17:00");
        PersonAvailableDuringPredicate predicate = new PersonAvailableDuringPredicate(query);

        Person person = new PersonBuilder()
                .withAvailabilities("TUESDAY,09:00,12:00", "MONDAY,13:00,18:00")
                .build();
        assertTrue(predicate.test(person));
    }

    @Test
    public void test_noAvailabilities_returnsFalse() {
        VolunteerAvailability query = VolunteerAvailability.fromString("MONDAY,14:00,17:00");
        PersonAvailableDuringPredicate predicate = new PersonAvailableDuringPredicate(query);

        Person person = new PersonBuilder().build();
        assertFalse(predicate.test(person));
    }

    @Test
    public void equals() {
        VolunteerAvailability queryMonday = VolunteerAvailability.fromString("MONDAY,14:00,17:00");
        VolunteerAvailability queryTuesday = VolunteerAvailability.fromString("TUESDAY,14:00,17:00");
        PersonAvailableDuringPredicate firstPredicate = new PersonAvailableDuringPredicate(queryMonday);
        PersonAvailableDuringPredicate secondPredicate = new PersonAvailableDuringPredicate(queryTuesday);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PersonAvailableDuringPredicate firstPredicateCopy = new PersonAvailableDuringPredicate(queryMonday);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different query -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void toStringMethod() {
        VolunteerAvailability query = VolunteerAvailability.fromString("MONDAY,14:00,17:00");
        PersonAvailableDuringPredicate predicate = new PersonAvailableDuringPredicate(query);
        String expected = PersonAvailableDuringPredicate.class.getCanonicalName()
                + "{query=" + query + "}";
        assertEquals(expected, predicate.toString());
    }
}
