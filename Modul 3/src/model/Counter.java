package model;

/**
 * Represents an atomic counter and used for getting a next value in a
 * concurrent environment.
 * Created by Jonas Eiselt on 2017-11-30.
 */
public class Counter
{
    private final int size;
    private volatile int count;

    public Counter(int size)
    {
        this.size = size;
    }

    /** Return and increment */
    public synchronized int get()
    {
        if (count == size)
            count = 0;

        return count++;
    }
}
