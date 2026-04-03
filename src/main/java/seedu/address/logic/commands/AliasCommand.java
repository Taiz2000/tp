package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.PersonListView;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.Model;

/**
 * Creates a command alias.
 */
public class AliasCommand extends Command {

    public static final String COMMAND_WORD = "alias";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Creates a command alias.\n"
            + "Parameters: SHORT COMMAND_WORD\n"
            + "Example: " + COMMAND_WORD + " ls list";
    public static final String MESSAGE_SUCCESS = "Alias created: %1$s -> %2$s";
    public static final String MESSAGE_DUPLICATE_ALIAS =
            "This alias already exists. Remove it first with unalias.";
    public static final String MESSAGE_INVALID_ALIAS_NAME =
            "Alias names should start with a lowercase letter and contain only lowercase letters, digits, or hyphens.";
    public static final String MESSAGE_RESERVED_ALIAS_NAME = "Alias name cannot be an existing command word.";
    public static final String MESSAGE_INVALID_ALIAS_TEMPLATE =
            "Alias target must be exactly one existing command word.";
    public static final String MESSAGE_RESERVED_ALIAS_TARGET =
            "Alias target cannot be alias, aliases, unalias, or editprev.";
    private static final String ALIAS_NAME_VALIDATION_REGEX = "[a-z][a-z0-9-]*";

    private final String shortName;
    private final String targetCommandWord;

    /**
     * Creates an AliasCommand.
     */
    public AliasCommand(String shortName, String targetCommandWord) {
        requireNonNull(shortName);
        requireNonNull(targetCommandWord);
        if (!isValidAliasName(shortName)) {
            throw new IllegalArgumentException(MESSAGE_INVALID_ALIAS_NAME);
        }
        if (CommandWords.isBuiltInCommandWord(shortName)) {
            throw new IllegalArgumentException(MESSAGE_RESERVED_ALIAS_NAME);
        }
        if (!isValidAliasTemplate(targetCommandWord)) {
            throw new IllegalArgumentException(MESSAGE_INVALID_ALIAS_TEMPLATE);
        }
        if (!isAllowedAliasTarget(targetCommandWord)) {
            throw new IllegalArgumentException(MESSAGE_RESERVED_ALIAS_TARGET);
        }

        this.shortName = shortName;
        this.targetCommandWord = targetCommandWord.trim();
    }

    /**
     * Returns true if the alias name follows the expected format.
     */
    public static boolean isValidAliasName(String shortName) {
        requireNonNull(shortName);
        return shortName.matches(ALIAS_NAME_VALIDATION_REGEX);
    }

    /**
     * Returns true if the alias target is exactly one existing command word.
     */
    public static boolean isValidAliasTemplate(String template) {
        requireNonNull(template);
        return ParserUtil.parseCommandComponents(template)
                .filter(commandComponents -> commandComponents.getArguments().trim().isEmpty())
                .map(commandComponents -> CommandWords.isBuiltInCommandWord(commandComponents.getCommandWord()))
                .orElse(false);
    }

    /**
     * Returns true if the alias target is allowed.
     */
    public static boolean isAllowedAliasTarget(String template) {
        requireNonNull(template);
        return ParserUtil.parseCommandComponents(template)
                .filter(commandComponents -> commandComponents.getArguments().trim().isEmpty())
                .map(commandComponents -> CommandWords.isAllowedAliasTarget(commandComponents.getCommandWord()))
                .orElse(false);
    }

    @Override
    public CommandResult execute(Model model, PersonListView personListView) throws CommandException {
        requireNonNull(model);

        if (model.hasCommandAlias(shortName)) {
            throw new CommandException(MESSAGE_DUPLICATE_ALIAS);
        }

        model.setCommandAlias(shortName, targetCommandWord);
        return new CommandResult(String.format(MESSAGE_SUCCESS, shortName, targetCommandWord));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AliasCommand)) {
            return false;
        }

        AliasCommand otherAliasCommand = (AliasCommand) other;
        return shortName.equals(otherAliasCommand.shortName)
                && targetCommandWord.equals(otherAliasCommand.targetCommandWord);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("shortName", shortName)
                .add("targetCommandWord", targetCommandWord)
                .toString();
    }
}
