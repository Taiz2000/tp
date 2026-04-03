package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.PersonListView;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    private final PersonListView personListView;

    /** Help information should be shown to the user. */
    private final boolean shouldShowHelp;

    /** The application should exit. */
    private final boolean shouldExit;

    /** The command box should be populated with this text. */
    private final String commandTextToPopulate;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, PersonListView personListView, boolean shouldShowHelp,
            boolean shouldExit, String commandTextToPopulate) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.personListView = requireNonNull(personListView);
        this.shouldShowHelp = shouldShowHelp;
        this.shouldExit = shouldExit;
        this.commandTextToPopulate = commandTextToPopulate;
    }

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, PersonListView personListView, boolean shouldShowHelp,
            boolean shouldExit) {
        this(feedbackToUser, personListView, shouldShowHelp, shouldExit, null);
    }

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, PersonListView personListView) {
        this(feedbackToUser, personListView, false, false);
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     *
     * @deprecated Use {@link #CommandResult(String, PersonListView)} instead, which specifies
     *         the personListView of the result.
     */
    @Deprecated
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, PersonListView.KEPT_PERSONS);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public PersonListView getPersonListView() {
        return personListView;
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
                && personListView == otherCommandResult.personListView
                && shouldShowHelp == otherCommandResult.shouldShowHelp
                && shouldExit == otherCommandResult.shouldExit
                && Objects.equals(commandTextToPopulate, otherCommandResult.commandTextToPopulate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, personListView, shouldShowHelp, shouldExit, commandTextToPopulate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("personListView", personListView)
                .add("shouldShowHelp", shouldShowHelp)
                .add("shouldExit", shouldExit)
                .add("commandTextToPopulate", commandTextToPopulate)
                .toString();
    }

}
