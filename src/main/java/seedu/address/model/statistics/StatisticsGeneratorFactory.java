package seedu.address.model.statistics;

import static java.util.Objects.requireNonNull;

/**
 * Creates statistics generators for the requested category.
 */
public final class StatisticsGeneratorFactory {
    private static final int DEFAULT_BAR_WIDTH = 20;

    private final AsciiBarChart barChart;

    /**
     * Creates a factory with a default bar chart configuration.
     */
    public StatisticsGeneratorFactory() {
        this(new AsciiBarChart(DEFAULT_BAR_WIDTH));
    }

    /**
     * Creates a factory that uses the provided bar chart.
     *
     * @param barChart Bar chart used by generated statistics.
     */
    public StatisticsGeneratorFactory(AsciiBarChart barChart) {
        this.barChart = requireNonNull(barChart);
    }

    /**
     * Returns a generator for the given statistics category.
     */
    public StatisticsGenerator create(StatisticsCategory category) {
        requireNonNull(category);
        // Indented case blocks in lambda-style switch statements are allowed
        // CHECKSTYLE.OFF: Indentation
        return switch (category) {
            case ROLE -> new RoleStatisticsGenerator(barChart);
            case RECORD -> new RecordStatisticsGenerator(barChart);
        };
        // CHECKSTYLE.ON: Indentation
    }
}
