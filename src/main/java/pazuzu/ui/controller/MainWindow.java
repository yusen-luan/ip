package pazuzu.ui.controller;

import javafx.application.Platform;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import pazuzu.Pazuzu;
import pazuzu.ui.components.DialogBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;
    @FXML
    private HBox inputContainer;

    private Pazuzu pazuzu;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.png"));
    private Image pazuzuImage = new Image(this.getClass().getResourceAsStream("/images/ai.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        
        // Initialize send button state
        updateSendButtonState();
        
        // Add listener to update send button when text changes
        userInput.textProperty().addListener((observable, oldValue, newValue) -> {
            updateSendButtonState();
        });
    }
    
    /**
     * Updates the send button appearance based on input field content.
     */
    private void updateSendButtonState() {
        boolean hasText = !userInput.getText().trim().isEmpty();
        if (hasText) {
            sendButton.setStyle("-fx-background-color: #007AFF; -fx-background-radius: 17; -fx-border-radius: 17; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-color: transparent; -fx-cursor: hand; -fx-opacity: 1.0;");
        } else {
            sendButton.setStyle("-fx-background-color: #C7C7CC; -fx-background-radius: 17; -fx-border-radius: 17; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-color: transparent; -fx-cursor: hand; -fx-opacity: 0.6;");
        }
        sendButton.setDisable(!hasText);
    }

    /** Injects the Pazuzu instance */
    public void setPazuzu(Pazuzu pazuzu) {
        this.pazuzu = pazuzu;
        dialogContainer.getChildren().add(DialogBox.getPazuzuDialog("Pazuzu, what u want?", pazuzuImage));
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();
        
        // Don't process empty messages
        if (input.isEmpty()) {
            return;
        }
        
        String response = pazuzu.processCommand(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getPazuzuDialog(response, pazuzuImage)
        );
        userInput.clear();

        // Check for bye command and exit after 2 seconds
        if (input.equals("bye")) {
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> Platform.exit());
            delay.play();
        }
    }
}

