package seedu.deliverymans.logic.commands.deliveryman;

import seedu.deliverymans.logic.commands.Command;
import seedu.deliverymans.logic.commands.CommandResult;
import seedu.deliverymans.logic.commands.exceptions.CommandException;
import seedu.deliverymans.model.Model;

/**
 * (To be added)
 */
public class SortCommand extends Command {
    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_SORT_SUCCESS = "Deliverymen sorted.";

    public SortCommand(String arguments) {

    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        return null;
    }
}