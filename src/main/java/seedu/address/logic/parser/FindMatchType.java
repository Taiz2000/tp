package seedu.address.logic.parser;

import java.util.Arrays;
import java.util.Optional;

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
     * Returns the {@code FindMatchType} that corresponds to the given {@code token}, if any.
     */
    public static Optional<FindMatchType> fromToken(String token) {
        return Arrays.stream(FindMatchType.values())
                .filter(matchType -> matchType.token.equals(token))
                .findFirst();
    }
}
