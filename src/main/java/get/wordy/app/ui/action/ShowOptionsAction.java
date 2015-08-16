package get.wordy.app.ui.action;

import get.wordy.app.ui.BasePresenter;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ShowOptionsAction extends AbstractAction {

    private BasePresenter basePresenter;

    public ShowOptionsAction(BasePresenter basePresenter) {
        this.basePresenter = basePresenter;
    }

	@Override
	public void actionPerformed(ActionEvent action) {
        basePresenter.showOptionsDialog();
	}
}