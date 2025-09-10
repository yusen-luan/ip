package pazuzu.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import pazuzu.Pazuzu;
import pazuzu.ui.controller.MainWindow;

/**
 * A GUI for Pazuzu using FXML.
 */
public class Main extends Application {

    private Pazuzu pazuzu = new Pazuzu();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setPazuzu(pazuzu);  // inject the Pazuzu instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



