package view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;
import model.User;
import other.Listener;
import other.Theme;

import java.util.ArrayList;

/**
 * View for displaying active users on the server.
 * Created by Jonas Eiselt on 2017-12-09.
 */
class UserListView extends VBox
{
    private Listener listener;
    private ArrayList<User> allUsers;

    private Font regularFont;
    private Label headerLabel;

    UserListView(Listener listener)
    {
        this.listener = listener;
        allUsers = new ArrayList<>();
    }

    void initializeView()
    {
        regularFont = Theme.getRegularFont(18);

        headerLabel = new Label("USERS—" + allUsers.size());
        headerLabel.setPadding(new Insets(0,20,5,20));
        headerLabel.setFont(regularFont);
        headerLabel.setStyle("-fx-text-fill: #a2a3a6;");

        // Styling...
        setSpacing(5);
        setPadding(new Insets(30,5,30,5));
        setPrefWidth(300);
        setStyle("-fx-background-color: #2e3136;");
        getChildren().add(headerLabel);
    }

    private HBox getUserBox(String name, String status)
    {
        ImageView userImageView = new ImageView(new Image("/files/user.png"));
        userImageView.setFitHeight(45);
        userImageView.setPreserveRatio(true);
        userImageView.setSmooth(true);

        Label nameLabel = new Label(name);
        Label statusLabel = new Label(status);

        nameLabel.setStyle("-fx-text-fill: #a2a3a6;");
        nameLabel.setFont(regularFont);

        statusLabel.setStyle("-fx-text-fill: #a2a3a6;");
        statusLabel.setFont(Theme.getRegularFont(14));

        VBox userBox = new VBox();
        userBox.getChildren().addAll(nameLabel, statusLabel);

        HBox root = new HBox(10);
        root.setPadding(new Insets(5,20,5,20));

        final boolean[] mouseEntered = {false};
        root.setOnMouseClicked(event ->
        {
            listener.onUserListClicked(name);
            root.setStyle("-fx-background-color: #525860;");
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100), arg0 ->
            {
                if (mouseEntered[0])
                    root.setStyle("-fx-background-color: #3b3f45;");
                else
                    root.setStyle("-fx-background-color: #2e3136;");

            }));

            timeline.play();
        });

        root.setOnMouseEntered(event ->
        {
            mouseEntered[0] = true;
            root.setStyle("-fx-background-color: #3b3f45;");
        });
        root.setOnMouseExited(event ->
        {
            mouseEntered[0] = false;
            root.setStyle("-fx-background-color: #2e3136;");
        });

        root.getChildren().addAll(userImageView, userBox);

        return root;
    }

    void updateUserList(ArrayList<User> userList)
    {
        allUsers.clear();
        getChildren().remove(1, getChildren().size());

        for (User user : userList)
            addUser(user);
    }

    private void addUser(User user)
    {
        allUsers.add(user);
        getChildren().add(getUserBox(user.getName(), user.getStatus().toString()));

        headerLabel.setText("USERS—" + allUsers.size());
    }
}
