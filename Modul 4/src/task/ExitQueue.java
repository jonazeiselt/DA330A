package task;

import model.*;

/**
 * Represents the customer exit event. It removes two customer (one
 * from each pool) at regular intervals.
 * Created by Jonas Eiselt on 2017-12-01.
 */
public class ExitQueue implements Runnable
{
    private final int exitTime;

    private Exit exit;
    private Reception reception;
    private AdventurePool adventurePool;
    private CommonPool commonPool;

    private Listener listener;
    private volatile boolean isRunning = true;

    public ExitQueue(Exit exit, Reception reception, AdventurePool adventurePool, CommonPool commonPool, int exitTime)
    {
        this.exit = exit;
        this.reception = reception;
        this.adventurePool = adventurePool;
        this.commonPool = commonPool;

        this.exitTime = exitTime;
    }

    /** Now the Controller can be notified. */
    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    @Override
    public void run()
    {
        while (isRunning)
        {
            removeFromExitPoolQueue(Pool.ADVENTURE);
            removeFromExitPoolQueue(Pool.COMMON);

            sleepForSomeTime();
        }
    }

    /**
     * Removes customer from exit pool queue and notifies the listener
     * about the event.
     */
    private void removeFromExitPoolQueue(Pool pool)
    {
        Customer customer = exit.removeVisitor(pool);
        if (customer != null)
        {
            listener.onCustomersExitingChanged(pool,
                    exit.getCustomersExiting(pool));
            System.out.println(customer + " is exiting!");
        }

        if (noVisitorsLeft(pool))
            listener.onVisitorsLeft(pool, false);
        else
            listener.onVisitorsLeft(pool, true);

    }

    /**
     * Checks and returns whether there are any visitors left in the reception,
     * pool or in the pool's exit area.
     */
    private boolean noVisitorsLeft(Pool pool)
    {
        if (reception.isOpen())
            return false;

        boolean noneInWaitingRoom = reception.getCustomersWaiting(pool) == 0;
        boolean noneInPool, noneInExitArea;
        if (pool == Pool.ADVENTURE)
        {
            noneInPool = adventurePool.getVisitorsInPool() == 0;
            noneInExitArea = exit.getCustomersExiting(Pool.ADVENTURE) == 0;
        }
        else
        {
            noneInPool = commonPool.getVisitorsInPool() == 0;
            noneInExitArea = exit.getCustomersExiting(Pool.COMMON) == 0;
        }

        return noneInWaitingRoom && noneInPool && noneInExitArea;
    }

    /**
     * Pushes the thread into sleep-mode for some period of time, depending
     * on the value of exitTime.
     */
    private void sleepForSomeTime()
    {
        try {
            Thread.sleep(exitTime);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void terminate()
    {
        isRunning = false;
    }
}
