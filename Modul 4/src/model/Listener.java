package model;

import view.PoolIndicator;

/**
 * Created by Jonas Eiselt on 2017-12-04.
 */
public interface Listener
{
    void onButtonClicked();
    void onCustomersWaitingChanged(Pool pool, int customersWaiting);

    void onPoolEntranceClosed(Pool adventure, PoolIndicator.State poolState);
    void onPoolSwitchClosed(Pool common, PoolIndicator.State poolState);

    void onVisitorsInPoolChanged(Pool pool, int visitors);
    void onCustomersExitingChanged(Pool pool, int customersExiting);
    void onVisitorsLeft(Pool adventure, boolean visitorsLeft);

    void onNoCustomerLeft();
}
