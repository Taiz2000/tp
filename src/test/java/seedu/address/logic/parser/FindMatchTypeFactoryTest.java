package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.predicates.PersonContainsFieldsPredicate;
import seedu.address.model.person.predicates.PersonContainsFuzzyKeywordsPredicate;
import seedu.address.model.person.predicates.PersonContainsKeywordsPredicate;
import seedu.address.model.person.predicates.PersonContainsSubstringsPredicate;

public class FindMatchTypeFactoryTest {
    @Test
    public void createPredicate_keywordMatchType_returnsKeywordPredicate() {
        PersonContainsFieldsPredicate predicate =
                FindMatchTypeFactory.createPredicate(FindMatchType.KEYWORD, List.of("alice"));

        assertTrue(PersonContainsKeywordsPredicate.class.isInstance(predicate));
    }

    @Test
    public void createPredicate_substringMatchType_returnsSubstringPredicate() {
        PersonContainsFieldsPredicate predicate =
                FindMatchTypeFactory.createPredicate(FindMatchType.SUBSTRING, List.of("alice"));

        assertTrue(PersonContainsSubstringsPredicate.class.isInstance(predicate));
    }

    @Test
    public void createPredicate_fuzzyMatchType_returnsFuzzyPredicate() {
        PersonContainsFieldsPredicate predicate =
                FindMatchTypeFactory.createPredicate(FindMatchType.FUZZY, List.of("alice"));

        assertTrue(PersonContainsFuzzyKeywordsPredicate.class.isInstance(predicate));
    }
}
