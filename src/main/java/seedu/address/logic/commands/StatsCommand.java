package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.statistics.StatisticsCategory.RECORD_TOKEN;
import static seedu.address.model.statistics.StatisticsCategory.ROLE_TOKEN;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.PersonListView;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.statistics.StatisticsCategory;
import seedu.address.model.statistics.StatisticsGenerator;
import seedu.address.model.statistics.StatisticsGeneratorFactory;
import seedu.address.model.statistics.StatisticsReport;

/**
 * Displays volunteer statistics for the requested category.
 */
public class StatsCommand extends Command {

    public static final String COMMAND_WORD = "stats";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Displays volunteer statistics.\n"
            + "Parameters: CATEGORY\n"
            + "Currently supported CATEGORY: " + ROLE_TOKEN + ", " + RECORD_TOKEN + "\n"
            + "Example: " + COMMAND_WORD + " " + ROLE_TOKEN;

    public static final String MESSAGE_UNKNOWN_CATEGORY = "Unknown statistics category: %1$s\n"
            + "Currently supported CATEGORY: " + ROLE_TOKEN + ", " + RECORD_TOKEN;

    public static final String MESSAGE_EXTRA_ARGUMENTS = "Too many arguments provided!\n"
            + MESSAGE_USAGE;

    private final StatisticsCategory category;
    private final StatisticsGeneratorFactory generatorFactory;

    /**
     * Constructs a {@code StatsCommand} with the specified statistics category.
     *
     * @param category Statistics category to display.
     */
    public StatsCommand(StatisticsCategory category) {
        this(category, new StatisticsGeneratorFactory());
    }

    /**
     * Constructs a {@code StatsCommand} with the specified category and generator factory.
     *
     * @param category Statistics category to display.
     * @param generatorFactory Factory for creating statistics generators.
     */
    public StatsCommand(StatisticsCategory category, StatisticsGeneratorFactory generatorFactory) {
        this.category = requireNonNull(category);
        this.generatorFactory = requireNonNull(generatorFactory);
    }

    @Override
    public CommandResult execute(Model model, PersonListView personListView) throws CommandException {
        requireNonNull(model);
        requireNonNull(personListView);
        StatisticsGenerator generator = generatorFactory.create(category);
        StatisticsReport report = generator.generate(model.getAddressBook().getKeptPersonList());
        return new CommandResult(report.render(), PersonListView.KEPT_PERSONS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof StatsCommand)) {
            return false;
        }

        StatsCommand otherStatsCommand = (StatsCommand) other;
        return category == otherStatsCommand.category;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("category", category)
                .toString();
    }
}
