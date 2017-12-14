package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.ReadersWritersApp;

/**
 * Used to display a view which can handle different user interactions, such
 * as handling button clicks and handling entered data in a numeric text
 * field (see NumberField in package view).
 * Created by Jonas Eiselt on 2017-11-28.
 */
class ControlBoardView extends HBox
{
    private ViewListener viewListener;
    private NumberField numberField;

    private StatusLabel w1Label;
    private StatusLabel w2Label;
    private StatusLabel r1Label;
    private StatusLabel r2Label;
    private StatusLabel r3Label;

    private Button clearButton;

    void initializeView(ViewListener viewListener)
    {
        this.viewListener = viewListener;

        VBox selectionBox = getInputBox();
        VBox buttonBox = getButtonBox();

        VBox inputBox = new VBox(20);
        inputBox.getChildren().addAll(selectionBox, buttonBox);
        inputBox.setPrefWidth(400);

        VBox infoBox = getStatusBox();
        infoBox.setPrefWidth(500);
        infoBox.setAlignment(Pos.CENTER);

        getChildren().addAll(inputBox, infoBox);

        setSpacing(20);
        setPadding(new Insets(10));
        setPrefHeight(ReadersWritersApp.getHeight());
    }

    private VBox getStatusBox()
    {
        w1Label = new StatusLabel();
        w1Label.setDefault("Writer 1: sleeping...");

        w2Label = new StatusLabel();
        w2Label.setDefault("Writer 2: sleeping...");

        r1Label = new StatusLabel();
        r1Label.setDefault("Reader 1: sleeping...");

        r2Label = new StatusLabel();
        r2Label.setDefault("Reader 2: sleeping...");

        r3Label = new StatusLabel();
        r3Label.setDefault("Reader 3: sleeping...");

        VBox root = new VBox(5);
        root.setPadding(new Insets(18,0,0,0));
        root.getChildren().addAll(w1Label, w2Label, r1Label, r2Label, r3Label);

        return root;
    }

    private VBox getInputBox()
    {
        Label label = new Label("Enter number of flights:");
        numberField = new NumberField();
        numberField.setOnKeyPressed(event ->
        {
            if (event.getCode() == KeyCode.ENTER)
                viewListener.onButtonClicked(ViewListener.ButtonValue.START);
        });

        VBox root = new VBox(5);
        root.getChildren().addAll(label, numberField);

        return root;
    }

    Integer getNumberOfFlightsToWrite() throws NumberFormatException
    {
        return numberField.getInt();
    }

    private VBox getButtonBox()
    {
        Button startButton = new Button("Start");
        Button stopButton = new Button("Stop");
        clearButton = new Button("Clear");

        startButton.setOnMouseClicked(event -> viewListener.onButtonClicked(ViewListener.ButtonValue.START));
        stopButton.setOnMouseClicked(event -> viewListener.onButtonClicked(ViewListener.ButtonValue.STOP));
        clearButton.setOnMouseClicked(event -> viewListener.onButtonClicked(ViewListener.ButtonValue.CLEAR));

        startButton.setPrefSize(Double.MAX_VALUE, 45);
        stopButton.setPrefSize(Double.MAX_VALUE, 45);
        clearButton.setPrefSize(Double.MAX_VALUE, 45);

        Separator separator = new Separator();
        separator.setPadding(new Insets(25,0,20,0));

        VBox root = new VBox(5);
        root.getChildren().addAll(startButton, stopButton, separator, clearButton);

        return root;
    }

    void updateStatusLabel(StatusLabel.LabelStatus labelStatus, String name)
    {
        switch (name)
        {
            case "Writer 1":
                w1Label.setStatus(labelStatus, name);
                break;
            case "Writer 2":
                w2Label.setStatus(labelStatus, name);
                break;
            case "Reader 1":
                r1Label.setStatus(labelStatus, name);
                break;
            case "Reader 2":
                r2Label.setStatus(labelStatus, name);
                break;
            case "Reader 3":
                r3Label.setStatus(labelStatus, name);
        }
    }

    void disableClearButton(boolean disable)
    {
        clearButton.setDisable(disable);
    }
}
