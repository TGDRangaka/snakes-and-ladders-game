package lk.ijse.Controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainWindowFormController {

    @FXML
    private AnchorPane root;

    @FXML
    void btn2PlayersOnAction(ActionEvent event) throws IOException {
        showGameBoard(2);
    }

    @FXML
    void btn3PlayerOnAction(ActionEvent event) throws IOException {
        showGameBoard(3);
    }

    @FXML
    void btn4PlayersOnAction(ActionEvent event) throws IOException {
        showGameBoard(4);
    }

    private void showGameBoard(int playersCount) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/board_1_form.fxml"));
        Node node = fxmlLoader.load();
        Board1FormController boardView = fxmlLoader.getController();
        root.getChildren().setAll(node);
        boardView.setPlayerCount(playersCount);
    }
}
