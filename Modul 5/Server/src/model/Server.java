package model;

import other.Listener;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server which uses a thread pool to store its client
 * connections.
 * Created by Jonas Eiselt on 2017-12-06.
 */
public class Server implements Runnable
{
    private ExecutorService executorService;
    private static ArrayList<ClientHandler> allClients;

    private final int portNumber;
    private boolean isRunning = true;

    private ServerSocket serverSocket;
    private Listener listener;

    public Server(int portNumber)
    {
        this.portNumber = portNumber;
        executorService = Executors.newFixedThreadPool(3);

        allClients = new ArrayList<>();
    }

    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    @Override
    public void run()
    {
        openServerSocket();
        while(isRunning)
        {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            // Handle incoming and outgoing messages
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            clientHandler.setListener(listener);

            allClients.add(clientHandler);
            executorService.execute(clientHandler);
        }
        closeServerSocket();
    }

    private void openServerSocket()
    {
        try {
            serverSocket = new ServerSocket(portNumber);
        }
        catch (IOException e) {
            listener.onExceptionCaught("Server is already running!");
        }
    }

    static void broadcast(Message message)
    {
        System.out.println("> Broadcast message");
        for (int i = 0; i < allClients.size(); i++)
        {
            if (!sendMessage(message, allClients.get(i).getSocket()))
                allClients.remove(i);
        }
    }

    static void forwardMessage(Message message)
    {
        int index;
        if ((index = allClients.indexOf(new ClientHandler(message.getSender(), message.getReceiver()))) != -1)
        {
            Socket socket = allClients.get(index).getSocket();
            if (!sendMessage(message, socket))
                allClients.remove(index);
        }
        else
            System.err.println("Couldn't forward message...");
    }

    static boolean sendMessage(Message message, Socket socket)
    {
        try
        {
            if (message.getContent() != null && !message.getContent().trim().equals(""))
            {
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(message.toString());
            }
        }
        catch (IOException e) {
            return false;
        }
        return true;
    }

    private void closeServerSocket()
    {
        try {
            serverSocket.close();
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
