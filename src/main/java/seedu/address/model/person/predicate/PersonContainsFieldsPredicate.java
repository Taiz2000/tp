package seedu.address.model.person.predicate;

import java.util.List;
import java.util.stream.Stream;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * Tests that a {@code Person}'s fields match any of the keywords given.
 * Fields include name, phone, email, address, role, notes, and tags.
 */
public abstract class PersonContainsFieldsPredicate implements PersonPredicate {
    private final List<String> keywords;

    public PersonContainsFieldsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        return keywords.stream()
                .anyMatch(keyword -> matchesAnyField(person, keyword));
    }

    private boolean matchesAnyField(Person person, String keyword) {
        Stream<String> simpleFields = Stream.of(
                person.getName().fullName,
                person.getPhone().value,
                person.getEmail().value,
                person.getAddress().value,
                person.getRole().value,
                person.getNotes().value
        );
        Stream<String> tagFields = person.getTags().stream()
                .map(tag -> tag.tagName);
        Stream<String> allFields = Stream.concat(simpleFields, tagFields);
        return allFields.anyMatch(field -> matchesField(field, keyword));
    }

    protected abstract boolean matchesField(String field, String keyword);

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonContainsFieldsPredicate)) {
            return false;
        }

        PersonContainsFieldsPredicate otherPredicate = (PersonContainsFieldsPredicate) other;
        return keywords.equals(otherPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", this.keywords).toString();
    }
}
