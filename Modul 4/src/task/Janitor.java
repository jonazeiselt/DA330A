package task;

import model.*;

/**
 * Checks whether there are any customers left in the facility.
 * Created by Jonas Eiselt on 2017-12-03.
 */
public class Janitor implements Runnable
{
    private Reception reception;
    private AdventurePool adventurePool;
    private CommonPool commonPool;
    private Exit exit;

    private Listener listener;
    private boolean isRunning = true;

    public Janitor(Reception reception, AdventurePool adventurePool, CommonPool commonPool, Exit exit)
    {
        this.reception = reception;
        this.adventurePool = adventurePool;
        this.commonPool = commonPool;
        this.exit = exit;
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
            // Check if there any customers left in the facility
            if (noCustomerLeft(Pool.ADVENTURE) && noCustomerLeft(Pool.COMMON))
            {
                listener.onNoCustomerLeft();
                isRunning = false;
            }
            sleep500ms();
        }
    }

    /**
     * Checks and returns whether there are any customers left in the corresponding
     * pool queues.
     */
    private boolean noCustomerLeft(Pool pool)
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



    private void sleep500ms()
    {
        try {
            Thread.sleep(500);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning()
    {
        return isRunning;
    }
}
