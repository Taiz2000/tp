package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

public class StringUtilTest {

    //---------------- Tests for isNonZeroUnsignedInteger --------------------------------------

    @Test
    public void isNonZeroUnsignedInteger() {

        // EP: empty strings
        assertFalse(StringUtil.isNonZeroUnsignedInteger("")); // Boundary value
        assertFalse(StringUtil.isNonZeroUnsignedInteger("  "));

        // EP: not a number
        assertFalse(StringUtil.isNonZeroUnsignedInteger("a"));
        assertFalse(StringUtil.isNonZeroUnsignedInteger("aaa"));

        // EP: zero
        assertFalse(StringUtil.isNonZeroUnsignedInteger("0"));

        // EP: zero as prefix
        assertTrue(StringUtil.isNonZeroUnsignedInteger("01"));

        // EP: signed numbers
        assertFalse(StringUtil.isNonZeroUnsignedInteger("-1"));
        assertFalse(StringUtil.isNonZeroUnsignedInteger("+1"));

        // EP: numbers with white space
        assertFalse(StringUtil.isNonZeroUnsignedInteger(" 10 ")); // Leading/trailing spaces
        assertFalse(StringUtil.isNonZeroUnsignedInteger("1 0")); // Spaces in the middle

        // EP: number larger than Integer.MAX_VALUE
        assertFalse(StringUtil.isNonZeroUnsignedInteger(Long.toString(Integer.MAX_VALUE + 1)));

        // EP: valid numbers, should return true
        assertTrue(StringUtil.isNonZeroUnsignedInteger("1")); // Boundary value
        assertTrue(StringUtil.isNonZeroUnsignedInteger("10"));
    }


    //---------------- Tests for containsWordIgnoreCase --------------------------------------

    /*
     * Invalid equivalence partitions for word: null, empty, multiple words
     * Invalid equivalence partitions for sentence: null
     * The four test cases below test one invalid input at a time.
     */

