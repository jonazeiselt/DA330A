package model;

import java.util.Random;

/**
 * A class used to represent a gate at an airport.
 * Created by Jonas Eiselt on 2017-11-30.
 */
public class Gate
{
    private static String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H"};

    private String letter;
    private Integer number;

    private void setLetter(String letter)
    {
        this.letter = letter;
    }

    private void setNumber(Integer number)
    {
        this.number = number;
    }

    /** Generates and returns a random gate containing a letter and a number. */
    public static Gate rand()
    {
        Random rand = new Random();
        String randLetter = letters[rand.nextInt(letters.length)]; // A... H
        Integer randNumber = rand.nextInt(59) + 1;  // 1... 60

        Gate gate = new Gate();
        gate.setLetter(randLetter);
        gate.setNumber(randNumber);

        return gate;
    }

    /**
     * Assumes the parameter's first character is a letter and its other characters
     * is a number (eg A60).
     */
    public static Gate parse(String gateData)
    {
        String parseLetter = gateData.substring(0, 1);
        Integer parseNumber = Integer.parseInt(gateData.substring(1));

        Gate gate = new Gate();
        gate.setLetter(parseLetter);
        gate.setNumber(parseNumber);

        return gate;
    }

    @Override
    public String toString()
    {
        return letter + number;
    }
}
