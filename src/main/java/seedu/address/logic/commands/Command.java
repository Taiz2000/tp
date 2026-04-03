package seedu.address.logic.commands;

import seedu.address.logic.PersonListView;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {

    public static final String MESSAGE_NOT_VIEWING_KEPT_PERSONS =
            "You must be viewing the working list of kept contacts to perform this command.";
    public static final String MESSAGE_NOT_VIEWING_DELETED_PERSONS =
            "You must be viewing the recycle bin of recently deleted contacts to perform this command.";

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @param personListView List which is viewed by the user before command execution.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract CommandResult execute(Model model, PersonListView personListView) throws CommandException;

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     * @deprecated Use {@link #execute(Model, PersonListView)} instead, which specifies the currently viewed list.
     */
    @Deprecated
    public CommandResult execute(Model model) throws CommandException {
        return execute(model, PersonListView.KEPT_PERSONS);
    }

    /**
     * Checks if the user is currently viewing the kept persons list.
     *
     * @throws CommandException If the user is not viewing the kept persons list.
     */
    protected static void requireViewingKeptPersons(PersonListView personListView) throws CommandException {
        if (personListView != PersonListView.KEPT_PERSONS) {
            throw new CommandException(MESSAGE_NOT_VIEWING_KEPT_PERSONS);
        }
    }

    /**
     * Checks if the user is currently viewing the deleted persons list.
     *
     * @throws CommandException If the user is not viewing the deleted persons list.
     */
    protected static void requireViewingDeletedPersons(PersonListView personListView) throws CommandException {
        if (personListView != PersonListView.DELETED_PERSONS) {
            throw new CommandException(MESSAGE_NOT_VIEWING_DELETED_PERSONS);
        }
    }

}
