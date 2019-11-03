package seedu.deliverymans.logic.commands.universal;

import static java.util.Objects.requireNonNull;
import static seedu.deliverymans.logic.parser.CliSyntax.PREFIX_CUSTOMER;
import static seedu.deliverymans.logic.parser.CliSyntax.PREFIX_DELIVERYMAN;
import static seedu.deliverymans.logic.parser.CliSyntax.PREFIX_RESTAURANT;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import seedu.deliverymans.commons.core.Messages;
import seedu.deliverymans.commons.core.index.Index;
import seedu.deliverymans.commons.util.CollectionUtil;
import seedu.deliverymans.logic.Logic;
import seedu.deliverymans.logic.commands.Command;
import seedu.deliverymans.logic.commands.CommandResult;
import seedu.deliverymans.logic.commands.exceptions.CommandException;
import seedu.deliverymans.model.Model;
import seedu.deliverymans.model.Name;
import seedu.deliverymans.model.order.Order;

/**
 * Edits the details of an existing order.
 */
public class EditOrderCommand extends Command {
    public static final String COMMAND_WORD = "edit_order";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edit an order already present in the manager. "
            + "Parameters: "
            + "[" + PREFIX_CUSTOMER + "CUSTOMER]\n"
            + "[" + PREFIX_RESTAURANT + "RESTAURANT]\n"
            + "[" + PREFIX_DELIVERYMAN + "DELIVERYMAN]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_CUSTOMER + "Sam "
            + PREFIX_RESTAURANT + "KFC "
            + PREFIX_DELIVERYMAN + "Deliveryman #1337";

    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_INVALID_FOOD_FORMAT = "The quantities of food ordered must be provided.";
    public static final String MESSAGE_SUCCESS_EDIT = "Successful edition of order: %1$s";

    private final Index index;
    private final EditOrderDescriptor editOrderDescriptor;

    /**
     * @param index               of the order in the filtered order list to edit
     * @param editOrderDescriptor details to edit the order with
     */
    public EditOrderCommand(Index index, EditOrderDescriptor editOrderDescriptor) {
        requireNonNull(index);
        requireNonNull(editOrderDescriptor);

        this.index = index;
        this.editOrderDescriptor = new EditOrderDescriptor(editOrderDescriptor);
    }

    @Override
    public CommandResult execute(Model model, Logic logic) throws CommandException {
        requireNonNull(model);
        List<Order> lastShownList = model.getFilteredOrderList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ORDER_DISPLAYED_INDEX);
        }

        Order orderToEdit = lastShownList.get(index.getZeroBased());
        Order editedOrder = createEditedOrder(orderToEdit, editOrderDescriptor);

        // model.updateFilteredOrderList(PREDICATE_SHOW_ALL_ORDERS);
        new AddOrderCommand(editedOrder, orderToEdit, false).execute(model, logic);
        return new CommandResult(String.format(MESSAGE_SUCCESS_EDIT, editedOrder));
    }

    /**
     * Creates and returns a {@code OrderBuilder} with the details of {@code orderToEdit}
     * edited with {@code editOrderDescriptor}.
     */
    private static Order createEditedOrder(Order orderToEdit, EditOrderDescriptor editOrderDescriptor) {
        assert orderToEdit != null;
        Name updatedCustomer = editOrderDescriptor.getCustomer().orElse(orderToEdit.getCustomer());
        Name updatedRestaurant = editOrderDescriptor.getRestaurant().orElse(orderToEdit.getRestaurant());
        Name updatedDeliveryman = editOrderDescriptor.getDeliveryman().orElse(orderToEdit.getDeliveryman());
        boolean updatedIsCompleted = editOrderDescriptor.getCompleted().orElse(orderToEdit.isCompleted());
        Map<Name, Integer> updatedFood = editOrderDescriptor.getFoods().orElse(orderToEdit.getFoodList());

        Order order = new Order.OrderBuilder().setCustomer(updatedCustomer).setRestaurant(updatedRestaurant)
                .setDeliveryman(updatedDeliveryman).setFood(updatedFood).setCompleted(updatedIsCompleted)
                .completeOrder();
        return order;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditOrderCommand)) {
            return false;
        }

        // state check
        EditOrderCommand e = (EditOrderCommand) other;
        return index == e.index
                && editOrderDescriptor.equals(e.editOrderDescriptor);
    }

    /**
     * Stores the details to edit the order with. Each non-empty field value will replace the
     * corresponding field value of the order.
     */
    public static class EditOrderDescriptor {
        private Name customer;
        private Name restaurant;
        private Name deliveryman;
        private Boolean isCompleted;
        private Map<Name, Integer> foods;

        public EditOrderDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditOrderDescriptor(EditOrderDescriptor toCopy) {
            setCustomer(toCopy.customer);
            setRestaurant(toCopy.restaurant);
            setDeliveryman(toCopy.deliveryman);
            setCompleted(toCopy.isCompleted);
            setFoods(toCopy.foods);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(customer, restaurant, deliveryman, foods);
        }

        public void setCustomer(Name customer) {
            this.customer = customer;
        }

        public Optional<Name> getCustomer() {
            return Optional.ofNullable(customer);
        }

        public void setRestaurant(Name restaurant) {
            this.restaurant = restaurant;
        }

        public Optional<Name> getRestaurant() {
            return Optional.ofNullable(restaurant);
        }

        public void setDeliveryman(Name deliveryman) {
            this.deliveryman = deliveryman;
        }

        public Optional<Name> getDeliveryman() {
            return Optional.ofNullable(deliveryman);
        }

        public void setCompleted(boolean isCompleted) {
            this.isCompleted = isCompleted;
        }

        public Optional<Boolean> getCompleted() {
            return Optional.ofNullable(isCompleted);
        }

        /**
         * Sets {@code tags} to this object's {@code food}.
         * A defensive copy of {@code food} is used internally.
         */
        public void setFoods(Map<Name, Integer> foods) {
            this.foods = (foods != null) ? new HashMap<>(foods) : null;
        }

        /**
         * Returns an unmodifiable food set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code food} is null.
         */
        public Optional<Map<Name, Integer>> getFoods() {
            return (foods != null) ? Optional.of(Collections.unmodifiableMap(foods)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditOrderDescriptor)) {
                return false;
            }

            // state check
            EditOrderDescriptor e = (EditOrderDescriptor) other;

            return getCustomer().equals(e.getCustomer())
                    && getRestaurant().equals(e.getRestaurant())
                    && getDeliveryman().equals(e.getDeliveryman())
                    && (getCompleted() == e.getCompleted())
                    && getFoods().equals(e.getFoods());
        }
    }
}