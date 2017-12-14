package main;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.User;
import view.ClientView;

import java.util.Locale;
import java.util.Optional;

/**
 * Main class that starts client.
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
        Locale.setDefault(Locale.ENGLISH);

        TextInputDialog dialog = new TextInputDialog("Jonas");
        dialog.setTitle("Login");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter name:");

        ((Stage) dialog.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/files/icon.png"));

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> startClient(stage, new User(name)));
    }

    private void startClient(Stage stage, User me)
    {
        ClientView clientView = new ClientView(me);
        Controller controller = new Controller(clientView);
        controller.initializeView();

        try {
            stage.getIcons().add(new Image("/files/icon.png"));
        }
        catch (IllegalArgumentException e) {
            // Ignore if file path is invalid
        }

        stage.setResizable(false);
        stage.setTitle("Client");
        stage.setScene(new Scene(clientView, 780, 690));
        stage.show();

        stage.setOnCloseRequest(event ->
        {
            controller.stopClient();
            System.exit(0);
        });

        controller.startClient(me.getName(), 4444);
    }
}
