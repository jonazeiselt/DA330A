package main;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import view.PoolMonitorView;

/**
 * Class for running the application by displaying the main window.
 * Created by Jonas Eiselt on 2017-12-01.
 */
public class PoolMonitorApp extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        PoolMonitorView poolMonitorView = new PoolMonitorView();

        Controller controller = new Controller(poolMonitorView);
        controller.initializeView();

        stage.setTitle("Pool Monitor App");
        stage.setScene(new Scene(poolMonitorView, 1200,600));

        try {
            stage.getIcons().add(new Image("/files/poolicon.png"));
        }
        catch (IllegalArgumentException e) {
            // Ignore if file path is invalid
        }

        stage.setResizable(false);
        stage.show();

        stage.setOnCloseRequest(e ->
        {
            controller.stopActiveThreads();
            System.exit(0);
        });
    }
}
