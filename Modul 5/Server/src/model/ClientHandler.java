package model;

import other.Listener;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Handles incoming and outgoing messages to server.
 * Created by Jonas Eiselt on 2017-12-07.
 */
public class ClientHandler implements Runnable
{
    private static final String LOGIN = "login";
    private static final String GET_USERS = "get_users";
    private static final String SERVER = "Server";
    private static final String EVERYONE = "Everyone";

    private Socket clientSocket;
    private Listener listener;

    private boolean isRunning = true;

    private String sender;
    private String receiver;

    ClientHandler(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    ClientHandler(String sender, String receiver)
    {
        this.sender = sender;
        this.receiver = receiver;
    }

    void setListener(Listener listener)
    {
        this.listener = listener;
    }

    @Override
    public void run()
    {
        // Handle outgoing messages to client
        new Thread(() ->
        {
            while(isRunning)
            {
                Message message = listener.getMessageToSend();
                if (message != null)
                    Server.broadcast(message);

                sleep250ms();
            }
        }).start();

        // Handle incoming messages from client
        DataInputStream in;
        Message message;
        while(isRunning)
        {
            try
            {
                in = new DataInputStream(clientSocket.getInputStream());
                String messageReceived = in.readUTF();

                MessageProtocol messageProtocol = new MessageProtocol();
                messageProtocol.analyzeReceivedMessage(messageReceived);

                message = messageProtocol.getReceivedMessage();
                sender = message.getSender();
                receiver = message.getReceiver();
                Message.Type type = message.getType();
                String content = message.getContent();

                if (type == Message.Type.MESSAGE)
                {
                    if (!receiver.equals(SERVER))
                    {
                        Server.forwardMessage(message);
                        listener.onMessageReceived("Forwarding a message to " + receiver);
                    }
                    else
                        listener.onMessageReceived(sender + " says \"" + message.getContent() + "\"");
                }
                else
                {
                    switch (content)
                    {
                        case LOGIN:
                            User user = new User(sender);
                            user.setStatus(User.Status.ONLINE);

                            message.setSender(SERVER);
                            message.setReceiver(sender);

                            // Send a welcome message
                            message.setType(Message.Type.MESSAGE);
                            message.setContent("Welcome " + sender + "!");
                            Server.sendMessage(message, clientSocket);

                            // We want to notify the other users when a client has logged in
                            message.setType(Message.Type.COMMAND);
                            message.setReceiver(EVERYONE);
                            message.setContent(listener.onClientLogin(user));
                            Server.broadcast(message);

                            listener.onMessageReceived(user.getName() + " logged in");
                            break;
                        case GET_USERS:
                            message.setSender(SERVER);
                            message.setReceiver(sender);
                            message.setType(Message.Type.COMMAND);
                            message.setContent(listener.onGetUsersRequested());

                            Server.sendMessage(message, clientSocket);
                            System.out.println("> Sent list with users");
                            break;
                    }
                }
            }
            catch (IOException e)
            {
                // Will go here when a client exits their application
                User user = new User(sender);
                user.setStatus(User.Status.OFFLINE);

                // Notify the other users when a client has logged out
                message = new Message();
                message.setSender(sender);
                message.setReceiver(EVERYONE);
                message.setType(Message.Type.COMMAND);
                message.setContent("user(" + user.getName() + "="
                        + user.getStatus().toString() + ")");
                Server.broadcast(message);

                listener.onMessageReceived(user.getName() + " logged out");
                terminate();
            }
        }
    }

    private void sleep250ms()
    {
        try {
            Thread.sleep(250);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    Socket getSocket()
    {
        return clientSocket;
    }

    private String getSender()
    {
        return sender;
    }

    private String getReceiver()
    {
        return receiver;
    }

    private void terminate()
    {
        isRunning = false;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof ClientHandler)
        {
            ClientHandler thatClient = (ClientHandler) obj;
            return thatClient.getSender().equals(receiver) && !thatClient.getReceiver().equals(receiver);
        }
        return false;
    }
}
