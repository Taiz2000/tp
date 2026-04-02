package seedu.address.logic.parser;

import java.util.List;

import seedu.address.model.person.predicates.PersonContainsFieldsPredicate;
import seedu.address.model.person.predicates.PersonContainsFuzzyKeywordsPredicate;
import seedu.address.model.person.predicates.PersonContainsKeywordsPredicate;
import seedu.address.model.person.predicates.PersonContainsSubstringsPredicate;

/**
 * Creates {@code PersonContainsFieldsPredicate} instances for supported find match types.
 */
public final class PersonContainsFieldsPredicateFactory {

    private PersonContainsFieldsPredicateFactory() {
        throw new UnsupportedOperationException("This is a factory class and cannot be instantiated");
    }

    /**
     * Returns a {@code PersonContainsFieldsPredicate} for the given match type and keywords.
     */
    public static PersonContainsFieldsPredicate createPredicate(FindMatchType matchType, List<String> keywords) {
        // Indented case blocks in lambda-style switch statements are allowed
        // CHECKSTYLE.OFF: Indentation
        return switch (matchType) {
            case KEYWORD -> new PersonContainsKeywordsPredicate(keywords);
            case SUBSTRING -> new PersonContainsSubstringsPredicate(keywords);
            case FUZZY -> new PersonContainsFuzzyKeywordsPredicate(keywords);
        };
        // CHECKSTYLE.ON: Indentation
    }
}
