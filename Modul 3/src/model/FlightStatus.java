package model;

import java.util.Random;

/**
 * Used to represent the various flight status at an airport.
 * Created by Jonas Eiselt on 2017-11-27.
 */
public enum FlightStatus
{
    CHECKING_IN {
        @Override
        public String toString() {
            return "Checking in";
        }
    },
    BOARDING {
        @Override
        public String toString() {
            return "Boarding";
        }
    },
    DEPARTED {
        @Override
        public String toString() {
            return "Departed";
        }
    },
    LANDED {
        @Override
        public String toString() {
            return "Landed";
        }
    };

    public static FlightStatus parse(String flightStatus)
    {
        FlightStatus fs;
        switch (flightStatus)
        {
            case "Checking in":
                fs = CHECKING_IN;
                break;
            case "Boarding":
                fs = BOARDING;
                break;
            case "Departed":
                fs = DEPARTED;
                break;
            case "Landed":
                fs = LANDED;
                break;
            default:
                fs = LANDED;
        }
        return fs;
    }

    /** Generates and returns a random flight status. */
    public static FlightStatus rand()
    {
        return FlightStatus.values()[new Random().nextInt(FlightStatus.values().length)];
    }
}
