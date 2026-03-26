package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
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
            + "Parameters: SHORT TEMPLATE\n"
            + "Example: " + COMMAND_WORD + " ls list";
    public static final String MESSAGE_SUCCESS = "Alias created: %1$s -> %2$s";
    public static final String MESSAGE_DUPLICATE_ALIAS =
            "This alias already exists. Remove it first with unalias.";
    public static final String MESSAGE_INVALID_ALIAS_NAME =
            "Alias names should start with a lowercase letter and contain only lowercase letters, digits, or hyphens.";
    public static final String MESSAGE_RESERVED_ALIAS_NAME = "Alias name cannot be an existing command word.";
    public static final String MESSAGE_INVALID_ALIAS_TEMPLATE =
            "Alias target's first word must be an existing command word.";
    private static final String ALIAS_NAME_VALIDATION_REGEX = "[a-z][a-z0-9-]*";

    private final String shortName;
    private final String template;

    /**
     * Creates an AliasCommand.
     */
    public AliasCommand(String shortName, String template) {
        requireNonNull(shortName);
        requireNonNull(template);
        if (!isValidAliasName(shortName)) {
            throw new IllegalArgumentException(MESSAGE_INVALID_ALIAS_NAME);
        }
        if (CommandWords.isBuiltInCommandWord(shortName)) {
            throw new IllegalArgumentException(MESSAGE_RESERVED_ALIAS_NAME);
        }
        if (!isValidAliasTemplate(template)) {
            throw new IllegalArgumentException(MESSAGE_INVALID_ALIAS_TEMPLATE);
        }

        this.shortName = shortName;
        this.template = template.trim();
    }

    /**
     * Returns true if the alias name follows the expected format.
     */
    public static boolean isValidAliasName(String shortName) {
        requireNonNull(shortName);
        return shortName.matches(ALIAS_NAME_VALIDATION_REGEX);
    }

    /**
     * Returns true if the alias template begins with a built-in command word.
     */
    public static boolean isValidAliasTemplate(String template) {
        requireNonNull(template);
        return ParserUtil.parseCommandComponents(template)
                .map(commandComponents -> CommandWords.isBuiltInCommandWord(commandComponents.getCommandWord()))
                .orElse(false);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasCommandAlias(shortName)) {
            throw new CommandException(MESSAGE_DUPLICATE_ALIAS);
        }

        model.setCommandAlias(shortName, template);
        return new CommandResult(String.format(MESSAGE_SUCCESS, shortName, template));
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
                && template.equals(otherAliasCommand.template);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("shortName", shortName)
                .add("template", template)
                .toString();
    }
}
