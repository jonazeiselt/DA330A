package model;

import java.util.Random;

public enum Pool
{
    ADVENTURE, COMMON;

    /** Generates and returns a random pool type. */
    public static Pool rand()
    {
        return Pool.values()[new Random().nextInt(Pool.values().length)];
    }
}
