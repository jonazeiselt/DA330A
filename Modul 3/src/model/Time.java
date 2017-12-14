package model;

import java.util.Random;

/**
 * A class used to represent a 12-hour time (AM/PM).
 * Created by Jonas Eiselt on 2017-11-27.
 */
public class Time
{
    private int hour;
    private int minute;

    private String amOrPm;

    private void setHour(int hour)
    {
        this.hour = hour;
    }

    private void setMinute(int minute)
    {
        this.minute = minute;
    }

    private void setAmOrPm(String amOrPm)
    {
        this.amOrPm = amOrPm;
    }

    /** Assumes the parameter has the format "HH:mm AM" or "HH:mm PM". */
    public static Time parse(String timeString)
    {
        /* We assume the argument is "HH:mm AM" */
        String[] timeData = timeString.split(":");
        String[] minuteData = timeData[1].split(" ");

        int hour = Integer.parseInt(timeData[0]);
        int minute = Integer.parseInt(minuteData[0]);

        Time time = new Time();
        time.setHour(hour);
        time.setMinute(minute);
        time.setAmOrPm(minuteData[1]);

        return time;
    }

    /** Generates and returns a random time (12-hour time). */
    public static Time rand()
    {
        Random rand = new Random();

        Time time = new Time();
        time.setHour(rand.nextInt(12));
        time.setMinute(rand.nextInt(60));

        int randAmOrPm = rand.nextInt(2);
        if (randAmOrPm == 0)
            time.setAmOrPm("AM");
        else
            time.setAmOrPm("PM");

        return time;
    }

    @Override
    public String toString()
    {
        String minutePrefix = "";
        if (minute < 10)
            minutePrefix = "0";

        return hour + ":" + minutePrefix + minute + " " + amOrPm;
    }
}
