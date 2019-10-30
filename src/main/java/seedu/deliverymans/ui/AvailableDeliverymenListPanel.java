package seedu.deliverymans.ui;

import static seedu.deliverymans.ui.MainWindow.*;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.deliverymans.commons.core.LogsCenter;
import seedu.deliverymans.model.deliveryman.Deliveryman;

/**
 * Panel containing the list of deliverymen.
 */
public class AvailableDeliverymenListPanel extends UiPart<Region> implements Initializable {
    private static final String FXML = "AvailableDeliverymenListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(DeliverymanListPanel.class);

    @FXML
    Label statusLabel;
    @FXML
    Button availableButton;
    @FXML
    Button unavailableButton;
    @FXML
    Button deliveringButton;

    ObservableList<Deliveryman> availableList;
    ObservableList<Deliveryman> unavailableList;
    ObservableList<Deliveryman> deliveringList;

    @javafx.fxml.FXML
    private ListView<Deliveryman> availableDeliverymanListView;

    public AvailableDeliverymenListPanel(ObservableList<Deliveryman> availableList,
                                         ObservableList<Deliveryman> unavailableList,
                                         ObservableList<Deliveryman> deliveringList) {
        super(FXML);
        this.availableList = availableList;
        this.unavailableList = unavailableList;
        this.deliveringList = deliveringList;
        availableDeliverymanListView.setItems(availableList);
        availableDeliverymanListView.setCellFactory(listView -> new DeliverymanListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Deliveryman} using a {@code DeliverymanCard}.
     */
    class DeliverymanListViewCell extends ListCell<Deliveryman> {
        @Override
        protected void updateItem(Deliveryman deliveryman, boolean empty) {
            super.updateItem(deliveryman, empty);

            if (empty || deliveryman == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new DeliverymanCard(deliveryman, getIndex() + 1).getRoot());
            }
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        availableButton.setOnAction(e -> {
            statusLabel.setText("AVAILABLE DELIVERYMEN");
            availableDeliverymanListView.setItems(availableList);
            availableDeliverymanListView.setCellFactory(listView -> new DeliverymanListViewCell());
        });
        unavailableButton.setOnAction(e -> {
            statusLabel.setText("UNAVAILABLE DELIVERYMEN");
            //statusLabel.setTextFill(Color.web("#0076a3"));
            availableDeliverymanListView.setItems(unavailableList);
            availableDeliverymanListView.setCellFactory(listView -> new DeliverymanListViewCell());
        });
        deliveringButton.setOnAction(e -> {
            statusLabel.setText("DELIVERING DELIVERYMEN");
            availableDeliverymanListView.setItems(deliveringList);
            availableDeliverymanListView.setCellFactory(listView -> new DeliverymanListViewCell());
        });
    }

}
