package get.wordy.app.ui.component;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import get.wordy.app.resources.Resources;
import get.wordy.app.ui.IReloadable;
import get.wordy.app.ui.UISettings;

public class ExampleTextArea extends JTextPane implements IReloadable {

    private Resources resources;
    private UISettings settings;
    private boolean permission;

    private ScrollPanel exampleScrollPane;

    public ExampleTextArea(Resources resources, UISettings settings, boolean permission) {
        this.resources = resources;
        this.settings = settings;
        this.permission = permission;
        exampleScrollPane = new ScrollPanel(this);
        customize();
    }

    private void customize() {
        setContentType("text/plain; charset=UTF-8");
        setEditable(permission);
        setFocusable(permission);
        setBorder(BorderFactory.createEmptyBorder(1, 2, 1, 1));
        setFont(settings.getFont());
    }

    public JScrollPane getScrolledArea() {
        return exampleScrollPane;
    }

    @Override
    public void reloadText() {
        // TODO Auto-generated method stub
    }

    @Override
    public void reloadFont() {
        setFont(settings.getFont());
    }

}
