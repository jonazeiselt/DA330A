package main;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import view.FlightSimulationView;

/**
 * Main file. Displays flights that are stored in a text file
 * in a visual flight board and modifies those flights by
 * readers and writers.
 * Created by Jonas Eiselt on 2017-11-27.
 */
public class ReadersWritersApp extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        FlightSimulationView flightSimulationView = new FlightSimulationView();

        Controller controller = new Controller(flightSimulationView);
        controller.initializeView();

        stage.setTitle("Readers/Writers App");
        stage.setScene(new Scene(flightSimulationView, getWidth(), getHeight()));

        try {
            stage.getIcons().add(new Image("/files/flighticon.png"));
        }
        catch (IllegalArgumentException e) {
            // Ignore if file path is invalid
        }

        stage.setResizable(false);
        stage.show();

        stage.setOnCloseRequest(e ->
        {
            controller.stopRunningThreads();
            System.exit(0);
        });
    }

    private static double getWidth()
    {
        return 1400;
    }

    public static double getHeight()
    {
        return 850;
    }
}
