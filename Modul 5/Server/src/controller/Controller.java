package controller;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import model.Message;
import model.Server;
import model.User;
import other.Listener;
import view.ServerView;

import java.util.ArrayList;

/**
 * Created by Jonas Eiselt on 2017-12-06.
 */
public class Controller implements Listener
{
    private ServerView serverView;
    private Server server;
    private ArrayList<User> allUsers = new ArrayList<>();

    public Controller(ServerView serverView)
    {
        this.serverView  = serverView;
    }

    public void initializeView()
    {
        serverView.initializeView();
    }

    public void startServer(int portNumber)
    {
        server = new Server(portNumber);
        server.setListener(this);

        Thread serverThread = new Thread(server);
        serverThread.start();
    }

    public void stopServer()
    {
        System.out.println("Stopping server!");

        if (server != null && server.isRunning())
            server.terminate();
    }

    @Override
    public Message getMessageToSend()
    {
        String content = serverView.getMessageToSend();
        if (content != null && !content.trim().equals(""))
        {
            Message message = new Message();
            message.setSender("Server");
            message.setReceiver("Everyone");
            message.setType(Message.Type.MESSAGE);
            message.setContent(content);

            return message;
        }
        return null;
    }

    @Override
    public void onMessageReceived(String receivedMessage)
    {
        Platform.runLater(() -> serverView.setMessageReceived(receivedMessage));
    }

    @Override
    public String onGetUsersRequested()
    {
        StringBuilder allUsersContent = new StringBuilder();
        allUsersContent.append("users(Server=Online");
        for (User aUser : allUsers)
        {
            allUsersContent.append(",");

            allUsersContent.append(aUser.getName());
            allUsersContent.append("=");
            allUsersContent.append(aUser.getStatus().toString());
        }
        allUsersContent.append(")");
        return allUsersContent.toString();
    }

    @Override
    public String onClientLogin(User user)
    {
        if (!allUsers.contains(user))
            allUsers.add(user);

        return "user(" + user.getName() + "=" + user.getStatus().toString() + ")";
    }

    @Override
    public void onExceptionCaught(String exceptionMessage)
    {
        Platform.runLater(() ->
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exceptionMessage);

            alert.showAndWait();

            System.exit(0);
        });
    }
}
