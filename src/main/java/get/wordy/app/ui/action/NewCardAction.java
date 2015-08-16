package get.wordy.app.ui.action;

import get.wordy.app.ui.BasePresenter;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NewCardAction extends AbstractAction {

    private BasePresenter basePresenter;

    public NewCardAction(BasePresenter basePresenter) {
        this.basePresenter = basePresenter;
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        basePresenter.createNewCard();
    }

}