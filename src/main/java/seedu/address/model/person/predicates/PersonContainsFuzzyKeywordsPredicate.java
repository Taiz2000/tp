package seedu.address.model.person.predicates;

import java.util.List;

import seedu.address.commons.util.StringUtil;

/**
 * Tests that a {@code Person}'s fields contain any of the keywords given,
 * using fuzzy matching based on Levenshtein distance.
 */
public class PersonContainsFuzzyKeywordsPredicate extends PersonContainsFieldsPredicate {
    private static final int MAX_DISTANCE_THRESHOLD = 2;

    public PersonContainsFuzzyKeywordsPredicate(List<String> keywords) {
        super(keywords);
    }

    @Override
    public boolean matchesField(String field, String keyword) {
        return StringUtil.containsFuzzyWordIgnoreCase(field, keyword, MAX_DISTANCE_THRESHOLD);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonContainsFuzzyKeywordsPredicate)) {
            return false;
        }

        return super.equals(other);
    }

    @Override
    public String toString() {
        String base = super.toString();
        return base.substring(0, base.length() - 1) + ", match_type=fuzzy}";
    }
}
