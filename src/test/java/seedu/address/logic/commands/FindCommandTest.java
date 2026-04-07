package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.parser.FindMatchType.KEYWORD_TOKEN;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.YVETTE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.PersonListView;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.VolunteerAvailability;
import seedu.address.model.person.predicates.CombinedAndPersonPredicate;
import seedu.address.model.person.predicates.PersonAvailableDuringPredicate;
import seedu.address.model.person.predicates.PersonContainsFuzzyKeywordsPredicate;
import seedu.address.model.person.predicates.PersonContainsKeywordsPredicate;
import seedu.address.model.person.predicates.PersonContainsSubstringsPredicate;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        PersonContainsKeywordsPredicate firstPredicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList("first"));
        PersonContainsKeywordsPredicate secondPredicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList("second"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    /* This is the only test where the FindCommand is executed while viewing deleted persons.
     *
     * It is expected that the logical flow of FindCommand is identical to the case where the
     * user views kept persons, with the only difference being the list which the FindCommand
     * acts on and displays after execution. This test aims to check that difference.
     *
     * For more robust integration testing of FindCommand with Predicate classes,
     * refer to the tests which are executed while viewing kept persons.
     */
    @Test
    public void execute_viewingDeletedPersons_success() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        PersonContainsKeywordsPredicate predicate = preparePredicate("street");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredDeletedPersonList(predicate);
        assertCommandSuccess(command, model, PersonListView.DELETED_PERSONS,
                expectedMessage, PersonListView.DELETED_PERSONS, expectedModel);
        assertEquals(Collections.singletonList(YVETTE), model.getFilteredDeletedPersonList());
    }

    // This test and all subsequent tests of execute() are executed while viewing kept persons.
    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        PersonContainsKeywordsPredicate predicate = preparePredicate(" ");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredKeptPersonList(predicate);
        assertCommandSuccess(command, model, PersonListView.KEPT_PERSONS,
                expectedMessage, PersonListView.KEPT_PERSONS, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredKeptPersonList());
    }

    @Test
    public void execute_singleKeyword_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        PersonContainsKeywordsPredicate predicate = preparePredicate("street");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredKeptPersonList(predicate);
        assertCommandSuccess(command, model, PersonListView.KEPT_PERSONS,
                expectedMessage, PersonListView.KEPT_PERSONS, expectedModel);
        assertEquals(Arrays.asList(CARL, DANIEL, GEORGE), model.getFilteredKeptPersonList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        PersonContainsKeywordsPredicate predicate = preparePredicate("Kurz Elle Kunz");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredKeptPersonList(predicate);
        assertCommandSuccess(command, model, PersonListView.KEPT_PERSONS,
                expectedMessage, PersonListView.KEPT_PERSONS, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredKeptPersonList());
    }

    @Test
    public void execute_substringMatch_singlePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        PersonContainsSubstringsPredicate predicate =
                new PersonContainsSubstringsPredicate(Collections.singletonList("ell"));
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredKeptPersonList(predicate);
        assertCommandSuccess(command, model, PersonListView.KEPT_PERSONS,
                expectedMessage, PersonListView.KEPT_PERSONS, expectedModel);
        assertEquals(Collections.singletonList(ELLE), model.getFilteredKeptPersonList());
    }

    @Test
    public void execute_substringMatch_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        PersonContainsSubstringsPredicate predicate =
                new PersonContainsSubstringsPredicate(Collections.singletonList("meie"));
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredKeptPersonList(predicate);
        assertCommandSuccess(command, model, PersonListView.KEPT_PERSONS,
                expectedMessage, PersonListView.KEPT_PERSONS, expectedModel);
        assertEquals(Arrays.asList(BENSON, DANIEL), model.getFilteredKeptPersonList());
    }

    @Test
    public void execute_fuzzyMatch_singlePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        PersonContainsFuzzyKeywordsPredicate predicate =
                new PersonContainsFuzzyKeywordsPredicate(Collections.singletonList("michigan"));
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredKeptPersonList(predicate);
        assertCommandSuccess(command, model, PersonListView.KEPT_PERSONS,
                expectedMessage, PersonListView.KEPT_PERSONS, expectedModel);
        assertEquals(Collections.singletonList(ELLE), model.getFilteredKeptPersonList());
    }

    @Test
    public void execute_fuzzyMatch_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        PersonContainsFuzzyKeywordsPredicate predicate =
                new PersonContainsFuzzyKeywordsPredicate(Collections.singletonList("stret"));
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredKeptPersonList(predicate);
        assertCommandSuccess(command, model, PersonListView.KEPT_PERSONS,
                expectedMessage, PersonListView.KEPT_PERSONS, expectedModel);
        assertEquals(Arrays.asList(CARL, DANIEL, GEORGE), model.getFilteredKeptPersonList());
    }

    @Test
    public void execute_availabilityFilter_matchingPersonFound() {
        AddressBook ab = new AddressBook();
        Person available = new PersonBuilder().withName("Alice")
                .withPhone("91111111").withEmail("alice@example.com")
                .withAvailabilities("MONDAY,13:00,18:00").build();
        Person unavailable = new PersonBuilder().withName("Bob")
                .withPhone("92222222").withEmail("bob@example.com")
                .withAvailabilities("TUESDAY,09:00,12:00").build();
        ab.addPerson(available);
        ab.addPerson(unavailable);

        Model availModel = new ModelManager(ab, new UserPrefs());
        Model expectedAvailModel = new ModelManager(ab, new UserPrefs());

        VolunteerAvailability query = VolunteerAvailability.fromString("MONDAY,14:00,17:00");
        PersonAvailableDuringPredicate predicate = new PersonAvailableDuringPredicate(query);
        FindCommand command = new FindCommand(predicate);

        expectedAvailModel.updateFilteredKeptPersonList(predicate);
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        assertCommandSuccess(command, availModel, PersonListView.KEPT_PERSONS,
                expectedMessage, PersonListView.KEPT_PERSONS, expectedAvailModel);
        assertEquals(List.of(available), availModel.getFilteredKeptPersonList());
    }

    @Test
    public void execute_availabilityFilter_noMatchingPerson() {
        AddressBook ab = new AddressBook();
        Person person = new PersonBuilder().withName("Alice")
                .withPhone("91111111").withEmail("alice@example.com")
                .withAvailabilities("MONDAY,13:00,15:00").build();
        ab.addPerson(person);

        Model availModel = new ModelManager(ab, new UserPrefs());
        Model expectedAvailModel = new ModelManager(ab, new UserPrefs());

        // Query ends after volunteer's availability — should not match (full coverage required)
        VolunteerAvailability query = VolunteerAvailability.fromString("MONDAY,14:00,17:00");
        PersonAvailableDuringPredicate predicate = new PersonAvailableDuringPredicate(query);
        FindCommand command = new FindCommand(predicate);

        expectedAvailModel.updateFilteredKeptPersonList(predicate);
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        assertCommandSuccess(command, availModel, PersonListView.KEPT_PERSONS,
                expectedMessage, PersonListView.KEPT_PERSONS, expectedAvailModel);
    }

    @Test
    public void execute_keywordsAndAvailability_bothMustMatch() {
        AddressBook ab = new AddressBook();
        // Matches both name keyword "Alice" and Monday availability
        Person matchesBoth = new PersonBuilder().withName("Alice Tan")
                .withPhone("91111111").withEmail("alice@example.com")
                .withAvailabilities("MONDAY,13:00,18:00").build();
        // Matches name keyword "Alice" but NOT Monday availability
        Person matchesNameOnly = new PersonBuilder().withName("Alice Lee")
                .withPhone("92222222").withEmail("alicelee@example.com")
                .withAvailabilities("TUESDAY,09:00,12:00").build();
        // Matches Monday availability but NOT name keyword "Alice"
        Person matchesAvailOnly = new PersonBuilder().withName("Bob")
                .withPhone("93333333").withEmail("bob@example.com")
                .withAvailabilities("MONDAY,13:00,18:00").build();
        ab.addPerson(matchesBoth);
        ab.addPerson(matchesNameOnly);
        ab.addPerson(matchesAvailOnly);

        Model combinedModel = new ModelManager(ab, new UserPrefs());
        Model expectedCombinedModel = new ModelManager(ab, new UserPrefs());

        VolunteerAvailability query = VolunteerAvailability.fromString("MONDAY,14:00,17:00");
        PersonContainsKeywordsPredicate textPredicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList("Alice"));
        PersonAvailableDuringPredicate availPredicate = new PersonAvailableDuringPredicate(query);
        CombinedAndPersonPredicate combinedPredicate =
                new CombinedAndPersonPredicate(List.of(textPredicate, availPredicate));
        FindCommand command = new FindCommand(combinedPredicate);

        expectedCombinedModel.updateFilteredKeptPersonList(combinedPredicate);
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        assertCommandSuccess(command, combinedModel, PersonListView.KEPT_PERSONS,
                expectedMessage, PersonListView.KEPT_PERSONS, expectedCombinedModel);
        assertEquals(List.of(matchesBoth), combinedModel.getFilteredKeptPersonList());
    }

    @Test
    public void execute_keywordsAndAvailability_noMatchingPerson() {
        AddressBook ab = new AddressBook();
        // Matches name keyword "Alice" but wrong day
        Person matchesNameOnly = new PersonBuilder().withName("Alice Tan")
                .withPhone("91111111").withEmail("alice@example.com")
                .withAvailabilities("TUESDAY,13:00,18:00").build();
        // Matches Monday availability but wrong name
        Person matchesAvailOnly = new PersonBuilder().withName("Bob")
                .withPhone("92222222").withEmail("bob@example.com")
                .withAvailabilities("MONDAY,13:00,18:00").build();
        ab.addPerson(matchesNameOnly);
        ab.addPerson(matchesAvailOnly);

        Model combinedModel = new ModelManager(ab, new UserPrefs());
        Model expectedCombinedModel = new ModelManager(ab, new UserPrefs());

        VolunteerAvailability query = VolunteerAvailability.fromString("MONDAY,14:00,17:00");
        PersonContainsKeywordsPredicate textPredicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList("Alice"));
        PersonAvailableDuringPredicate availPredicate = new PersonAvailableDuringPredicate(query);
        CombinedAndPersonPredicate combinedPredicate =
                new CombinedAndPersonPredicate(List.of(textPredicate, availPredicate));
        FindCommand command = new FindCommand(combinedPredicate);

        expectedCombinedModel.updateFilteredKeptPersonList(combinedPredicate);
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        assertCommandSuccess(command, combinedModel, PersonListView.KEPT_PERSONS,
                expectedMessage, PersonListView.KEPT_PERSONS, expectedCombinedModel);
        assertEquals(Collections.emptyList(), combinedModel.getFilteredKeptPersonList());
    }

    @Test
    public void toStringMethod() {
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(Arrays.asList(KEYWORD_TOKEN));
        FindCommand findCommand = new FindCommand(predicate);
        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code PersonContainsKeywordsPredicate}.
     */
    private PersonContainsKeywordsPredicate preparePredicate(String userInput) {
        return new PersonContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
