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

    private int playerCount;

    List<Piece> pieces;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn1.setDisable(true);
        btn2.setDisable(true);
        btn3.setDisable(true);
        btn4.setDisable(true);


        pieces = new ArrayList<>();
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

    private Board createBoard() {
        Integer[] ladderStartCells = new Integer[]{20,26,34,58};
        Integer[] ladderEndingCells = new Integer[]{42,56,73,79};
        List<Integer[]> ladderEndingCellsXY = new ArrayList<>();
        ladderEndingCellsXY.add(new Integer[]{136, 272});
        ladderEndingCellsXY.add(new Integer[]{340, 340});
        ladderEndingCellsXY.add(new Integer[]{544, 476});
        ladderEndingCellsXY.add(new Integer[]{136, 476});

        Integer[] snakesStartCells = new Integer[]{32, 52, 61, 85, 93, 98};
        Integer[] snakesEndingCells = new Integer[]{12, 7, 38, 66, 67, 16};
        List<Integer[]> snakesEndingCellsXY = new ArrayList<>();
        snakesEndingCellsXY.add(new Integer[]{612, 68});
        snakesEndingCellsXY.add(new Integer[]{476, 0});
        snakesEndingCellsXY.add(new Integer[]{204, 204});
        snakesEndingCellsXY.add(new Integer[]{408, 408});
        snakesEndingCellsXY.add(new Integer[]{476, 408});
        snakesEndingCellsXY.add(new Integer[]{340, 68});

        Board board = new Board(
                100,
                68,
                4,
                6,
                ladderStartCells,
                ladderEndingCells,
                ladderEndingCellsXY,
                snakesStartCells,
                snakesEndingCells,
                snakesEndingCellsXY,
                pieces
        );

        return board;
    }

    public void setPlayerCount(int playersCount) {
        System.out.println("Players Count = " + playersCount);
        this.playerCount = playersCount;

        Piece piece1 = new Piece("BLUE", 0, btn1);
        Piece piece2 = new Piece("YELLOW", 0, btn2);
        Piece piece3 = new Piece("PINK", 0, btn3);
        Piece piece4 = new Piece("GREEN", 0, btn4);

        switch (playerCount){
            case 4 : pieces.add(piece4); btn4.setDisable(false);
            case 3 : pieces.add(piece3); btn3.setDisable(false);
            case 2 : pieces.add(piece1);pieces.add(piece2); btn1.setDisable(false); btn2.setDisable(false);
        }
        selectedPiece = pieces.get(pieces.size() - 1);
        board = createBoard();
    }
}
