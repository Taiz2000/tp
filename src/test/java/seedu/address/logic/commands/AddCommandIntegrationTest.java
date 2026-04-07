package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.PersonListView;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_notViewingKeptPersons_throwsCommandException() {
        Person validPerson = new PersonBuilder().build();
        AddCommand addCommand = new AddCommand(validPerson);

        assertCommandFailure(addCommand, model, PersonListView.DELETED_PERSONS,
                Messages.MESSAGE_NOT_VIEWING_KEPT_PERSONS);
    }

    @Test
    public void execute_newPerson_success() {
        Person validPerson = new PersonBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(validPerson);

        assertCommandSuccess(new AddCommand(validPerson), model,
                PersonListView.KEPT_PERSONS,
                String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPerson)),
                PersonListView.KEPT_PERSONS,
                expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person personInList = model.getAddressBook().getKeptPersonList().get(0);
        assertCommandFailure(new AddCommand(personInList), model, PersonListView.KEPT_PERSONS,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonByEmailIgnoringCase_throwsCommandException() {
        Person personInList = model.getAddressBook().getKeptPersonList().get(0);
        Person duplicateByEmail = new PersonBuilder(personInList)
                .withName("Noah Lim")
                .withPhone("80001111")
                .withEmail(personInList.getEmail().value.toUpperCase())
                .build();

        assertCommandFailure(new AddCommand(duplicateByEmail), model, PersonListView.KEPT_PERSONS,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_sameNameDifferentPhoneAndEmail_success() {
        Person personInList = model.getAddressBook().getKeptPersonList().get(0);
        Person sameNameDifferentContacts = new PersonBuilder(personInList)
                .withPhone("80002222")
                .withEmail("alice.pauline.alt@example.com")
                .build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(sameNameDifferentContacts);

        assertCommandSuccess(new AddCommand(sameNameDifferentContacts), model,
                PersonListView.KEPT_PERSONS,
                String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(sameNameDifferentContacts)),
                PersonListView.KEPT_PERSONS,
                expectedModel);
    }

    @Test
    public void execute_duplicatePersonByPhone_throwsCommandException() {
        Person personInList = model.getAddressBook().getKeptPersonList().get(0);
        Person duplicateByPhone = new PersonBuilder(personInList)
                .withName("Sarah Teo")
                .withEmail("sarah.teo@example.com")
                .build();

        assertCommandFailure(new AddCommand(duplicateByPhone), model, PersonListView.KEPT_PERSONS,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonByPhoneAndEmail_throwsCommandException() {
        Person personInList = model.getAddressBook().getKeptPersonList().get(0);
        Person duplicateByBoth = new PersonBuilder(personInList)
                .withName("Ethan Chua")
                .withAddress("39 Yishun Ring Rd")
                .withNotes("Experienced event runner")
                .build();

        assertCommandFailure(new AddCommand(duplicateByBoth), model, PersonListView.KEPT_PERSONS,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

}
