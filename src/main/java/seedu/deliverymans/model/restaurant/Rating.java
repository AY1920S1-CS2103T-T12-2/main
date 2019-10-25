package seedu.deliverymans.model.restaurant;

import static java.util.Objects.requireNonNull;
import static seedu.deliverymans.commons.util.AppUtil.checkArgument;

/**
 * Represents a Rating
 */
public class Rating {

    public static final String MESSAGE_CONSTRAINTS =
            "Rating should be an integer from 0 to 5";
    public static final String VALIDATION_REGEX = "\\d{1}";

    public final String numberOfRatings;
    public final String rating;

    /**
     * Constructs a {@code Rating}.
     *
     * @param rating A valid rating.
     */
    public Rating(String rating) {
        requireNonNull(rating);
        checkArgument(isValidRating(rating), MESSAGE_CONSTRAINTS);
        this.rating = rating;
        this.numberOfRatings = "0";
    }

    /**
     * Constructs a {@code Rating}.
     *
     * @param rating A valid rating
     * @param numberOfRatings
     */
    public Rating(String rating, String numberOfRatings) {
        requireNonNull(rating, numberOfRatings);
        checkArgument(isValidRating(rating), MESSAGE_CONSTRAINTS);
        this.rating = rating;
        this.numberOfRatings = numberOfRatings;
    }

    /**
     * Returns true if a given string is a valid rating.
     */
    public static boolean isValidRating(String rating) {
        boolean matchRegex = rating.matches(VALIDATION_REGEX);

        if (matchRegex) {
            double value = getRatingValue(rating);
            boolean matchRange = (value <= 5) && (value >= 0);
            return matchRange;
        } else {
            return false;
        }
    }

    public static double getRatingValue(String rating) {
        return Double.parseDouble(rating);
    }

    @Override
    public String toString() {
        return rating;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Rating // instanceof handles nulls
                && rating.equals(((Rating) other).rating)); // state check
    }

    @Override
    public int hashCode() {
        return rating.hashCode();
    }
}
