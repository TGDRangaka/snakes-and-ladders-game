package lk.ijse.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lk.ijse.dto.Piece;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class MainWindowFormController implements Initializable {

    @FXML
    private JFXButton btn1;

    @FXML
    private JFXButton btn2;

    @FXML
    private TextField txtX;

    @FXML
    private JFXTextField txtCellNum;

    private Integer cellNum = 0;

    private Piece selectedPiece = null;
    private Piece piece1;
    private Piece piece2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        piece1 = new Piece("BLUE", cellNum, btn1);
        piece2 = new Piece("YELLOW", cellNum, btn2);

        selectedPiece = piece2;

        btn1.setOnAction((e) -> {
            TranslateTransition transition = new TranslateTransition();
            transition.setDuration(Duration.seconds(2));
            transition.setNode(piece1.getPiece());
//            transition.setAutoReverse(true);
//            transition.setCycleCount(2);
            transition.setToX(0);
            transition.setToY(0);

            transition.play();
            piece1.setPosition(0);
        });
        btn2.setOnAction((e) -> {
            TranslateTransition transition = new TranslateTransition();
            transition.setDuration(Duration.seconds(2));
            transition.setNode(piece2.getPiece());
//            transition.setAutoReverse(true);
//            transition.setCycleCount(2);
            transition.setToX(0);
            transition.setToY(0);

            transition.play();
            piece2.setPosition(0);
        });
    }

    @FXML
    void btnRandNumOnAction(ActionEvent event) {
        if(selectedPiece.equals(piece1)){
            selectedPiece = piece2;
        }else {
            selectedPiece = piece1;
        }

        Random r = new Random();
        int randNum = r.nextInt(6) + 1;
        cellNum = selectedPiece.getPosition();
        int nextCellNum = randNum + cellNum;

        //
        boolean isHaveAWinner = nextCellNum == 100;
        boolean isValidMoveInLastRow = 100 - cellNum >= randNum;
        if(cellNum > 93) {
            if (!isValidMoveInLastRow) {
                return;
            }
        }
        //calculate next moves
        System.out.println("--Cell number = " + cellNum);
        int currentRow = cellNum / 10;
        int nextRow = nextCellNum / 10;
        int currentRowMaxCellNum = (cellNum % 10 == 0 && cellNum > 9) ? cellNum : nextRow == currentRow ? (nextRow + 1) * 10 : nextRow * 10;
        boolean isPieceInCurrentRowMaxCell = cellNum < 10 ? false : cellNum % 10 == 0;
        int currentRowMoves = isPieceInCurrentRowMaxCell ? 0 : currentRow != nextRow ? (currentRowMaxCellNum - cellNum) : (nextCellNum - cellNum);
        boolean isHaveUpMove = isPieceInCurrentRowMaxCell ? true : nextCellNum > currentRowMaxCellNum;
        boolean isHaveNextRowMoves = isPieceInCurrentRowMaxCell ? nextCellNum > cellNum + 1 : nextCellNum > currentRowMaxCellNum + 1;
        int nextRowMoves = -1;
        if(isHaveNextRowMoves) nextRowMoves = nextCellNum - (currentRowMaxCellNum + 1);
        {
            System.out.println("  Current Row Max Cell Num = " + currentRowMaxCellNum);
            System.out.println("  Is Piece In Current Row Max Cell = " + isPieceInCurrentRowMaxCell);
            System.out.println("  Current Row Moves = " + currentRowMoves);
            System.out.println("  Is Have Up Move = " + isHaveUpMove);
            System.out.println("  Is Have Next Row Moves = " + isHaveNextRowMoves);
            System.out.println("  Next Row Moves = " + nextRowMoves);
        }

        //move piece to next position
        int firstMove = cellNum % 10 + currentRowMoves;
        int lastMove = ++nextRowMoves;
        int row = isPieceInCurrentRowMaxCell ? --currentRow : currentRow;
        int duration = currentRowMoves == 0 ? 1 : currentRowMoves * 333;
        int lastMoveDuration = nextRowMoves * 333;

        if(row == 0 || row == 2 || row == 4 || row == 6 || row == 8){
            moveLeft(currentRowMoves == 0 ? 0 : firstMove, currentRowMoves * 333);

            if(isHaveUpMove) {
                Timeline timeline1 = new Timeline(new KeyFrame(Duration.millis(duration), e -> {
                    moveUp(nextRow);

                    if(isHaveNextRowMoves) {
                        Timeline timeline2 = new Timeline(new KeyFrame(Duration.millis(333), ev -> {
                            moveRight(lastMove, lastMoveDuration);
                        }));
                        timeline2.play();
                    }

                }));
                timeline1.play();
            }
        }else{
            moveRight(currentRowMoves == 0 ? 0 : firstMove, currentRowMoves * 333);

            if(isHaveUpMove) {
                Timeline timeline1 = new Timeline(new KeyFrame(Duration.millis(duration), e -> {
                    moveUp(nextRow);

                    if(isHaveNextRowMoves) {
                        Timeline timeline2 = new Timeline(new KeyFrame(Duration.millis(333), ev -> {
                            moveLeft(lastMove, lastMoveDuration);
                        }));
                        timeline2.play();
                    }

                }));
                timeline1.play();
            }
        }

        cellNum += randNum;
        selectedPiece.setPosition(cellNum);
        System.out.println("\t"+ selectedPiece.toString());
        txtX.setText(String.valueOf(randNum));
        txtCellNum.setText(String.valueOf(cellNum));

        if(isHaveAWinner){
            new Alert(Alert.AlertType.CONFIRMATION, "Win -- " + selectedPiece.getColor()).show();
        }
    }

    void moveLeft(int cellsCount, int duration){
        if(cellsCount == 0) return;
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.millis(duration));
        transition.setNode(selectedPiece.getPiece());
        int x = 55 * cellsCount;
        transition.setToX(-x);

        transition.play();
        System.out.println("Moved Left -- " + duration);
    }

    void moveRight(int cellsCount, int duration){
        if(cellsCount == 0) return;
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.millis(duration));
        transition.setNode(selectedPiece.getPiece());
        int x = 55 * (11 - cellsCount);
        transition.setToX(-x);

        transition.play();
        System.out.println("Moved Right-- " + duration);
    }

    void moveUp(int row){
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.millis(333));
        transition.setNode(selectedPiece.getPiece());
        int y = 55 * row;
        transition.setToY(-y);

        transition.play();
        System.out.println("Moved Up");
    }

    void moveDown(int cellsCount){
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(2));
        transition.setNode(selectedPiece.getPiece());
//            transition.setAutoReverse(true);
//            transition.setCycleCount(2);
        int y = 55 * cellsCount - 55;
        transition.setToY(y);

        transition.play();
    }

}
