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
    public void compare_allAttributesAscending_returnsExpectedOrder() {
        Person first = new PersonBuilder()
                .withName("alice")
                .withPhone("12345")
                .withEmail("a@example.com")
                .withRole("Developer")
                .withTags("beta", "alpha")
                .build();
        Person second = new PersonBuilder()
                .withName("BOB")
                .withPhone("23456")
                .withEmail("b@example.com")
                .withRole("Tester")
                .withTags("beta", "charlie")
                .build();

        assertTrue(new PersonSortComparator(SortAttribute.NAME, SortOrder.ASC).compare(first, second) < 0);
        assertTrue(new PersonSortComparator(SortAttribute.PHONE, SortOrder.ASC).compare(first, second) < 0);
        assertTrue(new PersonSortComparator(SortAttribute.EMAIL, SortOrder.ASC).compare(first, second) < 0);
        assertTrue(new PersonSortComparator(SortAttribute.ROLE, SortOrder.ASC).compare(first, second) < 0);
        assertTrue(new PersonSortComparator(SortAttribute.TAG, SortOrder.ASC).compare(first, second) < 0);
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
}
