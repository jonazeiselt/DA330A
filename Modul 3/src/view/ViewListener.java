package view;

public interface ViewListener
{
    void onButtonClicked(ButtonValue buttonValue);

    enum ButtonValue
    {
        START, STOP, CLEAR
    }
}
