package seedu.deliverymans.model.food;

import static seedu.deliverymans.commons.util.AppUtil.checkArgument;
import static seedu.deliverymans.commons.util.CollectionUtil.requireAllNonNull;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import seedu.deliverymans.model.Name;
import seedu.deliverymans.model.Tag;

/**
 * Represents a Food in a restaurant.
 */
public class Food {

    public static final String PRICE_CONSTRAINTS = "Price should not be negative";
    public static final String PREP_CONSTRAINTS = "Preparation time should not be negative";

    private final Name name;
    private final BigDecimal price;
    private final Duration prepTime;
    private final Set<Tag> tags;

    /**
     * Constructs a {@code Food}.
     *
     * @param name A valid food name.
     * @param price A non-negative price.
     * @param prepTime A non-negative preparation time in seconds.
     * @param tags Tags of the food.
     */
    public Food(Name name, BigDecimal price, Duration prepTime, Set<Tag> tags) {
        requireAllNonNull(name, price, prepTime);
        checkArgument(isValidPrice(price), PRICE_CONSTRAINTS);
        checkArgument(isValidPrepTime(prepTime), PREP_CONSTRAINTS);
        this.name = name;
        this.price = price;
        this.prepTime = prepTime;
        this.tags = Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if a given number is a valid price.
     */
    public static boolean isValidPrice(BigDecimal test) {
        return test.compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * Returns true if a given duration is a valid preparation time.
     */
    public static boolean isValidPrepTime(Duration prepTime) {
        return !prepTime.isNegative();
    }

    public Name getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Duration getPrepTime() {
        return prepTime;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public String getDisplayPrice() {
        return NumberFormat.getCurrencyInstance(Locale.US).format(getPrice());
    }

    public String getDisplayPrepTime() {
        long seconds = getPrepTime().getSeconds();
        return seconds / 60 + "m " + seconds % 60 + "s";
    }

    /**
     * Returns true if both food are of the same name.
     * This defines a weaker notion of equality between two food.
     */
    public boolean isSameFood(Food otherFood) {
        if (otherFood == this) {
            return true;
        }

        return otherFood != null
                && otherFood.getName().equals(getName());
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof Food)) {
            return false;
        }

        // state check
        Food other = (Food) obj;
        return name.equals(other.name)
                && price.compareTo(other.price) == 0
                && prepTime == other.prepTime
                && tags.equals(other.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, prepTime, tags);
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Price: ")
                .append(getDisplayPrice())
                .append(" Preparation time: ")
                .append(getDisplayPrepTime())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}
