package get.wordy.app.ui.action.listener;

import java.awt.event.*;

public abstract class WordFieldListenerAdapter extends FocusListenerAdapter implements MouseListener, KeyListener {

	@Override
	public void keyPressed(KeyEvent arg0) {
		focusGained();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
        focusGained();
    }

	@Override
	public void mouseClicked(MouseEvent arg0) {
        focusGained();
    }

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusGained(FocusEvent arg0) {
        focusGained();
	}

	@Override
	public void focusLost(FocusEvent arg0) {
        focusLost();
    }

}
