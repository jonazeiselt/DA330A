package model;

import other.Listener;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Handles incoming messages from server.
 * Created by Jonas Eiselt on 2017-12-07.
 */
public class ServerHandler extends Thread
{
    private Socket clientSocket;
    private Listener listener;

    private boolean isRunning = true;

    ServerHandler(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    void setListener(Listener listener)
    {
        this.listener = listener;
    }

    @Override
    public void run()
    {
        DataInputStream in;
        while(isRunning)
        {
            try
            {
                in = new DataInputStream(clientSocket.getInputStream());
                String receivedMessage = in.readUTF();

                MessageProtocol messageProtocol = new MessageProtocol();
                messageProtocol.analyzeReceivedMessage(receivedMessage);

                Message message = messageProtocol.getReceivedMessage();
                Message.Type type = message.getType();

                if (type == Message.Type.MESSAGE)
                    listener.onMessageReceived(message);
                else
                {
                    MessageProtocol.Command command = messageProtocol.receivedCommand();
                    // Single user has been received
                    if (command == MessageProtocol.Command.USER)
                        listener.onUserReceived(messageProtocol.getUser());
                    // List with users has been received
                    else if (command == MessageProtocol.Command.USERS)
                        listener.onUserListReceived(messageProtocol.getUserList());
                }
            }
            catch (IOException e) {
                listener.onExceptionCaught("Lost connection to server!");
                terminate();
            }
        }
    }

    void terminate()
    {
        isRunning = false;
    }
}
