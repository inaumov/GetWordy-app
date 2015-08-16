package get.wordy.app.ui.event;

import get.wordy.app.ui.IReloadable;

public class FontChangedEvent implements EventObserver {

	private IReloadable reloadable;

	public FontChangedEvent(IReloadable reloadable) {
		this.reloadable = reloadable;
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void update() {
		reloadable.reloadFont();
	}

	@Override
	public IReloadable getObserver() {
		return reloadable;
	}

}
