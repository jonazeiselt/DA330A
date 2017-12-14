package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.Flight;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;

/**
 * Used to display a flight board with flight data. Maintains a list
 * of the flights that are displayed.
 * Created by Jonas Eiselt on 2017-11-27.
 */
class FlightBoardView extends VBox
{
    private static final double FLIGHT_NO_WIDTH = 150;
    private static final double DEPT_TIME_WIDTH = 150;
    private static final double DESTINATION_WIDTH = 300;
    private static final double GATE_NO_WIDTH = 100;
    private static final double FLIGHT_STATUS_WIDTH = 200;

    private LinkedList<Flight> flightQueue = new LinkedList<>();
    private final int capacity;

    private Font pixelFont;
    private Font airportFont;

    /** Limits the number of rows that can be displayed. */
    FlightBoardView(int capacity)
    {
        this.capacity = capacity;
    }

    /** Initializes the board view with styled header and styled empty rows. */
    void initializeView()
    {
        try {
            airportFont = Font.loadFont(new FileInputStream(new File(
                    "src/files/bebas.ttf")), 20);
            pixelFont = Font.loadFont(new FileInputStream(new File(
                    "src/files/pixelmix.ttf")), 16);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        HBox boardHeader = getBoardHeader();

        getChildren().add(boardHeader);
        for (int i = 0; i < capacity; i++)
            getChildren().add(getBoardEntry(null));

        setSpacing(10);
        setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to bottom, #595959, #333333);");
    }

    /** Displays the board header, with the white text color. */
    private HBox getBoardHeader()
    {
        Label flightNumberLabel = new Label("Flight No");
        flightNumberLabel.setPrefWidth(FLIGHT_NO_WIDTH);

        Label departureTimeLabel = new Label("Time");
        departureTimeLabel.setAlignment(Pos.CENTER);
        departureTimeLabel.setPrefWidth(DEPT_TIME_WIDTH);

        Label destinationLabel = new Label("Destination");
        destinationLabel.setPrefWidth(DESTINATION_WIDTH);

        Label gateNumberLabel = new Label("Gate");
        gateNumberLabel.setAlignment(Pos.CENTER);
        gateNumberLabel.setPrefWidth(GATE_NO_WIDTH);

        Label flightStatusLabel = new Label("Status");
        flightStatusLabel.setPrefWidth(FLIGHT_STATUS_WIDTH);

        setLabelStyle("white", flightNumberLabel, departureTimeLabel, destinationLabel,
                gateNumberLabel, flightNumberLabel, flightStatusLabel);

        HBox root = new HBox(20);
        root.getChildren().addAll(flightNumberLabel, departureTimeLabel, destinationLabel,
                gateNumberLabel, flightStatusLabel);
        root.setStyle("-fx-padding: 0 10 0 10; -fx-background-color: linear-gradient(to bottom, #22252b, #383e47); " +
                "-fx-border-color: black; -fx-border-width: 1px;");

        return root;
    }

    /** Displays a row with flight data. */
    private HBox getBoardEntry(Flight flight)
    {
        Label flightNumberLabel = new Label("");
        flightNumberLabel.setPrefWidth(FLIGHT_NO_WIDTH);

        Label departureTimeLabel = new Label("");
        departureTimeLabel.setAlignment(Pos.CENTER);
        departureTimeLabel.setPrefWidth(DEPT_TIME_WIDTH);

        Label destinationLabel = new Label("");
        destinationLabel.setPrefWidth(DESTINATION_WIDTH);

        Label gateNumberLabel = new Label("");
        gateNumberLabel.setAlignment(Pos.CENTER);
        gateNumberLabel.setPrefWidth(GATE_NO_WIDTH);

        Label flightStatusLabel = new Label("");
        flightStatusLabel.setPrefWidth(FLIGHT_STATUS_WIDTH);

        if (flight != null)
        {
            flightNumberLabel.setText(Integer.toString(flight.getFlightNumber()));
            departureTimeLabel.setText(flight.getDepartureTime().toString());
            destinationLabel.setText(flight.getDestination());
            gateNumberLabel.setText(flight.getGateNumber().toString());
            flightStatusLabel.setText(flight.getFlightStatus().toString());
        }

        setLabelStyle("yellow", flightNumberLabel, departureTimeLabel, destinationLabel,
                gateNumberLabel, flightNumberLabel, flightStatusLabel);

        HBox root = new HBox(20);
        root.setStyle("-fx-padding: 0 10 0 10; -fx-background-color: linear-gradient(to left, #22252b, #383e47); " +
                "-fx-border-color: black; -fx-border-width: 1px;");

        root.getChildren().addAll(flightNumberLabel, departureTimeLabel, destinationLabel,
                gateNumberLabel, flightStatusLabel);

        return root;
    }

    /** Adds a specific style to label, depending what color is assigned. */
    private void setLabelStyle(String color, Label... labels)
    {
        if (color.equals("yellow"))
        {
            for (Label label : labels)
            {
                label.setStyle("-fx-padding: 5 0 5 0; -fx-text-fill: yellow;");
                if (pixelFont != null)
                    label.setFont(pixelFont);
            }
        }
        else
        {
            for (Label label : labels)
            {
                label.setStyle("-fx-padding: 5 0 5 0; -fx-text-fill: " + color + ";");
                if (airportFont != null)
                    label.setFont(airportFont);
            }
        }
    }

    /** Adds flight to board's own queue and updates its gui. */
    void addFlight(Flight flight)
    {
        if (flightQueue.size() >= capacity)
            flightQueue.removeFirst();

        flightQueue.addLast(flight);
        updateFlightBoardView(flightQueue);
    }

    /**
     * Will only allow certain amount of flights to be displayed on the flight
     * board's gui.
     */
    private void updateFlightBoardView(LinkedList<Flight> flights)
    {
        getChildren().remove(1, getChildren().size());
        for (Flight flight : flights)
            getChildren().add(getBoardEntry(flight));

        int diff = capacity - flights.size();
        for (int i = 0; i < diff; i++)
            getChildren().add(getBoardEntry(null));
    }

    /** Updates the flight in flight board's own queue and its gui. */
    void updateFlight(Flight newFlight, int index)
    {
        try
        {
            Flight f = flightQueue.get(index);
            f.setDepartureTime(newFlight.getDepartureTime());
            f.setGateNumber(newFlight.getGateNumber());
            f.setFlightStatus(newFlight.getFlightStatus());

            updateFlightBoardView(flightQueue);
        }
        catch (IndexOutOfBoundsException e) {
            // Do nothing...
        }
    }

    /** Clears the board's own queue and its rows with flight data. */
    void clearBoard()
    {
        getChildren().remove(1, getChildren().size());
        flightQueue = new LinkedList<>();

        for (int i = 0; i < capacity; i++)
            getChildren().add(getBoardEntry(null));
    }
}
