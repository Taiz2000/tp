package seedu.address.model.person.predicate;

import java.util.List;

import seedu.address.commons.util.StringUtil;

/**
 * Tests that a {@code Person}'s fields contain any of the keywords given.
 * Keyword matching is case-insensitive and matches full words only.
 */
public class PersonContainsKeywordsPredicate extends PersonContainsFieldsPredicate {

    public PersonContainsKeywordsPredicate(List<String> keywords) {
        super(keywords);
    }

    @Override
    public boolean matchesField(String field, String keyword) {
        return StringUtil.containsWordIgnoreCase(field, keyword);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonContainsKeywordsPredicate)) {
            return false;
        }

        return super.equals(other);
    }

    @Override
    public String toString() {
        String base = super.toString();
        return base.substring(0, base.length() - 1) + ", match_type=keywords}";
    }
}
