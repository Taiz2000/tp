package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * Helper functions for handling strings.
 */
public class StringUtil {

    /**
     * Returns true if the {@code sentence} contains the {@code word}.
     *   Ignores case, but a full word match is required.
     *   <br>examples:<pre>
     *       containsWordIgnoreCase("ABc def", "abc") == true
     *       containsWordIgnoreCase("ABc def", "DEF") == true
     *       containsWordIgnoreCase("ABc def", "AB") == false //not a full word match
     *       </pre>
     * @param sentence cannot be null
     * @param word cannot be null, cannot be empty, must be a single word
     */
    public static boolean containsWordIgnoreCase(String sentence, String word) {
        requireNonNull(sentence);
        requireNonNull(word);

        String preppedWord = word.trim();
        checkArgument(!preppedWord.isEmpty(), "Word parameter cannot be empty");
        checkArgument(preppedWord.split("\\s+").length == 1, "Word parameter should be a single word");

        String preppedSentence = sentence;
        String[] wordsInPreppedSentence = preppedSentence.split("\\s+");

        return Arrays.stream(wordsInPreppedSentence)
                .anyMatch(preppedWord::equalsIgnoreCase);
    }

    /**
     * Returns true if the {@code sentence} contains the {@code substring}.
     *   Ignores case, and matches substrings within words.
     *   <br>examples:<pre>
     *       containsSubstringIgnoreCase("ABc def", "abc") == true
     *       containsSubstringIgnoreCase("ABc def", "B") == true
     *       containsSubstringIgnoreCase("ABc def", "ghi") == false
     *       </pre>
     * @param sentence cannot be null
     * @param substring cannot be null, cannot be empty, must be a single word
     */
    public static boolean containsSubstringIgnoreCase(String sentence, String substring) {
        requireNonNull(sentence);
        requireNonNull(substring);

        String preppedSubstring = substring.trim();
        checkArgument(!preppedSubstring.isEmpty(), "Substring parameter cannot be empty");
        checkArgument(preppedSubstring.split("\\s+").length == 1, "Substring parameter should be a single word");

        return sentence.toLowerCase().contains(preppedSubstring.toLowerCase());
    }

    /**
     * Returns true if the {@code sentence} contains a {@code word} within the given Levenshtein distance threshold.
     *   Ignores case.
     *   <br>examples:<pre>
     *       containsFuzzyWordIgnoreCase("ABc def", "abd", 1) == true
     *       containsFuzzyWordIgnoreCase("ABc def", "ace", 1) == false
     *       containsFuzzyWordIgnoreCase("ABc def", "DEF", 0) == true
     *       </pre>
     * @param sentence cannot be null
     * @param word cannot be null, cannot be empty, must be a single word
     * @param threshold must be non-negative
     */
    public static boolean containsFuzzyWordIgnoreCase(String sentence, String word, int threshold) {
        requireNonNull(sentence);
        requireNonNull(word);
        checkArgument(threshold >= 0, "Threshold must be non-negative");

        String preppedWord = word.trim();
        checkArgument(!preppedWord.isEmpty(), "Word parameter cannot be empty");
        checkArgument(preppedWord.split("\\s+").length == 1, "Word parameter should be a single word");
        String lowerWord = preppedWord.toLowerCase();

        String preppedSentence = sentence;
        String[] wordsInPreppedSentence = preppedSentence.split("\\s+");

        return Arrays.stream(wordsInPreppedSentence)
                .anyMatch(sentenceWord -> getLevenshteinDistance(sentenceWord.toLowerCase(), lowerWord) <= threshold);
    }

    /**
     * Returns the Levenshtein distance between {@code s} and {@code t}.
     * Based on the algorithm described at
     * <a href="https://en.wikipedia.org/wiki/Levenshtein_distance">
     *     https://en.wikipedia.org/wiki/Levenshtein_distance
     * </a>.
     */
    private static int getLevenshteinDistance(String s, String t) {
        int m = s.length();
        int n = t.length();
        int[] v0 = new int[n + 1];
        int[] v1 = new int[n + 1];

        for (int i = 0; i <= n; i++) {
            v0[i] = i;
        }

        for (int i = 0; i <= m - 1; i++) {
            v1[0] = i + 1;

            for (int j = 0; j <= n - 1; j++) {
                int deletionCost = v0[j + 1] + 1;
                int insertionCost = v1[j] + 1;
                int substitutionCost;
                if (s.charAt(i) == t.charAt(j)) {
                    substitutionCost = v0[j];
                } else {
                    substitutionCost = v0[j] + 1;
                }

                v1[j + 1] = Math.min(deletionCost, Math.min(insertionCost, substitutionCost));
            }

            int[] temp = v0;
            v0 = v1;
            v1 = temp;
        }

        return v0[n];
    }

    /**
     * Returns a detailed message of the t, including the stack trace.
     */
    public static String getDetails(Throwable t) {
        requireNonNull(t);
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return t.getMessage() + "\n" + sw.toString();
    }

    /**
     * Returns true if {@code s} represents a non-zero unsigned integer
     * e.g. 1, 2, 3, ..., {@code Integer.MAX_VALUE} <br>
     * Will return false for any other non-null string input
     * e.g. empty string, "-1", "0", "+1", and " 2 " (untrimmed), "3 0" (contains whitespace), "1 a" (contains letters)
     * @throws NullPointerException if {@code s} is null.
     */
    public static boolean isNonZeroUnsignedInteger(String s) {
        requireNonNull(s);

        try {
            int value = Integer.parseInt(s);
            return value > 0 && !s.startsWith("+"); // "+1" is successfully parsed by Integer#parseInt(String)
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
