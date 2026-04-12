package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalPersons;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.PersonListView;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.sort.PersonSortComparator;
import seedu.address.model.person.sort.SortAttribute;
import seedu.address.model.person.sort.SortOrder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    /* This is the only test where the ListCommand is executed while viewing deleted persons.
     *
     * It is expected that the logical flow of ListCommand is identical to the case where the
     * user views kept persons, with the only difference being that the viewed list switches to
     * the list of kept persons after execution. This test aims to check that difference.
     *
     * For more robust integration testing of ListCommand with the Model,
     * refer to the tests which are executed while viewing kept persons.
     */
    @Test
    public void execute_viewingDeletedPersons_switchesToKeptPersons() {
        assertCommandSuccess(new ListCommand(), model, PersonListView.DELETED_PERSONS,
                ListCommand.MESSAGE_SUCCESS, PersonListView.KEPT_PERSONS, expectedModel);
        assertEquals(getTypicalPersons(), model.getFilteredKeptPersonList());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListCommand(), model, PersonListView.KEPT_PERSONS,
                ListCommand.MESSAGE_SUCCESS, PersonListView.KEPT_PERSONS, expectedModel);
        assertEquals(getTypicalPersons(), model.getFilteredKeptPersonList());
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        assertCommandSuccess(new ListCommand(), model, PersonListView.KEPT_PERSONS,
                ListCommand.MESSAGE_SUCCESS, PersonListView.KEPT_PERSONS, expectedModel);
        assertEquals(getTypicalPersons(), model.getFilteredKeptPersonList());
    }

    @Test
    public void execute_listWithoutSort_clearsSorting() {
        model.updateSortedPersonList(new PersonSortComparator(SortAttribute.NAME, SortOrder.DESC));
        assertEquals(sortedTypicalPersons(SortAttribute.NAME, SortOrder.DESC), model.getFilteredKeptPersonList());
        assertCommandSuccess(new ListCommand(), model, PersonListView.KEPT_PERSONS,
                ListCommand.MESSAGE_SUCCESS, PersonListView.KEPT_PERSONS, expectedModel);
        assertEquals(getTypicalPersons(), model.getFilteredKeptPersonList());
    }

    @Test
    public void execute_listWithNameAsc_sortsList() {
        ListCommand command = new ListCommand(SortAttribute.NAME, SortOrder.ASC);
        expectedModel.updateFilteredKeptPersonList(PREDICATE_SHOW_ALL_PERSONS);
        expectedModel.updateSortedPersonList(new PersonSortComparator(SortAttribute.NAME, SortOrder.ASC));
        assertCommandSuccess(command, model, PersonListView.KEPT_PERSONS,
                String.format(ListCommand.MESSAGE_SUCCESS_SORTED, "name", "asc"),
                PersonListView.KEPT_PERSONS, expectedModel);
        assertEquals(sortedTypicalPersons(SortAttribute.NAME, SortOrder.ASC), model.getFilteredKeptPersonList());
    }

    @Test
    public void execute_listWithEmailDesc_sortsList() {
        ListCommand command = new ListCommand(SortAttribute.EMAIL, SortOrder.DESC);
        expectedModel.updateFilteredKeptPersonList(PREDICATE_SHOW_ALL_PERSONS);
        expectedModel.updateSortedPersonList(new PersonSortComparator(SortAttribute.EMAIL, SortOrder.DESC));
        assertCommandSuccess(command, model, PersonListView.KEPT_PERSONS,
                String.format(ListCommand.MESSAGE_SUCCESS_SORTED, "email", "desc"),
                PersonListView.KEPT_PERSONS, expectedModel);
        assertEquals(sortedTypicalPersons(SortAttribute.EMAIL, SortOrder.DESC), model.getFilteredKeptPersonList());
    }

    @Test
    public void execute_listWithAddressAsc_sortsList() {
        ListCommand command = new ListCommand(SortAttribute.ADDRESS, SortOrder.ASC);
        expectedModel.updateFilteredKeptPersonList(PREDICATE_SHOW_ALL_PERSONS);
        expectedModel.updateSortedPersonList(new PersonSortComparator(SortAttribute.ADDRESS, SortOrder.ASC));
        assertCommandSuccess(command, model, PersonListView.KEPT_PERSONS,
                String.format(ListCommand.MESSAGE_SUCCESS_SORTED, "address", "asc"),
                PersonListView.KEPT_PERSONS, expectedModel);
        assertEquals(sortedTypicalPersons(SortAttribute.ADDRESS, SortOrder.ASC), model.getFilteredKeptPersonList());
    }

    @Test
    public void execute_listWithSortWhileFiltered_showsAllSorted() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        ListCommand command = new ListCommand(SortAttribute.EMAIL, SortOrder.DESC);
        expectedModel.updateFilteredKeptPersonList(PREDICATE_SHOW_ALL_PERSONS);
        expectedModel.updateSortedPersonList(new PersonSortComparator(SortAttribute.EMAIL, SortOrder.DESC));
        assertCommandSuccess(command, model, PersonListView.KEPT_PERSONS,
                String.format(ListCommand.MESSAGE_SUCCESS_SORTED, "email", "desc"),
                PersonListView.KEPT_PERSONS, expectedModel);
        assertEquals(sortedTypicalPersons(SortAttribute.EMAIL, SortOrder.DESC), model.getFilteredKeptPersonList());
    }

    @Test
    public void equals() {
        ListCommand listCommand = new ListCommand();
        ListCommand listCommandCopy = new ListCommand();
        ListCommand listByNameAsc = new ListCommand(SortAttribute.NAME, SortOrder.ASC);
        ListCommand listByNameDesc = new ListCommand(SortAttribute.NAME, SortOrder.DESC);
        ListCommand listByEmailAsc = new ListCommand(SortAttribute.EMAIL, SortOrder.ASC);

        assertTrue(listCommand.equals(listCommand));
        assertTrue(listCommand.equals(listCommandCopy));
        assertFalse(listCommand.equals(null));
        assertFalse(listCommand.equals(1));
        assertFalse(listCommand.equals(listByNameAsc));
        assertFalse(listByNameAsc.equals(listByNameDesc));
        assertFalse(listByNameAsc.equals(listByEmailAsc));
    }

    @Test
    public void toStringMethod() {
        ListCommand listCommand = new ListCommand(SortAttribute.NAME, SortOrder.DESC);
        String expected = ListCommand.class.getCanonicalName() + "{sortAttribute=" + SortAttribute.NAME
                + ", sortOrder=" + SortOrder.DESC + "}";
        assertEquals(expected, listCommand.toString());
    }

    private List<Person> sortedTypicalPersons(SortAttribute attribute, SortOrder order) {
        return sortedPersons(getTypicalPersons(), attribute, order);
    }

    private List<Person> sortedPersons(List<Person> persons, SortAttribute attribute, SortOrder order) {
        List<Person> expected = new ArrayList<>(persons);
        expected.sort(new PersonSortComparator(attribute, order));
        return expected;
    }
}
