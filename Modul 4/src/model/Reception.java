package model;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents the waiting area in the swimming facility which doesn't
 * have a customer limit.
 * Created by Jonas Eiselt on 2017-12-01.
 */
public class Reception
{
    private volatile Queue<Customer> adventureQueue;
    private volatile Queue<Customer> commonQueue;

    private boolean receptionOpen;

    public Reception()
    {
        adventureQueue = new LinkedList<>();
        commonQueue = new LinkedList<>();

        receptionOpen = true;
    }

    /** Removes and returns customer which is first in queue for pool. */
    public synchronized Customer getNextInLine(Pool pool)
    {
        if (pool == Pool.ADVENTURE)
            return adventureQueue.poll();
        else
            return commonQueue.poll();
    }

    /** Adds customer last in queue for the adventure pool. */
    public synchronized void addToAdventureLine(Customer customer)
    {
        adventureQueue.add(customer);
    }

    /** Adds customer last in queue for the common pool. */
    public synchronized void addToCommonLine(Customer customer)
    {
        commonQueue.add(customer);
    }

    /** Returns the number of customers waiting for a specific pool. */
    public synchronized int getCustomersWaiting(Pool pool)
    {
        if (pool == Pool.ADVENTURE)
            return adventureQueue.size();
        else
            return commonQueue.size();
    }

    public boolean isOpen()
    {
        return receptionOpen;
    }

    public void setReceptionOpen(boolean receptionOpen)
    {
        this.receptionOpen = receptionOpen;
    }
}
