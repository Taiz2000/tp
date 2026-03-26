package seedu.address.model.person.predicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonContainsFuzzyKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        PersonContainsFuzzyKeywordsPredicate firstPredicate =
                new PersonContainsFuzzyKeywordsPredicate(firstPredicateKeywordList);
        PersonContainsFuzzyKeywordsPredicate secondPredicate =
                new PersonContainsFuzzyKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PersonContainsFuzzyKeywordsPredicate firstPredicateCopy =
                new PersonContainsFuzzyKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_fuzzyKeywordsMatchFields_returnsTrue() {
        // Name keyword (no edit)
        PersonContainsFuzzyKeywordsPredicate predicate =
                new PersonContainsFuzzyKeywordsPredicate(Collections.singletonList("Alice"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Name keyword (single insertion)
        predicate = new PersonContainsFuzzyKeywordsPredicate(Collections.singletonList("Alicee"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Name keyword (single deletion)
        predicate = new PersonContainsFuzzyKeywordsPredicate(Collections.singletonList("Aice"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Name keyword (single edit)
        predicate = new PersonContainsFuzzyKeywordsPredicate(Collections.singletonList("Alic"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Phone keyword (single edit)
        predicate = new PersonContainsFuzzyKeywordsPredicate(Collections.singletonList("1234"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("12345").build()));

        // Email keyword (single edit)
        predicate = new PersonContainsFuzzyKeywordsPredicate(Collections.singletonList("alice@email.co"));
        assertTrue(predicate.test(new PersonBuilder().withEmail("alice@email.com").build()));

        // Address keyword (single edit)
        predicate = new PersonContainsFuzzyKeywordsPredicate(Collections.singletonList("Stret"));
        assertTrue(predicate.test(new PersonBuilder().withAddress("Main Street").build()));

        // Role keyword (single edit)
        predicate = new PersonContainsFuzzyKeywordsPredicate(Collections.singletonList("Devloper"));
        assertTrue(predicate.test(new PersonBuilder().withRole("Developer").build()));

        // Notes keyword (single edit)
        predicate = new PersonContainsFuzzyKeywordsPredicate(Collections.singletonList("glof"));
        assertTrue(predicate.test(new PersonBuilder().withNotes("likes golf").build()));

        // Tag keyword (single edit)
        predicate = new PersonContainsFuzzyKeywordsPredicate(Collections.singletonList("frend"));
        assertTrue(predicate.test(new PersonBuilder().withTags("friend").build()));

        // Multiple keywords, from same field
        predicate = new PersonContainsFuzzyKeywordsPredicate(Arrays.asList("Alic", "Bbo"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Multiple keywords, from different fields
        predicate = new PersonContainsFuzzyKeywordsPredicate(Arrays.asList("1234", "alice@email.co"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("12345").withEmail("alice@email.com").build()));

        // Only one matching keyword
        predicate = new PersonContainsFuzzyKeywordsPredicate(Arrays.asList("Bbo", "Carol"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Mixed-case keywords
        predicate = new PersonContainsFuzzyKeywordsPredicate(Arrays.asList("aLIc", "bOB"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_fuzzyKeywordsDoNotMatchAnyField_returnsFalse() {
        // Zero keywords
        PersonContainsFuzzyKeywordsPredicate predicate =
                new PersonContainsFuzzyKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Non-matching keyword beyond threshold
        predicate = new PersonContainsFuzzyKeywordsPredicate(Collections.singletonList("Zachary"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Keyword does not match any field within threshold
        predicate = new PersonContainsFuzzyKeywordsPredicate(Arrays.asList("Nope", "StillNo"));
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
        PersonContainsFuzzyKeywordsPredicate predicate = new PersonContainsFuzzyKeywordsPredicate(keywords);

        String expected = PersonContainsFuzzyKeywordsPredicate.class.getCanonicalName()
                + "{keywords=" + keywords + ", match_type=fuzzy}";
        assertEquals(expected, predicate.toString());
    }
}
