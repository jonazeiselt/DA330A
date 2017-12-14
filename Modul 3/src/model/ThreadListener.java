package model;

public interface ThreadListener
{
    void onReadingStarted(String name);
    void onReadingUpdate(String name, Flight flight);
    void onReadingCompleted(String name);

    void onWritingStarted(String name);
    void onWritingUpdate(String name, Flight flight);
    void onWritingCompleted(String name, Flight writeIndex);
}
