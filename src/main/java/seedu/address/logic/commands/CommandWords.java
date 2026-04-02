package seedu.address.logic.commands;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility methods for command words.
 */
public final class CommandWords {

    /**
     * Top-level command words handled by {@code AddressBookParser}.
     * Keep this list in sync with {@code AddressBookParser} when adding new top-level commands.
     */
    public static final Set<String> TOP_LEVEL_COMMAND_WORDS = Set.of(
            AddCommand.COMMAND_WORD,
            AliasCommand.COMMAND_WORD,
            AliasesCommand.COMMAND_WORD,
            BinCommand.COMMAND_WORD,
            ClearCommand.COMMAND_WORD,
            DeleteCommand.COMMAND_WORD,
            EditCommand.COMMAND_WORD,
            EditPreviousCommand.COMMAND_WORD,
            ExitCommand.COMMAND_WORD,
            ExportCommand.COMMAND_WORD,
            FindCommand.COMMAND_WORD,
            HelpCommand.COMMAND_WORD,
            ImportCommand.COMMAND_WORD,
            ListCommand.COMMAND_WORD,
            StatsCommand.COMMAND_WORD,
            UnaliasCommand.COMMAND_WORD);

    private static final Set<String> DISALLOWED_ALIAS_TARGET_COMMAND_WORDS = Set.of(
            AliasCommand.COMMAND_WORD,
            AliasesCommand.COMMAND_WORD,
            EditPreviousCommand.COMMAND_WORD,
            UnaliasCommand.COMMAND_WORD);

    private static final Set<String> ALIAS_TARGET_COMMAND_WORDS;

    static {
        Set<String> aliasTargetCommandWords = new HashSet<>(TOP_LEVEL_COMMAND_WORDS);
        aliasTargetCommandWords.removeAll(DISALLOWED_ALIAS_TARGET_COMMAND_WORDS);
        ALIAS_TARGET_COMMAND_WORDS = Set.copyOf(aliasTargetCommandWords);
    }

    private CommandWords() {}

    /**
     * Returns true if the given command word is reserved by the application.
     */
    public static boolean isBuiltInCommandWord(String commandWord) {
        return TOP_LEVEL_COMMAND_WORDS.contains(commandWord);
    }

    /**
     * Returns true if the given command word can be used as an alias target.
     */
    public static boolean isAllowedAliasTarget(String commandWord) {
        return ALIAS_TARGET_COMMAND_WORDS.contains(commandWord);
    }
}
