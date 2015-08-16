package get.wordy.app.ui.event;

import get.wordy.app.ui.IReloadable;

public class LocalizationChangedEvent implements EventObserver {

	private IReloadable reloadable;

	public LocalizationChangedEvent(IReloadable reloadable) {
		this.reloadable = reloadable;
	}
	
	@Override
	public String getName() {
		return LocalizationChangedEvent.class.getSimpleName();
	}

	@Override
	public void update() {
		reloadable.reloadText();
	}

	@Override
	public IReloadable getObserver() {
		return reloadable;
	}

}
