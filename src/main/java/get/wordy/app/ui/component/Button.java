package get.wordy.app.ui.component;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;

import get.wordy.app.resources.Resources;
import get.wordy.app.ui.IReloadable;
import get.wordy.app.ui.UISettings;

public class Button extends JButton implements IReloadable {

    private Resources resources;
    private UISettings settings;
    private String key;

    public Button(Action action, Resources resources, UISettings settings, String key) {
        super(action);
        this.resources = resources;
        this.settings = settings;
        this.key = key;
        customize();
    }

    private void customize() {
        setText(resources.translate(key));
        setFont(settings.getFont());
        setFocusPainted(false);
        setBorderPainted(false);
        setVerticalTextPosition(AbstractButton.CENTER);
        setHorizontalTextPosition(AbstractButton.LEADING);
    }

    @Override
    public void reloadText() {
        setText(resources.translate(key));
    }

    @Override
    public void reloadFont() {
        setFont(settings.getFont());
    }

}