package other;

import model.Message;
import model.User;

public interface Listener
{
    Message getMessageToSend();
    void onMessageReceived(String receivedMessage);
    String onGetUsersRequested();

    String onClientLogin(User user);

    void onExceptionCaught(String exceptionMessage);
}
