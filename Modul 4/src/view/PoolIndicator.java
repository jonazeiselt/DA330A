package view;

import com.sun.javafx.scene.traversal.Direction;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by Jonas Eiselt on 2017-12-03.
 */
public class PoolIndicator
{
    private final ImageView imageView;
    private final Direction direction;

    public enum State
    {
        ON, OFF
    }

    PoolIndicator(Direction direction)
    {
        this.direction = direction;

        String path;
        switch (direction)
        {
            case RIGHT:
                path = "/files/rightarrow.png";
                break;
            case DOWN:
                path = "/files/downarrow.png";
                break;
            case UP:
                path = "/files/uparrow.png";
                break;
            default:
                path = "/files/downarrow.png";
        }

        imageView = new ImageView(new Image(path));
        imageView.setFitHeight(40);
        imageView.setPreserveRatio(true);
    }

    Pane getIndicator(State state)
    {
        Circle circle;
        if (state == State.ON)
            circle = new Circle(20, Color.rgb(98,205,77));
        else
            circle = new Circle(20, Color.rgb(239,60,60));

        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(1);

        if (direction == Direction.RIGHT || direction == Direction.LEFT)
        {
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            vBox.setPadding(new Insets(10));
            vBox.getChildren().addAll(imageView, circle);

            return vBox;
        }
        else
        {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setPadding(new Insets(10));
            hBox.getChildren().addAll(imageView, circle);

            return hBox;
        }
    }
}
