package seedu.address.model.person.predicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonContainsFieldsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        PersonContainsFieldsPredicate firstPredicate =
                new ExactMatchPredicate(firstPredicateKeywordList);
        PersonContainsFieldsPredicate secondPredicate =
                new ExactMatchPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PersonContainsFieldsPredicate firstPredicateCopy =
                new ExactMatchPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // same keywords, different subclass -> returns true
        PersonContainsFieldsPredicate differentSubclassSameKeywords =
                new PrefixMatchPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(differentSubclassSameKeywords));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different keywords -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_keywordsMatchFields_returnsTrue() {
        // One keyword per field
        PersonContainsFieldsPredicate predicate =
                new ExactMatchPredicate(Collections.singletonList("Alice Bob"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        predicate = new ExactMatchPredicate(Collections.singletonList("12345"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("12345").build()));

        predicate = new ExactMatchPredicate(Collections.singletonList("alice@email.com"));
        assertTrue(predicate.test(new PersonBuilder().withEmail("alice@email.com").build()));

        predicate = new ExactMatchPredicate(Collections.singletonList("Main Street"));
        assertTrue(predicate.test(new PersonBuilder().withAddress("Main Street").build()));

        predicate = new ExactMatchPredicate(Collections.singletonList("Developer"));
        assertTrue(predicate.test(new PersonBuilder().withRole("Developer").build()));

        predicate = new ExactMatchPredicate(Collections.singletonList("likes golf"));
        assertTrue(predicate.test(new PersonBuilder().withNotes("likes golf").build()));

        predicate = new ExactMatchPredicate(Collections.singletonList("friend"));
        assertTrue(predicate.test(new PersonBuilder().withTags("friend").build()));

        // Multiple keywords, one matches
        predicate = new ExactMatchPredicate(Arrays.asList("Nope", "Alice Bob"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_keywordsDoNotMatchAnyField_returnsFalse() {
        // Zero keywords
        PersonContainsFieldsPredicate predicate = new ExactMatchPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Non-matching keyword
        predicate = new ExactMatchPredicate(Collections.singletonList("Carol"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Keywords do not match any field
        predicate = new ExactMatchPredicate(Arrays.asList("Nope", "StillNo"));
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
        PersonContainsFieldsPredicate predicate = new ExactMatchPredicate(keywords);

        String expected = ExactMatchPredicate.class.getCanonicalName()
                + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }

    private static class ExactMatchPredicate extends PersonContainsFieldsPredicate {
        ExactMatchPredicate(List<String> keywords) {
            super(keywords);
        }

        @Override
        protected boolean matchesField(String field, String keyword) {
            return field.equals(keyword);
        }
    }

    private static class PrefixMatchPredicate extends PersonContainsFieldsPredicate {
        PrefixMatchPredicate(List<String> keywords) {
            super(keywords);
        }

        @Override
        protected boolean matchesField(String field, String keyword) {
            return field.startsWith(keyword);
        }
    }
}
