package seedu.deliverymans.logic.commands.customer;

import seedu.address.model.Model;
import seedu.deliverymans.logic.commands.exceptions.CommandException;
import seedu.deliverymans.logic.commands.Command;
import seedu.deliverymans.logic.commands.CommandResult;

public class HistoryCommand extends Command {
    public static final String COMMAND_WORD = "history";

    public HistoryCommand(String arguments) {

    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        return null;
    }
}
