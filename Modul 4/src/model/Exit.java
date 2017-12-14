package model;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents the exit area in the swimming facility.
 * Created by Jonas Eiselt on 2017-12-01.
 */
public class Exit
{
    private Queue<Customer> adventureExitQueue;
    private Queue<Customer> commonExitQueue;

    public Exit()
    {
        adventureExitQueue = new LinkedList<>();
        commonExitQueue = new LinkedList<>();
    }

    /** Adds a customer to an exit pool queue. */
    public synchronized void addVisitor(Pool pool, Customer customer)
    {
        if (pool == Pool.ADVENTURE)
            adventureExitQueue.add(customer);
        else
            commonExitQueue.add(customer);
    }

    /** Removes and returns a customer from an exit pool queue. */
    public synchronized Customer removeVisitor(Pool pool)
    {
        if (pool == Pool.ADVENTURE)
            return adventureExitQueue.poll();
        else
            return commonExitQueue.poll();
    }

    /** Returns the customer count which is about to exit from a pool. */
    public synchronized int getCustomersExiting(Pool pool)
    {
        if (pool == Pool.ADVENTURE)
            return adventureExitQueue.size();
        else
            return commonExitQueue.size();
    }
}
