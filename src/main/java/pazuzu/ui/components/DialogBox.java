package pazuzu.ui.components;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import pazuzu.ui.controller.MainWindow;

public class DialogBox extends HBox {

    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;
    @FXML
    private VBox messageBubble;

    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(img);
        
        // Make the image circular
        makeImageCircular();
    }

    /**
     * Makes the profile picture circular by applying a circular clip.
     */
    private void makeImageCircular() {
        // Create a circular clip with radius equal to half the image size (20px radius for 40px image)
        double radius = 20;
        Circle clip = new Circle(radius);
        
        // Center the clip
        clip.setCenterX(radius);
        clip.setCenterY(radius);
        
        // Apply the circular clip to the ImageView
        displayPicture.setClip(clip);
    }
    
    /**
     * Applies user message styling (blue bubble, right-aligned).
     */
    private void applyUserStyling() {
        dialog.setStyle("-fx-background-color: #007AFF; -fx-text-fill: white; -fx-background-radius: 18; " +
                       "-fx-padding: 12 16 12 16; -fx-font-size: 15px; -fx-font-family: 'Segoe UI', 'Arial', sans-serif; " +
                       "-fx-font-weight: 500;");
        messageBubble.setAlignment(Pos.TOP_RIGHT);
    }
    
    /**
     * Applies AI message styling (gray bubble, left-aligned).
     */
    private void applyAIStyling() {
        dialog.setStyle("-fx-background-color: #E5E5EA; -fx-text-fill: #000000; -fx-background-radius: 18; " +
                       "-fx-padding: 12 16 12 16; -fx-font-size: 15px; -fx-font-family: 'Segoe UI', 'Arial', sans-serif; " +
                       "-fx-font-weight: 500;");
        messageBubble.setAlignment(Pos.TOP_LEFT);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        this.setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        FXCollections.reverse(tmp);
        this.getChildren().setAll(tmp);
    }

    public static DialogBox getUserDialog(String s, Image i) {
        var db = new DialogBox(s, i);
        db.applyUserStyling();
        return db;
    }

    public static DialogBox getPazuzuDialog(String s, Image i) {
        var db = new DialogBox(s, i);
        db.applyAIStyling();
        db.flip();
        return db;
    }
}

