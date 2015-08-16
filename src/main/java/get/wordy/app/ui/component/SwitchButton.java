package get.wordy.app.ui.component;

import get.wordy.app.resources.Resources;
import get.wordy.app.ui.IReloadable;
import get.wordy.app.ui.UISettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SwitchButton extends JToggleButton implements IReloadable {

    private Resources resources;
    private UISettings settings;
    private String[] key;

    public SwitchButton(Action action, Resources resources, UISettings settings, String... key) {
        super(action);
        this.resources = resources;
        this.settings = settings;
        this.key = key;
        customize();
    }

    private void customize() {
        setText(resources.translate(key[0]));
        setFont(settings.getFont());
        setFocusPainted(false);
        setBorderPainted(false);
        setVerticalTextPosition(AbstractButton.CENTER);
        setHorizontalTextPosition(AbstractButton.LEADING);

        Dimension size = this.getPreferredSize();
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);

        addListeners();
    }

    private void addListeners() {
        SwitchButtonListener l = new SwitchButtonListener();
        addItemListener(l);
        addKeyListener(l);
    }

    @Override
    public void reloadText() {
        if (!getModel().isSelected())
            setText(resources.translate(key[0]));
        else
            setText(resources.translate(key[1]));
    }

    @Override
    public void reloadFont() {
        setFont(settings.getFont());
    }

    private class SwitchButtonListener extends KeyAdapter implements ItemListener {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_ENTER) {
                Toolkit.getDefaultToolkit().beep();
                doClick();
            }
        }

        @Override
        public void itemStateChanged(ItemEvent itemEvent) {
            int state = itemEvent.getStateChange();
            if (state == ItemEvent.SELECTED) {
            } else {
            }
            reloadText();
        }
    }

}