package view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Message;

/**
 * Displays a view for incoming messages, and another view
 * for getting input to be sent.
 * Created by Jonas Eiselt on 2017-12-06.
 */
public class ServerView extends VBox
{
    private TextArea textArea;
    private String messageToSend;

    public void initializeView()
    {
        String noBlueBorderStyle = "-fx-focus-color: transparent; -fx-faint-focus-color: transparent; " +
                "-fx-border-color: #ccc; -fx-border-width: 1;";

        textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setPrefHeight(410);
        textArea.setStyle(noBlueBorderStyle);

        TextField textField = new TextField();
        textField.setPrefWidth(400);
        textField.setStyle(noBlueBorderStyle);

        Button sendButton = new Button("Send");
        sendButton.setMinWidth(100);
        sendButton.setStyle(noBlueBorderStyle);

        sendButton.setOnMouseClicked(event -> messageToSend = textField.getText());

        HBox sendBox = new HBox(10);
        sendBox.setPrefWidth(Double.MAX_VALUE);
        sendBox.getChildren().addAll(textField, sendButton);

        setSpacing(15);
        setPadding(new Insets(15));
        getChildren().addAll(textArea, sendBox);
    }

    public String getMessageToSend()
    {
        String res = messageToSend;
        messageToSend = null;

        return res;
    }

    /** Adds string to text area. */
    public void setMessageReceived(String receivedMessage)
    {
        textArea.appendText(receivedMessage + "\n");
    }
}
