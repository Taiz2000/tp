package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;

/**
 * Reused from Codex suggestions upon providing specifications
 */
public class AliasCommandTest {

    @Test
    public void constructor_invalidAliasName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, AliasCommand.MESSAGE_INVALID_ALIAS_NAME, ()
                -> new AliasCommand("LS", "list"));
    }

    @Test
    public void constructor_reservedAliasName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, AliasCommand.MESSAGE_RESERVED_ALIAS_NAME, ()
                -> new AliasCommand(ListCommand.COMMAND_WORD, HelpCommand.COMMAND_WORD));
    }

    @Test
    public void constructor_invalidAliasTemplate_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, AliasCommand.MESSAGE_INVALID_ALIAS_TEMPLATE, ()
                -> new AliasCommand("l", "ls"));
    }

    @Test
    public void constructor_reservedAliasTarget_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, AliasCommand.MESSAGE_RESERVED_ALIAS_TARGET, ()
                -> new AliasCommand("aa", AliasCommand.COMMAND_WORD));
    }

    @Test
    public void execute_validAlias_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();
        expectedModel.setCommandAlias("ls", ListCommand.COMMAND_WORD);

        assertCommandSuccess(new AliasCommand("ls", ListCommand.COMMAND_WORD), model,
                String.format(AliasCommand.MESSAGE_SUCCESS, "ls", ListCommand.COMMAND_WORD), expectedModel);
    }

    @Test
    public void execute_validAliasToClear_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();
        expectedModel.setCommandAlias("wipe", ClearCommand.COMMAND_WORD);

        assertCommandSuccess(new AliasCommand("wipe", ClearCommand.COMMAND_WORD), model,
                String.format(AliasCommand.MESSAGE_SUCCESS, "wipe", ClearCommand.COMMAND_WORD), expectedModel);
    }

    @Test
    public void execute_duplicateAlias_throwsCommandException() {
        Model model = new ModelManager();
        model.setCommandAlias("ls", ListCommand.COMMAND_WORD);

        assertCommandFailure(new AliasCommand("ls", HelpCommand.COMMAND_WORD), model,
                AliasCommand.MESSAGE_DUPLICATE_ALIAS);
    }

    @Test
    public void isValidAliasTemplate() {
        assertTrue(AliasCommand.isValidAliasTemplate("list"));
        assertTrue(AliasCommand.isValidAliasTemplate("clear"));
        assertTrue(AliasCommand.isValidAliasTemplate("editprev"));
        assertFalse(AliasCommand.isValidAliasTemplate("ls"));
        assertFalse(AliasCommand.isValidAliasTemplate("find m/ss meie"));
        assertFalse(AliasCommand.isValidAliasTemplate(" "));
    }

    @Test
    public void isAllowedAliasTarget() {
        assertTrue(AliasCommand.isAllowedAliasTarget("list"));
        assertTrue(AliasCommand.isAllowedAliasTarget("clear"));
        assertTrue(AliasCommand.isAllowedAliasTarget(BinCommand.COMMAND_WORD));
        assertTrue(AliasCommand.isAllowedAliasTarget(StatsCommand.COMMAND_WORD));
        assertFalse(AliasCommand.isAllowedAliasTarget(AliasCommand.COMMAND_WORD));
        assertFalse(AliasCommand.isAllowedAliasTarget(AliasesCommand.COMMAND_WORD));
        assertFalse(AliasCommand.isAllowedAliasTarget(UnaliasCommand.COMMAND_WORD));
        assertFalse(AliasCommand.isAllowedAliasTarget(EditPreviousCommand.COMMAND_WORD));
    }

    @Test
    public void equals() {
        AliasCommand firstCommand = new AliasCommand("ls", ListCommand.COMMAND_WORD);
        AliasCommand secondCommand = new AliasCommand("rm", DeleteCommand.COMMAND_WORD);

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(new AliasCommand("ls", ListCommand.COMMAND_WORD)));
        assertFalse(firstCommand.equals(1));
        assertFalse(firstCommand.equals(null));
        assertFalse(firstCommand.equals(secondCommand));
    }

    @Test
    public void toStringMethod() {
        AliasCommand aliasCommand = new AliasCommand("ls", ListCommand.COMMAND_WORD);
        String expected = AliasCommand.class.getCanonicalName() + "{shortName=ls, targetCommandWord=list}";
        assertEquals(expected, aliasCommand.toString());
    }
}
