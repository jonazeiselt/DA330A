package main;

import controller.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import view.ServerView;

/**
 * Main class that starts server.
 * Created by Jonas Eiselt on 2017-12-06.
 */
public class Main extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        ServerView serverView = new ServerView();
        Controller controller = new Controller(serverView);
        controller.initializeView();

        try {
            stage.getIcons().add(new Image("/files/server.png"));
        }
        catch (IllegalArgumentException e) {
            // Ignore if file path is invalid
        }

        stage.setTitle("Server");
        stage.setScene(new Scene(serverView, 400, 500));
        stage.show();

        stage.setOnCloseRequest(event ->
        {
            controller.stopServer();
            System.exit(0);
        });

        Platform.runLater(() -> controller.startServer(4444));
    }
}