    @Test
    public void containsWordIgnoreCase_nullWord_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.containsWordIgnoreCase("typical sentence", null));
    }

    @Test
    public void containsWordIgnoreCase_emptyWord_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, "Word parameter cannot be empty", ()
                -> StringUtil.containsWordIgnoreCase("typical sentence", "  "));
    }

    @Test
    public void containsWordIgnoreCase_multipleWords_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, "Word parameter should be a single word", ()
                -> StringUtil.containsWordIgnoreCase("typical sentence", "aaa BBB"));
    }

    @Test
    public void containsWordIgnoreCase_nullSentence_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.containsWordIgnoreCase(null, "abc"));
    }

    /*
     * Valid equivalence partitions for word:
     *   - any word
     *   - word containing symbols/numbers
     *   - word with leading/trailing spaces
     *
     * Valid equivalence partitions for sentence:
     *   - empty string
     *   - one word
     *   - multiple words
     *   - sentence with extra spaces
     *
     * Possible scenarios returning true:
     *   - matches first word in sentence
     *   - last word in sentence
     *   - middle word in sentence
     *   - matches multiple words
     *
     * Possible scenarios returning false:
     *   - query word matches part of a sentence word
     *   - sentence word matches part of the query word
     *
     * The test method below tries to verify all above with a reasonably low number of test cases.
     */

    @Test
    public void containsWordIgnoreCase_validInputs_correctResult() {

        // Empty sentence
        assertFalse(StringUtil.containsWordIgnoreCase("", "abc")); // Boundary case
        assertFalse(StringUtil.containsWordIgnoreCase("    ", "123"));

        // Matches a partial word only
        assertFalse(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "bb")); // Sentence word bigger than query word
        assertFalse(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "bbbb")); // Query word bigger than sentence word

        // Matches word in the sentence, different upper/lower case letters
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bBb ccc", "Bbb")); // First word (boundary case)
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bBb ccc@1", "CCc@1")); // Last word (boundary case)
        assertTrue(StringUtil.containsWordIgnoreCase("  AAA   bBb   ccc  ", "aaa")); // Sentence has extra spaces
        assertTrue(StringUtil.containsWordIgnoreCase("Aaa", "aaa")); // Only one word in sentence (boundary case)
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "  ccc  ")); // Leading/trailing spaces

        // Matches multiple words in sentence
        assertTrue(StringUtil.containsWordIgnoreCase("AAA bBb ccc  bbb", "bbB"));
    }

    @Test
    public void containsWordIgnoreCase_examplesFromJavadoc_correctResult() {
        assertTrue(StringUtil.containsWordIgnoreCase("ABc def", "abc"));
        assertTrue(StringUtil.containsWordIgnoreCase("ABc def", "DEF"));
        assertFalse(StringUtil.containsWordIgnoreCase("ABc def", "AB")); // not a full word match
    }


    //---------------- Tests for containsSubstringIgnoreCase --------------------------------------

    @Test
    public void containsSubstringIgnoreCase_nullSubstring_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.containsSubstringIgnoreCase("typical sentence",
                null));
    }

    @Test
    public void containsSubstringIgnoreCase_emptySubstring_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, "Substring parameter cannot be empty", ()
                -> StringUtil.containsSubstringIgnoreCase("typical sentence", "  "));
    }

    @Test
    public void containsSubstringIgnoreCase_multipleWords_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, "Substring parameter should be a single word", ()
                -> StringUtil.containsSubstringIgnoreCase("typical sentence", "aaa BBB"));
    }

    @Test
    public void containsSubstringIgnoreCase_nullSentence_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.containsSubstringIgnoreCase(null, "abc"));
    }

    @Test
    public void containsSubstringIgnoreCase_validInputs_correctResult() {
        // Empty sentence
        assertFalse(StringUtil.containsSubstringIgnoreCase("", "abc"));
        assertFalse(StringUtil.containsSubstringIgnoreCase("    ", "123"));

        // No matching substring
        assertFalse(StringUtil.containsSubstringIgnoreCase("aaa bbb ccc", "ddd"));

        // Matches substrings within words
        assertTrue(StringUtil.containsSubstringIgnoreCase("ABc def", "abc"));
        assertTrue(StringUtil.containsSubstringIgnoreCase("ABc def", "B"));
        assertTrue(StringUtil.containsSubstringIgnoreCase("  AAA   bBb   ccc  ", "bbb"));
        assertTrue(StringUtil.containsSubstringIgnoreCase("Aaa", "aa"));
        assertTrue(StringUtil.containsSubstringIgnoreCase("aaa bbb ccc", "  cC  "));
    }

    @Test
    public void containsSubstringIgnoreCase_examplesFromJavadoc_correctResult() {
        assertTrue(StringUtil.containsSubstringIgnoreCase("ABc def", "abc"));
        assertTrue(StringUtil.containsSubstringIgnoreCase("ABc def", "B"));
        assertFalse(StringUtil.containsSubstringIgnoreCase("ABc def", "ghi"));
    }


    //---------------- Tests for containsFuzzyWordIgnoreCase --------------------------------------

    @Test
    public void containsFuzzyWordIgnoreCase_nullWord_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.containsFuzzyWordIgnoreCase("typical sentence",
                null, 1));
    }

    @Test
    public void containsFuzzyWordIgnoreCase_emptyWord_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, "Word parameter cannot be empty", ()
                -> StringUtil.containsFuzzyWordIgnoreCase("typical sentence", "  ", 1));
    }

    @Test
    public void containsFuzzyWordIgnoreCase_multipleWords_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, "Word parameter should be a single word", ()
                -> StringUtil.containsFuzzyWordIgnoreCase("typical sentence", "aaa BBB", 1));
    }

    @Test
    public void containsFuzzyWordIgnoreCase_nullSentence_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.containsFuzzyWordIgnoreCase(null, "abc", 1));
    }

    @Test
    public void containsFuzzyWordIgnoreCase_negativeThreshold_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, "Threshold must be non-negative", ()
                -> StringUtil.containsFuzzyWordIgnoreCase("abc", "abc", -1));
    }

    @Test
    public void containsFuzzyWordIgnoreCase_validInputs_correctResult() {
        // Empty sentence
        assertFalse(StringUtil.containsFuzzyWordIgnoreCase("", "abc", 1));
        assertFalse(StringUtil.containsFuzzyWordIgnoreCase("    ", "abc", 1));

        // No matching word within threshold
        assertFalse(StringUtil.containsFuzzyWordIgnoreCase("aaa bbb ccc", "ddd", 1));

        // Exact match with zero threshold
        assertTrue(StringUtil.containsFuzzyWordIgnoreCase("Main Street", "Street", 0));
        assertTrue(StringUtil.containsFuzzyWordIgnoreCase("Alice Bob", "bOb", 0));

        // Matches within threshold (single edit)
        assertTrue(StringUtil.containsFuzzyWordIgnoreCase("Alice Bob", "Alic", 1));
        assertTrue(StringUtil.containsFuzzyWordIgnoreCase("Main Street", "Stret", 1));

        // Matches within threshold (single insertion)
        assertTrue(StringUtil.containsFuzzyWordIgnoreCase("Alice Bob", "Alicee", 1));

        // Matches within threshold (single deletion)
        assertTrue(StringUtil.containsFuzzyWordIgnoreCase("Alice Bob", "Aice", 1));

        // Multiple edits required (kitten -> sitting)
        assertFalse(StringUtil.containsFuzzyWordIgnoreCase("kitten", "sitting", 2));
        assertTrue(StringUtil.containsFuzzyWordIgnoreCase("kitten", "sitting", 3));
    }

    @Test
    public void containsFuzzyWordIgnoreCase_examplesFromJavadoc_correctResult() {
        assertTrue(StringUtil.containsFuzzyWordIgnoreCase("ABc def", "abd", 1));
        assertFalse(StringUtil.containsFuzzyWordIgnoreCase("ABc def", "ace", 1));
        assertTrue(StringUtil.containsFuzzyWordIgnoreCase("ABc def", "DEF", 0));
    }


    //---------------- Tests for getDetails --------------------------------------

    /*
     * Equivalence Partitions: null, valid throwable object
     */

    @Test
    public void getDetails_exceptionGiven() {
        assertTrue(StringUtil.getDetails(new FileNotFoundException("file not found"))
                .contains("java.io.FileNotFoundException: file not found"));
    }

    @Test
    public void getDetails_nullGiven_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.getDetails(null));
    }

}
