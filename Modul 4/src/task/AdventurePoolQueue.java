package task;

import model.*;
import view.PoolIndicator;

import java.util.Random;

/**
 * Handles customer events such as, admitting it into the adventure pool and
 * admitting it into the exit area. It also notifies a listener about those events.
 * Created by Jonas Eiselt on 2017-12-01.
 */
public class AdventurePoolQueue implements Runnable
{
    private static final int EXIT = 0;

    private final int processingTime;
    private final int exitRate;

    private volatile AdventurePool adventurePool;
    private volatile CommonPool commonPool;
    private volatile Reception reception;
    private volatile Exit exit;

    private final Random random;

    private Listener listener;
    private boolean isRunning = true;

    public AdventurePoolQueue(AdventurePool adventurePool, CommonPool commonPool, Reception reception, Exit exit,
                              int processingTime, int exitRate)
    {
        this.adventurePool = adventurePool;
        this.commonPool = commonPool;
        this.reception = reception;
        this.exit = exit;

        this.processingTime = processingTime;
        this.exitRate = exitRate;

        random = new Random();
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
            checkEntranceAndSwitch();

            if (adventurePool.isFull() || random.nextInt(exitRate) == EXIT)
                goToExit();
            if (Pool.rand() == Pool.ADVENTURE)
                enterAdventurePool();
            else
                switchToCommonPool();

            sleepForSomeTime();
        }
    }

    /**
     * Checks the entrance to the adventure pool and its switch, in order for the
     * user interface to be correctly updated.
     */
    private void checkEntranceAndSwitch()
    {
        if (adventurePool.isFull())
        {
            listener.onPoolSwitchClosed(Pool.ADVENTURE, PoolIndicator.State.OFF);
            listener.onPoolEntranceClosed(Pool.ADVENTURE, PoolIndicator.State.OFF);
        }
        else
        {
            listener.onPoolSwitchClosed(Pool.ADVENTURE, PoolIndicator.State.ON);
            if (!reception.isOpen())
            {
                if (reception.getCustomersWaiting(Pool.ADVENTURE) == 0)
                    listener.onPoolEntranceClosed(Pool.ADVENTURE, PoolIndicator.State.OFF);
                else
                    listener.onPoolEntranceClosed(Pool.ADVENTURE, PoolIndicator.State.ON);
            }
            else if (!adventurePool.isFull())
                listener.onPoolEntranceClosed(Pool.ADVENTURE, PoolIndicator.State.ON);
        }
    }

    /**
     * Gets the next VIP-customer in the reception and adds it to the adventure
     * pool.
     */
    private void enterAdventurePool()
    {
        Customer customer = reception.getNextInLine(Pool.ADVENTURE);
        if (customer != null)
        {
            adventurePool.addVisitor(customer);
            listener.onCustomersWaitingChanged(Pool.ADVENTURE,
                    reception.getCustomersWaiting(Pool.ADVENTURE));
            listener.onVisitorsInPoolChanged(Pool.ADVENTURE,
                    adventurePool.getVisitorsInPool());

            System.out.println(customer + " entered adventure pool!");
        }
    }

    /** Gets the next customer from adventure pool and adds it to the exit queue. */
    private void goToExit()
    {
        Customer customer = adventurePool.getNextToExit();
        if (customer != null)
        {
            exit.addVisitor(Pool.ADVENTURE, customer);
            listener.onCustomersExitingChanged(Pool.ADVENTURE,
                    exit.getCustomersExiting(Pool.ADVENTURE));
            listener.onVisitorsInPoolChanged(Pool.ADVENTURE,
                    adventurePool.getVisitorsInPool());

            System.out.println(customer + " went to the exit!");
        }
    }

    /**
     * Gets the next VIP-customer from the adventure pool and adds it to common
     * pool.
     */
    private void switchToCommonPool()
    {
        Customer customer = adventurePool.getNextToCommonPool();
        if (customer != null)
        {
            // Update visitor count for adventure pool and common pool
            commonPool.addVisitor(customer);
            listener.onVisitorsInPoolChanged(Pool.ADVENTURE,
                    adventurePool.getVisitorsInPool());
            listener.onVisitorsInPoolChanged(Pool.COMMON,
                    commonPool.getVisitorsInPool());

            System.out.println(customer + " switched to the common pool!");
        }
    }

    /**
     * Pushes the thread into sleep-mode for some period of time, depending
     * on the value of processingTime.
     */
    private void sleepForSomeTime()
    {
        try {
            Thread.sleep(processingTime);
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
