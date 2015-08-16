package get.wordy.app.ui.action.listener;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public abstract class FocusListenerAdapter implements FocusListener {

	public abstract void focusGained();
	public abstract void focusLost();

	@Override
	public void focusGained(FocusEvent e) {
		focusGained();
	}

	@Override
	public void focusLost(FocusEvent e) {
        focusLost();
    }

}
