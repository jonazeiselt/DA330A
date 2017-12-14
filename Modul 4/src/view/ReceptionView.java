package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Listener;
import model.Pool;

/**
 * Created by Jonas Eiselt on 2017-12-01.
 */
public class ReceptionView extends VBox
{
    private final Listener listener;

    private ImageView openImageView;
    private Button openCloseButton;

    private Label vipTicketLabel;
    private Label ticketLabel;

    public enum State
    {
        OPENED {
            @Override
            public String toString() {
                return "Opened";
            }
        },
        CLOSED {
            @Override
            public String toString() {
                return "Closed";
            }
        }
    }

    ReceptionView(Listener listener)
    {
        this.listener = listener;
    }

    void initializeView()
    {
        Label titleLabel = new Label("Reception");

        openImageView = new ImageView(new Image("/files/closed.png"));
        openImageView.setFitHeight(60);
        openImageView.setPreserveRatio(true);

        Separator sep1 = new Separator();
        sep1.setPadding(new Insets(20,0,0,0));

        VBox queueInfoBox = getQueueInfoBox();
        queueInfoBox.setPadding(new Insets(65,0,60,0));

        openCloseButton = new Button("Open Pools");
        openCloseButton.setPrefSize(Double.MAX_VALUE, 45);
        openCloseButton.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; " +
                "-fx-border-color: #ccc; -fx-border-width: 1;");

        openCloseButton.setOnMouseClicked(event -> listener.onButtonClicked());

        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(10));
        setSpacing(20);
        setPrefWidth(250);
        setStyle("-fx-border-color: #ccc; -fx-border-width: 1;");
        getChildren().addAll(titleLabel, openImageView, sep1, queueInfoBox, new Separator(), openCloseButton);
    }

    private VBox getQueueInfoBox()
    {
        Label label = new Label("Queues");

        // ...
        ImageView vipTicketImageView = new ImageView(new Image("/files/vipticket.png"));
        vipTicketImageView.setFitHeight(50);
        vipTicketImageView.setPreserveRatio(true);

        vipTicketLabel = new Label();
        vipTicketLabel.setText(Integer.toString(0));

        HBox vipTicketBox = new HBox(40);
        vipTicketBox.getChildren().addAll(vipTicketImageView, vipTicketLabel);
        vipTicketBox.setAlignment(Pos.CENTER);

        // ...
        ImageView ticketImageView = new ImageView(new Image("/files/ticket.png"));
        ticketImageView.setFitHeight(50);
        ticketImageView.setPreserveRatio(true);

        ticketLabel = new Label();
        ticketLabel.setText(Integer.toString(0));

        HBox ticketBox = new HBox(40);
        ticketBox.getChildren().addAll(ticketImageView, ticketLabel);
        ticketBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(label, vipTicketBox, ticketBox);

        return root;
    }

    void setCustomersWaiting(Pool pool, int customersWaiting)
    {
        if (pool == Pool.ADVENTURE)
            vipTicketLabel.setText(Integer.toString(customersWaiting));
        else
            ticketLabel.setText(Integer.toString(customersWaiting));
    }

    void openPools(boolean open)
    {
        if (open)
        {
            openImageView.setImage(new Image("/files/opened.png"));
            openCloseButton.setText("Close Pools");
        }
        else
        {
            openImageView.setImage(new Image("/files/closed.png"));
            openCloseButton.setText("Open Pools");
        }
    }
}
