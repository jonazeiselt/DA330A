package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Message;
import model.User;
import other.Listener;
import other.Theme;

import java.util.ArrayList;

/**
 * Displays a view with active users, and a view for inputting
 * message to be sent to server or another user.
 * Created by Jonas Eiselt on 2017-12-07.
 */
public class ClientView extends VBox
{
    private User me;
    private Listener listener;

    private UserListView userListView;
    private MessageView messageView;

    private HBox conversationView;
    private User selectedUser;

    private Label statusLabel;

    public ClientView(User me)
    {
        this.me = me;
    }

    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    public void onUserSelected(User selectedUser)
    {
        this.selectedUser = selectedUser;
    }

    /** Updates the app bar. */
    public void setStatus(User.Status myOnlineStatus)
    {
        statusLabel.setText(myOnlineStatus.toString());
    }

    public void initializeView()
    {
        userListView = new UserListView(listener);
        userListView.initializeView();

        messageView = new MessageView(me, selectedUser);
        messageView.initializeView();

        conversationView = new HBox();
        conversationView.getChildren().addAll(userListView, messageView);

        getChildren().addAll(getAppBar(), conversationView);
        setStyle("-fx-background-color: white; -fx-font-family: 'Droid Sans'; -fx-font-size: 12pt;");
    }

    private HBox getAppBar()
    {
        ImageView userImageView = new ImageView(new Image("/files/user.png"));
        userImageView.setFitHeight(45);
        userImageView.setPreserveRatio(true);
        userImageView.setSmooth(true);

        Label nameLabel = new Label(me.getName());
        statusLabel = new Label(me.getStatus().toString());

        nameLabel.setFont(Theme.getRegularFont(20));
        nameLabel.setStyle("-fx-text-fill: #ffffff;");

        statusLabel.setFont(Theme.getRegularFont(16));
        statusLabel.setStyle("-fx-text-fill: #8a8e91;");

        VBox userBox = new VBox();
        userBox.getChildren().addAll(nameLabel, statusLabel);

        HBox root = new HBox(10);
        root.setPadding(new Insets(10,20,10,20));
        root.setAlignment(Pos.CENTER_LEFT);
        root.setPrefSize(Double.MAX_VALUE, 55);
        root.setStyle("-fx-background-color: #36393e;");

        root.getChildren().addAll(userImageView, userBox);

        return root;
    }

    public Message getMessageToSend()
    {
        return messageView.getMessageToSend();
    }

    public void setMessageReceived(Message receivedMessage)
    {
        messageView.addReceivingMessage(receivedMessage);
    }

    public void updateUserList(ArrayList<User> userList)
    {
        userList.remove(me);
        userListView.updateUserList(userList);
    }

    public void updateMessageView(User user)
    {
        conversationView.getChildren().remove(1);

        messageView = new MessageView(me, user);
        onUserSelected(user);
        messageView.initializeView();

        conversationView.getChildren().add(messageView);
    }
}