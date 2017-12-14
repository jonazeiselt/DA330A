package model;

/**
 * Used to represent a flight with data members for eg changing its
 * gate number or for changing its departure time.
 * Created by Jonas Eiselt on 2017-11-27.
 */
public class Flight
{
    private int flightNumber;
    private Time departureTime;
    private String destination;
    private Gate gateNumber;
    private FlightStatus flightStatus;

    public int getFlightNumber()
    {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber)
    {
        this.flightNumber = flightNumber;
    }

    public Time getDepartureTime()
    {
        return departureTime;
    }

    public void setDepartureTime(Time departureTime)
    {
        this.departureTime = departureTime;
    }

    public String getDestination()
    {
        return destination;
    }

    public void setDestination(String destination)
    {
        this.destination = destination;
    }

    public Gate getGateNumber()
    {
        return gateNumber;
    }

    public void setGateNumber(Gate gateNumber)
    {
        this.gateNumber = gateNumber;
    }

    public FlightStatus getFlightStatus()
    {
        return flightStatus;
    }

    public void setFlightStatus(FlightStatus flightStatus)
    {
        this.flightStatus = flightStatus;
    }

    /** Returns a copy of the current instance. */
    public Flight copy()
    {
        Flight copy = new Flight();
        copy.setFlightNumber(flightNumber);
        copy.setDepartureTime(departureTime);
        copy.setDestination(destination);
        copy.setGateNumber(gateNumber);
        copy.setFlightStatus(flightStatus);

        return copy;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Flight)
        {
            Flight f2 = (Flight) obj;
            return flightNumber == f2.getFlightNumber();    // the flight number is unique
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "Flight No: " + flightNumber + "\nDeparture Time: " + departureTime + "\nDestination: " + destination
                + "\nGate No: " + gateNumber + "\nFlight Status: " + flightStatus;
    }
}
