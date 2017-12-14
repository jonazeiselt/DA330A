package view;

import javafx.scene.control.TextField;

/**
 * Represents a text field which only permits positive numbers.
 * Created by Jonas Eiselt on 2017-11-28.
 */
public class NumberField extends TextField
{
    NumberField(String string)
    {
        super(string);
        setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; " +
                "-fx-border-color: #ccc; -fx-border-width: 1;");
    }

    @Override
    public void replaceText(int start, int end, String text)
    {
        if (matches(text))
            super.replaceText(start, end, text);
    }

    @Override
    public void replaceSelection(String replacement)
    {
        if (matches(replacement))
            super.replaceSelection(replacement);
    }

    private boolean matches(String text)
    {
        return text.matches("[0-9]*");
    }

    /** Returns a parsed integer of the entered text. */
    Integer getInt() throws NumberFormatException
    {
        return Integer.parseInt(getText());
    }
}
