package lk.ijse.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import lk.ijse.dto.Board;
import lk.ijse.dto.Piece;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class Board1FormController implements Initializable {
    public static Board board;

    @FXML
    private JFXButton btn4;

    @FXML
    private JFXButton btn3;

    @FXML
    private JFXButton btn2;

    @FXML
    private JFXButton btn1;

    @FXML
    private TextField txtX;

    private Piece selectedPiece;

    List<Piece> pieces;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("in board 1 ini...");
        Piece piece1 = new Piece("BLUE", 0, btn1);
        Piece piece2 = new Piece("YELLOW", 0, btn2);
        Piece piece3 = new Piece("PINK", 0, btn3);
        Piece piece4 = new Piece("GREEN", 0, btn4);

        pieces = new ArrayList<>();
        pieces.add(piece1);
        pieces.add(piece2);
        pieces.add(piece3);
        pieces.add(piece4);
        selectedPiece = pieces.get(pieces.size() - 1);

        board = new Board(
                100,
                55,
                0,
                0,
                null,
                null,
                null,
                null,
                pieces
        );
    }

    @FXML
    void btnRollDiceOnAction(ActionEvent event) {
        Random r = new Random();
        int diceNumber = r.nextInt(6) + 1;
        txtX.setText(String.valueOf(diceNumber));

        for (int i = 0; i < pieces.size(); i++) {
            if (selectedPiece == pieces.get(i)){
                try {
                    selectedPiece = pieces.get(i + 1);
                }catch (IndexOutOfBoundsException exception){
                    selectedPiece = pieces.get(0);
                }
                break;
            }
        }

        board.movePiece(selectedPiece, diceNumber);
    }
}
