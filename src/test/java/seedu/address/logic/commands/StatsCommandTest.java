package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.PersonListView;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.statistics.StatisticsCategory;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code StatsCommand}.
 */
public class StatsCommandTest {

    @Test
    public void equals() {
        StatsCommand roleCommand = new StatsCommand(StatisticsCategory.ROLE);
        StatsCommand roleCommandCopy = new StatsCommand(StatisticsCategory.ROLE);
        StatsCommand recordCommand = new StatsCommand(StatisticsCategory.RECORD);
        StatsCommand recordCommandCopy = new StatsCommand(StatisticsCategory.RECORD);

        // same values -> returns true
        assertTrue(roleCommand.equals(roleCommandCopy));
        assertTrue(recordCommand.equals(recordCommandCopy));

        // same object -> returns true
        assertTrue(roleCommand.equals(roleCommand));

        // null -> returns false
        assertFalse(roleCommand.equals(null));

        // different types -> returns false
        assertFalse(roleCommand.equals(1));

        // different category -> returns false
        assertFalse(roleCommand.equals(recordCommand));
    }

    @Test
    public void execute_roleStats_success() {
        Model model = new ModelManager(buildAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(buildAddressBook(), new UserPrefs());

        StatsCommand command = new StatsCommand(StatisticsCategory.ROLE);
        String expected = String.join("\n",
                "Role Distribution (4 volunteers)",
                "Leader     | ########## 50.0% (2)",
                "Medic      | ##### 25.0% (1)",
                "Unassigned | ##### 25.0% (1)");

        assertCommandSuccess(command, model, PersonListView.DELETED_PERSONS,
                expected, PersonListView.KEPT_PERSONS, expectedModel);
    }

    @Test
    public void execute_recordStats_success() {
        Model model = new ModelManager(buildAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(buildAddressBook(), new UserPrefs());

        StatsCommand command = new StatsCommand(StatisticsCategory.RECORD);
        String expected = String.join("\n",
                "Volunteer Record Counts (4 volunteers)",
                "Alex Tan | #################### 2 record(s)",
                "Beth Lim | ########## 1 record(s)",
                "Dana Yu  | ########## 1 record(s)",
                "Chen Wei |  0 record(s)");

        assertCommandSuccess(command, model, expected, expectedModel);
    }

    @Test
    public void toStringMethod() {
        StatsCommand command = new StatsCommand(StatisticsCategory.ROLE);
        String expected = StatsCommand.class.getCanonicalName() + "{category=ROLE}";
        assertEquals(expected, command.toString());
    }

    private AddressBook buildAddressBook() {
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(new PersonBuilder().withName("Alex Tan")
                .withPhone("90000001")
                .withEmail("alex.tan@example.com")
                .withRole("Leader")
                .withRecords("2026-03-20T09:00,2026-03-20T12:00",
                        "2026-03-21T09:00,2026-03-21T11:00")
                .build());
        addressBook.addPerson(new PersonBuilder().withName("Beth Lim")
                .withPhone("90000002")
                .withEmail("beth.lim@example.com")
                .withRole("Leader")
                .withRecords("2026-03-22T10:00,2026-03-22T12:00")
                .build());
        addressBook.addPerson(new PersonBuilder().withName("Chen Wei")
                .withPhone("90000003")
                .withEmail("chen.wei@example.com")
                .withRole("Medic")
                .build());
        addressBook.addPerson(new PersonBuilder().withName("Dana Yu")
                .withPhone("90000004")
                .withEmail("dana.yu@example.com")
                .withRecords("2026-03-23T13:00,2026-03-23T14:00")
                .build());
        return addressBook;
    }
}
