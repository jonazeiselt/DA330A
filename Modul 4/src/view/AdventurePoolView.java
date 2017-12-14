package view;

import com.sun.javafx.scene.traversal.Direction;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class AdventurePoolView extends HBox
{
    private PoolIndicator adventurePoolIndicator;

    private Label limitLabel;
    private Label vistorsLabel;
    private Label waitingAtExitLabel;

    private ImageView exitImageView;

    void initializeView()
    {
        adventurePoolIndicator = new PoolIndicator(Direction.RIGHT);
        VBox poolIndicatorBox = (VBox) adventurePoolIndicator.getIndicator(PoolIndicator.State.OFF);

        VBox pool = getPoolView(Color.rgb(62,150,239));

        VBox exitView = getExitView();

        setSpacing(10);
        getChildren().addAll(poolIndicatorBox, pool, exitView);
    }

    private VBox getPoolView(Color color)
    {
        Label titleLabel = new Label("Adventure Pool");
        ImageView ticketImageView = new ImageView(new Image("/files/vipticket.png"));
        ticketImageView.setFitHeight(40);
        ticketImageView.setPreserveRatio(true);

        HBox titleBox = new HBox(15);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.getChildren().addAll(titleLabel, ticketImageView);

        Label limitTitleLabel = new Label("Limit:");
        limitLabel = new Label(Integer.toString(0));
        HBox limitBox = new HBox(5);
        limitBox.getChildren().addAll(limitTitleLabel, limitLabel);

        Label visitorsTitleLabel = new Label("Visitors:");
        vistorsLabel = new Label(Integer.toString(0));
        HBox visitorsBox = new HBox(5);
        visitorsBox.getChildren().addAll(visitorsTitleLabel, vistorsLabel);

        HBox infoBox = new HBox(20);
        infoBox.getChildren().addAll(limitBox, visitorsBox);

        Rectangle rectangle = new Rectangle();
        rectangle.setFill(color);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(1);
        rectangle.setWidth(300);
        rectangle.setHeight(100);

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-border-color: #ccc; -fx-border-width: 1;");
        root.getChildren().addAll(titleBox, infoBox, rectangle);

        return root;
    }


    private VBox getExitView()
    {
        exitImageView = new ImageView(new Image("/files/exitclosed.png"));
        exitImageView.setFitHeight(50);
        exitImageView.setPreserveRatio(true);

        waitingAtExitLabel = new Label(Integer.toString(0));

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(exitImageView, waitingAtExitLabel);

        return root;
    }

    void setPoolEntranceClosed(PoolIndicator.State poolState)
    {
        VBox poolIndicatorBox = (VBox) adventurePoolIndicator.getIndicator(poolState);

        getChildren().remove(0);
        getChildren().add(0, poolIndicatorBox);
    }

    void setVisitorLimit(int visitorLimit)
    {
        limitLabel.setText(Integer.toString(visitorLimit));
    }

    void setVisitorsInPool(int visitors)
    {
        vistorsLabel.setText(Integer.toString(visitors));
    }

    void setCustomersExiting(int customersExiting)
    {
        waitingAtExitLabel.setText(Integer.toString(customersExiting));
    }

    void setVisitorsLeft(boolean visitorsLeft)
    {
        if (visitorsLeft)
            exitImageView.setImage(new Image("/files/exitopened.png"));
        else
            exitImageView.setImage(new Image("/files/exitclosed.png"));
    }
}
