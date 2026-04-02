package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.VolunteerAvailability;
import seedu.address.model.person.predicates.PersonAvailableDuringPredicate;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code FindAvailCommand}.
 */
public class FindAvailCommandTest {

    @Test
    public void equals() {
        VolunteerAvailability queryMonday = VolunteerAvailability.fromString("MONDAY,14:00,17:00");
        VolunteerAvailability queryTuesday = VolunteerAvailability.fromString("TUESDAY,14:00,17:00");
        PersonAvailableDuringPredicate firstPredicate = new PersonAvailableDuringPredicate(queryMonday);
        PersonAvailableDuringPredicate secondPredicate = new PersonAvailableDuringPredicate(queryTuesday);

        FindAvailCommand findFirstCommand = new FindAvailCommand(firstPredicate);
        FindAvailCommand findSecondCommand = new FindAvailCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindAvailCommand findFirstCommandCopy = new FindAvailCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different predicate -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_noMatchingVolunteers_noPersonFound() {
        AddressBook ab = new AddressBook();
        Person person = new PersonBuilder().withAvailabilities("TUESDAY,09:00,12:00").build();
        ab.addPerson(person);
        Model model = new ModelManager(ab, new UserPrefs());
        Model expectedModel = new ModelManager(ab, new UserPrefs());

        VolunteerAvailability query = VolunteerAvailability.fromString("MONDAY,14:00,17:00");
        PersonAvailableDuringPredicate predicate = new PersonAvailableDuringPredicate(query);
        FindAvailCommand command = new FindAvailCommand(predicate);

        expectedModel.updateFilteredKeptPersonList(predicate);
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_matchingVolunteer_personFound() {
        AddressBook ab = new AddressBook();
        Person person = new PersonBuilder().withAvailabilities("MONDAY,13:00,18:00").build();
        ab.addPerson(person);
        Model model = new ModelManager(ab, new UserPrefs());
        Model expectedModel = new ModelManager(ab, new UserPrefs());

        VolunteerAvailability query = VolunteerAvailability.fromString("MONDAY,14:00,17:00");
        PersonAvailableDuringPredicate predicate = new PersonAvailableDuringPredicate(query);
        FindAvailCommand command = new FindAvailCommand(predicate);

        expectedModel.updateFilteredKeptPersonList(predicate);
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void toStringMethod() {
        VolunteerAvailability query = VolunteerAvailability.fromString("MONDAY,14:00,17:00");
        PersonAvailableDuringPredicate predicate = new PersonAvailableDuringPredicate(query);
        FindAvailCommand command = new FindAvailCommand(predicate);
        String expected = FindAvailCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, command.toString());
    }
}
