package seedu.address.model.person.sort;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.stream.Collectors;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * Comparator for sorting {@link Person} by a specified attribute and order.
 */
public class PersonSortComparator implements Comparator<Person> {
    private static final Comparator<String> STRING_COMPARATOR = String.CASE_INSENSITIVE_ORDER;

    private final SortAttribute attribute;
    private final SortOrder order;

    /**
     * Creates a comparator that sorts by {@code attribute} in {@code order}.
     */
    public PersonSortComparator(SortAttribute attribute, SortOrder order) {
        this.attribute = requireNonNull(attribute);
        this.order = requireNonNull(order);
    }

    @Override
    public int compare(Person first, Person second) {
        int result;
        switch (attribute) {
        case NAME:
            result = STRING_COMPARATOR.compare(first.getName().fullName, second.getName().fullName);
            break;
        case PHONE:
            result = STRING_COMPARATOR.compare(first.getPhone().value, second.getPhone().value);
            break;
        case EMAIL:
            result = STRING_COMPARATOR.compare(first.getEmail().value, second.getEmail().value);
            break;
        case ROLE:
            result = STRING_COMPARATOR.compare(first.getRole().value, second.getRole().value);
            break;
        case TAG:
            result = STRING_COMPARATOR.compare(getTagKey(first), getTagKey(second));
            break;
        default:
            throw new UnsupportedOperationException("Unsupported sort attribute: " + attribute);
        }

        return order == SortOrder.DESC ? -result : result;
    }

    private static String getTagKey(Person person) {
        return person.getTags().stream()
                .map(tag -> tag.tagName)
                .sorted(STRING_COMPARATOR)
                .collect(Collectors.joining(","));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PersonSortComparator)) {
            return false;
        }

        PersonSortComparator otherComparator = (PersonSortComparator) other;
        return attribute == otherComparator.attribute && order == otherComparator.order;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("attribute", attribute)
                .add("order", order)
                .toString();
    }
}
