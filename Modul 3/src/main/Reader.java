package main;

import model.Counter;
import model.Flight;
import model.ThreadListener;

import java.util.Random;

/**
 * Used to select an element at a position in buffer depending on
 * the value of the Counter object.
 * Created by Jonas Eiselt on 2017-11-27.
 */
public class Reader implements Runnable
{
    private final String name;

    private FlightBuffer buffer;
    private volatile Counter counter;

    private ThreadListener threadListener;
    private volatile boolean isRunning = true;

    public Reader(String name, FlightBuffer buffer)
    {
        this.name = name;
        this.buffer = buffer;
    }

    public void setCounter(Counter counter)
    {
        this.counter = counter;
    }

    /**
     * Provides Reader with a ThreadListener so the Controller can be notified in
     * order for the gui to be updated.
     */
    public void setThreadListener(ThreadListener threadListener)
    {
        this.threadListener = threadListener;
    }

    @Override
    public void run()
    {
        Random rand = new Random();
        while (isRunning)
        {
            // Thread is awake, perform read
            threadListener.onReadingStarted(name);
            Flight flight = buffer.read(counter.get());
            sleep(rand.nextInt(500));

            // Flight has now been read, update GUI
            threadListener.onReadingUpdate(name, flight);
            sleep(rand.nextInt(500));

            // Put thread to sleep, update GUI
            threadListener.onReadingCompleted(name);
            sleep(rand.nextInt(500));
        }
    }

    /** Push the thread into a sleep-mode. */
    private void sleep(long milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void terminate()
    {
        isRunning = false;
    }

    public boolean isRunning()
    {
        return isRunning;
    }
}
