package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.PersonListView;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class ExportCommandTest {

    @TempDir
    public Path tempDir;

    @Test
    public void execute_emptyAddressBook_exportsHeaderOnly() throws Exception {
        Path outputFile = tempDir.resolve("empty.csv");
        ModelStub model = new ModelStubWithPersonLists(List.of(), List.of(), List.of());
        ExportCommand command = new ExportCommand(outputFile);

        CommandResult result = command.execute(model, PersonListView.KEPT_PERSONS);

        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, 0, outputFile),
                result.getFeedbackToUser());
        assertEquals(PersonListView.KEPT_PERSONS, result.getPersonListView());

        assertTrue(Files.exists(outputFile));
        String content = Files.readString(outputFile);
        assertEquals("name,phone,email,address,role,notes,tags,availabilities,records"
                + System.lineSeparator(), content);
    }

    @Test
    public void executeUnfilteredKeptViewExportsAllKeptPersonsAndKeepsView() throws Exception {
        Path outputFile = tempDir.resolve("volunteers.csv");

        Person alice = new PersonBuilder().withName("Alice Pauline")
                .withPhone("94351253")
                .withEmail("alice@example.com")
                .withAddress("123, Jurong West Ave 6, #08-111")
                .withRole("Leader")
                .withNotes("Experienced volunteer")
                .withTags("friend", "driver")
                .withAvailabilities("MONDAY,09:00,12:00", "TUESDAY,14:00,17:00")
                .withRecords("2026-03-20T09:00,2026-03-20T12:00")
                .build();

        Person bob = new PersonBuilder().withName("Bob Choo")
                .withPhone("91234567")
                .withEmail("bob@example.com")
                .withAddress("456 Clementi Ave 3")
                .build();

        ModelStub model = new ModelStubWithPersonLists(
                List.of(alice, bob),
                List.of(alice, bob),
                List.of());
        ExportCommand command = new ExportCommand(outputFile);

        CommandResult result = command.execute(model, PersonListView.KEPT_PERSONS);

        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, 2, outputFile),
                result.getFeedbackToUser());
        assertEquals(PersonListView.KEPT_PERSONS, result.getPersonListView());

        assertTrue(Files.exists(outputFile));

        String content = Files.readString(outputFile);
        String[] lines = content.split(System.lineSeparator());

        assertEquals("name,phone,email,address,role,notes,tags,availabilities,records", lines[0]);
        assertTrue(content.contains("Alice Pauline"));
        assertTrue(content.contains("Bob Choo"));
        assertTrue(content.contains("\"123, Jurong West Ave 6, #08-111\""));
        assertTrue(content.contains("Leader"));
        assertTrue(content.contains("Experienced volunteer"));
        assertTrue(content.contains("friend;driver") || content.contains("driver;friend"));
        assertTrue(content.contains("\"MONDAY,09:00,12:00;TUESDAY,14:00,17:00\"")
                || content.contains("\"TUESDAY,14:00,17:00;MONDAY,09:00,12:00\""));
        assertTrue(content.contains("\"2026-03-20T09:00,2026-03-20T12:00\""));
    }

    @Test
    public void executeFilteredKeptViewExportsOnlyFilteredPersonsAndKeepsView() throws Exception {
        Path outputFile = tempDir.resolve("filtered-volunteers.csv");

        Person alice = new PersonBuilder().withName("Alice Pauline")
                .withEmail("alice@example.com")
                .build();
        Person bob = new PersonBuilder().withName("Bob Choo")
                .withEmail("bob@example.com")
                .build();
        Person clara = new PersonBuilder().withName("Clara Tan")
                .withEmail("clara@example.com")
                .build();
        Person deletedDana = new PersonBuilder().withName("Dana Bin")
                .withEmail("dana.deleted@example.com")
                .build();

        ModelStub model = new ModelStubWithPersonLists(
                List.of(alice, bob, clara),
                List.of(alice, clara),
                List.of(deletedDana));
        ExportCommand command = new ExportCommand(outputFile);

        CommandResult result = command.execute(model, PersonListView.KEPT_PERSONS);

        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, 2, outputFile),
                result.getFeedbackToUser());
        assertEquals(PersonListView.KEPT_PERSONS, result.getPersonListView());

        String content = Files.readString(outputFile);
        assertCsvContainsPerson(content, alice);
        assertCsvContainsPerson(content, clara);
        assertCsvExcludesPerson(content, bob);
        assertCsvExcludesPerson(content, deletedDana);
    }

    @Test
    public void execute_existingFile_exportsToDerivedSiblingFile() throws Exception {
        Path requestedFile = tempDir.resolve("volunteers.csv");
        Files.writeString(requestedFile, "old content");

        Person alice = new PersonBuilder().withName("Alice Pauline")
                .withPhone("94351253")
                .withEmail("alice@example.com")
                .withAddress("123 Jurong West Ave 6")
                .build();

        ModelStub model = new ModelStubWithPersonLists(
                List.of(alice),
                List.of(alice),
                List.of());
        ExportCommand command = new ExportCommand(requestedFile);

        CommandResult result = command.execute(model, PersonListView.KEPT_PERSONS);
        Path generatedFile = findGeneratedSiblingFile(requestedFile);

        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS_REDIRECTED, 1, generatedFile, requestedFile),
                result.getFeedbackToUser());
        assertEquals(PersonListView.KEPT_PERSONS, result.getPersonListView());

        assertEquals("old content", Files.readString(requestedFile));
        assertEquals(tempDir, generatedFile.getParent());
        assertTrue(generatedFile.getFileName().toString()
                .matches("volunteers-\\d{8}T\\d{6}-[0-9a-f]{8}\\.csv"));

        String content = Files.readString(generatedFile);
        assertTrue(content.contains("name,phone,email,address,role,notes,tags,availabilities,records"));
        assertTrue(content.contains("Alice Pauline"));
        assertTrue(!content.contains("old content"));
    }

    @Test
    public void execute_missingParentDirectory_createsDirectoryAndExports() throws Exception {
        Path outputFile = tempDir.resolve("exports").resolve("archive").resolve("volunteers.csv");
        Person alice = new PersonBuilder().withName("Alice Pauline")
                .withEmail("alice@example.com")
                .build();
        ModelStub model = new ModelStubWithPersonLists(List.of(alice), List.of(alice), List.of());

        CommandResult result = new ExportCommand(outputFile).execute(model, PersonListView.KEPT_PERSONS);

        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, 1, outputFile), result.getFeedbackToUser());
        assertTrue(Files.isDirectory(outputFile.getParent()));
        assertTrue(Files.exists(outputFile));
    }

    @Test
    public void resolveOutputFilePath_existingFileWithoutExtension_appendsSuffixInSameDirectory() throws Exception {
        Path requestedFile = tempDir.resolve("volunteers");
        Files.writeString(requestedFile, "old content");

        Path resolvedFile = ExportCommand.resolveOutputFilePath(requestedFile);

        assertEquals(tempDir, resolvedFile.getParent());
        assertTrue(resolvedFile.getFileName().toString().matches("volunteers-\\d{8}T\\d{6}-[0-9a-f]{8}"));
        assertTrue(!resolvedFile.equals(requestedFile));
    }

    @Test
    public void messageUsage_mentionsGeneratedFileNameWhenPathExists() {
        assertEquals("export: Exports kept volunteers to a CSV file. "
                        + "If FILE_PATH already exists, a new file name will be generated automatically.\n"
                        + "When viewing the contact list, exports the currently displayed kept contacts, "
                        + "so active find filters are applied.\n"
                        + "When viewing the recycle bin, exports the full kept contact list instead; "
                        + "deleted contacts are never exported and the view switches back to the contact list.\n"
                        + "Parameters: FILE_PATH\n"
                        + "Example: export data/volunteers.csv",
                ExportCommand.MESSAGE_USAGE);
    }

    @Test
    public void executeDeletedViewExportsFullKeptListAndSwitchesToKeptView() throws Exception {
        Path outputFile = tempDir.resolve("deleted-view-export.csv");

        Person alice = new PersonBuilder().withName("Alice Pauline")
                .withEmail("alice@example.com")
                .build();
        Person bob = new PersonBuilder().withName("Bob Choo")
                .withEmail("bob@example.com")
                .build();
        Person clara = new PersonBuilder().withName("Clara Tan")
                .withEmail("clara@example.com")
                .build();
        Person deletedDana = new PersonBuilder().withName("Dana Bin")
                .withEmail("dana.deleted@example.com")
                .build();
        Person deletedEvan = new PersonBuilder().withName("Evan Goh")
                .withEmail("evan.deleted@example.com")
                .build();

        ModelStub model = new ModelStubWithPersonLists(
                List.of(alice, bob, clara),
                List.of(alice),
                List.of(deletedDana, deletedEvan));
        ExportCommand command = new ExportCommand(outputFile);

        CommandResult result = command.execute(model, PersonListView.DELETED_PERSONS);

        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, 3, outputFile),
                result.getFeedbackToUser());
        assertEquals(PersonListView.KEPT_PERSONS, result.getPersonListView());

        String content = Files.readString(outputFile);
        assertCsvContainsPerson(content, alice);
        assertCsvContainsPerson(content, bob);
        assertCsvContainsPerson(content, clara);
        assertCsvExcludesPerson(content, deletedDana);
        assertCsvExcludesPerson(content, deletedEvan);
    }

    @Test
    public void execute_neverExportsDeletedPersons() throws Exception {
        Person keptAlice = new PersonBuilder().withName("Alice Pauline")
                .withEmail("alice@example.com")
                .build();
        Person keptBob = new PersonBuilder().withName("Bob Choo")
                .withEmail("bob@example.com")
                .build();
        Person deletedDana = new PersonBuilder().withName("Dana Bin")
                .withEmail("dana.deleted@example.com")
                .build();

        ModelStub model = new ModelStubWithPersonLists(
                List.of(keptAlice, keptBob),
                List.of(keptAlice),
                List.of(deletedDana));

        Path keptViewFile = tempDir.resolve("never-export-deleted-kept.csv");
        CommandResult keptViewResult = new ExportCommand(keptViewFile).execute(model, PersonListView.KEPT_PERSONS);
        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, 1, keptViewFile),
                keptViewResult.getFeedbackToUser());
        String keptViewContent = Files.readString(keptViewFile);
        assertCsvContainsPerson(keptViewContent, keptAlice);
        assertCsvExcludesPerson(keptViewContent, keptBob);
        assertCsvExcludesPerson(keptViewContent, deletedDana);

        Path deletedViewFile = tempDir.resolve("never-export-deleted-bin.csv");
        CommandResult deletedViewResult = new ExportCommand(deletedViewFile)
                .execute(model, PersonListView.DELETED_PERSONS);
        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, 2, deletedViewFile),
                deletedViewResult.getFeedbackToUser());
        String deletedViewContent = Files.readString(deletedViewFile);
        assertCsvContainsPerson(deletedViewContent, keptAlice);
        assertCsvContainsPerson(deletedViewContent, keptBob);
        assertCsvExcludesPerson(deletedViewContent, deletedDana);
    }

    @Test
    public void toStringMethod() {
        Path filePath = Path.of("data/volunteers.csv");
        ExportCommand exportCommand = new ExportCommand(filePath);
        String expected = ExportCommand.class.getCanonicalName() + "{filePath=" + filePath + "}";
        assertEquals(expected, exportCommand.toString());
    }

    private Path findGeneratedSiblingFile(Path requestedFile) throws Exception {
        try (Stream<Path> files = Files.list(requestedFile.getParent())) {
            List<Path> generatedFiles = files
                    .filter(path -> !path.equals(requestedFile))
                    .toList();
            assertEquals(1, generatedFiles.size());
            return generatedFiles.get(0);
        }
    }

    /**
     * A default model stub that fails on all methods.
     */
    private abstract static class ModelStub implements Model {

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public GuiSettings getGuiSettings() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            fail("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            fail("This method should not be called.");
        }

        @Override
        public Map<String, String> getCommandAliases() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public boolean hasCommandAlias(String shortName) {
            fail("This method should not be called.");
            return false;
        }

        @Override
        public void setCommandAlias(String shortName, String template) {
            fail("This method should not be called.");
        }

        @Override
        public void removeCommandAlias(String shortName) {
            fail("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook addressBook) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public boolean hasPerson(Person person) {
            fail("This method should not be called.");
            return false;
        }

        @Override
        public void deletePerson(Person target) {
            fail("This method should not be called.");
        }

        @Override
        public void restorePerson(Person target) {
            fail("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            fail("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredKeptPersonList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public ObservableList<Person> getKeptPersonList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public ObservableList<Person> getFilteredDeletedPersonList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deleteAllPersons() {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredKeptPersonList(Predicate<Person> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredDeletedPersonList(Predicate<Person> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void updateSortedPersonList(Comparator<Person> comparator) {
            fail("This method should not be called.");
        }

        @Override
        public String getLastCommandText() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void setLastCommandText(String commandText) {
            fail("This method should not be called.");
        }
    }

    /**
     * A Model stub that returns supplied kept and deleted person lists.
     */
    private static class ModelStubWithPersonLists extends ModelStub {
        private final ObservableList<Person> keptPersons;
        private final ObservableList<Person> filteredKeptPersons;
        private final ObservableList<Person> filteredDeletedPersons;

        ModelStubWithPersonLists(List<Person> keptPersons, List<Person> filteredKeptPersons,
                List<Person> filteredDeletedPersons) {
            this.keptPersons = FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(keptPersons));
            this.filteredKeptPersons = FXCollections.unmodifiableObservableList(
                    FXCollections.observableArrayList(filteredKeptPersons));
            this.filteredDeletedPersons = FXCollections.unmodifiableObservableList(
                    FXCollections.observableArrayList(filteredDeletedPersons));
        }

        @Override
        public ObservableList<Person> getKeptPersonList() {
            return keptPersons;
        }

        @Override
        public ObservableList<Person> getFilteredKeptPersonList() {
            return filteredKeptPersons;
        }

        @Override
        public ObservableList<Person> getFilteredDeletedPersonList() {
            return filteredDeletedPersons;
        }
    }

    private static void assertCsvContainsPerson(String csvContent, Person person) {
        assertTrue(csvContent.contains(person.getName().fullName));
        assertTrue(csvContent.contains(person.getEmail().value));
    }

    private static void assertCsvExcludesPerson(String csvContent, Person person) {
        assertTrue(!csvContent.contains(person.getName().fullName));
        assertTrue(!csvContent.contains(person.getEmail().value));
    }
}
