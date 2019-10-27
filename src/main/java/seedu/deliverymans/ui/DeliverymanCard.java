package seedu.deliverymans.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import seedu.deliverymans.model.deliveryman.Deliveryman;

/**
 * An UI component that displays information of a {@code Deliveryman}.
 */
public class DeliverymanCard extends UiPart<Region> {

    private static final String FXML = "DeliverymanListCard.fxml";

    public final Deliveryman deliveryman;

    @javafx.fxml.FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private FlowPane tags;
    @FXML
    private Label statusTag;

    public DeliverymanCard(Deliveryman deliveryman, int displayedIndex) {
        super(FXML);
        this.deliveryman = deliveryman;
        id.setText(displayedIndex + ". ");
        name.setText(deliveryman.getName().fullName);
        phone.setText(deliveryman.getPhone().value);
        deliveryman.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        statusTag.setText(deliveryman.getStatus().getDescription());
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeliverymanCard)) {
            return false;
        }

        // state check
        DeliverymanCard card = (DeliverymanCard) other;
        return id.getText().equals(card.id.getText())
                && deliveryman.equals(card.deliveryman);
    }
}
