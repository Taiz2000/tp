package seedu.address.logic.parser;

import java.util.List;

import seedu.address.model.person.predicates.PersonContainsFieldsPredicate;
import seedu.address.model.person.predicates.PersonContainsFuzzyKeywordsPredicate;
import seedu.address.model.person.predicates.PersonContainsKeywordsPredicate;
import seedu.address.model.person.predicates.PersonContainsSubstringsPredicate;

/**
 * Creates {@code PersonContainsFieldsPredicate} instances for supported find match types.
 */
public final class FindMatchTypeFactory {

    private FindMatchTypeFactory() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Returns a {@code PersonContainsFieldsPredicate} for the given match type and keywords.
     */
    public static PersonContainsFieldsPredicate createPredicate(FindMatchType matchType, List<String> keywords) {
        if (matchType == FindMatchType.KEYWORD) {
            return new PersonContainsKeywordsPredicate(keywords);
        } else if (matchType == FindMatchType.SUBSTRING) {
            return new PersonContainsSubstringsPredicate(keywords);
        } else if (matchType == FindMatchType.FUZZY) {
            return new PersonContainsFuzzyKeywordsPredicate(keywords);
        }
        throw new IllegalStateException("Unsupported FindMatchType: " + matchType);
    }
}
