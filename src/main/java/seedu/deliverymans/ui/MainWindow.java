package seedu.deliverymans.ui;

import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import seedu.deliverymans.commons.core.GuiSettings;
import seedu.deliverymans.commons.core.LogsCenter;
import seedu.deliverymans.logic.Logic;
import seedu.deliverymans.logic.commands.CommandResult;
import seedu.deliverymans.logic.commands.exceptions.CommandException;
import seedu.deliverymans.logic.parser.exceptions.ParseException;
import seedu.deliverymans.logic.parser.universal.Context;
import seedu.deliverymans.model.restaurant.Restaurant;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    private Context currentContext;

    // Independent Ui parts residing in this Ui container
    private PersonListPanel personListPanel;
    private CustomerListPanel customerListPanel;
    private DeliverymanListPanel deliverymanListPanel;
    private AvailableDeliverymenListPanel availableDeliverymenListPanel;
    private UnavailableDeliverymenListPanel unavailableDeliverymenListPanel;
    private RestaurantListPanel restaurantListPanel;
    private OrderListPanel orderListPanel;
    private FoodListPanel foodListPanel;
    private ResultDisplay resultDisplay;
    private StatisticsDisplay statisticsDisplay;
    private HelpWindow helpWindow;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane editingRestaurantPlaceholder;

    @FXML
    private StackPane listPanelPlaceholder;

    @FXML
    private StackPane statisticsPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;
        this.currentContext = null;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     *
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        // orderListPanel = new OrderListPanel(logic.getFilteredOrderList());
        // listPanelPlaceholder.getChildren().add(orderListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    /**
     * Changes context of the system depending on {@code context}
     */
    private void changeDisplay(Context context) {

        editingRestaurantPlaceholder.setPrefHeight(0);
        editingRestaurantPlaceholder.setMinHeight(0);
        if (statisticsPlaceholder.getChildren().size() > 0) {
            statisticsPlaceholder.getChildren().remove(0);
        }

        switch (context) {
        case CUSTOMER:
            customerListPanel = new CustomerListPanel(logic.getFilteredCustomerList());
            listPanelPlaceholder.getChildren().add(customerListPanel.getRoot());
            break;
        case DELIVERYMEN:
            deliverymanListPanel = new DeliverymanListPanel(logic.getFilteredDeliverymenList());
            listPanelPlaceholder.getChildren().add(deliverymanListPanel.getRoot());
            availableDeliverymenListPanel = new AvailableDeliverymenListPanel(logic.getFilteredStatusList());
            statisticsPlaceholder.getChildren().add(availableDeliverymenListPanel.getRoot());
            break;
        case RESTAURANT:
            restaurantListPanel = new RestaurantListPanel(logic.getFilteredRestaurantList());
            listPanelPlaceholder.getChildren().add(restaurantListPanel.getRoot());
            break;
        case DELIVERYMENSTATUS:
            availableDeliverymenListPanel = new AvailableDeliverymenListPanel(logic.getUnavailableDeliverymenList());
            listPanelPlaceholder.getChildren().add(availableDeliverymenListPanel.getRoot());
            unavailableDeliverymenListPanel = new UnavailableDeliverymenListPanel(logic.getAvailableDeliverymenList());
            statisticsPlaceholder.getChildren().add(unavailableDeliverymenListPanel.getRoot());
            break;
        case EDITING:
            Restaurant editing = logic.getEditingRestaurantList().get(0);
            editingRestaurantPlaceholder.setPrefHeight(125.0);
            editingRestaurantPlaceholder.setMinHeight(125.0);
            restaurantListPanel = new RestaurantListPanel(logic.getEditingRestaurantList());
            editingRestaurantPlaceholder.getChildren().add(restaurantListPanel.getRoot());
            foodListPanel = new FoodListPanel(editing.getMenu());
            listPanelPlaceholder.getChildren().add(foodListPanel.getRoot());

            //statisticsDisplay = new StatisticsDisplay();
            //statisticsPlaceholder.getChildren().add(statisticsDisplay.getRoot());
            //statisticsDisplay.setFeedbackToUser("THIS PART IS FOR STATISTICS\nWORK IN PROGRESS");
            break;
        default:
            orderListPanel = new OrderListPanel(logic.getFilteredOrderList());
            listPanelPlaceholder.getChildren().add(orderListPanel.getRoot());
        }
    }

    public PersonListPanel getPersonListPanel() {
        return personListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.deliverymans.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
            Context nextContext = commandResult.getContext();
            boolean isNewContext = nextContext != null && nextContext != currentContext;

            if (isNewContext) {
                changeDisplay(nextContext);
            }

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
