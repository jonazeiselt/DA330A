package model;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents the adventure pool which has a visitor (customer) limit.
 * Created by Jonas Eiselt on 2017-12-01.
 */
public class AdventurePool
{
    private Queue<Customer> customerQueue;
    private int maxCustomers;

    public AdventurePool(int maxCustomers)
    {
        customerQueue = new LinkedList<>();
        this.maxCustomers = maxCustomers;
    }

    /** Adds a customer to the adventure pool queue. */
    public synchronized void addVisitor(Customer customer)
    {
        try
        {
            if (customerQueue.size() >= maxCustomers)
                wait();

            customerQueue.add(customer);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** Gets the visitor count in adventure pool. */
    public synchronized int getVisitorsInPool()
    {
        return customerQueue.size();
    }

    /** Gets next Customer who wants to switch to common pool. */
    public synchronized Customer getNextToCommonPool()
    {
        Customer customer = customerQueue.poll();
        notify();

        return customer;
    }

    /** Removes and returns Customer who has stayed the longest. */
    public synchronized Customer getNextToExit()
    {
        Customer customer = customerQueue.poll();
        notify();

        return customer;
    }

    /** Returns whether the adventure pool is full or not. */
    public synchronized boolean isFull()
    {
        return customerQueue.size() >= maxCustomers;
    }
}
