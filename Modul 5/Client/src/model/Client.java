package model;

import other.Listener;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Connects to server and transmits messages to server.
 * Created by Jonas Eiselt on 2017-12-06.
 */
public class Client implements Runnable
{
    private final String name;
    private final int portNumber;
    private boolean isRunning = true;

    private Socket clientSocket;
    private Listener listener;

    public Client(String name, int portNumber)
    {
        this.name = name;
        this.portNumber = portNumber;
    }

    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    @Override
    public void run()
    {
        openSocket();

        if (clientSocket != null)
        {
            Message message = new Message();
            message.setSender(name);
            message.setReceiver("Server");
            message.setType(Message.Type.COMMAND);

            // Send login details to server
            message.setContent("login");
            sendMessage(message);

            // Request a list of users on server
            message.setContent("get_users");
            sendMessage(message);

            // Handle incoming messages from server
            ServerHandler serverHandler = new ServerHandler(clientSocket);
            serverHandler.setListener(listener);
            serverHandler.start();

            // Handle outgoing messages to server
            while(isRunning)
            {
                message = listener.getMessageToSend();
                if (message != null && message.getContent() != null && !message.getContent().equals(""))
                {
                    if (sendMessage(message))
                        System.out.println("> Sent message");
                    else
                        listener.onExceptionCaught("Couldn't send message. " +
                                "Try restarting the application!");
                }
                sleep250ms();
            }

            serverHandler.terminate();
            closeSocket();
        }
        else
            listener.onLoginFailed();

        listener.onClientStopped();
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

    private void openSocket()
    {
        try {
            clientSocket = new Socket("localhost", portNumber);
        }
        catch (IOException e) {
            listener.onExceptionCaught("Couldn't connect to server. App will shut down momentarily!");
        }
    }

    private boolean sendMessage(Message message)
    {
        try
        {
            if (message.getContent() != null && !message.getContent().trim().equals(""))
            {
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                out.writeUTF(message.toString());
            }
        }
        catch (IOException e) {
            return false;
        }
        return true;
    }

    private void closeSocket()
    {
        try {
            clientSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning()
    {
        return isRunning;
    }

    public void terminate()
    {
        isRunning = false;
    }
}
