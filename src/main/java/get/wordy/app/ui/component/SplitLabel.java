package get.wordy.app.ui.component;

import get.wordy.app.resources.Resources;
import get.wordy.app.ui.IReloadable;
import get.wordy.app.ui.UISettings;

import javax.swing.*;

public class SplitLabel extends JPanel implements IReloadable {

    private Resources resources;
    private String key;
    private int parts;

    private Label[] panels;

    public SplitLabel(Resources resources, UISettings settings, String key, int parts) {
        this.resources = resources;
        this.key = key;
        this.parts = parts;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        panels = new Label[this.parts];
        String text = resources.translate(key);
        String arr[] = text.split("\\|");
        for (int i = 0; i < this.parts; i++) {
            panels[i] = new Label(resources, settings, arr[i].trim());
            add(panels[i]);
        }
    }

    @Override
    public void reloadText() {
        String text = resources.translate(key);
        String arr[] = text.split(";");
        for (int i = 0; i < parts; i++) {
            panels[i].reloadText(arr[i]);
        }
    }

    @Override
    public void reloadFont() {
        for (Label label : panels)
            label.reloadFont();
    }

}
