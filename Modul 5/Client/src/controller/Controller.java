package controller;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import model.Client;
import model.Message;
import model.User;
import other.Listener;
import view.ClientView;

import java.util.ArrayList;

/**
 * Creates and initializes the client application. A
 * connection to the server is made.
 * Created by Jonas Eiselt on 2017-12-07.
 */
public class Controller implements Listener
{
    private ClientView clientView;
    private Client client;

    private ArrayList<User> friendList;

    private User server;
    private User selectedUser;

    public Controller(ClientView clientView)
    {
        this.clientView  = clientView;
        this.friendList = new ArrayList<>();

        server = new User("Server");
        selectedUser = server;
        friendList.add(server);
    }

    public void initializeView()
    {
        clientView.onUserSelected(server);
        clientView.setListener(this);

        clientView.initializeView();
        clientView.updateUserList(friendList);
    }

    public void startClient(String name, int portNumber)
    {
        client = new Client(name, portNumber);
        client.setListener(this);

        Thread serverThread = new Thread(client);
        serverThread.start();
    }

    public void stopClient()
    {
        System.out.println("Stopping client!");

        if (client != null && client.isRunning())
            client.terminate();
    }

    /** Gets and returns a Message to be sent to or via server. */
    @Override
    public Message getMessageToSend()
    {
        Message message = clientView.getMessageToSend();
        if (message.getContent() != null && !message.getContent().equals(""))
            selectedUser.addMessage(message);

        return message;
    }

    /**
     * Adds the received messaged to a storage and updates gui
     * if a discussion with the sender of Message is selected.
     */
    @Override
    public void onMessageReceived(Message receivedMessage)
    {
        Platform.runLater(() ->
        {
            User tmpUser = new User(receivedMessage.getSender());

            int index;
            if ((index = friendList.indexOf(tmpUser)) != -1)
            {
                friendList.get(index).addMessage(receivedMessage);

                if (tmpUser.equals(selectedUser))
                    clientView.setMessageReceived(receivedMessage);
            }
        });
    }

    @Override
    public void onLoginFailed()
    {
        Platform.runLater(() ->
        {
            friendList.get(0).setStatus(User.Status.OFFLINE);
            clientView.setStatus(User.Status.OFFLINE);

            clientView.updateUserList(friendList);
            clientView.updateMessageView(server);
        });
    }

    @Override
    public void onClientStopped()
    {
        System.out.println("> System exit");
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    @Override
    public void onExceptionCaught(String errorMessage)
    {
        Platform.runLater(() ->
        {
            selectedUser = server;
            for (User user : friendList)
                user.setStatus(User.Status.OFFLINE);

            clientView.updateUserList(friendList);
            clientView.updateMessageView(selectedUser);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(errorMessage);

            alert.showAndWait();
        });
    }

    @Override
    public void onUserReceived(User user)
    {
        Platform.runLater(() ->
        {
            int index;
            if ((index = friendList.indexOf(user)) != -1)
                friendList.get(index).setStatus(user.getStatus());
            else
                friendList.add(user);

            clientView.updateUserList(friendList);
        });
    }

    @Override
    public void onUserListReceived(ArrayList<User> userList)
    {
        Platform.runLater(() ->
        {
            for (User user : userList)
            {
                if (!friendList.contains(user))
                    friendList.add(user);
            }
            clientView.updateUserList(friendList);
        });
    }

    @Override
    public void onUserListClicked(String name)
    {
        User tmpUser = new User(name);
        selectedUser = friendList.get(friendList.indexOf(tmpUser));

        clientView.updateMessageView(selectedUser);
    }
}
