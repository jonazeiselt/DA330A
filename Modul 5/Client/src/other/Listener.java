package other;

import model.Message;
import model.User;

import java.util.ArrayList;

public interface Listener
{
    Message getMessageToSend();
    void onMessageReceived(Message receivedMessage);

    void onLoginFailed();
    void onClientStopped();
    void onExceptionCaught(String errorMessage);

    void onUserReceived(User user);
    void onUserListReceived(ArrayList<User> userList);

    void onUserListClicked(String name);
}
