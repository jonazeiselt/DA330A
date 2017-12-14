package view;

import javafx.application.Platform;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.ReadersWritersApp;
import model.Flight;

/**
 * Used to combine three different views: a view for displaying the
 * main flight board, a view for allowing user interaction, and one for
 * displaying information in form of text in a text area.
 * Created by Jonas Eiselt on 2017-11-27.
 */
public class FlightSimulationView extends HBox
{
    private FlightBoardView flightBoardView;
    private ControlBoardView controlBoardView;
    private LogBoardView logBoardView;

    public void initializeView(ViewListener viewListener)
    {
        flightBoardView = new FlightBoardView(10);
        flightBoardView.initializeView();
        flightBoardView.setPrefWidth(900);

        TitledPane tp1 = new TitledPane();
        tp1.setText("Flight Board");
        tp1.setCollapsible(false);
        tp1.setContent(flightBoardView);

        controlBoardView = new ControlBoardView();
        controlBoardView.initializeView(viewListener);

        TitledPane tp2 = new TitledPane();
        tp2.setText("Control Board");
        tp2.setCollapsible(false);
        tp2.setContent(controlBoardView);

        logBoardView = new LogBoardView();
        logBoardView.initializeView();
        logBoardView.setPrefSize(550, ReadersWritersApp.getHeight());

        TitledPane tp3 = new TitledPane();
        tp3.setText("Log Board");
        tp3.setCollapsible(false);
        tp3.setContent(logBoardView);
        tp3.setPrefHeight(Double.MAX_VALUE);

        VBox infoAndCmdBox = new VBox();
        infoAndCmdBox.getChildren().addAll(tp1, tp2);

        getChildren().addAll(infoAndCmdBox, tp3);
    }

    public void addFlightToBoard(Flight flight)
    {
        Platform.runLater(() -> flightBoardView.addFlight(flight));
    }

    public void updateFlightBoard(Flight newFlight, int index)
    {
        Platform.runLater(() -> flightBoardView.updateFlight(newFlight, index));
    }

    public void updateStatusLabel(StatusLabel.LabelStatus labelStatus, String name)
    {
        Platform.runLater(() -> controlBoardView.updateStatusLabel(labelStatus, name));
    }

    public void disableClearButton(boolean disable)
    {
        controlBoardView.disableClearButton(disable);
    }

    /** Value from a numeric text field. */
    public Integer getNumberOfFlightsToWrite() throws NumberFormatException
    {
        return controlBoardView.getNumberOfFlightsToWrite();
    }

    public void appendString(String string)
    {
        Platform.runLater(() -> logBoardView.appendString(string));
    }

    public void clearFlightAndLogBoard()
    {
        flightBoardView.clearBoard();
        logBoardView.clear();
    }
}
