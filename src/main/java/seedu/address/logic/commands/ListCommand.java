package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.person.sort.PersonSortComparator;
import seedu.address.model.person.sort.SortAttribute;
import seedu.address.model.person.sort.SortOrder;

/**
 * Lists all persons in the address book to the user, optionally sorted by an attribute.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all persons.";
    public static final String MESSAGE_SUCCESS_SORTED = "Listed all persons sorted by %s (%s).";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all persons, optionally sorted by an attribute.\n"
            + "Parameters: list [ATTRIBUTE [asc|desc]]\n"
            + "Currently supported ATTRIBUTE: name, phone, email, role, tag\n"
            + "Example: list name desc";

    private final SortAttribute sortAttribute;
    private final SortOrder sortOrder;

    /**
     * Creates a ListCommand with no sorting.
     */
    public ListCommand() {
        this.sortAttribute = null;
        this.sortOrder = SortOrder.ASC;
    }

    /**
     * Creates a ListCommand that sorts by {@code sortAttribute} in {@code sortOrder}.
     */
    public ListCommand(SortAttribute sortAttribute, SortOrder sortOrder) {
        this.sortAttribute = requireNonNull(sortAttribute);
        this.sortOrder = requireNonNull(sortOrder);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        if (sortAttribute == null) {
            model.updateSortedPersonList(null);
            return new CommandResult(MESSAGE_SUCCESS);
        }

        model.updateSortedPersonList(new PersonSortComparator(sortAttribute, sortOrder));
        return new CommandResult(String.format(MESSAGE_SUCCESS_SORTED,
                sortAttribute.getToken(), sortOrder.getToken()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ListCommand)) {
            return false;
        }

        ListCommand otherCommand = (ListCommand) other;
        return sortAttribute == otherCommand.sortAttribute && sortOrder == otherCommand.sortOrder;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("sortAttribute", sortAttribute)
                .add("sortOrder", sortOrder)
                .toString();
    }
}
