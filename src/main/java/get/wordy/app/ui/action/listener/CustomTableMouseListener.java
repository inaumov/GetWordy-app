package get.wordy.app.ui.action.listener;

import get.wordy.app.ui.BasePresenter;
import get.wordy.app.ui.action.EditCardAction;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomTableMouseListener extends EditCardAction implements MouseListener {

    public CustomTableMouseListener(BasePresenter basePresenter) {
        super(basePresenter);
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        super.selectWord();
        if (event.getClickCount() == 2 && !event.isConsumed()) {
            event.consume();
            super.action();
        }
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

}
