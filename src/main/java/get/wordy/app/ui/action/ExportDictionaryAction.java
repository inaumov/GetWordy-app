package get.wordy.app.ui.action;

import get.wordy.app.ui.BasePresenter;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class ExportDictionaryAction extends AbstractAction {

    private BasePresenter basePresenter;

    public ExportDictionaryAction(BasePresenter basePresenter) {
        this.basePresenter = basePresenter;
    }

    @Override
    public void actionPerformed(ActionEvent action) {

    }

}