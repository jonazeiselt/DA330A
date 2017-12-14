package model;

import java.util.ArrayList;

/**
 * Protocol for message transmission between server and
 * client.
 * Created by Jonas Eiselt on 2017-12-09.
 */
class MessageProtocol
{
    private static final String USER = "user";
    private static final String USERS = "users";

    private Message receivedMessage;
    private Command receivedCommand = Command.NONE;

    private User receivedUser;
    private ArrayList<User> receivedUsers;

    enum Command { NONE, USER, USERS }

    /**
     * Messages that can be received are of the format:
     * 1) sender=...,receiver=...,msg=...
     * 2) sender=...,receiver=...,cmd=user(name=onlineStatus)
     * 3) sender=...,receiver=...,cmd=users(user,user,...)
     */
    void analyzeReceivedMessage(String messageReceived)
    {
        int firstEqualSign = messageReceived.indexOf("=");
        int firstCommaSign = messageReceived.indexOf(",");

        int secondEqualSign = messageReceived.indexOf("=",firstEqualSign+1);
        int secondCommaSign = messageReceived.indexOf(",",firstCommaSign+1);

        int thirdEqualSign = messageReceived.indexOf("=",secondEqualSign+1);

        String sender = messageReceived.substring(firstEqualSign+1, firstCommaSign);
        String receiver = messageReceived.substring(secondEqualSign+1, secondCommaSign);

        String cmdOrMsg = messageReceived.substring(secondCommaSign+1, thirdEqualSign);
        String content = messageReceived.substring(thirdEqualSign+1);

        receivedMessage = new Message();
        receivedMessage.setSender(sender);
        receivedMessage.setReceiver(receiver);
        receivedMessage.setContent(content);

        if (cmdOrMsg.equals("cmd"))
        {
            receivedMessage.setType(Message.Type.COMMAND);
            analyzeReceivedCommand(content);
        }
        else
            receivedMessage.setType(Message.Type.MESSAGE);
    }

    private void analyzeReceivedCommand(String content)
    {
        String typeOfCmd = content.substring(0, content.indexOf("("));
        if (typeOfCmd.equals(USER))
        {
            receivedCommand = Command.USER;
            setUser(content);
        }
        else if (typeOfCmd.equals(USERS))
        {
            receivedCommand = Command.USERS;
            setUserList(content);
        }
    }

    /** userContent has the format "user(name=onlineStatus)" */
    private void setUser(String userContent)
    {
        int firstParanthesesSign = userContent.indexOf("(");
        int secondParanthesesSign = userContent.indexOf(")");

        String userData = userContent.substring(firstParanthesesSign+1, secondParanthesesSign);
        String[] fragmentedUser = userData.split("=");

        receivedUser = new User(fragmentedUser[0]);
        receivedUser.setStatus(User.Status.parse(fragmentedUser[1]));
    }

    private void setUserList(String allUserContent)
    {
        receivedUsers = new ArrayList<>();
        int firstParanthesesSign = allUserContent.indexOf("(");
        int secondParanthesesSign = allUserContent.indexOf(")");

        String allUsers = allUserContent.substring(firstParanthesesSign+1, secondParanthesesSign);
        String[] fragmentedUsers = allUsers.split(",");

        for (String userContent : fragmentedUsers)
        {
            String[] fragmentedUser = userContent.split("=");

            User user = new User(fragmentedUser[0]);
            user.setStatus(User.Status.parse(fragmentedUser[1]));

            receivedUsers.add(user);
        }
    }

    public Message getReceivedMessage()
    {
        return receivedMessage;
    }

    User getUser()
    {
        return receivedUser;
    }

    ArrayList<User> getUserList()
    {
        return receivedUsers;
    }

    Command receivedCommand()
    {
        return receivedCommand;
    }
}
