package lk.ijse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/main_window_form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Snakes & Ladders");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
