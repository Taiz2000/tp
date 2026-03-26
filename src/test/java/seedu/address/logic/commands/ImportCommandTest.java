package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Person;

public class ImportCommandTest {

    @TempDir
    public Path tempDir;

    @Test
    public void execute_validRows_importsSuccessfully() throws Exception {
        Path inputFile = tempDir.resolve("volunteers.csv");
        Files.writeString(inputFile, String.join(System.lineSeparator(),
                "name,phone,email,address,role,notes,tags,availabilities,records",
                "Alice Pauline,94351253,alice@example.com,\"123, Jurong West Ave 6\",Leader,Experienced,"
                        +
                        "friend;driver,\"MONDAY,09:00,12:00\",\"2026-03-20T09:00,2026-03-20T12:00\"",
                "Bob Choo,91234567,bob@example.com,456 Clementi Ave 3,,,,,"
        ));

        ModelStubAcceptingPersonsAdded model = new ModelStubAcceptingPersonsAdded();
        ImportCommand command = new ImportCommand(inputFile);

        CommandResult result = command.execute(model);

        assertEquals("Imported 2 volunteers from " + inputFile + ".", result.getFeedbackToUser());
        assertEquals(2, model.personsAdded.size());
    }

    @Test
    public void execute_duplicateAndInvalidRows_reportsSummary() throws Exception {
        Path inputFile = tempDir.resolve("volunteers.csv");
        Files.writeString(inputFile, String.join(System.lineSeparator(),
                "name,phone,email,address,role,notes,tags,availabilities,records",
                "Alice Pauline,94351253,alice@example.com,123 Jurong West Ave 6,,,,,",
                "Alice Pauline,94351253,alice@example.com,123 Jurong West Ave 6,,,,,",
                "Charlie Tan,,charlie@example.com,789 Bukit Timah Road,,,,,"
        ));

        ModelStubAcceptingPersonsAdded model = new ModelStubAcceptingPersonsAdded();
        ImportCommand command = new ImportCommand(inputFile);

        CommandResult result = command.execute(model);

        assertEquals(String.join(System.lineSeparator(),
                "Imported 1 volunteers from " + inputFile + ".",
                "Duplicate rows: 1, Invalid rows: 1",
                "Duplicate row details: 3 (same phone and email)",
                "Invalid row details: 4 (missing phone)"), result.getFeedbackToUser());

        assertEquals(1, model.personsAdded.size());
    }

    @Test
    public void execute_missingRequiredHeaders_throwsCommandException() throws Exception {
        Path inputFile = tempDir.resolve("volunteers.csv");
        Files.writeString(inputFile, String.join(System.lineSeparator(),
                "name,phone,email",
                "Alice Pauline,94351253,alice@example.com"
        ));

        ModelStubAcceptingPersonsAdded model = new ModelStubAcceptingPersonsAdded();
        ImportCommand command = new ImportCommand(inputFile);

        try {
            command.execute(model);
            fail("Expected CommandException to be thrown.");
        } catch (seedu.address.logic.commands.exceptions.CommandException e) {
            assertEquals("Import failed: missing required headers: name, phone, email, address",
                    e.getMessage());
        }
    }

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
            return new AddressBook();
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
            return FXCollections.observableArrayList();
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

    private static class ModelStubAcceptingPersonsAdded extends ModelStub {
        final List<Person> personsAdded = new ArrayList<>();

        @Override
        public boolean hasPerson(Person person) {
            return personsAdded.stream().anyMatch(existing -> existing.isSamePerson(person));
        }

        @Override
        public void addPerson(Person person) {
            personsAdded.add(person);
        }
    }
}
