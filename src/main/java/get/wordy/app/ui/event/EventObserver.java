package get.wordy.app.ui.event;

import get.wordy.app.ui.IReloadable;

public interface EventObserver {
	
	String getName();
	
	void update();
	
	IReloadable getObserver();
	
}