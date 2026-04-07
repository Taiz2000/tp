package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.PersonListView;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

/**
 * Reused from Codex suggestions upon providing specifications
 */
public class AliasesCommandTest {

    @Test
    public void execute_noAliases_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new AliasesCommand(), model, PersonListView.KEPT_PERSONS,
                AliasesCommand.MESSAGE_NO_ALIASES, PersonListView.KEPT_PERSONS, expectedModel);
        assertCommandSuccess(new AliasesCommand(), model, PersonListView.DELETED_PERSONS,
                AliasesCommand.MESSAGE_NO_ALIASES, PersonListView.DELETED_PERSONS, expectedModel);
    }

    @Test
    public void execute_multipleAliases_success() {
        Model model = new ModelManager();
        model.setCommandAlias("rm", DeleteCommand.COMMAND_WORD);
        model.setCommandAlias("ls", ListCommand.COMMAND_WORD);

        Model expectedModel = new ModelManager();
        expectedModel.setCommandAlias("rm", DeleteCommand.COMMAND_WORD);
        expectedModel.setCommandAlias("ls", ListCommand.COMMAND_WORD);

        String expectedMessage = AliasesCommand.MESSAGE_ALIASES_HEADER
                + "\nls -> " + ListCommand.COMMAND_WORD
                + "\nrm -> " + DeleteCommand.COMMAND_WORD;

        assertCommandSuccess(new AliasesCommand(), model, PersonListView.KEPT_PERSONS,
                expectedMessage, PersonListView.KEPT_PERSONS, expectedModel);
        assertCommandSuccess(new AliasesCommand(), model, PersonListView.DELETED_PERSONS,
                expectedMessage, PersonListView.DELETED_PERSONS, expectedModel);
    }
}
