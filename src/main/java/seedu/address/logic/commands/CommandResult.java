package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    private final ListToShow listToShow;

    /** Help information should be shown to the user. */
    private final boolean shouldShowHelp;

    /** The application should exit. */
    private final boolean shouldExit;

    /** The command box should be populated with this text. */
    private final String commandTextToPopulate;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, ListToShow listToShow, boolean shouldShowHelp, boolean shouldExit,
            String commandTextToPopulate) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.listToShow = requireNonNull(listToShow);
        this.shouldShowHelp = shouldShowHelp;
        this.shouldExit = shouldExit;
        this.commandTextToPopulate = commandTextToPopulate;
    }

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, ListToShow listToShow, boolean shouldShowHelp, boolean shouldExit) {
        this(feedbackToUser, listToShow, shouldShowHelp, shouldExit, null);
    }

    public CommandResult(String feedbackToUser, ListToShow listToShow) {
        this(feedbackToUser, listToShow, false, false);
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, ListToShow.SAME_AS_PREVIOUS);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public ListToShow getListToShow() {
        return listToShow;
    }

    public boolean shouldShowHelp() {
        return shouldShowHelp;
    }

    public boolean shouldExit() {
        return shouldExit;
    }

    public Optional<String> getCommandTextToPopulate() {
        return Optional.ofNullable(commandTextToPopulate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && listToShow == otherCommandResult.listToShow
                && shouldShowHelp == otherCommandResult.shouldShowHelp
                && shouldExit == otherCommandResult.shouldExit
                && Objects.equals(commandTextToPopulate, otherCommandResult.commandTextToPopulate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, listToShow, shouldShowHelp, shouldExit, commandTextToPopulate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("listToShow", listToShow)
                .add("shouldShowHelp", shouldShowHelp)
                .add("shouldExit", shouldExit)
                .add("commandTextToPopulate", commandTextToPopulate)
                .toString();
    }

}
