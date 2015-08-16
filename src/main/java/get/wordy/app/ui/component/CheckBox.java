package get.wordy.app.ui.component;

import javax.swing.JCheckBox;

import get.wordy.app.resources.Resources;
import get.wordy.app.ui.IReloadable;
import get.wordy.app.ui.UISettings;

public class CheckBox extends JCheckBox implements IReloadable {

    private Resources resources;
    private UISettings settings;
    private String key;

    public CheckBox(Resources resources, UISettings settings, String key) {
        super(resources.translate(key));
        this.resources = resources;
        this.settings = settings;
        this.key = key;
        customize();
    }

    private void customize() {
        setFont(settings.getFont());
        setFocusPainted(false);
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
