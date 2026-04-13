package seedu.address.model.person.sort;

import java.util.Arrays;
import java.util.Optional;

/**
 * Represents sortable person attributes supported by list command.
 */
public enum SortAttribute {
    NAME("name"),
    PHONE("phone"),
    EMAIL("email"),
    ADDRESS("address"),
    ROLE("role"),
    TAG("tag"),
    VR("vr");

    private final String token;

    SortAttribute(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    /**
     * Parses the given {@code token} into a {@code SortAttribute} case insensitively, if any.
     */
    public static Optional<SortAttribute> fromToken(String token) {
        return Arrays.stream(values())
                .filter(attribute -> attribute.token.equalsIgnoreCase(token))
                .findFirst();
    }
}
