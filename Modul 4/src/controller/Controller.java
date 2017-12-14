package controller;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.*;
import task.*;
import view.PoolIndicator;
import view.PoolMonitorView;
import view.ReceptionView;

import java.io.*;
import java.util.ArrayList;

/**
 * Handles user interactions and controls the user interface by for example
 * changing it when a user clicks the button in ReceptionView.
 * Created by Jonas Eiselt on 2017-12-01.
 */
public class Controller implements Listener
{
    private static final int ADVENTURE_POOL_LIMIT = 10;
    private static final int COMMON_POOL_LIMIT = 15;

    private PoolMonitorView poolMonitorView;
    private ReceptionView.State currentState;
    private ReceptionView.State nextState = ReceptionView.State.CLOSED;

    private ArrayList<String> listWithNames;

    private ReceptionQueue receptionQueue;
    private AdventurePoolQueue adventurePoolQueue;
    private CommonPoolQueue commonPoolQueue;
    private ExitQueue exitQueue;

    private Janitor janitor;

    public Controller(PoolMonitorView poolMonitorView)
    {
        this.poolMonitorView = poolMonitorView;
    }

    public void initializeView()
    {
        String defaultFontStyle = "-fx-font-family: 'Droid Sans'; "
                + "-fx-font-size: 15pt;";

        poolMonitorView.setListener(this);

        poolMonitorView.initializeView();
        poolMonitorView.setStyle(defaultFontStyle);

        listWithNames = getNamesFromFile();
    }

    /** Reads a text file with common first names and returns an array list with the names. */
    private ArrayList<String> getNamesFromFile()
    {
        ArrayList<String> list = new ArrayList<>();
        try
        {
            BufferedReader r = new BufferedReader(new InputStreamReader(
                    new FileInputStream("src/files/firstnames.txt"), "UTF-8"));

            while (true)
            {
                String line = r.readLine();
                if (line == null)
                    break;

                System.out.println(line);
                list.add(line);
            }
            r.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void stopActiveThreads()
    {
        System.out.println("Stopping active threads!");

        if (receptionQueue != null)
            receptionQueue.terminate();

        if (adventurePoolQueue != null)
            adventurePoolQueue.terminate();

        if (commonPoolQueue != null)
            commonPoolQueue.terminate();

        if (exitQueue != null)
            exitQueue.terminate();
    }

    /** Handles the start-close-pools button click. */
    @Override
    public void onButtonClicked()
    {
        if (currentState == ReceptionView.State.OPENED)
        {
            nextState = ReceptionView.State.CLOSED;
            poolMonitorView.closePools();

            // Don't let people in...
            receptionQueue.closePools();
        }
        else
        {
            // Make sure the program isn't already running
            if (janitor == null || !janitor.isRunning())
            {
                int[] inputs = poolMonitorView.getInputs();
                nextState = ReceptionView.State.OPENED;
                poolMonitorView.openPools();

                poolMonitorView.setVisitorLimit(Pool.ADVENTURE, ADVENTURE_POOL_LIMIT);
                poolMonitorView.setVisitorLimit(Pool.COMMON, COMMON_POOL_LIMIT);

                startThreads(inputs);
            }
            else
                showWarningDialog();
        }
        currentState = nextState;
    }

    private void showWarningDialog()
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("There are still people inside the facility!");

        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/files/poolicon.png"));

        alert.showAndWait();
    }

    /** Changes the queue count in reception view. */
    @Override
    public void onCustomersWaitingChanged(Pool pool, int customersWaiting)
    {
        Platform.runLater(() -> poolMonitorView.setCustomersWaiting(pool, customersWaiting));
    }

    /** Changes the state of the indicator that is to the left of the pool view. */
    @Override
    public void onPoolEntranceClosed(Pool pool, PoolIndicator.State poolState)
    {
        Platform.runLater(() -> poolMonitorView.setPoolEntranceClosed(pool, poolState));
    }

    /** Changes the state of the indicator that is placed over/below the pool view. */
    @Override
    public void onPoolSwitchClosed(Pool pool, PoolIndicator.State poolState)
    {
        Platform.runLater(() -> poolMonitorView.setPoolSwitchClosed(pool, poolState));
    }

    /** Changes a pool's visitor count. */
    @Override
    public void onVisitorsInPoolChanged(Pool pool, int visitors)
    {
        Platform.runLater(() -> poolMonitorView.setVisitorsInPool(pool, visitors));
    }

    /** Changes the customer count which is exiting facility. */
    @Override
    public void onCustomersExitingChanged(Pool pool, int customersExiting)
    {
        Platform.runLater(() -> poolMonitorView.setCustomersExiting(pool, customersExiting));
    }

    /** Changes the color (state) of the exit sign. The color is either red or green. */
    @Override
    public void onVisitorsLeft(Pool pool, boolean visitorsLeft)
    {
        Platform.runLater(() -> poolMonitorView.setVisitorsLeft(pool, visitorsLeft));
    }

    /** Stops all active threads when no customer is in the facility. */
    @Override
    public void onNoCustomerLeft()
    {
        stopActiveThreads();
    }

    /**
     * Starts all threads. The parameter inputs is the values entered
     * from the SettingsView.
     */
    private void startThreads(int[] inputs)
    {
        // Shared variables
        Reception reception = new Reception();

        AdventurePool adventurePool = new AdventurePool(ADVENTURE_POOL_LIMIT);
        CommonPool commonPool = new CommonPool(COMMON_POOL_LIMIT);

        Exit exit = new Exit();

        // Reception runnable and thread
        receptionQueue = new ReceptionQueue(reception, inputs[0]);
        receptionQueue.handOverListWithNames(listWithNames);
        receptionQueue.setListener(this);

        Thread receptionThread = new Thread(receptionQueue);
        receptionThread.start();

        // Adventure runnable and thread
        adventurePoolQueue = new AdventurePoolQueue(adventurePool, commonPool, reception,
                exit, inputs[1], inputs[3]);
        adventurePoolQueue.setListener(this);

        Thread adventureThread = new Thread(adventurePoolQueue);
        adventureThread.start();

        // Common runnable and thread
        commonPoolQueue = new CommonPoolQueue(commonPool, adventurePool, reception,
                exit, inputs[2], inputs[4]);
        commonPoolQueue.setListener(this);

        Thread commonThread = new Thread(commonPoolQueue);
        commonThread.start();

        // Exit runnable and thread
        exitQueue = new ExitQueue(exit, reception, adventurePool, commonPool, inputs[5]);
        exitQueue.setListener(this);

        Thread exitThread = new Thread(exitQueue);
        exitThread.start();

        // App runnable and thread
        janitor = new Janitor(reception, adventurePool, commonPool, exit);
        janitor.setListener(this);

        Thread appThread = new Thread(janitor);
        appThread.start();
    }
}
