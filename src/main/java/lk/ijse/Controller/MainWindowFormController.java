package lk.ijse.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
        piece1 = new Piece(Color.BLUE, cellNum, btn1);
        piece2 = new Piece(Color.YELLOW, cellNum, btn2);

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

        //calculate next moves
        System.out.println("--Cell number = " + cellNum);
        int nextCellNum = randNum + cellNum;
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

        if(row == 0 || row == 2 || row == 4 || row == 6 || row == 8){
            moveLeft(currentRowMoves == 0 ? 0 : firstMove);

            if(isHaveUpMove) {
                int duration = currentRowMoves == 0 ? 10 : 2000;
                Timeline timeline1 = new Timeline(new KeyFrame(Duration.millis(duration), e -> {
                    moveUp(nextRow);

                    if(isHaveNextRowMoves) {
                        Timeline timeline2 = new Timeline(new KeyFrame(Duration.seconds(2), ev -> {
                            moveRight(lastMove);
                        }));
                        timeline2.play();
                    }

                }));
                timeline1.play();
            }
        }else{
            moveRight(currentRowMoves == 0 ? 0 : firstMove);

            if(isHaveUpMove) {
                int duration = currentRowMoves == 0 ? 10 : 2000;
                Timeline timeline1 = new Timeline(new KeyFrame(Duration.millis(duration), e -> {
                    moveUp(nextRow);

                    if(isHaveNextRowMoves) {
                        Timeline timeline2 = new Timeline(new KeyFrame(Duration.seconds(2), ev -> {
                            moveLeft(lastMove);
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
    }

    void moveLeft(int cellsCount){
        if(cellsCount == 0) return;
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(2));
        transition.setNode(selectedPiece.getPiece());
//            transition.setAutoReverse(true);
//            transition.setCycleCount(2);
        int x = 55 * cellsCount;
        transition.setToX(-x);

        transition.play();
        System.out.println("Moved Left");
    }

    void moveRight(int cellsCount){
        if(cellsCount == 0) return;
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(2));
        transition.setNode(selectedPiece.getPiece());
        int x = 55 * (11 - cellsCount);
        transition.setToX(-x);

        transition.play();
        System.out.println("Moved Right");
    }

    void moveUp(int row){
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(2));
        transition.setNode(selectedPiece.getPiece());
//            transition.setAutoReverse(true);
//            transition.setCycleCount(2);
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
