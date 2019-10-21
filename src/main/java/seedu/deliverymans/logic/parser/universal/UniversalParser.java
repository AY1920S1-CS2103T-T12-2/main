package seedu.deliverymans.logic.parser.universal;

import static seedu.deliverymans.commons.core.Messages.MESSAGE_ALREADY_IN_CONTEXT;
import static seedu.deliverymans.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.deliverymans.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.deliverymans.logic.commands.Command;
import seedu.deliverymans.logic.commands.universal.AddOrderCommand;
import seedu.deliverymans.logic.commands.universal.ContextCommand;
import seedu.deliverymans.logic.commands.universal.DeleteOrderCommand;
import seedu.deliverymans.logic.commands.universal.EditOrderCommand;
import seedu.deliverymans.logic.commands.universal.ExitCommand;
import seedu.deliverymans.logic.commands.universal.HelpCommand;
import seedu.deliverymans.logic.commands.universal.RedoCommand;
import seedu.deliverymans.logic.commands.universal.SummaryCommand;
import seedu.deliverymans.logic.commands.universal.UndoCommand;
import seedu.deliverymans.logic.parser.customer.CustomerParser;
import seedu.deliverymans.logic.parser.deliveryman.DeliverymanParser;
import seedu.deliverymans.logic.parser.exceptions.ParseException;
import seedu.deliverymans.logic.parser.restaurant.EditingParser;
import seedu.deliverymans.logic.parser.restaurant.RestaurantParser;

/**
 * (To be added)
 */
public class UniversalParser {
    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private static Context currentContext = Context.GLOBAL;

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        Context nextContext;
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {
        case AddOrderCommand.COMMAND_WORD:
            return new AddOrderCommandParser().parse(arguments);

        case EditOrderCommand.COMMAND_WORD:
            return new EditOrderCommandParser().parse(arguments);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case DeleteOrderCommand.COMMAND_WORD:
            return new DeleteOrderCommandParser().parse(arguments);

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand(arguments);

        case SummaryCommand.COMMAND_WORD:
            return new SummaryCommand();

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        case RedoCommand.COMMAND_WORD:
            return new RedoCommand();

        case CustomerParser.COMMAND_WORD:
            nextContext = Context.CUSTOMER;
            checkContext(nextContext);
            currentContext = nextContext;
            return new ContextCommand(nextContext);

        case DeliverymanParser.COMMAND_WORD:
            nextContext = Context.DELIVERYMEN;
            checkContext(nextContext);
            currentContext = nextContext;
            return new ContextCommand(nextContext);

        case RestaurantParser.COMMAND_WORD:
            nextContext = Context.RESTAURANT;
            checkContext(nextContext);
            currentContext = nextContext;
            return new ContextCommand(nextContext);

        default:
            switch (currentContext) {
            case CUSTOMER:
                return new CustomerParser().parseCommand(userInput);
            case RESTAURANT:
                Command command = new RestaurantParser().parseCommand(userInput);
                if (commandWord.equals("edit")) {
                    currentContext = Context.EDITING;
                }
                return command;
            case DELIVERYMEN:
                return new DeliverymanParser().parseCommand(userInput);
            case EDITING:
                return new EditingParser().parseCommand(userInput);
            default:
                throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
            }
        }
    }

    private static void checkContext(Context nextContext) throws ParseException {
        if (currentContext == nextContext) {
            throw new ParseException(String.format(MESSAGE_ALREADY_IN_CONTEXT, nextContext.toLowerCaseString()));
        }
    }
}
