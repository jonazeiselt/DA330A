package task;

import model.Customer;
import model.Listener;
import model.Pool;
import model.Reception;

import java.util.ArrayList;
import java.util.Random;

/**
 * Generates a random customer at regular intervals.
 * Created by Jonas Eiselt on 2017-12-01.
 */
public class ReceptionQueue implements Runnable
{
    private static final int MAX_AGE = 80;
    private static final int MIN_AGE = 6;

    private final int msTime;

    private Reception reception;
    private Random random;

    private ArrayList<String> listWithNames;
    private Listener listener;

    private volatile boolean isRunning = true;
    private volatile boolean poolsAreOpen = true;

    public ReceptionQueue(Reception reception, int msTime)
    {
        this.reception = reception;
        this.msTime = msTime;

        random = new Random();
    }

    public void handOverListWithNames(ArrayList<String> listWithNames)
    {
        this.listWithNames = listWithNames;
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
            while (poolsAreOpen)
            {
                Customer customer = generateRandomCustomer();
                if (customer.isVip() && Pool.rand() == Pool.ADVENTURE)
                {
                    reception.addToAdventureLine(customer);
                    listener.onCustomersWaitingChanged(Pool.ADVENTURE,
                            reception.getCustomersWaiting(Pool.ADVENTURE));
                }
                else
                {
                    reception.addToCommonLine(customer);
                    listener.onCustomersWaitingChanged(Pool.COMMON,
                            reception.getCustomersWaiting(Pool.COMMON));
                }
                sleepForSomeTime();
            }
            sleepForSomeTime();
        }
    }

    private Customer generateRandomCustomer()
    {
        Customer customer = new Customer(random.nextBoolean());
        customer.setName(listWithNames.get(random.nextInt(listWithNames.size())));
        customer.setAge(random.nextInt((MAX_AGE - MIN_AGE) + 1) + MIN_AGE);

        System.out.println(customer);

        return customer;
    }

    /**
     * Pushes the thread into sleep-mode for some period of time, depending
     * on the value of msTime.
     */
    private void sleepForSomeTime()
    {
        try {
            Thread.sleep(msTime);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** Closes the reception. */
    public void closePools()
    {
        poolsAreOpen = false;
        reception.setReceptionOpen(false);
    }

    public void terminate()
    {
        isRunning = false;
    }
}
