package seedu.address.logic.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import seedu.address.logic.Messages;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Stores mapping of prefixes to their respective arguments.
 * Each key may be associated with multiple argument values.
 * Values for a given key are stored in a list, and the insertion ordering is maintained.
 * Keys are unique, but the list of argument values may contain duplicate argument values, i.e. the same argument value
 * can be inserted multiple times for the same prefix.
 */
public class ArgumentMultimap {

    /**
     * Matches prefix-like tokens: one or two lowercase letters followed by a slash,
     * preceded by whitespace or at the start of the string.
     */
    private static final Pattern PREFIX_PATTERN = Pattern.compile("(?:^|\\s)([a-z]{1,2}/)");

    /** Prefixes mapped to their respective arguments**/
    private final Map<Prefix, List<String>> argMultimap = new HashMap<>();

    /**
     * Associates the specified argument value with {@code prefix} key in this map.
     * If the map previously contained a mapping for the key, the new value is appended to the list of existing values.
     *
     * @param prefix   Prefix key with which the specified argument value is to be associated
     * @param argValue Argument value to be associated with the specified prefix key
     */
    public void put(Prefix prefix, String argValue) {
        List<String> argValues = getAllValues(prefix);
        argValues.add(argValue);
        argMultimap.put(prefix, argValues);
    }

    /**
     * Returns the last value of {@code prefix}.
     */
    public Optional<String> getValue(Prefix prefix) {
        List<String> values = getAllValues(prefix);
        return values.isEmpty() ? Optional.empty() : Optional.of(values.get(values.size() - 1));
    }

    /**
     * Returns all values of {@code prefix}.
     * If the prefix does not exist or has no values, this will return an empty list.
     * Modifying the returned list will not affect the underlying data structure of the ArgumentMultimap.
     */
    public List<String> getAllValues(Prefix prefix) {
        if (!argMultimap.containsKey(prefix)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(argMultimap.get(prefix));
    }

    /**
     * Returns the preamble (text before the first valid prefix). Trims any leading/trailing spaces.
     */
    public String getPreamble() {
        return getValue(new Prefix("")).orElse("");
    }

    /**
     * Throws a {@code ParseException} if any parsed value contains a prefix-like token
     * that was not recognised during tokenization.
     * This catches unknown prefixes (e.g. {@code x/value}) that were silently absorbed
     * into the preceding field's value. Common abbreviations like {@code s/o} and
     * {@code c/o} are excluded from detection.
     *
     * @param knownPrefixList Human-readable list of valid prefixes for the error message
     */
    public void verifyNoUnknownPrefixes(String knownPrefixList) throws ParseException {
        List<String> unknown = new ArrayList<>();

        for (Map.Entry<Prefix, List<String>> entry : argMultimap.entrySet()) {
            for (String value : entry.getValue()) {
                collectUnknownPrefixesFromValue(value, unknown);
            }
        }

        if (!unknown.isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_UNKNOWN_PREFIX,
                    String.join(", ", unknown), knownPrefixList));
        }
    }

    /**
     * Scans a single field value for prefix-like tokens that are not in the known prefix set,
     * adding any new unknown prefixes to the given list.
     */
    private void collectUnknownPrefixesFromValue(String value, List<String> unknown) {
        Matcher matcher = PREFIX_PATTERN.matcher(value);
        while (matcher.find()) {
            String found = matcher.group(1);
            int afterSlash = matcher.end();
            if (isLikelyNotPrefix(value, afterSlash)) {
                continue;
            }
            if (!argMultimap.containsKey(new Prefix(found)) && !unknown.contains(found)) {
                unknown.add(found);
            }
        }
    }

    /**
     * Returns true if the text after a slash is unlikely to be an unknown prefix's value.
     * Filters out common abbreviations (s/o, c/o, f/t) where a single character follows
     * the slash before a word break, and "with" patterns (w/ garden) where a space
     * follows the slash.
     */
    private static boolean isLikelyNotPrefix(String value, int afterSlashPos) {
        // Nothing after slash (e.g., trailing "x/") — could be a prefix with empty value
        if (afterSlashPos >= value.length()) {
            return false;
        }
        // Space immediately after slash (e.g., "w/ garden") — natural text, not a prefix
        if (Character.isWhitespace(value.charAt(afterSlashPos))) {
            return true;
        }
        // Single non-whitespace char then whitespace or end (e.g., "s/o", "c/o", "f/t")
        return afterSlashPos + 1 >= value.length()
                || Character.isWhitespace(value.charAt(afterSlashPos + 1));
    }

    /**
     * Throws a {@code ParseException} if any of the prefixes given in {@code prefixes} appeared more than
     * once among the arguments.
     */
    public void verifyNoDuplicatePrefixesFor(Prefix... prefixes) throws ParseException {
        Prefix[] duplicatedPrefixes = Stream.of(prefixes).distinct()
                .filter(prefix -> argMultimap.containsKey(prefix) && argMultimap.get(prefix).size() > 1)
                .toArray(Prefix[]::new);

        if (duplicatedPrefixes.length > 0) {
            throw new ParseException(Messages.getErrorMessageForDuplicatePrefixes(duplicatedPrefixes));
        }
    }
}
