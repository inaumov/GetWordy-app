package get.wordy.app.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class ExitAction extends AbstractAction {

    public ExitAction() {

    }

    @Override
    public void actionPerformed(ActionEvent action) {
        System.exit(0);
    }

}