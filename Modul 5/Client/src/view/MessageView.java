package view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Message;
import model.User;
import other.Theme;

/**
 * Displays the view where the user can input a message.
 * Created by Jonas Eiselt on 2017-12-08.
 */
class MessageView extends VBox
{
    private User me;
    private User selectedUser;

    private VBox messageArea;
    private String messageToSend;

    MessageView(User me, User selectedUser)
    {
        this.me = me;
        this.selectedUser = selectedUser;
    }

    void initializeView()
    {
        HBox userBar = getUserBar();
        HBox scrollableMessageArea = getScrollableMessageArea();
        HBox sendBar = getSendBar();

        setPrefWidth(500);
        getChildren().addAll(userBar, scrollableMessageArea, sendBar);

        for (Message message : selectedUser.getMessages())
        {
            if (message.isFrom(me.getName()))
                addSendingMessage(message.getContent());
            else
                addReceivingMessage(message);
        }
    }

    private HBox getUserBar()
    {
        ImageView userImageView = new ImageView(new Image("/files/user.png"));
        userImageView.setFitHeight(45);
        userImageView.setPreserveRatio(true);
        userImageView.setSmooth(true);

        Label nameLabel = new Label(selectedUser.getName());
        Label statusLabel = new Label(selectedUser.getStatus().toString());

        nameLabel.setFont(Theme.getRegularFont(18));
        statusLabel.setFont(Theme.getRegularFont(14));

        VBox userBox = new VBox();
        userBox.getChildren().addAll(nameLabel, statusLabel);

        HBox root = new HBox(10);
        root.setPadding(new Insets(10,20,10,20));
        root.setStyle("-fx-background-color: #d6d8db; -fx-border-color: #c9cbcf; -fx-border-weight: 1;");
        root.getChildren().addAll(userImageView, userBox);

        return root;
    }

    private HBox getScrollableMessageArea()
    {
        messageArea = new VBox();

        messageArea.setSpacing(10);
        messageArea.setPadding(new Insets(15));
        messageArea.setPrefSize(500, 476);
        messageArea.setAlignment(Pos.BOTTOM_CENTER);
        messageArea.setStyle("-fx-background-color: white;");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(messageArea);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vvalueProperty().bind(messageArea.heightProperty());
        scrollPane.setStyle("-fx-background-color: white;");

        HBox root = new HBox();
        root.setPadding(new Insets(2,0,0,2));
        root.getChildren().add(scrollPane);

        return root;
    }

    private HBox getSendBar()
    {
        String noBlueBorderStyle = "-fx-focus-color: transparent; -fx-faint-focus-color: transparent; " +
                "-fx-border-color: #ccc; -fx-border-width: 1;";

        TextArea textArea = new TextArea();
        textArea.setPrefWidth(400);
        textArea.setPrefRowCount(1);
        textArea.setWrapText(true);
        textArea.setStyle(noBlueBorderStyle + " -fx-border-radius: 5 0 0 5; -fx-background-radius: 5 0 0 5;");
        textArea.setFont(Theme.getRegularFont(16));

        KeyCombination shiftEnter = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.SHIFT_DOWN);
        KeyCombination enter = new KeyCodeCombination(KeyCode.ENTER);
        textArea.addEventFilter(KeyEvent.KEY_PRESSED, event ->
        {
            if (enter.match(event))
            {
                messageToSend = textArea.getText();
                event.consume();
            }
            else if (shiftEnter.match(event))
            {
                textArea.insertText(textArea.getCaretPosition(), "\n");
                event.consume();
            }
        });

        ImageView sendImageView = new ImageView(new Image("/files/send.png"));
        sendImageView.setFitHeight(35);
        sendImageView.setPreserveRatio(true);
        sendImageView.setSmooth(true);

        Button sendButton = new Button();
        sendButton.setGraphic(sendImageView);
        sendButton.setPrefSize(50,48);
        sendButton.setStyle("-fx-background-color: #d6d8db; -fx-border-color: #ccc; -fx-border-weight: 1 1 1 0; -fx-border-radius: 0 5 5 0; -fx-background-radius: 0 5 5 0;");

        sendButton.setOnMouseClicked(event -> messageToSend = textArea.getText());

        HBox sendBox = new HBox();
        sendBox.setPrefWidth(Double.MAX_VALUE);
        sendBox.setPadding(new Insets(20));
        sendBox.getChildren().addAll(textArea, sendButton);

        return sendBox;
    }

    void addReceivingMessage(Message message)
    {
        Text text = new Text();
        text.setText(message.getContent());
        text.setFont(Theme.getRegularFont(17));

        double width = text.getLayoutBounds().getWidth();
        text.setWrappingWidth(Math.min(width, 300));

        HBox textBox = new HBox();
        textBox.getChildren().add(text);
        textBox.setStyle("-fx-background-color: #e6e6e6; -fx-border-color: #ccc; -fx-border-radius: 5 5 5 0; -fx-background-radius: 5 5 5 0;");
        textBox.setPadding(new Insets(5,20,5,20));

        textBox.setOnMouseEntered(event -> textBox.setStyle("-fx-background-color: #f2f2f2; -fx-border-color: #ccc; -fx-border-radius: 5 5 5 0; -fx-background-radius: 5 5 5 0;"));
        textBox.setOnMouseExited(event -> textBox.setStyle("-fx-background-color: #e6e6e6; -fx-border-color: #ccc; -fx-border-radius: 5 5 5 0; -fx-background-radius: 5 5 5 0;"));

        HBox messageBox = new HBox();
        messageBox.setMinWidth(350);
        messageBox.setPadding(new Insets(0,20,0,40));
        messageBox.setAlignment(Pos.CENTER_LEFT);
        messageBox.getChildren().add(textBox);

        messageArea.getChildren().add(messageBox);
    }

    Message getMessageToSend()
    {
        String res = messageToSend;
        messageToSend = null;

        if (res != null && !res.trim().equals(""))
            Platform.runLater(() -> addSendingMessage(res));

        Message message = new Message();
        message.setContent(res);
        message.setSender(me.getName());
        message.setReceiver(selectedUser.getName());

        return message;
    }

    private void addSendingMessage(String message)
    {
        Text text = new Text();
        text.setText(message);
        text.setFill(Color.WHITE);
        text.setFont(Theme.getRegularFont(17));

        double width = text.getLayoutBounds().getWidth();
        text.setWrappingWidth(Math.min(width, 300));

        HBox textBox = new HBox();
        textBox.getChildren().add(text);
        textBox.setStyle("-fx-background-color: #7eb3e7; -fx-border-color: #68a6e3; -fx-border-radius: 5 5 0 5; -fx-background-radius: 5 5 0 5;");
        textBox.setPadding(new Insets(5,20,5,20));

        textBox.setOnMouseEntered(event -> textBox.setStyle("-fx-background-color: #93bfeb; -fx-border-color: #68a6e3; -fx-border-radius: 5 5 0 5; -fx-background-radius: 5 5 0 5;"));
        textBox.setOnMouseExited(event -> textBox.setStyle("-fx-background-color: #7eb3e7; -fx-border-color: #68a6e3; -fx-border-radius: 5 5 0 5; -fx-background-radius: 5 5 0 5;"));

        HBox messageBox = new HBox();
        messageBox.setMinWidth(350);
        messageBox.setPadding(new Insets(0,60,0,40));
        messageBox.setAlignment(Pos.CENTER_RIGHT);
        messageBox.getChildren().add(textBox);

        messageArea.getChildren().add(messageBox);
    }
}
