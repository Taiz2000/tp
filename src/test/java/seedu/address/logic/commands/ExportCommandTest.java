package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.AddressBook;
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
        ModelStub model = new ModelStubWithAddressBook(new AddressBook());
        ExportCommand command = new ExportCommand(outputFile);

        CommandResult result = command.execute(model);

        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, 0, outputFile),
                result.getFeedbackToUser());

        assertTrue(Files.exists(outputFile));
        String content = Files.readString(outputFile);
        assertEquals("name,phone,email,address,role,notes,tags,availabilities,records"
                + System.lineSeparator(), content);
    }

    @Test
    public void execute_nonEmptyAddressBook_exportsSuccessfully() throws Exception {
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

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(alice);
        addressBook.addPerson(bob);

        ModelStub model = new ModelStubWithAddressBook(addressBook);
        ExportCommand command = new ExportCommand(outputFile);

        CommandResult result = command.execute(model);

        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, 2, outputFile),
                result.getFeedbackToUser());

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
    public void execute_existingFile_overwritesFile() throws Exception {
        Path outputFile = tempDir.resolve("volunteers.csv");
        Files.writeString(outputFile, "old content");

        Person alice = new PersonBuilder().withName("Alice Pauline")
                .withPhone("94351253")
                .withEmail("alice@example.com")
                .withAddress("123 Jurong West Ave 6")
                .build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(alice);

        ModelStub model = new ModelStubWithAddressBook(addressBook);
        ExportCommand command = new ExportCommand(outputFile);

        CommandResult result = command.execute(model);

        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, 1, outputFile),
                result.getFeedbackToUser());

        String content = Files.readString(outputFile);
        assertTrue(content.contains("name,phone,email,address,role,notes,tags,availabilities,records"));
        assertTrue(content.contains("Alice Pauline"));
        assertTrue(!content.contains("old content"));
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
        public void addPerson(Person person) {
            fail("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void updateSortedPersonList(Comparator<Person> comparator) {
            fail("This method should not be called.");
        }
    }

    /**
     * A Model stub that returns a supplied address book.
     */
    private static class ModelStubWithAddressBook extends ModelStub {
        private final ReadOnlyAddressBook addressBook;

        ModelStubWithAddressBook(ReadOnlyAddressBook addressBook) {
            this.addressBook = addressBook;
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return addressBook;
        }
    }
}
