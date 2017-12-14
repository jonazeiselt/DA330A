package view;

import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import main.ReadersWritersApp;

/**
 * Used to display information in a text area.
 * Created by Jonas Eiselt on 2017-11-28.
 */
class LogBoardView extends VBox
{
    private TextArea textArea;

    void initializeView()
    {
        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefHeight(ReadersWritersApp.getHeight());

        getChildren().add(textArea);
    }

    void appendString(String string)
    {
        textArea.appendText(string);
    }

    void clear()
    {
        textArea.clear();
    }
}
