package lk.ijse.dto;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.util.Duration;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Data
@AllArgsConstructor
public class Board {
    private Integer cellCount;
    private Integer cellWidth;
    private Integer laddersCount;
    private Integer snakesCount;
    private Integer[] ladderStartCells;
    private Integer[] ladderEndingCells;
    private List<Integer[]> ladderEndingCellsXY;
    private Integer[] snakeStartCells;
    private Integer[] snakeEndingCells;
    private List<Integer[]> snakeEndingCellsXY;
    private List<Piece> pieces;


    public void movePiece(Piece selectedPiece, int diceNumber) {
//        if(selectedPiece.equals(piece1)){
//            selectedPiece = piece2;
//        }else {
//            selectedPiece = piece1;
//        }
        int cellNum = selectedPiece.getPosition();
        int randNum = diceNumber;
        int nextCellNum = randNum + cellNum;
        Node node = selectedPiece.getPiece();
        System.out.println(selectedPiece.getColor() + " --Cell number = " + cellNum);
        //
        boolean isHaveAWinner = nextCellNum == 100;
        boolean isValidMoveInLastRow = 100 - cellNum >= randNum;
        if(cellNum >= 93) {
            if (!isValidMoveInLastRow) {
                return;
            }
        }
        //calculate next moves
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
        int ladderMove = isInLadderMove(nextCellNum);
        int snakeMove = isInSnakeMove(nextCellNum);

        //move piece to next position
        int firstMove = cellNum % 10 + currentRowMoves;
        int lastMove = ++nextRowMoves;
        int row = isPieceInCurrentRowMaxCell ? --currentRow : currentRow;
        int duration = currentRowMoves == 0 ? 1 : currentRowMoves * 333;
        int lastMoveDuration = nextRowMoves == -1 ? 0 : nextRowMoves * 333;

        if(row == 0 || row == 2 || row == 4 || row == 6 || row == 8){
            moveLeft(currentRowMoves == 0 ? 0 : firstMove, currentRowMoves * 333, node);

            if(isHaveUpMove) {
                Timeline timeline1 = new Timeline(new KeyFrame(Duration.millis(duration), e -> {
                    moveUp(nextRow, node);

                    if(isHaveNextRowMoves) {
                        Timeline timeline2 = new Timeline(new KeyFrame(Duration.millis(333), ev -> {
                            moveRight(lastMove, lastMoveDuration, node);
                        }));
                        timeline2.play();
                    }

                }));
                timeline1.play();
            }
        }else{
            moveRight(currentRowMoves == 0 ? 0 : firstMove, currentRowMoves * 333, node);

            if(isHaveUpMove) {
                Timeline timeline1 = new Timeline(new KeyFrame(Duration.millis(duration), e -> {
                    moveUp(nextRow, node);

                    if(isHaveNextRowMoves) {
                        Timeline timeline2 = new Timeline(new KeyFrame(Duration.millis(333), ev -> {
                            moveLeft(lastMove, lastMoveDuration, node);


                        }));
                        timeline2.play();
                    }

                }));
                timeline1.play();
            }
        }

        int moveDuration = duration + (isHaveUpMove ? 333 : 0) + lastMoveDuration;
        if(ladderMove != -1 || snakeMove != -1){
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(moveDuration), ev -> {

                Integer[] xy = new Integer[2];
                if(ladderMove != -1){
                    xy = getLadderEndingCellsXY(ladderMove);
                }else if(snakeMove != -1){
                    xy = getSnakeEndingCellsXY(snakeMove);
                }


                movePiece(xy, node);

            }));
            timeline.play();
        }

        if(ladderMove != -1){
            nextCellNum = ladderEndingCells[ladderMove];
        }else if(snakeMove != -1){
            nextCellNum = snakeEndingCells[snakeMove];
        }
        selectedPiece.setPosition(nextCellNum);

        if(isHaveAWinner){
            new Alert(Alert.AlertType.CONFIRMATION, "Win -- " + selectedPiece.getColor()).show();
        }
    }

    private void movePiece(Integer[] xy, Node node){
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(2));
        transition.setNode(node);
        transition.setToX(-xy[0]);
        transition.setToY(-xy[1]);

        transition.play();
        System.out.println("Moved  -- " + Arrays.toString(xy));
    }

    private Integer[] getLadderEndingCellsXY(int ladderMove) {
        return ladderEndingCellsXY.get(ladderMove);
    }
    private Integer[] getSnakeEndingCellsXY(int snakeMove){
        return snakeEndingCellsXY.get(snakeMove);
    }

    private int isInLadderMove(int cellNum) {
        for (int i = 0; i < ladderStartCells.length; i++) {
            if (cellNum == ladderStartCells[i]){
                return i;
            }
        }
        return -1;
    }

    private int isInSnakeMove(int cellNum) {
        for (int i = 0; i < snakeStartCells.length; i++) {
            if (cellNum == snakeStartCells[i]){
                return i;
            }
        }
        return -1;
    }

    private void moveLeft(int cellsCount, int duration, Node node){
        if(cellsCount == 0) return;
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.millis(duration));
        transition.setNode(node);
        int x = cellWidth * cellsCount;
        transition.setToX(-x);

        transition.play();
        System.out.println("Moved Left -- " + duration);
    }

    private void moveRight(int cellsCount, int duration, Node node){
        if(cellsCount == 0) return;
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.millis(duration));
        transition.setNode(node);
        int x = cellWidth * (11 - cellsCount);
        transition.setToX(-x);

        transition.play();
        System.out.println("Moved Right-- " + duration);
    }

    private void moveUp(int row, Node node){
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.millis(333));
        transition.setNode(node);
        int y = cellWidth * row;
        transition.setToY(-y);

        transition.play();
        System.out.println("Moved Up");
    }
}
