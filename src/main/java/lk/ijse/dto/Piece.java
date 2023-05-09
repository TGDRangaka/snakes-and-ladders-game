package lk.ijse.dto;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Piece {
    private String color;
    private Integer position;
    private Node piece;
}
