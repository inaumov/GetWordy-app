package get.wordy.app.ui.action;

import get.wordy.app.ui.BasePresenter;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class StartExerciseAction extends AbstractAction {

    private BasePresenter basePresenter;

    public StartExerciseAction(BasePresenter basePresenter) {
        this.basePresenter = basePresenter;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        basePresenter.startExercise();
    }

}