package seedu.address.model.person.sort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class PersonSortComparatorTest {

    @Test
    public void constructor_nullAttribute_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PersonSortComparator(null, SortOrder.ASC));
    }

    @Test
    public void constructor_nullOrder_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PersonSortComparator(SortAttribute.NAME, null));
    }

    @Test
    public void equals() {
        PersonSortComparator nameAscComparator = new PersonSortComparator(SortAttribute.NAME, SortOrder.ASC);
        PersonSortComparator nameAscComparatorCopy = new PersonSortComparator(SortAttribute.NAME, SortOrder.ASC);
        PersonSortComparator phoneAscComparator = new PersonSortComparator(SortAttribute.PHONE, SortOrder.ASC);
        PersonSortComparator nameDescComparator = new PersonSortComparator(SortAttribute.NAME, SortOrder.DESC);

        // same object -> returns true
        assertTrue(nameAscComparator.equals(nameAscComparator));

        // same values -> returns true
        assertTrue(nameAscComparator.equals(nameAscComparatorCopy));

        // different types -> returns false
        assertFalse(nameAscComparator.equals(1));

        // null -> returns false
        assertFalse(nameAscComparator.equals(null));

        // different attribute -> returns false
        assertFalse(nameAscComparator.equals(phoneAscComparator));

        // different order -> returns false
        assertFalse(nameAscComparator.equals(nameDescComparator));
    }

    @Test
    public void toStringMethod() {
        PersonSortComparator comparator = new PersonSortComparator(SortAttribute.EMAIL, SortOrder.ASC);
        String expected = PersonSortComparator.class.getCanonicalName() + "{attribute=" + SortAttribute.EMAIL
                + ", order=" + SortOrder.ASC + "}";
        assertEquals(expected, comparator.toString());
    }

    @Test
    public void compare_allAttributesAscendingAndDescending_returnsExpectedOrder() {
        Person first = new PersonBuilder()
                .withName("alice")
                .withPhone("12345")
                .withEmail("a@example.com")
                .withAddress("Apple Street")
                .withRole("Developer")
                .withTags("beta", "alpha")
                .withRecords("2026-03-20T09:00,2026-03-20T12:00")
                .build();
        Person second = new PersonBuilder()
                .withName("BOB")
                .withPhone("23456")
                .withEmail("b@example.com")
                .withAddress("Banana Avenue")
                .withRole("Tester")
                .withTags("beta", "charlie")
                .withRecords("2026-04-01T09:00,2026-04-01T12:00")
                .build();

        assertTrue(new PersonSortComparator(SortAttribute.NAME, SortOrder.ASC).compare(first, second) < 0);
        assertTrue(new PersonSortComparator(SortAttribute.PHONE, SortOrder.ASC).compare(first, second) < 0);
        assertTrue(new PersonSortComparator(SortAttribute.EMAIL, SortOrder.ASC).compare(first, second) < 0);
        assertTrue(new PersonSortComparator(SortAttribute.ADDRESS, SortOrder.ASC).compare(first, second) < 0);
        assertTrue(new PersonSortComparator(SortAttribute.ROLE, SortOrder.ASC).compare(first, second) < 0);
        assertTrue(new PersonSortComparator(SortAttribute.TAG, SortOrder.ASC).compare(first, second) < 0);
        assertTrue(new PersonSortComparator(SortAttribute.VR, SortOrder.ASC).compare(first, second) < 0);

        assertTrue(new PersonSortComparator(SortAttribute.NAME, SortOrder.DESC).compare(first, second) > 0);
        assertTrue(new PersonSortComparator(SortAttribute.PHONE, SortOrder.DESC).compare(first, second) > 0);
        assertTrue(new PersonSortComparator(SortAttribute.EMAIL, SortOrder.DESC).compare(first, second) > 0);
        assertTrue(new PersonSortComparator(SortAttribute.ADDRESS, SortOrder.DESC).compare(first, second) > 0);
        assertTrue(new PersonSortComparator(SortAttribute.ROLE, SortOrder.DESC).compare(first, second) > 0);
        assertTrue(new PersonSortComparator(SortAttribute.TAG, SortOrder.DESC).compare(first, second) > 0);
        assertTrue(new PersonSortComparator(SortAttribute.VR, SortOrder.DESC).compare(first, second) > 0);
    }

    @Test
    public void compare_phoneWithPlusPrefixVsPlainDigits_plusSortsFirst() {
        // Regression pin for the accepted caveat: PersonSortComparator compares phones lexicographically
        // via String.CASE_INSENSITIVE_ORDER, and '+' (ASCII 43) sorts before digits (ASCII 48-57).
        // This means '+6591234567' sorts ahead of '6591234567' in ascending order.
        Person intlPhonePerson = new PersonBuilder().withName("Alice").withPhone("+6591234567").build();
        Person localPhonePerson = new PersonBuilder().withName("Bob").withPhone("6591234567").build();

        PersonSortComparator ascComparator = new PersonSortComparator(SortAttribute.PHONE, SortOrder.ASC);
        assertTrue(ascComparator.compare(intlPhonePerson, localPhonePerson) < 0);

        PersonSortComparator descComparator = new PersonSortComparator(SortAttribute.PHONE, SortOrder.DESC);
        assertTrue(descComparator.compare(intlPhonePerson, localPhonePerson) > 0);
    }

    @Test
    public void compare_tagOrderIgnoresInputOrder_returnsZero() {
        Person first = new PersonBuilder().withTags("beta", "alpha").build();
        Person second = new PersonBuilder().withTags("alpha", "beta").build();

        PersonSortComparator comparator = new PersonSortComparator(SortAttribute.TAG, SortOrder.ASC);
        assertEquals(0, comparator.compare(first, second));
    }

    @Test
    public void compare_descending_negatesResult() {
        Person first = new PersonBuilder().withName("Alice").build();
        Person second = new PersonBuilder().withName("Bob").build();

        PersonSortComparator comparator = new PersonSortComparator(SortAttribute.NAME, SortOrder.DESC);
        assertTrue(comparator.compare(first, second) > 0);
    }

    @Test
    public void compare_volunteerRecordsEmptyVsNonEmpty_expectedOrdersForBothOrders() {
        Person noRecords = new PersonBuilder().withName("Alice").build();
        Person withRecords = new PersonBuilder().withName("Bob")
                .withRecords("2026-03-20T09:00,2026-03-20T12:00")
                .build();

        PersonSortComparator ascComparator = new PersonSortComparator(SortAttribute.VR, SortOrder.ASC);
        assertTrue(ascComparator.compare(noRecords, withRecords) < 0);

        PersonSortComparator descComparator = new PersonSortComparator(SortAttribute.VR, SortOrder.DESC);
        assertTrue(descComparator.compare(noRecords, withRecords) > 0);
    }

    @Test
    public void compare_multipleVolunteerRecordsPerPerson_ordersByLatestRecordEnd() {
        Person earlierLatest = new PersonBuilder().withName("Alice")
                .withRecords("2026-03-20T09:00,2026-03-20T12:00",
                        "2026-03-25T14:00,2026-03-25T16:00")
                .build();
        Person laterLatest = new PersonBuilder().withName("Bob")
                .withRecords("2026-03-18T09:00,2026-03-18T12:00",
                        "2026-04-01T14:00,2026-04-01T16:00")
                .build();

        PersonSortComparator ascComparator = new PersonSortComparator(SortAttribute.VR, SortOrder.ASC);
        assertTrue(ascComparator.compare(earlierLatest, laterLatest) < 0);

        PersonSortComparator descComparator = new PersonSortComparator(SortAttribute.VR, SortOrder.DESC);
        assertTrue(descComparator.compare(earlierLatest, laterLatest) > 0);
    }
}
