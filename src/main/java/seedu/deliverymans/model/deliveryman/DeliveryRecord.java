package seedu.deliverymans.model.deliveryman;

import java.time.LocalDateTime;
import java.util.Objects;

import seedu.deliverymans.model.Name;

/**
 * Represents the delivery record of a deliveryman.
 */
public class DeliveryRecord {

    private Name name;
    private LocalDateTime timeJoined;
    private int noOrdersCompleted;
    private int noOrdersUncompleted;

    public DeliveryRecord(Name name) {
        this.name = name;
        timeJoined = LocalDateTime.now();
        noOrdersCompleted = 0;
    }

    public Name getName() {
        return name;
    }

    public LocalDateTime getTimeJoined() {
        return timeJoined;
    }

    public int getNoOrdersCompleted() {
        return noOrdersCompleted;
    }

    public int getNoOrdersUncompleted() {
        return noOrdersUncompleted;
    }

    /**
     * Returns true if both persons have the same identity. Data fields need not be same.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeliveryRecord)) {
            return false;
        }

        DeliveryRecord otherRecord = (DeliveryRecord) other;
        return otherRecord.getName().equals(getName())
                && otherRecord.getTimeJoined().equals(getTimeJoined())
                && otherRecord.getNoOrdersCompleted() == getNoOrdersCompleted();

    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" Name: ")
                .append(getName() + "\n")
                .append(" Time Joined")
                .append(getTimeJoined() + "\n")
                .append(" No.completed orders: ")
                .append(getNoOrdersCompleted());
        return builder.toString();
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, timeJoined, noOrdersCompleted);
    }

}
