package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public class FindMatchTypeTest {

    @Test
    public void fromToken_supportedTokens_returnsMatchType() {
        assertEquals(Optional.of(FindMatchType.KEYWORD), FindMatchType.fromToken(FindMatchType.KEYWORD_TOKEN));
        assertEquals(Optional.of(FindMatchType.SUBSTRING), FindMatchType.fromToken(FindMatchType.SUBSTRING_TOKEN));
        assertEquals(Optional.of(FindMatchType.FUZZY), FindMatchType.fromToken(FindMatchType.FUZZY_TOKEN));
    }

    @Test
    public void fromToken_unsupportedToken_returnsEmpty() {
        assertEquals(Optional.empty(), FindMatchType.fromToken("regex"));
    }
}
