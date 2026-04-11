package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AliasCommand;
import seedu.address.logic.commands.AliasesCommand;
import seedu.address.logic.commands.BinCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.CommandWords;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.EditPreviousCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RestoreCommand;
import seedu.address.logic.commands.StatsCommand;
import seedu.address.logic.commands.UnaliasCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.model.person.VolunteerAvailability;
import seedu.address.model.person.predicates.CombinedAndPersonPredicate;
import seedu.address.model.person.predicates.PersonAvailableDuringPredicate;
import seedu.address.model.person.predicates.PersonContainsKeywordsPredicate;
import seedu.address.model.person.sort.SortAttribute;
import seedu.address.model.person.sort.SortOrder;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

/**
 * Reused from Codex suggestions upon providing specifications
 */
public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_alias() throws Exception {
        AliasCommand command = (AliasCommand) parser.parseCommand(AliasCommand.COMMAND_WORD + " ls list");
        assertEquals(new AliasCommand("ls", "list"), command);
    }

    @Test
    public void parseCommand_aliases() throws Exception {
        assertTrue(parser.parseCommand(AliasesCommand.COMMAND_WORD) instanceof AliasesCommand);
        assertTrue(parser.parseCommand(AliasesCommand.COMMAND_WORD + " ignored") instanceof AliasesCommand);
    }

    @Test
    public void parseCommand_bin() throws Exception {
        assertTrue(parser.parseCommand(BinCommand.COMMAND_WORD) instanceof BinCommand);
        assertTrue(parser.parseCommand(BinCommand.COMMAND_WORD + " 3") instanceof BinCommand);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(List.of(INDEX_FIRST_PERSON)), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_export() throws Exception {
        ExportCommand command = (ExportCommand) parser.parseCommand(
                ExportCommand.COMMAND_WORD + " data/volunteers.csv");
        assertEquals(new ExportCommand(Paths.get("data/volunteers.csv")), command);
    }

    @Test
    public void parseCommand_findWithAvailability() throws Exception {
        VolunteerAvailability query = VolunteerAvailability.fromString("MONDAY,14:00,17:00");
        PersonAvailableDuringPredicate predicate = new PersonAvailableDuringPredicate(query);
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " va/MONDAY,14:00,17:00");
        assertEquals(new FindCommand(predicate), command);
    }

    @Test
    public void parseCommand_findWithAvailabilityAndSearchTerms() throws Exception {
        VolunteerAvailability query = VolunteerAvailability.fromString("MONDAY,14:00,17:00");
        PersonAvailableDuringPredicate availPredicate = new PersonAvailableDuringPredicate(query);
        PersonContainsKeywordsPredicate textPredicate =
                new PersonContainsKeywordsPredicate(Arrays.asList("alice"));
        FindCommand expected = new FindCommand(
                new CombinedAndPersonPredicate(List.of(textPredicate, availPredicate)));
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " va/MONDAY,14:00,17:00 alice");
        assertEquals(expected, command);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> searchTerms = Arrays.asList("foo", "bar", "baz");
        FindCommand defaultCommand = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + searchTerms.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new PersonContainsKeywordsPredicate(searchTerms)), defaultCommand);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_import() throws Exception {
        ImportCommand command = (ImportCommand) parser.parseCommand(
                ImportCommand.COMMAND_WORD + " data/volunteers.csv");
        assertEquals(new ImportCommand(Paths.get("data/volunteers.csv")), command);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertEquals(new ListCommand(), parser.parseCommand(ListCommand.COMMAND_WORD));
        assertEquals(new ListCommand(SortAttribute.NAME, SortOrder.ASC),
                parser.parseCommand(ListCommand.COMMAND_WORD + " name"));
        assertEquals(new ListCommand(SortAttribute.EMAIL, SortOrder.DESC),
                parser.parseCommand(ListCommand.COMMAND_WORD + " email desc"));
    }

    @Test
    public void parseCommand_restore() throws Exception {
        RestoreCommand command = (RestoreCommand) parser.parseCommand(
                RestoreCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new RestoreCommand(List.of(INDEX_FIRST_PERSON)), command);
    }

    @Test
    public void parseCommand_unalias() throws Exception {
        UnaliasCommand command = (UnaliasCommand) parser.parseCommand(UnaliasCommand.COMMAND_WORD + " ls");
        assertEquals(new UnaliasCommand("ls"), command);
    }

    @Test
    public void parseCommand_allBuiltInCommandWordsHaveWorkingExamples_success() throws Exception {
        Map<String, String> commandExamples = Map.ofEntries(
                Map.entry(AddCommand.COMMAND_WORD, "add n/Amy p/91234567 e/amy@example.com a/123, Clementi Rd"),
                Map.entry(AliasCommand.COMMAND_WORD, "alias ls list"),
                Map.entry(AliasesCommand.COMMAND_WORD, "aliases"),
                Map.entry(BinCommand.COMMAND_WORD, "bin"),
                Map.entry(ClearCommand.COMMAND_WORD, "clear"),
                Map.entry(DeleteCommand.COMMAND_WORD, "delete 1"),
                Map.entry(EditCommand.COMMAND_WORD, "edit 1 n/Amy"),
                Map.entry(EditPreviousCommand.COMMAND_WORD, "editprev"),
                Map.entry(ExitCommand.COMMAND_WORD, "exit"),
                Map.entry(ExportCommand.COMMAND_WORD, "export data/volunteers.csv"),
                Map.entry(FindCommand.COMMAND_WORD, "find Amy"),
                Map.entry(HelpCommand.COMMAND_WORD, "help"),
                Map.entry(ImportCommand.COMMAND_WORD, "import data/volunteers.csv"),
                Map.entry(ListCommand.COMMAND_WORD, "list"),
                Map.entry(RestoreCommand.COMMAND_WORD, "restore 1"),
                Map.entry(StatsCommand.COMMAND_WORD, "stats role"),
                Map.entry(UnaliasCommand.COMMAND_WORD, "unalias ls"));

        assertEquals(CommandWords.TOP_LEVEL_COMMAND_WORDS, commandExamples.keySet());
        for (String commandExample : commandExamples.values()) {
            parser.parseCommand(commandExample);
        }
    }

    @Test
    public void parseCommand_documentedCommandSummaryExamples_success() throws Exception {
        List<String> commandExamples = List.of(
                "add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 "
                        + "t/friend t/colleague r/Usher nt/Available weekends",
                "alias ls list",
                "aliases",
                "unalias ls",
                "clear",
                "delete 2 3",
                "edit 2 n/James Lee e/jameslee@example.com va/MONDAY,14:00,17:00",
                "export data/volunteers.csv",
                "find m/kw James Jake",
                "help",
                "import data/volunteers.csv",
                "list name desc",
                "exit");

        for (String commandExample : commandExamples) {
            parser.parseCommand(commandExample);
        }
    }

    @Test
    public void parseCommand_stats() throws Exception {
        StatsCommand roleCommand = (StatsCommand) parser.parseCommand(StatsCommand.COMMAND_WORD + " role");
        assertEquals(new StatsCommand(seedu.address.model.statistics.StatisticsCategory.ROLE), roleCommand);

        StatsCommand recordCommand = (StatsCommand) parser.parseCommand(StatsCommand.COMMAND_WORD + " record");
        assertEquals(new StatsCommand(seedu.address.model.statistics.StatisticsCategory.RECORD), recordCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
