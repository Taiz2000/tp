package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.predicates.PersonContainsFieldsPredicate;
import seedu.address.model.person.predicates.PersonContainsFuzzyKeywordsPredicate;
import seedu.address.model.person.predicates.PersonContainsKeywordsPredicate;
import seedu.address.model.person.predicates.PersonContainsSubstringsPredicate;

public class FindMatchTypeTest {

    @Test
    public void createPredicate_keywordMatchType_returnsKeywordPredicate() {
        PersonContainsFieldsPredicate predicate = FindMatchType.KEYWORD.createPredicate(List.of("alice"));

        assertTrue(PersonContainsKeywordsPredicate.class.isInstance(predicate));
    }

    @Test
    public void createPredicate_substringMatchType_returnsSubstringPredicate() {
        PersonContainsFieldsPredicate predicate = FindMatchType.SUBSTRING.createPredicate(List.of("alice"));

        assertTrue(PersonContainsSubstringsPredicate.class.isInstance(predicate));
    }

    @Test
    public void createPredicate_fuzzyMatchType_returnsFuzzyPredicate() {
        PersonContainsFieldsPredicate predicate = FindMatchType.FUZZY.createPredicate(List.of("alice"));

        assertTrue(PersonContainsFuzzyKeywordsPredicate.class.isInstance(predicate));
    }
}
