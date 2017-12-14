package model;

import java.util.ArrayList;

/**
 * Represents a user stored at the server side.
 * Created by Jonas Eiselt on 2017-12-09.
 */
public class User
{
    private String name;
    private Status onlineStatus = Status.ONLINE;

    private ArrayList<Message> messages;

    public enum Status
    {
        OFFLINE {
            @Override
            public String toString() {
                return "Offline";
            }
        },
        ONLINE {
            @Override
            public String toString() {
                return "Online";
            }
        };

        public static Status parse(String status)
        {
            if (status.equals("Online"))
                return ONLINE;
            else
                return OFFLINE;
        }
    }

    public User(String name)
    {
        this.name = name;
        messages = new ArrayList<>();
    }

    public String getName()
    {
        return name;
    }

    public Status getStatus()
    {
        return onlineStatus;
    }

    public void setStatus(Status onlineStatus)
    {
        this.onlineStatus = onlineStatus;
    }

    public void addMessage(Message message)
    {
        messages.add(message);
    }

    public ArrayList<Message> getMessages()
    {
        return messages;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof User)
        {
            User user = (User) obj;
            return name.equals(user.getName());
        }
        return false;
    }
}
