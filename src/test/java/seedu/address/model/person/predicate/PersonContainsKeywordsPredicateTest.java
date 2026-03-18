package seedu.address.model.person.predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        PersonContainsKeywordsPredicate firstPredicate =
                new PersonContainsKeywordsPredicate(firstPredicateKeywordList);
        PersonContainsKeywordsPredicate secondPredicate =
                new PersonContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PersonContainsKeywordsPredicate firstPredicateCopy =
                new PersonContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_keywordsMatchFields_returnsTrue() {
        // One keyword
        // Name keyword
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList("Alice"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Phone keyword
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("12345"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("12345").build()));

        // Email keyword
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("alice@email.com"));
        assertTrue(predicate.test(new PersonBuilder().withEmail("alice@email.com").build()));

        // Address keyword
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("Main"));
        assertTrue(predicate.test(new PersonBuilder().withAddress("Main Street").build()));

        // Role keyword
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("Developer"));
        assertTrue(predicate.test(new PersonBuilder().withRole("Developer").build()));

        // Notes keyword
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("golf"));
        assertTrue(predicate.test(new PersonBuilder().withNotes("likes golf").build()));

        // Tag keyword
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("friend"));
        assertTrue(predicate.test(new PersonBuilder().withTags("friend").build()));

        // Multiple keywords, from same field
        predicate = new PersonContainsKeywordsPredicate(Arrays.asList("Alice", "Bob"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Multiple keywords, from different fields
        predicate = new PersonContainsKeywordsPredicate(Arrays.asList("12345", "alice@email.com"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("12345").withEmail("alice@email.com").build()));

        // Only one matching keyword
        predicate = new PersonContainsKeywordsPredicate(Arrays.asList("Bob", "Carol"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Carol").build()));

        // Mixed-case keywords
        predicate = new PersonContainsKeywordsPredicate(Arrays.asList("aLIce", "bOB"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_keywordsDoNotMatchAnyField_returnsFalse() {
        // Zero keywords
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Non-matching keyword
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("Carol"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Keyword does not match any field
        predicate = new PersonContainsKeywordsPredicate(Arrays.asList("Nope", "StillNo"));
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
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords);

        String expected = PersonContainsKeywordsPredicate.class.getCanonicalName()
                + "{keywords=" + keywords + ", match_type=keywords}";
        assertEquals(expected, predicate.toString());
    }
}
