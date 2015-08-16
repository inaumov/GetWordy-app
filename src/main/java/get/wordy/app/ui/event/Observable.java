package get.wordy.app.ui.event;

import get.wordy.app.ui.IReloadable;

public interface Observable {

    boolean attach(IReloadable IReloadable, EventObserver eventObserver);

    boolean detach(IReloadable IReloadable, EventObserver eventObserver);

    void fireEvent(String eventName);
}