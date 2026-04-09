package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AVAILABILITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MATCH_TYPE;
import static seedu.address.logic.parser.FindMatchType.FUZZY_TOKEN;
import static seedu.address.logic.parser.FindMatchType.KEYWORD_TOKEN;
import static seedu.address.logic.parser.FindMatchType.SUBSTRING_TOKEN;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.PersonListView;
import seedu.address.model.Model;
import seedu.address.model.person.predicates.PersonPredicate;

/**
 * Finds and lists all persons in address book whose fields contain any of the argument keywords.
 * Matching is case-insensitive. Fields include name, phone, email, address, role, notes, and tags.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose fields contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Searches across name, phone, email, address, role, notes, and tags.\n"
            + "Optionally filter by volunteer availability with " + PREFIX_AVAILABILITY + "DAY,HH:mm,HH:mm.\n"
            + "Parameters: [" + PREFIX_MATCH_TYPE + "MATCH_TYPE] "
            + "[" + PREFIX_AVAILABILITY + "DAY,HH:mm,HH:mm] [KEYWORD ...]\n"
            + "Currently supported MATCH_TYPE: " + KEYWORD_TOKEN + ", " + SUBSTRING_TOKEN + ", " + FUZZY_TOKEN + "\n"
            + "If " + PREFIX_MATCH_TYPE + " is specified, at least one KEYWORD must also be provided.\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_MATCH_TYPE + KEYWORD_TOKEN + " alice bob charlie\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_MATCH_TYPE + SUBSTRING_TOKEN + " ali\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_MATCH_TYPE + FUZZY_TOKEN + " meyr\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_AVAILABILITY + "MONDAY,14:00,17:00\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_AVAILABILITY + "MONDAY,14:00,17:00 alice";

    private final PersonPredicate predicate;

    public FindCommand(PersonPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model, PersonListView personListView) {
        requireNonNull(model);
        requireNonNull(personListView);

        switch (personListView) {
        case KEPT_PERSONS:
            model.updateFilteredKeptPersonList(predicate);
            int numberOfKeptPersons = model.getFilteredKeptPersonList().size();
            return new CommandResult(
                    String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, numberOfKeptPersons),
                    PersonListView.KEPT_PERSONS);
        case DELETED_PERSONS:
            model.updateFilteredDeletedPersonList(predicate);
            int numberOfDeletedPersons = model.getFilteredDeletedPersonList().size();
            return new CommandResult(
                    String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, numberOfDeletedPersons),
                    PersonListView.DELETED_PERSONS);
        default:
            throw new IllegalArgumentException("Unexpected PersonListView: " + personListView);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
