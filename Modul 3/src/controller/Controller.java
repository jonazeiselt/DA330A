package controller;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.FlightBuffer;
import main.Reader;
import main.Writer;
import model.*;
import view.FlightSimulationView;
import view.StatusLabel;
import view.ViewListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Handles user interaction with the three different views (see java
 * files in package view). Used to read flights stored in a text
 * file.
 * Created by Jonas Eiselt on 2017-11-27.
 */
public class Controller implements ViewListener, ThreadListener
{
    private final FlightSimulationView flightSimulationView;
    private final ArrayList<Flight> flightsInDatabase;

    private Reader reader1;
    private Reader reader2;
    private Reader reader3;

    private Writer writer1;
    private Writer writer2;

    public Controller(FlightSimulationView flightSimulationView)
    {
        this.flightSimulationView = flightSimulationView;

        flightsInDatabase = getAllFlightsFromFile();
        System.out.println("No of flights: " + flightsInDatabase.size());
    }

    public void initializeView()
    {
        String defaultFontStyle = "-fx-font-family: 'Droid Sans'; "
                + "-fx-font-size: 15pt;";

        flightSimulationView.initializeView(this);
        flightSimulationView.setStyle(defaultFontStyle);
    }

    /** Gets all flights stored in text file. */
    private ArrayList<Flight> getAllFlightsFromFile()
    {
        ArrayList<Flight> flights = new ArrayList<>();
        try
        {
            BufferedReader r = new BufferedReader(new InputStreamReader(
                    new FileInputStream("src/files/flights.txt"), "UTF-8"));

            while (true)
            {
                String line = r.readLine();
                if (line == null)
                    break;

                String[] flightData = line.split(",");

                Flight flight = new Flight();
                flight.setFlightNumber(Integer.parseInt(flightData[0]));
                flight.setDepartureTime(Time.parse(flightData[1]));
                flight.setDestination(flightData[2]);
                flight.setGateNumber(Gate.parse(flightData[3]));
                flight.setFlightStatus(FlightStatus.parse(flightData[4]));

                System.out.println(flight);
                flights.add(flight);
            }
            r.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return flights;
    }

    @Override
    public void onButtonClicked(ButtonValue buttonValue)
    {
        switch (buttonValue)
        {
            case START:
                try
                {
                    int numberOfFlights = flightSimulationView.getNumberOfFlightsToWrite();
                    if (numberOfFlights == 0 || numberOfFlights > flightsInDatabase.size())
                    {
                        showDialog(Alert.AlertType.ERROR, flightsInDatabase.size() + " flights stored " +
                                "at the moment. Try again with a number that is between 0 and " +
                                + (flightsInDatabase.size() + 1));
                    }
                    else
                    {
                        // OK
                        if (isAnyReaderRunning() || isAnyWriterRunning())
                            showDialog(Alert.AlertType.WARNING, "Program already running!");
                        else
                        {
                            flightSimulationView.clearFlightAndLogBoard();
                            ArrayList<Flight> loadedFlights = loadAndDisplayFlights(numberOfFlights);

                            flightSimulationView.disableClearButton(true);
                            startWritersAndReaders(loadedFlights);
                        }
                    }
                }
                catch (NumberFormatException e) {
                    showDialog(Alert.AlertType.ERROR,"The input must a positive integer...");
                }
                break;
            case STOP:
                stopRunningThreads();
                flightSimulationView.disableClearButton(false);
                break;
            case CLEAR:
                flightSimulationView.clearFlightAndLogBoard();
        }
    }

    private boolean isAnyReaderRunning()
    {
        return (reader1 != null && reader1.isRunning()) || (reader2 != null && reader2.isRunning())
                || (reader3 != null && reader3.isRunning());
    }

    private boolean isAnyWriterRunning()
    {
        return (writer1 != null && writer1.isRunning()) || (writer2 != null && writer2.isRunning());
    }

    private void startWritersAndReaders(ArrayList<Flight> flights)
    {
        FlightBuffer buffer = new FlightBuffer(flights);
        Counter counter = new Counter(flights.size());

        reader1 = new Reader("Reader 1", buffer);
        reader1.setCounter(counter);
        reader1.setThreadListener(this);

        Thread reader1Thread = new Thread(reader1);
        reader1Thread.start();

        reader2 = new Reader("Reader 2", buffer);
        reader2.setCounter(counter);
        reader2.setThreadListener(this);

        Thread reader2Thread = new Thread(reader2);
        reader2Thread.start();

        reader3 = new Reader("Reader 3", buffer);
        reader3.setCounter(counter);
        reader3.setThreadListener(this);

        Thread reader3Thread = new Thread(reader3);
        reader3Thread.start();

        writer1 = new Writer("Writer 1", buffer);
        writer1.setThreadListener(this);

        Thread writer1Thread = new Thread(writer1);
        writer1Thread.start();

        writer2 = new Writer("Writer 2", buffer);
        writer2.setThreadListener(this);

        Thread writer2Thread = new Thread(writer2);
        writer2Thread.start();
    }

    /**
     * Copies numberOfFlights of flights to a local array list which is used to
     * display them on the visual flight board.
     */
    private ArrayList<Flight> loadAndDisplayFlights(int numberOfFlights)
    {
        ArrayList<Flight> flights = new ArrayList<>();
        for (int i = 0; i < numberOfFlights; i++)
            flights.add(flightsInDatabase.get(i));

        Platform.runLater(() ->
        {
            for (Flight flight : flights)
            {
                flightSimulationView.addFlightToBoard(flight);

                try {
                    Thread.sleep(50);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return flights;
    }

    /** Displays a pop-up dialog. */
    private void showDialog(Alert.AlertType alertType, String message)
    {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setTitle("Information");

        try {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/files/flighticon.png"));
        }
        catch (IllegalArgumentException e) {
            // Ignore if file path is invalid
        }
        alert.showAndWait();
    }

    @Override
    public void onReadingStarted(String name)
    {
        flightSimulationView.appendString(name + " is awake and waiting\n");
        flightSimulationView.updateStatusLabel(StatusLabel.LabelStatus.WAITING, name);
    }

    @Override
    public void onReadingUpdate(String name, Flight flight)
    {
        flightSimulationView.updateFlightBoard(flight, flightsInDatabase.indexOf(flight));
        flightSimulationView.appendString(name + " has updated flight " + flight.getFlightNumber() +
                " on flight board\n");

        flightSimulationView.updateStatusLabel(StatusLabel.LabelStatus.COMPLETED, name);
    }

    @Override
    public void onReadingCompleted(String name)
    {
        flightSimulationView.appendString(name + " is put to sleep\n");
        flightSimulationView.updateStatusLabel(StatusLabel.LabelStatus.DEFAULT, name);
    }

    @Override
    public void onWritingStarted(String name)
    {
        flightSimulationView.updateStatusLabel(StatusLabel.LabelStatus.WAITING, name);
        flightSimulationView.appendString(name + " is awake and waiting\n");
    }

    @Override
    public void onWritingUpdate(String name, Flight flight)
    {
        flightSimulationView.appendString(name + " has changed flight " + flight.getFlightNumber() + "\n");
        flightSimulationView.updateStatusLabel(StatusLabel.LabelStatus.COMPLETED, name);
    }

    @Override
    public void onWritingCompleted(String name, Flight newFlight)
    {
        flightSimulationView.appendString(name + " is put to sleep\n");
        flightSimulationView.updateStatusLabel(StatusLabel.LabelStatus.DEFAULT, name);
    }

    public void stopRunningThreads()
    {
        stopReaderThreads();
        stopWriterThreads();
    }

    private void stopReaderThreads()
    {
        if (reader1 != null)
        {
            reader1.terminate();
            reader1 = null;
        }

        if (reader2 != null)
        {
            reader2.terminate();
            reader2 = null;
        }

        if (reader3 != null)
        {
            reader3.terminate();
            reader3 = null;
        }
    }

    private void stopWriterThreads()
    {
        if (writer1 != null)
        {
            writer1.terminate();
            writer1 = null;
        }

        if (writer2 != null)
        {
            writer2.terminate();
            writer2 = null;
        }
    }
}
