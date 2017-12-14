package main;

import model.*;

import java.util.Random;

/**
 * Used to randomly select and change flights in a buffer until Writer
 * has been terminated.
 * Created by Jonas Eiselt on 2017-11-27.
 */
public class Writer implements Runnable
{
    private static final int WAYS_TO_CHANGE = 3;
    private static final int CHANGE_TIME = 0;
    private static final int CHANGE_GATE = 1;
    private static final int CHANGE_STATUS = 2;

    private final String name;
    private FlightBuffer buffer;

    private ThreadListener threadListener;
    private volatile boolean isRunning = true;

    public Writer(String name, FlightBuffer buffer)
    {
        this.name = name;
        this.buffer = buffer;
    }

    /**
     * Provides Writer with a ThreadListener so the Controller can be notified in
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
            // Thread is awake, perform write
            threadListener.onWritingStarted(name);

            // Select a random flight to modify
            int writeIndex = rand.nextInt(buffer.size());
            Flight flight = buffer.read(writeIndex);

            Flight newFlight = modifyFlight(flight, rand.nextInt(WAYS_TO_CHANGE));
            buffer.write(writeIndex, newFlight);
            sleep(rand.nextInt(500) + 500);

            // Flight has now been modified, update GUI
            threadListener.onWritingUpdate(name, newFlight);
            sleep(rand.nextInt(500) + 500);

            // Put thread to sleep, update GUI
            threadListener.onWritingCompleted(name, newFlight);
            sleep(rand.nextInt(500) + 500);
        }
    }

    /**
     * Modifies flight by changing one of its data members, depending on the
     * value of the function's second parameter.
     */
    private Flight modifyFlight(Flight flight, int whatToChange)
    {
        Flight newFlight = flight.copy();
        switch (whatToChange)
        {
            case CHANGE_TIME:
                newFlight.setDepartureTime(Time.rand());
                break;
            case CHANGE_GATE:
                newFlight.setGateNumber(Gate.rand());
                break;
            case CHANGE_STATUS:
                newFlight.setFlightStatus(FlightStatus.rand());
        }
        return newFlight;
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
