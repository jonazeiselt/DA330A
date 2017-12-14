package model;

/**
 * Message that is sent between server and client.
 * Created by Jonas Eiselt on 2017-12-09.
 */
public class Message
{
    private String sender;
    private String receiver;

    private Type type;

    private String content;

    public enum Type
    {
        COMMAND {
            @Override
            public String toString() {
                return "cmd";
            }
        },
        MESSAGE {
            @Override
            public String toString() {
                return "msg";
            }
        };

        public static Type parse(String cmdOrMsg)
        {
            if (cmdOrMsg.equals("cmd"))
                return COMMAND;
            else
                return MESSAGE;
        }
    }

    public Message()
    {
        sender = "";
        receiver = "";
        type = Type.MESSAGE;
        content = "";
    }

    public String getSender()
    {
        return sender;
    }

    public void setSender(String sender)
    {
        this.sender = sender;
    }

    public String getReceiver()
    {
        return receiver;
    }

    public void setReceiver(String receiver)
    {
        this.receiver = receiver;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public boolean isFrom(String anotherSender)
    {
        return sender.equals(anotherSender);
    }

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "sender=" + sender + ",receiver=" + receiver + ","
                + type.toString() + "=" + content;
    }
}
