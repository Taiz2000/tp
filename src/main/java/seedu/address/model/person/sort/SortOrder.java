package seedu.address.model.person.sort;

import java.util.Arrays;
import java.util.Optional;

/**
 * Represents sorting order for list command.
 */
public enum SortOrder {
    ASC("asc"),
    DESC("desc");

    private final String token;

    SortOrder(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    /**
     * Parses the given {@code token} into a {@code SortOrder} case insensitively, if any.
     */
    public static Optional<SortOrder> fromToken(String token) {
        return Arrays.stream(values())
                .filter(order -> order.token.equalsIgnoreCase(token))
                .findFirst();
    }
}
