package view;

import com.sun.javafx.scene.traversal.Direction;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Listener;
import model.Pool;

/**
 * Created by Jonas Eiselt on 2017-12-01.
 */
public class PoolMonitorView extends HBox
{
    private Listener listener;

    private SettingsView settingsView;
    private ReceptionView receptionView;
    private AdventurePoolView adventurePoolView;
    private CommonPoolView commonPoolView;

    private PoolIndicator upIndicator;
    private PoolIndicator downIndicator;

    private HBox indicatorBox;

    public void initializeView()
    {
        settingsView = new SettingsView();
        settingsView.initializeView();

        receptionView = new ReceptionView(listener);
        receptionView.initializeView();

        adventurePoolView = new AdventurePoolView();
        adventurePoolView.initializeView();

        commonPoolView = new CommonPoolView();
        commonPoolView.initializeView();

        HBox switchPoolView = getSwitchPoolView();

        VBox poolsView = new VBox(47);
        poolsView.getChildren().addAll(adventurePoolView, switchPoolView, commonPoolView);

        setSpacing(40);
        setPadding(new Insets(20));
        getChildren().addAll(settingsView, receptionView, poolsView);
    }

    private HBox getSwitchPoolView()
    {
        upIndicator = new PoolIndicator(Direction.UP);
        HBox upBox = (HBox) upIndicator.getIndicator(PoolIndicator.State.OFF);

        downIndicator = new PoolIndicator(Direction.DOWN);
        HBox downBox = (HBox) downIndicator.getIndicator(PoolIndicator.State.OFF);

        indicatorBox = new HBox(10);
        indicatorBox.setAlignment(Pos.CENTER);
        indicatorBox.getChildren().addAll(downBox, upBox);

        return indicatorBox;
    }

    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    public void setCustomersWaiting(Pool pool, int customersWaiting)
    {
        receptionView.setCustomersWaiting(pool, customersWaiting);
    }

    public void openPools()
    {
        receptionView.openPools(true);

        setPoolEntranceClosed(Pool.ADVENTURE, PoolIndicator.State.ON);
        setPoolEntranceClosed(Pool.COMMON, PoolIndicator.State.ON);

        setPoolSwitchClosed(Pool.ADVENTURE, PoolIndicator.State.ON);
        setPoolSwitchClosed(Pool.COMMON, PoolIndicator.State.ON);
    }

    public void closePools()
    {
        receptionView.openPools(false);
    }

    public void setPoolEntranceClosed(Pool pool, PoolIndicator.State poolState)
    {
        if (pool == Pool.ADVENTURE)
            adventurePoolView.setPoolEntranceClosed(poolState);
        else
            commonPoolView.setPoolEntranceClosed(poolState);
    }

    public void setPoolSwitchClosed(Pool pool, PoolIndicator.State poolState)
    {
        if (pool == Pool.ADVENTURE)
        {
            HBox upBox = (HBox) upIndicator.getIndicator(poolState);
            indicatorBox.getChildren().remove(1);
            indicatorBox.getChildren().add(1, upBox);
        }
        else
        {
            HBox downBox = (HBox) downIndicator.getIndicator(poolState);
            indicatorBox.getChildren().remove(0);
            indicatorBox.getChildren().add(0, downBox);
        }
    }

    public void setVisitorsInPool(Pool pool, int visitors)
    {
        if (pool == Pool.ADVENTURE)
            adventurePoolView.setVisitorsInPool(visitors);
        else
            commonPoolView.setVisitorsInPool(visitors);
    }

    public void setCustomersExiting(Pool pool, int customersExiting)
    {
        if (pool == Pool.ADVENTURE)
            adventurePoolView.setCustomersExiting(customersExiting);
        else
            commonPoolView.setCustomersExiting(customersExiting);
    }

    public void setVisitorLimit(Pool pool, int visitorLimit)
    {
        if (pool == Pool.ADVENTURE)
            adventurePoolView.setVisitorLimit(visitorLimit);
        else
            commonPoolView.setVisitorLimit(visitorLimit);
    }

    public void setVisitorsLeft(Pool pool, boolean visitorsLeft)
    {
        if (pool == Pool.ADVENTURE)
            adventurePoolView.setVisitorsLeft(visitorsLeft);
        else
            commonPoolView.setVisitorsLeft(visitorsLeft);
    }

    public int[] getInputs()
    {
        int[] inputs = new int[6];
        inputs[0] = settingsView.getNewCustomerRate();
        inputs[1] = settingsView.getAdvPoolProcRate();
        inputs[2] = settingsView.getCommPoolProcRate();
        inputs[3] = settingsView.getAdvPoolExitRate();
        inputs[4] = settingsView.getCommPoolExitRate();
        inputs[5] = settingsView.getAvgExitRate();

        return inputs;
    }
}
