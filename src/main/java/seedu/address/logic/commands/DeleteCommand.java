package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes persons identified using their displayed indices from the address book.
 *
 * Assumption: Indices are provided in increasing order without duplicates.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the persons identified by the index numbers used in the displayed person list.\n"
            + "Parameters: INDEX [MORE_INDICES] (must be positive integers)\n"
            + "Example: " + COMMAND_WORD + " 1 2";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS_PREFIX = "Deleted person(s):";
    public static final String MESSAGE_DELETE_PERSON_SUCCESS_DELIMITER = "\n";

    private final List<Index> targetIndices;

    public DeleteCommand(List<Index> targetIndices) {
        this.targetIndices = List.copyOf(targetIndices); //defensive copy to ensure immutability
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredKeptPersonList();
        requireIndicesInRange(model);
        List<Person> personsToDelete = new ArrayList<>();

        for (Index index : targetIndices) {
            Person person = lastShownList.get(index.getZeroBased());
            personsToDelete.add(person);
        }

        for (Person person : personsToDelete) {
            model.deletePerson(person);
        }

        return new CommandResult(buildSuccessMessage(personsToDelete));
    }

    private void requireIndicesInRange(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredKeptPersonList();
        for (Index targetIndex : targetIndices) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndices.equals(otherDeleteCommand.targetIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndices", targetIndices)
                .toString();
    }

    /**
     * Builds a success message for the DeleteCommand execution, listing all deleted persons.
     *
     * @param deletedPersons The list of persons that were deleted, in the order they were displayed in the list.
     * @return A formatted success message listing all deleted persons.
     */
    public static String buildSuccessMessage(List<Person> deletedPersons) {
        StringBuilder sb = new StringBuilder(MESSAGE_DELETE_PERSON_SUCCESS_PREFIX);
        for (Person person : deletedPersons) {
            sb.append(MESSAGE_DELETE_PERSON_SUCCESS_DELIMITER).append(Messages.format(person));
        }
        return sb.toString();
    }
}
