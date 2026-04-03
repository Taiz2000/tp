package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Map;
import java.util.stream.Collectors;

import seedu.address.logic.PersonListView;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Lists all command aliases.
 */
public class AliasesCommand extends Command {

    public static final String COMMAND_WORD = "aliases";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all command aliases.\n"
            + "Example: " + COMMAND_WORD;
    public static final String MESSAGE_NO_ALIASES = "No aliases defined.";
    public static final String MESSAGE_ALIASES_HEADER = "Command aliases:";

    @Override
    public CommandResult execute(Model model, PersonListView personListView) throws CommandException {
        requireNonNull(model);

        Map<String, String> aliases = model.getCommandAliases();
        if (aliases.isEmpty()) {
            return new CommandResult(MESSAGE_NO_ALIASES);
        }

        String aliasList = aliases.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + " -> " + entry.getValue())
                .collect(Collectors.joining("\n"));

        return new CommandResult(MESSAGE_ALIASES_HEADER + "\n" + aliasList);
    }
}
