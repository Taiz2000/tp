package seedu.address.logic.commands;

import java.util.Set;

/**
 * Utility methods for built-in command words.
 * Keep this list in sync with {@code AddressBookParser} when adding new top-level commands so
 * those commands remain valid alias targets.
 */
public final class CommandWords {

    public static final Set<String> BUILT_IN_COMMAND_WORDS = Set.of(
            AddCommand.COMMAND_WORD,
            AliasCommand.COMMAND_WORD,
            AliasesCommand.COMMAND_WORD,
            ClearCommand.COMMAND_WORD,
            DeleteCommand.COMMAND_WORD,
            EditCommand.COMMAND_WORD,
            ExitCommand.COMMAND_WORD,
            FindCommand.COMMAND_WORD,
            HelpCommand.COMMAND_WORD,
            ListCommand.COMMAND_WORD,
            UnaliasCommand.COMMAND_WORD);

    private CommandWords() {}

    /**
     * Returns true if the given command word is a built-in command.
     */
    public static boolean isBuiltInCommandWord(String commandWord) {
        return BUILT_IN_COMMAND_WORDS.contains(commandWord);
    }
}
