package get.wordy.app.ui.component;

import get.wordy.app.resources.Resources;
import get.wordy.app.ui.IReloadable;
import get.wordy.app.ui.UISettings;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;

public class Label extends JPanel implements IReloadable {

    private Resources resources;
    private UISettings settings;
    private String key;
    private Object[] args;

    private JLabel label;

    public Label(Resources resources, UISettings settings, String key) {
        this.resources = resources;
        this.settings = settings;
        this.key = key;

        label = new JLabel(resources.translate(key));
        customize();
    }

    public Label(Resources resources, UISettings settings, String key, Object... arguments) {
        this.resources = resources;
        this.settings = settings;
        this.key = key;
        this.args = arguments;

        String text = resources.translate(key);
        label = new JLabel(MessageFormat.format(text, arguments));
        customize();
    }

    private void customize() {
        label.setFont(settings.getFont());
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        add(label, BorderLayout.WEST);
    }

    @Override
    public void reloadText() {
        String text = resources.translate(key);
        if (args != null || args.length != 0) {
            text = MessageFormat.format(text, args);
        }
        label.setText(text);
    }

    @Override
    public void reloadFont() {
        label.setFont(settings.getFont());
    }

    /*
     * Customized method for SplitCustomPanel
     */
    public void reloadText(String text) {
        label.setText(text);
    }

}