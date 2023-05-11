package lk.ijse.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lk.ijse.dto.Board;
import lk.ijse.dto.Piece;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Board1FormController implements Initializable {
    public static Board board;
    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton btnRollDice;

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

    @FXML
    private ImageView imgDiceRolling;

    @FXML
    private ImageView imgWinner;

    @FXML
    private Label lblWins;

    @FXML
    private Label lblTurn;

    private Piece selectedPiece;

    private String winner;

    private int playerCount;

    List<Piece> pieces;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn1.setVisible(false);
        btn2.setVisible(false);
        btn3.setVisible(false);
        btn4.setVisible(false);


        pieces = new ArrayList<>();
    }

    @FXML
    void btnRollDiceOnAction(ActionEvent event) {
        btnRollDice.setDisable(true);
        txtX.setText(null);
        Random r = new Random();
        int diceNumber = r.nextInt(6) + 1;

        imgDiceRolling.setImage(new Image("/img/diceRolling.gif"));
        selectPiece();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2100), ev -> {
            imgDiceRolling.setImage(null);
            txtX.setText(String.valueOf(diceNumber));
            winner = board.movePiece(selectedPiece, diceNumber);

            if(winner != null){
                showWinner(winner);
                return;
            }

            updateWhosTurnLbl();

            btnRollDice.setDisable(false);
        }));
        timeline.play();
    }

    private void showWinner(String winner) {
        System.out.println("winner === " + winner);
        lblTurn.setText(winner);
        lblWins.setText("Wins!");
        imgWinner.setImage(new Image("/img/winnerGif.gif"));
    }

    private void selectPiece() {
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
    }

    private void updateWhosTurnLbl() {
        String color = null;
        for (int i = 0; i < pieces.size(); i++) {
            if (selectedPiece == pieces.get(i)){
                try {
                    color = pieces.get(i + 1).getColor();
                }catch (IndexOutOfBoundsException exception){
                    color = pieces.get(0).getColor();
                }
                break;
            }
        }
        lblTurn.setText(color + "'s Turn");
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
            case 4 : pieces.add(piece4); btn4.setVisible(true);
            case 3 : pieces.add(piece3); btn3.setVisible(true);
            case 2 : pieces.add(piece1);pieces.add(piece2); btn1.setVisible(true); btn2.setVisible(true);
        }
        selectedPiece = pieces.get(pieces.size() - 1);
        board = createBoard();
    }

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("/view/main_window_form.fxml"));
        root.getChildren().setAll(node);
    }
}
