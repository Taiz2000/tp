package seedu.address.logic.parser;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import seedu.address.model.person.predicates.PersonContainsFuzzyKeywordsPredicate;
import seedu.address.model.person.predicates.PersonContainsKeywordsPredicate;
import seedu.address.model.person.predicates.PersonContainsSubstringsPredicate;
import seedu.address.model.person.predicates.PersonPredicate;

/**
 * Supported match types for the find command.
 */
public enum FindMatchType {
    KEYWORD("kw"),
    SUBSTRING("ss"),
    FUZZY("fz");

    public static final String KEYWORD_TOKEN = KEYWORD.token;
    public static final String SUBSTRING_TOKEN = SUBSTRING.token;
    public static final String FUZZY_TOKEN = FUZZY.token;

    private final String token;
    FindMatchType(String token) {
        this.token = token;
    }

    /**
     * Returns a {@code PersonPredicate} for this match type and the given keywords.
     */
    public PersonPredicate createPredicate(List<String> keywords) {
        if (this == KEYWORD) {
            return new PersonContainsKeywordsPredicate(keywords);
        } else if (this == SUBSTRING) {
            return new PersonContainsSubstringsPredicate(keywords);
        } else if (this == FUZZY) {
            return new PersonContainsFuzzyKeywordsPredicate(keywords);
        }
        throw new IllegalStateException("Unsupported FindMatchType: " + this);
    }

    /**
     * Returns the {@code FindMatchType} that corresponds to the given {@code token}, if any.
     */
    public static Optional<FindMatchType> fromToken(String token) {
        return Arrays.stream(FindMatchType.values())
                .filter(matchType -> matchType.token.equals(token))
                .findFirst();
    }
}
