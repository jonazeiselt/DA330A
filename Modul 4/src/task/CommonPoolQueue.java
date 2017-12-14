package task;

import model.*;
import view.PoolIndicator;

import java.util.Random;

/**
 * Handles customer events such as, admitting it into the common pool and
 * admitting it into the exit area. It also notifies a listener about those events.
 * Created by Jonas Eiselt on 2017-12-01.
 */
public class CommonPoolQueue implements Runnable
{
    private static final int EXIT = 0;

    private final int processingTime;
    private final int exitRate;

    private volatile CommonPool commonPool;
    private volatile AdventurePool adventurePool;
    private volatile Reception reception;
    private volatile Exit exit;

    private final Random random;

    private Listener listener;
    private boolean isRunning = true;

    public CommonPoolQueue(CommonPool commonPool, AdventurePool adventurePool, Reception reception, Exit exit,
                           int processingTime, int exitRate)
    {
        this.commonPool = commonPool;
        this.adventurePool = adventurePool;
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

            if (commonPool.isFull() || random.nextInt(exitRate) == EXIT)
                goToExit();
            else if (Pool.rand() == Pool.COMMON)
                enterCommonPool();
            else
                switchToAdventurePool();

            sleepForSomeTime();
        }
    }

    /**
     * Checks the entrance to the common pool and its switch, in order for the user
     * interface to be correctly updated.
     */
    private void checkEntranceAndSwitch()
    {
        if (commonPool.isFull())
        {
            listener.onPoolSwitchClosed(Pool.COMMON, PoolIndicator.State.OFF);
            listener.onPoolEntranceClosed(Pool.COMMON, PoolIndicator.State.OFF);
        }
        else
        {
            listener.onPoolSwitchClosed(Pool.COMMON, PoolIndicator.State.ON);
            if (!reception.isOpen())
            {
                if (reception.getCustomersWaiting(Pool.COMMON) == 0)
                    listener.onPoolEntranceClosed(Pool.COMMON, PoolIndicator.State.OFF);
                else
                    listener.onPoolEntranceClosed(Pool.COMMON, PoolIndicator.State.ON);
            }
            else if (!commonPool.isFull())
                listener.onPoolEntranceClosed(Pool.COMMON, PoolIndicator.State.ON);
        }
    }

    /** Gets the next customer in the reception and adds it to the common pool. */
    private void enterCommonPool()
    {
        Customer customer = reception.getNextInLine(Pool.COMMON);
        if (customer != null)
        {
            commonPool.addVisitor(customer);

            listener.onCustomersWaitingChanged(Pool.COMMON,
                    reception.getCustomersWaiting(Pool.COMMON));
            listener.onVisitorsInPoolChanged(Pool.COMMON,
                    commonPool.getVisitorsInPool());
            System.out.println(customer + " entered common pool!");
        }
    }

    /** Gets the next customer from common pool and adds it to the exit queue. */
    private void goToExit()
    {
        Customer customer = commonPool.getNextToExit();
        if (customer != null)
        {
            exit.addVisitor(Pool.COMMON, customer);
            listener.onCustomersExitingChanged(Pool.COMMON,
                    exit.getCustomersExiting(Pool.COMMON));
            listener.onVisitorsInPoolChanged(Pool.COMMON,
                    commonPool.getVisitorsInPool());

            System.out.println(customer + " went to the exit!");
        }
    }

    /**
     * Gets the next VIP-customer from the common pool and adds it to adventure
     * pool.
     */
    private void switchToAdventurePool()
    {
        Customer customer = commonPool.getNextToAdventurePool();
        if (customer != null)
        {
            // Update visitor count for common pool and adventure pool
            adventurePool.addVisitor(customer);
            listener.onVisitorsInPoolChanged(Pool.COMMON,
                    commonPool.getVisitorsInPool());
            listener.onVisitorsInPoolChanged(Pool.ADVENTURE,
                    adventurePool.getVisitorsInPool());

            System.out.println(customer + " switched to the adventure pool!");
        }
    }

    /**
     * Pushes the thread into sleep-mode for some period of time, depending
     * on the value of prcessingTime.
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
