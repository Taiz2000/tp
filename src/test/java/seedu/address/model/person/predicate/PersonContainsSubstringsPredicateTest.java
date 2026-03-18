package seedu.address.model.person.predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonContainsSubstringsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        PersonContainsSubstringsPredicate firstPredicate =
                new PersonContainsSubstringsPredicate(firstPredicateKeywordList);
        PersonContainsSubstringsPredicate secondPredicate =
                new PersonContainsSubstringsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PersonContainsSubstringsPredicate firstPredicateCopy =
                new PersonContainsSubstringsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_substringsMatchFields_returnsTrue() {
        // One substring
        // Name substring
        PersonContainsSubstringsPredicate predicate =
                new PersonContainsSubstringsPredicate(Collections.singletonList("Ali"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Phone substring
        predicate = new PersonContainsSubstringsPredicate(Collections.singletonList("234"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("12345").build()));

        // Email substring
        predicate = new PersonContainsSubstringsPredicate(Collections.singletonList("email"));
        assertTrue(predicate.test(new PersonBuilder().withEmail("alice@email.com").build()));

        // Address substring
        predicate = new PersonContainsSubstringsPredicate(Collections.singletonList("St"));
        assertTrue(predicate.test(new PersonBuilder().withAddress("Main Street").build()));

        // Role substring
        predicate = new PersonContainsSubstringsPredicate(Collections.singletonList("Dev"));
        assertTrue(predicate.test(new PersonBuilder().withRole("Developer").build()));

        // Notes substring
        predicate = new PersonContainsSubstringsPredicate(Collections.singletonList("gol"));
        assertTrue(predicate.test(new PersonBuilder().withNotes("likes golf").build()));

        // Tag substring
        predicate = new PersonContainsSubstringsPredicate(Collections.singletonList("fri"));
        assertTrue(predicate.test(new PersonBuilder().withTags("friend").build()));

        // Multiple substrings, from same field
        predicate = new PersonContainsSubstringsPredicate(Arrays.asList("Ali", "Bob"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Multiple substrings, from different fields
        predicate = new PersonContainsSubstringsPredicate(Arrays.asList("234", "email"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("12345").withEmail("alice@email.com").build()));

        // Only one matching substring
        predicate = new PersonContainsSubstringsPredicate(Arrays.asList("Bob", "rol"));
        assertTrue(predicate.test(new PersonBuilder().withRole("Controller").build()));

        // Mixed-case substrings
        predicate = new PersonContainsSubstringsPredicate(Arrays.asList("aLI", "bOB"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_substringsDoNotMatchAnyField_returnsFalse() {
        // Zero substrings
        PersonContainsSubstringsPredicate predicate = new PersonContainsSubstringsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Non-matching substring
        predicate = new PersonContainsSubstringsPredicate(Collections.singletonList("Carol"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Substring does not match any field
        predicate = new PersonContainsSubstringsPredicate(Arrays.asList("Nope", "StillNo"));
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withPhone("12345")
                .withEmail("alice@email.com")
                .withAddress("Main Street")
                .withRole("Developer")
                .withNotes("likes golf")
                .withTags("friend")
                .build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        PersonContainsSubstringsPredicate predicate = new PersonContainsSubstringsPredicate(keywords);

        String expected = PersonContainsSubstringsPredicate.class.getCanonicalName()
                + "{keywords=" + keywords + ", match_type=substrings}";
        assertEquals(expected, predicate.toString());
    }
}
