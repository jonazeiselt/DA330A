package main;

import model.Flight;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Used to represent a buffer with multiple flights which can be accessed
 * by multiple reader threads whenever it is not being modified by a writer
 * thread (see Reader and Writer in package main).
 * Created by Jonas Eiselt on 2017-11-27.
 */
public class FlightBuffer
{
    private Semaphore mutex = new Semaphore(1);
    private Semaphore rwMutex = new Semaphore(1);

    private volatile int readers;
    private volatile ArrayList<Flight> buffer = new ArrayList<>();

    public FlightBuffer(ArrayList<Flight> buffer)
    {
        readers = 0;
        this.buffer = buffer;
    }

    /**
     * A flight at a specific index will be returned if there's no writing
     * done in the buffer. If there's another reader, it doesn't matter.
     */
    Flight read(Integer readIndex)
    {
        try
        {
            mutex.acquire();
            readers++;
            if (readers == 1)
                rwMutex.acquire();
            mutex.release();

            // Do some reading (critical section)
            Flight flight = buffer.get(readIndex);

            mutex.acquire();
            readers--;
            if (readers == 0)
                rwMutex.release();
            mutex.release();

            return flight;
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Only one writer will be allowed to modify buffer. If there's another
     * reader, the writer will be blocked.
     */
    void write(Integer writeIndex, Flight newFlight)
    {
        try
        {
            rwMutex.acquire();

            // Do some writing (critical section)
            Flight oldFlight = buffer.get(writeIndex);
            oldFlight.setDepartureTime(newFlight.getDepartureTime());
            oldFlight.setGateNumber(newFlight.getGateNumber());
            oldFlight.setFlightStatus(newFlight.getFlightStatus());

            rwMutex.release();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    int size()
    {
        return buffer.size();
    }
}
