package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s fields match any of the keywords given.
 */
public class PersonContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public PersonContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        return keywords.stream()
                .anyMatch(keyword -> matchesAnyField(person, keyword));
    }

    private boolean matchesAnyField(Person person, String keyword) {
        if (StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword)) {
            return true;
        }
        if (StringUtil.containsWordIgnoreCase(person.getPhone().value, keyword)) {
            return true;
        }
        if (StringUtil.containsWordIgnoreCase(person.getEmail().value, keyword)) {
            return true;
        }
        if (StringUtil.containsWordIgnoreCase(person.getAddress().value, keyword)) {
            return true;
        }
        if (StringUtil.containsWordIgnoreCase(person.getRole().value, keyword)) {
            return true;
        }
        if (StringUtil.containsWordIgnoreCase(person.getNotes().value, keyword)) {
            return true;
        }
        return person.getTags().stream()
                .anyMatch(tag -> StringUtil.containsWordIgnoreCase(tag.tagName, keyword));
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

        PersonContainsKeywordsPredicate otherPredicate = (PersonContainsKeywordsPredicate) other;
        return keywords.equals(otherPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
