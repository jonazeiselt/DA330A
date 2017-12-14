package model;

/**
 * Protocol for message transmission between server and
 * client.
 * Created by Jonas Eiselt on 2017-12-09.
 */
class MessageProtocol
{
    private Message receivedMessage;

    /**
     * 1) sender=...,receiver=...,msg=...
     * 2) sender=...,receiver=...,cmd=...
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
        receivedMessage.setType(Message.Type.parse(cmdOrMsg));
        receivedMessage.setContent(content);
    }

    Message getReceivedMessage()
    {
        return receivedMessage;
    }
}
