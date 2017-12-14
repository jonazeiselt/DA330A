package model;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents the common pool which has a visitor (customer) limit.
 * Created by Jonas Eiselt on 2017-12-01.
 */
public class CommonPool
{
    private Queue<Customer> customerQueue;
    private int maxCustomers;

    public CommonPool(int maxCustomers)
    {
        customerQueue = new LinkedList<>();
        this.maxCustomers = maxCustomers;
    }

    /** Adds a customer to the common pool queue. */
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

    /** Gets the visitor count in common pool. */
    public synchronized int getVisitorsInPool()
    {
        return customerQueue.size();
    }

    /** Gets next Customer who wants to switch to adventure pool. */
    public synchronized Customer getNextToAdventurePool()
    {
        for (Customer customer : customerQueue)
        {
            if (customer.isVip())
            {
                if (customerQueue.remove(customer))
                {
                    notify();
                    return customer;
                }
            }
        }
        return null;
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
